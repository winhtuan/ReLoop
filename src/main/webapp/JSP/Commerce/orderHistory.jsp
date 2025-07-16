<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.*" %>
<%@ page import="Model.entity.*" %>

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
        <link rel="stylesheet" href="css/conversation/CSS_chatbox.css"/>
        <link rel="icon" href="img/core-img/favicon.ico">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <link rel="stylesheet" href="css/core-style.css">
        <link rel="stylesheet" href="css/jsp_css/loader.css">
        <link rel="stylesheet" href="css/avatar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/notification.css">
        <link rel="stylesheet" href="css/orderHistory.css"/>
        <link rel="stylesheet" href="css/feedbackModal.css"/> <!-- Thêm CSS mới -->
    </head>
    <body>
        <!-- Page Preloder -->
        <div id="preloader">
            <div class="loader"></div>
        </div>

        <c:import url="/JSP/Home/Search.jsp" />
        <c:import url="/JSP/Conversation/chatBox.jsp"/>

        <!-- ##### Main Content Wrapper Start ##### -->
        <div class="main-content-wrapper d-flex clearfix">
            <c:import url="/JSP/Home/Nav.jsp" />
            <div class="container py-4" style="color: black;">
                <h2>🧾 Order History</h2>

                <c:forEach var="order" items="${orders}">
                    <div class="order-card" data-order-id="${order.orderId}">
                        <div class="order-header">
                            <h3><i class="fas fa-receipt"></i> Order ID: ${order.orderId}</h3>
                            <span class="order-status status-${order.status}">
                                <i class="fas fa-circle-notch"></i> ${order.status.toUpperCase()}
                            </span>
                        </div>

                        <div class="info-row">
                            <i class="far fa-calendar-alt"></i> Created Date: 
                            <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                        </div>

                        <div class="info-row">
                            <i class="fas fa-money-bill-wave"></i> Total Amount: 
                            <span class="total"><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="" groupingUsed="true" /> VND</span>
                        </div>

                        <div class="info-row">
                            <i class="fas fa-tag"></i> Discount: 
                            <fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol="" groupingUsed="true" /> VND
                        </div>

                        <div class="info-row">
                            <i class="fas fa-location-dot"></i> Shipping Address: ${order.shippingAddress}
                        </div>

                        <div class="product-list">
                            <h4><i class="fas fa-box"></i> Product Details</h4>
                            <ul>
                                <c:forEach var="item" items="${order.listItems}">
                                    <li>
                                        🆔 Product: ${item.productName} | 
                                        📦 Quantity: ${item.quantity} | 
                                        💵 Price: 
                                        <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="" groupingUsed="true" /> VND
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <!-- Buttons based on status -->
                        <c:choose>
                            <c:when test="${order.status == 'cancelled'}">
                                <!-- Không hiển thị nút Actions khi trạng thái là cancelled -->
                            </c:when>
                            <c:when test="${order.status == 'received'}">
                                <button class="feedback-trigger-btn" data-order-id="${order.orderId}">
                                    <i class="fas fa-star"></i> Rate Order
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="order-action-btn" data-order-id="${order.orderId}">
                                    <i class="fas fa-ellipsis-v"></i> Actions
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </div>

            <!-- Order Modal -->
            <div id="orderModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3><i class="fas fa-receipt"></i> Actions for Order: <span id="modalOrderId"></span></h3>
                        <span class="modal-close">×</span>
                    </div>
                    <div class="modal-body">
                        <button class="modal-btn cancel-order">Cancel Order</button>
                        <button class="modal-btn return-order">Return Order</button>
                        <button class="modal-btn received-order">Received</button>
                    </div>
                </div>
            </div>

            <!-- Feedback Modal -->
            <div id="feedbackModal" class="feedback-modal">
                <div class="feedback-modal-content">
                    <div class="feedback-modal-header">
                        <h3><i class="fas fa-star"></i> Rate Your Order</h3>
                        <span class="feedback-modal-close">×</span>
                    </div>
                    <div class="feedback-modal-body">
                        <form id="feedbackForm">
                            <p>Order ID: <span id="feedbackOrderId"></span></p>
                            <p>Please rate and comment on your experience:</p>
                            <div class="rating">
                                <input type="radio" id="star5" name="rating" value="5"><label for="star5">★</label>
                                <input type="radio" id="star4" name="rating" value="4"><label for="star4">★</label>
                                <input type="radio" id="star3" name="rating" value="3"><label for="star3">★</label>
                                <input type="radio" id="star2" name="rating" value="2"><label for="star2">★</label>
                                <input type="radio" id="star1" name="rating" value="1"><label for="star1">★</label>
                            </div>
                            <textarea id="feedbackComment" placeholder="Your comment..." rows="3"></textarea>
                            <button type="submit" class="feedback-modal-btn">Submit Feedback</button>
                        </form>
                    </div>
                </div>
            </div>

            <c:import url="/JSP/Home/Footer.jsp" />
            <!-- ##### jQuery (Necessary for All JavaScript Plugins) ##### -->
            <script src="js/jquery/jquery-2.2.4.min.js"></script>
            <!-- Popper js -->
            <script src="js/lib_js/popper.min.js"></script>
            <!-- Bootstrap js -->
            <script src="js/lib_js/bootstrap.min.js"></script>
            <!-- Plugins js -->
            <script src="js/lib_js/plugins.js"></script>
            <!-- js -->
            <script src="js/active.js"></script>
            <script src="js/JS_search.js"></script>
            <script src="${pageContext.request.contextPath}/js/search-menu.js"></script>
            <script src="${pageContext.request.contextPath}/js/notification.js"></script>
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
            <c:if test="${not empty sessionScope.Message}">
                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                <script>
                modal.classList.add('show');

                window.addEventListener("DOMContentLoaded", function () {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Notification',
                        text: "${fn:escapeXml(sessionScope.Message)}"
                    });
                });
                </script>
                <c:remove var="Message" scope="session" />
            </c:if>
            <c:if test="${not empty param.regis}">
                <script>
                    modal.classList.add('show');
                    loginAcessRegister.classList.add('active');
                </script>
            </c:if>
            <script src="js/conversation/JS_chatBox.js"></script>
            <script src="js/JS_search.js"></script>
            <script src="js/orderHistoryModal.js"></script>
            <script src="js/feedbackModal.js"></script> <!-- Thêm JS mới -->
    </body>
</html>