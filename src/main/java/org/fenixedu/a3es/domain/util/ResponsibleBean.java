package org.fenixedu.a3es.domain.util;

import java.io.Serializable;

import org.fenixedu.a3es.domain.A3esFile;
import org.fenixedu.a3es.domain.DegreeFile;
import org.fenixedu.bennu.core.domain.User;

public class ResponsibleBean implements Serializable {

    User responsible;
    A3esFile a3esFile;
    DegreeFile degreeFile;

    public ResponsibleBean() {
    }

    public ResponsibleBean(A3esFile a3esFile, DegreeFile degreeFile) {
        setA3esFile(a3esFile);
        setDegreeFile(degreeFile);
    }

    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
    }

    public A3esFile getA3esFile() {
        return a3esFile;
    }

    public void setA3esFile(A3esFile a3esFile) {
        this.a3esFile = a3esFile;
    }

    public DegreeFile getDegreeFile() {
        return degreeFile;
    }

    public void setDegreeFile(DegreeFile degreeFile) {
        this.degreeFile = degreeFile;
    }

}
