package org.fenixedu.a3es.ui;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.fenixedu.a3es.domain.A3esTeacherCategory;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.Atomic;

@RequestMapping("/a3esConfiguration")
@SpringFunctionality(app = A3esApplication.class, title = "title.configuration", accessGroup = "#a3esManagers")
public class A3esConfigurationController {

    @RequestMapping
    public String home(Model model) {
        model.addAttribute("a3esTeacherCategories", Bennu.getInstance().getA3esTeacherCategorySet());
        model.addAttribute("teacherCategories", Bennu.getInstance().getTeacherCategorySet());
        return "a3es/configuration";
    }

    @RequestMapping(method = POST, value = "createCategory")
    public String createCategory(Model model, @RequestParam String name) {
        A3esTeacherCategory.create(name);
        return home(model);
    }

    @RequestMapping(method = POST, value = "editCategory")
    public String editCategory(Model model, @RequestParam A3esTeacherCategory a3esTeacherCategory,
            @RequestParam org.fenixedu.academic.domain.TeacherCategory teacherCategory) {
        editCategory(a3esTeacherCategory, teacherCategory);
        return home(model);
    }

    @Atomic
    private void editCategory(A3esTeacherCategory a3esTeacherCategory,
            org.fenixedu.academic.domain.TeacherCategory teacherCategory) {
        a3esTeacherCategory.addTeacherCategory(teacherCategory);
    }
}
