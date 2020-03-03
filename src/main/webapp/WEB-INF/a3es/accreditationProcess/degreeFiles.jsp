<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${portal.toolkit()}

<div class="page-header">
	<h1><spring:message code="label.degrees"/> <small><c:out value="${form.accreditationProcess.processName}"/></small></h1>
</div>

<c:if test="${not empty error}">
	<div class="alert alert-danger">
		<c:out value="${error}"/>
	</div>
</c:if>

<c:if test="${!empty form.accreditationProcess.degreeFileSet}">
	<table class="table table-condensed">
		<thead>
			<tr>
				<th><spring:message code="label.degree"/></th>
				<th><spring:message code="label.degreeAcronym"/></th>
				<th><spring:message code="label.degreeCode"/></th>
				<th></th>
				<th />
			</tr>
		</thead>
		<tbody>
		<c:forEach var="degreeFile" items="${form.accreditationProcess.degreeFileSet}">
				<tr>
					<td><c:out value="${degreeFile.fileName}"/></td>
					<td><c:out value="${degreeFile.degreeAcronym}"/></td>
					<td><c:out value="${degreeFile.degreeCode}"/></td>
					<td>
						<c:forEach var="member" items="${degreeFile.responsibleGroup.members.iterator()}">
							<c:out value='${member.person.presentationName}'/>
							<br/>
						</c:forEach>
					</td>
					<td>
						<c:if test="${degreeFile.canBeManageByUser}">
							<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/addResponsible/${degreeFile.externalId}"><i class="glyphicon glyphicon-edit"></i> <spring:message code="label.responsibles"/></a>
							<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/editDegreeFile/${degreeFile.externalId}"><i class="glyphicon glyphicon-edit"></i> <spring:message code="label.edit"/></a>
							<a class="btn btn-default" href="#" data-toggle="modal" data-target="#delete-dialog<c:out value='${degreeFile.externalId}'/>"><i class="glyphicon glyphicon-remove"></i> <spring:message code="label.remove"/></a>
								<div class="modal fade" id="delete-dialog<c:out value='${degreeFile.externalId}'/>">
								    <div class="modal-dialog">
								        <div class="modal-content">
								            <div class="modal-header">
								                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
								                        class="sr-only">Close</span></button>
								                <h4><c:out value="${degreeFile.fileName}"/></h4>
								            </div>
								            <div class="modal-body">
								                <p><spring:message code="label.delete.confirm"/></p>
								            </div>
								            <div class="modal-footer">
								                <a class="btn btn-danger" href="${pageContext.request.contextPath}/accreditationProcess/removeDegreeFile/${degreeFile.externalId}"><i class="glyphicon glyphicon-remove"></i> <spring:message code="label.remove"/></a>
							                    <a class="btn btn-default" data-dismiss="modal"><spring:message code="label.cancel"/></a>
								            </div>
								        </div>
								    </div>
								</div>
						</c:if>
					</td>
				</tr>
		</c:forEach>
		</tbody>
	</table>
</c:if>
<hr />

<c:choose>
	<c:when test="${!form.accreditationProcess.isFillingPeriodOver()}">
		<h3><spring:message code="label.add.degrees"/></h3>
		<spring:url var="addUrl" value="/accreditationProcess/addDegreeFiles"></spring:url>
		<c:forEach var="degreeType" items="${degreeTypes}">
			<div class="row" style="margin-left: 50px;">
				<a href="#" data-toggle="modal" data-target="#add-degrees-dialog<c:out value='${degreeType.externalId}'/>"><c:out value="${degreeType.name.content}"/></a></td>
				<div class="modal fade" id="add-degrees-dialog<c:out value='${degreeType.externalId}'/>">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">
									<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
								</button>
								<h4 class="modal-title">
									<h1><spring:message code="label.degrees"/> <small><c:out value="${degreeType.name.content}"/></small></h1>
									<%--<spring:message code="label.create.contract"/> --%>
								</h4>
							</div>
							<div class="modal-body">
								<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal" action="${addUrl}">
										${csrf.field()}
									<input type="hidden" name="accreditationProcess" value="<c:out value='${form.accreditationProcess.externalId}'/>"/>
								<c:forEach var="degree" items="${form.getDegrees(degreeType)}">
									<div class="checkbox" ><form:checkbox path="degrees" value="${degree.externalId}" /><c:out value='${degree.name}'/></div> 
								</c:forEach>
							</div>
							<div class="modal-footer">
									<button type="submit" class="btn btn-primary"><i class="glyphicon glyphicon-plus-sign"></i> <spring:message code="label.add" /></button>
								</form:form>
								<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="button.cancel" /></button>
							</div>
						</div>
					</div>
				</div>
				
			</div>
		</c:forEach>
		<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal" action="${addUrl}">
			${csrf.field()}
			<input type="hidden" name="accreditationProcess" value="<c:out value='${form.accreditationProcess.externalId}'/>"/>
			<div class="form-group">
					<label for="otherDegree" class="col-sm-2 control-label"><spring:message code="label.other"/></label>
					<div class="col-sm-10">
						<input id="otherDegree" name="otherDegree" class="form-control" value="<c:out value='${form.otherDegree}'/>" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-push-2 col-sm-10" >
						<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/showProcess/${form.accreditationProcess.externalId}"><i class="glyphicon glyphicon-chevron-left"></i> <spring:message code="label.back"/></a>
						<button type="submit" class="btn btn-primary" id="form"><i class="glyphicon glyphicon-plus-sign"></i> <spring:message code="label.add" /></button>
					</div>
				</div>
		</form:form>
	</c:when>
	<c:otherwise>
		<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/showProcess/${form.accreditationProcess.externalId}"><i class="glyphicon glyphicon-chevron-left"></i> <spring:message code="label.back"/></a>
	</c:otherwise>
</c:choose>