<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Forgot Password</title>
        <link rel="stylesheet" href="css/fw.css">
        <style>
            :root {
                --first-color: #007BFF;
                --text-color: #333;
                --title-color: #000;
                --container-color: #fff;
                --font-semi-bold: 600;
                --tiny-font-size: 0.75rem;
            }

            body {
                font-family: Arial, sans-serif;
                display: flex;
                justify-content: center;      /* Căn giữa theo chiều ngang */
                align-items: center;          /* Căn giữa theo chiều dọc */
                height: 100vh;                /* Chiều cao toàn màn hình */
                margin: 0;
                background-color: #f5f5f5;    /* Màu nền nhẹ */
            }

            .forgot-password-card {
                background: white;
                padding: 2rem;
                border-radius: 1rem;
                box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
                width: 100%;
                max-width: 450px;
                box-sizing: border-box;
            }
            h2 {
                text-align: center;
                margin-bottom: 1.5rem;
            }

            .form-group {
                position: relative;
                margin-bottom: 1.5rem;
            }

            .login__input {
                width: 100%;
                padding: 1.2rem 0.75rem 0.5rem 0.75rem;
                font-size: 1rem;
                font-weight: 400;
                border: 2px solid #ccc;
                border-radius: 0.75rem;
                background-color: #f7f9fc;
                outline: none;
                transition: border-color 0.3s, background-color 0.3s;
                box-sizing: border-box;
            }


            .login__input:focus {
                border-color: #1a73e8;
                background-color: #f1f7ff;
            }

            .login__label {
                position: absolute;
                left: 0.75rem;
                top: 0.95rem;
                color: #666;
                pointer-events: none;
                font-size: 0.9rem;
                background-color: transparent;
                transition: all 0.3s ease;
            }

            .login__input:focus + .login__label,
            .login__input:not(:placeholder-shown) + .login__label {
                top: 0.4rem;
                left: 0.75rem;
                font-size: 0.75rem;
                color: #1a73e8;
                background-color: #f7f9fc;
                padding: 0 0.25rem;
            }


            button {
                width: 100%;
                padding: 0.75rem;
                background-color: var(--first-color);
                color: white;
                border: none;
                border-radius: 0.75rem;
                cursor: pointer;
                font-weight: bold;
            }

            button:hover {
                background-color: #0056b3;
            }

            .alert {
                padding: 1rem;
                margin-bottom: 1rem;
                border-radius: 0.75rem;
                text-align: center;
            }

            .alert-info {
                background-color: #e7f3fe;
                color: #31708f;
                border: 1px solid #bce8f1;
            }

            .alert-error {
                background-color: #f2dede;
                color: #a94442;
                border: 1px solid #ebccd1;
            }

            .back-link {
                margin-top: 1rem;
                text-align: center;
            }

            .back-link a {
                color: black;
                text-decoration: none;
            }

            .back-link a:hover {
                color: var(--first-color);
            }
        </style>
    </head>
    <body>
        <div class="forgot-password-card">
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
                <a href="javascript:history.back()">Back to Sign In</a>
            </div>
        </div>
    </body>
</html>
