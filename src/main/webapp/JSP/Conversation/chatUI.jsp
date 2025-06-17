<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reloop | Chat Interface</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core-style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/avatar.css">
        <link rel="icon" href="${pageContext.request.contextPath}/img/core-img/favicon.ico">
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/conversation/CSS_chatFunction.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/conversation/chat.css"/>
    </head>

    <body>
        <div class="search-wrapper section-padding-100" style="display: none;">
            <div class="search-close">
                <i class="fa fa-close" aria-hidden="true"></i>
            </div>
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="search-content d-flex justify-content-center">
                            <form action="#" method="get" class="position-relative w-100" style="max-width: 700px;">
                                <input type="search" name="search" placeholder="Type your keyword...">
                                <button type="submit"><ion-icon name="search-outline"></ion-icon></button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="main-content-wrapper d-flex clearfix">
            <div class="mobile-nav">
                <div class="amado-navbar-brand">
                    <a href="${pageContext.request.contextPath}/home"><img src="${pageContext.request.contextPath}/img/core-img/logo.png" alt=""></a>
                </div>
                <div class="amado-navbar-toggler">
                    <span></span><span></span><span></span>
                </div>
            </div>

            <header class="header-area clearfix">
                <div class="nav-close">
                    <i class="fa fa-close" aria-hidden="true"></i>
                </div>
                <div class="logo">
                    <a href="${pageContext.request.contextPath}/home"><img src="${pageContext.request.contextPath}/img/core-img/logo.png" alt=""></a>
                </div>

                <nav class="amado-nav mt-30 mb-15">
                    <ul>
                        <li class="active"><a href="${pageContext.request.contextPath}/home">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/shop.html">Post</a></li>
                        <li><a href="#">Shop</a></li>
                        <li><a href="${pageContext.request.contextPath}/checkout.html">Premium</a></li>
                    </ul>
                </nav>

                <div class="cart-fav-search">
                    <a href="#" class="search-nav"><ion-icon name="search-outline"></ion-icon> Search</a>
                    <a href="${pageContext.request.contextPath}/cart.html" class="cart-nav"><ion-icon name="cart-outline"></ion-icon> Cart <span>(0)</span></a>
                    <div class="fav-nav favourite-container">
                        <a href="#"><ion-icon name="heart-outline"></ion-icon> Favourite</a>
                    </div>
                    <div class="not-nav notification-container">
                        <a href="#"><ion-icon name="notifications-outline"></ion-icon> Notification</a>
                    </div>
                    <div class="nav-brand">
                        <a href="#"><ion-icon name="person-outline"></ion-icon> Join In</a>
                    </div>
                </div>

                <div class="amado-btn-group mt-15 mb-100">
                    <a href="#" class="amado-btn-custom">
                        <ion-icon name="document-text-outline" class="btn-icon"></ion-icon>
                        <span>Up Post</span>
                    </a>
                </div>
            </header>

            <section class="products-catagories-area clearfix">

                <div class="container-fluid">
                    <div class="chat-container">
                        <div class="chat-sidebar">
                            <div class="chat-search">
                                <input type="text" id="search" placeholder="Search user..." />
                            </div>
                            <div class="chat-list">
                                <ul id="userList">
                                    <c:forEach var="u" items="${userList}">
                                        <c:if test="${u.userId != cus.userId}">
                                            <li data-user-id="${u.userId}" onclick="selectChatUser('${u.userId}', '${u.fullName}')">
                                                <img src="${pageContext.request.contextPath}/img/bg-img/1.jpg" alt="Profile" style="width: 48px; height: 48px; border-radius: 50%; border: 2px solid #fbb710; margin-right: 5px;"/>
                                                <div>
                                                    <strong>${u.fullName}</strong><br>
                                                </div>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>

                        <div class="chat-main" style="display: none;">
                            <div class="chat-header">
                                <div class="chat-header-user">
                                    <img src="https://placehold.co/400" alt="Avatar" class="chat-header-avatar">
                                    <div class="chat-header-info">
                                        <strong><span id="chatWith">[Select user]</span></strong>
                                        <small>
                                            <c:if test="${not empty sessionScope.chatProductId}">
                                                about Product ID: ${sessionScope.chatProductId}
                                            </c:if>
                                        </small>
                                    </div>
                                    <div class="dropdown" style="display: inline-block; margin-left: 10px; position: relative;">
                                        <div class="chat-actions">
                                            <ion-icon id="userDropDown" name="ellipsis-vertical-outline"></ion-icon>
                                        </div>
                                        <div id="userDropDownContent" class="dropdown-content" style="top: 20px; right: 0; display: none; ">
                                            <button id="blockBtn" onclick="blockUser()" style="display: none;">Block</button>
                                            <button id="unblockBtn" onclick="unblockUser()" style="display: none;">Unblock</button>
                                        </div>
                                    </div>
                                </div>
                                <c:if test="${not empty sessionScope.chatProductId}">
                                    <div class="chat-header-product">
                                        <a href="#"><img src="${pageContext.request.contextPath}/img/product-img/pro-big-1.jpg" alt="Product Image" class="chat-header-product-img"></a>
                                        <div class="chat-header-product-info">
                                            <strong>Cute Cat House</strong> <span>2,000,000 đ</span> </div>
                                    </div>
                                </c:if>
                            </div>

                            <div class="chat-body" id="chatBox">
                            </div>

                            <div class="inputBox chat-input-wrap" style="position: relative; display: flex; align-items: center; gap: 10px; margin-top: 10px;">
                                <label class="chat-btn" id="emojiBtn" style="cursor: pointer; font-size: 20px;">
                                    <ion-icon name="happy-outline"></ion-icon>
                                </label>
                                <div id="emojiPicker" style="position: absolute; bottom: 60px; left: 0; display: none; z-index: 1000;"></div>

                                <label id="imageUploadLabel" class="chat-btn" for="imageUpload" style="cursor: pointer; font-size: 20px;">
                                    <ion-icon name="images-outline"></ion-icon>
                                </label>
                                <input type="file" id="imageUpload" style="display: none;" multiple accept="image/*" onchange="sendImage()" />

                                <input type="text" id="messageInput" placeholder="Enter message..." onkeydown="handleKeyPress(event)" autocomplete="off" autocorrect="off" autocapitalize="off"/>
                                <button onclick="sendMessages()" class="chat-btn chat-btn-send">
                                    <ion-icon name="paper-plane-outline"></ion-icon>
                                </button>
                            </div>
                            <p id="blockNotice" style="color: red; font-weight: bold; display: none;"></p>
                        </div>
                    </div>
                </div>
            </section>
        </div>
        <input type="file" id="chatImageInput" accept="image/*" style="display: none;" multiple>

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

            // Close emoji picker if clicking outside
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