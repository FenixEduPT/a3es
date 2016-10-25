<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${portal.toolkit()}

<c:if test="${not empty error}">
	<div class="alert alert-danger">
		<c:out value="${error}"/>
	</div>
</c:if>

<spring:url var="editUrl" value="/accreditationProcess/createTeacherFile"></spring:url>
<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal" action="${editUrl}">
	<input type="hidden" name="degreeFile" value="<c:out value='${form.degreeFile.externalId}'/>"/>
	<div class="form-group">
		<label for="fileName" class="col-sm-2 control-label"><spring:message code="label.name" />:</label>
		<div class="col-sm-10">
			<input id="fileName" name="fileName" class="form-control" value="<c:out value='${form.fileName}'/>" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-push-2 col-sm-10">
			<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/showTeacherFiles/${form.degreeFile.externalId}"><i class="glyphicon glyphicon-chevron-left"></i> <spring:message code="label.back"/></a>
			<button type="submit" class="btn btn-default" id="form">
				<spring:message code="label.create" />
			</button>
		</div>
	</div>
</form:form>