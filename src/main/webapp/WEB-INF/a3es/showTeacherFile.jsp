<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${portal.toolkit()}

<jsp:include page="fileScripts.jsp"/>

<div class="page-header"><h1><spring:message code="label.teacherFile" /> <small><c:out value="${form.fileName}"/></small></h1></div>

<c:if test="${not empty error}">
	<div class="alert alert-danger">
		<c:out value="${error}"/>
	</div>
</c:if>

<spring:url var="backUrl" value="/accreditation"></spring:url>
<c:if test="${form.teacherFile.canBeManageByUser}">
	<spring:url var="backUrl" value="/accreditationProcess/showTeacherFiles/${form.degreeFile.externalId}"></spring:url>	
</c:if>

<a class="btn btn-default" href="${backUrl}"><spring:message code="label.back"/></a>
<hr />

<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal">
	<h2><spring:message code="label.personalData" /></h2>
	<div class="form-group">
		<label for=teacherName class="col-sm-3 control-label"><spring:message code="label.name" />:</label>
		<div class="col-sm-9">
			<input id="teacherName" name="teacherName" class="form-control" value="<c:out value='${form.teacherName}'/>" disabled="disabled" readonly="true"/>
		</div>
	</div>
	<div class="form-group">
		<label for="institution" class="col-sm-3 control-label"><spring:message code="label.higherEducationInstitution" />:</label>
		<div class="col-sm-9">
			<input id="institution" name="institution" class="form-control" value="<c:out value='${form.teacherFile.institution}'/>" disabled="disabled" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group">
		<label for="organicUnit" class="col-sm-3 control-label"><spring:message code="label.organicUnit" />:</label>
		<div class="col-sm-9">
			<input id="organicUnit" name="organicUnit" class="form-control" value="<c:out value='${form.teacherFile.organicUnit}'/>" disabled="disabled" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group">
		<label for="researchUnit" class="col-sm-3 control-label"><spring:message code="label.researchUnitFiliation" />:</label>
		<div class="col-sm-9">
			<input id="researchUnit" name="researchUnit" class="form-control" value="<c:out value='${form.researchUnit}'/>" disabled="disabled" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group">
		<label for="category" class="col-sm-3 control-label"><spring:message code="label.category" />:</label>
		<div class="col-sm-9">
			<input id="a3esTeacherCategory" name="a3esTeacherCategory" class="form-control" value="<c:out value='${form.a3esTeacherCategory.name.content}'/>" disabled="disabled" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group">
		<label for="a3esDegreeType" class="col-sm-3 control-label"><spring:message code="label.degreeType" />:</label>
		<div class="col-sm-9">
			<input id="a3esDegreeType" name="a3esDegreeType" class="form-control" value="<c:out value='${form.a3esDegreeType.localizedName.content}'/>" disabled="disabled" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group">
		<label for="degreeScientificArea" class="col-sm-3 control-label"><spring:message code="label.degreeScientificArea" />:</label>
		<div class="col-sm-9">
			<input id="degreeScientificArea" name="degreeScientificArea" class="form-control" value="<c:out value='${form.degreeScientificArea}'/>" disabled="disabled" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group">
		<label for="degreeYear" class="col-sm-3 control-label"><spring:message code="label.degreeYear" />:</label>
		<div class="col-sm-9">
			<input id="degreeYear" name="degreeYear" class="form-control" value="<c:out value='${form.degreeYear}'/>" disabled="disabled" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group">
		<label for="degreeInstitution" class="col-sm-3 control-label"><spring:message code="label.degreeInstitution" />:</label>
		<div class="col-sm-9">
			<input id="degreeInstitution" name="degreeInstitution" class="form-control" value="<c:out value='${form.degreeInstitution}'/>" disabled="disabled" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group">
		<label for="regime" class="col-sm-3 control-label"><spring:message code="label.regime" />:</label>
		<div class="col-sm-9">
			<input id="regime" name="regime" class="form-control" value="<c:out value='${form.regime}'/>" disabled="disabled" readonly="readonly"/>
		</div>
	</div>
	
	<h2><spring:message code="label.otherAcademicDegreesOrTitle" /></h2>
	<table class="table table-condensed">
		<thead>
			<tr>
				<th class="col-sm-1"><spring:message code="label.year"/></th>
				<th class="col-sm-2"><spring:message code="label.degreeType"/></th>
				<th class="col-sm-2"><spring:message code="label.area"/></th>
				<th class="col-sm-5"><spring:message code="label.institution"/></th>
				<th class="col-sm-1"><spring:message code="label.classification"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="qualification" items="${form.a3esQualificationBeanSet}" varStatus="status">
				<tr>
					<td><input id="a3esQualificationBeanSet[${status.index}].year" class="form-control" value="${qualification.year}" disabled="disabled" readonly="readonly"/></td>
					<td><input id="a3esQualificationBeanSet[${status.index}].degree" class="form-control" value="${qualification.degree}" disabled="disabled" readonly="readonly"/></td>
					<td><input id="a3esQualificationBeanSet[${status.index}].area" class="form-control" value="${qualification.area}" disabled="disabled" readonly="readonly"/></td>
					<td><input id="a3esQualificationBeanSet[${status.index}].institution" class="form-control" value="${qualification.institution}" disabled="disabled" readonly="readonly"/></td>
					<td><input id="a3esQualificationBeanSet[${status.index}].classification" class="form-control" value="${qualification.classification}" disabled="disabled" readonly="readonly"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	
	<h2><spring:message code="label.scientificActivity" /> <small><spring:message code="label.scientificActivity.message" /></small></h2>
	<table class="table table-condensed">
		<tbody>
		<c:forEach var="teacherActivity" items="${form.scientificActivitySet}" varStatus="status">
				<tr><td><textarea id="scientificActivitySet${status.index}.activity" name="scientificActivitySet[${status.index}].activity" class="form-control" rows="2" disabled="disabled" readonly="readonly"><c:out value='${teacherActivity.activity}'/></textarea></td></tr>
		</c:forEach></tbody>
	</table>
	<h2><spring:message code="label.developmentActivity" /> <small><spring:message code="label.developmentActivity.message" /></small></h2>
	<table class="table table-condensed">
		<tbody><c:forEach var="teacherActivity" items="${form.developmentActivitySet}" varStatus="status">
				<tr><td><textarea id="developmentActivitySet" name="developmentActivitySet[${status.index}].activity" class="form-control" rows="2" disabled="disabled" readonly="readonly"><c:out value='${teacherActivity.activity}'/></textarea></td></tr>
		</c:forEach></tbody>
	</table>
	<h2><spring:message code="label.otherPublicationActivity" /> <small><spring:message code="label.otherPublicationActivity.message" /></small></h2>
	<table class="table table-condensed">
		<tbody><c:forEach var="teacherActivity" items="${form.otherPublicationActivitySet}" varStatus="status">
				<tr><td><textarea id="otherPublicationActivitySet" name="otherPublicationActivitySet[${status.index}].activity" class="form-control" rows="2" disabled="disabled" readonly="readonly"><c:out value='${teacherActivity.activity}'/></textarea></td></tr>
		</c:forEach></tbody>
	</table>
	
	<h2><spring:message code="label.otherProfessionalActivity" /> <small><spring:message code="label.otherProfessionalActivity.message" /></small></h2>
	<table class="table table-condensed">
		<tbody><c:forEach var="teacherActivity" items="${form.otherProfessionalActivitySet}" varStatus="status">
				<tr><td><textarea id="otherProfessionalActivitySet" name="otherProfessionalActivitySet[${status.index}].activity" class="form-control" rows="2" disabled="disabled" readonly="readonly"><c:out value='${teacherActivity.activity}'/></textarea></td></tr>
		</c:forEach></tbody>
	</table>
	
	<spring:eval expression="T(org.fenixedu.a3es.domain.MethodologyType).values()" var="methodologyTypes" />
	<h2><spring:message code="label.teachingServiceAllocation" /> <small><c:forEach var="methodologyType" items="${methodologyTypes}"> (<c:out value="${methodologyType.sigla}"/>)<c:out value="${methodologyType.localizedName.content}"/></c:forEach></small></h2>
	<table class="table table-condensed">
		<thead>
			<tr>
				<th class="col-sm-6"><spring:message code="label.curricularUnit"/></th>
				<th class="col-sm-4"><spring:message code="label.studyCycle"/></th>
				<th class="col-sm-1"><spring:message code="label.type"/></th>
				<th class="col-sm-1"><spring:message code="label.totalContactHours"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="teachingService" items="${form.a3esTeachingServiceBeanSet}" varStatus="status">
				<tr>
					<td><input id="a3esTeachingServiceBeanSet[${status.index}].curricularUnitName" class="form-control" value="${teachingService.curricularUnitName}" disabled="disabled" readonly="readonly"/></td>
					<td><input id="a3esTeachingServiceBeanSet[${status.index}].studyCycles" class="form-control" value="${teachingService.studyCycles}" disabled="disabled" readonly="readonly"/></td>
					<td><input id="a3esTeachingServiceBeanSet[${status.index}].methodologyTypes" class="form-control" value="${teachingService.methodologyTypes}" disabled="disabled" readonly="readonly"/></td>
					<td><input id="a3esTeachingServiceBeanSet[${status.index}].totalHours" class="form-control" value="${teachingService.totalHours}" disabled="disabled" readonly="readonly"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</form:form>