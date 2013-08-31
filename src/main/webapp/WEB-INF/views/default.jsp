<html
        xmlns:jsp="http://java.sun.com/JSP/Page"
        xmlns:tiles="http://tiles.apache.org/tags-tiles"
        xmlns:spring="http://www.springframework.org/tags"
        >

    <%--<jsp:output doctype-root-element="HTML" doctype-system="about:legacy-compat"/>--%>

    <%--<jsp:directive.page contentType="text/html;charset=UTF-8"/>
    <jsp:directive.page pageEncoding="UTF-8"/>
--%>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=8">
        <link rel="stylesheet" type="text/css" media="screen"
                 href="http://localhost:8080/web-resources/bootstrap/css/dark.css">
        <link rel="stylesheet" type="text/css" media="screen" href="http://localhost:8080/web-resources/css/default.css">

        <title>
            Title
            <%--<spring:message code="welcome" arguments="${app_name}"></spring:message>--%>
        </title>
    </head>
    <body>
    <%--<spring:message code="application_name" var="app_name" htmlEscape="false"></spring:message>--%>

        <insertAttribute name="header" ignore="true"></insertAttribute>
        <insertAttribute name="body"></insertAttribute>
        <insertAttribute name="footer"></insertAttribute>
    </body>
</html>