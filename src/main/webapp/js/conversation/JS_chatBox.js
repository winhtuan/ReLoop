function toggleChat() {
    let chatContainer = document.getElementById("chatContainer");
    chatContainer.style.display = (chatContainer.style.display === "none" || chatContainer.style.display === "") ? "block" : "none";

    let chatBox = document.getElementById("chatBoxAI");

    if (!chatBox.dataset.initialized) {
        chatBox.innerHTML += `
            <div class="chat-bubble bot">
                <b>Chào mừng bạn đến với ReLoop!</b>
            </div>
            <div class="chat-bubble bot">
                <b>Bạn muốn mua gì hôm nay nào?</b>
            </div>
        `;
        chatBox.dataset.initialized = "true";
    }
}


async function sendMessage() {
    let userInput = document.getElementById("userInputAI").value.trim();
    if (userInput === "") return;

    let requestData = {
        message: userInput,
//        database: databaseScript,
        instruction: "Chỉ trả lời dựa trên dữ liệu trong database. Nếu không có thông tin, hãy trả lời: 'Vui lòng hỏi câu liên quan đến đồ vạt bạn muốn mua.'"
    };

    let chatBox = document.getElementById("chatBoxAI");
    chatBox.innerHTML += `<div class=\"messageAI\"><b>Bạn:</b> ${userInput}</div>`;
    document.getElementById("userInputAI").value = "";

    try {
        let response = await fetch("/ReLoop/s_chatBox", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestData)
        });

        let data = await response.json();
        let botResponse = data.response || "Không có phản hồi từ AI.";
        chatBox.innerHTML += `<div class=\"messageAI\"><b>ReLoop:</b> ${botResponse}</div>`;
    } catch (error) {
        console.error("Lỗi kết nối AI:", error);
        chatBox.innerHTML += `<div style="color:red;"><b>Lỗi:</b> Không thể kết nối AI!</div>`;
    }

    chatBox.scrollTop = chatBox.scrollHeight;
}

function handleKeyPresss(event) {
    if (event.key === "Enter") {
        event.preventDefault();
        sendMessage();
    }
}
