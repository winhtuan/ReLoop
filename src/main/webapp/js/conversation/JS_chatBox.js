let isTyping = false;

function toggleChat() {
    let chatContainer = document.getElementById("chatContainer");
    let isVisible = chatContainer.classList.contains("show");

    if (isVisible) {
        chatContainer.classList.remove("show");
        chatContainer.classList.add("hide");
        setTimeout(() => {
            chatContainer.style.display = "none";
            chatContainer.classList.remove("hide");
            chatContainer.style.animation = "";
            chatContainer.style.zIndex = "";
        }, 350);
    } else {
        chatContainer.style.display = "block";
        chatContainer.style.zIndex = "1000";
        chatContainer.classList.add("show");
        chatContainer.classList.remove("hide");
        setTimeout(() => {
            chatContainer.style.animation = "";
        }, 350);
    }

    let chatBox = document.getElementById("chatBoxAI");
    if (!chatBox.dataset.initialized) {
        setTimeout(() => {
            addBotMessage("Chào mừng bạn đến với <strong>ReLoop</strong>! 👋", true, "welcome");
        }, 600);
        setTimeout(() => {
            addBotMessage("Tôi là trợ lý AI thông minh, có thể giúp bạn tìm kiếm sản phẩm và giải đáp mọi thắc mắc. Bạn muốn mua gì hôm nay nào? 🛍️", true);
        }, 1800);
        chatBox.dataset.initialized = "true";
    }
}

function addBotMessage(message, isWelcome = false, className = "") {
    let chatBox = document.getElementById("chatBoxAI");
    let messageDiv = document.createElement("div");
    messageDiv.className = `chat-bubble bot ${className}`;
    messageDiv.innerHTML = message;
    
    if (isWelcome) {
        messageDiv.style.opacity = "0";
        messageDiv.style.transform = "translateY(15px) scale(0.9)";
    }
    
    chatBox.appendChild(messageDiv);
    scrollToBottomChat();

    if (isWelcome) {
        setTimeout(() => {
            messageDiv.style.transition = "all 0.4s ease";
            messageDiv.style.opacity = "1";
            messageDiv.style.transform = "translateY(0) scale(1)";
        }, 100);
    }
}

function addUserMessage(message) {
    let chatBox = document.getElementById("chatBoxAI");
    let messageDiv = document.createElement("div");
    messageDiv.className = "chat-bubble user";
    messageDiv.innerHTML = message;
    messageDiv.style.opacity = "0";
    messageDiv.style.transform = "translateY(15px) scale(0.9)";
    
    chatBox.appendChild(messageDiv);
    scrollToBottomChat();

    setTimeout(() => {
        messageDiv.style.transition = "all 0.4s ease";
        messageDiv.style.opacity = "1";
        messageDiv.style.transform = "translateY(0) scale(1)";
    }, 100);
}

function showTypingIndicator() {
    let chatBox = document.getElementById("chatBoxAI");
    // Xóa typing indicator cũ nếu có
    let oldTyping = document.getElementById("typingIndicator");
    if (oldTyping) oldTyping.remove();
    let typingDiv = document.createElement("div");
    typingDiv.className = "typing-indicator";
    typingDiv.id = "typingIndicator";
    typingDiv.innerHTML = `
        <span style="display: flex; align-items: center; gap: 3px;">
            <span class="typing-dot" style="width:8px;height:8px;background:#bdbdbd;border-radius:50%;display:inline-block;animation:typingBounce 1.4s infinite ease-in-out;"></span>
            <span class="typing-dot" style="width:8px;height:8px;background:#bdbdbd;border-radius:50%;display:inline-block;animation:typingBounce 1.4s infinite ease-in-out;animation-delay:-0.16s;"></span>
            <span class="typing-dot" style="width:8px;height:8px;background:#bdbdbd;border-radius:50%;display:inline-block;animation:typingBounce 1.4s infinite ease-in-out;animation-delay:-0.32s;"></span>
        </span>
    `;
    typingDiv.style.opacity = "0";
    typingDiv.style.transform = "translateY(10px)";
    chatBox.appendChild(typingDiv);
    scrollToBottomChat();
    setTimeout(() => {
        typingDiv.style.transition = "all 0.3s ease";
        typingDiv.style.opacity = "1";
        typingDiv.style.transform = "translateY(0)";
    }, 100);
}

function hideTypingIndicator() {
    let typingIndicator = document.getElementById("typingIndicator");
    if (typingIndicator) {
        typingIndicator.style.opacity = "0";
        typingIndicator.style.transform = "translateY(-10px)";
        setTimeout(() => {
            typingIndicator.remove();
            scrollToBottomChat();
        }, 300);
    }
}

async function sendMessage() {
    let userInput = document.getElementById("userInputAI");
    let userMessage = userInput.value.trim();
    
    if (userMessage === "" || isTyping) return;

    isTyping = true;
    userInput.disabled = true;
    let sendButton = document.getElementById("buttonChat");
    sendButton.disabled = true;
    sendButton.style.opacity = "0.6";

    addUserMessage(userMessage);
    userInput.value = "";

    showTypingIndicator();

    let requestData = {
        message: userMessage,
        instruction: "Chỉ trả lời dựa trên dữ liệu trong database. Nếu không có thông tin, hãy trả lời: 'Vui lòng hỏi câu liên quan đến đồ vật bạn muốn mua.'"
    };

    try {
        let response = await fetch("/ReLoop/s_chatBox", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestData)
        });

        let data = await response.json();
        let botResponse = data.response || "Xin lỗi, tôi không thể xử lý yêu cầu của bạn ngay lúc này. Vui lòng thử lại sau.";
        
        hideTypingIndicator();
        
        setTimeout(() => {
            addBotMessage(botResponse);
        }, 600);
        
    } catch (error) {
        console.error("Lỗi kết nối AI:", error);
        hideTypingIndicator();
        setTimeout(() => {
            addBotMessage("❌ Xin lỗi, có lỗi kết nối. Vui lòng thử lại sau hoặc liên hệ hỗ trợ.");
        }, 600);
    } finally {
        setTimeout(() => {
            isTyping = false;
            userInput.disabled = false;
            sendButton.disabled = false;
            sendButton.style.opacity = "1";
            userInput.focus();
        }, 1200);
    }
}

function handleKeyPresss(event) {
    if (event.key === "Enter" && !event.shiftKey) {
        event.preventDefault();
        sendMessage();
    }
}

function scrollToBottomChat() {
    let chatBox = document.getElementById("chatBoxAI");
    if (chatBox) {
        const isNearBottom = chatBox.scrollHeight - chatBox.clientHeight <= chatBox.scrollTop + 100;
        if (isNearBottom) {
            chatBox.scrollTo({
                top: chatBox.scrollHeight,
                behavior: 'smooth'
            });
        }
    }
}