package org.fenixedu.a3es.domain;

import java.util.Objects;

import org.fenixedu.a3es.domain.exception.A3esDomainException;

import com.google.common.base.Strings;

public class A3esQualification extends A3esQualification_Base {

    protected A3esQualification(TeacherFile teacherFile, String degree, Integer year, String area, String institution,
            String classification) {
        super();
        Objects.requireNonNull(teacherFile);
        setTeacherFile(teacherFile);
        setDegree(degree);
        setYear(year);
        setArea(area);
        setInstitution(institution);
        setClassification(classification);
    }

    public static A3esQualification create(TeacherFile teacherFile, String degree, Integer year, String area, String institution,
            String classification) {
        if (teacherFile.getA3esQualificationSet().size() >= TeacherFile.MAXIMUM_QUALIFICATION_NUMBER) {
            throw new A3esDomainException("error.exceded.maximum.qualification.number");
        }
        return new A3esQualification(teacherFile, degree, year, area, institution, classification);
    }

    public void update(TeacherFile teacherFile, String degree, Integer year, String area, String institution,
            String classification) {
        if (year == null && Strings.isNullOrEmpty(degree) && Strings.isNullOrEmpty(area) && Strings.isNullOrEmpty(institution)
                && Strings.isNullOrEmpty(classification)) {
            delete();
        } else {
            setDegree(degree);
            setYear(year);
            setArea(area);
            setInstitution(institution);
            setClassification(classification);
        }
    }

    public void delete() {
        setTeacherFile(null);
        super.deleteDomainObject();
    }

}
