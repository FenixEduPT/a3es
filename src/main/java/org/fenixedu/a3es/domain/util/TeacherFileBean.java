package org.fenixedu.a3es.domain.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.fenixedu.a3es.domain.A3esDegreeType;
import org.fenixedu.a3es.domain.A3esQualification;
import org.fenixedu.a3es.domain.A3esTeacherCategory;
import org.fenixedu.a3es.domain.A3esTeachingService;
import org.fenixedu.a3es.domain.DegreeFile;
import org.fenixedu.a3es.domain.TeacherActivity;
import org.fenixedu.a3es.domain.TeacherFile;
import org.json.simple.JSONArray;

import com.google.common.base.Strings;

public class TeacherFileBean implements Serializable {

    DegreeFile degreeFile;
    TeacherFile teacherFile;

    String fileName;
    String teacherName;
    String organicUnit;
    String researchUnit;
    A3esTeacherCategory a3esTeacherCategory;
    A3esDegreeType a3esDegreeType;
    String degreeScientificArea;
    Integer degreeYear;
    String degreeInstitution;
    Integer regime;

    List<A3esQualificationBean> a3esQualificationBeanSet;
    List<ActivityBean> scientificActivitySet;
    List<ActivityBean> developmentActivitySet;
    List<ActivityBean> otherPublicationActivitySet;
    List<ActivityBean> otherProfessionalActivitySet;
    List<A3esTeachingServiceBean> a3esTeachingServiceBeanSet;
    public static ScientificPublication scientificPublication = null;

    public TeacherFileBean() {
    }

