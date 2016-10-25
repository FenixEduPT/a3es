package org.fenixedu.a3es.domain;

import java.util.stream.Stream;

import org.fenixedu.bennu.A3esSpringConfiguration;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

public enum MethodologyType {

    THEORETICAL("T"), THEORETICAL_AND_PRATICAL("TP"), PRATICAL_AND_LABORATORIAL("PL"), FIELD_WORK("TC"), SEMINAR("S"), TRAINING(
            "E"), TUTORIAL("OT"), OTHER("O");

    private String sigla;

    private MethodologyType(String sigla) {
        this.sigla = sigla;
    }

    public LocalizedString getLocalizedName() {
        return BundleUtil.getLocalizedString(A3esSpringConfiguration.BUNDLE, this.getClass().getName() + "." + name());
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public static MethodologyType valueOfSigla(String sigla) {
        return Stream.of(values()).filter(mt -> mt.getSigla().equalsIgnoreCase(sigla)).findAny().orElse(null);
    }

}
