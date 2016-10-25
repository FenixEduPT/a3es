<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${portal.toolkit()}

<div class="page-header">
	<h1><spring:message code="label.add.responsible" /> <small><c:out value="${form.a3esFile.fileName}"/> (<c:out value="${form.a3esFile.accreditationProcess.processName}"/>)</small></h1>
</div>

<c:if test="${not empty error}">
	<div class="alert alert-danger">
		<c:out value="${error}"/>
	</div>
</c:if>

<spring:url var="addUrl" value="/accreditationProcess/addResponsible"></spring:url>
<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal" action="${addUrl}">
	<input type="hidden" name="a3esFile" value="<c:out value='${form.a3esFile.externalId}'/>"/>
	<input type="hidden" name="degreeFile" value="<c:out value='${form.degreeFile.externalId}'/>"/>
	<div class="form-group">
		<label for="username" class="col-sm-1 control-label"><spring:message code="label.username" /></label>
		<div class="col-sm-11">
			<input id="responsible" name="responsible" bennu-user-autocomplete class="form-control col-sm-11 user-search" required  value=""/>
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-push-1 col-sm-11">
			<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/showFiles/${form.a3esFile.externalId}?degreeFile=${form.degreeFile.externalId}"><i class="glyphicon glyphicon-chevron-left"></i> <spring:message code="label.back"/></a>
			<button type="submit" class="btn btn-primary" id="form">
				<i class="glyphicon glyphicon-plus-sign"></i> <spring:message code="label.add.responsible" />
			</button>
		</div>
	</div>
</form:form>

<hr />

<c:if test="${!empty form.a3esFile.responsibleGroup.members}">
	<table class="table table-condensed">
		<thead>
			<tr>
				<th><spring:message code="label.user"/></th>
				<th><spring:message code="label.name"/></th>
				<th />
			</tr>
		</thead>
		<tbody>
		<c:forEach var="user" items="${form.a3esFile.responsibleGroup.members}">
				<tr>
					<td><c:out value="${user.username}"/></td>
					<td><c:out value="${user.name}"/></td>
					<td><a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/removeResponsible/${form.a3esFile.externalId}/${user.externalId}?degreeFile=${form.degreeFile.externalId}"><i class="glyphicon glyphicon-remove"></i> <spring:message code="label.remove"/></a></td>
				</tr>
		</c:forEach>
		</tbody>
	</table>
</c:if>