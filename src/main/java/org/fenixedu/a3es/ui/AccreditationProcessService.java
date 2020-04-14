package org.fenixedu.a3es.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.fenixedu.a3es.domain.A3esFile;
import org.fenixedu.a3es.domain.A3esQualification;
import org.fenixedu.a3es.domain.A3esTeachingService;
import org.fenixedu.a3es.domain.AccreditationProcess;
import org.fenixedu.a3es.domain.CurricularUnitFile;
import org.fenixedu.a3es.domain.DegreeFile;
import org.fenixedu.a3es.domain.DevelopmentActivity;
import org.fenixedu.a3es.domain.OtherProfessionalActivity;
import org.fenixedu.a3es.domain.OtherPublicationActivity;
import org.fenixedu.a3es.domain.ScientificActivity;
import org.fenixedu.a3es.domain.TeacherFile;
import org.fenixedu.a3es.domain.util.AccreditationProcessBean;
import org.fenixedu.a3es.domain.util.CurricularUnitFileBean;
import org.fenixedu.a3es.domain.util.DegreeFileBean;
import org.fenixedu.a3es.domain.util.ResponsibleBean;
import org.fenixedu.a3es.domain.util.TeacherFileBean;
import org.fenixedu.a3es.domain.util.TeacherFileBean.A3esQualificationBean;
import org.fenixedu.a3es.domain.util.TeacherFileBean.A3esTeachingServiceBean;
import org.fenixedu.a3es.domain.util.TeacherFileBean.ActivityBean;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.domain.degreeStructure.RootCourseGroup;
import org.fenixedu.academic.domain.organizationalStructure.ScientificAreaUnit;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.commons.spreadsheet.SheetData;
import org.fenixedu.commons.spreadsheet.SpreadsheetBuilder;
import org.fenixedu.commons.spreadsheet.WorkbookExportFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

@Service
public class AccreditationProcessService {

    public static final Set<A3esFileFiller> FILLERS = new HashSet<>();

    @Autowired
    private MessageSource messageSource;

