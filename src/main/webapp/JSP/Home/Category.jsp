<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="category-container">
    <ul class="main-category-list">
        <c:forEach var="cat" items="${categoryList}">
            <c:if test="${cat.level == 0}">
                <li class="main-category-item">
                    <c:url var="mainCategoryUrl" value="categoryViewServlet">
                        <c:param name="slug" value="${cat.slug}"/>
                    </c:url>
                    <a class="main-category-label" href="${mainCategoryUrl}">
                        <c:choose>
                            <c:when test="${cat.name eq 'Books & Documents'}">
                                <ion-icon name="book-outline"></ion-icon>
                                </c:when>
                                <c:when test="${cat.name eq 'Digital Goods'}">
                                <ion-icon name="laptop-outline"></ion-icon>
                                </c:when>
                                <c:when test="${cat.name eq 'Electronics'}">
                                <ion-icon name="hardware-chip-outline"></ion-icon>
                                </c:when>
                                <c:when test="${cat.name eq 'Fashion'}">
                                <ion-icon name="shirt-outline"></ion-icon>
                                </c:when>
                                <c:when test="${cat.name eq 'Furniture'}">
                                <ion-icon name="bed-outline"></ion-icon>
                                </c:when>
                                <c:when test="${cat.name eq 'Home Appliances'}">
                                <ion-icon name="home-outline"></ion-icon>
                                </c:when>
                                <c:when test="${cat.name eq 'Jewelry'}">
                                <ion-icon name="sparkles-outline"></ion-icon>
                                </c:when>
                                <c:when test="${cat.name eq 'Pets'}">
                                <ion-icon name="paw-outline"></ion-icon>
                                </c:when>
                                <c:otherwise>
                                <ion-icon name="folder-outline"></ion-icon>
                                </c:otherwise>
                            </c:choose>
                            ${cat.name}
                    </a>
                    <ul class="sub-category-list">
                        <c:forEach var="sub" items="${categoryList}">
                            <c:if test="${sub.parentId == cat.categoryId}">
                                <li class="sub-category-item">
                                    <a href="categoryViewServlet?slug=${sub.slug}">
                                        <ion-icon name="chevron-forward-outline"></ion-icon> ${sub.name}
                                    </a>
                                </li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </li>
            </c:if>
        </c:forEach>
    </ul>
</div>
