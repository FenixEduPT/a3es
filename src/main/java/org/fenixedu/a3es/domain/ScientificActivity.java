package org.fenixedu.a3es.domain;

import java.util.Objects;

import org.fenixedu.a3es.domain.exception.A3esDomainException;

import com.google.common.base.Strings;

public class ScientificActivity extends ScientificActivity_Base {

    protected ScientificActivity(TeacherFile teacherFile, Integer order, String activity) {
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
            if (teacherFile.getScientificActivitySet().size() >= TeacherFile.MAXIMUM_ACTIVITIES_NUMBER) {
                throw new A3esDomainException("error.exceded.maximum.scientificActivity.number");
            }
            new ScientificActivity(teacherFile, teacherFile.getScientificActivitySet().size() + 1, activity);
        }
    }

    @Override
    public void delete() {
        setTeacherFile(null);
        super.deleteDomainObject();
    }

}
