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
        <title>Reloop - User Profile</title>
        <link rel="icon" href="img/core-img/favicon.ico">

        <!-- External Libraries -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <script src="https://cdn.tailwindcss.com"></script>
        <script>
            tailwind.config = {
                theme: {
                    extend: {
                        colors: {
                            gold: {
                                100: '#FFF9E6',
                                200: '#FFEBB3',
                                500: '#FFD700',
                                600: '#E6C200',
                            }
                        }
                    }
                }
            }
        </script>

        <!-- Custom Styles -->
        <link rel="stylesheet" href="css/conversation/CSS_chatbox.css"/>
        <link rel="stylesheet" href="css/core-style.css">
        <link rel="stylesheet" href="css/jsp_css/loader.css">
        <link rel="stylesheet" href="css/avatar.css">
        <link rel="stylesheet" href="css/orderHistory.css"/>
    </head>
    <body class="bg-gray-50 min-h-screen">
        <!-- Page Preloader -->
        <div id="preloader"><div class="loader"></div></div>

        <!-- Shared Layouts -->
        <c:import url="/JSP/Home/Search.jsp" />
        <c:import url="/JSP/Conversation/chatBox.jsp"/>
        <div class="main-content-wrapper d-flex clearfix">
            <c:import url="/JSP/Home/Nav.jsp" />
            <div class="container mx-auto px-4 py-8">
                <jsp:include page="/JSP/User/followingProductBody.jsp" />
            </div>
            <c:import url="/JSP/Home/Footer.jsp" />
        </div>

        <!-- Scripts -->
        <script src="js/jquery/jquery-2.2.4.min.js"></script>
        <script src="js/lib_js/popper.min.js"></script>
        <script src="js/lib_js/bootstrap.min.js"></script>
        <script src="js/lib_js/plugins.js"></script>
        <script src="js/active.js"></script>
        <script src="js/JS_search.js"></script>
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
        <script src="js/conversation/JS_chatBox.js"></script>

        <!-- Preloader Fade-out -->
        <script>
            window.addEventListener("load", function () {
                const preloader = document.getElementById("preloader");
                preloader.style.opacity = "0";
                preloader.style.pointerEvents = "none";
                setTimeout(() => preloader.style.display = "none", 500);
            });
        </script>

        <!-- Alerts -->
        <c:if test="${not empty sessionScope.Message}">
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
            <script>
                window.addEventListener("DOMContentLoaded", function () {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Thông báo',
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

    </body>
</html>