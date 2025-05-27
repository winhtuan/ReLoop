<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body {
            background: linear-gradient(135deg, #e0eafc, #cfdef3);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            font-family: 'Inter', sans-serif;
            position: relative;
            overflow: hidden;
        }

        .reset-password-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 2rem;
            width: 100%;
            max-width: 400px;
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
            position: relative;
            z-index: 10;
        }

        .signin-button {
            position: fixed;
            top: 1rem;
            right: 1rem;
            background: #4c51bf;
            color: white;
            padding: 0.5rem 1.5rem;
            border-radius: 20px;
            font-weight: 500;
            font-size: 1rem;
            transition: all 0.3s ease;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
            z-index: 20;
        }

        .signin-button:hover {
            background: #3b82f6;
            transform: translateY(-2px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
        }

        .input-field {
            width: 100%;
            padding: 0.75rem;
            margin-bottom: 1rem;
            border: 1px solid #e5e7eb;
            border-radius: 10px;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        .input-field:focus {
            border-color: #4c51bf;
            outline: none;
            box-shadow: 0 0 0 3px rgba(76, 81, 191, 0.2);
        }

        .submit-button {
            width: 100%;
            padding: 0.75rem;
            background: #4c51bf;
            color: white;
            border-radius: 20px;
            font-size: 1.1rem;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .submit-button:hover {
            background: #3b82f6;
            transform: translateY(-2px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
        }

        .back-to-login {
            text-align: center;
            margin-top: 1rem;
            font-size: 0.875rem;
            color: #4c51bf;
        }

        .message {
            background: #eff6ff;
            border: 1px solid #bfdbfe;
            color: #2563eb;
            padding: 0.75rem;
            border-radius: 10px;
            text-align: center;
            margin-bottom: 1rem;
        }

        .error-message {
            background: #fef2f2;
            border: 1px solid #fecaca;
            color: #dc2626;
            padding: 0.75rem;
            border-radius: 10px;
            text-align: center;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <a href="login.jsp" class="signin-button">Sign In</a>
    <div class="reset-password-card">
        <section class="text-center">
            <h2 class="text-2xl font-bold text-gray-800 mb-4">Reset Password</h2>
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            <% if (request.getAttribute("message") != null) { %>
                <div class="message">
                    <%= request.getAttribute("message") %>
                </div>
            <% } %>
            <% if (request.getAttribute("showForm") == null || !(Boolean)request.getAttribute("showForm")) { %>
                <div class="back-to-login">
                    <a href="login.jsp" class="text-blue-600 hover:underline font-medium">Back to Login</a>
                </div>
            <% } else { %>
                <form action="resetPassword" method="post" class="space-y-4">
                    <input type="hidden" name="token" value="<%= request.getParameter("token") %>">
                    <div>
                        <label for="newPassword" class="block text-sm font-medium text-gray-700 mb-1">New Password</label>
                        <input type="password" name="newPassword" id="newPassword" class="input-field" placeholder="New Password" required>
                    </div>
                    <div>
                        <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-1">Confirm Password</label>
                        <input type="password" name="confirmPassword" id="confirmPassword" class="input-field" placeholder="Confirm Password" required>
                    </div>
                    <button type="submit" class="submit-button">Reset Password</button>
                </form>
            <% } %>
        </section>
    </div>
</body>
</html>