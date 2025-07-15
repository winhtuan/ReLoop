<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search.css">
    <body>
        <!-- Search Wrapper Area Start -->
        <div class="search-wrapper section-padding-100">
            <div class="search-close">
                <i class="fa fa-close" aria-hidden="true"></i>
            </div>
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="search_box search-content d-flex justify-content-center">
                            <div class="position-relative w-100" style="max-width: 700px;">
                                <div class="category-dropdown-container">
                                    <a href="#" class="category-nav">
                                        <ion-icon name="menu-outline"></ion-icon> CATEGORY
                                    </a>

                                    <div class="category-dropdown">
                                        <div class="category-container">
                                            <c:import url="/JSP/Home/SearchCategory.jsp" />
                                        </div>
                                    </div>
                                </div>
                                <form action="s_search" method="post">
                                    <input type="search" name="search" id="search" onkeyup="delaySearch()" placeholder="Type your keyword...">
                                    <!-- Icon search bên trái -->
                                    <button type="submit" class="search-btn">
                                        <ion-icon name="search-outline"></ion-icon>
                                    </button>
                                    <!--Icon close bên phải--> 
                                    <button type="button" class="clear-btn">
                                        <ion-icon name="close-outline"></ion-icon>
                                    </button>
                                </form>
                            </div>
                            <div id="productResults" class="search-results-container"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Search Wrapper Area End -->



    </body>
</html>
