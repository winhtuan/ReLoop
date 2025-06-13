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
                            <li data-user-id="${u.userId}" onclick="selectChatUser('${u.userId}', '${u.fullName}')">
                                <img src="${pageContext.request.contextPath}/img/bg-img/1.jpg" alt="Profile" style=" width:7%; vertical-align: middle; margin-right: 5px; border-radius: 50%;"/>
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

                <div class="inputBox" style="position: relative; display: flex; align-items: center; gap: 10px; margin-top: 10px;">
                    <input type="text" id="messageInput" placeholder="Type a message..." onkeydown="handleKeyPress(event)" style="flex: 1; padding: 10px; border: 1px solid #ccc; border-radius: 6px; font-size: 14px;" />
                    <label id="emojiBtn" style="cursor: pointer; font-size: 20px;">😊</label>
                    <div id="emojiPicker" style="position: absolute; bottom: 60px; right: 20px; display: none; z-index: 1000;"></div>
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

            const lastNotificationTimestamps = {};
            const NOTIFY_COOLDOWN_MS = 500;

            ws.onmessage = function (event) {
                const msg = JSON.parse(event.data);
                if (msg.type === "unread_list") {
                    msg.senders.forEach(senderId => markUserAsUnread(senderId));
                    return;
                }
                if (msg.type === "block_status") {
                    const messageInput = document.getElementById("messageInput");
                    const sendBtn = document.querySelector(".inputBox button");
                    const blockBtn = document.getElementById("blockBtn");
                    const unblockBtn = document.getElementById("unblockBtn");
                    const blockNotice = document.getElementById("blockNotice");

                    if ((msg.status === "blocked_by_me" || msg.status === "unblocked_by_me") && currentChatUserId !== msg.blockedId)
                        return;
                    if ((msg.status === "blocked_me" || msg.status === "unblocked_me") && currentChatUserId !== msg.blockerId)
                        return;

                    if (msg.status === "blocked_by_me" || msg.status === "blocked_me") {
                        messageInput.style.display = "none";
                        sendBtn.style.display = "none";
                        blockBtn.style.display = "none";
                        document.getElementById("imageUploadLabel").style.display = "none";
                        document.getElementById("imageUpload").style.display = "none";
                        document.getElementById("emojiBtn").style.display = "none";
                        if (msg.status === "blocked_by_me")
                            unblockBtn.style.display = "inline-block";
                        else
                            unblockBtn.style.display = "none";
                        blockNotice.textContent = msg.status === "blocked_by_me" ? "Bạn đã block người dùng này" : "Người dùng này đã block bạn";
                        blockNotice.style.display = "block";
                    } else if (msg.status === "unblocked_by_me" || msg.status === "unblocked_me") {
                        messageInput.style.display = "block";
                        sendBtn.style.display = "inline-block";
                        blockBtn.style.display = "inline-block";
                        unblockBtn.style.display = "none";
                        blockNotice.style.display = "none";
                        document.getElementById("imageUploadLabel").style.display = "inline-block";
                        document.getElementById("imageUpload").style.display = "none";
                        document.getElementById("emojiBtn").style.display = "inline-block";
                    }
                    return;
                }

                if (msg.type === "recall") {
                    handleRecallMessage(msg);
                    return;
                }

                if (msg.type === "recall_failed") {
                    alert(msg.message);
                    return;
                }

                const isRelated = (msg.fromUserId === currentChatUserId && msg.toUserId === currentUserId) ||
                        (msg.toUserId === currentChatUserId && msg.fromUserId === currentUserId);
                const isSentByMe = msg.fromUserId === currentUserId;

                if (isRelated)
                    addMessageToChatBox(msg);

                if (!isSentByMe && (!isRelated || document.hidden)) {
                    const now = Date.now();
                    const lastTime = lastNotificationTimestamps[msg.fromUserId] || 0;
                    if (now - lastTime > NOTIFY_COOLDOWN_MS) {
                        showBrowserNotification(msg.fromUsername, msg.content);
                        lastNotificationTimestamps[msg.fromUserId] = now;
                    }
                }

                if (!isSentByMe && msg.fromUserId !== currentChatUserId)
                    markUserAsUnread(msg.fromUserId);
                else
                    markMessagesAsRead(msg.fromUserId);
            };
        </script>
        <script type="module" src="https://cdn.jsdelivr.net/npm/emoji-picker-element@1.6.2/index.js"></script>
        <script type="module">
            const emojiBtn = document.getElementById("emojiBtn");
            const emojiPickerContainer = document.getElementById("emojiPicker");
            const messageInput = document.getElementById("messageInput");

            const picker = document.createElement('emoji-picker');
            emojiPickerContainer.appendChild(picker);

            emojiBtn.addEventListener('click', () => {
                emojiPickerContainer.style.display = emojiPickerContainer.style.display === 'none' ? 'block' : 'none';
            });

            picker.addEventListener('emoji-click', event => {
                messageInput.value += event.detail.unicode;
                emojiPickerContainer.style.display = 'none';
                messageInput.focus();
            });

            // Đóng emoji picker nếu click ra ngoài
            document.addEventListener("click", function (event) {
                if (!emojiPickerContainer.contains(event.target) && event.target !== emojiBtn) {
                    emojiPickerContainer.style.display = "none";
                }
            });
        </script>

        <script src="${pageContext.request.contextPath}/js/conversation/JS_chatFunction.js"></script>

        <c:if test="${not empty requestScope.sellid}">
            <script>
            loadChatHistory(${requestScope.sellid});
            </script>
        </c:if>

    </body>
</html>
