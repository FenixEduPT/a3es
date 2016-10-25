package org.fenixedu.a3es.domain;

import com.google.common.base.Strings;

public class TeacherActivity extends TeacherActivity_Base implements Comparable<TeacherActivity> {

    public TeacherActivity() {
        super();
    }

    @Override
    public int compareTo(TeacherActivity o) {
        return getOrder().compareTo(o.getOrder());
    }

    public void update(String activity) {
        if (Strings.isNullOrEmpty(activity)) {
            delete();
        } else {
            setActivity(activity);
        }
    }

    public void delete() {
        super.deleteDomainObject();
    }

}
