<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Post Table Manager</title>

     <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- ✅ Font Awesome v6 (KHÔNG dùng integrity) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!-- Google Font: Poppins -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />

    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap');
        :root {
            --text-primary: #2c3e50;
            --text-secondary: #16a34a;
            --text-warning: #f59e0b;
            --text-danger: #dc2626;
            --bg-gray: #f3f5f9;
            --card-bg: #ffffff;
            --border-color: #ddd;
            --shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
        }
        body {
            font-family: 'Poppins', sans-serif;
            background-color: var(--card-bg);
            margin: 0;
            padding: 0;
        }
        main {
            background: var(--card-bg);
            min-height: 100vh;
        }
        main .header {
            background: var(--bg-gray);
            display: flex;
            align-items: center;
            padding: 16px 24px;
            border-bottom: 1px solid var(--border-color);
            margin-bottom: 50px;
        }
        main .header h1 {
            font-size: 30px;
            font-weight: 600;
            color: var(--text-primary);
            margin: 0;
        }
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 24px;
            margin-bottom: 24px;
            padding: 0 24px;
        }
        .stat-card {
            background-color: var(--card-bg);
            border-radius: 12px;
            box-shadow: var(--shadow);
            padding: 24px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .stat-title {
            font-size: 14px;
            font-weight: 500;
            color: #6b7280;
        }
        .stat-value {
            font-size: 24px;
            font-weight: bold;
            margin-top: 4px;
        }
        .text-warning { color: var(--text-warning); }
        .text-secondary { color: var(--text-secondary); }
        .text-primary { color: #4f46e5; }
        .text-danger { color: var(--text-danger); }
        .bg-yellow-100 { background-color: #fef9c3; }
        .bg-green-100 { background-color: #dcfce7; }
        .bg-indigo-100 { background-color: #e0e7ff; }
        .bg-red-100 { background-color: #fee2e2; }
        .icon-wrapper {
            padding: 12px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .material-symbols-rounded {
            font-variation-settings: 'FILL' 1, 'wght' 400, 'GRAD' 0, 'opsz' 24;
        }
        .modal-overlay {
            background-color: rgba(0, 0, 0, 0.4);
        }
    </style>
</head>
<body>

    <!-- Sidebar nếu có -->
    <%--<c:import url="sidebar.jsp" />--%>

    <main class="flex-1">
        <!-- Header -->
        <div class="header">
            <h1>Post Table Manager</h1>
        </div>

        <!-- Stats Cards -->
        <div class="stats-grid">
            <div class="stat-card">
                <div>
                    <p class="stat-title">Pending Reports</p>
                    <p class="stat-value text-warning">12</p>
                </div>
                <div class="icon-wrapper bg-yellow-100 text-warning">
                    <i class="fas fa-exclamation-triangle text-xl"></i>
                </div>
            </div>

            <div class="stat-card">
                <div>
                    <p class="stat-title">Resolved Today</p>
                    <p class="stat-value text-secondary">8</p>
                </div>
                <div class="icon-wrapper bg-green-100 text-secondary">
                    <i class="fas fa-check-circle text-xl"></i>
                </div>
            </div>

            <div class="stat-card">
                <div>
                    <p class="stat-title">Active Chats</p>
                    <p class="stat-value text-primary">5</p>
                </div>
                <div class="icon-wrapper bg-indigo-100 text-primary">
                    <i class="fas fa-comments text-xl"></i>
                </div>
            </div>

            <div class="stat-card">
                <div>
                    <p class="stat-title">Banned Users</p>
                    <p class="stat-value text-danger">3</p>
                </div>
                <div class="icon-wrapper bg-red-100 text-danger">
                    <i class="fas fa-user-slash text-xl"></i>
                </div>
            </div>
        </div>
        <c:import url="/JSP/Conversation/chatUI.jsp" />

    </main>

</body>
</html>
