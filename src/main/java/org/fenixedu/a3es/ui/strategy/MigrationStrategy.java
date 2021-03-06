package org.fenixedu.a3es.ui.strategy;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.fenixedu.a3es.domain.CurricularContext;
import org.fenixedu.a3es.domain.DegreeFile;
import org.fenixedu.a3es.domain.TeacherActivity;
import org.fenixedu.a3es.domain.TeacherFile;
import org.fenixedu.a3es.domain.util.ExportDegreeProcessBean;
import org.fenixedu.bennu.A3esSpringConfiguration;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.context.MessageSource;

import com.google.common.base.Strings;

public class MigrationStrategy {

    protected static final int TEACHER_SERVICE_ITEMS = 10;

    private static final Locale PT = new Locale("pt");

    private static final Locale UK = Locale.UK;

    private static final LocalizedString NO_GROUP = new LocalizedString(PT, "Sem Grupo").with(UK, "No group");

    private static final String API_PROCESS = "api_process";

    private static final String API_FORM = "api_form";

    private static final String API_FOLDER = "api_folder";

    private static final String API_ANNEX = "api_annex";

    protected String base64Hash;

    protected String formId;

    private MessageSource messageSource;
    
    protected String competenceCoursesFolderIndex = "6.2.1.";
    
    protected String degreeStudyPlanFolderIndex = "4.3.";

    public enum AccreditationType {

        PROGRAM_ACCREDITATION, NEW_PROGRAM_ACCREDITATION, ACCREDITATION_RENEWAL, INSTITUTIONAL_EVALUATION;

        @Override
        public String toString() {
            return getLocalizedName().getContent();
        }

        public LocalizedString getLocalizedName() {
            return BundleUtil.getLocalizedString(A3esSpringConfiguration.BUNDLE, this.getClass().getName() + "." + name());
        }
    }

    public static MigrationStrategy getStrategy(ExportDegreeProcessBean form) {
        if (form.getAccreditationType().equals(AccreditationType.INSTITUTIONAL_EVALUATION)) {
            return new InstitutionalMigrationStrategy();
        } else if (form.getAccreditationType().equals(AccreditationType.ACCREDITATION_RENEWAL)) {
            return new AccreditationRenewalMigrationStrategy();
        } else if (form.getAccreditationType().equals(AccreditationType.NEW_PROGRAM_ACCREDITATION)) {
            return new NewProgramAccreditationMigrationStrategy();
        }
        return new MigrationStrategy();
    }

    public List<String> exportCurricularUnitFilesToA3es(ExportDegreeProcessBean form, MessageSource messageSource) {
        this.messageSource = messageSource;
        initialize(form);
        List<String> output = uploadCompetenceCourses(form);
        return output;
    }

    public List<String> exportTeacherUnitFilesToA3es(ExportDegreeProcessBean form, MessageSource messageSource) {
        this.messageSource = messageSource;
        initialize(form);
        List<String> output = uploadTeacherCurriculum(form);
        return output;
    }

    public List<String> exportDegreeStudyPlanToA3es(ExportDegreeProcessBean form, MessageSource messageSource) {
        this.messageSource = messageSource;
        initialize(form);
        List<String> output = uploadDegreeStudyPlan(form);
        return output;
    }

    private void initialize(ExportDegreeProcessBean form) {
        initializeHash(form);
        initializeFormId(form);
    }

    protected void initializeHash(ExportDegreeProcessBean form) {
        base64Hash = form.getBase64Hash();
        if (Strings.isNullOrEmpty(form.getBase64Hash())) {
            base64Hash = new String(Base64.getEncoder().encode((form.getUser() + ":" + form.getPassword()).getBytes()));
            form.setBase64Hash(base64Hash);
        }
    }

