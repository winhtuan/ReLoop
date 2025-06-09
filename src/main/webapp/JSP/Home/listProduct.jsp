<%-- 
    Document   : listProduct
    Created on : Jun 9, 2025, 7:50:30 AM
    Author     : Thanh Loc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="description" content="">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <!-- The above 4 meta tags *must* come first in the head; any other head content must come *after* these tags -->

        <!-- Title  -->
        <title>Amado - Furniture Ecommerce Template | Shop</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <!-- Favicon  -->
        <link rel="stylesheet" href="css/core-style.css">
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <header>
            <div>
                <div>
                    <nav>
                        <ul>
                            <li><a href="#">Home</a></li>
                            <li><a href="#">About us</a></li>
                            <li><a href="#">Post</a></li>
                            <li><a href="#">Shop</a></li>
                            <li><a href="#">Premium</a></li>
                        </ul>
                        <div>
                            <a href="#">Search</a>
                            <a href="#">Cart (0)</a>
                            <a href="#">Favourite (0)</a>
                            <a href="#">Join In</a>
                        </div>
                        <a href="#">Up Post</a>
                    </nav>
                </div>
            </div>
        </header>

        <main>
            <div>
                <aside>
                    <div>
                        <h3>Categories</h3>
                        <ul>
                            <li><a href="#">Chairs</a></li>
                            <li><a href="#">Beds</a></li>
                            <li><a href="#">Accessories</a></li>
                            <li><a href="#">Furniture</a></li>
                            <li><a href="#">Home Deco</a></li>
                            <li><a href="#">Dressings</a></li>
                            <li><a href="#">Tables</a></li>
                        </ul>
                    </div>
                    <div>
                        <h3>Brands</h3>
                        <ul>
                            <li><a href="#">Amado</a></li>
                            <li><a href="#">Ikea</a></li>
                            <li><a href="#">Furniture Inc</a></li>
                            <li><a href="#">The factory</a></li>
                            <li><a href="#">Artdeco</a></li>
                        </ul>
                    </div>
                    <div>
                        <h3>Color</h3>
                        <div>
                            <span style="background-color: red;"></span>
                            <span style="background-color: blue;"></span>
                            <span style="background-color: green;"></span>
                            <span style="background-color: yellow;"></span>
                            <span style="background-color: black;"></span>
                            <span style="background-color: white;"></span>
                        </div>
                    </div>
                    <div>
                        <h3>Price</h3>
                        <div>
                            <input type="range" min="10" max="1000" value="500">
                            <p>$10 - $1000</p>
                        </div>
                    </div>
                </aside>

                <section>
                    <div>
                        <div>
                            <h3>Showing 1-8 of ${sessionScope.listProduct != null ? sessionScope.listProduct.size() : 0}</h3>
                            <div>
                                <label>Sort by</label>
                                <select>
                                    <option>Date</option>
                                    <option>Newest</option>
                                    <option>Popular</option>
                                </select>
                            </div>
                            <div>
                                <label>View</label>
                                <select>
                                    <option>12</option>
                                    <option>24</option>
                                    <option>48</option>
                                    <option>96</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div>
                        <c:choose>
                            <c:when test="${not empty sessionScope.listProduct}">
                                <c:forEach var="product" items="${sessionScope.listProduct}">
                                    <div>
                                        <form action="${pageContext.request.contextPath}/s_productDetail" method="get">
                                            <input type="hidden" name="productId" value="${product.id}">
                                            <div>
                                                <img src="${not empty product.images ? product.images[0].imageUrl : 'https://via.placeholder.com/150'}" alt="${product.title}">
                                                <div>
                                                    <p><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$" /></p>
                                                    <h4>${product.title}</h4>
                                                    <div>
                                                        <span style="color: gold;">★</span>
                                                        <span style="color: gold;">★</span>
                                                        <span style="color: gold;">★</span>
                                                        <span style="color: gold;">★</span>
                                                        <span style="color: grey;">★</span>
                                                    </div>
                                                    <button onclick="callDetail(${product.id})" class="btn amado-btn">View Details</button>
                                                    <button type="button" class="btn amado-btn">Add to Cart</button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p>No products available.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div>
                        <ul>
                            <li><a href="#">01.</a></li>
                            <li><a href="#">02.</a></li>
                            <li><a href="#">03.</a></li>
                            <li><a href="#">04.</a></li>
                        </ul>
                    </div>
                </section>
            </div>
        </main>

        <footer>
            <div>
                <div>
                    <h3>Subscribe for a 25% Discount</h3>
                    <p>Nulla ac convallis lorem, eget euismod nisl. Donec in libero sit amet mi vulputate consectetur. Donec auctor interdum purus, ac finibus massa bibendum nec.</p>
                </div>
                <div>
                    <input type="email" placeholder="Your email">
                    <button>Subscribe</button>
                </div>
            </div>
            <div>
                <div>
                    <p>Copyright © All rights reserved | This template is made with by Colorlib & Re-distributed by Themewagon</p>
                </div>
                <div>
                    <ul>
                        <li><a href="#">Home</a></li>
                        <li><a href="#">Shop</a></li>
                        <li><a href="#">Product</a></li>
                        <li><a href="#">Cart</a></li>
                        <li><a href="#">Checkout</a></li>
                    </ul>
                </div>
            </div>
        </footer>

        <!-- ##### jQuery (Necessary for All JavaScript Plugins) ##### -->
        <script src="js/jquery/jquery-2.2.4.min.js"></script>
        <!-- Popper js -->
        <script src="js/lib_js/popper.min.js"></script>
        <!-- Bootstrap js -->
        <script src="js/lib_js/bootstrap.min.js"></script>
        <!-- Plugins js -->
        <script src="js/lib_js/plugins.js"></script>
        <!-- Active js -->
        <script src="js/active.js"></script>
        <!-- Ion Icons -->
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
        <script>
            function callDetail(productId) {
                window.location.href = "${pageContext.request.contextPath}/s_productDetail?productId=" + productId;
            }
        </script>
    </body>
</html>