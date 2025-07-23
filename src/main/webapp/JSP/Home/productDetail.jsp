<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="description" content="">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <!-- The above 4 meta tags *must* come first in the head; any other head content must come *after* these tags -->

        <!-- Title  -->
        <title>Product Details</title>

        <!-- Favicon  -->
        <link rel="icon" href="${pageContext.request.contextPath}/img/core-img/favicon.ico">

        <!-- Core Style CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core-style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jsp_css/loader.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/avatar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/notification.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product-detail.css">
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

            <!-- Product Details Area Start -->
            <div class="single-product-area clearfix">
                <div class="container-fluid">

                    <div class="row">
                        <div class="col-12">
                            <nav aria-label="breadcrumb">
                                <ol class="breadcrumb mt-50">
                                    <li class="breadcrumb-item active" aria-current="page">${sessionScope.product.title}</li>
                                </ol>
                            </nav>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12 col-lg-6">
                            <div class="single_product_thumb custom-product-thumb">
                                <div id="product_details_slider" class="carousel slide" data-ride="carousel">
                                    <ol class="carousel-indicators">
                                        <c:forEach var="image" items="${sessionScope.product.images}" varStatus="status">
                                            <li class="${status.first ? 'active' : ''}" data-target="#product_details_slider" data-slide-to="${status.index}" style="background-image: url(${image.imageUrl});"></li>
                                            </c:forEach>
                                    </ol>
                                    <div class="carousel-inner">
                                        <c:forEach var="image" items="${sessionScope.product.images}" varStatus="status">
                                            <div class="carousel-item ${status.first ? 'active' : ''}">
                                                <a class="gallery_img" href="${image.imageUrl}">
                                                    <img class="d-block w-100 product-main-img" src="${image.imageUrl}" alt="Slide ${status.index + 1}">
                                                </a>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-12 col-lg-6">
                            <div class="single_product_desc">
                                <!-- Product Meta Data -->
                                <div class="product-meta-data">
                                    <div class="line"></div>
                                    <p class="product-price"><fmt:formatNumber value="${sessionScope.product.price}" type="currency" currencySymbol="$" /></p>
                                    <a href="#">
                                        <h6>${sessionScope.product.title}</h6>
                                    </a>
                                    <!-- Ratings & Review -->
                                    <div class="ratings-review-box">
                                        <div class="ratings">
                                            <ion-icon name="star" aria-hidden="true"></ion-icon>
                                            <ion-icon name="star" aria-hidden="true"></ion-icon>
                                            <ion-icon name="star" aria-hidden="true"></ion-icon>
                                            <ion-icon name="star" aria-hidden="true"></ion-icon>
                                            <ion-icon name="star-half" aria-hidden="true"></ion-icon>
                                            <span class="rating-score">4.5</span>
                                        </div>
                                        <div class="review">
                                            <a href="#commentTab" class="review-link">Write A Review</a>
                                        </div>
                                    </div>
                                    <!-- Availability -->
                                    <p class="avaibility"><span class="avaibility-dot"></span> ${sessionScope.product.status}</p>
                                </div>
                                <div class="short_overview my-5">
                                    <p>${sessionScope.product.description}</p>
                                </div>
                                <!-- Add to Cart Form -->
                                <form class="cart clearfix" action="${pageContext.request.contextPath}/s_addToCart" method="post">
                                    <form class="cart clearfix" action="${pageContext.request.contextPath}/s_addToCart" method="post">

                                        <div class="cart-btn d-flex mb-50">
                                            <p>Quantity</p>
                                            <div class="quantity">
                                                <span class="qty-minus" onclick="var effect = document.getElementById('qty');
                                                    var qty = effect.value;
                                                    if (!isNaN(qty) && qty > 1)
                                                        effect.value--;
                                                    return false;"><ion-icon name="chevron-down-outline"></ion-icon></i></span>

                                                <input type="number" class="qty-text" id="qty" step="1" min="1" max="300" name="quantity" value="1">
                                                <span class="qty-plus" onclick="var effect = document.getElementById('qty');
                                                    var qty = effect.value;
                                                    if (!isNaN(qty))
                                                        effect.value++;
                                                    return false;"><ion-icon name="chevron-up-outline"></ion-icon></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="amado-btn-group cart-buy-group">
                                        <input type="hidden" name="postID" value="${sessionScope.product.productId}">
                                        <input type="hidden" name="customerId" value="${sessionScope.customerId}">
                                        <button type="submit" name="action" class="amado-btn-custom">
                                            <span class="btn-icon"><ion-icon name="cart-outline"></ion-icon></span>Add to Cart
                                        </button>
                                        <button type="submit" name="action" value="buynow" class="amado-btn-custom" style="background-color:#20d34a;">
                                            <span class="btn-icon"><ion-icon name="flash-outline"></ion-icon></span>Buy Now
                                        </button>
                                    </div>
                                </form>
                                <!-- Seller Info Card -->
                                <div class="seller-info-horizontal">
                                    <div class="seller-avatar-hz">
                                        <img src="${sessionScope.product.user.srcImg}" alt="Seller Avatar">
                                    </div>
                                    <div class="seller-main-hz">
                                        <span class="seller-name-hz">${sessionScope.product.user.fullName}</span>
                                        <div class="seller-rating-hz">
                                            <ion-icon name="star-outline"></ion-icon> 4.5 <span class="rating-count">(2)</span>
                                        </div>
                                    </div>
                                    <div class="seller-contact-hz">
                                        <form method="post" action="${pageContext.request.contextPath}/UsersServlet">
                                            <input type="hidden" name="sellerId" value="${sessionScope.product.user.userId}" />
                                            <input type="hidden" name="productId" value="${sessionScope.product.productId}" />
                                            <button type="submit" class="amado-btn-custom">Contact</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="product-tabs-container" id="productTabs">
                        <ul class="product-tabs-nav">
                            <li class="tab-link active" data-tab="detailTab">Detail</li>
                            <li class="tab-link" data-tab="commentTab" id="commentTabBtn">Comment</li>
                        </ul>
                        <div class="product-tab-content">
                            <div id="detailTab" class="tab-content active">
                                <!-- Nội dung chi tiết sản phẩm sẽ thêm sau -->
                                <p>Product detail content here...</p>
                            </div>
                            <div id="commentTab" class="tab-content">
                                <!-- Nội dung bình luận sẽ thêm sau -->
                                <p>Comment section here...</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Product Details Area End -->

            <!-- Tabs for Detail and Comment -->

        </div>
        <!-- ##### Main Content Wrapper End ##### -->

        <c:import url="/JSP/Home/Footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <!-- ##### jQuery (Necessary for All JavaScript Plugins) ##### -->
        <script src="${pageContext.request.contextPath}/js/jquery/jquery-2.2.4.min.js"></script>
        <!-- Popper js -->
        <script src="${pageContext.request.contextPath}/js/lib_js/popper.min.js"></script>
        <!-- Bootstrap js -->
        <script src="${pageContext.request.contextPath}/js/lib_js/bootstrap.min.js"></script>
        <!-- Plugins js -->
        <script src="${pageContext.request.contextPath}/js/lib_js/plugins.js"></script>
        <!-- Active js -->
        <script src="${pageContext.request.contextPath}/js/active.js"></script>

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

                                                // Xử lý click vào "Review" để trượt xuống Customer Reviews
                                                document.addEventListener("DOMContentLoaded", function () {
                                                    const reviewLink = document.querySelector(".scroll-to-reviews");
                                                    const feedbackSection = document.getElementById("customer-reviews");

                                                    if (reviewLink && feedbackSection) {
                                                        reviewLink.addEventListener("click", function (e) {
                                                            e.preventDefault(); // Ngăn chặn hành vi mặc định của liên kết
                                                            feedbackSection.scrollIntoView({behavior: "smooth"}); // Trượt mượt mà đến section
                                                        });
                                                    }

                                                    // Tạo sao động dựa trên trung bình rating
                                                    const ratingElement = document.getElementById("product-rating");
                                                    const averageRatingSpan = document.querySelector(".average-rating");
                                                    let averageRating = parseFloat(averageRatingSpan.getAttribute("data-rating")) || 0;
                                                    console.log("Average Rating:", averageRating); // Debug

                                                    if (ratingElement) {
                                                        for (let i = 0; i < 5; i++) {
                                                            const starWrapper = document.createElement("span");
                                                            starWrapper.className = "star-wrapper";
                                                            starWrapper.style.position = "relative";
                                                            starWrapper.style.display = "inline-block";
                                                            starWrapper.style.width = "20px";
                                                            starWrapper.style.height = "20px";
                                                            starWrapper.style.marginRight = "2px";

                                                            // Tạo sao nền (star-outline)
                                                            const starOutline = document.createElement("ion-icon");
                                                            starOutline.setAttribute("name", "star-outline");
                                                            starOutline.setAttribute("aria-hidden", "true");
                                                            starOutline.style.color = "#ddd"; // Màu nền xám
                                                            starOutline.style.fontSize = "20px";
                                                            starOutline.style.position = "absolute";
                                                            starOutline.style.top = "0";
                                                            starOutline.style.left = "0";

                                                            // Tạo sao lấp đầy
                                                            const starFillWrapper = document.createElement("span");
                                                            starFillWrapper.className = "star-fill-wrapper";
                                                            starFillWrapper.style.position = "absolute";
                                                            starFillWrapper.style.top = "0";
                                                            starFillWrapper.style.left = "0";
                                                            starFillWrapper.style.overflow = "hidden";
                                                            starFillWrapper.style.width = (averageRating - i > 0 ? Math.min(1, averageRating - i) * 100 : 0) + "%";
                                                            starFillWrapper.style.height = "100%";

                                                            const starFill = document.createElement("ion-icon");
                                                            starFill.setAttribute("name", "star");
                                                            starFill.setAttribute("aria-hidden", "true");
                                                            starFill.style.color = "#f39c12"; // Màu cam giống Shopee
                                                            starFill.style.fontSize = "20px";
                                                            starFill.style.position = "absolute";
                                                            starFill.style.top = "0";
                                                            starFill.style.left = "0";

                                                            starFillWrapper.appendChild(starFill);
                                                            starWrapper.appendChild(starOutline);
                                                            starWrapper.appendChild(starFillWrapper);
                                                            ratingElement.insertBefore(starWrapper, averageRatingSpan);
                                                        }
                                                        averageRatingSpan.style.display = "none"; // Ẩn span chứa dữ liệu

                                                    }
                                                });
        </script>
        <c:if test="${not empty requestScope.messCartAdd}">
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
            <script>
                                                window.addEventListener("DOMContentLoaded", function () {
                                                    Swal.fire({
                                                        icon: 'success',
                                                        title: 'Thành công',
                                                        text: "${fn:escapeXml(messCartAdd)}"
                                                    });
                                                });
            </script>
            <c:remove var="messCartAdd" scope="request" />
        </c:if>

                // Gán dữ liệu vào các input hidden
                document.getElementById("denounceReason").value = reason;
                document.getElementById("denounceDescription").value = desc;

                // Submit form
                document.getElementById("denounceForm").submit();
            }


            let selectedReason = "";

            function showPopup() {
                document.getElementById("popupOverlay").style.display = "flex";
                document.getElementById("step1").style.display = "block";
                document.getElementById("step2").style.display = "none";
            }

            function hidePopup() {
                document.getElementById("popupOverlay").style.display = "none";
            }

            function selectReason(el) {
                selectedReason = el.innerText;
                document.getElementById("selectedReasonText").innerText = "Reason: " + selectedReason;
                document.getElementById("denounceReason").value = selectedReason; // ✅ sửa lại chỗ này
                document.getElementById("step1").style.display = "none";
                document.getElementById("step2").style.display = "block";
            }

            document.querySelectorAll('.add-to-cart').forEach(btn => {
                btn.addEventListener('click', function (e) {
                    e.preventDefault();
                    const productId = this.dataset.productid;
                    const quantityy = document.getElementById("qty").value;

                    fetch('s_addToCart', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams({
                            postID: productId,
                            quantity: quantityy
                        })
                    })
                            .then(response => {
                                if (response.ok) {
                                    Swal.fire({
                                        icon: 'success',
                                        title: 'Thêm vào giỏ hàng',
                                        text: "Thêm vào giỏ hàng thành công",
                                        confirmButtonText: 'OK'
                                    });
                                } else {
                                    console.error('Lỗi khi thêm giỏ hàng');
                                }
                            });
                });
            });
        </script>
    </body>

</html>