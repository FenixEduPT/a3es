<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${portal.toolkit()}

<jsp:include page="fileScripts.jsp"/>

<div class="page-header"><h1><spring:message code="label.curricularUnitFile"/> <small><c:out value="${form.curricularUnitName.content}"/></small></h1></div>

<c:if test="${not empty error}">
	<div class="alert alert-danger">
		<c:out value="${error}"/>
	</div>
</c:if>

<spring:url var="curricularUnitFilesUrl" value="/accreditation"></spring:url>
<spring:url var="editUrl" value="/accreditation/editCurricularUnitFile"></spring:url>
<c:if test="${form.curricularUnitFile.canBeManageByUser}">
	<spring:url var="curricularUnitFilesUrl" value="/accreditationProcess/showCurricularUnitFiles/${form.curricularUnitFile.degreeFile.externalId}"></spring:url>
	<spring:url var="editUrl" value="/accreditationProcess/editCurricularUnitFile"></spring:url>
</c:if>


<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal" action="${editUrl}">
	${csrf.field()}
	<input type="hidden" name="curricularUnitFile" value="<c:out value='${form.curricularUnitFile.externalId}'/>"/>
	<input type="hidden" name="degreeFile" value="<c:out value='${form.degreeFile.externalId}'/>"/>
	<hr />
	<div class="form-group">
		<label for="curricularUnitName" class="col-sm-12"><spring:message code="label.curricularUnit" />:</label>
		<div class="col-sm-12">
			<input id="curricularUnitName" bennu-localized-string  name="curricularUnitName" class="form-control" value="<c:out value='${form.curricularUnitName.json()}'/>" readonly="readonly"/>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="scientificArea" class="col-sm-12"><spring:message code="label.scientificAreaAcronym" />:</label>
		<div class="col-sm-12">
			<input id="scientificArea" name="scientificArea" class="form-control limitedSizeString" value="<c:out value='${form.scientificArea}'/>" oninput="validateStringSize(this, 100)"/>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="courseRegime" class="col-sm-12"><spring:message code="label.courseRegime" />:</label>
		<div class="col-sm-12">
			<input id="courseRegime" name="courseRegime" class="form-control limitedSizeString" value="<c:out value='${form.courseRegime}'/>" oninput="validateStringSize(this, 100)"/>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="workingHours" class="col-sm-12"><spring:message code="label.workingHours" />:</label>
		<div class="col-sm-12">
			<input id="workingHours" name="workingHours" class="form-control limitedSizeString" value="<c:out value='${form.workingHours}'/>" oninput="validateStringSize(this, 100)"/>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="contactHours" class="col-sm-12"><spring:message code="label.contactHours" />:</label>
		<div class="col-sm-12">
			<input id="contactHours" name="contactHours" class="form-control limitedSizeString" value="<c:out value='${form.contactHours}'/>" oninput="validateStringSize(this, 100)"/>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="courseLoadPerType" class="col-sm-12"><spring:message code="label.courseLoadPerType" />:</label>
		<div class="col-sm-12">
			<input id="courseLoadPerType" name="courseLoadPerType" class="form-control limitedSizeString" value="<c:out value='${form.courseLoadPerType}'/>" oninput="validateStringSize(this, 100)"/>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="ects" class="col-sm-12"><spring:message code="label.ects" />:</label>
		<div class="col-sm-12">
			<input id="ects" name="ects" class="form-control limitedSizeString" value="<c:out value='${form.ects}'/>" oninput="validateStringSize(this, 100)"/>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="observations" class="col-sm-12"><spring:message code="label.observations" />:</label>
		<div class="col-sm-12 ">
			<textarea class="limitedSizeString"  bennu-localized-string name="observations" id="observations"  onchange="validateLocalizedStringSize(this, 1000)"><c:out value="${form.observations.json()}"/></textarea>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="responsibleTeacherAndTeachingHours" class="col-sm-12"><spring:message code="label.responsibleTeacherAndTeachingHours" />:</label>
		<div class="col-sm-12">
			<input id="responsibleTeacherAndTeachingHours" name="responsibleTeacherAndTeachingHours" class="form-control mandatoryString" value="<c:out value='${form.responsibleTeacherAndTeachingHours}'/>" required="required" oninput="validateStringSize(this, 100)"/>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="otherTeachersAndTeachingHours" class="col-sm-12"><spring:message code="label.otherTeachersAndTeachingHours" />:</label>
		<div class="col-sm-12">
			<form:textarea id="otherTeachersAndTeachingHours" path="otherTeachersAndTeachingHours" rows="5" class="form-control limitedSizeString" value="<c:out value='${form.otherTeachersAndTeachingHours}'/>" oninput="validateStringSize(this, 1000)"/>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="learningOutcomes" class="col-sm-12"><spring:message code="label.learningOutcomes" />:</label>
		<div class="col-sm-12 ">
			<textarea class="mandatoryLocalizedString" bennu-localized-string name="learningOutcomes" id="learningOutcomes"  onchange="validateLocalizedStringSize(this, 1000)"><c:out value="${form.learningOutcomes.json()}"/></textarea>
		</div>
	</div>
	<hr />	
	<div class="form-group">
		<label for="syllabus" class="col-sm-12"><spring:message code="label.syllabus" />:</label>
		<div class="col-sm-12">
			<textarea class="mandatoryLocalizedString" bennu-localized-string name="syllabus" id="syllabus"  onchange="validateLocalizedStringSize(this, 1000)"><c:out value="${form.syllabus.json()}" /></textarea>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="syllabusDemonstration" class="col-sm-12"><spring:message code="label.syllabusDemonstration" />:</label>
		<div class="col-sm-12">
			<textarea class="mandatoryLocalizedString" bennu-localized-string name="syllabusDemonstration" id="syllabusDemonstration" class="form-control" onchange="validateLocalizedStringSize(this, 1000)"><c:out value="${form.syllabusDemonstration.json()}"/></textarea>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="teachingMethodologies" class="col-sm-12"><spring:message code="label.teachingMethodologies" />:</label>
		<div class="col-sm-12">
			<textarea class="mandatoryLocalizedString" bennu-localized-string name="teachingMethodologies" id="teachingMethodologies" class="form-control" onchange="validateLocalizedStringSize(this, 1000)"><c:out value="${form.teachingMethodologies.json()}"/></textarea>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="teachingMethodologiesDemonstration" class="col-sm-12"><spring:message code="label.teachingMethodologiesDemonstration" />:</label>
		<div class="col-sm-12">
			<textarea class="mandatoryLocalizedString" bennu-localized-string name="teachingMethodologiesDemonstration" id="teachingMethodologiesDemonstration" class="form-control" onchange="validateLocalizedStringSize(this, 3000)"><c:out value="${form.teachingMethodologiesDemonstration.json()}"/></textarea>
		</div>
	</div>
	<hr />
	<div class="form-group">
		<label for="bibliographicReferences" class="col-sm-12"><spring:message code="label.bibliographicReferences" />:</label>
		<div class="col-sm-12">
			<form:textarea id="bibliographicReferences" path="bibliographicReferences" rows="5" class="form-control mandatoryString" oninput="validateStringSize(this, 1000)" value='<c:out value="${form.bibliographicReferences}"/>' required="required"/>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-12">
			<a class="btn btn-default" href="${curricularUnitFilesUrl}"><spring:message code="label.cancel"/></a>
			<button type="submit" class="btn btn-primary" id="form"><i class="glyphicon glyphicon-floppy-disk"></i> <spring:message code="label.save" /></button>
		</div>
	</div>
</form:form>
