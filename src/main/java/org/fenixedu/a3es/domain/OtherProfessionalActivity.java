package org.fenixedu.a3es.domain;

import java.util.Objects;

import org.fenixedu.a3es.domain.exception.A3esDomainException;

import com.google.common.base.Strings;

public class OtherProfessionalActivity extends OtherProfessionalActivity_Base {

    protected OtherProfessionalActivity(TeacherFile teacherFile, Integer order, String activity) {
        super();
        Objects.requireNonNull(teacherFile);
        Objects.requireNonNull(order);
        Objects.requireNonNull(activity);
        setOrder(order);
        setActivity(activity);
        setTeacherFile(teacherFile);
    }

    public static void create(TeacherFile teacherFile, String activity) {
        if (!Strings.isNullOrEmpty(activity)) {
            if (teacherFile.getOtherProfessionalActivitySet().size() >= TeacherFile.MAXIMUM_ACTIVITIES_NUMBER) {
                throw new A3esDomainException("error.exceded.maximum.otherProfessionalActivity.number");
            }
            new OtherProfessionalActivity(teacherFile, teacherFile.getOtherProfessionalActivitySet().size() + 1, activity);
        }
    }

    @Override
    public void delete() {
        setTeacherFile(null);
        super.deleteDomainObject();
    }

}
