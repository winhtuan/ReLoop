<%-- 
    Document   : checkout_success
    Created on : Jun 21, 2025, 10:37:46 AM
    Author     : Thanh Loc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt hàng thành công • Reloop</title>

    <!-- Favicon & FontAwesome nếu cần -->
    <link rel="icon" href="${pageContext.request.contextPath}/img/core-img/favicon.ico">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- Style tuỳ bạn: dùng chung core-style hoặc ghi nhanh -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core-style.css">
    <style>
        .success-wrapper{
            max-width:600px;margin:80px auto;text-align:center;
        }
        .success-icon{
            font-size:64px;color:#4caf50;margin-bottom:20px;
        }
        .success-btn{
            display:inline-block;padding:12px 30px;background:#fbb710;
            color:#fff;border-radius:4px;text-decoration:none;margin-top:25px;
        }
        .success-btn:hover{opacity:.9;}
    </style>
</head>
<body>

<main class="success-wrapper">
    <div class="success-icon">
        <i class="fa-solid fa-circle-check"></i>
    </div>

    <h1>Thanh toán thành công!</h1>
    <p>Cảm ơn bạn đã tin tưởng và mua hàng tại <strong>Reloop</strong>.</p>

    <!-- Nếu muốn hiển thị thêm thông tin, bạn có thể đặt vào request trước khi redirect -->
    <!-- Ví dụ: -->
    <c:if test="${not empty requestScope.orderCount}">
        <p>Bạn đã tạo <strong>${requestScope.orderCount}</strong> đơn hàng mới.</p>
    </c:if>

    <a href="${pageContext.request.contextPath}/home" class="success-btn">
        Tiếp tục mua sắm
    </a>

    <br><br>
    <a href="${pageContext.request.contextPath}/orders">Xem lịch sử đơn hàng của bạn</a>
</main>

</body>
</html>
