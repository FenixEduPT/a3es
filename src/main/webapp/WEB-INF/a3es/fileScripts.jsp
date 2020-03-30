<style>
textarea.bennu-localized-string-textarea{
    height: 10em;
}
</style>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type='text/javascript'>

function validateStringSize(fieldName, size) {
	var escapedLength = fieldName.value.length;
	if (escapedLength > size) {
		$(fieldName).closest(".form-group").find("label").find("span").last().text(getStringSizeMessage(escapedLength, size));
		$(fieldName).addClass("alert-warning");
    }else{
    	$(fieldName).closest(".form-group").find("label").find("span").last().text("");
    	validateMandatoryString(fieldName);
    }
}

function getStringSizeMessage(escapedLength, maxSize) {
	return " (<spring:message code='error.exceded.field.size' arguments='"+escapedLength+","+maxSize+"' />)";
}

function validateLocalizedStringSize(fieldName, size) {
	var obj = JSON.parse(fieldName.value);
	var allLanguageWithValidSize = true;
	var localizedStringSizeMessage="";
	for(var key in obj){
		var change = false;
        var attrName = key;
        var attrValue = obj[key];
        var escapedLength = attrValue.length;
    	if (escapedLength > size) {
            localizedStringSizeMessage = localizedStringSizeMessage + " ("+key.split("-")[0].toUpperCase()+": "+ escapedLength +" de " +size+" caracteres)";    		
    		allLanguageWithValidSize=false;
        }
    }
	if(!allLanguageWithValidSize){
		$(fieldName).closest(".form-group").find("label").find("span").last().text(localizedStringSizeMessage);
		$(fieldName).next("div").find("textarea").addClass("alert-warning");
	}else{
		$(fieldName).closest(".form-group").find("label").find("span").last().text("");
		validateMandatoryLocalizedString(fieldName);
	}
}

function getUTF8Length(string) {
	string = string.replace(/\r?\n/g, "\r\n");
	string = JSON.stringify(string);
	string = string.replace(/\//g,"\\/");
	string = string.replace(/[\x00-\x1F\x7F-\x9F\u2000-\u20ff]/g,"\\u0000");
	
    var utf8length = 0;
    for (var n = 0; n < string.length; n++) {
        var c = string.charCodeAt(n);
        if (c < 128) {
            utf8length++;
        }
        else if((c > 127) && (c < 2048)) {
            utf8length = utf8length+2;
        }
        else {
            utf8length = utf8length+3;
        }
    }
    return utf8length-2;
 }
 
function validateMandatoryString(fieldName) {
	if($(fieldName).hasClass("mandatoryString") && fieldName.value.length == 0){
		$(fieldName).addClass("alert-warning");
	}else{
		$(fieldName).removeClass("alert-warning");	
	}
}

function validateMandatorySelect(fieldName) {
	if(fieldName.options[fieldName.selectedIndex].value.length == 0){
		$(fieldName).addClass("alert-warning");
	}else{
		$(fieldName).removeClass("alert-warning");	
	}
}

function validateMandatoryLocalizedString(fieldName) {
	var allLanguageFilled = true;
	var localizedStringSizeMessage="";
	for (var localeIndex in Bennu.locales) {
		var val = JSON.parse(fieldName.value);
		var tag = Bennu.locales[localeIndex].tag;
        if (!(tag in val)){
            var singleTag = Bennu.locales[localeIndex].tag.split("-")[0].toLowerCase();
            if (singleTag in val){
                tag = singleTag;
            }
        }
    	if(val[tag]==undefined || val[tag].trim().length == 0){
    		localizedStringSizeMessage = localizedStringSizeMessage + " ("+tag.split("-")[0].toUpperCase()+": 0 caracteres)";    		
    		allLanguageFilled = false;
    	}
    }
	if(allLanguageFilled || !$(fieldName).hasClass("mandatoryLocalizedString")){
		$(fieldName).closest(".form-group").find("label").find("span").last().text();
		$(fieldName).next("div").find("textarea").removeClass("alert-warning");		        		
	}else{
		var span =$(fieldName).closest(".form-group").find("label").find("span").last();
		span.text(span.text()+localizedStringSizeMessage);
		$(fieldName).next("div").find("textarea").addClass("alert-warning");
	}
}

$().ready(function() {
	$(".mandatoryLocalizedString, .mandatoryString").closest(".form-group").find("label").prepend("<span>(*) </span>");
	$(".mandatoryLocalizedString, .mandatoryString, .limitedSizeString").closest(".form-group").find("label").append("<span class='alert-warning'></span>");
	$(".mandatoryLocalizedString").change();
	$(".limitedSizeString").change();
	$(".mandatoryString").trigger("oninput");
	$(".limitedSizeString").trigger("oninput");
	
});

</script>



