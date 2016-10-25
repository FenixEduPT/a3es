package org.fenixedu.a3es.domain;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.a3es.domain.exception.A3esDomainException;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class AccreditationProcess extends AccreditationProcess_Base {

    protected AccreditationProcess(String processName, DateTime beginFillingPeriod, DateTime endFillingPeriod,
            ExecutionYear executionYear) {
        super();
        if (findByProcessName(processName) != null) {
            throw new A3esDomainException("error.process.name.already.exists");
        }
        if (beginFillingPeriod.isAfter(endFillingPeriod)) {
            throw new A3esDomainException("error.invalid.dates");
        }
        setBennu(Bennu.getInstance());
        setProcessName(processName);
        setBeginFillingPeriod(beginFillingPeriod);
        setEndFillingPeriod(endFillingPeriod);
        setExecutionYear(executionYear);
    }

    public static AccreditationProcess create(String processName, DateTime beginDate, DateTime endDate,
            ExecutionYear executionYear) {
        return new AccreditationProcess(processName, beginDate, endDate, executionYear);
    }

    public static boolean getCanBeManageByUser() {
        final User user = Authenticate.getUser();
        return DynamicGroup.get("a3esManagers").isMember(user);
    }

    @Deprecated
    public boolean getCanBeDeleted() {
        return getCanBeManageByUser();
    }

    public static AccreditationProcess findByProcessName(String processName) {
        return Bennu.getInstance().getAccreditationProcessSet().stream()
                .filter(ap -> ap.getProcessName().equalsIgnoreCase(processName)).findAny().orElse(null);
    }

    public void edit(String processName, DateTime beginDateTime, DateTime endDateTime) {
        AccreditationProcess findByProcessName = findByProcessName(processName);
        if (findByProcessName != null && !findByProcessName.equals(this)) {
            throw new A3esDomainException("error.process.name.already.exists");
        }
        if (beginDateTime.isAfter(endDateTime)) {
            throw new A3esDomainException("error.invalid.dates");
        }
        setProcessName(processName);
        setBeginFillingPeriod(beginDateTime);
        setEndFillingPeriod(endDateTime);
    }

    @Override
    public String getProcessName() {
        return super.getProcessName();
    }

    @Override
    public DateTime getBeginFillingPeriod() {
        return super.getBeginFillingPeriod();
    }

    @Override
    public DateTime getEndFillingPeriod() {
        return super.getEndFillingPeriod();
    }

    public Interval getFillingInterval() {
        return new Interval(getBeginFillingPeriod(), getEndFillingPeriod());
    }

    public Boolean isInFillingPeriod() {
        return getFillingInterval().containsNow();
    }

    public Boolean isFillingPeriodOver() {
        return getEndFillingPeriod().isBeforeNow();
    }

    public void delete() {
        for (DegreeFile degreeFile : getDegreeFileSet()) {
            degreeFile.delete();
        }
        setBennu(null);
        setExecutionYear(null);
        super.deleteDomainObject();
    }

    public Set<TeacherFile> getCompletedTeacherFileSet() {
        return getTeacherFileSet().stream().filter(tf -> tf.isFilledAndValid()).collect(Collectors.toSet());
    }

    public Set<CurricularUnitFile> getCurricularUnitFileSet() {
        return getDegreeFileSet().stream().flatMap(df -> df.getCurricularUnitFileSet().stream()).collect(Collectors.toSet());
    }

    public Set<CurricularUnitFile> getCompletedCurricularUnitFileSet() {
        return getDegreeFileSet().stream().flatMap(df -> df.getCurricularUnitFileSet().stream())
                .filter(cu -> cu.isFilledAndValid()).collect(Collectors.toSet());
    }

    public Set<TeacherFile> getTeacherFileSet() {
        return getDegreeFileSet().stream().flatMap(df -> df.getTeacherFileSet().stream()).collect(Collectors.toSet());
    }
}
