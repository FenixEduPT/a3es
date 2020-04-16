package org.fenixedu.a3es.domain;

import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.bennu.A3esSpringConfiguration;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

public class CurricularContext extends CurricularContext_Base {

    public CurricularContext(LocalizedString curricularGroup, LocalizedString curricularPeriod) {
        super();
        setCurricularGroup(curricularGroup);
        setCurricularPeriod(curricularPeriod);
        setBennu(Bennu.getInstance());
    }

    public static void createContexts(CurricularUnitFile curricularUnitFile) {
        ExecutionYear executionYear = curricularUnitFile.getAccreditationProcess().getExecutionYear();
        for (Context context : curricularUnitFile.getCurricularCourse().getParentContextsSet().stream().filter(c -> c.isOpen(executionYear))
                .collect(Collectors.toSet())) {
            LocalizedString curricularGroup = groupFor(context, executionYear);
            ExecutionSemester executionSemester = getExecutionSemester(context, executionYear);
            LocalizedString curricularPeriod =
                    BundleUtil.getLocalizedString(A3esSpringConfiguration.BUNDLE, "label.curricularPeriod",
                            String.valueOf(context.getCurricularYear()), String.valueOf(executionSemester.getSemester()));

            CurricularContext curricularContext =
                    Bennu.getInstance().getCurricularContextSet().stream()
                            .filter(cc -> cc.getCurricularGroup().equals(curricularGroup) && cc.getCurricularPeriod().equals(curricularPeriod))
                            .findAny().orElse(null);
            if (curricularContext == null) {
                curricularContext = new CurricularContext(curricularGroup, curricularPeriod);
            }
            curricularContext.addCurricularUnitFile(curricularUnitFile);
        }
    }

    public static LocalizedString groupFor(final Context context, final ExecutionYear executionYear) {
        LocalizedString groupFor = groupFor(context.getParentCourseGroup(), executionYear, new LocalizedString());
        return groupFor != null ? groupFor : BundleUtil.getLocalizedString(A3esSpringConfiguration.BUNDLE, "label.noGroup");
    }

    public static LocalizedString groupFor(final CourseGroup group, final ExecutionYear executionYear, final LocalizedString groupName) {
        if (group == null || group.isCycleCourseGroup()) {
            return groupName;
        }
        final LocalizedString newName = group.getNameI18N(executionYear).append(groupName, ", ");
        return group.getParentContextsSet().stream().filter(c -> c.isOpen(executionYear))
                .map(c -> groupFor(c.getParentCourseGroup(), executionYear, newName)).findAny().orElse(newName);
    }

    public static ExecutionSemester getExecutionSemester(final Context context, final ExecutionYear executionYear) {
        final CurricularPeriod curricularPeriod = context.getCurricularPeriod();
        if (curricularPeriod.getAcademicPeriod().equals(AcademicPeriod.SEMESTER)) {
            return (curricularPeriod.getChildOrder() == 1) ? executionYear.getFirstExecutionPeriod() : executionYear.getLastExecutionPeriod();
        } else {
            return executionYear.getFirstExecutionPeriod();
        }
    }

    public void removeCurricularUnitFile(CurricularUnitFile curricularUnitFile) {
        getCurricularUnitFileSet().remove(curricularUnitFile);
        delete();
    }

    public void delete() {
        if (getCurricularUnitFileSet().size() == 0) {
            setBennu(null);
            super.deleteDomainObject();
        }
    }

    @Override
    public LocalizedString getCurricularGroup() {
        return super.getCurricularGroup();
    }

    @Override
    public LocalizedString getCurricularPeriod() {
        return super.getCurricularPeriod();
    }

}
