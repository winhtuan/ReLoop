<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="not-nav notification-container" id="notificationContainer">
    <c:choose>
        <c:when test="${sessionScope.user != null}">
            <a href="#" id="notificationLink">
                <ion-icon name="notifications-outline"></ion-icon> Notification
            </a>
        </c:when>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/callLogin">
                <ion-icon name="notifications-outline"></ion-icon> Notification
            </a>
        </c:otherwise>
    </c:choose>

    <!-- Khung hiện thông báo -->
    <div id="notificationBox" class="notification-box">
        <c:choose>
            <c:when test="${empty notifications}">
                <p style="text-align: center; font-size: 17px;">No New <strong>Notifications</strong></p>
            </c:when>
            <c:otherwise>
                <c:forEach var="n" items="${notifications}">
                    <div class="notification-item ${n.read ? '' : 'unread'}">
                        <div class="notification-text">
                            <p>${n.content}</p>
                            <small>${n.createdAt}</small>
                        </div>
                    </div>
                </c:forEach>
                <!-- Nút xem thêm -->
                <div id="seeMoreNotifications" style="text-align: center; margin-top: 5px;">
                    <a href="#" style="font-size: 14px; color: #fbb710; cursor: pointer;">Xem thêm</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Toast Notification Modal -->
<div id="toastNotification" class="toast-notification">
    <span id="toastMessage"></span>
    <button class="toast-close" onclick="hideToast()">×</button>
</div>