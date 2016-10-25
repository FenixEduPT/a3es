package org.fenixedu.a3es.domain.util;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.a3es.domain.AccreditationProcess;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AccreditationProcessBean implements Serializable {
    final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

    String name;
    String beginDate;
    String endDate;
    ExecutionYear executionYear;
    Set<Degree> degrees;
    AccreditationProcess accreditationProcess;
    String otherDegree;

    public AccreditationProcessBean() {
    }

    public AccreditationProcessBean(AccreditationProcess accreditationProcess) {
        setAccreditationProcess(accreditationProcess);
        setName(accreditationProcess.getProcessName());
        setBeginDate(formatter.print(accreditationProcess.getBeginFillingPeriod()));
        setEndDate(formatter.print(accreditationProcess.getEndFillingPeriod()));
        setExecutionYear(accreditationProcess.getExecutionYear());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public DateTime getBeginDateTime() {
        return formatter.parseDateTime(getBeginDate());
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public DateTime getEndDateTime() {
        return formatter.parseDateTime(getEndDate());
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public Set<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(Set<Degree> degrees) {
        this.degrees = degrees;
    }

    public AccreditationProcess getAccreditationProcess() {
        return accreditationProcess;
    }

    public void setAccreditationProcess(AccreditationProcess accreditationProcess) {
        this.accreditationProcess = accreditationProcess;
    }

    public String getOtherDegree() {
        return otherDegree;
    }

    public void setOtherDegree(String otherDegree) {
        this.otherDegree = otherDegree;
    }

    public List<Degree> getDegrees(DegreeType degreeType) {
        return Degree.readBolonhaDegrees().stream().filter(d -> d.getDegreeType().equals(degreeType))
                .sorted(Degree.COMPARATOR_BY_DEGREE_TYPE_DEGREE_NAME_AND_ID).collect(Collectors.toList());
    }
}
