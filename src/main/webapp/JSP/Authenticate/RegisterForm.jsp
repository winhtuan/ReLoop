<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!--===== LOGIN REGISTER =====-->
<div class="login__register">
    <h1 class="login__title" style="text-align: center;">Create new account.</h1>

    <div class="login__area">
        <form action="RegisterServlet" method="" class="login__form">
            <div class="login__content grid">
                <div class="login__group grid">
                    <div class="login__box">
                        <input name="name" type="text" id="names" required placeholder=" " class="login__input">
                        <label for="names" class="login__label">Names</label>

                        <i class="ri-id-card-fill login__icon"></i>
                    </div>

                    <div class="login__box">
                        <input name="surname" type="text" id="surnames" required placeholder=" " class="login__input">
                        <label for="surnames" class="login__label">Surnames</label>

                        <i class="ri-id-card-fill login__icon"></i>
                    </div>
                </div>

                <div class="login__box">
                    <input name="email" type="email" id="emailCreate" required placeholder=" " class="login__input">
                    <label for="emailCreate" class="login__label">Email</label>

                    <i class="ri-mail-fill login__icon"></i>
                </div>

                <div class="login__box">
                    <input name="password" type="password" id="passwordCreate" required placeholder=" " class="login__input">
                    <label for="passwordCreate" class="login__label">Password</label>

                    <i class="ri-eye-off-fill login__icon login__password" id="loginPasswordCreate"></i>
                </div>
            </div>

            <button type="submit" class="login__button">Create account</button>
        </form>

        <p class="login__switch">
            Already have an account? 
            <button id="loginButtonAccess">Log In</button>
        </p>
    </div>
</div>