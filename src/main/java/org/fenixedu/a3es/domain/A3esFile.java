package org.fenixedu.a3es.domain;

import java.util.Locale;

import org.fenixedu.a3es.domain.exception.A3esDomainException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.LocalizedString;
import org.json.simple.JSONObject;

import com.google.common.base.Strings;

public abstract class A3esFile extends A3esFile_Base {

    public abstract AccreditationProcess getAccreditationProcess();

    protected boolean hasValidSize(LocalizedString localizedString, int size) {
        if (localizedString != null) {
            for (Locale locale : localizedString.getLocales()) {
                if (!hasValidSize(localizedString.getContent(locale), size)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String getFileName() {
        return super.getFileName();
    }

    public String getPresentationName() {
        return getFileName();
    }

    protected boolean hasValidSize(String content, int size) {
        return (Strings.isNullOrEmpty(content) || content.length() <= size);
    }

    public void addResponsible(User user) {
        if (user == null) {
            throw new A3esDomainException("error.invalid.user");
        }
        Group responsibleGroup = Group.users(user);
        if (getResponsibleGroup() != null) {
            responsibleGroup = responsibleGroup.or(getResponsibleGroup().toGroup());
        }
        super.setResponsibleGroup(responsibleGroup.toPersistentGroup());
    }

    public void removeResponsible(User user) {
        super.setResponsibleGroup(getResponsibleGroup().toGroup().revoke(user).toPersistentGroup());
    }

    public boolean getCanBeManageByUser() {
        final User user = Authenticate.getUser();
        return DynamicGroup.get("a3esManagers").isMember(user);
    }

    @Deprecated
    public boolean getCanBeDeleted() {
        return getCanBeManageByUser();
    }

    public boolean isUserAllowedToView() {
        final User user = Authenticate.getUser();
        return DynamicGroup.get("a3esManagers").isMember(user)
                || (getResponsibleGroup() != null && getResponsibleGroup().isMember(user));
    }

    public boolean isUserAllowedToEdit() {
        return isUserAllowedToView() && getAccreditationProcess().isInFillingPeriod();
    }
}
