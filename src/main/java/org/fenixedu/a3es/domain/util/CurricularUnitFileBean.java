package org.fenixedu.a3es.domain.util;

import java.io.Serializable;

import org.fenixedu.a3es.domain.CurricularUnitFile;
import org.fenixedu.a3es.domain.DegreeFile;
import org.fenixedu.commons.i18n.LocalizedString;

public class CurricularUnitFileBean implements Serializable {

    DegreeFile degreeFile;
    CurricularUnitFile curricularUnitFile;
    String curricularUnitName;
    String responsibleTeacherAndTeachingHours;
    String otherTeachersAndTeachingHours;
    LocalizedString learningOutcomes;
    LocalizedString syllabus;
    LocalizedString syllabusDemonstration;
    LocalizedString teachingMethodologies;
    LocalizedString teachingMethodologiesDemonstration;
    String bibliographicReferences;

    public CurricularUnitFileBean() {
    }

    public CurricularUnitFileBean(CurricularUnitFile curricularUnitFile) {
        setCurricularUnitFile(curricularUnitFile);
        setCurricularUnitName(curricularUnitFile.getFileName());
        setResponsibleTeacherAndTeachingHours(curricularUnitFile.getResponsibleTeacherAndTeachingHours());
        setOtherTeachersAndTeachingHours(curricularUnitFile.getOtherTeachersAndTeachingHours());
        setLearningOutcomes(curricularUnitFile.getLearningOutcomes());
        setSyllabus(curricularUnitFile.getSyllabus());
        setSyllabusDemonstration(curricularUnitFile.getSyllabusDemonstration());
        setTeachingMethodologies(curricularUnitFile.getTeachingMethodologies());
        setTeachingMethodologiesDemonstration(curricularUnitFile.getTeachingMethodologiesDemonstration());
        setBibliographicReferences(curricularUnitFile.getBibliographicReferences());
    }

    public DegreeFile getDegreeFile() {
        return degreeFile;
    }

    public void setDegreeFile(DegreeFile degreeFile) {
        this.degreeFile = degreeFile;
    }

    public CurricularUnitFile getCurricularUnitFile() {
        return curricularUnitFile;
    }

    public void setCurricularUnitFile(CurricularUnitFile curricularUnitFile) {
        this.curricularUnitFile = curricularUnitFile;
    }

    public String getCurricularUnitName() {
        return curricularUnitName;
    }

    public void setCurricularUnitName(String curricularUnitName) {
        this.curricularUnitName = curricularUnitName;
    }

    public String getResponsibleTeacherAndTeachingHours() {
        return responsibleTeacherAndTeachingHours;
    }

    public void setResponsibleTeacherAndTeachingHours(String responsibleTeacherAndTeachingHours) {
        this.responsibleTeacherAndTeachingHours = responsibleTeacherAndTeachingHours;
    }

    public String getOtherTeachersAndTeachingHours() {
        return otherTeachersAndTeachingHours;
    }

    public void setOtherTeachersAndTeachingHours(String otherTeachersAndTeachingHours) {
        this.otherTeachersAndTeachingHours = otherTeachersAndTeachingHours;
    }

    public LocalizedString getLearningOutcomes() {
        return learningOutcomes;
    }

    public void setLearningOutcomes(LocalizedString learningOutcomes) {
        this.learningOutcomes = learningOutcomes;
    }

    public LocalizedString getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(LocalizedString syllabus) {
        this.syllabus = syllabus;
    }

    public LocalizedString getSyllabusDemonstration() {
        return syllabusDemonstration;
    }

    public void setSyllabusDemonstration(LocalizedString syllabusDemonstration) {
        this.syllabusDemonstration = syllabusDemonstration;
    }

    public LocalizedString getTeachingMethodologies() {
        return teachingMethodologies;
    }

    public void setTeachingMethodologies(LocalizedString teachingMethodologies) {
        this.teachingMethodologies = teachingMethodologies;
    }

    public LocalizedString getTeachingMethodologiesDemonstration() {
        return teachingMethodologiesDemonstration;
    }

    public void setTeachingMethodologiesDemonstration(LocalizedString teachingMethodologiesDemonstration) {
        this.teachingMethodologiesDemonstration = teachingMethodologiesDemonstration;
    }

    public String getBibliographicReferences() {
        return bibliographicReferences;
    }

    public void setBibliographicReferences(String bibliographicReferences) {
        this.bibliographicReferences = bibliographicReferences;
    }

}
