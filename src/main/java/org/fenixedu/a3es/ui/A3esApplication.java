package org.fenixedu.a3es.ui;

import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/a3es")
@SpringApplication(group = "#a3esManagers | professors", path = "a3es", title = "title.a3es")
public class A3esApplication {

}
