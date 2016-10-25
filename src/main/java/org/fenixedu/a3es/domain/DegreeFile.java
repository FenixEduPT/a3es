package org.fenixedu.a3es.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.a3es.domain.exception.A3esDomainException;

import com.google.common.base.Strings;

public class DegreeFile extends DegreeFile_Base {

    protected DegreeFile(AccreditationProcess accreditationProcess, String degreeName) {
        super();
        Objects.requireNonNull(accreditationProcess);
        Objects.requireNonNull(degreeName);
        setAccreditationProcess(accreditationProcess);
        setFileName(degreeName);
    }

    public static DegreeFile create(AccreditationProcess accreditationProcess, String degreeName) {
        return new DegreeFile(accreditationProcess, degreeName);
    }

    public void edit(String fileName, String degreeCode) {
        setFileName(fileName);
        setDegreeCode(degreeCode);
    }

    @Override
    public void setFileName(String fileName) {
        if (getAccreditationProcess().getDegreeFileSet().stream()
                .filter(df -> !df.equals(this) && df.getFileName().trim().equalsIgnoreCase(fileName.trim())).findAny()
                .isPresent()) {
            throw new A3esDomainException("error.degree.name.already.exists.in.process");
        }
        super.setFileName(fileName);
    }

    @Override
    public void setDegreeCode(String degreeCode) {
        if (getAccreditationProcess().getDegreeFileSet().stream().filter(df -> !df.equals(this)
                && !Strings.isNullOrEmpty(df.getDegreeCode()) && df.getDegreeCode().equalsIgnoreCase(degreeCode)).findAny()
                .isPresent()) {
            throw new A3esDomainException("error.degree.code.already.exists.in.process");
        }
        super.setDegreeCode(degreeCode);
    }

    public void delete() {
        getCurricularUnitFileSet().forEach(CurricularUnitFile::delete);
        getTeacherFileSet().forEach(tf -> tf.removeDegreeFile(this));
        setAccreditationProcess(null);
        setResponsibleGroup(null);
        super.deleteDomainObject();
    }

    @Override
    public Set<TeacherFile> getTeacherFileSet() {
        Set<TeacherFile> teacherFileSet = new HashSet<TeacherFile>();
        teacherFileSet.addAll(super.getTeacherFileSet());
        teacherFileSet.addAll(
                getCurricularUnitFileSet().stream().flatMap(cuf -> cuf.getTeacherFileSet().stream()).collect(Collectors.toSet()));
        return teacherFileSet;
    }

    public Set<TeacherFile> getCompletedTeacherFileSet() {
        return getTeacherFileSet().stream().filter(tf -> tf.isFilledAndValid()).collect(Collectors.toSet());
    }

    public Set<CurricularUnitFile> getCompletedCurricularUnitFileSet() {
        return getCurricularUnitFileSet().stream().filter(cu -> cu.isFilledAndValid()).collect(Collectors.toSet());
    }

}
