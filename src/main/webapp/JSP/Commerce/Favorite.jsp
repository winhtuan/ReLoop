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
        <link rel="stylesheet" href="css/avatar.css">
        <link rel="stylesheet" href="css/favorite.css"/>

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
            <div class="wrapper" style="color: black;">
                <h2>Your Favorite Products</h2>

                <c:choose>
                    <c:when test="${not empty favorites}">
                        <table>
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Price</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="product" items="${favorites}">
                                    <tr>
                                        <td>
                                            <img class="product-img" src="${product.images[0].imageUrl}" alt="product">
                                        </td>
                                        <td>${product.title}</td>
                                        <td>${product.description}</td>
                                        <td><c:out value="${product.price}"/> VND</td>
                                        <td>
                                            <form action="s_favorite" method="post">
                                                <input type="hidden" name="userId" value="${sessionScope.user.userId}">
                                                <input type="hidden" name="productId" value="${product.productId}">
                                                <input type="hidden" name="state" value="unfavorite">
                                                <button type="submit" class="btn-action" title="Bỏ khỏi yêu thích">
                                                    <ion-icon name="heart-dislike-outline"></ion-icon>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="no-favorites">
                            <p>You haven't added any products to your favorites yet.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
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