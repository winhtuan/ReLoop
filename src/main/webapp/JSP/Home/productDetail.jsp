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
    <style>
        .popup-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 10000;
        }

        .popup {
            background: #fff;
            padding: 24px;
            border-radius: 12px;
            width: 400px;
            max-width: 90%;
            box-shadow: 0 6px 20px rgba(0,0,0,0.3);
            position: relative;
            animation: fadeIn 0.3s ease-in-out;
        }

        .popup h3 {
            margin-top: 0;
            margin-bottom: 15px;
        }

        .popup label {
            display: block;
            margin: 8px 0;
            cursor: pointer;
        }

        .popup textarea {
            width: 100%;
            padding: 8px;
            resize: vertical;
            margin-top: 10px;
            font-size: 14px;
        }

        .popup button {
            margin-top: 12px;
            padding: 8px 14px;
            background-color: #ff5e5e;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: bold;
        }

        .popup .close-btn {
            position: absolute;
            top: 10px;
            right: 14px;
            background: none;
            color: black;
            font-size: 20px;
            border: none;
            cursor: pointer;
        }

        .denounce-btn {
            /*            background-color: #ff3c3c;*/
            color: black;
            /*            padding: 8px 14px;
                        border: none;
                        border-radius: 6px;
                        cursor: pointer;
                        font-weight: bold;*/
            margin-bottom: 20px;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        .review-denounce {
            display: flex;
            gap: 10px;
            align-items: center;
        }

        .review-btn,
        .denounce-btn {
            background-color: transparent;
            border: 1px solid #333;
            padding: 6px 12px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            color: #333;
            transition: all 0.3s ease;
        }

        .review-btn:hover,
        .denounce-btn:hover {
            background-color: #333;
            color: white;
        }

        .denounce-btn {
            margin: 0; /* xóa margin thừa nếu có */
        }
        .reason-list {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .reason-item {
            padding: 10px 14px;
            background-color: #f5f5f5;
            border-radius: 6px;
            cursor: pointer;
            transition: background 0.2s;
        }

        .reason-item:hover {
            background-color: #ffecec;
            color: #ff3c3c;
        }
        .d-flex {
            display: flex;
            align-items: center;
        }

        .gap-3 {
            gap: 16px;
        }

        .btn-review,
        .btn-denounce {
            font-size: 14px;
            font-weight: 500;
            color: #666;
            text-decoration: none;
            transition: color 0.3s ease;
            padding: 4px 8px;
            border-radius: 4px;
        }

        .btn-review:hover,
        .btn-denounce:hover {
            color: #ff3c3c;
            background-color: rgba(255, 60, 60, 0.1);
        }

    </style>
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
                        <div class="col-12 col-lg-7">
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
                        <div class="col-12 col-lg-5">
                            <div class="single_product_desc">
                                <!-- Product Meta Data -->
                                <div class="product-meta-data">
                                    <div class="line"></div>
                                    <p class="product-price"><fmt:formatNumber value="${sessionScope.product.price}" type="currency" currencySymbol="$" /></p>
                                    <a href="#">
                                        <h6>${sessionScope.product.title}</h6>
                                    </a>
                                    <!-- Ratings & Review -->

                                    <div class="ratings-review mb-15 d-flex align-items-center justify-content-between">
                                        <div class="ratings" id="product-rating">
                                            <!-- Số sao sẽ được tạo động bởi JavaScript -->
                                            <c:set var="totalRating" value="0" />
                                            <c:set var="ratingCount" value="0" />
                                            <c:if test="${not empty feedbackList}">
                                                <c:forEach var="feedback" items="${feedbackList}">
                                                    <c:set var="totalRating" value="${totalRating + feedback.rating}" />
                                                    <c:set var="ratingCount" value="${ratingCount + 1}" />
                                                </c:forEach>
                                            </c:if>
                                            <c:set var="averageRating" value="${ratingCount > 0 ? totalRating / ratingCount : 0}" />
                                            <span class="average-rating" data-rating="${averageRating}"></span>
                                            <!-- Debug JSTL -->
                                            <c:if test="${ratingCount > 0}">
                                                <span style="display:none;" class="debug-rating">Total: ${totalRating}, Count: ${ratingCount}, Avg: ${averageRating}</span>
                                            </c:if>
                                        </div>
                                        <!-- Denounce Button -->
                                        <div class="d-flex gap-3">
                                            <div class="review">
                                                <a href="#" class="btn-review">Write A Review</a>
                                            </div>
                                            <div class="denounce">
                                                <a href="#" class="btn-denounce" onclick="showPopup()">Denounce</a>
                                            </div>
                                        </div>
                                        <!-- Popup Overlay -->
                                        <div id="popupOverlay" class="popup-overlay">
                                            <div class="popup">
                                                <button class="close-btn" onclick="hidePopup()">✖</button>

                                                <!-- Step 1: Select Reason -->
                                                <div id="step1">
                                                    <h3>Select a Reason</h3>
                                                    <div class="reason-list">
                                                        <div class="reason-item" onclick="selectReason(this)">Product is fraudulent</div>
                                                        <div class="reason-item" onclick="selectReason(this)">Counterfeit product</div>
                                                        <div class="reason-item" onclick="selectReason(this)">Unknown origin</div>
                                                        <div class="reason-item" onclick="selectReason(this)">Unclear images</div>
                                                        <div class="reason-item" onclick="selectReason(this)">Offensive content</div>
                                                        <div class="reason-item" onclick="selectReason(this)">Other</div>
                                                    </div>
                                                </div>

                                                <!-- Step 2: Description -->
                                                <div id="step2" style="display: none;">
                                                    <h3>Describe the Issue</h3>
                                                    <p id="selectedReasonText" style="font-weight: bold; margin-bottom: 10px;"></p>
                                                    <textarea id="description" placeholder="Enter 10-50 characters..."></textarea>
                                                    <form id="denounceForm" method="post" action="${pageContext.request.contextPath}/DenounceServlet">
                                                        <input type="hidden" name="productId" value="${sessionScope.product.productId}" />
                                                        <input type="hidden" name="userId" value="${sessionScope.customerId}" />
                                                        <input type="hidden" name="reason" id="denounceReason" />
                                                        <input type="hidden" name="description" id="denounceDescription" />
                                                        <button onclick="submitReport()">Submit Report</button>
                                                    </form>
                                                </div>
                                            </div>
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

                                        <div class="amado-btn-group">
                                            <input type="hidden" name="postID" value="${sessionScope.product.productId}">
                                            <!--                                        <input type="hidden" name="customerId">-->

                                            <button data-productid="${product.productId}" class="amado-btn-custom add-to-cart">
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
                                            <img src="${sessionScope.seller.srcImg}" alt="Seller Avatar">
                                        </div>
                                        <div class="seller-main-hz">
                                            <div class="seller-top-row">
                                                <span class="seller-name-hz">${sessionScope.seller.fullName}</span>
                                                <span class="seller-rating-hz">
                                                    <ion-icon name="star-outline"></ion-icon> 4.5 <span class="rating-count">(2)</span>
                                                </span>

                                            </div>
                                            <div class="seller-stats-hz">
                                                <span>55 sold</span> · <span>4 for sale</span>
                                            </div>
                                            <div class="seller-status-hz">
                                                • Active 1 day ago
                                            </div>
                                        </div>
                                        <div class="seller-contact-hz">
                                            <form method="post" action="${pageContext.request.contextPath}/UsersServlet">
                                                <input type="hidden" name="sellerId" value="${sessionScope.product.userId}" />
                                                <input type="hidden" name="productId" value="${sessionScope.product.productId}" />
                                                <button type="submit" class="amado-btn-custom">Contact</button>
                                            </form>
                                        </div>
                                    </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
            <!-- Product Details Area End -->
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
        <script src="${pageContext.request.contextPath}/js/search-menu.js"></script>
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
        <script>
            function showPopup() {
                document.getElementById("popupOverlay").style.display = "flex";
                document.getElementById("step1").style.display = "block";
                document.getElementById("step2").style.display = "none";
            }

            function hidePopup() {
                document.getElementById("popupOverlay").style.display = "none";
            }

            function nextStep() {
                const selected = document.querySelector('input[name="reason"]:checked');
                if (!selected) {
                    alert("Please select a reason.");
                    return;
                }
                document.getElementById("step1").style.display = "none";
                document.getElementById("step2").style.display = "block";
            }

            function submitReport() {
                const desc = document.getElementById("description").value.trim();
                const reason = selectedReason; // ✅ lấy từ biến toàn cục đã lưu khi chọn

                if (!reason) {
                    alert("Please select a reason.");
                    return;
                }

                if (desc.length < 10 || desc.length > 50) {
                    alert("Description must be between 10 and 50 characters.");
                    return;
                }

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