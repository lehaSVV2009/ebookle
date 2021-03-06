<div xmlns:jsp="http://java.sun.com/JSP/Page"
     xmlns:c="http://java.sun.com/jsp/jstl/core"
     xmlns:spring="http://www.springframework.org/tags"
     version="2.0">
    <jsp:directive.page contentType="text/html; charset=UTF-8"/>
    <jsp:directive.page pageEncoding="UTF-8"/>

    <jsp:output omit-xml-declaration="yes"/>

    <spring:message code="label.tags" var="labelTags"/>
    <spring:message code="label.add_tag" var="labelAddTag"/>
    <spring:message code="label.add_like" var="labelLike"/>
    <spring:message code="label.create_new_chapter" var="labelCreateChapter"/>
    <spring:message code="label.edit_chapter" var="labelEdit"/>
    <spring:message code="label.remove_chapter" var="labelRemove"/>
    <spring:message code="label.show_chapter" var="labelShow"/>
    <spring:message code="label.save_chapter" var="labelSave"/>
    <spring:message code="label.add_dislike" var="labelDislike"/>

    <table>
        <tr>
            <h2>${book.title} </h2>
        </tr>
        <tr>
            <h2>${currentChapter.title}</h2>
        </tr>
    </table>
    <c:choose>
        <!--for Users who can edit -->
        <c:when test="${person eq 'ownUser'}">
            <c:choose>
                <!--edit book-->
                <c:when test="${userAction eq 'edit'}">

                    <div class="well leftSidePanel">
                        <!--<c:url var="url" value=""><c:param name="bookTitle" value="${bookTitle}" /></c:url>-->
                        <!--<c:url value="/${userLogin}/editBook/${bookTitle}/createNewChapter" var="linkCreateNewChapter"/>-->
                        <c:out value="/${userLogin}/editBook/${bookTitle}/createNewChapter" escapeXml="true" />
                        <a href="${linkCreateNewChapter}">${labelCreateChapter}</a>
                        <table class="table table-bordered leftSideTable">
                            <c:forEach items="${book.chapters}" var="chapter">
                                <tr>
                                    <td>
                                        <form action="/${userLogin}/editBook/${bookTitle}/${chapter.chapterNumber}"
                                              method="get">
                                            <input type="submit" value="${chapter.title}"/>
                                        </form>
                                    </td>
                                    <td>
                                        <a href="/${userLogin}/editBook/${bookTitle}/${chapter.chapterNumber}/deleteChapter">
                                            <img src="http://localhost:8080/web-resources/img/delete16.png"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                        <br/>

                        <form action="/${userLogin}/editBook/${bookTitle}/${currentChapter.chapterNumber}/addTag"
                              method="post">
                            <input type="text" name="bookTag" placeholder="${labelAddTag}"/>
                        </form>
                        <br/>
                        ${labelTags}:
                        <br/>
                        <c:if test="${not empty tags}">
                            <c:forEach items="${tags}" var="tag">
                                ${tag.bookTag}
                                <br/>
                            </c:forEach>
                        </c:if>
                    </div>


                    <div class="well content">
                        <form action="/${userLogin}/editBook/${bookTitle}/${currentChapter.chapterNumber}" method="get">
                            <input type="submit" value="${labelEdit}"/>
                        </form>
                        <form action="/${userLogin}/editBook/${bookTitle}/${currentChapter.chapterNumber}/show"
                              method="get">
                            <input type="submit" value="${labelShow}"/>
                        </form>
                        <form action="/${userLogin}/editBook/${bookTitle}/${currentChapter.chapterNumber}/save"
                              method="post">
                            <textarea name="text" class="reader">${currentChapter.text}</textarea>
                            <br/>
                            <input type="submit" value="${labelSave}"/>
                        </form>

                    </div>
                </c:when>

                <!--read book-->

                <c:otherwise>
                    <div class="well leftSidePanel">
                        <table class="table table-bordered leftSideTable">
                            <c:forEach items="${book.chapters}" var="chapter">
                                <tr>
                                    <td>
                                        <form action="/${userLogin}/editBook/${bookTitle}/${chapter.chapterNumber}/show"
                                              method="get">
                                            <input type="submit" value="${chapter.title}"/>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                        <br/>
                        ${labelTags}:
                        <br/>
                        <c:if test="${not empty tags}">
                            <c:forEach items="${tags}" var="tag">
                                ${tag.bookTag}
                                <br/>
                            </c:forEach>
                        </c:if>
                    </div>
                    <div class="well content">

                        <!--TODO: change position-->
                        <form action="/${userLogin}/editBook/${bookTitle}/${currentChapter.chapterNumber}" method="get">
                            <input type="submit" value="${labelEdit}"/>
                        </form>
                        <form action="/${userLogin}/editBook/${bookTitle}/${currentChapter.chapterNumber}/show"
                              method="get">
                            <input type="submit" value="${labelShow}"/>
                        </form>
                        <div disabled="disabled" class="reader">${htmlChapterText}</div>

                        <br/>
                        <img src="http://localhost:8080/web-resources/img/rating.png"/>
                        ${book.rating}
                    </div>
                </c:otherwise>
            </c:choose>

        </c:when>

        <!--for user who cant edit-->
        <c:otherwise>

            <div class="well leftSidePanel">
                <table class="table table-bordered leftSideTable">
                    <c:forEach items="${book.chapters}" var="chapter">
                        <tr>
                            <td>
                                <form action="/${userLogin}/editBook/${bookTitle}/${chapter.chapterNumber}/show"
                                      method="get">
                                    <input type="submit" value="${chapter.title}"/>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br/>
                ${labelTags}:
                <br/>
                <c:if test="${not empty tags}">
                    <c:forEach items="${tags}" var="tag">
                        ${tag.bookTag}
                        <br/>
                    </c:forEach>
                </c:if>
            </div>
            <div class="well content">
                <div disabled="disabled" class="reader">${htmlChapterText}</div>

                <br/>

                <img src="http://localhost:8080/web-resources/img/rating.png"/>
                ${book.rating}
                <c:if test="${person eq 'notOwnUser'}">
                    <spring:url value="/${userLogin}/editBook/${bookTitle}/${currentChapter.chapterNumber}/show/1"
                                var="likeUrl"/>
                    <spring:url value="/${userLogin}/editBook/${bookTitle}/${currentChapter.chapterNumber}/show/-1"
                                var="dislikeUrl"/>

                    <c:if test="${mark ne 'showJustDislike'}">
                        <a href="${likeUrl}">
                            <img src="http://localhost:8080/web-resources/img/like.png"/>
                            <!--${labelLike}-->
                        </a>
                    </c:if>
                    <c:if test="${mark ne 'showJustLike'}">
                        <a href="${dislikeUrl}">
                            <img src="http://localhost:8080/web-resources/img/dislike.png"/>
                            <!--${labelDislike}-->
                        </a>
                    </c:if>

                    <div style="float : right">
                        <a href="/${userLogin}/editBook/${bookTitle}/${chapterNumber}/savePdf">
                            <img src="http://localhost:8080/web-resources/img/pdf.png"/>
                        </a>
                    </div>
                </c:if>

            </div>
        </c:otherwise>

    </c:choose>

</div>