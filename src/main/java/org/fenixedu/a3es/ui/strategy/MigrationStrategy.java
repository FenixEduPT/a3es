package org.fenixedu.a3es.ui.strategy;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.fenixedu.a3es.domain.DegreeFile;
import org.fenixedu.a3es.domain.TeacherActivity;
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

    private static final Locale PT = new Locale("pt");

    private static final Locale UK = Locale.UK;

    private static final String API_PROCESS = "api_process";

    private static final String API_FORM = "api_form";

    private static final String API_FOLDER = "api_folder";

    private static final String API_ANNEX = "api_annex";

    protected String base64Hash;

    protected String formId;

    private MessageSource messageSource;

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
        }
        return new MigrationStrategy();
    }

    public List<String> exportCurricularUnitFilesToA3es(ExportDegreeProcessBean form, MessageSource messageSource) {
        this.messageSource = messageSource;
        initialize(form);
        List<String> output = uploadCompetenceCourses(form.getDegreeFile());
        return output;
    }

    public List<String> exportTeacherUnitFilesToA3es(ExportDegreeProcessBean form, MessageSource messageSource) {
        this.messageSource = messageSource;
        initialize(form);
        List<String> output = uploadTeacherCurriculum(form.getDegreeFile());
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
            if (!Strings.isNullOrEmpty(form.getDegreeFile().getDegreeCode())
                    && form.getDegreeFile().getDegreeCode().equalsIgnoreCase(name)) {

                JSONArray forms = invokeToArray(webResource().path(API_FORM).queryParam("processId", id));
                for (Object object : forms) {
                    JSONObject jsonForm = (JSONObject) object;
                    if (getProcessFolderName().equals(jsonForm.get("name"))) {
                        formId = (String) jsonForm.get("id");
                        form.setFormId(formId);
                        return;
                    }
                }
                throw new RuntimeException(message("error.process.without.evaluation.form", name));
            } else {
                throw new RuntimeException(message("error.invalid.degree.code", form.getDegreeFile().getDegreeCode()));
            }
        }
    }

    protected String getProcessFolderName() {
        return "Guião para a auto-avaliação";
    }

    protected WebTarget webResource() {
        Client client = ClientBuilder.newClient();
        return client.target(A3esSpringConfiguration.getConfiguration().a3esURL());
    }

    protected JSONObject invoke(WebTarget resource) {
        return (JSONObject) JSONValue.parse(
                resource.request(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + base64Hash).get(String.class));
    }

    protected JSONArray invokeToArray(WebTarget resource) {
        return (JSONArray) ((JSONObject) JSONValue.parse(
                resource.request(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + base64Hash).get(String.class)))
                        .get("list");
    }

    protected Response post(WebTarget resource, String arg) {
        return resource.request(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + base64Hash)
                .buildPost(Entity.text(arg)).invoke();
    }

    protected Response delete(WebTarget resource) {
        return resource.request(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + base64Hash).buildDelete().invoke();
    }

    protected List<String> uploadCompetenceCourses(DegreeFile degreeFile) {
        List<String> output = new ArrayList<String>();
        for (Object object : invokeToArray(webResource().path(API_FOLDER).queryParam("formId", formId))) {
            JSONObject folder = (JSONObject) object;
            if (getCompetenceCoursesFolderName().equals(folder.get("name"))) {
                String competencesId = (String) folder.get(getCompetenceCourseId());
                for (Object annexObj : invokeToArray(
                        webResource().path(API_ANNEX).queryParam("formId", formId).queryParam("folderId", competencesId))) {
                    JSONObject annex = (JSONObject) annexObj;
                    delete(webResource().path(API_ANNEX).path((String) annex.get("id")).queryParam("formId", formId)
                            .queryParam("folderId", competencesId));
                }
                for (Entry<JSONObject, String> json : buildCompetenceCoursesJson(degreeFile).entrySet()) {
                    Response response =
                            post(webResource().path(API_ANNEX).queryParam("formId", formId).queryParam("folderId", competencesId),
                                    json.getKey().toJSONString());
                    int status = response.getStatus();
                    if (status == 201) {
                        output.add(status + ": " + json.getKey().get(getCompetenceCoursesFieldKey("1")) + ": " + json.getValue());
                    } else {
                        output.add(status + ": " + json.getKey().get(getCompetenceCoursesFieldKey("1")) + ": "
                                + response.getEntity() + " input: " + json.getKey().toJSONString());
                    }
                }
                break;
            }
        }
        return output;
    }

    protected String getCompetenceCoursesFolderIndex() {
        return "6.2.1.";
    }

    protected String getCompetenceCoursesFolderName() {
        return getCompetenceCoursesFolderIndex() + " Ficha das unidades curriculares";
    }

    protected String getCompetenceCourseId() {
        return "id";
    }

    protected String getCompetenceCoursesFieldKey(String keyIndex) {
        return "q-" + getCompetenceCoursesFolderIndex() + keyIndex;
    }

    protected Map<JSONObject, String> buildCompetenceCoursesJson(DegreeFile degreeFile) {
        Map<JSONObject, String> jsons = new HashMap<JSONObject, String>();

        degreeFile.getCurricularUnitFileSet().forEach(curricularUnitFile -> {

            JSONObject json = new JSONObject();
            StringBuilder output = new StringBuilder();

            json.put(getCompetenceCoursesFieldKey("1"), curricularUnitFile.getFileName());
            json.put(getCompetenceCoursesFieldKey("2"), cut(message("label.responsibleTeacherAndTeachingHours"),
                    curricularUnitFile.getResponsibleTeacherAndTeachingHours(), output, 100));
            json.put(getCompetenceCoursesFieldKey("3"), curricularUnitFile.getOtherTeachersAndTeachingHours());

            String ukLanguage = " (" + UK.getDisplayLanguage() + ")";
            String ptLanguage = " (" + PT.getDisplayLanguage() + ")";

            JSONObject q6214 = new JSONObject();
            q6214.put("en", cut(message("label.learningOutcomes") + ukLanguage,
                    curricularUnitFile.getLearningOutcomes().getContent(UK), output, 1000));
            q6214.put("pt", cut(message("label.learningOutcomes") + ptLanguage,
                    curricularUnitFile.getLearningOutcomes().getContent(PT), output, 1000));
            json.put(getCompetenceCoursesFieldKey("4"), q6214);

            JSONObject q6215 = new JSONObject();
            q6215.put("en",
                    cut(message("label.syllabus") + ukLanguage, curricularUnitFile.getSyllabus().getContent(UK), output, 1000));
            q6215.put("pt",
                    cut(message("label.syllabus") + ptLanguage, curricularUnitFile.getSyllabus().getContent(PT), output, 1000));
            json.put(getCompetenceCoursesFieldKey("5"), q6215);

            JSONObject q6216 = new JSONObject();
            q6216.put("en", cut(message("label.syllabusDemonstration") + ukLanguage,
                    curricularUnitFile.getSyllabusDemonstration().getContent(UK), output, 1000));
            q6216.put("pt", cut(message("label.syllabusDemonstration") + ptLanguage,
                    curricularUnitFile.getSyllabusDemonstration().getContent(PT), output, 1000));
            json.put(getCompetenceCoursesFieldKey("6"), q6216);

            JSONObject q6217 = new JSONObject();
            q6217.put("en", cut(message("label.teachingMethodologies") + ukLanguage,
                    curricularUnitFile.getTeachingMethodologies().getContent(UK), output, 1000));
            q6217.put("pt", cut(message("label.teachingMethodologies") + ptLanguage,
                    curricularUnitFile.getTeachingMethodologies().getContent(PT), output, 1000));
            json.put(getCompetenceCoursesFieldKey("7"), q6217);

            JSONObject q6218 = new JSONObject();
            q6218.put("en", cut(message("label.teachingMethodologiesDemonstration") + ukLanguage,
                    curricularUnitFile.getTeachingMethodologiesDemonstration().getContent(UK), output, 3000));
            q6218.put("pt", cut(message("label.teachingMethodologiesDemonstration") + ptLanguage,
                    curricularUnitFile.getTeachingMethodologiesDemonstration().getContent(PT), output, 3000));
            json.put(getCompetenceCoursesFieldKey("8"), q6218);

            json.put(getCompetenceCoursesFieldKey("9"),
                    cut(message("label.bibliographicReferences"), curricularUnitFile.getBibliographicReferences(), output, 1000));
            jsons.put(json, output.toString());
        });
        return jsons;
    }

    protected List<String> uploadTeacherCurriculum(DegreeFile degreeFile) {
        List<String> output = new ArrayList<String>();
        for (Object object : getTeacherCurriculumFolders()) {
            JSONObject folder = (JSONObject) object;
            if (isTeacherCurriculumnFolder(folder)) {
                String teacherCurriculumId = (String) folder.get(getTeacherCurriculumId());
                for (Object annexObj : invokeToArray(
                        webResource().path(API_ANNEX).queryParam("formId", formId).queryParam("folderId", teacherCurriculumId))) {
                    JSONObject annex = (JSONObject) annexObj;
                    delete(webResource().path(API_ANNEX).path((String) annex.get("id")).queryParam("formId", formId)
                            .queryParam("folderId", teacherCurriculumId));
                }
                for (Entry<JSONObject, String> json : buildTeacherCurriculumJson(degreeFile).entrySet()) {
                    Response response = post(webResource().path(API_ANNEX).queryParam("formId", formId).queryParam("folderId",
                            teacherCurriculumId), json.getKey().toJSONString());
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

    protected String getTeacherCurriculumnFolderName() {
        return "4.1.1. Fichas curriculares";
    }

    protected String getTeacherCurriculumId() {
        return "id";
    }

    protected boolean isTeacherCurriculumnFolder(JSONObject folder) {
        return getTeacherCurriculumnFolderName().equals(folder.get("name"));
    }

    protected JSONArray getTeacherCurriculumFolders() {
        return invokeToArray(webResource().path(API_FOLDER).queryParam("formId", formId));
    }

    protected Map<JSONObject, String> buildTeacherCurriculumJson(DegreeFile degreeFile) {
        Map<JSONObject, String> jsons = new HashMap<JSONObject, String>();

        degreeFile.getTeacherFileSet().forEach(teacherFile -> {
            JSONObject toplevel = new JSONObject();
            StringBuilder output = new StringBuilder();

            toplevel.put("q-cf-name", teacherFile.getFileName());
            toplevel.put("q-cf-ies", teacherFile.getInstitution());
            toplevel.put("q-cf-uo", teacherFile.getOrganicUnit());
            if (teacherFile.getA3esTeacherCategory() != null) {
                toplevel.put("q-cf-cat",
                        teacherFile.getA3esTeacherCategory().getName().getContent().replaceAll(" ou equivalente", ""));
            } else {
                output.append(message("message.fieldName.empty", "q-cf-cat"));
            }
            if (teacherFile.getRegime() != null) {
                toplevel.put("q-cf-time", Float.valueOf(teacherFile.getRegime()));
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
                    file.put("cat",
                            teacherFile.getA3esTeacherCategory().getName().getContent().replaceAll(" ou equivalente", ""));
                } else {
                    output.append(message("message.fieldName.empty", "cat"));
                }
                if (teacherFile.getA3esDegreeType() != null) {
                    file.put("deg", teacherFile.getA3esDegreeType().toString());
                } else {
                    output.append(message("message.fieldName.empty", "deg"));
                }

                if (teacherFile.getDegreeScientificArea() != null) {
                    file.put("degarea", cut("area cientifica", teacherFile.getDegreeScientificArea(), output, 200));
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

                teacherFile.getA3esQualifications().forEach(qualification -> {

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
                        academic.put("rank", cut("classificação", qualification.getClassification(), output, 30));
                    } else {
                        output.append(message("message.fieldName.empty", "rank"));
                    }
                    academicArray.add(academic);
                });
                file.put("form-academic", academicArray);

                file.put("form-investigation",
                        getJsonActivities(teacherFile.getScientificActivitySet(), "investigation", "investigation", 500, output));
                file.put("form-highlevelactivities", getJsonActivities(teacherFile.getDevelopmentActivitySet(),
                        "highlevelactivities", "actividade", 200, output));
                file.put("form-otherpublications", getJsonActivities(teacherFile.getOtherPublicationActivitySet(),
                        "otherpublications", "outras publicações", 500, output));
                file.put("form-professional", getJsonActivities(teacherFile.getOtherProfessionalActivitySet(), "profession",
                        "profession", 200, output));

                JSONArray insideLectures = new JSONArray();
                teacherFile.getA3esTeachingServiceSet().forEach(teachingService -> {
                    JSONObject lecture = new JSONObject();
                    lecture.put("curricularUnit", cut("curricularUnit", teachingService.getCurricularUnitName(), output, 100));
                    lecture.put("studyCycle", cut("studyCycle", teachingService.getStudyCycles(), output, 200));
                    lecture.put("type", cut("type", teachingService.getMethodologyTypes(), output, 30));
                    lecture.put("hoursPerWeek", teachingService.getTotalHours());
                    insideLectures.add(lecture);
                });
                file.put("form-unit", insideLectures);
            }
            toplevel.put("q-cf-cfile", file);

            jsons.put(toplevel, output.toString());
        });

        return jsons;
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

    private String cut(String field, String content, StringBuilder output, int size) {
        if (content == null) {
            output.append(message("message.fieldName.empty", field));
        } else {
            int escapedLength = JSONObject.escape(content).getBytes().length;
            if (escapedLength > size) {
                output.append(message("message.field.cutted", field, size));
                return content.substring(0, size - 4 - (escapedLength - content.length())) + " ...";
            }
        }
        return content;
    }

    private String message(String code, Object... args) {
        return messageSource.getMessage(code, args, I18N.getLocale());
    }

}
