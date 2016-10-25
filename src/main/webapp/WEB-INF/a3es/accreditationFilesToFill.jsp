<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="page-header">
	<h1><spring:message code="label.curricularUnitFiles"/></h1>
</div>

<c:choose>
	<c:when test="${curricularUnitFiles == null || empty curricularUnitFiles}">
		<spring:message code="message.no.files.to.fill"/>
	</c:when>
	<c:otherwise>
		<table class="table table-condensed">
			<thead><tr>
					<th class="col-sm-2"><spring:message code="label.process"/></th>
					<th class="col-sm-4"><spring:message code="label.degree"/></th>
					<th class="col-sm-5"/><spring:message code="label.name"/></th>
					<th class="col-sm-2"/></tr>
			</thead>
			<tbody>
				<c:forEach var="file" items="${curricularUnitFiles}">
					<tr>
						<td><c:out value="${file.accreditationProcess.processName}"/></td>
						<td><c:out value="${file.degreeFile.presentationName}"/></td>
						<td>
							<c:if test="${!file.isFilledAndValid()}">
							<i class="alert-warning glyphicon glyphicon-warning-sign" title='<spring:message code="error.incomplete.file"/>'></i>
							</c:if>
							<c:out value="${file.presentationName}"/>
						</td>
						<td>
							<c:if test="${file.isUserAllowedToView()}">
								<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditation/showFile/${file.externalId}?degreeFile=${degreeFile.externalId}"><spring:message code="label.file"/></a>
							</c:if>
						</td>
					</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>

<div class="page-header">
	<h1><spring:message code="label.teacherFiles"/></h1>
</div>
	
<c:choose>
	<c:when test="${teacherFiles == null || empty teacherFiles}">
		<spring:message code="message.no.files.to.fill"/>
	</c:when>
	<c:otherwise>
		<table class="table table-condensed">
			<thead><tr>
					<th class="col-sm-2"/><spring:message code="label.process"/></th>
					<th class="col-sm-8"/><spring:message code="label.name"/></th>
					<th class="col-sm-2"/></tr>
			</thead>
			<tbody>
				<c:forEach var="file" items="${teacherFiles}">
					<tr>
						<td><c:out value="${file.accreditationProcess.processName}"/></td>
						<td>
							<c:if test="${!file.isFilledAndValid()}">
							<i class="alert-warning glyphicon glyphicon-warning-sign" title='<spring:message code="error.incomplete.file"/>'></i>
							</c:if>
							<c:out value="${file.presentationName}"/>
						</td>
						<td>
							<c:if test="${file.isUserAllowedToView()}">							
								<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditation/showFile/${file.externalId}?degreeFile=${degreeFile.externalId}"><spring:message code="label.file"/></a>
							</c:if>
						</td>
					</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>
