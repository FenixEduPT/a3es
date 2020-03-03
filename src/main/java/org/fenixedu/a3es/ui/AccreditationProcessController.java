package org.fenixedu.a3es.ui;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.a3es.domain.A3esFile;
import org.fenixedu.a3es.domain.AccreditationProcess;
import org.fenixedu.a3es.domain.CurricularUnitFile;
import org.fenixedu.a3es.domain.DegreeFile;
import org.fenixedu.a3es.domain.util.AccreditationProcessBean;
import org.fenixedu.a3es.domain.util.CurricularUnitFileBean;
import org.fenixedu.a3es.domain.util.DegreeFileBean;
import org.fenixedu.a3es.domain.util.ExportDegreeProcessBean;
import org.fenixedu.a3es.domain.util.ResponsibleBean;
import org.fenixedu.a3es.domain.util.TeacherFileBean;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/accreditationProcess")
@SpringFunctionality(app = A3esApplication.class, title = "title.a3es.accreditation.processes", accessGroup = "#a3esManagers")
public class AccreditationProcessController extends AccreditationController {

    @Autowired
    protected AccreditationProcessService service;
    
    @Autowired
    AccreditationProcessMigrationService migrationService;

    @Override
    @RequestMapping
    public String home(Model model) {
        model.addAttribute("processes", Bennu.getInstance().getAccreditationProcessSet());
        return "a3es/accreditationProcess/accreditationProcesses";
    }

    @RequestMapping(method = GET, value = "createProcess")
    public String createProcess(Model model) {
        model.addAttribute("form", new AccreditationProcessBean());
        model.addAttribute("executionYears", service.getExecutionYears());
        return "a3es/accreditationProcess/createAccreditationProcess";
    }

    @RequestMapping(method = POST, value = "createProcess")
    public String createProcess(Model model, @ModelAttribute AccreditationProcessBean form) {
        try {
            service.createProcess(form);
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
            model.addAttribute("form", form);
            model.addAttribute("executionYears", service.getExecutionYears());
            return "a3es/accreditationProcess/createAccreditationProcess";
        }
        model.addAttribute("processes", Bennu.getInstance().getAccreditationProcessSet());
        return "a3es/accreditationProcess/accreditationProcesses";
    }

    @RequestMapping(method = GET, value = "editProcess/{accreditationProcess}")
    public String editProcess(Model model, @PathVariable AccreditationProcess accreditationProcess) {
        model.addAttribute("form", new AccreditationProcessBean(accreditationProcess));
        return "a3es/accreditationProcess/editAccreditationProcess";
    }

    @RequestMapping(method = POST, value = "editProcess")
    public String editProcess(Model model, @ModelAttribute AccreditationProcessBean form) {
        try {
            service.editProcess(form);
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
            model.addAttribute("form", form);
            return "a3es/accreditationProcess/editAccreditationProcess";
        }
        model.addAttribute("processes", Bennu.getInstance().getAccreditationProcessSet());
        return "a3es/accreditationProcess/accreditationProcesses";
    }

    @RequestMapping(method = GET, value = "showProcess/{accreditationProcess}")
    public String showProcess(Model model, @PathVariable AccreditationProcess accreditationProcess) {
        model.addAttribute("process", accreditationProcess);
        return "a3es/accreditationProcess/accreditationProcess";
    }

    @RequestMapping(method = GET, value = "deleteProcess/{accreditationProcess}")
    public String deleteProcess(Model model, @PathVariable AccreditationProcess accreditationProcess) {
        service.deleteProcess(accreditationProcess);
        model.addAttribute("processes", Bennu.getInstance().getAccreditationProcessSet());
        return "a3es/accreditationProcess/accreditationProcesses";
    }

    @RequestMapping(method = GET, value = "exportProcessFiles/{accreditationProcess}")
    public String exportProcessFiles(Model model, @ModelAttribute AccreditationProcess accreditationProcess,
            RedirectAttributes attrs, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        String fileName = accreditationProcess.getProcessName().replaceAll(" ", "_") + ".xls";
        response.setHeader("Content-Disposition", "filename=" + fileName);
        service.exportProcessFiles(accreditationProcess, response.getOutputStream());
        response.flushBuffer();
        return null;
    }

