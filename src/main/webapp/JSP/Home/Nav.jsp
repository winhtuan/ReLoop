<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Mobile Nav (max width 767px)-->
<div class="mobile-nav">
    <!-- Navbar Brand -->
    <div class="amado-navbar-brand">
        <a href="home"><img src="img/core-img/logo.png" alt=""></a>
    </div>
    <!-- Navbar Toggler -->
    <div class="amado-navbar-toggler">
        <span></span><span></span><span></span>
    </div>
</div>

<!-- Header Area Start -->
<header class="header-area clearfix">
    <!-- Close Icon -->
    <div class="nav-close">
        <i class="fa fa-close" aria-hidden="true"></i>
    </div>
    <!-- Logo -->
    <div class="logo">
        <a href="home"><img src="img/core-img/logo.png" alt=""></a>
    </div>

    <!-- Amado Nav -->
    <nav class="amado-nav mt-30 mb-15">
        <ul>
            <li class="active"><a href="home">Home</a></li>
            <li><a href="#">About us</a></li>
            <li><a href="#">Post</a></li>
            <li><a href="#">Shop</a></li>
            <li><a href="premium?user_id=${sessionScope.cus.userId}">Premium</a></li>
        </ul>
    </nav>
    <!-- Cart Menu -->
    <div class="cart-fav-search">
        <a href="#" class="search-nav"><ion-icon name="search-outline"></ion-icon> Search</a>
        <a href="cart.html" class="cart-nav"><ion-icon name="cart-outline"></ion-icon> Cart <span>(0)</span></a>

        <div class="fav-nav favourite-container" id="favouriteContainer">
            <a href="#" id="favouriteLink"><ion-icon name="heart-outline"></ion-icon> 
                Favourite
            </a>
            <!-- Khung hiện yêu thích -->
            <div id="favouriteBox" class="favourite-box">
                <p>No Items In Your <strong>Favourite List</strong></p>
                <!-- Một mục yêu thích -->
                <div class="favourite-item">
                    <img src="img/product-img/pro-big-1.jpg" alt="Product" class="img-thumbnail">

                    <div class="fav-left-content">
                        <div class="fav-tittle">
                            <span><strong>Leather wallet</strong></span>
                            <span>$99.00</span>
                        </div>
                        <div class="fav-posted-by">
                            <ion-icon name="person-circle-outline"></ion-icon>
                            <span class="ms-1">Lisa</span>
                        </div>
                    </div>
                    <div class="fav-actions">
                        <form action="" method="">
                            <button title="Remove">
                                <ion-icon name="heart-outline" class="del-fav-item"></ion-icon>
                            </button>
                        </form>
                        <button title="Send">
                            <ion-icon name="mail-outline" class="mes-fav-item"></ion-icon>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="not-nav notification-container" id="notificationContainer">
            <a href="#" id="notificationLink">
                <ion-icon name="notifications-outline"></ion-icon> Notification
            </a>

            <!-- Khung hiện thông báo -->
            <div id="notificationBox" class="notification-box">
                <p style="text-align: center; font-size: 17px;">No New <strong>Notifications</strong></p>

                <!-- Các dòng thông báo -->
                <div class="notification-item">
                    <div class="notification-text">
                        <p><strong>Message </strong>Hung send</p>
                        <p>Content message.</p>
                    </div>
                </div>
                <div class="notification-item">
                    <div class="notification-text">
                        <p><strong>New Post </strong>From Hung</p>
                        <p>Product.</p>
                    </div>
                </div>

                <!-- Nút xem thêm -->
                <div id="seeMoreNotifications" style="text-align: center; margin-top: 5px;">
                    <a href="#" style="font-size: 14px; color: #fbb710; cursor: pointer;">Xem thêm</a>
                </div>
            </div>
        </div>

        <!-- Message Icon Button -->
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <a href="${pageContext.request.contextPath}/UsersServlet" class="search-nav">
                    <ion-icon name="chatbubble-outline"></ion-icon> Message
                </a>
            </c:when>
        </c:choose>

        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <div class="nav-brand" id="join-in-btn">
                    <a href="#">
                        <ion-icon name="bug-outline"></ion-icon> ${sessionScope.cus.fullName}
                    </a>
                    <ul id="menu" class="menu">
                        <li><a href="#" id="menu-item"><ion-icon name="bag-handle-outline"></ion-icon>Order History</a></li>
                        <li><a href="#" id="menu-item"><ion-icon name="newspaper-outline"></ion-icon>Manager Post</a></li>
                        <li><a href="#" id="menu-item"><ion-icon name="chatbubble-ellipses-outline"></ion-icon>Message</a></li>
                        <li><a href="#" id="menu-item"><ion-icon name="document-text-outline"></ion-icon>Profile</a></li>
                        <li><a href="s_logout" id="menu-item" class="text-danger"><ion-icon name="log-out"></ion-icon> Log Out</a></li>
                    </ul>
                </div>
            </c:when>
            <c:otherwise>
                <a href="#" id="joinInBtn">
                    <ion-icon name="person-outline"></ion-icon> Join In
                </a>
            </c:otherwise>
        </c:choose>
    </div>
    <!-- Button Group -->
    <div class="amado-btn-group mt-15 mb-100">
        <a href="#" class="item-btn-custom">
            <ion-icon name="document-text-outline" class="btn-icon"></ion-icon>
            <span>Up Post</span>
        </a>
    </div>

</header>
<!-- Header Area End -->

<c:import url="/JSP/Authenticate/JoinIn.jsp" />