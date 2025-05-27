<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password</title>
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

        .forgot-password-card {
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
        }

        .back-to-login {
            text-align: center;
            margin-top: 1rem;
            font-size: 0.875rem;
            color: #4c51bf;
        }
    </style>
</head>
<body>
    <div class="forgot-password-card">
        <section class="text-center">
            <h2 class="text-2xl font-bold text-gray-800 mb-4">Forgot Password</h2>
            <% if (request.getAttribute("message") != null) { %>
                <div class="bg-blue-50 border border-blue-300 text-blue-600 px-4 py-2 rounded-lg text-center mb-6" role="alert">
                    <%= request.getAttribute("message") %>
                </div>
            <% } %>
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="bg-red-50 border border-red-300 text-red-600 px-4 py-2 rounded-lg text-center mb-6" role="alert">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            <form action="forgotPassword" method="post" class="space-y-4">
                <div>
                    <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                    <input type="email" name="email" id="email" class="input-field" placeholder="name@example.com" required>
                </div>
                <button type="submit" class="submit-button">Submit</button>
            </form>
            <p class="back-to-login">
                <a href="login.jsp" class="text-blue-600 hover:underline font-medium">Back to Login</a>
            </p>
        </section>
    </div>
</body>
</html>