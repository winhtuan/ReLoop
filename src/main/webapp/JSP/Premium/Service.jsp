<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Nội dung chính -->
<div class="content-area" style="flex:1; background:#fff;">
    <section class="bg-white" style="width:100%;max-width:1100px; margin-top:38px;">
        <div class="text-center mb-5">
            <h2 class="premium-section-title font-bold mb-3" style="color: #242424; font-size: 2.7rem;">
                Upgrade your service
            </h2>
            <div class="line" style="width: 120px; height: 4px; background: #fbb710; margin: 0 auto 24px auto;"></div>
        </div>
        <div class="premium-compare-row">
            <!-- Free Card -->
            <div class="premium-card-box">
                <div class="premium-card free-card">
                    <div class="card-body text-center px-5 py-5">
                        <h3 class="font-bold mb-3" style="color: #242424; font-size:2rem;">Free</h3>
                        <p class="mb-4" style="color:#6d6d6d; font-size:1.1rem;">Basic experience, free forever</p>
                        <ul class="list-unstyled mb-5 text-left mx-auto" style="font-size:1.07rem; max-width:320px;">
                            <li><ion-icon name="checkmark-circle-outline"></ion-icon> Unlimited posting</li>
                            <li><ion-icon name="checkmark-circle-outline"></ion-icon> Chat with buyers/sellers</li>
                            <li><ion-icon name="close-circle-outline"></ion-icon> Priority listing display</li>
                            <li><ion-icon name="close-circle-outline"></ion-icon> Auto-renew listings</li>
                            <li><ion-icon name="close-circle-outline"></ion-icon> View followers</li>
                        </ul>
                        <c:choose>
                            <c:when test="${cus.isPremium}">
                                <!-- Nếu là Premium, hiển thị thông báo và vô hiệu hóa nút -->
                                <button class="amado-btn-custom mt-auto" disabled>Free Plan</button>

                            </c:when>
                            <c:otherwise>
                                <!-- Nếu không phải Premium, hiển thị nút để nâng cấp -->
                                <button class="amado-btn-custom mt-auto" disabled>CURRENT PLAN</button>

                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- Premium Card -->
            <div class="premium-card-box">
                <div class="premium-card premium-card-highlight">
                    <div class="card-body text-center px-5 py-5">
                        <div class="d-flex justify-content-between align-items-center mb-4" style="gap: 10px;">
                            <h3 class="font-bold mb-0" style="color:#fbb710; font-size:2rem; flex: 1; text-align: center;">Premium</h3>
                            <p class="mb-0" style="color:#242424; font-size: 1rem; font-weight: 600;">
                                <span style="font-size: 1rem; color: #242424; font-weight: bold;">$20</span> USD / Month
                            </p>
                        </div>

                        <p class="mb-4" style="color:#242424; font-size:1.1rem;">Upgrade your experience, maximize results</p>
                        <ul class="list-unstyled mb-5 text-left mx-auto" style="font-size:1.07rem; max-width:320px;">
                            <li><ion-icon name="checkmark-circle-outline"></ion-icon> Unlimited posting</li>
                            <li><ion-icon name="checkmark-circle-outline"></ion-icon> Chat with buyers/sellers</li>
                            <li><ion-icon name="checkmark-circle-outline"></ion-icon> Priority listing display</li>
                            <li><ion-icon name="checkmark-circle-outline"></ion-icon> Auto-renew listings</li>
                            <li><ion-icon name="checkmark-circle-outline"></ion-icon> View followers</li>
                        </ul>

                        <form action="createQR" method="post" style="width: 100%;">
                            <input type="hidden" name="paidService_id" value="PRO0001" />
                            <input type="hidden" name="cus" value=${sessionScope.cus} />
                            <input type="hidden" name="buyerId" value=${sessionScope.cus.userId} />

                            <c:choose>
                                <c:when test="${cus.isPremium}">
                                    <!-- Nếu là Premium, hiển thị thông báo và vô hiệu hóa nút -->
                                    <button type="button" class="amado-btn-custom mt-auto" style="width: 100%; border: none;" disabled>
                                        Already a Premium member
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <!-- Nếu không phải Premium, hiển thị nút để nâng cấp -->
                                    <button type="submit" class="amado-btn-custom mt-auto" style="width: 100%; border: none;">
                                        UPGRADE NOW
                                    </button>
                                </c:otherwise>
                            </c:choose>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