    @RequestMapping(method = GET, value = "exportDegreeFiles/{degreeFile}")
    public String exportDegreeFiles(Model model, @ModelAttribute DegreeFile degreeFile, RedirectAttributes attrs,
            HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "filename=" + degreeFile.getFileName().replaceAll(" ", "_") + ".xls");
        service.exportDegreeFiles(degreeFile, response.getOutputStream());
        response.flushBuffer();
        return null;
    }

    @RequestMapping(method = GET, value = "degreesFiles/{accreditationProcess}")
    public String degreesFiles(Model model, @PathVariable AccreditationProcess accreditationProcess) {
        return showDegreeFiles(model, new AccreditationProcessBean(accreditationProcess));
    }

    private String showDegreeFiles(Model model, AccreditationProcessBean accreditationProcessBean) {
        model.addAttribute("form", accreditationProcessBean);
        model.addAttribute("degreeTypes",
                DegreeType.all().filter(dt -> dt.isBolonhaType() && !dt.isEmpty() && !dt.getDegreeSet().isEmpty()).sorted()
                        .collect(Collectors.toList()));
        return "a3es/accreditationProcess/degreeFiles";
    }

    @RequestMapping(method = POST, value = "addDegreeFiles")
    public String addDegreeFiles(Model model, @ModelAttribute AccreditationProcessBean form) {
        try {
            service.addDegreeFiles(form);
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
        }
        return showDegreeFiles(model, form);
    }

    @RequestMapping(method = GET, value = "addDegreeFile/{accreditationProcess}/{degree}")
    public String addDegreeFile(Model model, @PathVariable AccreditationProcess accreditationProcess,
            @PathVariable Degree degree) {
        try {
            service.addDegreeFile(accreditationProcess, degree);
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
        }
        return showDegreeFiles(model, new AccreditationProcessBean(accreditationProcess));
    }

    @RequestMapping(method = GET, value = "removeDegreeFile/{degreeFile}")
    public String removeDegreeFile(Model model, @ModelAttribute DegreeFile degreeFile) {
        AccreditationProcessBean accreditationProcessBean = new AccreditationProcessBean(degreeFile.getAccreditationProcess());
        try {
            service.deleteA3esFile(degreeFile, null);
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
        }
        return showDegreeFiles(model, accreditationProcessBean);
    }

    @RequestMapping(method = GET, value = "editDegreeFile/{degreeFile}")
    public String editDegreeFile(Model model, @PathVariable DegreeFile degreeFile) {
        model.addAttribute("form", new DegreeFileBean(degreeFile));
        return "a3es/degreeFile";
    }

    @RequestMapping(method = POST, value = "editDegreeFile")
    public String editDegreeFile(Model model, @ModelAttribute DegreeFileBean form) {
        try {
            service.editDegreeFile(form);
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
            model.addAttribute("form", form);
            return "a3es/degreeFile";
        }
        return showDegreeFiles(model, new AccreditationProcessBean(form.getDegreeFile().getAccreditationProcess()));
    }

    @RequestMapping(method = GET, value = "addResponsible/{a3esFile}")
    public String addResponsible(Model model, @PathVariable A3esFile a3esFile,
            @RequestParam(value = "degreeFile", required = false) DegreeFile degreeFile) {
        model.addAttribute("form", new ResponsibleBean(a3esFile, degreeFile));
        return "a3es/addResponsible";
    }

    @RequestMapping(method = POST, value = "addResponsible")
    public String addResponsible(Model model, @ModelAttribute ResponsibleBean form) {
        try {
            service.addResponsible(form);
        } catch (RuntimeException re) {
            model.addAttribute("error", re.getLocalizedMessage());
        }
        model.addAttribute("form", form);
        return "a3es/addResponsible";
    }

    @RequestMapping(method = GET, value = "removeResponsible/{a3esFile}/{user}")
    public String removeResponsible(Model model, @PathVariable A3esFile a3esFile, @PathVariable User user,
            @RequestParam(value = "degreeFile", required = false) DegreeFile degreeFile) {
        try {
            service.removeResponsible(a3esFile, user);
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
        }
        model.addAttribute("form", new ResponsibleBean(a3esFile, degreeFile));
        return "a3es/addResponsible";
    }

    @RequestMapping(method = GET, value = "showFiles/{a3esFile}")
    public String showFiles(Model model, @PathVariable A3esFile a3esFile,
            @RequestParam(value = "degreeFile", required = false) DegreeFile degreeFile) {
        if (a3esFile instanceof CurricularUnitFile) {
            return showCurricularUnitFiles(model, ((CurricularUnitFile) a3esFile).getDegreeFile());
        } else if (a3esFile instanceof DegreeFile) {
            return degreesFiles(model, a3esFile.getAccreditationProcess());
        }
        return showTeacherFiles(model, degreeFile);
    }

    @RequestMapping(method = GET, value = "createCurricularUnitFile/{degreeFile}")
    public String createCurricularUnitFile(Model model, @PathVariable DegreeFile degreeFile) {
        CurricularUnitFileBean form = new CurricularUnitFileBean();
        form.setDegreeFile(degreeFile);
        model.addAttribute("form", form);
        return "a3es/createCurricularUnitFile";
    }

    @RequestMapping(method = POST, value = "createCurricularUnitFile")
    public String createCurricularUnitFile(Model model, @ModelAttribute CurricularUnitFileBean form) {
        try {
            service.createCurricularUnitFile(form);
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
            model.addAttribute("form", form);
            return "a3es/createCurricularUnitFile";
        }
        return showCurricularUnitFiles(model, form.getDegreeFile());
    }

    @RequestMapping(method = GET, value = "createTeacherFile/{degreeFile}")
    public String createTeacherFile(Model model, @PathVariable DegreeFile degreeFile) {
        TeacherFileBean form = new TeacherFileBean();
        form.setDegreeFile(degreeFile);
        model.addAttribute("form", form);
        return "a3es/createTeacherFile";
    }

    @RequestMapping(method = POST, value = "createTeacherFile")
    public String createTeacherFile(Model model, @ModelAttribute TeacherFileBean form) {
        try {
            service.createTeacherFile(form);
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
            model.addAttribute("form", form);
            return "a3es/createTeacherFile";
        }
        return showTeacherFiles(model, form.getDegreeFile());
    }

    @RequestMapping(method = GET, value = "removeFile/{a3esFile}")
    public String removeFile(Model model, @PathVariable A3esFile a3esFile,
            @RequestParam(value = "degreeFile", required = false) DegreeFile degreeFile) {
        service.deleteA3esFile(a3esFile, degreeFile);
        if (a3esFile instanceof CurricularUnitFile) {
            return showCurricularUnitFiles(model, degreeFile);
        }
        return showTeacherFiles(model, degreeFile);
    }

    @RequestMapping(method = GET, value = "exportToA3es/{degreeFile}")
    public String exportToA3es(Model model, @ModelAttribute DegreeFile degreeFile) {
        model.addAttribute("form", new ExportDegreeProcessBean(degreeFile));
        return "a3es/accreditationProcess/exportDegreeProcess";
    }

    @RequestMapping(method = POST, value = "exportCurricularUnitFilesToA3es")
    public String exportCurricularUnitFilesToA3es(Model model, @ModelAttribute ExportDegreeProcessBean form) {
        try {
            migrationService.exportCurricularUnitFilesToA3es(form);
            model.addAttribute("output", "label.processing.in.background");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getLocalizedMessage());
        }
        model.addAttribute("form", form);
        return "a3es/accreditationProcess/exportDegreeProcess";
    }

    @RequestMapping(method = POST, value = "exportTeacherFilesToA3es")
    public String exportTeacherFilesToA3es(Model model, @ModelAttribute ExportDegreeProcessBean form) {
        migrationService.exportTeacherUnitFilesToA3es(form);
        model.addAttribute("output", "label.processing.in.background");
        model.addAttribute("form", form);
        return "a3es/accreditationProcess/exportDegreeProcess";
    }

}
