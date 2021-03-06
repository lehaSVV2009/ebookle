<?xml version="1.0" encoding="UTF-8" standalone="no"?>

<div <%--id="header"--%>
     xmlns:jsp="http://java.sun.com/JSP/Page"
     xmlns:spring="http://www.springframework.org/tags"
     version="2.0">

    <jsp:directive.page contentType="text/html;charset=UTF-8"/>
    <jsp:output omit-xml-declaration="yes"/>
    <spring:message code="search_by_content" var="serachByContent"/>
    <spring:message code="search_by_tag" var="searchByTag"/>
    <spring:message code="search_by_category" var="searchByCategory"/>
    <spring:url value="/" var="homeUrl"/>

    <div id="Header" class="navbar nav-bar-default fixed-top">
        <div class="container">
            <div class="navbar-header">
                <a href="${homeUrl}" title="Home">

                    <img src="http://localhost:8080/web-resources/img/logo-small.png"/>
                </a>
            </div>
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control col-lg-8" placeholder="Search"/>
            </form>
            <form class="navbar-form navbar-right">
                <select class="form-control" id="select">
                    <option value="1">${serachByContent}</option>
                    <option value="2">${searchByTag}</option>
                    <option value="3">${searchByCategory}</option>
                </select>
            </form>
        </div>
    </div>

</div>