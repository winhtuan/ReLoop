<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Chat</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/conversation/CSS_chatFunction.css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    </head>
    <body>
        <h2>Welcome, ${cus.fullName}</h2>

        <div class="chat-container">
            <div class="chat-users">
                <h3>Users</h3>
                <input type="text" id="search" placeholder="Search" style="width: 100%; padding: 5px; margin-bottom: 10px;"/>
                <ul id="userList">
                    <c:forEach var="u" items="${userList}">
                        <c:if test="${u.userId != cus.userId}">
                            <li data-customerid="${u.customerId}" onclick="selectChatUser('${u.userId}', '${u.fullName}')">
                                <img src="https://via.placeholder.com/30" alt="Profile" style="vertical-align: middle; margin-right: 5px; border-radius: 50%;"/>
                                ${u.fullName}
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
            </div>

            <div class="chat-main">
                <h3>
                    Chat with <span id="chatWith">[Select user]</span>
                    <c:if test="${not empty sessionScope.chatProductId}">
                        <span> about Product ID: ${sessionScope.chatProductId}</span>
                    </c:if>
                    <div class="dropdown" style="display: inline-block; margin-left: 10px; position: relative;">
                        <button class="dropdown-btn">⋮</button>
                        <div class="dropdown-content" style="top: 20px; right: 0;">
                            <button id="blockBtn" onclick="blockUser()" style="display: none;">Block</button>
                            <button id="unblockBtn" onclick="unblockUser()" style="display: none;">Unblock</button>
                        </div>
                    </div>
                </h3>

                <div id="chatBox"></div>

                <div class="inputBox" style="display: flex; align-items: center; gap: 10px; margin-top: 10px;">
                    <input type="text" id="messageInput" placeholder="Type a message..." onkeydown="handleKeyPress(event)" style="flex: 1; padding: 10px; border: 1px solid #ccc; border-radius: 6px; font-size: 14px;" />
                    <label id="imageUploadLabel" for="imageUpload" style="cursor: pointer; font-size: 20px;">📷</label>
                    <input type="file" id="imageUpload" style="display: none;" multiple accept="image/*" onchange="sendImage()" />
                    <button onclick="sendMessages()" style="padding: 10px 16px; font-size: 14px; background-color: #007bff; border: none; color: white; border-radius: 6px; cursor: pointer;">Send</button>
                </div>
                <p id="blockNotice" style="color: red; font-weight: bold; display: none;"></p>
            </div>
        </div>

        <script>
        const currentUserId = "${cus.userId}";
        const currentUsername = "${cus.fullName}";
        let currentChatUserId = null;
        let currentChatUserName = null;

        const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));

        const ws = new WebSocket("ws://" + location.host + "/ReLoop/chat");
        ws.onopen = () => console.log("WebSocket connected");
        ws.onclose = () => console.log("WebSocket closed");
        ws.onerror = err => console.error("WebSocket error", err);

        // ... Các logic JS y như cũ
        </script>

        <script src="${pageContext.request.contextPath}/js/conversation/JS_chatFunction.js"></script>

        <c:if test="${not empty requestScope.sellid}">
            <script>
        loadChatHistory(${requestScope.sellid});
            </script>
        </c:if>

    </body>
</html>
