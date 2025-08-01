<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Supporter Dashboard</title>

    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!-- Google Fonts: Poppins -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet" />

    <style>
        body {
            font-family: 'Poppins', sans-serif;
        }
    </style>
</head>

<body class="bg-white text-yellow-900">

    <!-- Sidebar -->
    <jsp:include page="sidebarS.jsp"/>

    <main class="min-h-screen">
        <!-- Header -->
        <div class="bg-yellow-100 px-6 py-4 border-b border-yellow-300 mb-8 flex items-center shadow-sm">
            <h1 class="text-2xl font-semibold text-yellow-800">Supporter Dashboard</h1>
        </div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 px-6">
            <!-- User count -->
            <div class="bg-yellow-50 shadow-md rounded-xl p-6 flex items-center space-x-4 hover:shadow-lg transition">
                <div class="p-4 bg-yellow-200 text-yellow-700 rounded-full">
                    <span class="material-symbols-rounded text-3xl">person</span>
                </div>
                <div>
                    <h3 class="text-sm text-yellow-600">Users</h3>
                    <p class="text-xl font-bold">${totalUsers}</p>
                </div>
            </div>

            <!-- Today's Posts -->
            <div class="bg-yellow-50 shadow-md rounded-xl p-6 flex items-center space-x-4 hover:shadow-lg transition">
                <div class="p-4 bg-yellow-200 text-yellow-700 rounded-full">
                    <span class="material-symbols-rounded text-3xl">article</span>
                </div>
                <div>
                    <h3 class="text-sm text-yellow-600">Posts Today</h3>
                    <p class="text-xl font-bold">${todayProducts}</p>
                </div>
            </div>

            <!-- Revenue -->
            <div class="bg-yellow-50 shadow-md rounded-xl p-6 flex items-center space-x-4 hover:shadow-lg transition">
                <div class="p-4 bg-yellow-200 text-yellow-700 rounded-full">
                    <span class="material-symbols-rounded text-3xl">attach_money</span>
                </div>
                <div>
                    <h3 class="text-sm text-yellow-600">Revenue</h3>
                    <p class="text-xl font-bold">$${revenue}</p>
                </div>
            </div>

            <!-- Total Posts -->
            <div class="bg-yellow-50 shadow-md rounded-xl p-6 flex items-center space-x-4 hover:shadow-lg transition">
                <div class="p-4 bg-yellow-200 text-yellow-700 rounded-full">
                    <span class="material-symbols-rounded text-3xl">summarize</span>
                </div>
                <div>
                    <h3 class="text-sm text-yellow-600">Total Posts</h3>
                    <p class="text-xl font-bold">${totalProducts}</p>
                </div>
            </div>
        </div>

        <!-- Chat component -->
        <c:import url="/JSP/Conversation/chatUI.jsp" />

    </main>

</body>
</html>
