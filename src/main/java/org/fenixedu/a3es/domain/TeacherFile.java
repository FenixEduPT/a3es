package org.fenixedu.a3es.domain;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.a3es.domain.exception.A3esDomainException;
import org.fenixedu.academic.domain.TeacherAuthorization;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

import com.google.common.base.Strings;

public class TeacherFile extends TeacherFile_Base {

    public static final int MAXIMUM_QUALIFICATION_NUMBER = 3;
    public static final int MAXIMUM_ACTIVITIES_NUMBER = 5;
    public static final int MAXIMUM_TEACHING_SERVICE_NUMBER = 10;

    protected TeacherFile(String fileName) {
        super();
        Objects.requireNonNull(fileName);
        setFileName(fileName);
        setTeacherName(fileName);
        setInstitution(UniversityUnit.getInstitutionsUniversityUnit().getName());
        setOrganicUnit(Bennu.getInstance().getInstitutionUnit().getName());
    }

    public static TeacherFile create(String fileName, A3esFile a3esFile) {
        Objects.requireNonNull(a3esFile);
        TeacherFile teacherFile = a3esFile.getAccreditationProcess().getTeacherFileSet().stream()
                .filter(tf -> tf.getPresentationName().equals(fileName)).findAny().orElse(null);
        if (teacherFile != null) {
            throw new A3esDomainException("error.teacher.name.already.exists.in.process");
        }
        teacherFile = new TeacherFile(fileName);
        teacherFile.addA3esFile(a3esFile);
        return teacherFile;
    }

    public static TeacherFile create(User user, A3esFile a3esFile) {
        Objects.requireNonNull(a3esFile);
        TeacherFile teacherFile = a3esFile.getAccreditationProcess().getTeacherFileSet().stream()
                .filter(tf -> tf.getUser() != null && tf.getUser().equals(user)).findAny().orElse(null);
        if (teacherFile == null) {
            teacherFile = new TeacherFile(user.getPerson().getName());
            teacherFile.setUser(user);
            teacherFile.addResponsible(user);
            TeacherAuthorization teacherAuthorization = user.getPerson().getTeacher()
                    .getLatestTeacherAuthorizationInInterval(
                            a3esFile.getAccreditationProcess().getExecutionYear().getAcademicInterval().toInterval())
                    .orElse(null);
            if (teacherAuthorization != null) {
                teacherFile.setA3esTeacherCategory(teacherAuthorization.getTeacherCategory().getA3esTeacherCategory());
            }
            teacherFile.prefill();
        }
        teacherFile.addA3esFile(a3esFile);
        return teacherFile;
    }

    private void prefill() {
        if (getUser() != null) {
            TeacherFile lastTeacherFile =
                    getUser().getTeacherFileSet().stream()
                            .filter(f -> !f.equals(this)).sorted(Comparator
                                    .comparing((TeacherFile f) -> f.getAccreditationProcess().getEndFillingPeriod()).reversed())
                    .findFirst().orElse(null);
            if (lastTeacherFile != null) {
                setA3esDegreeType(lastTeacherFile.getA3esDegreeType());
                setResearchUnit(lastTeacherFile.getResearchUnit());
                setDegreeScientificArea(lastTeacherFile.getDegreeScientificArea());
                setDegreeYear(lastTeacherFile.getDegreeYear());
                setDegreeInstitution(lastTeacherFile.getDegreeInstitution());
                lastTeacherFile.getA3esQualificationSet().forEach(qualification -> {
                    A3esQualification.create(this, qualification.getDegree(), qualification.getYear(), qualification.getArea(),
                            qualification.getInstitution(), qualification.getClassification());
                });
                lastTeacherFile.getScientificActivitySet()
                        .forEach(publication -> ScientificActivity.create(this, publication.getActivity()));
                lastTeacherFile.getDevelopmentActivitySet()
                        .forEach(publication -> DevelopmentActivity.create(this, publication.getActivity()));
                lastTeacherFile.getOtherPublicationActivitySet()
                        .forEach(publication -> OtherPublicationActivity.create(this, publication.getActivity()));
                lastTeacherFile.getOtherProfessionalActivitySet()
                        .forEach(publication -> OtherProfessionalActivity.create(this, publication.getActivity()));
            }
        }
    }

