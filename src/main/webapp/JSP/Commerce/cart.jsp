<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="Model.entity.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart</title>
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
                            <c:forEach var="userEntry" items="${sessionScope.cartItems}" varStatus="sellerStatus">
                                <div class="seller-block" data-seller-index="${sellerStatus.index}">
                                    <!-- Seller Checkbox -->
                                    <div class="seller-header">
                                        <input type="checkbox" class="seller-select-all" data-seller-index="${sellerStatus.index}" />
                                        <label>${userEntry.key.fullName}</label>
                                    </div>

                                    <!-- Product List for Seller -->
                                    <c:forEach var="product" items="${userEntry.value}">
                                        <div class="cart-card" data-seller-index="${sellerStatus.index}">
                                            <input type="checkbox" name="productIds" value="${product.productId}" class="item-checkbox"/>
                                            <img class="cart-card-img" src="<c:choose>
                                                <c:when test='${not empty product.images}'>
                                                    ${product.images[0].imageUrl}
                                                </c:when>
                                                <c:otherwise>/assets/img/no-image.png</c:otherwise>
                                            </c:choose>" alt="${product.title}" />
                                            
                                            <div class="cart-card-info">
                                                <div class="cart-card-title">${product.title}</div>
                                                
                                                <c:if test="${product.quantity == 0}">
                                                    <div style="color: red; font-weight: bold;">This product is out of stock</div>
                                                </c:if>
                                                
                                                <div class="cart-card-price">
                                                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol=""/>
                                                    <span class="cart-card-currency">VND</span>
                                                </div>
                                                
                                                <div class="cart-card-qty">
                                                    <button type="button" class="qty-btn" data-id="${product.productId}" data-change="-1">-</button>
                                                    <input type="text" value="${product.cQuantity}" id="q-${product.productId}" readonly class="cart-qty-input">
                                                    <input type="hidden" name="qty_${product.productId}" value="${product.cQuantity}" />
                                                    <input type="hidden" name="qtyr_${product.productId}" value="${product.quantity}" />
                                                    <button type="button" class="qty-btn" data-id="${product.productId}" data-change="1">+</button>
                                                    <button type="button" class="cart-card-remove" onclick="removeFromCart('${product.productId}', this)">Remove</button>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:forEach>
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <div class="empty-cart">
                        <img src="${pageContext.request.contextPath}/img/cart_tutorial.png" alt="Empty cart" class="empty-img"/>
                        <h2>Your cart is empty</h2>
                        <p>Start adding products to your cart now.</p>
                        <a href="${pageContext.request.contextPath}/home" class="amado-btn">Start Shopping</a>
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
                        <label for="sidebar-select-all">Select All (<span class="cart-sidebar-count"></span>)</label>
                    </div>
                    <div class="cart-sidebar-total-label">
                        Total (<span class="cart-sidebar-count"></span> items):
                    </div>
                    <div class="cart-sidebar-total">
                        <span class="cart-sidebar-total-value"></span> <span class="cart-sidebar-currency">VND</span>
                    </div>
                    <button type="submit" form="cartForm" class="cart-sidebar-checkout">Checkout</button>
                </div>
            </div>
        </c:if>
    </div>
</body>
</html>
