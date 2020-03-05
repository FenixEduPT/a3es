<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${portal.toolkit()}

<div class="page-header">
	<h1><spring:message code="title.a3es.accreditation.processes"/> <small><c:out value="${form.degreeFile.accreditationProcess.processName}"/></small></h1>
</div>

<c:if test="${not empty error}">
	<div class="alert alert-danger">
		<c:out value="${error}"/>
	</div>
</c:if>

<spring:url var="editUrl" value="/accreditationProcess/editDegreeFile"></spring:url>
<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal" action="${editUrl}">
	${csrf.field()}
	<input type="hidden" name="degreeFile" value="<c:out value='${form.degreeFile.externalId}'/>"/>
	<div class="form-group">
		<label for="fileName" class="col-sm-2 control-label"><spring:message code="label.degree" /></label>
		<div class="col-sm-10">
			<input id="fileName" name="fileName" class="form-control" value="<c:out value='${form.fileName}'/>"/>
		</div>
	</div>
	<div class="form-group">
		<label for="degreeAcronym" class="col-sm-2 control-label"><spring:message code="label.degreeAcronym" /></label>
		<div class="col-sm-10">
			<input id="degreeAcronym" name="degreeAcronym" class="form-control" value="<c:out value='${form.degreeAcronym}'/>"/>
		</div>
	</div>
	<div class="form-group">
		<label for="degreeCode" class="col-sm-2 control-label"><spring:message code="label.degreeCode" /></label>
		<div class="col-sm-10">
			<input id="degreeCode" name="degreeCode" class="form-control" value="<c:out value='${form.degreeCode}'/>"/>
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-push-2 col-sm-10">
			<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/degreesFiles/${form.degreeFile.accreditationProcess.externalId}"><i class="glyphicon glyphicon-chevron-left"></i> <spring:message code="label.back"/></a>
			<button type="submit" class="btn btn-primary" id="form">
				<i class="glyphicon glyphicon-floppy-disk"></i> <spring:message code="label.save" />
			</button>
		</div>
	</div>
</form:form>