    public void edit(String teacherName, String researchUnit, A3esTeacherCategory a3esTeacherCategory, Integer regime,
            A3esDegreeType a3esDegreeType, String degreeScientificArea, Integer degreeYear, String degreeInstitution) {
        setTeacherName(teacherName);
        setA3esTeacherCategory(a3esTeacherCategory);
        setA3esDegreeType(a3esDegreeType);
        setResearchUnit(researchUnit);
        setRegime(regime);
        setDegreeScientificArea(degreeScientificArea);
        setDegreeYear(degreeYear);
        setDegreeInstitution(degreeInstitution);
    }

    @Override
    public String getPresentationName() {
        StringBuilder result = new StringBuilder();
        result.append(getFileName());
        if (getUser() != null) {
            result.append(" (").append(getUser().getUsername()).append(")");
        }
        return result.toString();
    }

    @Override
    protected void setTeacherName(String teacherName) {
        teacherName = StringUtils.trimToNull(teacherName);
        super.setTeacherName(teacherName);
    }

    public void removeCurricularUnitFile(CurricularUnitFile curricularUnitFile) {
        removeA3esFile(curricularUnitFile);
        delete();
    }

    public void removeDegreeFile(DegreeFile degreeFile) {
        degreeFile.getCurricularUnitFileSet().stream().filter(cuf -> cuf.getTeacherFileSet().contains(this))
                .forEach(cuf -> removeA3esFile(cuf));
        if (degreeFile.getTeacherFileSet().contains(this)) {
            removeA3esFile(degreeFile);
        }
        delete();
    }

    public void delete() {
        if (getA3esFileSet().size() == 0) {
            getA3esQualificationSet().forEach(A3esQualification::delete);
            getScientificActivitySet().forEach(ScientificActivity::delete);
            getDevelopmentActivitySet().forEach(DevelopmentActivity::delete);
            getOtherPublicationActivitySet().forEach(OtherPublicationActivity::delete);
            getOtherProfessionalActivitySet().forEach(OtherProfessionalActivity::delete);
            getA3esTeachingServiceSet().forEach(A3esTeachingService::delete);
            setUser(null);
            setA3esDegreeType(null);
            setA3esTeacherCategory(null);
            setResponsibleGroup(null);
            super.deleteDomainObject();
        }
    }

    @Deprecated
    public void delete(A3esFile a3esFile) {
        removeA3esFile(a3esFile);
        if (getA3esFileSet().size() == 0) {
            getA3esQualificationSet().forEach(A3esQualification::delete);
            getScientificActivitySet().forEach(ScientificActivity::delete);
            getDevelopmentActivitySet().forEach(DevelopmentActivity::delete);
            getOtherPublicationActivitySet().forEach(OtherPublicationActivity::delete);
            getOtherProfessionalActivitySet().forEach(OtherProfessionalActivity::delete);
            getA3esTeachingServiceSet().forEach(A3esTeachingService::delete);
            setUser(null);
            setA3esDegreeType(null);
            setA3esTeacherCategory(null);
            setResponsibleGroup(null);
            super.deleteDomainObject();
        }
    }

    @Override
    public String getTeacherName() {
        return super.getTeacherName();
    }

    @Override
    public String getInstitution() {
        return super.getInstitution();
    }

    @Override
    public String getOrganicUnit() {
        return super.getOrganicUnit();
    }

    @Override
    public String getResearchUnit() {
        return super.getResearchUnit();
    }

    @Override
    public Integer getRegime() {
        return super.getRegime();
    }

    @Override
    public A3esDegreeType getA3esDegreeType() {
        return super.getA3esDegreeType();
    }

    @Override
    public String getDegreeScientificArea() {
        return super.getDegreeScientificArea();
    }

    @Override
    public Integer getDegreeYear() {
        return super.getDegreeYear();
    }

    @Override
    public String getDegreeInstitution() {
        return super.getDegreeInstitution();
    }

    @Override
    public void addA3esQualification(A3esQualification a3esQualification) {
        if (getA3esQualificationSet().size() >= MAXIMUM_QUALIFICATION_NUMBER) {
            throw new A3esDomainException("error.exceded.maximum.qualification.number");
        }
        super.addA3esQualification(a3esQualification);
    }

