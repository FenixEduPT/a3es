<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script src="https://rawgithub.com/timrwood/moment/2.0.0/moment.js"></script>
<script type='text/javascript'>

function toggle(icon) {
	if (icon.hasClass("glyphicon-collapse-down")) {
		icon.removeClass("glyphicon-collapse-down");
		icon.addClass("glyphicon-collapse-up");
	} else if (icon.hasClass("glyphicon-collapse-up")) {
		icon.removeClass("glyphicon-collapse-up");
		icon.addClass("glyphicon-collapse-down");
	}
}

function validateDate(dateFieldName) {
	var dateField=$(dateFieldName);
	var input = moment(dateField.val(), "DD/MM/YYYY HH:mm");
	if (input.isValid()) {
		dateField.val(input.format("DD/MM/YYYY HH:mm"));
		dateField.closest("div").removeClass("has-error");
	} else {
		input = moment(dateField.val(), "DD/MM/YYYY");
		if (input.isValid()) {
			input = moment(dateField.val(), "DD/MM/YYYY").startOf('hour');
			dateField.val(input.format("DD/MM/YYYY HH:mm"));
			dateField.closest("div").removeClass("has-error");
		} else {
			dateField.closest("div").addClass("has-error");
			return false;
		}
	}
	return true;
}
		
function validateFields() {
		var allIsOk = true;
		if (!validateDate("#beginDate")) { allIsOk = false; };
		if (!validateDate("#endDate")) { allIsOk = false; };
		return allIsOk;
}

$(document).ready(function() {
	$(".show-degrees").click(function(el) {
		el.preventDefault();
		el.stopPropagation();
		$(el.target).closest("label").next("div").toggle();
		var icon = $(el.target).find("i");
		toggle(icon);
		
	});	
	
	$(".degrees").hide();
	
});

</script>

<div class="page-header">
	<h1><spring:message code="title.a3es.accreditation.processes"/></h1>
</div>

<c:if test="${not empty error}">
	<div class="alert alert-danger">
		<c:out value="${error}"/>
	</div>
</c:if>

<spring:url var="createUrl" value="/accreditationProcess/createProcess"></spring:url>
<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal" action="${createUrl}">
	<div class="form-group">
		<label for="name" class="col-sm-2 control-label"><spring:message code="label.name" />:</label>
		<div class="col-sm-10">
			<input id="name" name="name" class="form-control" value="<c:out value='${form.name}'/>" required="required"/>
		</div>
	</div>
	<div class="form-group">
		<label for="beginDate" class="col-sm-2 control-label"><spring:message code="label.beginDate" />:</label>
		<div class="col-sm-10">
			<input id="beginDate" name="beginDate" class="form-control" value="<c:out value='${form.beginDate}'/>" onchange="validateDate(this)" required="required" placeholder="<spring:message code="label.dateTimeFormat" />"/>
		</div>
	</div>
	<div class="form-group">
		<label for="endDate" class="col-sm-2 control-label"><spring:message code="label.endDate" />:</label>
		<div class="col-sm-10">
			<input id="endDate" name="endDate" class="form-control" value="<c:out value='${form.endDate}'/>" onchange="validateDate(this)" required="required" placeholder="<spring:message code="label.dateTimeFormat" />"/>
		</div>
	</div>
	<div class="form-group">
		<label for="executionYear" class="col-sm-2 control-label"><spring:message code="label.executionYear" /></label>
		<div class="col-sm-10">
			<form:select class="form-control" path="executionYear" id="executionYear" items="${executionYears}" itemLabel="qualifiedName" itemValue="externalId" required="required" />
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-push-2 col-sm-10">
			<a class="btn btn-default" href="${pageContext.request.contextPath}/accreditationProcess"><spring:message code="label.cancel"/></a>
			<button type="submit" class="btn btn-primary" id="form" onclick="return validateFields();">
				<i class="glyphicon glyphicon-plus-sign"></i> <spring:message code="label.create" />
			</button>
		</div>
	</div>
</form:form>
