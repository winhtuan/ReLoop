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
        <link rel="icon" href="img/core-img/favicon.ico">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <link rel="stylesheet" href="css/core-style.css">
        <link rel="stylesheet" href="css/jsp_css/loader.css">
        <link rel="stylesheet" href="css/avatar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/notification.css">
        <link rel="stylesheet" href="css/css_cart.css"/>

    </head>
    <body>
        <!-- Page Preloder -->
        <div id="preloader">
            <div class="loader"></div>
        </div>

        <c:import url="/JSP/Home/Search.jsp" />

        <c:import url="/JSP/Commerce/cart.jsp" />
        
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
        <script src="${pageContext.request.contextPath}/js/notification.js"></script>
        <script src="${pageContext.request.contextPath}/js/search-menu.js"></script>
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
        <c:if test="${not empty requestScope.message}">
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
            <script>
                window.addEventListener("DOMContentLoaded", function () {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Thông báo',
                        text: "${fn:escapeXml(requestScope.message)}"
                    });
                });
            </script>
            <c:remove var="message" scope="request" />
        </c:if>
        <script>
            document.addEventListener("DOMContentLoaded", () => {
                // gắn sự kiện sau khi DOM đã sẵn sàng
                document.querySelectorAll(".item-checkbox")
                        .forEach(cb => cb.addEventListener("change", updateSubtotal));

                updateSubtotal();          // tính tổng lần đầu (sẽ = 0 vì chưa tick)
            });

        </script>
        <<script src="${pageContext.request.contextPath}/js/JS_search.js"></script>

    </body>
</html>
