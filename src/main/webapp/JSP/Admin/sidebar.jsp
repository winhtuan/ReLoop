<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Document</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
        <!-- Link Google Fonts For icons -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@24,400,0,0" />
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />

        <style>
        *, *::before, *::after { box-sizing: border-box; }
        html, body { height: 100%; margin: 0; padding: 0; }
        body { display: flex; flex-direction: row; }
        .sidebar {
            position: fixed;
            top: 0;
            left: 0;
            width: 260px;
            height: 100vh;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
            background-color: #fff;
            box-shadow: 2px 0 5px rgba(0,0,0,0.1);
            z-index: 999;
        }
        .sidebar-nav { flex-grow: 1; }
        main, .content-wrapper {
            margin-left: 260px;
            width: calc(100% - 260px);
            padding: 1rem;
            flex-grow: 1;
        }
    </style>
    </head>

    <body>
        <!-- Mobile Sidebar Menu Button -->
        <button class="sidebar-menu-button">
            <span class="material-symbols-rounded">menu</span>
        </button>

        <aside class="sidebar">
            <!-- Sidebar Header -->
            <header class="sidebar-header">
                <a href="#" class="header-logo">
                    <img src="${pageContext.request.contextPath}/img/core-img/logo.png" alt="ReLoop">
                </a>
                <button class="sidebar-toggler">
                    <span class="material-symbols-rounded">chevron_left</span>
                </button>
            </header>

            <nav class="sidebar-nav">
                <!-- Primary Top Nav -->
                <ul class="nav-list primary-nav">
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/StatictisServlet" class="nav-link">
                            <span class="material-symbols-rounded">dashboard</span>
                            <span class="nav-label">Dashboard</span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="nav-item">
                                <a class="nav-link dropdown-title">Dashboard</a>
                            </li>
                        </ul>
                    </li>
                    <!--DropDown-->
                    <li class="nav-item dropdown-container">
                        <a href="#" class="nav-link dropdown-toggle">
                            <span class="material-symbols-rounded">manage_accounts</span>
                            <span class="nav-label">Account</span>
                            <span class="dropdown-icon material-symbols-rounded">keyboard_arrow_down</span>
                        </a>
                        <!--DropDown Menu-->
                        <ul class="dropdown-menu">
                            <li class="nav-item">
                                <a class="nav-link dropdown-title">Account</a>
                            </li>

                            <li class="nav-item">
                                <a href="${pageContext.request.contextPath}/AccountManageServlet" class="nav-link dropdown-link">
                                    Manage Users
                                </a>

                            </li>

                            <li class="nav-item">
                                <a href="${pageContext.request.contextPath}/BlockListAccountServlet" class="nav-link dropdown-link">Block User</a>
                            </li>

                        </ul>
                    </li>

                    <li class="nav-item dropdown-container">
                        <a href="#" class="nav-link dropdown-toggle">
                            <span class="material-symbols-rounded">text_ad</span>
                            <span class="nav-label">Post</span>
                            <span class="dropdown-icon material-symbols-rounded">keyboard_arrow_down</span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="nav-item">
                                <a class="nav-link dropdown-title">Post</a>
                            </li>

                            <li class="nav-item">
                                <a href="${pageContext.request.contextPath}/ApprovalPost" class="nav-link dropdown-link">Approval Post</a>
                            </li>
                            <li class="nav-item">
                                <a href="${pageContext.request.contextPath}/RejectPost" class="nav-link dropdown-link">Reject Post</a>
                            </li>
                            <li class="nav-item">
                                <a href="${pageContext.request.contextPath}/DeletePost" class="nav-link dropdown-link">Delete Post</a>
                            </li>
                        </ul>
                    </li>

                    <li class="nav-item dropdown-container">
                        <a href="#" class="nav-link dropdown-toggle">
                            <span class="material-symbols-outlined">report</span>
                            <span class="nav-label">Report</span>
                            <span class="dropdown-icon material-symbols-rounded">keyboard_arrow_down</span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="nav-item">
                                <a class="nav-link dropdown-title">Report</a>
                            </li>
                            <li class="nav-item">
                                <a href="${pageContext.request.contextPath}/ProductReportPost" class="nav-link dropdown-link">Product Reports</a>
                            </li>
                            <li class="nav-item">
                                <a href="${pageContext.request.contextPath}/HandleReportPost" class="nav-link dropdown-link">Handle Reports</a>
                            </li>
                        </ul>
                    </li>
                </ul>

                <ul class="nav-list secondary-nav">
                    <li class="nav-item">
                        <a href="#" class="nav-link">
                            <span class="material-symbols-rounded">settings</span>
                            <span class="nav-label">Settings</span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="nav-item">
                                <a class="nav-link dropdown-title">Settings</a>
                            </li>
                        </ul>
                    </li>

                    <li class="nav-item">
                        <a href="s_logout" class="nav-link">
                            <span class="material-symbols-rounded">logout</span>
                            <span class="nav-label">Logout</span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="nav-item">
                                <a class="nav-link dropdown-title">Logout</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </nav>
        </aside>
    </body>

    <script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
    </html>