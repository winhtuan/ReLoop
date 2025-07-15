<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

        <link rel="stylesheet" href="css/avatar.css">
        <link rel="stylesheet" href="css/core-style.css">
        <link rel="stylesheet" href="css/jsp_css/loader.css">
        <link rel="stylesheet" href="css/dropdown.css">

        <link rel="stylesheet" href="css/product-styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/category-menu.css">
    </head>
    <body>
        <div id="preloader">
            <div class="loader"></div>
        </div>

        <c:import url="/JSP/Home/Search.jsp" />

        <!-- ##### Main Content Wrapper Start ##### -->

        <div class="main-content-wrapper d-flex clearfix">
            <div class="nav-container">
                <c:import url="/JSP/Home/Nav.jsp" />
            </div>
            <div>
                <div class="product-container">
                    <h2>Product of category: ${currentCategory.name}</h2>

                    <!-- FILTER SECTION -->
                    <div class="filter-bar">
                        <div class="filter-icon">
                            <ion-icon name="funnel-outline"></ion-icon> Filter
                        </div>
                        <!-- Địa điểm -->
                        <div class="filter-option dropdown">
                            <button class="btn btn-orange">Tp Hồ Chí Minh <ion-icon name="chevron-down-outline"></ion-icon></button>
                            <div class="dropdown-content">
                                <c:url var="locationHanoi" value="categoryViewServlet">
                                    <c:if test="${currentCategory != null}">
                                        <c:param name="slug" value="${currentCategory.slug}"/>
                                    </c:if>
                                    <c:param name="location" value="Hà Nội"/>
                                </c:url>
                                <c:url var="locationDanang" value="categoryViewServlet">
                                    <c:if test="${currentCategory != null}">
                                        <c:param name="slug" value="${currentCategory.slug}"/>
                                    </c:if>
                                    <c:param name="location" value="Đà Nẵng"/>
                                </c:url>
                                <c:url var="locationHCM" value="categoryViewServlet">
                                    <c:if test="${currentCategory != null}">
                                        <c:param name="slug" value="${currentCategory.slug}"/>
                                    </c:if>
                                    <c:param name="location" value="Tp Hồ Chí Minh"/>
                                </c:url>
                                <a href="${locationHanoi}">Hà Nội</a>
                                <a href="${locationDanang}">Đà Nẵng</a>
                                <a href="${locationHCM}">Tp Hồ Chí Minh</a>
                            </div>
                        </div>

                        <!-- Danh mục (Category) -->
                        <div class="filter-option dropdown" id="categoryDropdown">
                            <button class="btn btn-orange" id="categoryButton" onclick="toggleCategoryDropdown()">
                                <c:out value="${currentCategory != null ? currentCategory.name : 'Danh mục'}" />
                                <ion-icon name="chevron-down-outline"></ion-icon>
                            </button>
                            <div class="dropdown-content" id="categoryContent">
                                <c:choose>
                                    <c:when test="${not empty categoryList and currentCategory != null}">
                                        <c:choose>
                                            <c:when test="${currentCategory.level == 0}">
                                                <!-- Chỉ hiển thị Lv0 hiện tại một lần -->
                                                <c:forEach var="cat" items="${categoryList}" varStatus="loop">
                                                    <c:if test="${cat.level == 0 and loop.first}"> <!-- Chỉ lấy lần đầu tiên -->
                                                        <c:url var="categoryUrl" value="categoryViewServlet">
                                                            <c:param name="slug" value="${cat.slug}"/>
                                                            <c:if test="${param.minPrice != null}">
                                                                <c:param name="minPrice" value="${param.minPrice}"/>
                                                            </c:if>
                                                            <c:if test="${param.maxPrice != null}">
                                                                <c:param name="maxPrice" value="${param.maxPrice}"/>
                                                            </c:if>
                                                            <c:if test="${param.state != null}">
                                                                <c:param name="state" value="${param.state}"/>
                                                            </c:if>
                                                        </c:url>
                                                        <a href="${categoryUrl}" 
                                                           <c:if test="${cat.categoryId == currentCategory.categoryId}">class="active"</c:if>>
                                                            ${cat.name}
                                                        </a>
                                                    </c:if>
                                                </c:forEach>
                                                <!-- Hiển thị tất cả Lv1 con -->
                                                <c:forEach var="cat" items="${categoryList}">
                                                    <c:if test="${cat.level == 1 and cat.parentId == currentCategory.categoryId}">
                                                        <c:url var="categoryUrl" value="categoryViewServlet">
                                                            <c:param name="slug" value="${cat.slug}"/>
                                                            <c:if test="${param.minPrice != null}">
                                                                <c:param name="minPrice" value="${param.minPrice}"/>
                                                            </c:if>
                                                            <c:if test="${param.maxPrice != null}">
                                                                <c:param name="maxPrice" value="${param.maxPrice}"/>
                                                            </c:if>
                                                            <c:if test="${param.state != null}">
                                                                <c:param name="state" value="${param.state}"/>
                                                            </c:if>
                                                        </c:url>
                                                        <a href="${categoryUrl}">
                                                            ${cat.name}
                                                        </a>
                                                    </c:if>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${currentCategory.level == 1}">
                                                <!-- Hiển thị Lv0 cha -->
                                                <c:set var="parentCat" value="${categoryList[0]}" />
                                                <c:forEach var="cat" items="${categoryList}">
                                                    <c:if test="${cat.categoryId == currentCategory.parentId}">
                                                        <c:set var="parentCat" value="${cat}" />
                                                    </c:if>
                                                </c:forEach>
                                                <c:url var="parentUrl" value="categoryViewServlet">
                                                    <c:param name="slug" value="${parentCat.slug}"/>
                                                    <c:if test="${param.minPrice != null}">
                                                        <c:param name="minPrice" value="${param.minPrice}"/>
                                                    </c:if>
                                                    <c:if test="${param.maxPrice != null}">
                                                        <c:param name="maxPrice" value="${param.maxPrice}"/>
                                                    </c:if>
                                                    <c:if test="${param.state != null}">
                                                        <c:param name="state" value="${param.state}"/>
                                                    </c:if>
                                                </c:url>
                                                <a href="${parentUrl}" class="parent-category">
                                                    ${parentCat.name}
                                                </a>
                                                <!-- Hiển thị các Lv1 thuộc Lv0 cha -->
                                                <c:forEach var="cat" items="${categoryList}">
                                                    <c:if test="${cat.level == 1 and cat.parentId == parentCat.categoryId}">
                                                        <c:url var="categoryUrl" value="categoryViewServlet">
                                                            <c:param name="slug" value="${cat.slug}"/>
                                                            <c:if test="${param.minPrice != null}">
                                                                <c:param name="minPrice" value="${param.minPrice}"/>
                                                            </c:if>
                                                            <c:if test="${param.maxPrice != null}">
                                                                <c:param name="maxPrice" value="${param.maxPrice}"/>
                                                            </c:if>
                                                            <c:if test="${param.state != null}">
                                                                <c:param name="state" value="${param.state}"/>
                                                            </c:if>
                                                        </c:url>
                                                        <a href="${categoryUrl}" 
                                                           <c:if test="${cat.categoryId == currentCategory.categoryId}">class="active"</c:if>>
                                                            ${cat.name}
                                                        </a>
                                                    </c:if>
                                                </c:forEach>
                                            </c:when>
                                        </c:choose>
                                        <!-- Thêm mục Tất cả danh mục -->
                                        <button class="all-categories-btn" onclick="showAllCategories(event)">Tất cả danh mục</button>
                                    </c:when>
                                    <c:otherwise>
                                        <p>Không có danh mục để hiển thị (Debug: categoryList is empty). Slug: ${param.slug}, MinPrice: ${param.minPrice}, MaxPrice: ${param.maxPrice}, State: ${param.state}</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <!-- Giá -->
                        <div class="filter-option dropdown">
                            <button class="btn btn-outline">Giá <ion-icon name="chevron-down-outline"></ion-icon></button>
                            <div class="dropdown-content">
                                <div class="price-slider">
                                    <div class="slider-track" id="sliderTrack">
                                        <div class="thumb min-thumb" id="minThumb"></div>
                                        <div class="thumb max-thumb" id="maxThumb"></div>
                                    </div>
                                </div>
                                <div class="price-inputs">
                                    <input type="text" id="minPrice" placeholder="Giá từ" value="${param.minPrice != null ? param.minPrice : 0}" onchange="updateSliderFromInput()">
                                    <span>-</span>
                                    <input type="text" id="maxPrice" placeholder="Giá đến" value="${param.maxPrice != null ? param.maxPrice : 20000000}" onchange="updateSliderFromInput()">
                                </div>
                                <button class="apply-price-btn">Áp dụng</button>
                                <button class="clear-price-btn">Xóa lọc</button>
                            </div>
                        </div>

                        <!-- Tình trạng -->
                        <div class="filter-option dropdown">
                            <button class="btn btn-outline">Tình trạng <ion-icon name="chevron-down-outline"></ion-icon></button>
                            <div class="dropdown-content">
                                <c:set var="isPetCategory" value="${currentCategory != null && currentCategory.categoryId >= 41 && currentCategory.categoryId <= 46}" />
                                <c:choose>
                                    <c:when test="${isPetCategory}">
                                        <!-- Lọc đặc biệt cho category_id 41-46 (Pets) -->
                                        <c:url var="stateAll" value="categoryViewServlet">
                                            <c:if test="${currentCategory != null}">
                                                <c:param name="slug" value="${currentCategory.slug}"/>
                                            </c:if>
                                            <c:if test="${param.minPrice != null}">
                                                <c:param name="minPrice" value="${param.minPrice}"/>
                                            </c:if>
                                            <c:if test="${param.maxPrice != null}">
                                                <c:param name="maxPrice" value="${param.maxPrice}"/>
                                            </c:if>
                                        </c:url>
                                        <c:url var="stateYoung" value="categoryViewServlet">
                                            <c:if test="${currentCategory != null}">
                                                <c:param name="slug" value="${currentCategory.slug}"/>
                                            </c:if>
                                            <c:param name="state" value="Con non"/>
                                            <c:if test="${param.minPrice != null}">
                                                <c:param name="minPrice" value="${param.minPrice}"/>
                                            </c:if>
                                            <c:if test="${param.maxPrice != null}">
                                                <c:param name="maxPrice" value="${param.maxPrice}"/>
                                            </c:if>
                                        </c:url>
                                        <c:url var="stateAdult" value="categoryViewServlet">
                                            <c:if test="${currentCategory != null}">
                                                <c:param name="slug" value="${currentCategory.slug}"/>
                                            </c:if>
                                            <c:param name="state" value="Trưởng thành"/>
                                            <c:if test="${param.minPrice != null}">
                                                <c:param name="minPrice" value="${param.minPrice}"/>
                                            </c:if>
                                            <c:if test="${param.maxPrice != null}">
                                                <c:param name="maxPrice" value="${param.maxPrice}"/>
                                            </c:if>
                                        </c:url>
                                        <a href="${stateAll}">Tất cả</a>
                                        <a href="${stateYoung}">Con non</a>
                                        <a href="${stateAdult}">Trưởng thành</a>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Lọc mặc định cho các category khác -->
                                        <c:url var="stateAll" value="categoryViewServlet">
                                            <c:if test="${currentCategory != null}">
                                                <c:param name="slug" value="${currentCategory.slug}"/>
                                            </c:if>
                                            <c:if test="${param.minPrice != null}">
                                                <c:param name="minPrice" value="${param.minPrice}"/>
                                            </c:if>
                                            <c:if test="${param.maxPrice != null}">
                                                <c:param name="maxPrice" value="${param.maxPrice}"/>
                                            </c:if>
                                        </c:url>
                                        <c:url var="stateNew" value="categoryViewServlet">
                                            <c:if test="${currentCategory != null}">
                                                <c:param name="slug" value="${currentCategory.slug}"/>
                                            </c:if>
                                            <c:param name="state" value="mới"/>
                                            <c:if test="${param.minPrice != null}">
                                                <c:param name="minPrice" value="${param.minPrice}"/>
                                            </c:if>
                                            <c:if test="${param.maxPrice != null}">
                                                <c:param name="maxPrice" value="${param.maxPrice}"/>
                                            </c:if>
                                        </c:url>
                                        <c:url var="stateOld" value="categoryViewServlet">
                                            <c:if test="${currentCategory != null}">
                                                <c:param name="slug" value="${currentCategory.slug}"/>
                                            </c:if>
                                            <c:param name="state" value="cũ"/>
                                            <c:if test="${param.minPrice != null}">
                                                <c:param name="minPrice" value="${param.minPrice}"/>
                                            </c:if>
                                            <c:if test="${param.maxPrice != null}">
                                                <c:param name="maxPrice" value="${param.maxPrice}"/>
                                            </c:if>
                                        </c:url>
                                        <c:url var="stateDamaged" value="categoryViewServlet">
                                            <c:if test="${currentCategory != null}">
                                                <c:param name="slug" value="${currentCategory.slug}"/>
                                            </c:if>
                                            <c:param name="state" value="hư hỏng nhẹ"/>
                                            <c:if test="${param.minPrice != null}">
                                                <c:param name="minPrice" value="${param.minPrice}"/>
                                            </c:if>
                                            <c:if test="${param.maxPrice != null}">
                                                <c:param name="maxPrice" value="${param.maxPrice}"/>
                                            </c:if>
                                        </c:url>
                                        <a href="${stateAll}">Tất cả</a>
                                        <a href="${stateNew}">Mới</a>
                                        <a href="${stateOld}">Cũ</a>
                                        <a href="${stateDamaged}">Hư hỏng nhẹ</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <!-- PRODUCT LIST -->
                    <p>Product list size: ${fn:length(productList)}</p>

                    <div class="product-list">
                        <c:set var="page" value="${param.page != null ? param.page : 1}" />
                        <c:set var="pageSize" value="8" />
                        <c:set var="start" value="${(page - 1) * pageSize}" />
                        <c:set var="end" value="${start + pageSize - 1}" />
                        <c:set var="totalPages" value="${(fn:length(productList) + pageSize - 1) / pageSize}" />

                        <c:choose>
                            <c:when test="${not empty productList}">
                                <c:forEach var="p" items="${productList}" begin="${start}" end="${end}">
                                    <div class="product-card">
                                        <a href="${pageContext.request.contextPath}/s_productDetail?productId=${p.productId}" class="product-link">
                                            <div class="product-image-wrapper">
                                                <c:choose>
                                                    <c:when test="${not empty p.images and not empty p.images[0].imageUrl}">
                                                        <img class="product-image" src="${p.images[0].imageUrl}" alt="${p.title}" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img class="product-image" src="https://via.placeholder.com/150" alt="No image" />
                                                    </c:otherwise>
                                                </c:choose>

                                                <!-- Action buttons with ion-icon -->
                                                <div class="product-actions">
                                                    <button class="btn-action wishlist">
                                                        <ion-icon name="heart-outline"></ion-icon>
                                                    </button>
                                                    <button class="btn-action buy-now">
                                                        <ion-icon name="flash-outline"></ion-icon>
                                                    </button>
                                                    <button class="btn-action add-to-cart">
                                                        <ion-icon name="cart-outline"></ion-icon>
                                                    </button>
                                                </div>
                                            </div>

                                            <div class="product-info" style="padding: 1rem;">
                                                <div class="product-title">${p.title}</div>
                                                <div class="product-price">${p.price} VNĐ</div>
                                                <div class="product-description">${p.description}</div>
                                                <div class="product-meta">
                                                    <p class="location"><i class="fa fa-map-marker-alt"></i> ${p.location}</p>
                                                    <p class="posted-date">
                                                        <i class="fa fa-calendar-alt"></i> <fmt:formatDate value="${p.createdAt}" pattern="dd/MM/yyyy" />
                                                    </p>
                                                </div>
                                            </div>
                                        </a>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p>This category doesn't have any products now.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Pagination -->
                    <div class="pagination" style="text-align: center; margin-top: 20px;">
                        <c:if test="${totalPages > 1}">
                            <c:url var="prevUrl" value="categoryViewServlet">
                                <c:if test="${currentCategory != null}">
                                    <c:param name="slug" value="${currentCategory.slug}"/>
                                </c:if>
                                <c:if test="${param.minPrice != null}">
                                    <c:param name="minPrice" value="${param.minPrice}"/>
                                </c:if>
                                <c:if test="${param.maxPrice != null}">
                                    <c:param name="maxPrice" value="${param.maxPrice}"/>
                                </c:if>
                                <c:if test="${param.state != null}">
                                    <c:param name="state" value="${param.state}"/>
                                </c:if>
                                <c:param name="page" value="${page - 1}"/>
                            </c:url>
                            <c:url var="nextUrl" value="categoryViewServlet">
                                <c:if test="${currentCategory != null}">
                                    <c:param name="slug" value="${currentCategory.slug}"/>
                                </c:if>
                                <c:if test="${param.minPrice != null}">
                                    <c:param name="minPrice" value="${param.minPrice}"/>
                                </c:if>
                                <c:if test="${param.maxPrice != null}">
                                    <c:param name="maxPrice" value="${param.maxPrice}"/>
                                </c:if>
                                <c:if test="${param.state != null}">
                                    <c:param name="state" value="${param.state}"/>
                                </c:if>
                                <c:param name="page" value="${page + 1}"/>
                            </c:url>

                            <c:if test="${page > 1}">
                                <a href="${prevUrl}" class="pagination-link">Previous</a>
                            </c:if>
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <c:url var="pageUrl" value="categoryViewServlet">
                                    <c:if test="${currentCategory != null}">
                                        <c:param name="slug" value="${currentCategory.slug}"/>
                                    </c:if>
                                    <c:if test="${param.minPrice != null}">
                                        <c:param name="minPrice" value="${param.minPrice}"/>
                                    </c:if>
                                    <c:if test="${param.maxPrice != null}">
                                        <c:param name="maxPrice" value="${param.maxPrice}"/>
                                    </c:if>
                                    <c:if test="${param.state != null}">
                                        <c:param name="state" value="${param.state}"/>
                                    </c:if>
                                    <c:param name="page" value="${i}"/>
                                </c:url>
                                <a href="${pageUrl}" class="pagination-link ${i == page ? 'active' : ''}">${i}</a>
                            </c:forEach>
                            <c:if test="${page < totalPages}">
                                <a href="${nextUrl}" class="pagination-link">Next</a>
                            </c:if>
                        </c:if>
                    </div>
                </div>
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
        <!-- Ion Icons -->
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
        <!-- Font Awesome (chỉ nếu bạn dùng thêm icon fa) -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

        <!-- Dropdown handler for filters -->
        <script src="js/filter-dropdown.js"></script>
        <script>
                                        const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
                                        let originalContent = ''; // Biến lưu nội dung ban đầu

                                        window.addEventListener("load", function () {
                                            const preloader = document.getElementById("preloader");
                                            preloader.style.opacity = "0";
                                            preloader.style.pointerEvents = "none";
                                            setTimeout(() => preloader.style.display = "none", 500);

                                            // Lưu nội dung ban đầu khi trang tải
                                            originalContent = document.getElementById('categoryContent').innerHTML;
                                        });

                                        function updateSliderFromInput() {
                                            const minPriceInput = document.getElementById('minPrice');
                                            const maxPriceInput = document.getElementById('maxPrice');
                                            let minVal = parseInt(minPriceInput.value.replace(/\D/g, '')) || 0;
                                            let maxVal = parseInt(maxPriceInput.value.replace(/\D/g, '')) || 20000000;
                                            if (minVal < 0)
                                                minVal = 0;
                                            if (maxVal > 20000000)
                                                maxVal = 20000000;
                                            if (minVal > maxVal - 200000)
                                                minVal = maxVal - 200000;
                                            if (maxVal < minVal + 200000)
                                                maxVal = minVal + 200000;
                                            updateSlider(minVal, maxVal);
                                        }

                                        function updateSlider(minVal, maxVal) {
                                            const minThumb = document.getElementById('minThumb');
                                            const maxThumb = document.getElementById('maxThumb');
                                            const sliderTrack = document.getElementById('sliderTrack');
                                            const trackRect = sliderTrack.getBoundingClientRect();
                                            const thumbWidth = 15;

                                            minVal = Math.max(0, Math.min(20000000, minVal));
                                            maxVal = Math.max(0, Math.min(20000000, maxVal));
                                            if (minVal > maxVal - 200000)
                                                minVal = maxVal - 200000;
                                            if (maxVal < minVal + 200000)
                                                maxVal = minVal + 200000;

                                            const minPercent = (minVal / 20000000) * 100;
                                            const maxPercent = (maxVal / 20000000) * 100;

                                            minThumb.style.left = `calc(${minPercent}% - ${thumbWidth / 2}px)`;
                                            maxThumb.style.left = `calc(${maxPercent}% - ${thumbWidth / 2}px)`;
                                            document.getElementById('minPrice').value = minVal.toLocaleString('vi-VN') + ' VND';
                                            document.getElementById('maxPrice').value = maxVal.toLocaleString('vi-VN') + ' VND';
                                        }

                                        function toggleCategoryDropdown() {
                                            const dropdownContent = document.getElementById('categoryContent');
                                            if (dropdownContent.style.display === 'block') {
                                                dropdownContent.style.display = 'none';
                                            } else {
                                                dropdownContent.style.display = 'block';
                                                // Reset nội dung về ban đầu nếu không phải từ "Tất cả danh mục"
                                                if (dropdownContent.innerHTML !== originalContent && !dropdownContent.innerHTML.includes('Đang tải...')) {
                                                    dropdownContent.innerHTML = originalContent;
                                                }
                                            }
                                        }

                                        function showAllCategories(event) {
                                            event.preventDefault();
                                            const dropdownContent = document.getElementById('categoryContent');
                                            dropdownContent.innerHTML = '<div style="text-align: center;">Đang tải...</div>';

                                            $.ajax({
                                                url: contextPath + '/categoryViewServlet?action=getAllCategories',
                                                method: 'GET',
                                                dataType: 'json',
                                                success: function (data) {
                                                    dropdownContent.innerHTML = '';
                                                    data.categories.forEach(function (cat) {
                                                        const mainItem = document.createElement('div');
                                                        mainItem.className = 'main-category-item';
                                                        const categoryUrl = contextPath + '/categoryViewServlet?slug=' + encodeURIComponent(cat.slug) +
                                                                (document.getElementById('minPrice').value ? '&minPrice=' + document.getElementById('minPrice').value.replace(/\D/g, '') : '') +
                                                                (document.getElementById('maxPrice').value ? '&maxPrice=' + document.getElementById('maxPrice').value.replace(/\D/g, '') : '') +
                                                                (document.querySelector('input[name="state"]:checked') ? '&state=' + document.querySelector('input[name="state"]:checked').value : '');

                                                        const iconMap = {
                                                            'Books & Documents': 'book-outline',
                                                            'Digital Goods': 'laptop-outline',
                                                            'Electronics': 'hardware-chip-outline',
                                                            'Fashion': 'shirt-outline',
                                                            'Furniture': 'bed-outline',
                                                            'Home Appliances': 'home-outline',
                                                            'Jewelry': 'sparkles-outline',
                                                            'Pets': 'paw-outline'
                                                        };
                                                        const iconName = iconMap[cat.name] || 'folder-outline';

                                                        mainItem.innerHTML = '<a href="' + categoryUrl + '" class="main-category-label">' +
                                                                '<ion-icon name="' + iconName + '"></ion-icon>' +
                                                                cat.name + '</a>';

                                                        const subList = document.createElement('ul');
                                                        subList.className = 'sub-category-list';
                                                        if (data.subCategories[cat.slug]) {
                                                            data.subCategories[cat.slug].forEach(function (sub) {
                                                                const subCategoryUrl = contextPath + '/categoryViewServlet?slug=' + encodeURIComponent(sub.slug) +
                                                                        (document.getElementById('minPrice').value ? '&minPrice=' + document.getElementById('minPrice').value.replace(/\D/g, '') : '') +
                                                                        (document.getElementById('maxPrice').value ? '&maxPrice=' + document.getElementById('maxPrice').value.replace(/\D/g, '') : '') +
                                                                        (document.querySelector('input[name="state"]:checked') ? '&state=' + document.querySelector('input[name="state"]:checked').value : '');
                                                                const subItem = document.createElement('li');
                                                                subItem.className = 'sub-category-item';
                                                                subItem.innerHTML = '<a href="' + subCategoryUrl + '"><ion-icon name="chevron-forward-outline"></ion-icon> ' + sub.name + '</a>';
                                                                subList.appendChild(subItem);
                                                            });
                                                        }
                                                        mainItem.appendChild(subList);
                                                        dropdownContent.appendChild(mainItem);
                                                    });
                                                    dropdownContent.style.display = 'block';
                                                },
                                                error: function (xhr, status, error) {
                                                    dropdownContent.innerHTML = '<p>Lỗi khi tải danh mục: ' + error + '</p>';
                                                }
                                            });
                                        }

                                        // Tắt dropdown khi click ra ngoài và reset nội dung
                                        document.addEventListener('click', function (event) {
                                            const dropdown = document.getElementById('categoryContent');
                                            const button = document.getElementById('categoryButton');
                                            if (dropdown.style.display === 'block' &&
                                                    !dropdown.contains(event.target) &&
                                                    event.target !== button) {
                                                dropdown.style.display = 'none';
                                                // Reset nội dung về ban đầu khi tắt
                                                if (dropdown.innerHTML !== originalContent) {
                                                    dropdown.innerHTML = originalContent;
                                                }
                                            }
                                        });
        </script>
        <!<!-- category -->
        <script src="js/dropdown-handler.js"></script>
    </body>
</html>