    public TeacherFileBean(TeacherFile teacherFile) {
        setTeacherFile(teacherFile);
        setFileName(teacherFile.getFileName());
        setTeacherName(teacherFile.getTeacherName());
        setOrganicUnit(teacherFile.getOrganicUnit());
        setResearchUnit(teacherFile.getResearchUnit());
        setA3esTeacherCategory(teacherFile.getA3esTeacherCategory());
        setA3esDegreeType(teacherFile.getA3esDegreeType());
        setDegreeScientificArea(teacherFile.getDegreeScientificArea());
        setDegreeYear(teacherFile.getDegreeYear());
        setDegreeInstitution(teacherFile.getDegreeInstitution());
        setRegime(teacherFile.getRegime());

        setA3esQualificationBeanSet(teacherFile.getA3esQualifications().stream().map(q -> new A3esQualificationBean(q))
                .filter(Objects::nonNull).collect(Collectors.toList()));

        setScientificActivitySet(teacherFile.getScientificActivitySet().stream().sorted().map(a -> new ActivityBean(a))
                .filter(Objects::nonNull).collect(Collectors.toList()));
        setDevelopmentActivitySet(teacherFile.getDevelopmentActivitySet().stream().map(a -> new ActivityBean(a))
                .filter(Objects::nonNull).collect(Collectors.toList()));
        setOtherPublicationActivitySet(teacherFile.getOtherPublicationActivitySet().stream().map(a -> new ActivityBean(a))
                .filter(Objects::nonNull).collect(Collectors.toList()));
        setOtherProfessionalActivitySet(teacherFile.getOtherProfessionalActivitySet().stream().map(a -> new ActivityBean(a))
                .filter(Objects::nonNull).collect(Collectors.toList()));

        setA3esTeachingServiceBeanSet(teacherFile.getA3esTeachingServiceSet().stream().map(ts -> new A3esTeachingServiceBean(ts))
                .filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public DegreeFile getDegreeFile() {
        return degreeFile;
    }

    public void setDegreeFile(DegreeFile degreeFile) {
        this.degreeFile = degreeFile;
    }

    public TeacherFile getTeacherFile() {
        return teacherFile;
    }

    public void setTeacherFile(TeacherFile teacherFile) {
        this.teacherFile = teacherFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public A3esDegreeType getA3esDegreeType() {
        return a3esDegreeType;
    }

    public void setA3esDegreeType(A3esDegreeType a3esDegreeType) {
        this.a3esDegreeType = a3esDegreeType;
    }

    public String getDegreeScientificArea() {
        return degreeScientificArea;
    }

    public void setDegreeScientificArea(String degreeScientificArea) {
        this.degreeScientificArea = degreeScientificArea;
    }

    public Integer getDegreeYear() {
        return degreeYear;
    }

    public void setDegreeYear(Integer degreeYear) {
        this.degreeYear = degreeYear;
    }

    public String getDegreeInstitution() {
        return degreeInstitution;
    }

    public void setDegreeInstitution(String degreeInstitution) {
        this.degreeInstitution = degreeInstitution;
    }

    public List<ActivityBean> getScientificActivitySet() {
        return scientificActivitySet;
    }

    public void setScientificActivitySet(List<ActivityBean> scientificActivitySet) {
        this.scientificActivitySet = scientificActivitySet;
        for (int i = scientificActivitySet.size(); i < TeacherFile.MAXIMUM_ACTIVITIES_NUMBER; i++) {
            this.scientificActivitySet.add(new ActivityBean());
        }
    }

    public List<ActivityBean> getDevelopmentActivitySet() {
        return developmentActivitySet;
    }

    public void setDevelopmentActivitySet(List<ActivityBean> developmentActivitySet) {
        this.developmentActivitySet = developmentActivitySet;
        for (int i = developmentActivitySet.size(); i < TeacherFile.MAXIMUM_ACTIVITIES_NUMBER; i++) {
            this.developmentActivitySet.add(new ActivityBean());
        }
    }

    public List<ActivityBean> getOtherPublicationActivitySet() {
        return otherPublicationActivitySet;
    }

    public void setOtherPublicationActivitySet(List<ActivityBean> otherPublicationActivitySet) {
        this.otherPublicationActivitySet = otherPublicationActivitySet;
        for (int i = otherPublicationActivitySet.size(); i < TeacherFile.MAXIMUM_ACTIVITIES_NUMBER; i++) {
            this.otherPublicationActivitySet.add(new ActivityBean());
        }
    }

    public List<ActivityBean> getOtherProfessionalActivitySet() {
        return otherProfessionalActivitySet;
    }

    public void setOtherProfessionalActivitySet(List<ActivityBean> otherProfessionalActivitySet) {
        this.otherProfessionalActivitySet = otherProfessionalActivitySet;
        for (int i = otherProfessionalActivitySet.size(); i < TeacherFile.MAXIMUM_ACTIVITIES_NUMBER; i++) {
            this.otherProfessionalActivitySet.add(new ActivityBean());
        }
    }

    public String getOrganicUnit() {
        return organicUnit;
    }

    public void setOrganicUnit(String organicUnit) {
        this.organicUnit = organicUnit;
    }

    public String getResearchUnit() {
        return researchUnit;
    }

    public void setResearchUnit(String researchUnit) {
        this.researchUnit = researchUnit;
    }

    public A3esTeacherCategory getA3esTeacherCategory() {
        return a3esTeacherCategory;
    }

    public void setA3esTeacherCategory(A3esTeacherCategory a3esTeacherCategory) {
        this.a3esTeacherCategory = a3esTeacherCategory;
    }

    public Integer getRegime() {
        return regime;
    }

    public void setRegime(Integer regime) {
        this.regime = regime;
    }

    public List<A3esQualificationBean> getA3esQualificationBeanSet() {
        return a3esQualificationBeanSet;
    }

    public void setA3esQualificationBeanSet(List<A3esQualificationBean> a3esQualificationBeanSet) {
        this.a3esQualificationBeanSet = a3esQualificationBeanSet;
        for (int i = a3esQualificationBeanSet.size(); i < TeacherFile.MAXIMUM_QUALIFICATION_NUMBER; i++) {
            this.a3esQualificationBeanSet.add(new A3esQualificationBean());
        }
    }

    public List<A3esTeachingServiceBean> getA3esTeachingServiceBeanSet() {
        return a3esTeachingServiceBeanSet;
    }

    public void setA3esTeachingServiceBeanSet(List<A3esTeachingServiceBean> a3esTeachingServiceBeanSet) {
        this.a3esTeachingServiceBeanSet = a3esTeachingServiceBeanSet;
        for (int i = a3esTeachingServiceBeanSet.size(); i < TeacherFile.MAXIMUM_TEACHING_SERVICE_NUMBER; i++) {
            this.a3esTeachingServiceBeanSet.add(new A3esTeachingServiceBean());
        }
    }

    public class A3esQualificationBean implements Serializable {
        protected String degree;
        protected Integer year;
        protected String area;
        protected String institution;
        protected String classification;
        protected A3esQualification a3esQualification;

        public A3esQualificationBean() {
        }

        public A3esQualificationBean(A3esQualification a3esQualification) {
            setA3esQualification(a3esQualification);
            setDegree(a3esQualification.getDegree());
            setYear(a3esQualification.getYear());
            setArea(a3esQualification.getArea());
            setInstitution(a3esQualification.getInstitution());
            setClassification(a3esQualification.getClassification());
        }

        public A3esQualification getA3esQualification() {
            return a3esQualification;
        }

        public void setA3esQualification(A3esQualification a3esQualification) {
            this.a3esQualification = a3esQualification;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getInstitution() {
            return institution;
        }

        public void setInstitution(String institution) {
            this.institution = institution;
        }

        public String getClassification() {
            return classification;
        }

        public void setClassification(String classification) {
            this.classification = classification;
        }

        public boolean isEmpty() {
            return year == null && Strings.isNullOrEmpty(degree) && Strings.isNullOrEmpty(area)
                    && Strings.isNullOrEmpty(institution) && Strings.isNullOrEmpty(classification);
        }
    }

    public class ActivityBean implements Serializable {
        protected TeacherActivity teacherActivity;
        protected String activity;

        public ActivityBean() {
        }

        public ActivityBean(TeacherActivity teacherActivity) {
            this.teacherActivity = teacherActivity;
            this.activity = teacherActivity.getActivity();
        }

        public TeacherActivity getTeacherActivity() {
            return teacherActivity;
        }

        public void setTeacherActivity(TeacherActivity teacherActivity) {
            this.teacherActivity = teacherActivity;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

    }

    public class A3esTeachingServiceBean {
        protected A3esTeachingService a3esTeachingService;
        protected String curricularUnitName;
        protected String studyCycles;
        protected String methodologyTypes;
        protected Float totalHours;

        public A3esTeachingServiceBean() {
        }

        public A3esTeachingServiceBean(A3esTeachingService a3esTeachingService) {
            setA3esTeachingService(a3esTeachingService);
            setCurricularUnitName(a3esTeachingService.getCurricularUnitName());
            setStudyCycles(a3esTeachingService.getStudyCycles());
            setMethodologyTypes(a3esTeachingService.getMethodologyTypes());
            setTotalHours(a3esTeachingService.getTotalHours());
        }

        public A3esTeachingService getA3esTeachingService() {
            return a3esTeachingService;
        }

        public void setA3esTeachingService(A3esTeachingService a3esTeachingService) {
            this.a3esTeachingService = a3esTeachingService;
        }

        public String getCurricularUnitName() {
            return curricularUnitName;
        }

        public void setCurricularUnitName(String curricularUnitName) {
            this.curricularUnitName = curricularUnitName;
        }

        public String getStudyCycles() {
            return studyCycles;
        }

        public void setStudyCycles(String studyCycles) {
            this.studyCycles = studyCycles;
        }

        public String getMethodologyTypes() {
            return methodologyTypes;
        }

        public void setMethodologyTypes(String methodologyTypes) {
            this.methodologyTypes = methodologyTypes;
        }

        public Float getTotalHours() {
            return totalHours;
        }

        public void setTotalHours(Float totalHours) {
            this.totalHours = totalHours;
        }

        public boolean isEmpty() {
            return Strings.isNullOrEmpty(curricularUnitName) && Strings.isNullOrEmpty(studyCycles)
                    && Strings.isNullOrEmpty(methodologyTypes) && totalHours == null;
        }
    }

    public String getScientificPublicationJson() {
        JSONArray obj = new JSONArray();
        if (getTeacherFile().getUser() != null && scientificPublication != null) {
            obj.addAll(scientificPublication.getScientificPublicationSet(getTeacherFile().getUser()));
        }
        return obj.toJSONString();
    }
}