    public Set<ExecutionYear> getExecutionYears() {
        return Bennu.getInstance().getExecutionYearsSet().stream().sorted(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR).limit(5)
                .collect(Collectors.toCollection(() -> new TreeSet<>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR)));
    }

    @Atomic(mode = TxMode.WRITE)
    public AccreditationProcess createProcess(AccreditationProcessBean form) {
        return AccreditationProcess.create(form.getName(), form.getBeginDateTime(), form.getEndDateTime(),
                form.getExecutionYear());
    }

    @Atomic(mode = TxMode.WRITE)
    public void editProcess(AccreditationProcessBean form) {
        form.getAccreditationProcess().edit(form.getName(), form.getBeginDateTime(), form.getEndDateTime());
        return;
    }

    @Atomic(mode = TxMode.WRITE)
    public void deleteProcess(AccreditationProcess accreditationProcess) {
        accreditationProcess.delete();
    }

    @Atomic(mode = TxMode.WRITE)
    public void addDegreeFiles(AccreditationProcessBean form) {
        if (form.getDegrees() != null && !form.getDegrees().isEmpty()) {
            for (Degree degree : form.getDegrees()) {
                createDegreeFile(form.getAccreditationProcess(), degree);
            }
        }
        if (!Strings.isNullOrEmpty(form.getOtherDegree())) {
            DegreeFile.create(form.getAccreditationProcess(), null, form.getOtherDegree(), null);
        }
        return;
    }

    @Atomic(mode = TxMode.WRITE)
    public void addDegreeFile(AccreditationProcess accreditationProcess, Degree degree) {
        createDegreeFile(accreditationProcess, degree);
        return;
    }

    private void createDegreeFile(AccreditationProcess accreditationProcess, Degree degree) {
        ExecutionYear executionYear = accreditationProcess.getExecutionYear();
        String degreeName = degree.getPresentationName(accreditationProcess.getExecutionYear());
        DegreeFile degreeFile = DegreeFile.create(accreditationProcess, degree, degreeName, degree.getSigla());
        DegreeCurricularPlan lastActiveDegreeCurricularPlan = findDegreeCurricularPlan(degree, executionYear);
        if (lastActiveDegreeCurricularPlan != null) {
            RootCourseGroup root = lastActiveDegreeCurricularPlan.getRoot();
            Set<CurricularCourse> curricularCourseSet = new HashSet<CurricularCourse>();
            for (ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
                for (CurricularCourse course : root.getAllCurricularCourses(executionSemester)) {
                    CompetenceCourse competence = course.getCompetenceCourse();
                    if (competence != null && !curricularCourseSet.contains(course)) {
                        curricularCourseSet.add(course);
                        LocalizedString curricularUnitName = competence.getNameI18N(executionSemester);
                        CurricularUnitFile curricularUnitFile = CurricularUnitFile.create(degreeFile, course, curricularUnitName);
                        Set<Professorship> professorship =
                                course.getAssociatedExecutionCoursesSet().stream()
                                        .filter(ec -> executionYear.getExecutionPeriodsSet().contains(ec.getExecutionPeriod()))
                                        .flatMap(ec -> ec.getProfessorshipsSet().stream()).collect(Collectors.toSet());
                        Set<User> responsibles =
                                professorship.stream().filter(p -> p.isResponsibleFor()).map(p -> p.getPerson().getUser())
                                        .collect(Collectors.toSet());
                        Set<String> otherTeachers =
                                professorship.stream().filter(p -> !p.isResponsibleFor()).map(p -> p.getPerson().getName())
                                        .collect(Collectors.toSet());

                        curricularUnitFile.setResponsibleGroup(Group.users(responsibles.stream()).toPersistentGroup());
                        String responsibleTeacherAndTeachingHours =
                                Joiner.on(", ").join(
                                        responsibles.stream().map(u -> u.getPerson().getName()).collect(Collectors.toSet()));

                        String otherTeachersAndTeachingHours = Joiner.on(", ").join(otherTeachers);

                        ScientificAreaUnit scientificAreaUnit = competence.getScientificAreaUnit(executionSemester);
                        String scientificArea = scientificAreaUnit == null ? null : scientificAreaUnit.getAcronym();
                        String workingHours = String.valueOf(course.getAutonomousWorkHours(executionSemester));
                        String contactHours = String.valueOf(course.getContactLoad(executionSemester));
                        String ects = String.valueOf(course.getEctsCredits(executionSemester));
                        RegimeType regime = course.getRegime(executionSemester);
                        String courseRegime = regime == null ? null : regime.getLocalizedName();
                        LocalizedString observations = new LocalizedString();
                        if (course.isOptionalCurricularCourse()) {
                            CoreConfiguration.supportedLocales().forEach(locale -> {
                                observations.with(locale, messageSource.getMessage("label.optionalCourse", null, locale));
                            });
                        }
                        LocalizedString learningOutcomes = competence.getObjectivesI18N(executionSemester);
                        LocalizedString syllabus = competence.getProgramI18N(executionSemester);
                        LocalizedString teachingMethodologies = competence.getLocalizedEvaluationMethod(executionSemester);

                        List<String> references = new ArrayList<String>();
                        final BibliographicReferences bibliographicReferences =
                                competence.getBibliographicReferences(executionSemester);
                        if (bibliographicReferences != null) {
                            for (BibliographicReference reference : bibliographicReferences.getMainBibliographicReferences()) {
                                references.add(Joiner.on(", ").join(reference.getTitle(), reference.getAuthors(),
                                        reference.getYear(), reference.getReference()));
                            }
                        }

                        String bibliographicReferencesString = Joiner.on("; ").join(references);

                        curricularUnitFile.edit(scientificArea, courseRegime, workingHours, contactHours, ects, observations,
                                responsibleTeacherAndTeachingHours, otherTeachersAndTeachingHours, learningOutcomes, syllabus,
                                null, teachingMethodologies, null, bibliographicReferencesString);

                        for (Professorship professorhip : professorship) {
                            TeacherFile teacherFile =
                                    TeacherFile.create(professorhip.getTeacher().getPerson().getUser(), curricularUnitFile);
                            FILLERS.forEach(f -> f.fill(professorhip.getTeacher(), teacherFile, degree));
                        }
                        FILLERS.forEach(f -> f.fill(course, curricularUnitFile));
                    }
                }
            }
        }
        FILLERS.forEach(f -> f.fill(degree, degreeFile));
    }

    private DegreeCurricularPlan findDegreeCurricularPlan(final Degree degree, final ExecutionYear executionYear) {
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            for (final ExecutionYear year : degreeCurricularPlan.getRoot().getBeginContextExecutionYears()) {
                if (year == executionYear) {
                    return degreeCurricularPlan;
                }
            }
        }
        return  degree.getLastActiveDegreeCurricularPlan();
    }

	@Atomic(mode = TxMode.WRITE)
    public void editDegreeFile(DegreeFileBean form) {
        form.getDegreeFile().edit(form.getFileName(), form.getDegreeCode(), form.getDegreeAcronym());
    }

    @Atomic(mode = TxMode.WRITE)
    public void addResponsible(ResponsibleBean form) {
        form.getA3esFile().addResponsible(form.getResponsible());
    }

    @Atomic(mode = TxMode.WRITE)
    public void removeResponsible(A3esFile a3esFile, User user) {
        a3esFile.removeResponsible(user);
    }

    @Atomic(mode = TxMode.WRITE)
    public void createCurricularUnitFile(CurricularUnitFileBean form) {
        CurricularUnitFile.create(form.getDegreeFile(), null, form.getCurricularUnitName());
    }

    @Atomic(mode = TxMode.WRITE)
    public void editCurricularUnitFile(CurricularUnitFileBean form) {
        form.getCurricularUnitFile().edit(form.getScientificArea(), form.getCourseRegime(), form.getWorkingHours(),
                form.getContactHours(), form.getEcts(), form.getObservations(), form.getResponsibleTeacherAndTeachingHours(),
                form.getOtherTeachersAndTeachingHours(), form.getLearningOutcomes(), form.getSyllabus(),
                form.getSyllabusDemonstration(), form.getTeachingMethodologies(), form.getTeachingMethodologiesDemonstration(),
                form.getBibliographicReferences());
    }

    @Atomic(mode = TxMode.WRITE)
    public void createTeacherFile(TeacherFileBean form) {
        if (form.getUser() != null) {
            TeacherFile teacherFile = TeacherFile.create(form.getUser(), form.getDegreeFile());
            if (form.getUser().getPerson().getTeacher() != null) {
                FILLERS.forEach(f -> f.fill(form.getUser().getPerson().getTeacher(), teacherFile, form.getDegreeFile().getDegree()));
            }
        } else {
            TeacherFile.create(form.getFileName(), form.getDegreeFile());
        }
        return;
    }

    @Atomic(mode = TxMode.WRITE)
    public void deleteA3esFile(A3esFile a3esFile, DegreeFile degreeFile) {
        if (a3esFile instanceof TeacherFile) {
            ((TeacherFile) a3esFile).removeDegreeFile(degreeFile);
        } else if (a3esFile instanceof CurricularUnitFile) {
            ((CurricularUnitFile) a3esFile).delete();
        } else {
            ((DegreeFile) a3esFile).delete();
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void editTeacherFile(TeacherFileBean form) {
        form.getTeacherFile().edit(form.getTeacherName(), form.getResearchUnit(), form.getA3esTeacherCategory(), form.getRegime(),
                form.getA3esDegreeType(), form.getDegreeScientificArea(), form.getDegreeYear(), form.getDegreeInstitution());

        for (A3esQualificationBean a3esQualificationBean : form.getA3esQualificationBeanSet()) {
            if (a3esQualificationBean.getA3esQualification() != null) {
                a3esQualificationBean.getA3esQualification().update(form.getTeacherFile(), a3esQualificationBean.getDegree(),
                        a3esQualificationBean.getYear(), a3esQualificationBean.getArea(), a3esQualificationBean.getInstitution(),
                        a3esQualificationBean.getClassification());
            } else if (!a3esQualificationBean.isEmpty()) {
                A3esQualification.create(form.getTeacherFile(), a3esQualificationBean.getDegree(),
                        a3esQualificationBean.getYear(), a3esQualificationBean.getArea(), a3esQualificationBean.getInstitution(),
                        a3esQualificationBean.getClassification());
            }
        }

        for (ActivityBean activityBean : form.getScientificActivitySet()) {
            if (activityBean.getTeacherActivity() != null) {
                activityBean.getTeacherActivity().update(activityBean.getActivity());
            } else {
                ScientificActivity.create(form.getTeacherFile(), activityBean.getActivity());
            }
        }

        for (ActivityBean activityBean : form.getDevelopmentActivitySet()) {
            if (activityBean.getTeacherActivity() != null) {
                activityBean.getTeacherActivity().update(activityBean.getActivity());
            } else {
                DevelopmentActivity.create(form.getTeacherFile(), activityBean.getActivity());
            }
        }

        for (ActivityBean activityBean : form.getOtherPublicationActivitySet()) {
            if (activityBean.getTeacherActivity() != null) {
                activityBean.getTeacherActivity().update(activityBean.getActivity());
            } else {
                OtherPublicationActivity.create(form.getTeacherFile(), activityBean.getActivity());
            }
        }

        for (ActivityBean activityBean : form.getOtherProfessionalActivitySet()) {
            if (activityBean.getTeacherActivity() != null) {
                activityBean.getTeacherActivity().update(activityBean.getActivity());
            } else {
                OtherProfessionalActivity.create(form.getTeacherFile(), activityBean.getActivity());
            }
        }

        for (A3esTeachingServiceBean a3esTeachingServiceBean : form.getA3esTeachingServiceBeanSet()) {
            if (a3esTeachingServiceBean.getA3esTeachingService() != null) {
                a3esTeachingServiceBean.getA3esTeachingService().update(a3esTeachingServiceBean.getCurricularUnitName(),
                        a3esTeachingServiceBean.getStudyCycles(), a3esTeachingServiceBean.getMethodologyTypes(),
                        a3esTeachingServiceBean.getTotalHours());
            } else if (!a3esTeachingServiceBean.isEmpty()) {
                A3esTeachingService.create(form.getTeacherFile(), a3esTeachingServiceBean.getCurricularUnitName(),
                        a3esTeachingServiceBean.getStudyCycles(), a3esTeachingServiceBean.getMethodologyTypes(),
                        a3esTeachingServiceBean.getTotalHours());
            }
        }

    }

    public void exportProcessFiles(AccreditationProcess accreditationProcess, OutputStream out) throws IOException {
        exportFiles(accreditationProcess.getCurricularUnitFileSet(), accreditationProcess.getTeacherFileSet(), out);
    }

    public void exportDegreeFiles(DegreeFile degreeFile, OutputStream out) throws IOException {
        exportFiles(degreeFile.getCurricularUnitFileSet(), degreeFile.getTeacherFileSet(), out);
    }

    public void exportFiles(Set<CurricularUnitFile> curricularUnitFiles, Set<TeacherFile> teacherFiles, OutputStream out)
            throws IOException {

        SpreadsheetBuilder builder = new SpreadsheetBuilder();

        builder.addSheet(message("label.curricularUnitFiles").replaceAll(" ", "_"),
                new SheetData<CurricularUnitFile>(curricularUnitFiles) {
                    @Override
                    protected void makeLine(CurricularUnitFile curricularUnitFile) {
                        addCell(message("label.degree"), curricularUnitFile.getDegreeFile().getFileName());
                        for (Locale locale : CoreConfiguration.supportedLocales()) {
                            addCell(String.format("%s (%s)", message("label.curricularUnit"), locale.getDisplayLanguage()),
                                    getLocalizedStringContent(curricularUnitFile.getCurricularUnitName(), locale));
                        }
                        addCell(message("label.scientificAreaAcronym"), curricularUnitFile.getScientificArea());
                        addCell(message("label.courseRegime"), curricularUnitFile.getCourseRegime());
                        addCell(message("label.workingHours"), curricularUnitFile.getWorkingHours());
                        addCell(message("label.contactHours"), curricularUnitFile.getContactHours());
                        addCell(message("label.ects"), curricularUnitFile.getEcts());
                        for (Locale locale : CoreConfiguration.supportedLocales()) {
                            addCell(String.format("%s (%s)", message("label.observations"), locale.getDisplayLanguage()),
                                    getLocalizedStringContent(curricularUnitFile.getObservations(), locale));
                        }
                        addCell(message("label.responsibleTeacherAndTeachingHours"),
                                curricularUnitFile.getResponsibleTeacherAndTeachingHours());
                        addCell(message("label.otherTeachersAndTeachingHours"),
                                curricularUnitFile.getOtherTeachersAndTeachingHours());
                        for (Locale locale : CoreConfiguration.supportedLocales()) {
                            addCell(String.format("%s (%s)", message("label.learningOutcomes"), locale.getDisplayLanguage()),
                                    getLocalizedStringContent(curricularUnitFile.getLearningOutcomes(), locale));
                            addCell(String.format("%s (%s)", message("label.syllabus"), locale.getDisplayLanguage()),
                                    getLocalizedStringContent(curricularUnitFile.getSyllabus(), locale));
                            addCell(String.format("%s (%s)", message("label.syllabusDemonstration"), locale.getDisplayLanguage()),
                                    getLocalizedStringContent(curricularUnitFile.getSyllabusDemonstration(), locale));
                            addCell(String.format("%s (%s)", message("label.teachingMethodologies"), locale.getDisplayLanguage()),
                                    getLocalizedStringContent(curricularUnitFile.getTeachingMethodologies(), locale));
                            addCell(String.format("%s (%s)", message("label.teachingMethodologiesDemonstration"),
                                    locale.getDisplayLanguage()),
                                    getLocalizedStringContent(curricularUnitFile.getTeachingMethodologiesDemonstration(),
                                            locale));
                        }
                        addCell(message("label.bibliographicReferences"), curricularUnitFile.getBibliographicReferences());
                    }

                    private String getLocalizedStringContent(LocalizedString localizedString, Locale locale) {
                        return localizedString == null ? null : localizedString.getContent(locale);
                    }
                });

        builder.addSheet(message("label.teacherFiles").replaceAll(" ", "_"), new SheetData<TeacherFile>(teacherFiles) {
            @Override
            protected void makeLine(TeacherFile teacherFile) {
                addCell(message("label.name"), teacherFile.getFileName());
                addCell(message("label.presentationName"), teacherFile.getTeacherName());
                addCell(message("label.higherEducationInstitution"), teacherFile.getInstitution());
                addCell(message("label.organicUnit"), teacherFile.getOrganicUnit());
                addCell(message("label.researchUnitFiliation"), teacherFile.getResearchUnit());
                addCell(message("label.category"), teacherFile.getA3esTeacherCategory() != null ? teacherFile
                        .getA3esTeacherCategory().getName().getContent() : null);
                addCell(message("label.degreeType"),
                        teacherFile.getA3esDegreeType() != null ? teacherFile.getA3esDegreeType().getLocalizedName() : null);
                addCell(message("label.degreeScientificArea"), teacherFile.getDegreeScientificArea());
                addCell(message("label.degreeYear"), teacherFile.getDegreeYear());
                addCell(message("label.degreeInstitution"), teacherFile.getDegreeInstitution());
                addCell(message("label.regime"), teacherFile.getRegime());

                TeacherFileBean teacherFileBean = new TeacherFileBean(teacherFile);
                teacherFileBean.getA3esQualificationBeanSet().forEach(q -> {
                    addCell(message("label.year"), q.getYear());
                    addCell(message("label.degreeType"), q.getDegree());
                    addCell(message("label.area"), q.getArea());
                    addCell(message("label.institution"), q.getInstitution());
                    addCell(message("label.classification"), q.getClassification());
                });
                teacherFileBean.getScientificActivitySet().forEach(a -> {
                    addCell(message("label.scientificActivity"), a.getActivity());
                });

                teacherFileBean.getDevelopmentActivitySet().forEach(a -> {
                    addCell(message("label.developmentActivity"), a.getActivity());
                });
                teacherFileBean.getOtherPublicationActivitySet().forEach(a -> {
                    addCell(message("label.otherPublicationActivity"), a.getActivity());
                });
                teacherFileBean.getOtherProfessionalActivitySet().forEach(a -> {
                    addCell(message("label.otherProfessionalActivity"), a.getActivity());
                });
                teacherFileBean.getA3esTeachingServiceBeanSet().forEach(ts -> {
                    addCell(message("label.curricularUnit"), ts.getCurricularUnitName());
                    addCell(message("label.studyCycle"), ts.getStudyCycles());
                    addCell(message("label.type"), ts.getMethodologyTypes());
                    addCell(message("label.totalContactHours"), ts.getTotalHours());
                });

            }
        });
        builder.build(WorkbookExportFormat.EXCEL, out);
    }

    private String message(String code, Object... args) {
        return messageSource.getMessage(code, args, I18N.getLocale());
    }

}