    protected void initializeFormId(ExportDegreeProcessBean form) {
        formId = form.getFormId();
        if (Strings.isNullOrEmpty(form.getFormId())) {
            JSONArray processes = null;
            try {
                processes = invokeToArray(webResource().path(API_PROCESS));
            } catch (javax.ws.rs.NotAuthorizedException e) {
                throw new RuntimeException(messageSource.getMessage("error.not.authorized", null, I18N.getLocale()));
            }
            JSONObject json = (JSONObject) processes.iterator().next();
            String id = (String) json.get("id");
            String name = (String) json.get("name");
            String degreeCode = form.getDegreeFile().getDegreeCode();
            if (!Strings.isNullOrEmpty(degreeCode) && degreeCode.trim().equalsIgnoreCase(name)) {
                JSONArray forms = invokeToArray(webResource().path(API_FORM).queryParam("processId", id));
                for (Object object : forms) {
                    JSONObject jsonForm = (JSONObject) object;
                    if (form.getProcessFolderName().equals(jsonForm.get("name"))) {
                        formId = (String) jsonForm.get("id");
                        form.setFormId(formId);
                        return;
                    }
                }
                throw new RuntimeException(message("error.process.without.evaluation.form", name));
            } else {
                throw new RuntimeException(message("error.invalid.degree.code", degreeCode));
            }
        }
    }

    public String getProcessFolderName() {
        return "Guião para a auto-avaliação";
    }

    protected WebTarget webResource() {
        Client client = ClientBuilder.newClient();
        return client.target(A3esSpringConfiguration.getConfiguration().a3esURL());
    }

