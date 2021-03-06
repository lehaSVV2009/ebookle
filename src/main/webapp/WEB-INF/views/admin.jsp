<div xmlns:jsp="http://java.sun.com/JSP/Page"
     xmlns:c="http://java.sun.com/jsp/jstl/core"
     xmlns:spring="http://www.springframework.org/tags"
     version="2.0">
    <jsp:directive.page contentType="text/html; charset=UTF-8"/>
    <jsp:output omit-xml-declaration="yes"/>

    <spring:message code="label.administration" var="labelAdministration"/>
    <spring:message code="label.remove" var="labelRemove"/>

    <h2>${labelAdministration}</h2>

    <table class="table table-bordered table1">
        <c:forEach items="${users}" var="user">
            <tr>
                <td>
                    ${user.surname}
                </td>
                <td>
                    ${user.name}
                </td>
                <td>
                    ${user.login}
                </td>
                <td>
                    <a href="/removeUser/${user.id}" methos="post">
                        <img src="http://localhost:8080/web-resources/img/delete16.png"/>
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>

</div>