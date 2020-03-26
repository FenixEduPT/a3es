package org.fenixedu.a3es.ui.strategy;

import org.fenixedu.a3es.domain.util.ExportDegreeProcessBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.base.Strings;

public class InstitutionalMigrationStrategy extends MigrationStrategy {

    protected static final String API_FOLDER = "api_cvfolder";

    private JSONArray teacherCurriculumFolders;

    @Override
    protected void initializeFormId(ExportDegreeProcessBean form) {
        formId = form.getFormId();
        if (Strings.isNullOrEmpty(form.getFormId())) {
            try {
                JSONObject result = invoke(webResource().path(API_FOLDER));
                formId = (String) result.get("formId");
                form.setFormId(formId);
                teacherCurriculumFolders = (JSONArray) result.get("folders");
            } catch (javax.ws.rs.NotAuthorizedException e) {
                throw e;
            }
        }
    }

    @Override
    public String getTeacherCurriculumnFolderName() {
        return "D5.1.1 - Fichas curriculares";
    }

    @Override
    protected String getTeacherCurriculumId() {
        return "cvFolderId";
    }

    @Override
    protected boolean isTeacherCurriculumnFolder(String teacherCurriculumnFolderName, JSONObject folder) {
        return teacherCurriculumnFolderName.equals(folder.get("name"))
                && Bennu.getInstance().getInstitutionUnit().getName().equals(folder.get("uo"));
    }

    @Override
    protected JSONArray getTeacherCurriculumFolders() {
        return teacherCurriculumFolders != null ? teacherCurriculumFolders : (JSONArray) invoke(webResource().path(API_FOLDER))
                .get("folders");
    }
}
