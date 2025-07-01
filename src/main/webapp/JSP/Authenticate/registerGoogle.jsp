<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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

        .register-card {
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
    <a href="JSP/Authenticate/JoinIn.jsp" class="signin-button">Sign In</a>
    <div class="register-card">
        <section class="text-center">
            <div class="mb-5">
                <h2 class="text-3xl font-bold text-gray-800">Complete Your Registration</h2>
                <p class="text-gray-600 mt-2">Welcome to ReLoop</p>
            </div>
            <c:if test="${not empty requestScope.erormess}">
                <div class="error-message">
                    ${requestScope.erormess}
                </div>
            </c:if>
            <form method="post" action="s_regisGoogle" class="space-y-4">
                <div>
                    <label for="address" class="block text-sm font-medium text-gray-700 mb-1">Address</label>
                    <input type="text" name="address" id="address" class="input-field" placeholder="Address" required>
                </div>
                <div>
                    <label for="Phone" class="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
                    <input type="text" name="Phone" id="Phone" class="input-field" placeholder="Phone Number" required>
                </div>
                <button type="submit" class="submit-button">Submit</button>
            </form>
            <div class="back-to-login">
                <a href="JSP/Authenticate/JoinIn.jsp" class="text-blue-600 hover:underline font-medium">Back to Login</a>
            </div>
        </section>
    </div>
</body>
</html>