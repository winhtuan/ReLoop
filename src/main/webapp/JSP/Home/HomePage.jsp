<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        <script src="js/active.js"></script>
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
    </body>
</html>