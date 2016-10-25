package org.fenixedu.a3es.domain.exception;

import org.fenixedu.bennu.A3esSpringConfiguration;

public class A3esDomainException extends org.fenixedu.bennu.core.domain.exceptions.DomainException {

    private static final String DEFAULT_BUNDLE = A3esSpringConfiguration.BUNDLE;

    public A3esDomainException(final String key, final String... args) {
        super(DEFAULT_BUNDLE, key, args);
    }

}
