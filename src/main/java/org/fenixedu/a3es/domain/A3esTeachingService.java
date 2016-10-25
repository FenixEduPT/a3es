package org.fenixedu.a3es.domain;

import java.util.Objects;

import org.fenixedu.a3es.domain.exception.A3esDomainException;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class A3esTeachingService extends A3esTeachingService_Base {

    protected A3esTeachingService(TeacherFile teacherFile, String curricularUnitName, String studyCycle, String methodologyType,
            float hours) {
        super();
        Objects.requireNonNull(teacherFile);
        Objects.requireNonNull(curricularUnitName);
        Objects.requireNonNull(studyCycle);
        Objects.requireNonNull(methodologyType);
        Objects.requireNonNull(hours);
        setTeacherFile(teacherFile);
        setCurricularUnitName(curricularUnitName);
        setStudyCycles(studyCycle);
        setMethodologyTypes(methodologyType);
        setTotalHours(hours);
    }

    public void delete() {
        setTeacherFile(null);
        super.deleteDomainObject();
    }

    public static void create(TeacherFile teacherFile, String curricularUnitName, String studyCycles, String methodologyTypes,
            Float totalHours) {
        if (teacherFile.getA3esTeachingServiceSet().size() >= TeacherFile.MAXIMUM_TEACHING_SERVICE_NUMBER) {
            throw new A3esDomainException("error.exceded.maximum.teaching.service.number");
        }
        new A3esTeachingService(teacherFile, curricularUnitName, studyCycles, methodologyTypes, totalHours);
    }

    public void update(String curricularUnitName, String studyCycles, String methodologyTypes, Float totalHours) {
        if (Strings.isNullOrEmpty(curricularUnitName) && Strings.isNullOrEmpty(studyCycles)
                && Strings.isNullOrEmpty(methodologyTypes) && totalHours == null) {
            delete();
        } else {
            setCurricularUnitName(curricularUnitName);
            setStudyCycles(studyCycles);
            setMethodologyTypes(methodologyTypes);
            setTotalHours(totalHours);
        }
    }

    @Override
    public void setMethodologyTypes(String methodologyTypes) {
        if (Lists.newArrayList(methodologyTypes.split(",")).stream().filter(s -> MethodologyType.valueOfSigla(s.trim()) == null)
                .count() != 0) {
            throw new A3esDomainException("error.invalid.methodologyType");
        }
        super.setMethodologyTypes(methodologyTypes);
    }

}
