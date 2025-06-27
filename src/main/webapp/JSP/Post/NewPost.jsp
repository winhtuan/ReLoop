<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="description" content="">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <!-- Title  -->
        <title>New Post</title>
        <!-- Favicon  -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="css/post.css" />
        <link rel="stylesheet" href="css/avatar.css">
        <link rel="stylesheet" href="css/core-style.css">
        <link rel="stylesheet" href="css/jsp_css/loader.css">

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
            <c:import url="/JSP/Post/Post.jsp" />
        </div>

        <c:import url="/JSP/Home/Footer.jsp" />

        <script src="js/jquery/jquery-2.2.4.min.js"></script>
        <script src="js/lib_js/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="js/lib_js/plugins.js"></script>
        <script src="js/active.js"></script>
        <script src="js/JS_search.js"></script>
        <script src="js/post.js"></script>
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
    </body>
</html>