    @Override
    public void addScientificActivity(ScientificActivity scientificActivity) {
        if (getScientificActivitySet().size() >= MAXIMUM_ACTIVITIES_NUMBER) {
            throw new A3esDomainException("error.exceded.maximum.scientificActivity.number");
        }
        super.addScientificActivity(scientificActivity);
    }

    @Override
    public void addDevelopmentActivity(DevelopmentActivity developmentActivity) {
        if (getDevelopmentActivitySet().size() >= MAXIMUM_ACTIVITIES_NUMBER) {
            throw new A3esDomainException("error.exceded.maximum.developmentActivity.number");
        }
        super.addDevelopmentActivity(developmentActivity);
    }

    @Override
    public void addOtherPublicationActivity(OtherPublicationActivity otherPublicationActivity) {
        if (getOtherPublicationActivitySet().size() >= MAXIMUM_ACTIVITIES_NUMBER) {
            throw new A3esDomainException("error.exceded.maximum.otherPublicationActivity.number");
        }
        super.addOtherPublicationActivity(otherPublicationActivity);
    }

    @Override
    public void addOtherProfessionalActivity(OtherProfessionalActivity otherProfessionalActivity) {
        if (getOtherProfessionalActivitySet().size() >= MAXIMUM_ACTIVITIES_NUMBER) {
            throw new A3esDomainException("error.exceded.maximum.otherProfessionalActivity.number");
        }
        super.addOtherProfessionalActivity(otherProfessionalActivity);
    }

    @Override
    public void addA3esTeachingService(A3esTeachingService a3esTeachingService) {
        if (getA3esTeachingServiceSet().size() >= MAXIMUM_TEACHING_SERVICE_NUMBER) {
            throw new A3esDomainException("error.exceded.maximum.teaching.service.number");
        }
        super.addA3esTeachingService(a3esTeachingService);
    }

    public List<A3esQualification> getA3esQualifications() {
        return super.getA3esQualificationSet().stream().sorted(new Comparator<A3esQualification>() {
            @Override
            public int compare(final A3esQualification o1, final A3esQualification o2) {
                if (o1.getYear() == null && o2.getYear() == null) {
                    return o1.getDegree().compareTo(o2.getDegree());
                } else if (o1.getYear() == null) {
                    return -1;
                } else if (o2.getYear() == null) {
                    return 1;
                }
                return o1.getYear().compareTo(o2.getYear());
            }
        }.reversed()).collect(Collectors.toList());
    }

    public boolean isFilledAndValid() {
        return !Strings.isNullOrEmpty(getTeacherName()) && getA3esTeacherCategory() != null
                && !Strings.isNullOrEmpty(getInstitution()) && !Strings.isNullOrEmpty(getOrganicUnit())
                && getA3esDegreeType() != null && !Strings.isNullOrEmpty(getDegreeScientificArea()) && getDegreeYear() != null
                && !Strings.isNullOrEmpty(getDegreeInstitution()) && getRegime() != null && !getA3esTeachingServiceSet().isEmpty()
                && hasValidSize(getResearchUnit(), 200) && hasValidSize(getDegreeScientificArea(), 200)
                && hasValidSize(getDegreeInstitution(), 200) && hasValidSize(getTeacherName(), 200)
                && getScientificActivitySet().stream().filter(a -> !hasValidSize(a.getActivity(), 500)).count() == 0
                && getDevelopmentActivitySet().stream().filter(a -> !hasValidSize(a.getActivity(), 200)).count() == 0
                && getOtherPublicationActivitySet().stream().filter(a -> !hasValidSize(a.getActivity(), 500)).count() == 0
                && getOtherProfessionalActivitySet().stream().filter(a -> !hasValidSize(a.getActivity(), 200)).count() == 0;
    }

    @Override
    public AccreditationProcess getAccreditationProcess() {
        return getA3esFileSet().iterator().next().getAccreditationProcess();
    }

    @Override
    public boolean isUserAllowedToView() {
        return super.isUserAllowedToView() || getA3esFileSet().stream()
                .filter(af -> (af instanceof DegreeFile && af.isUserAllowedToView())
                        || (af instanceof CurricularUnitFile && ((CurricularUnitFile) af).getDegreeFile().isUserAllowedToView()))
                .count() != 0;
    }
}
