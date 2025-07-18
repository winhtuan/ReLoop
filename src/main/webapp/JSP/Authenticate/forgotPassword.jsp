<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <!-- Google Font -->
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- CSS chung -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/fw.css">
    
    <style>
        :root {
            --main-yellow: #ffc107;
            --main-black: #111;
            --main-white: #fff;
            --main-gray: #888;
            --main-bg-gray: #f5f5f5;
        }

        body {
            font-family: 'Montserrat', sans-serif;
            background-color: var(--main-bg-gray);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .card {
            background-color: var(--main-white);
            padding: 2rem;
            border-radius: 1.5rem;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
            max-width: 420px;
            width: 100%;
            box-sizing: border-box;
        }

        h2 {
            text-align: center;
            margin-bottom: 1.5rem;
            font-weight: 600;
            color: var(--main-black);
        }

        .form-group {
            position: relative;
            margin-bottom: 1.5rem;
        }

        .login__input {
            width: 100%;
            padding: 1.2rem 0.75rem 0.5rem 0.75rem;
            font-size: 1rem;
            border: 2px solid #ccc;
            border-radius: 0.75rem;
            background-color: #f7f9fc;
            outline: none;
            transition: border-color 0.3s;
        }

        .login__input:focus {
            border-color: var(--main-yellow);
            background-color: #fffef4;
        }

        .login__label {
            position: absolute;
            left: 0.75rem;
            top: 0.95rem;
            color: #666;
            pointer-events: none;
            font-size: 0.9rem;
            transition: all 0.3s ease;
            background-color: transparent;
        }

        .login__input:focus + .login__label,
        .login__input:not(:placeholder-shown) + .login__label {
            top: 0.4rem;
            left: 0.75rem;
            font-size: 0.75rem;
            color: var(--main-yellow);
            background-color: #f7f9fc;
            padding: 0 0.25rem;
        }

        button {
            width: 100%;
            padding: 0.75rem;
            background-color: var(--main-yellow);
            color: var(--main-black);
            border: none;
            border-radius: 0.75rem;
            font-weight: 600;
            cursor: pointer;
            font-size: 1rem;
        }

        button:hover {
            background-color: #e0a800;
        }

        .alert {
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: 0.75rem;
            text-align: center;
            font-size: 0.9rem;
        }

        .alert-info {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeeba;
        }

        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .back-link {
            margin-top: 1.2rem;
            text-align: center;
        }

        .back-link a {
            color: var(--main-black);
            text-decoration: none;
            font-size: 0.9rem;
        }

        .back-link a:hover {
            color: var(--main-yellow);
        }
    </style>
</head>
<body>
    <div class="card">
        <h2>Forgot Password</h2>

        <c:if test="${not empty message}">
            <div class="alert alert-info">${message}</div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <form action="forgotPassword" method="post">
            <div class="form-group">
                <input type="email" name="email" id="email" class="login__input" placeholder=" " required>
                <label for="email" class="login__label">Email</label>
            </div>
            <button type="submit">Submit</button>
        </form>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/home?login=true">Back to Sign In</a>
        </div>
    </div>
</body>
</html>
