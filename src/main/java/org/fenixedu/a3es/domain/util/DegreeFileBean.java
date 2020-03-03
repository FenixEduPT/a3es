package org.fenixedu.a3es.domain.util;

import java.io.Serializable;

import org.fenixedu.a3es.domain.DegreeFile;

public class DegreeFileBean implements Serializable {

    DegreeFile degreeFile;
    String fileName;
    String degreeCode;
    String degreeAcronym;

    public DegreeFileBean() {
    }

    public DegreeFileBean(DegreeFile degreeFile) {
        setDegreeFile(degreeFile);
        setFileName(degreeFile.getFileName());
        setDegreeCode(degreeFile.getDegreeCode());
        setDegreeAcronym(degreeFile.getDegreeAcronym());
    }

    public DegreeFile getDegreeFile() {
        return degreeFile;
    }

    public void setDegreeFile(DegreeFile degreeFile) {
        this.degreeFile = degreeFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDegreeCode() {
        return degreeCode;
    }

    public void setDegreeCode(String degreeCode) {
        this.degreeCode = degreeCode;
    }

    public String getDegreeAcronym() {
        return degreeAcronym;
    }

    public void setDegreeAcronym(String degreeAcronym) {
        this.degreeAcronym = degreeAcronym;
    }

}
