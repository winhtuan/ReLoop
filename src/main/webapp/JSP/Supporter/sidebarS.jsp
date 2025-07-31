<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebarS.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@24,400,0,0" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />

    <style>
        .user-avatar {
            width: 40px;
            height: 40px;
            margin: 7px;
            padding: 0;
            object-fit: cover;
            border-radius: 50%;
            border: 2px solid #ccc;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s ease;
        }

        .user-avatar:hover {
            transform: scale(1.05);
            border-color: #007bff;
        }

        .role-badge {
            display: inline-block;
            font-size: 12px;
            font-weight: 600;
            color: #856404;
            background-color: #fff3cd;
            border: 1px solid #ffeeba;
            padding: 2px 8px;
            border-radius: 20px;
            margin-top: 4px;
            width: fit-content;
        }
    </style>
</head>

<body>
    <button class="sp-sidebar-menu-button">
        <span class="material-symbols-rounded">menu</span>
    </button>

    <aside class="sp-sidebar">
        <header class="sp-sidebar-header">
            <a href="#" class="sp-header-logo">
                <img src="${pageContext.request.contextPath}/img/core-img/logo.png" alt="ReLoop">
            </a>
            <button class="sp-sidebar-toggler">
                <span class="material-symbols-rounded">chevron_left</span>
            </button>
        </header>

        <nav class="sp-sidebar-nav">
            <ul class="sp-nav-list sp-primary-nav">
                <li class="sp-nav-item">
                    <c:if test="${not empty cus}">
                        <a href="s_userProfile" class="sp-nav-link sp-user-info-link" style="margin: 0;padding: 0;">
                            <img src="${cus.srcImg}" alt="Avatar" class="user-avatar rounded-circle">
                            <div class="sp-user-info-text">
                                <span class="sp-nav-label font-semibold">${cus.fullName}</span><br>
                                <span class="sp-nav-label role-badge">
                                    <c:choose>
                                        <c:when test="${cus.role == 'supporter'}">Supporter</c:when>
                                    </c:choose>
                                </span>
                            </div>
                        </a>
                    </c:if>
                    <ul class="sp-dropdown-menu">
                        <li class="sp-nav-item">
                            <a class="sp-nav-link sp-dropdown-title">Profile</a>
                        </li>
                    </ul>
                </li>

                <li class="sp-nav-item">
                    <a href="${pageContext.request.contextPath}/SupporterServlet" class="sp-nav-link">
                        <span class="material-symbols-rounded">dashboard</span>
                        <span class="sp-nav-label">Dashboard</span>
                    </a>
                    <ul class="sp-dropdown-menu">
                        <li class="sp-nav-item">
                            <a class="sp-nav-link sp-dropdown-title">Dashboard</a>
                        </li>
                    </ul>
                </li>

                <li class="sp-nav-item sp-dropdown-container">
                    <a href="#" class="sp-nav-link sp-dropdown-toggle">
                        <span class="material-symbols-outlined">report</span>
                        <span class="sp-nav-label">Report</span>
                        <span class="sp-dropdown-icon material-symbols-rounded">keyboard_arrow_down</span>
                    </a>
                    <ul class="sp-dropdown-menu">
                        <li class="sp-nav-item">
                            <a class="sp-nav-link sp-dropdown-title">Report</a>
                        </li>
                        <li class="sp-nav-item">
                            <a href="${pageContext.request.contextPath}/ProductReportPostSupporter" class="sp-nav-link sp-dropdown-link">Product Reports</a>
                        </li>
                        <li class="sp-nav-item">
                            <a href="${pageContext.request.contextPath}/HandleReportPostSupporter" class="sp-nav-link sp-dropdown-link">Handle Reports</a>
                        </li>
                    </ul>
                </li>
            </ul>

            <ul class="sp-nav-list sp-secondary-nav">
                <li class="sp-nav-item">
                    <a href="#" class="sp-nav-link">
                        <span class="material-symbols-rounded">settings</span>
                        <span class="sp-nav-label">Settings</span>
                    </a>
                    <ul class="sp-dropdown-menu">
                        <li class="sp-nav-item">
                            <a class="sp-nav-link sp-dropdown-title">Settings</a>
                        </li>
                    </ul>
                </li>

                <li class="sp-nav-item">
                    <a href="${pageContext.request.contextPath}/s_logout" class="sp-nav-link">
                        <span class="material-symbols-rounded">logout</span>
                        <span class="sp-nav-label">Logout</span>
                    </a>
                    <ul class="sp-dropdown-menu">
                        <li class="sp-nav-item">
                            <a class="sp-nav-link sp-dropdown-title">Logout</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </nav>
    </aside>
</body>

<script src="${pageContext.request.contextPath}/js/sidebarS.js"></script>
</html>
