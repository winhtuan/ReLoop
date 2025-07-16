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
        <title>Reloop</title>
        <link rel="icon" href="${pageContext.request.contextPath}/img/core-img/favicon.ico">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core-style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jsp_css/loader.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/avatar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/notification.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/css_cart.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/checkout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order.css"/>

    </head>
    <body>
        <!-- Page Preloder -->
        <div id="preloader"><div class="loader"></div></div>

        <c:import url="/JSP/Home/Search.jsp" />

        <div class="main-content-wrapper d-flex clearfix">
            <c:import url="/JSP/Home/Nav.jsp" />
            <div class="container py-5">
                <form id="checkout-form" action="${pageContext.request.contextPath}/createQR" method="post">
                    <div class="row g-4">
                        <!-- Left column: Order Information -->
                        <div class="col-lg-6">
                            <div class="card order-info-card px-4 py-4 shadow-sm h-100" style="border-radius: 18px;">
                                <div class="mb-4 d-flex align-items-center gap-2 text-brand fs-5 fw-bold">
                                    <ion-icon name="receipt-outline" style="color:#fbb710;font-size:28px"></ion-icon>
                                    Order Information
                                </div>
                                <c:forEach var="order" items="${pendingOrders}">
                                    <div class="mb-4">
                                        <div class="d-flex align-items-center mb-2">
                                            <ion-icon name="storefront-outline" class="me-2 text-secondary"></ion-icon>
                                            <span class="fw-semibold fs-6 me-2" style="color:#39424e; font-weight: bold; margin-left: 8px">${order.listItems[0].sellerName}</span>
                                        </div>
                                        <c:forEach var="item" items="${order.listItems}">
                                            <div class="d-flex align-items-start gap-3 mb-3 ps-3">
                                                <img src="${item.product.images[0].imageUrl}"
                                                     class="product-img-square flex-shrink-0"
                                                     alt="${item.productName}" />
                                                <div class="flex-grow-1">
                                                    <div class="fw-semibold product-title mb-1" style="font-size:1.15rem; color:#232323;">
                                                        ${item.productName}
                                                    </div>
                                                    <div class="fw-bold text-danger product-price" style="font-size:1rem; margin-top:10px;">
                                                        <fmt:formatNumber value="${item.price}" pattern="#,###" /> VND
                                                        <span class="ms-2 text-muted small">×${item.quantity}</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="text-muted small">${item.user.address}</div>
                                        </c:forEach>
                                        <hr class="my-3" style="border-top: 1.2px dashed #ececec;">
                                    </div>
                                </c:forEach>
                                <c:set var="amount" value="0" />
                                <c:forEach var="order" items="${pendingOrders}" varStatus="st">
                                    <c:set var="orderTotal" value="0" />
                                    <c:forEach var="item" items="${order.listItems}" varStatus="itemSt">
                                        <c:set var="orderTotal" value="${orderTotal + (item.price * item.quantity)}" />
                                        <c:set var="amount" value="${amount + (item.price * item.quantity)}" />
                                        <!-- Gửi từng item của từng đơn -->
                                        <input type="hidden" name="orders[${st.index}].productId[${itemSt.index}]" value="${item.productId}" />
                                        <input type="hidden" name="orders[${st.index}].quantity[${itemSt.index}]" value="${item.quantity}" />
                                        <input type="hidden" name="orders[${st.index}].price[${itemSt.index}]" value="${item.price}" />
                                    </c:forEach>
                                    <input type="hidden" name="orders[${st.index}].orderId" value="${order.orderId}" />
                                    <input type="hidden" name="orders[${st.index}].productTotal" value="${orderTotal}" />
                                </c:forEach>


                                <div class="mb-1 d-flex align-items-center gap-2 text-secondary fs-5 fw-bold mt-4">
                                    <ion-icon name="wallet-outline" class="me-1"></ion-icon>
                                    Total
                                </div>

                                <div class="card px-3 py-2 mb-2 border-0 bg-light" style="border-radius: 12px;">
                                    <div class="d-flex justify-content-between py-1">
                                        <span>Subtotal</span>
                                        <span class="fw-bold" id="product_total_detail">
                                            <fmt:formatNumber value="${amount}" pattern="#,###" /> VND
                                        </span>
                                    </div>
                                    <div class="d-flex justify-content-between py-1">
                                        <span>Shipping Fee</span>
                                        <span id="fee_ship_detail">--</span>
                                    </div>
                                    <div class="d-flex justify-content-between py-1">
                                        <span>Discount (Voucher)</span>
                                        <span class="text-success" id="voucher_discount_detail">--</span>
                                    </div>
                                    <hr>
                                    <div class="d-flex justify-content-between fw-bold py-1" style="font-size:1.16rem;">
                                        <span>Total</span>
                                        <span class="text-danger fw-bold" id="final_total_detail">
                                            <fmt:formatNumber value="${amount}" pattern="#,###" /> VND
                                        </span>
                                    </div>
                                </div>

                                <button type="submit" class="btn btn-warning w-100 py-2 fs-5 mt-2 fw-bold">
                                    <ion-icon name="card-outline"></ion-icon> Confirm Payment
                                </button>
                            </div>
                        </div>
                        <!-- Right column: Recipient Information -->
                        <div class="col-lg-6">
                            <div class="card order-info-card px-4 py-4 shadow-sm h-100" style="border-radius: 18px;">
                                <input type="hidden" name="buyerId" value="${buyer.userId}" />
                                <div class="mb-3 d-flex align-items-center gap-2">
                                    <ion-icon name="person-outline" style="color:#2563eb;font-size:28px"></ion-icon>
                                    <span class="order-title fs-5">Recipient Information</span>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-medium">Full Name</label>
                                    <input type="text" class="form-control" name="fullname" value="${buyer.fullName}" placeholder="Recipient's Full Name" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-medium">Phone Number</label>
                                    <input type="text" class="form-control" name="phone" value="${buyer.phoneNumber}" placeholder="Phone Number" required>
                                </div>
                                <div class="mb-3 position-relative">
                                    <label for="address" class="form-label fw-medium">Shipping Address</label>
                                    <input type="text" id="address" name="address" value="${buyer.address}" placeholder="Enter shipping address..." required class="form-control px-4 py-2" autocomplete="nope">
                                    <div id="suggestions" class="position-absolute start-0 w-100 bg-white border rounded shadow d-none" style="top:100%;z-index:1000;"></div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-medium">Discount Code / Voucher</label>
                                    <div class="input-group voucher-group">
                                        <input type="text" class="form-control" name="voucher" placeholder="Enter voucher code" id="voucherInput">
                                        <button class="btn btn-outline-warning" type="button" id="applyVoucherBtn">
                                            <ion-icon name="pricetag-outline"></ion-icon> Apply
                                        </button>
                                    </div>
                                    <!-- Thông báo sẽ hiện ở đây -->
                                    <div id="voucherMessage" class="mt-2"></div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-medium">Shipping Fee</label>
                                    <div class="input-group">
                                        <input type="text" class="form-control bg-light" name="shippingFee" placeholder="Not calculated yet" readonly id="shippingFeeInput">
                                        <span class="input-group-text" id="shippingStatus">
                                            <ion-icon name="bicycle-outline"></ion-icon>
                                        </span>
                                    </div>
                                    <div class="form-text">Shipping fee will be automatically calculated based on recipient and shop addresses.</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <pre id="shippingDetailDebug" style="color: #225; font-size: 0.96rem; background: #f8f9fa; border: 1px solid #eee; border-radius:8px; padding: 8px; margin-top: 8px;"></pre>

            <script>
                const vouchers = [
                <c:forEach var="v" items="${allVouchers}" varStatus="st">
                {
                code: "${v.code}",
                        discountValue: ${v.discountValue}
                }<c:if test="${!st.last}">,</c:if>
                </c:forEach>
                ];
            </script>

            <script>
                const shopAddresses = [
                <c:forEach var="order" items="${pendingOrders}" varStatus="st">
                "${order.listItems[0].user.address}"<c:if test="${!st.last}">,</c:if>
                </c:forEach>
                ];
            </script>
            <script>
                const goongApiKey = "${sessionScope.goongapi}";
                let productTotal = ${amount};
                if (isNaN(productTotal))
                    productTotal = 0;
            </script>

            <c:import url="/JSP/Home/Footer.jsp" />
            <script src="${pageContext.request.contextPath}/js/jquery/jquery-2.2.4.min.js"></script>
            <script src="${pageContext.request.contextPath}/js/lib_js/popper.min.js"></script>
            <script src="${pageContext.request.contextPath}/js/lib_js/bootstrap.min.js"></script>
            <script src="${pageContext.request.contextPath}/js/lib_js/plugins.js"></script>
            <script src="${pageContext.request.contextPath}/js/active.js"></script>
            <script src="${pageContext.request.contextPath}/js/JS_search.js"></script>
            <script src="${pageContext.request.contextPath}/js/order.js"></script>
            <script src="${pageContext.request.contextPath}/js/notification.js"></script>
            <script src="${pageContext.request.contextPath}/js/search-menu.js"></script>
            <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
            <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>

            <script>
                const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
                window.addEventListener("load", function () {
                    const preloader = document.getElementById("preloader");
                    preloader.style.opacity = "0";
                    preloader.style.pointerEvents = "none";
                    setTimeout(() => preloader.style.display = "none", 500); // Hide after fade out
                });
            </script>

    </body>
</html>
