<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--=============== REMIXICONS ===============-->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/remixicon/4.2.0/remixicon.css">
        <!--=============== CSS ===============-->
        <link rel="stylesheet" href="css/join_in.css">    
    </head>
    <body>
        <!--=============== MODAL ===============-->
        <div id="loginModal" class="modal">
            <div class="modal-content">
                <div class="login container grid" id="loginAccessRegister">
                    <c:import url="/JSP/Authenticate/LoginForm.jsp" />
                    <c:import url="/JSP/Authenticate/RegisterForm.jsp" />
                </div>
            </div>
        </div>

        <script src="js/join_in.js"></script>
    </body>
</html>
