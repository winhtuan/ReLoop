<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!--  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core-style.css" />-->
<!-- ##### Footer Area Start ##### -->
<footer class="footer_area clearfix">
    <div class="container">
        <div class="row align-items-center">
            <!-- Single Widget Area -->
            <div class="col-12 col-lg-4">
                <div class="single_widget_area">
                    <!-- Logo -->
                    <div class="footer-logo mr-50">
                        <a href="${contextPath}/index.jsp"><img src="${contextPath}/img/core-img/logo2.png" alt=""></a>
                    </div>
                    <!-- Copywrite Text -->
                    <p class="copywrite">
                        &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved | Your trusted platform for buying, selling, and exchanging second-hand goods.
                    </p>
                </div>
            </div>
            <!-- Single Widget Area -->
            <div class="col-12 col-lg-8">
                <div class="single_widget_area">
                    <!-- Footer Menu -->
                    <div class="footer_menu">
                        <nav class="navbar navbar-expand-lg justify-content-end">
                            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#footerNavContent" aria-controls="footerNavContent" aria-expanded="false" aria-label="Toggle navigation"><i class="fa fa-bars"></i></button>
                            <div class="collapse navbar-collapse" id="footerNavContent">
                                <ul class="navbar-nav ml-auto">
                                    <li class="nav-item active">
                                        <a class="nav-link" href="${contextPath}/index.jsp">Home</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${contextPath}/about.jsp">About us</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${contextPath}/post.jsp">Post</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${contextPath}/cart.jsp">Cart</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${contextPath}/premium.jsp">Premium</a>
                                    </li>
                                </ul>
                            </div>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </div>
</footer>
<!-- ##### Footer Area End ##### -->
