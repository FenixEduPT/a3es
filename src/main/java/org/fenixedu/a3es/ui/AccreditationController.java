package org.fenixedu.a3es.ui;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.fenixedu.a3es.domain.A3esFile;
import org.fenixedu.a3es.domain.AccreditationProcess;
import org.fenixedu.a3es.domain.CurricularUnitFile;
import org.fenixedu.a3es.domain.DegreeFile;
import org.fenixedu.a3es.domain.TeacherFile;
import org.fenixedu.a3es.domain.util.CurricularUnitFileBean;
import org.fenixedu.a3es.domain.util.TeacherFileBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/accreditation")
@SpringFunctionality(app = A3esApplication.class, title = "title.a3es", accessGroup = "professors")
public class AccreditationController {

    @Autowired
    AccreditationProcessService service;

    @RequestMapping
    public String home(Model model) {
        model.addAttribute(
                "curricularUnitFiles",
                Bennu.getInstance()
                        .getAccreditationProcessSet()
                        .stream()
                        .flatMap(ap -> ap.getCurricularUnitFileSet().stream())
                        .filter(cuf -> cuf.isUserAllowedToView())
                        .sorted(Comparator.comparing((CurricularUnitFile f) -> f.getAccreditationProcess().getProcessName())
                                .thenComparing(f -> f.getDegreeFile().getFileName()).thenComparing(f -> f.getFileName()))
                        .collect(Collectors.toList()));
        model.addAttribute(
                "teacherFiles",
                Bennu.getInstance()
                        .getAccreditationProcessSet()
                        .stream()
                        .flatMap(ap -> ap.getTeacherFileSet().stream())
                        .filter(tf -> tf.isUserAllowedToView())
                        .sorted(Comparator.comparing((TeacherFile f) -> f.getAccreditationProcess().getProcessName())
                                .thenComparing(f -> f.getFileName())).collect(Collectors.toList()));
        return "a3es/accreditationFilesToFill";
    }

    @RequestMapping(method = GET, value = "showCurricularUnitFiles/{degreeFile}")
    public String showCurricularUnitFiles(Model model, @PathVariable DegreeFile degreeFile) {
        model.addAttribute("degreeFile", degreeFile);
        return "a3es/curricularUnitFiles";
    }

    @RequestMapping(method = GET, value = "showTeacherFiles/{degreeFile}")
    public String showTeacherFiles(Model model, @PathVariable DegreeFile degreeFile) {
        model.addAttribute("degreeFile", degreeFile);
        return "a3es/teacherFiles";
    }

    @RequestMapping(method = GET, value = "showFile/{a3esFile}")
    public String showFile(Model model, @PathVariable A3esFile a3esFile,
            @RequestParam(value = "degreeFile", required = false) DegreeFile degreeFile) {
        if (a3esFile instanceof CurricularUnitFile) {
            CurricularUnitFileBean curricularUnitFileBean = new CurricularUnitFileBean((CurricularUnitFile) a3esFile);
            curricularUnitFileBean.setDegreeFile(degreeFile);
            model.addAttribute("form", curricularUnitFileBean);
            if (a3esFile.isUserAllowedToEdit()) {
                return "a3es/editCurricularUnitFile";
            }
            return "a3es/showCurricularUnitFile";
        }
        TeacherFileBean teacherFileBean = new TeacherFileBean((TeacherFile) a3esFile);
        teacherFileBean.setDegreeFile(degreeFile);
        model.addAttribute("form", teacherFileBean);
        if (a3esFile.isUserAllowedToEdit()) {
            return "a3es/editTeacherFile";
        }
        return "a3es/showTeacherFile";
    }

    @RequestMapping(method = POST, value = "editCurricularUnitFile")
    public String editCurricularUnitFile(Model model, @ModelAttribute CurricularUnitFileBean form) {
        model.addAttribute("degreeFile", form.getCurricularUnitFile().getDegreeFile());
        try {
            service.editCurricularUnitFile(form);
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
            model.addAttribute("form", form);
            return "a3es/editCurricularUnitFile";
        }
        if (AccreditationProcess.getCanBeManageByUser()) {
            return showCurricularUnitFiles(model, form.getDegreeFile());
        }
        return home(model);
    }

    @RequestMapping(method = POST, value = "editTeacherFile")
    public String editTeacherFile(Model model, @ModelAttribute TeacherFileBean form) {
        model.addAttribute("process", form.getTeacherFile().getAccreditationProcess());
        try {
            service.editTeacherFile(form);
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
            model.addAttribute("form", form);
            return "a3es/editTeacherFile";
        }
        if (AccreditationProcess.getCanBeManageByUser()) {
            return showTeacherFiles(model, form.getDegreeFile());
        }
        return home(model);
    }
}
