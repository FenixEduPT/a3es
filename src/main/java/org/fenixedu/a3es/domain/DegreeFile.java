package org.fenixedu.a3es.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.a3es.domain.exception.A3esDomainException;
import org.fenixedu.academic.domain.Degree;

import com.google.common.base.Strings;

public class DegreeFile extends DegreeFile_Base {

    protected DegreeFile(AccreditationProcess accreditationProcess, Degree degree, String degreeName, String degreeAcronym) {
        super();
        Objects.requireNonNull(accreditationProcess);
        Objects.requireNonNull(degreeName);
        setAccreditationProcess(accreditationProcess);
        setFileName(degreeName);
        setDegreeAcronym(degreeAcronym);
    }

    public static DegreeFile create(AccreditationProcess accreditationProcess, Degree degree, String degreeName,
            String degreeAcronym) {
        return new DegreeFile(accreditationProcess, degree, degreeName, degreeAcronym);
    }

    public void edit(String fileName, String degreeCode, String degreeAcronym) {
        setFileName(fileName);
        setDegreeCode(degreeCode);
        setDegreeAcronym(degreeAcronym);
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
    
    @Override
    public void setDegreeAcronym(String degreeAcronym) {
        if (getAccreditationProcess().getDegreeFileSet().stream().filter(df -> !df.equals(this)
                && !Strings.isNullOrEmpty(df.getDegreeAcronym()) && df.getDegreeAcronym().equalsIgnoreCase(degreeAcronym)).findAny()
                .isPresent()) {
            throw new A3esDomainException("error.degree.acronym.already.exists.in.process");
        }
        super.setDegreeAcronym(degreeAcronym);
    }

    public void delete() {
        getCurricularUnitFileSet().forEach(CurricularUnitFile::delete);
        getTeacherFileSet().forEach(tf -> tf.removeDegreeFile(this));
        setAccreditationProcess(null);
        setResponsibleGroup(null);
        setDegree(null);
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
