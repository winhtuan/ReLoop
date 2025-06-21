<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="description" content="">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <!-- Title  -->
        <title>Reloop</title>
        <!-- Favicon  -->
        <link rel="icon" href="${pageContext.request.contextPath}/img/core-img/favicon.ico">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core-style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jsp_css/loader.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/avatar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/css_cart.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/checkout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search.css"/>
    </head>
    <body>
        <!-- Page Preloder -->
        <div id="preloader">
            <div class="loader"></div>
        </div>

        <c:import url="/JSP/Home/Search.jsp" />

        <!-- ##### Main Content Wrapper Start ##### -->
        <div class="main-content-wrapper d-flex clearfix">
            <c:import url="/JSP/Home/Nav.jsp" />
            <jsp:useBean id="pendingOrders" scope="session" type="java.util.List" />
            <div class="checkout-container">

                <!-- Left: Danh sách đơn -->
                <div class="checkout-left">
                    <h2>Danh sách đơn hàng cần thanh toán</h2>

                    <c:set var="grandTotal" value="0" scope="page" />

                    <c:forEach var="order" items="${pendingOrders}">
                        <div class="order-block">
                            <!-- Header đơn hàng -->
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                                <div>
                                    <h3 style="margin: 0;">🧾 Đơn hàng 
                                        #${fn:substring(order.orderId, fn:length(order.orderId) - 4, fn:length(order.orderId))}
                                    </h3>
                                    <p style="margin: 3px 0;">
                                        👤 <strong>Người bán:</strong> ${order.listItems[0].sellerName}
                                    </p>
                                    <p style="margin: 3px 0;">
                                        🔁 <strong>Trạng thái:</strong> <span style="color: orange;">${order.status}</span>
                                    </p>
                                </div>
                                <div style="text-align: right;">
                                    <p style="margin: 0;">💰 <strong>Tổng tiền:</strong></p>
                                    <p style="font-size: 18px; color: red; font-weight: bold;">
                                    <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="VND" />
                                    </p>
                                </div>
                            </div>

                            <!-- Danh sách sản phẩm -->
                            <ul class="order-items">
                                <c:forEach var="item" items="${order.listItems}">
                                    <li>
                                        📦 <strong>${item.productName}</strong><br/>
                                        <small>
                                            🔢 Số lượng: ${item.quantity} 
                                            💵 <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="VND"/>
                                        </small>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>

                        <c:set var="grandTotal" value="${grandTotal + order.totalAmount}" />
                    </c:forEach>

                </div>

                <!-- Right: Tổng cộng & nút -->
                <div class="checkout-right">
                    <h3>Cart Total</h3>
                    <div class="checkout-total">
                        Tổng cộng: 
                        <span><fmt:formatNumber value="${grandTotal}" type="currency" currencySymbol="VND"/></span>
                    </div>

                    <form action="${pageContext.request.contextPath}/s_cartBuy" method="get">
                        <button type="submit" class="btn-checkout">Xác nhận thanh toán</button>
                    </form>
                </div>
            </div>
        </div>

        <c:import url="/JSP/Home/Footer.jsp" />
        <!-- ##### jQuery (Necessary for All JavaScript Plugins) ##### -->
        <script src="${pageContext.request.contextPath}/js/jquery/jquery-2.2.4.min.js"></script>
        <!-- Popper js -->
        <script src="${pageContext.request.contextPath}/js/lib_js/popper.min.js"></script>
        <!-- Bootstrap js -->
        <script src="${pageContext.request.contextPath}/js/lib_js/bootstrap.min.js"></script>
        <!-- Plugins js -->
        <script src="${pageContext.request.contextPath}/js/lib_js/plugins.js"></script>
        <!-- js -->
        <script src="${pageContext.request.contextPath}/js/active.js"></script>
        <script src="${pageContext.request.contextPath}/js/JS_search.js"></script>
        <!-- Ion Icons -->
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
        <script>
            const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
            window.addEventListener("load", function () {
                const preloader = document.getElementById("preloader");
                preloader.style.opacity = "0";
                preloader.style.pointerEvents = "none";
                setTimeout(() => preloader.style.display = "none", 500); // Ẩn hẳn sau fade out
            });

        </script>
        <c:if test="${not empty requestScope.openLogin}">
            <script>
                console.warn("Hàm openModal không tồn tại.");
                window.addEventListener("DOMContentLoaded", function () {
                    if (typeof openModal === 'function') {
                        openModal();
                    } else {
                        console.warn("Hàm openModal không tồn tại.");
                    }
                });
            </script>
        </c:if>

    </body>
</html>