<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:choose>
	<c:when test="${files == null}">
	</c:when>
	<c:when test="${empty files}">
	</c:when>
	<c:otherwise>
		<table class="table table-condensed">
			<thead>
				<tr>
					<th class="col-sm-6"><spring:message code="label.name"/></th>
					<th class="col-sm-4"/>
					<th class="col-sm-2"/>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="file" items="${files}">
					<tr>
						<td>
							
							<c:if test="${!file.isFilledAndValid()}">
							<i class="alert-warning glyphicon glyphicon-warning-sign" title='<spring:message code="error.incomplete.file"/>'></i>
							</c:if>
							<c:out value="${file.presentationName}"/>
						</td>
						<td>
							<c:if test="${file.canBeManageByUser}">
								<c:forEach var="member" items="${file.responsibleGroup.members}">
									<c:out value='${member.person.presentationName}'/>
									<br/>
								</c:forEach>
							</c:if>
						</td>
						<td>
							<c:if test="${file.isUserAllowedToView()}">
								<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/showFile/${file.externalId}?degreeFile=${degreeFile.externalId}"><i class="glyphicon glyphicon-eye-open"></i> <spring:message code="label.show"/></a>
							</c:if>
							<c:if test="${file.canBeManageByUser}">
								<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess/addResponsible/${file.externalId}?degreeFile=${degreeFile.externalId}"><i class="glyphicon glyphicon-edit"></i> <spring:message code="label.responsibles"/></a>
								<a class="btn btn-default" href="#" data-toggle="modal" data-target="#delete-dialog<c:out value='${file.externalId}'/>"><i class="glyphicon glyphicon-remove"></i> <spring:message code="label.remove"/></a>
									<div class="modal fade" id="delete-dialog<c:out value='${file.externalId}'/>">
									    <div class="modal-dialog">
									        <div class="modal-content">
									            <div class="modal-header">
									                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
									                        class="sr-only">Close</span></button>
									               <h4><c:out value="${file.fileName}"/></h4>
									            </div>
									            <div class="modal-body">
									                <p><spring:message code="label.delete.confirm"/></p>
									            </div>
									            <div class="modal-footer">
									                <a class="btn btn-danger" href="${pageContext.request.contextPath}/accreditationProcess/removeFile/${file.externalId}?degreeFile=${degreeFile.externalId}"><i class="glyphicon glyphicon-remove"></i> <spring:message code="label.remove"/></a>
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
	</c:otherwise>		
</c:choose>