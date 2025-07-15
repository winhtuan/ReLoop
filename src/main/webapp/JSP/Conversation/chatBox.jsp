<%@page contentType="text/html" pageEncoding="UTF-8"%>
<section id="chatbox">
    <!-- Chat Button -->
    <div id="chatButton" onclick="toggleChat()">
        <ion-icon name="chatbubbles-outline"></ion-icon>
    </div>

    <!-- Chat Container -->
    <div id="chatContainer">
        <div id="chatHeader">
            <div style="display: flex; align-items: center; gap: 8px;">
                <ion-icon name="logo-reddit"></ion-icon>
                <span>ReLoop Assistant</span>
            </div>
            <button onclick="toggleChat()" title="Đóng chat">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
                </svg>
            </button>
        </div>

        <div id="chatBoxAI"></div>

        <div id="butt">
            <input type="text" id="userInputAI" placeholder="Nhập tin nhắn..." onkeypress="handleKeyPresss(event)" />
            <button id="buttonChat" onclick="sendMessage()" title="Gửi tin nhắn">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
                </svg>
            </button>
        </div>
    </div>
</section>
