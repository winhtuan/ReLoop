<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Mobile Nav (max width 767px)-->
<div class="mobile-nav">
    <!-- Navbar Brand -->
    <div class="amado-navbar-brand">
        <a href="home"><img src="${pageContext.request.contextPath}/img/core-img/logo.png" alt=""></a>
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
        <a href="${pageContext.request.contextPath}/home"><img src="${pageContext.request.contextPath}/img/core-img/logo.png" alt=""></a>
    </div>

    <!-- Amado Nav -->
    <nav class="amado-nav mt-30 mb-15">
        <ul>
            <li class="active"><a href="${pageContext.request.contextPath}/home">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/NewPostPage">New Post</a></li>
            <div class="category-dropdown-container">
                <li>
                    <a href="#" class="category-nav">CATEGORY</a>
                </li>

                <div class="category-dropdown">
                    <div class="category-container">
                        <c:import url="/JSP/Home/SearchCategory.jsp" />
                    </div>
                </div>
            </div>
            <li><a href="#">Shop</a></li>
                <c:choose>
                    <c:when test="${sessionScope.user != null}">
                    <li><a href="${pageContext.request.contextPath}/premium?user_id=${sessionScope.cus.userId}">Premium</a></li>
                    </c:when>
                    <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/callLogin">Premium</a></li>
                    </c:otherwise>
                </c:choose>
        </ul>
    </nav>
    <!-- Cart Menu -->
    <div class="cart-fav-search">

        <a href="#" class="search-nav"><ion-icon name="search-outline"></ion-icon> Search</a>
                <c:choose>
                    <c:when test="${sessionScope.user != null}">
                <a href="${pageContext.request.contextPath}/s_cart" class="cart-nav"><ion-icon name="cart-outline"></ion-icon> Cart <span style="color: #fbb710;">(<c:out value="${sessionScope.cartN}" default="0" />)</span></a>
                    </c:when>
                    <c:otherwise>
                <a href="${pageContext.request.contextPath}/callLogin" class="cart-nav"><ion-icon name="cart-outline"></ion-icon> Cart <span style="color: #fbb710;">(<c:out value="${sessionScope.cartN}" default="0" />)</span></a>
                    </c:otherwise>
                </c:choose>

        <div class="fav-nav favourite-container" id="favouriteContainer">
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <a href="${pageContext.request.contextPath}/s_favorite?userId=${sessionScope.user.userId}" ><ion-icon name="heart-outline"></ion-icon> 
                        Favourite
                    </a>                    
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/callLogin" ><ion-icon name="heart-outline"></ion-icon> 
                        Favourite
                    </a>                    
                </c:otherwise>
            </c:choose>           
        </div>

        <c:import url="/JSP/Home/Notification.jsp" />

        <c:choose>
            <c:when test="${cus != null}">
                <div class="nav-brand" id="join-in-btn">
                    <c:if test="${cus.isPremium}">
                        <div class="avatar-pro-container d-flex align-items-center gap-3">
                            <div class="avatar-wrapper position-relative">
                                <img src="${cus.srcImg}" alt="Avatar" class="avatar-pro">
                                <span class="badge-pro position-absolute bottom-0 start-50 translate-middle-x">pro</span>
                            </div>
                            <span class="name-pro">${cus.fullName}</span>
                        </div>
                    </c:if>
                    <c:if test="${!cus.isPremium}">
                        <a href="#" class="nav-user-toggle d-flex align-items-center gap-3">
                            <img src="${cus.srcImg}" alt="Avatar" class="user-avatar rounded-circle">
                            <span class="join-label">${cus.fullName}</span>
                        </a>
                    </c:if>
                    <ul id="menu" class="menu">
                        <li><a href="${pageContext.request.contextPath}/s_orderHistory" id="menu-item"><ion-icon name="bag-handle-outline"></ion-icon>Order History</a></li>
                        <li><a href="#" id="menu-item"><ion-icon name="newspaper-outline"></ion-icon>Manager Post</a></li>
                        <li><a href="${pageContext.request.contextPath}/UsersServlet" id="menu-item"><ion-icon name="chatbubble-ellipses-outline"></ion-icon>Message</a></li>
                        <li><a href="#" id="menu-item"><ion-icon name="document-text-outline"></ion-icon>Profile</a></li>
                        <li><a href="${pageContext.request.contextPath}/s_logout" id="menu-item" class="text-danger"><ion-icon name="log-out"></ion-icon> Log Out</a></li>
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

        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <a href="${pageContext.request.contextPath}/upPostServlet" class="item-btn-custom">
                    <ion-icon name="document-text-outline" class="btn-icon"></ion-icon>
                    <span>Up Post</span>
                </a>                    
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/callLogin" class="item-btn-custom">
                    <ion-icon name="document-text-outline" class="btn-icon"></ion-icon>
                    <span>Up Post</span>
                </a>                   
            </c:otherwise>
        </c:choose>

    </div>

</header>
<!-- Header Area End -->

<c:import url="/JSP/Authenticate/JoinIn.jsp" />