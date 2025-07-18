<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        <link rel="stylesheet" href="css/conversation/CSS_chatbox.css"/>
        <link rel="icon" href="img/core-img/favicon.ico">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <link rel="stylesheet" href="css/core-style.css">
        <link rel="stylesheet" href="css/jsp_css/loader.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/category-menu.css">
        <link rel="stylesheet" href="css/avatar.css">
        <link rel="stylesheet" href="css/notification.css">
        <link rel="stylesheet" href="css/category-menu.css">
        
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
            <c:import url="/JSP/Home/Catagory.jsp" />
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
        <script src="${pageContext.request.contextPath}/js/active.js"></script>
        <script src="${pageContext.request.contextPath}/js/JS_search.js"></script>
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
        <!<!-- category -->
        <script src="js/dropdown-handler.js"></script>
        <c:if test="${not empty sessionScope.Message}">
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
            <script>
            modal.classList.add('show');

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

        <script src="js/conversation/JS_chatBox.js"></script>

    </body>
</html>