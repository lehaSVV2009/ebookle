<div xmlns:jsp="http://java.sun.com/JSP/Page" class="wrapper"
     xmlns:c="http://java.sun.com/jsp/jstl/core"
     xmlns:fn="http://java.sun.com/jsp/jstl/functions"
     xmlns:spring="http://www.springframework.org/tags"
     xmlns:sec="http://www.springframework.org/security/tags"
     version="2.0"><%--
    <jsp:directive.page contentType="text/html; charset=UTF-8"/>
    <jsp:output omit-xml-declaration="yes"/>--%>

    <spring:message code="label.hello" var="labelHello"/>
    <spring:message code="label.create_book" var="labelCreateBook"/>
    <spring:message code="label.logout" var="labelLogout"/>
    <spring:message code="label.admin" var="labelAdmin"/>
    <spring:message code="label.login" var="labelLogin"/>
    <spring:message code="label.registration" var="labelRegistration"/>
    <spring:message code="label.most_popular" var="labelMostPopular"/>
    <spring:message code="label.recent" var="labelRecent"/>
    <spring:message code="label.statistics" var="labelStatistics"/>


    <c:if test="${not empty flashMessage}">
        <div class="error">
            ${flashMessage}
        </div>
        <br/>
    </c:if>

    <c:if test="${not empty goodMessage}">
        <div class="success">
            ${goodMessage}
        </div>
        <br/>
    </c:if>

    <c:url value='/j_spring_security_logout' var="securityLogout"/>

    <sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_USER')">
        <div class="well leftSidePanel">

            ${labelHello}, ${user.name}
            <br/>
            Your books
            <br/>

            <table class="table table-bordered leftSideTable">
                <c:forEach items="${user.books}" var="userBook">
                    <tr>
                        <td class="tableCol1">
                            <a href="/${user.login}/editBook/${userBook.title}/1">
                                <c:choose>
                                    <c:when test="${fn:length(userBook.title) gt 20}">
                                        ${fn:substring(userBook.title, 0, 10)}..
                                    </c:when>
                                    <c:otherwise>
                                        ${userBook.title}
                                    </c:otherwise>
                                </c:choose>
                            </a>
                        </td>
                        <td>
                            <a href="/${user.login}/remove/${userBook.id}">
                                <img src="http://localhost:8080/web-resources/img/delete16.png"/>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <br/>
            <a href="/${user.login}/bookCreation">${labelCreateBook}</a>
            <br/>
            <a href="${securityLogout}">${labelLogout}</a>
            <c:if test="${person eq 'admin'}">
                <div style="float: right">
                    <br/>
                    <a href="/admin">${labelAdmin}</a>
                </div>
            </c:if>
        </div>
    </sec:authorize>

    <c:url value='j_spring_security_check' var="securityCheck"/>
    <c:if test="${person eq 'guest'}">
        <div class="well leftSidePanel">
            <form name='login_form' action="${securityCheck}"
                  method='POST'>
                <input type="text" class="form-control" id="inputEmail" placeholder="Email" name='j_username' value=''/>
                <input type="password" class="form-control" id="inputPassword" placeholder="Password"
                       name='j_password'/>
                <button type="submit" class="btn btn-primary">${labelLogin}</button>
            </form>
            <a href="/registration">${labelRegistration}</a>
        </div>
    </c:if>




    <div class="well content">
        <form class="bs-example form-horizontal">
            <fieldset>
                <div class="btn-group btn-group-justified">
                    <a href="/home/mostPopular" class="btn btn-default">${labelMostPopular}</a>
                    <a href="/home/recent" class="btn btn-default">${labelRecent}</a>

                    <c:if test="${not empty categories}">
                        <c:forEach items="${categories}" var="category">
                            <a href="/home/${category.name}" class="btn btn-default">
                                ${category.name}
                            </a>
                        </c:forEach>
                    </c:if>

                </div>
                <c:if test="${not empty books}">
                    <table class="table table-bordered table1">
                        <tbody>
                        <c:forEach items="${books}" var="book">
                            <tr>
                                <td>
                                    <a href="/${book.user.login}/editBook/${book.title}/1/show">
                                        ${book.title}
                                    </a>
                                </td>
                                <td>
                                    ${book.user.surname}
                                </td>
                                <td>
                                    ${book.rating}
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <a href="/statistics/authorCounts">${labelStatistics}</a>


            </fieldset>
        </form>
    </div>

</div>