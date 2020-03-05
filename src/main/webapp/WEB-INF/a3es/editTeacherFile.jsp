<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${portal.toolkit()}


<jsp:include page="fileScripts.jsp"/>

<div class="page-header"><h1><spring:message code="label.teacherFile" /> <small><c:out value="${form.fileName}"/></small></h1></div>

<c:if test="${not empty error}">
	<div class="alert alert-danger">
		<c:out value="${error}"/>
	</div>
</c:if>


  
<script type='text/javascript'>

function selectTeacherActivity(field, autocomplete) {
	$('textarea[id="'+field+'"]').val($(autocomplete).val())
	$('textarea[id="'+field+'"]').change();
}

$(document).ready( function() {
	$('a.btn[data-dismiss=modal],button.close').click(function(element) { $('.typeahead').val('') } )

    var my_Suggestion_class = new Bloodhound({
        limit: 100,
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        local: $.map(${form.scientificPublicationJson}, function(item) {
            return {value: item};
        })
    });

    my_Suggestion_class.initialize();

    var typeahead_elem = $('.typeahead');
    typeahead_elem.typeahead({
        hint: true,
        highlight: true,
        minLength: 1
    },
    {source: my_Suggestion_class.ttAdapter()});
});

</script>

<spring:eval expression="T(org.fenixedu.a3es.domain.A3esTeacherCategory).getAll()" var="categories" />
<spring:eval expression="T(org.fenixedu.a3es.domain.A3esDegreeType).values()" var="a3esDegreeTypes" />
<spring:eval expression="T(org.fenixedu.a3es.domain.MethodologyType).values()" var="methodologyTypes" />

<spring:url var="teacherFilesUrl" value="/accreditation"></spring:url>
<spring:url var="editUrl" value="/accreditation/editTeacherFile"></spring:url>

<c:if test="${form.teacherFile.canBeManageByUser}">
	<spring:url var="teacherFilesUrl" value="/accreditationProcess/showTeacherFiles/${form.degreeFile.externalId}"></spring:url>
	<spring:url var="editUrl" value="/accreditationProcess/editTeacherFile"></spring:url>	
</c:if>

<c:if test="${baseUrl==null}">
	<c:set var="baseUrl" value="accreditation"/>
</c:if>



