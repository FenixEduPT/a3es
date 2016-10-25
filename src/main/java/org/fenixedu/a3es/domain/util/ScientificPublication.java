package org.fenixedu.a3es.domain.util;

import java.util.List;

import org.fenixedu.bennu.core.domain.User;

public interface ScientificPublication {
    public default List<String> getScientificPublicationSet(final User user) {
        return null;
    }
}
