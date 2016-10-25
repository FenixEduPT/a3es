package org.fenixedu.a3es.domain;

import java.util.Locale;
import java.util.Objects;

import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.LocalizedString;

import com.google.common.base.Strings;

public class CurricularUnitFile extends CurricularUnitFile_Base {

    protected CurricularUnitFile(DegreeFile degreeFile, String curricularUnitName) {
        Objects.requireNonNull(degreeFile);
        Objects.requireNonNull(curricularUnitName);
        setDegreeFile(degreeFile);
        setFileName(curricularUnitName);
    }

    public static CurricularUnitFile create(DegreeFile degreeFile, String curricularUnitName) {
        return new CurricularUnitFile(degreeFile, curricularUnitName);
    }

    public void edit(String responsibleTeacherAndTeachingHours, String otherTeachersAndTeachingHours,
            LocalizedString learningOutcomes, LocalizedString syllabus, LocalizedString syllabusDemonstration,
            LocalizedString teachingMethodologies, LocalizedString teachingMethodologiesDemonstration,
            String bibliographicReferences) {
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
