package org.fenixedu.a3es.ui;

import org.fenixedu.a3es.domain.CurricularUnitFile;
import org.fenixedu.a3es.domain.DegreeFile;
import org.fenixedu.a3es.domain.TeacherFile;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Teacher;

public interface A3esFileFiller {

    public default void fill(final Degree degree, final DegreeFile degreeFile) {

    }

    public default void fill(final CurricularCourse curricularCourse, final CurricularUnitFile curricularUnitFile) {

    }

    public default void fill(final Teacher teacher, final TeacherFile teacherFile, final Degree degree) {

    }

}
