<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
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
        <link rel="icon" href="img/core-img/favicon.ico">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <link rel="stylesheet" href="css/core-style.css">
        <link rel="stylesheet" href="css/jsp_css/loader.css">
        <link rel="stylesheet" href="css/avatar.css">
        <link rel="stylesheet" href="css/css_cart.css"/>

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
            <c:choose>
                <c:when test="${not empty sessionScope.cartItems}">
                    <div class="wrapper">

                        <div class="cart-left">
                            <div class="cart-title">Shopping Cart</div>

                            <div class="thead">
                                <div></div>
                                <div></div>
                                <div >Name</div>
                                <div>Price</div>
                                <div>Quantity</div>
                                <div></div>
                            </div>


                            <c:forEach var="item" items="${sessionScope.cartItems}">
                                <div class="item">
                                    <div class="col checkbox">
                                        <input type="checkbox" class="item-checkbox" checked />
                                    </div>

                                    <div class="col image">
                                        <img src="<c:choose>
                                                 <c:when test='${not empty item.images}'>
                                                     ${item.images[0].imageUrl}
                                                 </c:when>
                                                 <c:otherwise>/assets/img/no-image.png</c:otherwise>
                                             </c:choose>" alt="${item.title}" />
                                    </div>

                                    <div class="col name">${item.title}</div>

                                    <div class="col price" data-price="${item.price}">
                                        <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="VND"/>
                                    </div>

                                    <div class="col qty">
                                        <button class="dec" data-id="${item.productId}" onclick="updateQty(this, -1)">–</button>
                                        <span id="q-${item.productId}">${item.quantity}</span>
                                        <button class="inc" data-id="${item.productId}" onclick="updateQty(this, 1)">+</button>
                                    </div>

                                    <div class="col delete">
                                        <button class="delete-btn" onclick="removeFromCart('${item.productId}', this)">🗑</button>
                                    </div>
                                </div>

                            </c:forEach>
                        </div>

                        <div class="cart-right">
                            <h3>Cart Total</h3>

                            <c:set var="sub" value="0" scope="page"/>
                            <c:forEach var="item" items="${sessionScope.cartItems}">
                                <c:set var="sub"
                                       value="${sub + item.price * item.quantity}" scope="page"/>
                            </c:forEach>

                            <div class="line" style="font-weight:600">
                                <span>Total:</span>
                                <span><fmt:formatNumber value="${sub}" type="currency" currencySymbol="VND"/></span>
                            </div>

                            <button class="checkout" onclick="location.href = '${pageContext.request.contextPath}/checkout'">
                                Checkout
                            </button>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-cart">
                        <img src="${pageContext.request.contextPath}/img/cart_tutorial.png"
                             alt="Empty cart" class="empty-img"/>

                        <h2>Your cart is empty</h2>
                        <p>Looks like you haven’t added anything yet.</p>

                        <a href="${pageContext.request.contextPath}/JSP/Home/HomePage.jsp" class="browse-btn">
                            Start shopping
                        </a>
                    </div>
                </c:otherwise>

            </c:choose>
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
        <script src="js/JS_cart.js"></script>
    </body>
</html>
