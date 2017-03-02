<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="page-header">
	<h1><spring:message code="label.exportToA3es"/> <small><c:out value="${form.degreeFile.fileName}"/> (<c:out value="${form.degreeFile.accreditationProcess.processName}"/>)</small></h1>
</div>

<spring:url var="exportCurricularUnitFilesUrl" value="/accreditationProcess/exportCurricularUnitFilesToA3es"></spring:url>
<spring:url var="exportTeacherFilesUrl" value="/accreditationProcess/exportTeacherFilesToA3es"></spring:url>

<c:if test="${not empty error}">
	<div class="alert alert-danger">
		<c:out value="${error}"/>
	</div>
</c:if>
<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal">
	${csrf.field()}
	<input type="hidden" name="degreeFile" value="<c:out value='${form.degreeFile.externalId}'/>"/>
	<div class="form-group">
		<label for="user" class="col-sm-2 control-label"><spring:message code="label.user" />:</label>
		<div class="col-sm-10">
			<input id="user" name="user" class="form-control" value="<c:out value='${form.user}'/>" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<label for="password" class="col-sm-2 control-label"><spring:message code="label.password" />:</label>
		<div class="col-sm-10">
			<input id="password" name="password" class="form-control" value="<c:out value='${form.password}'/>" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-push-2 col-sm-10">
			<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/showProcess/${form.degreeFile.accreditationProcess.externalId}"><i class="glyphicon glyphicon-chevron-left"></i> <spring:message code="label.back"/></a>
			<button type="submit" class="btn btn-primary" id="form" onclick='this.form.action="${exportCurricularUnitFilesUrl}";'>
				<i class="glyphicon glyphicon-send"></i> <spring:message code="label.export" /> <spring:message code="label.curricularUnitFiles" />
			</button>
			<button type="submit" class="btn btn-primary" id="form" onclick='this.form.action="${exportTeacherFilesUrl}";'>
				<i class="glyphicon glyphicon-send"></i> <spring:message code="label.export" /> <spring:message code="label.teacherFiles" />
			</button>
		</div>
	</div>
</form:form>

<div>
<c:forEach var="output" items="${output}">
	<div><c:out value="${output}"/></div>
</c:forEach>
</div>