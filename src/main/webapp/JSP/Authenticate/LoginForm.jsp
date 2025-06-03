<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="Utils.AppConfig" %>

<!--===== LOGIN ACCESS =====-->
<div class="login__access">
    <h1 class="login__title" style="text-align: center;">Log in to your account.</h1>

    <div class="login__area">
        <form action="LoginServlet" method="post" class="login__form">
            <div class="login__content grid">
                <div class="login__box">
                    <input name="email" type="email" id="email" required placeholder=" " class="login__input">
                    <label for="email" class="login__label">Email</label>         
                    <i class="ri-mail-fill login__icon"></i>
                </div>

                <div class="login__box">
                    <input name="password" type="password" id="password" required placeholder=" " class="login__input">
                    <label for="password" class="login__label">Password</label>
                    <i class="ri-eye-off-fill login__icon login__password" id="loginPassword"></i>
                </div>
            </div>

            <a href="JSP/Authenticate/forgotPassword.jsp" class="login__forgot">Forgot your password?</a>

            <button type="submit" class="login__button">Login</button>
        </form>

        <div class="login__social">
            <p class="login__social-title">Or login with</p>
            <%
                AppConfig appConfig = new AppConfig();
            %>
            <div class="login__social-links">
                <a class="login__social-link" href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&redirect_uri=<%= appConfig.get("google.redirect_uri") %>&response_type=code&client_id=<%= appConfig.get("google.client_id") %>&approval_prompt=force">
                    <img src="img/modal-img/icon-google.svg" alt="image" class="login__social-img">
                </a>

                <a class="login__social-link" href="https://www.facebook.com/v20.0/dialog/oauth?client_id=<%= appConfig.get("facebook.app_id") %>&redirect_uri=http://localhost:8080/ReLoop/LoginServlet&state=facebook&scope=email">
                    <img src="img/modal-img/icon-facebook.svg" alt="image" class="login__social-img">
                </a>

                <a href="#" class="login__social-link">
                    <img src="img/modal-img/icon-apple.svg" alt="image" class="login__social-img">
                </a>
            </div>
        </div>

        <p class="login__switch">
            Don't have an account? 
            <button id="loginButtonRegister">Create Account</button>
        </p>
    </div>
</div>