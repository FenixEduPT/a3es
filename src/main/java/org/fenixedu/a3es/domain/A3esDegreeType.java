package org.fenixedu.a3es.domain;

import org.fenixedu.bennu.A3esSpringConfiguration;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

public enum A3esDegreeType {

    DOCTORATE_DEGREE, MASTER_DEGREE, DEGREE, BACHELOR_DEGREE;

    @Override
    public String toString() {
        return getLocalizedName().getContent();
    }

    public LocalizedString getLocalizedName() {
        return BundleUtil.getLocalizedString(A3esSpringConfiguration.BUNDLE, this.getClass().getName() + "." + name());
    }
}