<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal" action="${editUrl}">
	${csrf.field()}
	<h2><spring:message code="label.personalData" /></h2>
	<input type="hidden" name="teacherFile" value="<c:out value='${form.teacherFile.externalId}'/>"/>
	<input type="hidden" name="degreeFile" value="<c:out value='${form.degreeFile.externalId}'/>"/>
	<div class="form-group">
		<label for=teacherName class="col-sm-3 control-label"><spring:message code="label.name" />:</label>
		<div class="col-sm-9">
			<input id="teacherName" name="teacherName" class="form-control mandatoryString" value="<c:out value='${form.teacherName}'/>" required="required" oninput="validateStringSize(this,200)"/>
		</div>
	</div>
	<div class="form-group">
		<label for="institution" class="col-sm-3 control-label"><spring:message code="label.higherEducationInstitution" />:</label>
		<div class="col-sm-9">
			<input id="institution" name="institution" class="form-control mandatoryString" value="<c:out value='${form.teacherFile.institution}'/>" disabled="disabled" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group">
		<label for="organicUnit" class="col-sm-3 control-label"><spring:message code="label.organicUnit" />:</label>
		<div class="col-sm-9">
			<input id="organicUnit" name="organicUnit" class="form-control mandatoryString" value="<c:out value='${form.teacherFile.organicUnit}'/>" disabled="disabled" readonly="readonly"/>
		</div>
	</div>
	<div class="form-group">
		<label for="researchUnit" class="col-sm-3 control-label"><spring:message code="label.researchUnitFiliation" />:</label>
		<div class="col-sm-9">
			<input id="researchUnit" name="researchUnit" class="form-control limitedSizeString" value="<c:out value='${form.researchUnit}'/>" oninput="validateStringSize(this,200)"/>
		</div>
	</div>
	<div class="form-group">
		<label for="category" class="col-sm-3 control-label"><spring:message code="label.category" />:</label>
		<div class="col-sm-9">
			<form:select path="a3esTeacherCategory" id="a3esTeacherCategory" class="form-control mandatoryString" required="required" oninput="validateMandatorySelect(this)">
				<c:if test="${form.a3esTeacherCategory==null}">
					<form:option value="" selected="true" disabled="true"/>
				</c:if>
			    <form:options items="${categories}" itemLabel="name.content" itemValue="externalId"/>
			</form:select>
		</div>
	</div>
	<div class="form-group">
		<label for="a3esDegreeType" class="col-sm-3 control-label"><spring:message code="label.degreeType" />:</label>
		<div class="col-sm-9">
			<form:select path="a3esDegreeType" id="a3esDegreeType" class="form-control mandatoryString" required="required" oninput="validateMandatorySelect(this)">
				<c:if test="${form.a3esDegreeType==null}">
					<form:option value="" selected="true" disabled="true"/>
				</c:if>
			    <form:options items="${a3esDegreeTypes}" itemLabel="localizedName.content"/>
			</form:select>
		</div>
	</div>
	<div class="form-group">
		<label for="degreeScientificArea" class="col-sm-3 control-label"><spring:message code="label.degreeScientificArea" />:</label>
		<div class="col-sm-9">
			<input id="degreeScientificArea" name="degreeScientificArea" class="form-control mandatoryString" value="<c:out value='${form.degreeScientificArea}'/>" required="required" oninput="validateStringSize(this,200)"/>
		</div>
	</div>
	<div class="form-group">
		<label for="degreeYear" class="col-sm-3 control-label"><spring:message code="label.degreeYear" />:</label>
		<div class="col-sm-9">
			<input id="degreeYear" name="degreeYear" class="form-control mandatoryString" value="<c:out value='${form.degreeYear}'/>" required="required" type="number" step="1" min="1900" max="2100" size="4" oninput="validateMandatoryString(this)"/>
		</div>
	</div>
	<div class="form-group">
		<label for="degreeInstitution" class="col-sm-3 control-label"><spring:message code="label.degreeInstitution" />:</label>
		<div class="col-sm-9">
			<input id="degreeInstitution" name="degreeInstitution" class="form-control mandatoryString" value="<c:out value='${form.degreeInstitution}'/>" required="required" oninput="validateStringSize(this,200)"/>
		</div>
	</div>
	<div class="form-group">
		<label for="regime" class="col-sm-3 control-label"><spring:message code="label.regime" />:</label>
		<div class="col-sm-9">
			<input id="regime" name="regime" class="form-control mandatoryString" value="<c:out value='${form.regime}'/>" required="required" type="number" step="1" min="0" max="100" size="2" oninput="validateMandatoryString(this)"/>
		</div>
	</div>
	
	<h2><spring:message code="label.otherAcademicDegreesOrTitle" /></h2>
	<table class="table table-condensed">
		<thead>
			<tr>
				<th class="col-sm-1"><spring:message code="label.year"/></th>
				<th class="col-sm-2"><spring:message code="label.degreeType"/></th>
				<th class="col-sm-2"><spring:message code="label.area"/></th>
				<th class="col-sm-5"><spring:message code="label.institution"/></th>
				<th class="col-sm-1"><spring:message code="label.classification"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="qualification" items="${form.a3esQualificationBeanSet}" varStatus="status">
				<c:if test="${qualification.a3esQualification != null}">
					<input type="hidden" name="a3esQualificationBeanSet[${status.index}].a3esQualification" value="<c:out value='${qualification.a3esQualification.externalId}'/>"/>
				</c:if>
				<tr>
					<td><form:input path="a3esQualificationBeanSet[${status.index}].year" class="form-control" value="${qualification.year}" type="number" step="1" min="1900" max="2100" size="4"/></td>
					<td><form:input path="a3esQualificationBeanSet[${status.index}].degree" class="form-control" value="${qualification.degree}" oninput="validateStringSize(this,30)"/></td>
					<td><form:input path="a3esQualificationBeanSet[${status.index}].area" class="form-control" value="${qualification.area}" oninput="validateStringSize(this,100)"/></td>
					<td><form:input path="a3esQualificationBeanSet[${status.index}].institution" class="form-control" value="${qualification.institution}" oninput="validateStringSize(this,100)"/></td>
					<td><form:input path="a3esQualificationBeanSet[${status.index}].classification" class="form-control" value="${qualification.classification}" oninput="validateStringSize(this,30)"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<h2><spring:message code="label.scientificActivity" /> <small><spring:message code="label.scientificActivity.message" /></small></h2>
	<c:forEach var="teacherActivity" items="${form.scientificActivitySet}" varStatus="status">
		<div class="form-group">
			<div class="col-sm-12"><label></label></div>
			<div class="col-sm-11">
				<c:if test="${teacherActivity.teacherActivity != null}">
					<input type="hidden" name="scientificActivitySet[${status.index}].teacherActivity" value="<c:out value='${teacherActivity.teacherActivity.externalId}'/>"/>
				</c:if>
			<textarea id="scientificActivitySet${status.index}.activity" name="scientificActivitySet[${status.index}].activity" class="form-control limitedSizeString" rows="2" oninput="validateStringSize(this,500)"><c:out value='${teacherActivity.activity}'/></textarea>
			</div>
			<div class="col-sm-1">
			 	<a class="btn btn-default" href="#" data-toggle="modal" data-target="#change-scientificActivity<c:out value='${status.index}'/>"><i class="glyphicon glyphicon-edit"></i></a>
				 <div class="modal fade " id="change-scientificActivity<c:out value='${status.index}'/>">
				    <div class="modal-dialog">
				        <div class="modal-content">
				            <div class="modal-header">
				                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
				                        class="sr-only">Close</span></button>
				                <h4><spring:message code="label.search"/></h4>
				            </div>
				            <div class="modal-body">
					            <input id="scientificActivityAutocomplete${status.index}" class="typeahead form-control" type="text">
					        </div>
				            <div class="modal-footer">
				            	<a class="btn btn-primary" data-dismiss="modal" onclick="selectTeacherActivity('scientificActivitySet${status.index}.activity', '#scientificActivityAutocomplete${status.index}');"><spring:message code="label.select"/></a>
			                    <a class="btn btn-default" data-dismiss="modal"><spring:message code="label.cancel"/></a>
				            </div>
				        </div>
				    </div>
				</div>
			</div>
		</div>
	</c:forEach>
	
	<h2><spring:message code="label.developmentActivity" /> <small><spring:message code="label.developmentActivity.message" /></small></h2>
	<c:forEach var="teacherActivity" items="${form.developmentActivitySet}" varStatus="status">
		<div class="form-group">
			<div class="col-sm-12"><label></label></div>
			<div class="col-sm-12">
				<c:if test="${teacherActivity.teacherActivity != null}">
					<input type="hidden" name="developmentActivitySet[${status.index}].teacherActivity" value="<c:out value='${teacherActivity.teacherActivity.externalId}'/>"/>
				</c:if>
				<textarea id="developmentActivitySet" name="developmentActivitySet[${status.index}].activity" class="form-control limitedSizeString" rows="2" oninput="validateStringSize(this,200)"><c:out value='${teacherActivity.activity}'/></textarea>
			</div>
		</div>
	</c:forEach>

	<h2><spring:message code="label.otherPublicationActivity" /> <small><spring:message code="label.otherPublicationActivity.message" /></small></h2>
	<c:forEach var="teacherActivity" items="${form.otherPublicationActivitySet}" varStatus="status">
		<div class="form-group">
			<div class="col-sm-12"><label></label></div>
			<div class="col-sm-11">
				<c:if test="${teacherActivity.teacherActivity != null}">
					<input type="hidden" name="otherPublicationActivitySet[${status.index}].teacherActivity" value="<c:out value='${teacherActivity.teacherActivity.externalId}'/>"/>
				</c:if>
				<textarea id="otherPublicationActivitySet${status.index}.activity" name="otherPublicationActivitySet[${status.index}].activity" class="form-control limitedSizeString" rows="2" oninput="validateStringSize(this,500)"><c:out value='${teacherActivity.activity}'/></textarea>
			</div>
			<div class="col-sm-1">
			 	<a class="btn btn-default" href="#" data-toggle="modal" data-target="#change-otherPublicationActivity<c:out value='${status.index}'/>"><i class="glyphicon glyphicon-edit"></i></a>
				 <div class="modal fade " id="change-otherPublicationActivity<c:out value='${status.index}'/>">
				    <div class="modal-dialog">
				        <div class="modal-content">
				            <div class="modal-header">
				                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
				                        class="sr-only">Close</span></button>
				                <h4><spring:message code="label.search"/></h4>
				            </div>
				            <div class="modal-body">
				            	<input id="otherPublicationAutocomplete${status.index}" class="typeahead form-control">
				            </div>
				            <div class="modal-footer">
				            	<a class="btn btn-primary" data-dismiss="modal" onclick="selectTeacherActivity('otherPublicationActivitySet${status.index}.activity', '#otherPublicationAutocomplete${status.index}');"><spring:message code="label.select"/></a>
			                    <a class="btn btn-default" data-dismiss="modal"><spring:message code="label.cancel"/></a>
				            </div>
				        </div>
				    </div>
				</div>
			</div>
		</div>
	</c:forEach>
	
	<h2><spring:message code="label.otherProfessionalActivity" /> <small><spring:message code="label.otherProfessionalActivity.message" /></small></h2>
	<c:forEach var="teacherActivity" items="${form.otherProfessionalActivitySet}" varStatus="status">
		<div class="form-group">
			<div class="col-sm-12"><label></label></div>
			<div class="col-sm-12">
				<c:if test="${teacherActivity.teacherActivity != null}">
					<input type="hidden" name="otherProfessionalActivitySet[${status.index}].teacherActivity" value="<c:out value='${teacherActivity.teacherActivity.externalId}'/>"/>
				</c:if>
				<textarea id="otherProfessionalActivitySet" name="otherProfessionalActivitySet[${status.index}].activity" class="form-control limitedSizeString" rows="2" oninput="validateStringSize(this,200)"><c:out value='${teacherActivity.activity}'/></textarea>
			</div>
		</div>
	</c:forEach>
	
	<h2><spring:message code="label.teachingServiceAllocation" /> <small><c:forEach var="methodologyType" items="${methodologyTypes}">(<c:out value="${methodologyType.sigla}"/>) <c:out value="${methodologyType.localizedName.content}"/></c:forEach></small></h2>
	<span class='alert-warning'><spring:message code="message.teachingServiceAllocation.maxElements" /></span>
	<table class="table table-condensed">
		<thead>
			<tr>
				<th class="col-sm-6"><spring:message code="label.curricularUnit"/></th>
				<th class="col-sm-4"><spring:message code="label.studyCycle"/></th>
				<th class="col-sm-1"><spring:message code="label.type"/></th>
				<th class="col-sm-1"><spring:message code="label.totalContactHours"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="teachingService" items="${form.a3esTeachingServiceBeanSet}" varStatus="status">
				<tr>
					<c:if test="${teachingService.a3esTeachingService != null}">
						<input type="hidden" name="a3esTeachingServiceBeanSet[${status.index}].a3esTeachingService" value="<c:out value='${teachingService.a3esTeachingService.externalId}'/>"/>
					</c:if>
					<td><form:input path="a3esTeachingServiceBeanSet[${status.index}].curricularUnitName" class="form-control" value="${teachingService.curricularUnitName}" oninput="validateStringSize(this,100)"/></td>
					<td><form:input path="a3esTeachingServiceBeanSet[${status.index}].studyCycles" class="form-control" value="${teachingService.studyCycles}" oninput="validateStringSize(this,200)"/></td>
					<td><form:input path="a3esTeachingServiceBeanSet[${status.index}].methodologyTypes" class="form-control" value="${teachingService.methodologyTypes}" oninput="validateStringSize(this,30)"/></td>
					<td><form:input path="a3esTeachingServiceBeanSet[${status.index}].totalHours" class="form-control" value="${teachingService.totalHours}" type="number" step="any" min="0"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	
	
	<div class="form-group">
		<div class="col-sm-12">
			<a class="btn btn-default" href="${teacherFilesUrl}"><spring:message code="label.cancel"/></a>
			<button type="submit" class="btn btn-primary" id="form"><i class="glyphicon glyphicon-floppy-disk"></i> <spring:message code="label.save" /></button>
		</div>
	</div>
</form:form>


