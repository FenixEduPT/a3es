package org.fenixedu.a3es.domain;

import java.util.Objects;

import org.fenixedu.a3es.domain.exception.A3esDomainException;

import com.google.common.base.Strings;

public class OtherPublicationActivity extends OtherPublicationActivity_Base {

    protected OtherPublicationActivity(TeacherFile teacherFile, Integer order, String activity) {
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
            if (teacherFile.getOtherPublicationActivitySet().size() >= TeacherFile.MAXIMUM_ACTIVITIES_NUMBER) {
                throw new A3esDomainException("error.exceded.maximum.otherPublicationActivity.number");
            }
            new OtherPublicationActivity(teacherFile, teacherFile.getOtherPublicationActivitySet().size() + 1, activity);
        }
    }

    @Override
    public void delete() {
        setTeacherFile(null);
        super.deleteDomainObject();
    }

}
