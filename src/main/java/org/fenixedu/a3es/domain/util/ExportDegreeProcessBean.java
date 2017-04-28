package org.fenixedu.a3es.domain.util;

import java.io.Serializable;

import org.fenixedu.a3es.domain.DegreeFile;
import org.fenixedu.a3es.ui.strategy.MigrationStrategy.AccreditationType;

public class ExportDegreeProcessBean implements Serializable {

    protected DegreeFile degreeFile;

    protected String user;

    protected String password;

    protected String base64Hash;

    protected String formId;

    protected AccreditationType accreditationType;

    public ExportDegreeProcessBean() {
    }

    public ExportDegreeProcessBean(DegreeFile degreeFile) {
        setDegreeFile(degreeFile);
    }

    public DegreeFile getDegreeFile() {
        return degreeFile;
    }

    public void setDegreeFile(DegreeFile degreeFile) {
        this.degreeFile = degreeFile;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBase64Hash() {
        return base64Hash;
    }

    public void setBase64Hash(String base64Hash) {
        this.base64Hash = base64Hash;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public AccreditationType getAccreditationType() {
        return accreditationType;
    }

    public void setAccreditationType(AccreditationType accreditationType) {
        this.accreditationType = accreditationType;
    }

}
