<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="products-catagories-area clearfix">
    <div class="container">
        <c:import url="/JSP/Post/CateNav.jsp" />

        <section class="headPost"></section>

        <div class="row" id="product-list">
            <c:forEach var="product" items="${sessionScope.allPost}">
                <div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xxl-2-4 mb-4 product-card-wrap">
                    <div class="product-card h-100">
                        <div class="product-image-wrapper">
                            <a href="s_productDetail?productId=${product.productId}">
                                <img src="<c:choose>
                                         <c:when test="${not empty product.images}">
                                             ${product.images[0].imageUrl}
                                         </c:when>
                                         <c:otherwise>
                                             img/product-img/default.jpg
                                         </c:otherwise>
                                     </c:choose>" class="product-image" />
                            </a>
                            <div class="product-actions">
                                <form action="s_favorite" method="post">
                                    <input type="hidden" name="userId" value="${sessionScope.user.userId}">
                                    <input type="hidden" name="productId" value="${product.productId}">
                                    <button type="submit" class="btn-action wishlist" title="Thêm vào yêu thích"><ion-icon name="heart-outline"></ion-icon></button>
                                </form>

                                <form action="s_cartBuy" method="post">
                                    <input type="hidden" name="productIds" value="${product.productId}" />
                                    <input type="hidden" name="qty_${product.productId}" value="1" />
                                    <button type="submit" class="btn-action buy-now" title="Mua ngay">
                                        <ion-icon name="flash-outline"></ion-icon>
                                    </button>
                                </form>

                                <button class="btn-action add-to-cart" data-productid="${product.productId}" title="Thêm vào giỏ hàng">
                                    <ion-icon name="cart-outline"></ion-icon>
                                </button>


                            </div>
                        </div>
                        <div class="product-info p-3">
                            <a href="s_productDetail?productId=${product.productId}" class="text-dark"><h6>${product.title}</h6></a>
                            <div class="d-flex align-items-center mb-2">
                                <span class="product-price font-bold me-2">
                                    <fmt:formatNumber value="${product.price}" pattern="#,###"/> VNĐ
                                </span>
                                <span class="badge bg-success">
                                    <c:choose>
                                        <c:when test="${product.status eq 'available'}">Còn bán</c:when>
                                        <c:otherwise>Hết hàng</c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                            <div class="text-muted small mb-2">
                                <ion-icon name="location-outline"></ion-icon> ${product.location}
                            </div>
                            <div class="product-description mb-2">${product.description}</div>
                            <div class="d-flex justify-content-between small text-muted">
                                <span>
                                    <ion-icon name="calendar-outline"></ion-icon>
                                        <fmt:formatDate value="${product.createdAt}" pattern="dd/MM/yyyy"/>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <nav aria-label="Page navigation" class="d-flex justify-content-center mt-4">
                <ul class="pagination" id="pagination"></ul>
            </nav>
        </div>

    </div>
</div>
