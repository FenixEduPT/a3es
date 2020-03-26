<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="page-header">
	<h1><spring:message code="label.exportToA3es"/> <small><c:out value="${form.degreeFile.fileName}"/> (<c:out value="${form.degreeFile.accreditationProcess.processName}"/>)</small></h1>
</div>

<spring:url var="reloadUrl" value="/accreditationProcess/exportToA3es"></spring:url>
<spring:url var="exportCurricularUnitFilesUrl" value="/accreditationProcess/exportCurricularUnitFilesToA3es"></spring:url>
<spring:url var="exportTeacherFilesUrl" value="/accreditationProcess/exportTeacherFilesToA3es"></spring:url>
<spring:url var="exportDegreeStudyPlanUrl" value="/accreditationProcess/exportDegreeStudyPlanToA3es"></spring:url>
<spring:eval expression="T(org.fenixedu.a3es.ui.strategy.MigrationStrategy.AccreditationType).values()" var="accreditationTypes" />

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
		<label for="accreditationType" class="col-sm-2 control-label"><spring:message code="label.accreditationType" />:</label>
		<div class="col-sm-10">
			<form:select path="accreditationType" id="accreditationType" class="form-control" required="required" onchange='this.form.action="${reloadUrl}";this.form.submit();'>
				<c:if test="${form.accreditationType==null}">
					<form:option value="" selected="true" disabled="true"/>
				</c:if>
			    <form:options items="${accreditationTypes}" itemLabel="localizedName.content"/>
			</form:select>
		</div>
	</div>
	<div class="form-group">
		<label for="processFolderName" class="col-sm-2 control-label"><spring:message code="label.processFolderName" />:</label>
		<div class="col-sm-10">
			<input id="processFolderName" name="processFolderName" class="form-control" value="<c:out value='${form.processFolderName}'/>" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<label for="competenceCoursesFolderName" class="col-sm-2 control-label"><spring:message code="label.competenceCoursesFolderName" />:</label>
		<div class="col-sm-10">
			<input id="competenceCoursesFolderName" name="competenceCoursesFolderName" class="form-control" value="<c:out value='${form.competenceCoursesFolderName}'/>" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<label for="competenceCoursesFolderIndex" class="col-sm-2 control-label"><spring:message code="label.competenceCoursesFolderIndex" />:</label>
		<div class="col-sm-10">
			<input id="competenceCoursesFolderIndex" name="competenceCoursesFolderIndex" class="form-control" value="<c:out value='${form.competenceCoursesFolderIndex}'/>" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<label for="teacherCurriculumnFolderName" class="col-sm-2 control-label"><spring:message code="label.teacherCurriculumnFolderName" />:</label>
		<div class="col-sm-10">
			<input id="teacherCurriculumnFolderName" name="teacherCurriculumnFolderName" class="form-control" value="<c:out value='${form.teacherCurriculumnFolderName}'/>" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<label for="degreeStudyPlanFolderName" class="col-sm-2 control-label"><spring:message code="label.degreeStudyPlanFolderName" />:</label>
		<div class="col-sm-10">
			<input id="degreeStudyPlanFolderName" name="degreeStudyPlanFolderName" class="form-control" value="<c:out value='${form.degreeStudyPlanFolderName}'/>" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<label for="degreeStudyPlanFolderIndex" class="col-sm-2 control-label"><spring:message code="label.degreeStudyPlanFolderIndex" />:</label>
		<div class="col-sm-10">
			<input id="degreeStudyPlanFolderIndex" name="degreeStudyPlanFolderIndex" class="form-control" value="<c:out value='${form.degreeStudyPlanFolderIndex}'/>" required="required"/>
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
			<button type="submit" class="btn btn-primary" id="form" onclick='this.form.action="${exportDegreeStudyPlanUrl}";'>
				<i class="glyphicon glyphicon-send"></i> <spring:message code="label.export" /> <spring:message code="label.degreeStudyPlan" />
			</button>			
		</div>
	</div>
</form:form>

<c:if test="${not empty output}">
	<div class="alert alert-danger">
		<spring:message code="${output}" />
	</div>
</c:if>