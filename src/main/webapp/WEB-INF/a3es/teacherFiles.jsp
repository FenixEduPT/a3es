<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="page-header">
	<h1><spring:message code="label.teacherFiles"/> <small><c:out value="${degreeFile.fileName}"/> (<c:out value="${degreeFile.accreditationProcess.processName}"/>)</small></h1>
</div>

<spring:eval expression="T(org.fenixedu.a3es.domain.AccreditationProcess).getCanBeManageByUser()" var="canBeManageByUser" />
<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/showProcess/${degreeFile.accreditationProcess.externalId}"><i class="glyphicon glyphicon-chevron-left"></i> <spring:message code="label.back"/></a>
<c:if test="${canBeManageByUser && !process.isFillingPeriodOver() && degreeFile!=null}">
	<spring:url var="createUrl" value="/accreditationProcess/createTeacherFile/${degreeFile.externalId}"></spring:url>
	<a class="btn btn-primary" href="${createUrl}"><i class="glyphicon glyphicon-plus-sign"></i> <spring:message code="label.create.file"/></a>
</c:if>
<hr/>

<c:set var="files" value="${degreeFile.teacherFileSet}"/>
<%@include file="a3esFiles.jsp" %>