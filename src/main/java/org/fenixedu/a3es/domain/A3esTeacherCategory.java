package org.fenixedu.a3es.domain;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class A3esTeacherCategory extends A3esTeacherCategory_Base implements Comparable<A3esTeacherCategory> {

    public A3esTeacherCategory(LocalizedString name) {
        super();
        setBennu(Bennu.getInstance());
        setName(name);
    }

    @Atomic
    public static A3esTeacherCategory create(String name) {
        return new A3esTeacherCategory(new LocalizedString(Locale.getDefault(), name));
    }

    public static List<A3esTeacherCategory> getAll() {
        return Bennu.getInstance().getA3esTeacherCategorySet().stream().sorted().collect(Collectors.toList());
    }

    @Override
    public int compareTo(A3esTeacherCategory o) {
        return getName().getContent().compareTo(o.getName().getContent());
    }
}
