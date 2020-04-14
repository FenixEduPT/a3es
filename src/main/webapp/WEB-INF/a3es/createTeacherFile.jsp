<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${portal.toolkit()}

<c:if test="${not empty error}">
	<div class="alert alert-danger">
		<c:out value="${error}"/>
	</div>
</c:if>


<div role="tabpanel">
	<ul class="nav nav-tabs" role="tablist">
		<li role="presentation" class="active"><a href="#internalUser" aria-controls="internalUser" role="tab" data-toggle="tab"><spring:message code="label.user"/></a></li>
		<li role="presentation"><a href="#externalUser" aria-controls="externalUser" role="tab" data-toggle="tab"><spring:message code="label.externalPerson"/></a></li>
	</ul>
	
	
	<div class="tab-content">
		
		<div role="tabpanel" class="tab-pane active" id="internalUser">
			<spring:url var="editUrl" value="/accreditationProcess/createTeacherFile"></spring:url>
			<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal" action="${editUrl}">
				${csrf.field()}
				<input type="hidden" name="degreeFile" value="<c:out value='${form.degreeFile.externalId}'/>"/>
				<div class="form-group">
					<label for="fileName" class="col-sm-2 control-label"><spring:message code="label.user" />:</label>
					<div class="col-sm-10">
						<input id="user" name="user" bennu-user-autocomplete class="form-control col-sm-11 user-search" required  value=""/>
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
		</div>
		<div role="tabpanel" class="tab-pane" id="externalUser">
			<spring:url var="editUrl" value="/accreditationProcess/createTeacherFile"></spring:url>
			<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal" action="${editUrl}">
				${csrf.field()}
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
		</div>
	</div>

</div>


	