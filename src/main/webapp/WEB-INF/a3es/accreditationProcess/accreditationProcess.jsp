<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="createUrl" value="/accreditationProcess/createProcess"></spring:url>


<div class="page-header">
	<h1><spring:message code="title.a3es.accreditation.processes"/> <small><c:out value="${process.processName}"/></small></h1>
</div>

<div>
<c:forEach var="output" items="${outputList}">
	<div><c:out value="${output}"/></div>
</c:forEach>
</div>

<div>
	<div class="row">
		<label class="col-sm-2 text-right"><spring:message code="label.name"/>:</label>
		<div class="col-sm-10"><c:out value="${process.processName}"/></div>
	</div>
	<div class="row">
		<label class="col-sm-2 text-right"><spring:message code="label.beginDate"/>:</label>
		<div class="col-sm-10"><c:out value="${process.beginFillingPeriod.toString('dd/MM/yyyy HH:mm')}"/></div>
	</div>
	<div class="row">
		<label class="col-sm-2 text-right"><spring:message code="label.endDate"/>:</label>
		<div class="col-sm-10"><c:out value="${process.endFillingPeriod.toString('dd/MM/yyyy HH:mm')}"/></div>
	</div>
	<div class="row">
		<label class="col-sm-2 text-right"><spring:message code="label.executionYear"/>:</label>
		<div class="col-sm-10"><c:out value="${process.executionYear.qualifiedName}"/></div>
	</div>
	<div class="row">
		<label class="col-sm-2 text-right"><spring:message code="label.teacherFiles"/>:</label>
		<div class="col-sm-10"><c:out value="${process.completedTeacherFileSet.size()}"/> de <c:out value="${process.teacherFileSet.size()}"/></div>
	</div>	
	<div class="row">
		<label class="col-sm-2 text-right"><spring:message code="label.curricularUnitFiles"/>:</label>
		<div class="col-sm-10"><c:out value="${process.completedCurricularUnitFileSet.size()}"/> de <c:out value="${process.curricularUnitFileSet.size()}"/></div>
	</div>
	<div class="row">
		<div class="col-sm-10 col-sm-offset-2">
			<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/degreesFiles/${process.externalId}"><i class="glyphicon glyphicon-edit"></i> <spring:message code="label.degrees"/></a>
			<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/exportProcessFiles/${process.externalId}"><i class="glyphicon glyphicon-edit"></i> <spring:message code="label.export"/></a>
		</div>
	</div>
</div>
<hr />
<table class="table table-condensed">
	<thead>
		<tr>
			<th><spring:message code="label.degree"/></th>
			<th><spring:message code="label.degreeCode"/></th>
			<th><spring:message code="label.curricularUnitFiles"/></th>
			<th><spring:message code="label.teacherFiles"/></th>
			<th/>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="degreeFile" items="${process.degreeFileSet}">
		<tr>
			<td><c:out value="${degreeFile.fileName}"/></td>
			<td><c:out value="${degreeFile.degreeCode}"/></td>
			<td><c:out value="${degreeFile.completedCurricularUnitFileSet.size()}"/> de <c:out value="${degreeFile.curricularUnitFileSet.size()}"/></td>
			<td><c:out value="${degreeFile.completedTeacherFileSet.size()}"/> de <c:out value="${degreeFile.teacherFileSet.size()}"/></td>
			<td>
				<div>
					<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/showCurricularUnitFiles/${degreeFile.externalId}"><i class="glyphicon glyphicon-eye-open"></i> <spring:message code="label.curricularUnitFiles"/></a>
					<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/showTeacherFiles/${degreeFile.externalId}"><i class="glyphicon glyphicon-eye-open"></i> <spring:message code="label.teacherFiles"/></a>
					<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/exportDegreeFiles/${degreeFile.externalId}"><i class="glyphicon glyphicon-edit"></i> <spring:message code="label.export"/></a>
					<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/exportToA3es/${degreeFile.externalId}"><i class="glyphicon glyphicon-edit"></i> <spring:message code="label.exportToA3es"/></a>
				</div>
			</td>
		</tr>
		</c:forEach>
	</tbody>
</table>