    protected JSONObject invoke(WebTarget resource) {
        return (JSONObject) JSONValue.parse(resource.request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + base64Hash).get(String.class));
    }

    protected JSONArray invokeToArray(WebTarget resource) {
        return (JSONArray) ((JSONObject) JSONValue.parse(resource.request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + base64Hash).get(String.class))).get("list");
    }

    protected Response post(WebTarget resource, String arg) {
        return resource.request(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + base64Hash)
                .buildPost(Entity.text(arg)).invoke();
    }

    protected Response delete(WebTarget resource) {
        return resource.request(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + base64Hash).buildDelete().invoke();
    }

    protected List<String> uploadCompetenceCourses(ExportDegreeProcessBean form) {
        competenceCoursesFolderIndex = form.getCompetenceCoursesFolderIndex();
        List<String> output = new ArrayList<String>();
        for (Object object : invokeToArray(webResource().path(API_FOLDER).queryParam("formId", formId))) {
            JSONObject folder = (JSONObject) object;
            if (form.getCompetenceCoursesFolderName().equals(folder.get("name"))) {
                String competencesId = (String) folder.get(getCompetenceCourseId());
                for (Object annexObj : invokeToArray(webResource().path(API_ANNEX).queryParam("formId", formId)
                        .queryParam("folderId", competencesId))) {
                    JSONObject annex = (JSONObject) annexObj;
                    delete(webResource().path(API_ANNEX).path((String) annex.get("id")).queryParam("formId", formId)
                            .queryParam("folderId", competencesId));
                }
                for (Entry<JSONObject, String> json : buildCompetenceCoursesJson(form.getDegreeFile()).entrySet()) {
                    Response response =
                            post(webResource().path(API_ANNEX).queryParam("formId", formId).queryParam("folderId", competencesId),
                                    json.getKey().toJSONString());
                    int status = response.getStatus();

                    if (status == 201) {
                        output.add(status + ": "
                                + ((JSONObject) json.getKey().get(getCompetenceCoursesFieldKey("1.1"))).get("pt") + ": "
                                + json.getValue());
                    } else {
                        output.add(status + ": "
                                + ((JSONObject) json.getKey().get(getCompetenceCoursesFieldKey("1.1"))).get("pt") + ": "
                                + response.getEntity() + " input: " + json.getKey().toJSONString());
                    }
                }
                break;
            }
        }
        return output;
    }

    public String getCompetenceCoursesFolderIndex() {
        return competenceCoursesFolderIndex;
    }

    public String getCompetenceCoursesFolderName() {
        return "6.2.1. Ficha das unidades curriculares";
    }

    protected String getCompetenceCourseId() {
        return "id";
    }

    protected String getCompetenceCoursesFieldKey(String keyIndex) {
        return "q-" + getCompetenceCoursesFolderIndex() + keyIndex;
    }
    
    protected String getCompetenceCoursesFieldKeyII(String keyIndex) {
        return "q-II." + getCompetenceCoursesFolderIndex() + keyIndex;
    }
    protected void setCompetenceCoursesField(JSONObject json, String keyIndex, Object value) {
        json.put(getCompetenceCoursesFieldKey(keyIndex), value);
        json.put(getCompetenceCoursesFieldKeyII(keyIndex), value);
    }

    protected Map<JSONObject, String> buildCompetenceCoursesJson(DegreeFile degreeFile) {
        Map<JSONObject, String> jsons = new HashMap<JSONObject, String>();

        degreeFile.getCurricularUnitFileSet().forEach(
                curricularUnitFile -> {

                    JSONObject json = new JSONObject();
                    StringBuilder output = new StringBuilder();
                    String ukLanguage = " (" + UK.getDisplayLanguage() + ")";
                    String ptLanguage = " (" + PT.getDisplayLanguage() + ")";

                    setCompetenceCoursesField(json, "1.1", curricularUnitFile.getFileName());
                    
                    JSONObject q62111 = new JSONObject();
                    q62111.put(
                            "en",
                            cut(message("label.curricularUnitName") + ukLanguage, curricularUnitFile.getCurricularUnitName()
                                    .getContent(UK), output, 1000));
                    q62111.put(
                            "pt",
                            cut(message("label.curricularUnitName") + ptLanguage, curricularUnitFile.getCurricularUnitName()
                                    .getContent(PT), output, 1000));
                    setCompetenceCoursesField(json,"1.1", q62111);

                    setCompetenceCoursesField(json,"1.2",  cut(message("label.scientificAreaAcronym"),
                            curricularUnitFile.getScientificArea(), output, 100));
                    setCompetenceCoursesField(json,"1.3", cut(message("label.courseRegime"),
                            curricularUnitFile.getCourseRegime(), output, 100));
                    setCompetenceCoursesField(json,"1.4", cut(message("label.workingHours"),
                            curricularUnitFile.getTotalLoad(), output, 100));
                    setCompetenceCoursesField(json,"1.5", cut(message("label.contactHours"),
                            curricularUnitFile.getContactHours(), output, 100));
                    setCompetenceCoursesField(json,"1.6", cut(message("label.ects"),
                            curricularUnitFile.getEcts(), output, 100));

                    JSONObject q62117 = new JSONObject();
                    q62117.put(
                            "en",
                            cut(message("label.observations") + ukLanguage, curricularUnitFile.getObservations().getContent(UK),
                                    output, 1000));
                    q62117.put(
                            "pt",
                            cut(message("label.observations") + ptLanguage, curricularUnitFile.getObservations().getContent(PT),
                                    output, 1000));
                    setCompetenceCoursesField(json,"1.7", q62117);

                    setCompetenceCoursesField(json,"2",
                            cut(message("label.responsibleTeacherAndTeachingHours"),
                                    curricularUnitFile.getResponsibleTeacherAndTeachingHours(), output, 100));
                    setCompetenceCoursesField(json,"3", curricularUnitFile.getOtherTeachersAndTeachingHours());

                    JSONObject q6214 = new JSONObject();
                    q6214.put(
                            "en",
                            cut(message("label.learningOutcomes") + ukLanguage, curricularUnitFile.getLearningOutcomes()
                                    .getContent(UK), output, 1000));
                    q6214.put(
                            "pt",
                            cut(message("label.learningOutcomes") + ptLanguage, curricularUnitFile.getLearningOutcomes()
                                    .getContent(PT), output, 1000));
                    setCompetenceCoursesField(json,"4", q6214);

                    JSONObject q6215 = new JSONObject();
                    q6215.put(
                            "en",
                            cut(message("label.syllabus") + ukLanguage, curricularUnitFile.getSyllabus().getContent(UK), output,
                                    1000));
                    q6215.put(
                            "pt",
                            cut(message("label.syllabus") + ptLanguage, curricularUnitFile.getSyllabus().getContent(PT), output,
                                    1000));
                    setCompetenceCoursesField(json,"5", q6215);

                    JSONObject q6216 = new JSONObject();
                    q6216.put(
                            "en",
                            cut(message("label.syllabusDemonstration") + ukLanguage, curricularUnitFile
                                    .getSyllabusDemonstration().getContent(UK), output, 1000));
                    q6216.put(
                            "pt",
                            cut(message("label.syllabusDemonstration") + ptLanguage, curricularUnitFile
                                    .getSyllabusDemonstration().getContent(PT), output, 1000));
                    setCompetenceCoursesField(json,"6", q6216);

                    JSONObject q6217 = new JSONObject();
                    q6217.put(
                            "en",
                            cut(message("label.teachingMethodologies") + ukLanguage, curricularUnitFile
                                    .getTeachingMethodologies().getContent(UK), output, 1000));
                    q6217.put(
                            "pt",
                            cut(message("label.teachingMethodologies") + ptLanguage, curricularUnitFile
                                    .getTeachingMethodologies().getContent(PT), output, 1000));
                    setCompetenceCoursesField(json,"7", q6217);

                    JSONObject q6218 = new JSONObject();
                    q6218.put(
                            "en",
                            cut(message("label.teachingMethodologiesDemonstration") + ukLanguage, curricularUnitFile
                                    .getTeachingMethodologiesDemonstration().getContent(UK), output, 3000));
                    q6218.put(
                            "pt",
                            cut(message("label.teachingMethodologiesDemonstration") + ptLanguage, curricularUnitFile
                                    .getTeachingMethodologiesDemonstration().getContent(PT), output, 3000));
                    setCompetenceCoursesField(json,"8", q6218);

                            setCompetenceCoursesField(json, "9",
                                    cut(message("label.bibliographicReferences"), curricularUnitFile.getBibliographicReferences(), output, 1000));
                    jsons.put(json, output.toString());
                });
        return jsons;
    }

    protected List<String> uploadTeacherCurriculum(ExportDegreeProcessBean form) {
        List<String> output = new ArrayList<String>();
        for (Object object : getTeacherCurriculumFolders()) {
            JSONObject folder = (JSONObject) object;
            if (isTeacherCurriculumnFolder(form.getTeacherCurriculumnFolderName(), folder)) {
                String teacherCurriculumId = (String) folder.get(getTeacherCurriculumId());
                for (Object annexObj : invokeToArray(webResource().path(API_ANNEX).queryParam("formId", formId)
                        .queryParam("folderId", teacherCurriculumId))) {
                    JSONObject annex = (JSONObject) annexObj;
                    delete(webResource().path(API_ANNEX).path((String) annex.get("id")).queryParam("formId", formId)
                            .queryParam("folderId", teacherCurriculumId));
                }
                for (Entry<JSONObject, String> json : buildTeacherCurriculumJson(form.getDegreeFile()).entrySet()) {
                    Response response =
                            post(webResource().path(API_ANNEX).queryParam("formId", formId)
                                    .queryParam("folderId", teacherCurriculumId), json.getKey().toJSONString());
                    int status = response.getStatus();
                    if (status == 201) {
                        output.add("201 Created: " + json.getKey().get("q-cf-name") + ": " + json.getValue());
                    } else {
                        output.add(status + ": " + json.getKey().get("q-cf-name") + ": " + response.getEntity() + " input: "
                                + json.getKey().toJSONString());
                    }
                }
                break;
            }
        }
        return output;
    }

    public String getTeacherCurriculumnFolderName() {
        return "3.2. Fichas curriculares dos docentes do ciclo de estudos";
    }

    protected String getTeacherCurriculumId() {
        return "id";
    }

    protected boolean isTeacherCurriculumnFolder(String teacherCurriculumnFolderName, JSONObject folder) {
        return teacherCurriculumnFolderName.equals(folder.get("name"));
    }

    protected JSONArray getTeacherCurriculumFolders() {
        return invokeToArray(webResource().path(API_FOLDER).queryParam("formId", formId));
    }

    protected Map<JSONObject, String> buildTeacherCurriculumJson(DegreeFile degreeFile) {
        Map<JSONObject, String> jsons = new HashMap<JSONObject, String>();

        degreeFile.getTeacherFileSet()
                .forEach(
                        teacherFile -> {
                            JSONObject toplevel = new JSONObject();
                            StringBuilder output = new StringBuilder();

                            toplevel.put("q-cf-name", teacherFile.getFileName());
                            toplevel.put("q-new-cf-name", teacherFile.getFileName());
                            toplevel.put("q-cf-ies", teacherFile.getInstitution());
                            toplevel.put("q-new-cf-ies", teacherFile.getInstitution());
                            toplevel.put("q-cf-uo", teacherFile.getOrganicUnit());
                            toplevel.put("q-new-cf-uo", teacherFile.getOrganicUnit());
                            if (teacherFile.getA3esTeacherCategory() != null) {
                                toplevel.put(
                                        "q-cf-cat",
                                        teacherFile.getA3esTeacherCategory().getName().getContent()
                                                .replaceAll(" ou equivalente", ""));
                                toplevel.put(
                                        "q-new-cf-cat",
                                        teacherFile.getA3esTeacherCategory().getName().getContent()
                                                .replaceAll(" ou equivalente", ""));
                            } else {
                                output.append(message("message.fieldName.empty", "q-cf-cat"));
                            }
                            if (teacherFile.getRegime() != null) {
                                toplevel.put("q-cf-time", Float.valueOf(teacherFile.getRegime()));
                                toplevel.put("q-new-cf-time", Float.valueOf(teacherFile.getRegime()));
                            } else {
                                output.append(message("message.fieldName.empty", "q-cf-time"));
                            }

                            JSONObject file = new JSONObject();
                            {
                                file.put("name", cut("nome", teacherFile.getTeacherName(), output, 200));
                                file.put("ies", cut("ies", teacherFile.getInstitution(), output, 200));
                                file.put("uo", cut("uo", teacherFile.getOrganicUnit(), output, 200));
                                file.put("research_center", cut("research_center", teacherFile.getResearchUnit(), output, 200));

                                if (teacherFile.getA3esTeacherCategory() != null) {
                                    file.put(
                                            "cat",
                                            teacherFile.getA3esTeacherCategory().getName().getContent()
                                                    .replaceAll(" ou equivalente", ""));
                                } else {
                                    output.append(message("message.fieldName.empty", "cat"));
                                }
                                if (teacherFile.getA3esDegreeType() != null) {
                                    file.put("deg", teacherFile.getA3esDegreeType().toString());
                                } else {
                                    output.append(message("message.fieldName.empty", "deg"));
                                }

                                if (teacherFile.getDegreeScientificArea() != null) {
                                    file.put("degarea",
                                            cut("area cientifica", teacherFile.getDegreeScientificArea(), output, 200));
                                } else {
                                    output.append(message("message.fieldName.empty", "degarea"));
                                }
                                if (teacherFile.getDegreeYear() != null) {
                                    file.put("ano_grau", teacherFile.getDegreeYear());
                                } else {
                                    output.append(message("message.fieldName.empty", "ano_grau"));
                                }
                                if (teacherFile.getDegreeInstitution() != null) {
                                    file.put("instituicao_conferente",
                                            cut("instituicao_conferente", teacherFile.getDegreeInstitution(), output, 200));
                                } else {
                                    output.append(message("message.fieldName.empty", "instituicao_conferente"));
                                }

                                if (teacherFile.getRegime() != null) {
                                    file.put("regime", Float.valueOf(teacherFile.getRegime()));
                                } else {
                                    output.append(message("message.fieldName.empty", "regime"));
                                }
                                JSONArray academicArray = new JSONArray();

                                teacherFile.getA3esQualifications().forEach(
                                        qualification -> {

                                            JSONObject academic = new JSONObject();

                                            if (qualification.getYear() != null) {
                                                academic.put("year", qualification.getYear());
                                            } else {
                                                output.append(message("message.fieldName.empty", "year"));
                                            }
                                            if (qualification.getDegree() != null) {
                                                academic.put("degree", cut("degree", qualification.getDegree(), output, 30));
                                            } else {
                                                output.append(message("message.fieldName.empty", "degree"));
                                            }
                                            if (qualification.getArea() != null) {
                                                academic.put("area", cut("area", qualification.getArea(), output, 100));
                                            } else {
                                                output.append(message("message.fieldName.empty", "area"));
                                            }
                                            if (qualification.getInstitution() != null) {
                                                academic.put("ies", cut("ies", qualification.getInstitution(), output, 100));
                                            } else {
                                                output.append(message("message.fieldName.empty", "ies"));
                                            }
                                            if (qualification.getClassification() != null) {
                                                academic.put("rank",
                                                        cut("classificação", qualification.getClassification(), output, 30));
                                            } else {
                                                output.append(message("message.fieldName.empty", "rank"));
                                            }
                                            academicArray.add(academic);
                                        });
                                file.put("form-academic", academicArray);

                                file.put(
                                        "form-investigation",
                                        getJsonActivities(teacherFile.getScientificActivitySet(), "investigation",
                                                "investigation", 500, output));
                                file.put(
                                        "form-highlevelactivities",
                                        getJsonActivities(teacherFile.getDevelopmentActivitySet(), "highlevelactivities",
                                                "actividade", 200, output));
                                file.put(
                                        "form-otherpublications",
                                        getJsonActivities(teacherFile.getOtherPublicationActivitySet(), "otherpublications",
                                                "outras publicações", 500, output));
                                file.put(
                                        "form-professional",
                                        getJsonActivities(teacherFile.getOtherProfessionalActivitySet(), "profession",
                                                "profession", 200, output));

                                setTeachingService(degreeFile, teacherFile, output, file);

                            }
                            toplevel.put("q-cf-cfile", file);
                            toplevel.put("q-new-cf-cfile", file);

                            jsons.put(toplevel, output.toString());
                        });

        return jsons;
    }

    protected void setTeachingService(DegreeFile degreeFile, TeacherFile teacherFile, StringBuilder output, JSONObject file) {
        JSONArray insideLectures = new JSONArray();
        teacherFile.getA3esTeachingService().forEach(teachingService -> {
            JSONObject lecture = new JSONObject();
            lecture.put("curricularUnit", cut("curricularUnit", teachingService.getCurricularUnitName(), output, 100));
            lecture.put("studyCycle", cut("studyCycle", teachingService.getStudyCycles(), output, 200));
            lecture.put("type", cut("type", teachingService.getMethodologyTypes(), output, 30));
            lecture.put("hoursPerWeek", teachingService.getTotalHours());
            insideLectures.add(lecture);
        });
        file.put("form-unit", insideLectures.subList(0, Math.min(TEACHER_SERVICE_ITEMS, insideLectures.size())));
    }

    private JSONArray getJsonActivities(Set<? extends TeacherActivity> teacherActivities, String jsonObject, String outputLabel,
            int size, StringBuilder output) {
        final JSONArray researchArray = new JSONArray();
        teacherActivities.forEach(activity -> {
            JSONObject research = new JSONObject();
            research.put(jsonObject, cut(outputLabel, activity.getActivity(), output, size));
            researchArray.add(research);
        });
        return researchArray;
    }

    private List<String> uploadDegreeStudyPlan(ExportDegreeProcessBean form) {
        degreeStudyPlanFolderIndex = form.getDegreeStudyPlanFolderIndex();
        List<String> output = new ArrayList<String>();
        for (Object object : invokeToArray(webResource().path(API_FOLDER).queryParam("formId", formId))) {
            JSONObject folder = (JSONObject) object;
            if (form.getDegreeStudyPlanFolderName().equals(folder.get("name"))) {
                String competencesId = (String) folder.get("id");
                for (Object annexObj : invokeToArray(webResource().path(API_ANNEX).queryParam("formId", formId)
                        .queryParam("folderId", competencesId))) {
                    JSONObject annex = (JSONObject) annexObj;
                    delete(webResource().path(API_ANNEX).path((String) annex.get("id")).queryParam("formId", formId)
                            .queryParam("folderId", competencesId));
                }
                for (Entry<JSONObject, String> json : buildDegreeStudyPlanJson(form.getDegreeFile()).entrySet()) {
                    Response response =
                            post(webResource().path(API_ANNEX).queryParam("formId", formId).queryParam("folderId", competencesId),
                                    json.getKey().toJSONString());
                    int status = response.getStatus();
                    if (status == 201) {
                        output.add("201 Created: " + ((JSONObject) json.getKey().get(getDegreeStudyPlanFieldKey("1"))).get("pt")
                                + ": " + json.getValue());
                    } else {
                        output.add(status + ": " + ((JSONObject) json.getKey().get(getDegreeStudyPlanFieldKey("1"))).get("pt")
                                + ": " + response.getEntity() + " input: " + json.getKey().toJSONString());
                    }
                };
            }
        }

        return output;
    }

    private Map<JSONObject, String> buildDegreeStudyPlanJson(DegreeFile degreeFile) {
        String ukLanguage = " (" + UK.getDisplayLanguage() + ")";
        String ptLanguage = " (" + PT.getDisplayLanguage() + ")";
        
        Set<CurricularContext> curricularContextSet =
                degreeFile.getCurricularUnitFileSet().stream().flatMap(cuf -> cuf.getCurricularContextSet().stream()).collect(Collectors.toSet());
        Map<JSONObject, String> result = new HashMap<JSONObject, String>();

        for (CurricularContext curricularContext : curricularContextSet) {
            StringBuilder output = new StringBuilder();
            JSONObject json = new JSONObject();
            JSONObject q431 = new JSONObject();
            q431.put("en",
                    cutBegining(getDegreeStudyPlanFieldKey("1") + ukLanguage, curricularContext.getCurricularGroup().getContent(UK), output, 100));
            q431.put("pt",
                    cutBegining(getDegreeStudyPlanFieldKey("1") + ptLanguage, curricularContext.getCurricularGroup().getContent(PT), output, 100));
            setDegreeStudyPlanField(json, "1", q431);
            json.put(getDegreeStudyPlanFieldKey("2"),
                    cut(getDegreeStudyPlanFieldKey("2"), curricularContext.getCurricularPeriod().getContent(PT), output, 100));

            JSONObject q432II = new JSONObject();
            q432II.put("en",
                    cutBegining(getDegreeStudyPlanFieldKeyII("2") + ukLanguage, curricularContext.getCurricularPeriod().getContent(UK), output, 100));
            q432II.put("pt",
                    cutBegining(getDegreeStudyPlanFieldKeyII("2") + ptLanguage, curricularContext.getCurricularPeriod().getContent(PT), output, 100));
            json.put(getDegreeStudyPlanFieldKeyII("2"), q432II);

            JSONArray curricularUnitsArray = new JSONArray();
            curricularContext.getCurricularUnitFileSet().stream().filter(cuf -> cuf.getDegreeFile().equals(degreeFile)).forEach(cf -> {
                JSONObject curricularUnit = new JSONObject();
                curricularUnit.put("curricularUnit", cf.getCurricularUnitName().getContent(PT) + " / " + cf.getCurricularUnitName().getContent(UK));
                curricularUnit.put("scientificArea", cf.getScientificArea());
                curricularUnit.put("type", cf.getCourseRegime());
                curricularUnit.put("totalWorkingHours", cf.getTotalLoad());
                curricularUnit.put("totalContactHours", cf.getCourseLoadPerType());
                curricularUnit.put("credits", cf.getEcts());
                curricularUnit.put("ects", cf.getEcts());
                curricularUnit.put("observations", cf.getObservations().getContent());
                curricularUnitsArray.add(curricularUnit);
            });
            json.put("grid", curricularUnitsArray);
            result.put(json, output.toString());
        }
        return result;
    }


    public String getDegreeStudyPlanFolderName() {
        return "4.3 Plano de estudos";
    }

    public String getDegreeStudyPlanFolderIndex() {
        return degreeStudyPlanFolderIndex;
    }

    protected String getDegreeStudyPlanFieldKey(String keyIndex) {
        return "q-" + getDegreeStudyPlanFolderIndex() + keyIndex;
    }
    
    protected String getDegreeStudyPlanFieldKeyII(String keyIndex) {
        return "q-II." + getDegreeStudyPlanFolderIndex() + keyIndex;
    }
    protected void setDegreeStudyPlanField(JSONObject json, String keyIndex, Object value) {
        json.put(getDegreeStudyPlanFieldKey(keyIndex), value);
        json.put(getDegreeStudyPlanFieldKeyII(keyIndex), value);
    }

    protected String cut(String field, String originalContent, StringBuilder output, int size) {
        if (originalContent == null) {
            output.append(message("message.fieldName.empty", field));
            return originalContent;
        }
        String content = originalContent.replace("\r\n", "\n");
        if (content.length() > size) {
            output.append(message("message.field.cutted", field, size));
            return content.substring(0, size - 4) + " ...";
        }
        return content;
    }

    protected String cutBegining(String field, String originalContent, StringBuilder output, int size) {
        if (originalContent == null) {
            output.append(message("message.fieldName.empty", field));
            return originalContent;
        }
        String content = originalContent.replace("\r\n", "\n");
        if (content.length() > size) {
            output.append(message("message.field.cutted", field, size));
            return "..." + content.substring((content.length() - size) + 3, content.length());
        }
        return content;
    }

    private String message(String code, Object... args) {
        return messageSource.getMessage(code, args, I18N.getLocale());
    }
    
    private String message(String code, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }

}
