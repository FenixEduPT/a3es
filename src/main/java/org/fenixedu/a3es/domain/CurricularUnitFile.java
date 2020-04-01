package org.fenixedu.a3es.domain;

import java.util.Locale;
import java.util.Objects;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

import com.google.common.base.Strings;

public class CurricularUnitFile extends CurricularUnitFile_Base {

    public static class CurricularCourseListener extends RelationAdapter<Bennu, DegreeModule> {

        @Override
        public void beforeRemove(final Bennu bennu, final DegreeModule degreeModule) {
            if (bennu != null && degreeModule instanceof CurricularCourse) {
                ((CurricularCourse) degreeModule).getCurricularUnitFileSet().clear();
            }
        }
    }

    static {
        CurricularCourse.getRelationRootDomainObjectDegreeModule().addListener(new CurricularCourseListener());
    }

    protected CurricularUnitFile(DegreeFile degreeFile, CurricularCourse curricularCourse, LocalizedString curricularUnitName) {
        Objects.requireNonNull(degreeFile);
        Objects.requireNonNull(curricularUnitName);
        setDegreeFile(degreeFile);
        setCurricularCourse(curricularCourse);
        setFileName(curricularUnitName.getContent(Locale.forLanguageTag(CoreConfiguration.getConfiguration().defaultLocale())));
        setCurricularUnitName(curricularUnitName);
    }

    public static CurricularUnitFile create(DegreeFile degreeFile, CurricularCourse curricularCourse,
            LocalizedString curricularUnitName) {
        return new CurricularUnitFile(degreeFile, curricularCourse, curricularUnitName);
    }

    public void edit(String scientificArea, String courseRegime, String workingHours, String contactHours, String ects,
            LocalizedString observations, String responsibleTeacherAndTeachingHours, String otherTeachersAndTeachingHours,
            LocalizedString learningOutcomes, LocalizedString syllabus, LocalizedString syllabusDemonstration,
            LocalizedString teachingMethodologies, LocalizedString teachingMethodologiesDemonstration,
            String bibliographicReferences) {
        setScientificArea(scientificArea);
        setCourseRegime(courseRegime);
        setWorkingHours(workingHours);
        setContactHours(contactHours);
        setEcts(ects);
        setObservations(observations);
        setResponsibleTeacherAndTeachingHours(responsibleTeacherAndTeachingHours);
        setOtherTeachersAndTeachingHours(otherTeachersAndTeachingHours);
        setLearningOutcomes(learningOutcomes);
        setSyllabus(syllabus);
        setSyllabusDemonstration(syllabusDemonstration);
        setTeachingMethodologies(teachingMethodologies);
        setTeachingMethodologiesDemonstration(teachingMethodologiesDemonstration);
        setBibliographicReferences(bibliographicReferences);
    }

    @Override
    public LocalizedString getCurricularUnitName() {
        return super.getCurricularUnitName();
    }

    @Override
    public String getScientificArea() {
        return super.getScientificArea();
    }

    @Override
    public String getCourseRegime() {
        return super.getCourseRegime();
    }

    @Override
    public String getWorkingHours() {
        return super.getWorkingHours();
    }

    @Override
    public String getContactHours() {
        return super.getContactHours();
    }

    @Override
    public String getEcts() {
        return super.getEcts();
    }

    @Override
    public LocalizedString getObservations() {
        return super.getObservations();
    }

    @Override
    public String getResponsibleTeacherAndTeachingHours() {
        return super.getResponsibleTeacherAndTeachingHours();
    }

    @Override
    public String getOtherTeachersAndTeachingHours() {
        return super.getOtherTeachersAndTeachingHours();
    }

    @Override
    public LocalizedString getLearningOutcomes() {
        return super.getLearningOutcomes();
    }

    @Override
    public LocalizedString getSyllabus() {
        return super.getSyllabus();
    }

    @Override
    public LocalizedString getSyllabusDemonstration() {
        return super.getSyllabusDemonstration();
    }

    @Override
    public LocalizedString getTeachingMethodologies() {
        return super.getTeachingMethodologies();
    }

    @Override
    public LocalizedString getTeachingMethodologiesDemonstration() {
        return super.getTeachingMethodologiesDemonstration();
    }

    @Override
    public String getBibliographicReferences() {
        return super.getBibliographicReferences();
    }

    public void delete() {
        getTeacherFileSet().forEach(tf -> tf.removeCurricularUnitFile(this));
        setDegreeFile(null);
        setResponsibleGroup(null);
        setCurricularCourse(null);
        super.deleteDomainObject();
    }

    @Override
    public AccreditationProcess getAccreditationProcess() {
        return getDegreeFile().getAccreditationProcess();
    }

    public boolean isFilledAndValid() {
        return !Strings.isNullOrEmpty(getResponsibleTeacherAndTeachingHours()) && !isFilledForAllLanguages(getLearningOutcomes())
                && !isFilledForAllLanguages(getSyllabus()) && !isFilledForAllLanguages(getSyllabusDemonstration())
                && !isFilledForAllLanguages(getTeachingMethodologies())
                && !isFilledForAllLanguages(getTeachingMethodologiesDemonstration())
                && !Strings.isNullOrEmpty(getBibliographicReferences())
                && hasValidSize(getResponsibleTeacherAndTeachingHours(), 100) && hasValidSize(getLearningOutcomes(), 1000)
                && hasValidSize(getSyllabus(), 1000) && hasValidSize(getSyllabusDemonstration(), 1000)
                && hasValidSize(getTeachingMethodologies(), 1000) && hasValidSize(getTeachingMethodologiesDemonstration(), 3000)
                && hasValidSize(getBibliographicReferences(), 1000);
    }

    private boolean isFilledForAllLanguages(LocalizedString localizedString) {
        if (localizedString != null) {
            for (Locale locale : CoreConfiguration.supportedLocales()) {
                if (Strings.isNullOrEmpty(localizedString.getContent(locale))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isUserAllowedToView() {
        return super.isUserAllowedToView() || getDegreeFile().isUserAllowedToView();
    }
}
