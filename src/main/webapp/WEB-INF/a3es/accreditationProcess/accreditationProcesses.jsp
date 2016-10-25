<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="createUrl" value="/accreditationProcess/createProcess"></spring:url>


<div class="page-header">
	<h1><spring:message code="title.a3es.accreditation.processes"/></h1>
</div>

<spring:eval expression="T(org.fenixedu.a3es.domain.AccreditationProcess).getCanBeManageByUser()" var="canBeManageByUser" />
<c:if test="${canBeManageByUser}">
	<a class="btn btn-primary" href="${createUrl}"><i class="glyphicon glyphicon-plus-sign"></i> <spring:message code="label.create"/></a>
</c:if>

<div>
	<c:forEach var="output" items="${outputList}">
		<div><c:out value="${output}"/></div>
	</c:forEach>
</div>
<hr />
<c:choose>
	<c:when test="${processes == null}">
	</c:when>
	<c:when test="${empty processes}">
	</c:when>
	<c:otherwise>
		<table class="table table-condensed">
			<thead>
				<tr>
					<th><spring:message code="label.name"/></th>
					<th><spring:message code="label.beginDate"/></th>
					<th><spring:message code="label.endDate"/></th>
					<th><spring:message code="label.executionYear"/></th>
					<th><spring:message code="label.degrees"/></th>
					<th/>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="process" items="${processes}">
					<tr>
						<td><c:out value="${process.processName}"/></td>
						<td><c:out value="${process.beginFillingPeriod.toString('dd/MM/yyyy HH:mm')}"/></td>
						<td><c:out value="${process.endFillingPeriod.toString('dd/MM/yyyy HH:mm')}"/></td>
						<td><c:out value="${process.executionYear.qualifiedName}"/></td>
						<td>
							<c:forEach var="degreeFile" items="${process.degreeFileSet}">
								<c:out value="${degreeFile.fileName}"/>
								<br/>
							</c:forEach>
						</td>
						<td>
							<div>
								<c:if test="${canBeManageByUser}">
									<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/showProcess/${process.externalId}"><i class="glyphicon glyphicon-eye-open"></i> <spring:message code="label.show"/></a>		
									<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/editProcess/${process.externalId}"><i class="glyphicon glyphicon-edit"></i> <spring:message code="label.edit"/></a>
									<a class="btn btn-default" href="#" data-toggle="modal" data-target="#delete-dialog<c:out value='${process.externalId}'/>"><i class="glyphicon glyphicon-remove"></i> <spring:message code="label.remove"/></a>
									<div class="modal fade" id="delete-dialog<c:out value='${process.externalId}'/>">
									    <div class="modal-dialog">
									        <div class="modal-content">
									            <div class="modal-header">
									                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
									                        class="sr-only">Close</span></button>
											<h4><c:out value="${process.processName}"/></h4>
									            </div>
									            <div class="modal-body">
									                <p><spring:message code="label.delete.confirm"/></p>
									            </div>
									            <div class="modal-footer">
									           		<a class="btn btn-danger" href="${pageContext.request.contextPath}/accreditationProcess/deleteProcess/${process.externalId}"><i class="glyphicon glyphicon-remove"></i> <spring:message code="label.remove"/></a>
								                    <a class="btn btn-default" data-dismiss="modal"><spring:message code="label.cancel"/></a>
									            </div>
									        </div>
									    </div>
									</div>
								</c:if>
							</div>
						</td>
					</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:otherwise>		
</c:choose>
