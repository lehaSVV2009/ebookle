<div xmlns:jsp="http://java.sun.com/JSP/Page"
     xmlns:spring="http://www.springframework.org/tags"
     version="2.0">
    <jsp:directive.page contentType="text/html; charset=UTF-8"/>
    <jsp:output omit-xml-declaration="yes"/>

    <spring:message code="label.registration_success" var="labelSuccess"/>

    <div class="success">
        ${labelSuccess}
    </div>



</div>