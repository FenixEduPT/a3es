<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${ portal.toolkit() }

<div class="page-header">
	<h1><spring:message code="title.configuration"/></h1>
</div>

<div role="tabpanel">
	<ul class="nav nav-tabs" role="tablist">
		<li role="presentation" class="active"><a href="#categories" aria-controls="categories" role="tab" data-toggle="tab"><spring:message code="label.categories"/></a></li>
	</ul>


	<div class="tab-content">
		<spring:url var="createUrl" value="/a3esConfiguration/createCategory"></spring:url>
		<spring:url var="editUrl" value="/a3esConfiguration/editCategory"></spring:url>
		<spring:url var="createDegreeUrl" value="/a3esConfiguration/createDegreeType"></spring:url>
		<spring:url var="editDegreeUrl" value="/a3esConfiguration/editDegreeType"></spring:url>
	
		<div role="tabpanel" class="tab-pane active" id="categories">
			<div class="page-header">
				<h1><spring:message code="label.categories" /><small><spring:message code="title.configuration" /></small></h1>
			</div>
			<a href="#" data-toggle="modal" data-target="#create-teacherCategory-dialog"><span class="glyphicon glyphicon-edit"></span> <spring:message code="label.create"/></a>
			<div class="modal fade" id="create-teacherCategory-dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<form:form role="form" method="POST" action="${createUrl}">
							${csrf.field()}
							<div class="modal-body">
								<dl class="dl-horizontal"><dt><spring:message code="label.a3esTeacherCategories"/>:</dt>
								<dd><input id="name" name="name" class="form-control" required="required"/></dd></dl>
							</div>
							<div class="modal-footer">
									<button type="submit" class="btn btn-primary"><spring:message code="label.create" /></button>
									<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="button.cancel" /></button>
							</div>
						</form:form>
					</div>
				</div>
			</div>
			<c:choose>
				<c:when test="${teacherCategories == null}">
				</c:when>
				<c:when test="${empty teacherCategories}">
				</c:when>
				<c:otherwise>
					<table class="table table-condensed">
						<thead>
							<tr>
								<th><spring:message code="label.categories"/></th>
								<th><spring:message code="label.a3esTeacherCategories"/></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="teacherCategory" items="${teacherCategories}">
								<tr>
									<td><c:out value="${teacherCategory.name.content}"/></td>
									<td><c:out value="${teacherCategory.a3esTeacherCategory.name.content}"/></td>
									
									<td><a href="#" data-toggle="modal" data-target="#edit-teacherCategory-dialog<c:out value='${teacherCategory.externalId}'/>"><span class="glyphicon glyphicon-edit"></span> <spring:message code="label.edit"/></a></td>
									<div class="modal fade" id="edit-teacherCategory-dialog<c:out value='${teacherCategory.externalId}'/>">
										<div class="modal-dialog">
											<div class="modal-content">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal">
														<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
													</button>
													<h4 class="modal-title">
														<spring:message code="label.edit"/>
													</h4>
												</div>
												 <form:form role="form" method="POST" action="${editUrl}">
													 ${csrf.field()}
													 <input type="hidden" name="teacherCategory" value="<c:out value='${teacherCategory.externalId}'/>"/>
													<div class="modal-body">
														<dl class="dl-horizontal"><dt><spring:message code="label.categories"/>:</dt><dd><c:out value="${teacherCategory.name.content}"/>&nbsp;</dd></dl>
														<dl class="dl-horizontal"><dt><spring:message code="label.a3esTeacherCategories"/>:</dt><dd>
														<select id="a3esTeacherCategory" name="a3esTeacherCategory" class="form-control">
															<c:forEach var="a3esTeacherCategory" items="${a3esTeacherCategories}">
																<option value="${a3esTeacherCategory.externalId}">${a3esTeacherCategory.name.content}</option>
															</c:forEach>
														</select>&nbsp;</dd></dl>
														
													</div>
													<div class="modal-footer">
														<button type="submit" class="btn btn-primary"><spring:message code="label.edit" /></button>
														<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="button.cancel" /></button>
													</div>
												</form:form>
											</div>
										</div>
									</div>

								</tr>
						</c:forEach>
						</tbody>
					</table>
				</c:otherwise>		
			</c:choose>
		</div>
	</div>	
</div>
