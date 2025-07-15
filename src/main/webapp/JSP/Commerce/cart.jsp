<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Giỏ hàng</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core-style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/css_cart.css">
    </head>
    <body>
        <div class="main-content-wrapper d-flex clearfix">
            <c:import url="/JSP/Home/Nav.jsp" />

            <!-- Cart Content Area -->
            <div class="cart-card-area">
                <c:choose>
                    <c:when test="${not empty sessionScope.cartItems}">
                        <form id="cartForm" method="post" action="s_cartBuy">
                            <div class="cart-card-list">
                                <c:forEach var="userEntry" items="${sessionScope.cartItems}">
                                    <c:forEach var="product" items="${userEntry.value}">
                                        <div class="cart-card">
                                            <input type="checkbox" name="productIds" value="${product.productId}" class="item-checkbox"/>
                                            <img class="cart-card-img" src="<c:choose>
                                                     <c:when test='${not empty product.images}'>
                                                         ${product.images[0].imageUrl}
                                                     </c:when>
                                                     <c:otherwise>/assets/img/no-image.png</c:otherwise>
                                                 </c:choose>" alt="${product.title}" />
                                            <div class="cart-card-info">
                                                <div class="cart-card-title">${product.title}</div>
                                                <div class="cart-card-price">
                                                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol=""/>
                                                    <span class="cart-card-currency">VND</span>
                                                </div>
                                                <div class="cart-card-qty">
                                                    <button type="button" class="qty-btn" data-id="${product.productId}" data-change="-1">-</button>
                                                    <input type="text" value="${product.quantity}" id="q-${product.productId}" readonly class="cart-qty-input">
                                                    <input type="hidden" name="qty_${product.productId}" value="${product.quantity}" />
                                                    <button type="button" class="qty-btn" data-id="${product.productId}" data-change="1">+</button>
                                                    <button type="button" class="cart-card-remove" onclick="removeFromCart('${product.productId}', this)">Xóa</button>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:forEach>
                            </div>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-cart">
                            <img src="${pageContext.request.contextPath}/img/cart_tutorial.png" alt="Empty cart" class="empty-img"/>
                            <h2>Giỏ hàng của bạn đang trống</h2>
                            <p>Hãy bắt đầu thêm sản phẩm vào giỏ ngay nhé.</p>
                            <a href="${pageContext.request.contextPath}/home" class="amado-btn">Bắt đầu mua sắm</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Cart Summary Sidebar -->
            <c:if test="${not empty sessionScope.cartItems}">
                <div class="cart-sidebar-box">
                    <div class="cart-sidebar-content">
                        <div class="cart-sidebar-row">
                            <input type="checkbox" id="sidebar-select-all" />
                            <label for="sidebar-select-all">Chọn Tất Cả (<span class="cart-sidebar-count"></span>)</label>
                        </div>
                        <div class="cart-sidebar-total-label">
                            Tổng cộng (<span class="cart-sidebar-count"></span> Sản phẩm ):
                        </div>
                        <div class="cart-sidebar-total">
                            <span class="cart-sidebar-total-value"></span> <span class="cart-sidebar-currency">VND</span>
                        </div>
                        <button type="submit" form="cartForm" class="cart-sidebar-checkout">Mua Hàng</button>
                    </div>
                </div>
            </c:if>
        </div>
        <script src="${pageContext.request.contextPath}/js/JS_cart.js"></script>
    </body>
</html>
