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
        <style>
             .booknow {
                width: 100% !important; 
                background: linear-gradient(135deg, #007bff, #0056b3);
                color: white;
                border: none;
                padding: 10px 16px;
                font-size: 14px;
                font-weight: bold;
                border-radius: 30px;
                cursor: pointer;
                transition: all 0.3s ease;
                box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
            }

            .booknow:hover {
                background: linear-gradient(135deg, #0056b3, #003580);
                transform: scale(1.05);
            }

            .booknow:active {
                transform: scale(0.95);
            }
            #productResults {
                max-height: 500px;
                overflow-y: auto;
                padding: 20px;
                display: flex;
                flex-wrap: wrap;
                justify-content: center;
                gap: 20px;
            }

            .product-item {
                width: 260px;
                height: 300px;
                background: white;
                border-radius: 20px;
                box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
                overflow: hidden;
                display: flex;
                flex-direction: column;
                align-items: center;
                text-align: center;
                padding: 20px 15px;
                transition: transform 0.3s ease, box-shadow 0.3s ease;
                position: relative;
            }

            .product-item:hover {
                transform: translateY(-5px);
                box-shadow: 0px 10px 25px rgba(0, 0, 0, 0.25);
            }

            .product-item img {
                width: 100%;
                height: 180px;
                object-fit: cover;
                border-radius: 12px;
            }

            .product-item h3 {
                margin-top: 15px;
                font-size: 1.1rem;
                color: #222;
                font-weight: 600;
            }

            .product-item p {
                font-size: 1rem;
                color: #444;
                margin: 8px 0;
            }

            .product-item p b {
                color: #000;
                font-weight: 700;
            }

            /* Nút "View more information" */
            #nutmore{
                padding-right: 20%;
            }

        </style>
    </head>
    <body>
        <!-- Page Preloder -->
        <div id="preloader">
            <div class="loader"></div>
        </div>

        <!-- Search Wrapper Area Start -->
        <div class="search-wrapper section-padding-100">
            <div class="search-close">
                <i class="fa fa-close" aria-hidden="true"></i>
            </div>
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="search-content d-flex justify-content-center">
                            <div class="position-relative w-100" id="searchBox" style="max-width: 700px;">
                                <input type="search" name="search" id="search" onkeyup="searchCar()" placeholder="Type your keyword...">
                                <a type="submit"><ion-icon name="search-outline"></ion-icon></a>
                            </div>
                            <div id="productResults"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Search Wrapper Area End -->
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
        <script src="js/JS_search.js"></script>
    </body>
</html>