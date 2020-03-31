package org.fenixedu.a3es.ui.strategy;

import org.fenixedu.a3es.domain.DegreeFile;
import org.fenixedu.a3es.domain.TeacherFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class NewProgramAccreditationMigrationStrategy extends MigrationStrategy {

    @Override
    public String getProcessFolderName() {
        return "Apresentação do pedido - Novo ciclo de estudos";
    }

    @Override
    public String getCompetenceCoursesFolderIndex() {
        return "4.4.";
    }

    @Override
    public String getCompetenceCoursesFolderName() {
        return getCompetenceCoursesFolderIndex() + " Unidades Curriculares";
    }

    @Override
    public String getTeacherCurriculumnFolderName() {
        return "5.2. Fichas curriculares dos docentes do ciclo de estudos";
    }

    @Override
    protected void setTeachingService(DegreeFile degreeFile, TeacherFile teacherFile, StringBuilder output, JSONObject file) {
        JSONArray insideLectures = new JSONArray();
        JSONArray otherLectures = new JSONArray();
        teacherFile.getA3esTeachingService().forEach(teachingService -> {
            JSONObject lecture = new JSONObject();
            lecture.put("curricularUnit", cut("curricularUnit", teachingService.getCurricularUnitName(), output, 100));
            if (teachingService.getStudyCycles().contains(degreeFile.getDegreeAcronym())) {
                lecture.put("type", cut("type", teachingService.getMethodologyTypes(), output, 30));
                lecture.put("hoursPerWeek", teachingService.getTotalHours());
                insideLectures.add(lecture);
            } else {
                lecture.put("studyCycle", cut("studyCycle", teachingService.getStudyCycles(), output, 200));
                lecture.put("contactHours", teachingService.getTotalHours());
                otherLectures.add(lecture);
            }
        });
        file.put("form-unit", insideLectures.subList(0, Math.min(TEACHER_SERVICE_ITEMS, insideLectures.size())));
        file.put("form-otherunit", otherLectures.subList(0, Math.min(TEACHER_SERVICE_ITEMS, otherLectures.size())));
    }
}
