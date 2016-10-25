package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

@BennuSpringModule(basePackages = "org.fenixedu.a3es", bundles = "A3esResources")
public class A3esSpringConfiguration {

    public static final String BUNDLE = "resources/A3esResources";

    @ConfigurationManager(description = "A3es Configuration")
    public interface ConfigurationProperties {
        @ConfigurationProperty(key = "a3es.url", defaultValue = "http://testes.a3es.pt/si/iportal.php")
        public String a3esURL();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }
}
