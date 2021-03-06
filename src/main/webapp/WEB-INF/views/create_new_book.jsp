<div xmlns:jsp="http://java.sun.com/JSP/Page"
     xmlns:c="http://java.sun.com/jsp/jstl/core"
     xmlns:spring="http://www.springframework.org/tags"
     version="2.0">
    <jsp:directive.page contentType="text/html; charset=UTF-8"/>
    <jsp:output omit-xml-declaration="yes"/>

    <spring:message code="create_book_label" var="createBookLabel"/>
    <spring:message code="title_label" var="titleLabel"/>
    <spring:message code="description_label" var="descriptionLabel"/>
    <spring:message code="category_label" var="categoryLabel"/>
    <spring:message code="add_tag_label" var="addTagLabel"/>
    <spring:message code="create_book_button_text" var="createBookButtonText"/>
    <spring:message code="cancel" var="cancel"/>

    <c:if test="${not empty error}">
        <div class="error">
            ${error}
        </div>
        <br/>
    </c:if>

    <form action="/${userLogin}/createNewBook" method="post" id="bookCreationForm" name="bookCreationForm">
        <h2>${createBookLabel}</h2>
        <br/>
        ${titleLabel}
        <br/>
        <input type="text" name="title"/>
        <br/>
        ${descriptionLabel}
        <br/>
        <input type="text" name="description"/>
        <br/>
        ${categoryLabel}
        <br/>
        <c:if test="${not empty categories}">
            <select name="category">

                <c:forEach items="${categories}" var="categoryItem">
                    <option value="${categoryItem.id}">${categoryItem.name}</option>
                </c:forEach>

            </select>
        </c:if>
        <br/>
        ${addTagLabel}
        <br/>
        <input type="text" name="bookTag"/>
        <br/>
        <input type="submit" value="${createBookButtonText}"/>

    </form>
</div>