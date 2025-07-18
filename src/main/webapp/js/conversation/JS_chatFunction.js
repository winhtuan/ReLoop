function selectChatUser(userId, username) {
    document.querySelector(".chat-main").style.display = "flex";
    currentChatUserId = userId;
    currentChatUserName = username;
    document.getElementById("chatWith").textContent = username;

    document.querySelectorAll("#userList li").forEach(item => {
        item.classList.toggle("selected", parseInt(item.dataset.userid) === userId);
    });

    document.getElementById("messageInput").style.display = "block";
    document.querySelector(".inputBox button").style.display = "inline-block";
    document.getElementById("blockNotice").style.display = "none";
    
    loadChatHistory(userId);
    markMessagesAsRead(userId);

    fetch("/ReLoop/checkBlock?user1=" + currentUserId + "&user2=" + userId)
        .then(res => res.json())
        .then(data => {
            const blockBtn = document.getElementById("blockBtn");
            const unblockBtn = document.getElementById("unblockBtn");
            const messageInput = document.getElementById("messageInput");
            const sendBtn = document.querySelector(".inputBox button");
            const blockNotice = document.getElementById("blockNotice");
            const imageUploadl = document.getElementById("imageUploadLabel");
            imageUploadl.style.display = "none";
            const imageUpload = document.getElementById("imageUpload");
            imageUpload.style.display = "none";

            if (data.blockedByMe) {
                blockBtn.style.display = "none";
                unblockBtn.style.display = "inline-block";
                messageInput.style.display = "none";
                sendBtn.style.display = "none";
                blockNotice.style.display = "block";
                blockNotice.textContent = "You have blocked this user";
            } else if (data.blockedMe) {
                blockBtn.style.display = "none";
                unblockBtn.style.display = "none";
                messageInput.style.display = "none";
                sendBtn.style.display = "none";
                blockNotice.style.display = "block";
                blockNotice.textContent = "You have been blocked by this user";
            } else {
                blockBtn.style.display = "inline-block";
                unblockBtn.style.display = "none";
                messageInput.style.display = "block";
                sendBtn.style.display = "inline-block";
                blockNotice.style.display = "none";
                imageUpload.style.display = "none";
                imageUploadl.style.display = "inline-block";
            }
        })
        .catch(err => {
            console.error("Failed to check block status", err);
            document.getElementById("blockNotice").textContent = "Unable to check block status.";
            document.getElementById("blockNotice").style.display = "block";
        });

    const dropdownBtn = document.querySelector("#userDropDown");
    const dropdownContent = document.querySelector("#userDropDownContent");
    dropdownBtn.addEventListener("click", function (e) {
        e.stopPropagation();
        const isOpen = dropdownContent.style.display === "flex";
        document.querySelectorAll(".dropdown-content").forEach(content => content.style.display = "none");
        dropdownContent.style.display = isOpen ? "none" : "flex";
    });
}

function addMessageToChatBox(msg) {
    const chatBox = document.getElementById("chatBox");
    const isSentByMe = (msg.fromUserId ?? msg.senderId) === currentUserId;
    const msID = msg.messageId;
    const content = msg.content;
    const avatarUrl = "https://placehold.co/400";
    const senderName = isSentByMe ? "" : currentChatUserName + ":";

    let timeText = "";
    if (msg.sentAt) {
        const time = new Date(msg.sentAt);
        timeText = isNaN(time)
            ? '<small style="color:red">Invalid Date</small>'
            : time.toLocaleString("vi-VN", { hour: "2-digit", minute: "2-digit", weekday: "short" });
    }

    const isRecalled = msg.is_recall === true || msg.type === "recall";

    const wrapper = document.createElement("div");
    wrapper.className = "chat-msg" + (isSentByMe ? " me" : "");
    wrapper.dataset.messageId = msID;

    const avatar = document.createElement("img");
    avatar.className = "avatar";
    avatar.src = avatarUrl;
    avatar.alt = "avatar";

    const contentBox = document.createElement("div");
    contentBox.className = "chat-msg-content";

    const timeSpan = document.createElement("span");
    timeSpan.className = "time";
    timeSpan.innerHTML = timeText;

    if (isRecalled) {
        const p = document.createElement("p");
        p.classList.add("msg-recalled");
        p.innerHTML = `<b>${senderName}</b>Message has been recalled`;
        contentBox.appendChild(p);
    } else {
        if (msg.type === "img") {
            contentBox.appendChild(addImageToChatBox(msg));
        } else {
            const p = document.createElement("p");
            p.innerHTML = content;
            p.style.margin = 0;
            contentBox.appendChild(p);
        }
    }

    const actions = document.createElement("div");
    actions.className = "chat-actions";

    const dropdownBtn = document.createElement("ion-icon");
    dropdownBtn.name = "ellipsis-vertical-outline";
    actions.appendChild(dropdownBtn);

    const menu = document.createElement("div");
    menu.className = "dropdown-content";
    menu.style.display = "none";

    const copyBtn = document.createElement("button");
    copyBtn.textContent = "Copy";
    copyBtn.onclick = () => copyMessage(encodeURIComponent(content), copyBtn);
    menu.appendChild(copyBtn);

    if (isSentByMe && msID !== undefined && msID !== null && msID !== "") {
        const recallBtn = document.createElement("button");
        recallBtn.textContent = "Recall";
        recallBtn.onclick = () => recallMessage(msID);
        menu.appendChild(recallBtn);
    }

    actions.appendChild(menu);

    dropdownBtn.addEventListener("click", (e) => {
        e.stopPropagation();
        const isOpen = menu.style.display === "flex";
        document.querySelectorAll(".dropdown-content").forEach(m => m.style.display = "none");
        menu.style.display = isOpen ? "none" : "flex";
    });

    document.addEventListener("click", () => menu.style.display = "none");

    contentBox.appendChild(timeSpan);
    if (!isRecalled) {
        contentBox.appendChild(actions);
    }

    wrapper.appendChild(avatar);
    wrapper.appendChild(contentBox);
    chatBox.appendChild(wrapper);

    chatBox.scrollTop = chatBox.scrollHeight;
}

function addImageToChatBox(msg) {
    const msID = msg.messageId;

    let mediaElement;
    if (getFileExtension(msg.content) === "mp4") {
        mediaElement = document.createElement("video");
        mediaElement.controls = true;
    } else {
        mediaElement = document.createElement("img");
    }

    mediaElement.src = msg.content;
    mediaElement.className = "chat-img";

    return mediaElement;
}

function handleRecallMessage(msg) {
    const isSentByMe = (msg.fromUserId !== undefined && msg.fromUserId === currentUserId) ||
        (msg.senderId !== undefined && msg.senderId === currentUserId);
    const senderName = isSentByMe ? "" : currentChatUserName + ":";
    const messageElement = document.querySelector(".chat-body .chat-msg[data-message-id='" + msg.messageId + "']");
    if (messageElement !== null) {
        messageElement.querySelector(".chat-actions").remove();

        const imgElement = messageElement.querySelector(".chat-msg-content img");
        if (imgElement !== null) {
            const p = document.createElement("p");
            p.className = "msg-recalled";
            p.innerHTML = `<b>${senderName}</b>Message has been recalled`;
            imgElement.replaceWith(p);
        } else {
            const pElement = messageElement.querySelector("p");
            pElement.className = "msg-recalled";
            pElement.innerHTML = `<b>${senderName}</b>Message has been recalled`;
        }
    } else {
        console.error("Message element not found for messageId:", msg.messageId);
    }
}

function sendMessages() {
    const input = document.getElementById("messageInput");
    const content = input.value.trim();

    if (!currentChatUserId)
        return alert("Please select a user to chat with");

    if (!content)
        return alert("Message cannot be empty");

    const msg = {
        type: "message",
        fromUserId: currentUserId,
        fromUsername: currentUsername,
        toUserId: currentChatUserId,
        content: content,
        sentAt: new Date()
    };

    ws.send(JSON.stringify(msg));
    input.value = "";
}

function handleKeyPress(event) {
    if (event.key === "Enter") {
        sendMessages();
    }
}

function loadChatHistory(userId) {
    fetch("/ReLoop/GetChatHistory?user1=" + currentUserId + "&user2=" + userId)
        .then(response => {
            if (!response.ok) {
                response.text().then(text => console.error("Error body:", text));
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const chatBox = document.getElementById("chatBox");
            chatBox.innerHTML = "";

            data.messages.forEach(addMessageToChatBox);
            if (data.product && userId !== supporterID) {
//                document.getElementById("linktoproduct").href ="s_productDetail?productId=" + data.product.productId;
                document.getElementById("chat-header-product").style.display = "flex";
                document.getElementById("chat-product-image").src = data.product.images[0].imageUrl;
                document.getElementById("chat-product-title").textContent = data.product.title;
                document.getElementById("chat-product-price").textContent = data.product.price + "₫";
                document.querySelector("#chat-header-product a").href = "s_productDetail?productId=" + data.product.productId;
            } else {
                document.getElementById("chat-header-product").style.display = "none";
            }
        })
        .catch(err => {
            console.error("Failed to load chat history", err);
            document.getElementById("chatBox").innerHTML = "<p style='color: red;'>Failed to load chat history.</p>";
        });
}

if ("Notification" in window && Notification.permission !== "granted") {
    Notification.requestPermission().then(permission => {
        if (permission !== "granted") {
            console.log("Notification permission denied");
        }
    });
}

function showBrowserNotification(username, content) {
    if (Notification.permission === "granted") {
        const notification = new Notification("New message from " + username, {
            icon: "https://uxwing.com/wp-content/themes/uxwing/download/communication-chat-call/new-message-icon.png"
        });

        notification.onclick = () => {
            window.focus();
        };
    }
}

function markUserAsUnread(userId) {
    const li = [...document.querySelectorAll("#userList li")].find(li => li.dataset.userId === userId);
    if (li && !li.classList.contains("user-unread")) {
        li.classList.add("user-unread");
    }
}

function markMessagesAsRead(fromUserId) {
    const message = {
        type: "read",
        fromUserId: fromUserId
    };
    ws.send(JSON.stringify(message));
    clearUnreadMark(fromUserId);
}

function clearUnreadMark(userId) {
    const li = [...document.querySelectorAll("#userList li")].find(li => li.dataset.userId === userId.toString());
    if (li) {
        li.classList.remove("user-unread");
    }
}

function blockUser() {
    if (!currentChatUserId || !ws || ws.readyState !== ws.OPEN)
        return;

    const message = {
        type: "block",
        fromUserId: currentUserId,
        blockedId: currentChatUserId
    };

    ws.send(JSON.stringify(message));
    document.getElementById("blockBtn").style.display = "none";
    document.getElementById("unblockBtn").style.display = "inline-block";
    document.getElementById("messageInput").style.display = "none";
    document.querySelector(".inputBox button").style.display = "none";
    document.getElementById("blockNotice").style.display = "block";
    document.getElementById("blockNotice").textContent = "You have blocked this user";
    document.getElementById("imageUploadLabel").style.display = "none";
    document.getElementById("imageUpload").style.display = "none";
    document.getElementById("emojiBtn").style.display = "none";
}

function unblockUser() {
    if (!currentChatUserId || !ws || ws.readyState !== ws.OPEN)
        return;

    const message = {
        type: "unblock",
        fromUserId: currentUserId,
        blockedId: currentChatUserId
    };

    ws.send(JSON.stringify(message));
    document.getElementById("blockBtn").style.display = "inline-block";
    document.getElementById("unblockBtn").style.display = "none";
    document.getElementById("messageInput").style.display = "block";
    document.querySelector(".inputBox button").style.display = "inline-block";
    document.getElementById("blockNotice").style.display = "none";
    document.getElementById("imageUploadLabel").style.display = "inline-block";
    document.getElementById("imageUpload").style.display = "none";
    document.getElementById("emojiBtn").style.display = "inline-block";
}

function recallMessage(meId) {
    let messageId = meId;

    if (!currentChatUserId) {
        alert("Please select a user to chat with");
        return;
    }
    if (!messageId) {
        alert("Message ID error");
        return;
    }
    if (confirm("Are you sure you want to recall this message?")) {
        const recallMsg = {
            type: "recall",
            messageId: messageId,
            fromUserId: currentUserId,
            toUserId: currentChatUserId
        };
        ws.send(JSON.stringify(recallMsg));
    }
}

async function sendImage() {
    const files = document.getElementById("imageUpload").files;
    if (files.length === 0)
        return;

    const formData = new FormData();
    for (const f of files)
        formData.append("file", f);

    try {
        const res = await fetch("/ReLoop/api/files", { method: "POST", body: formData });
        if (!res.ok) {
            const errTxt = await res.text();
            throw new Error(`HTTP ${res.status}: ${errTxt}`);
        }

        const { uploaded } = await res.json();
        if (!(Array.isArray(uploaded) && uploaded.length)) {
            console.warn("No image was returned", uploaded);
            return;
        }

        uploaded.forEach(img =>
            ws.send(JSON.stringify({
                type: "image",
                fromUserId: currentUserId,
                toUserId: currentChatUserId,
                imageUrl: img.shareLink,
                fileType: img.contentType
            }))
        );
    } catch (err) {
        console.error("Upload failed", err);
    }
}

function searchUsers() {
    const searchInput = document.getElementById("searchUser").value.toLowerCase();
    const userList = document.getElementById("userList");
    const users = userList.getElementsByTagName("li");

    for (let i = 0; i < users.length; i++) {
        const username = users[i].textContent.toLowerCase();
        users[i].style.display = username.includes(searchInput) ? "" : "none";
    }
}

document.getElementById("searchUser").addEventListener("input", searchUsers);

document.addEventListener("click", function (event) {
    const dropdowns = document.getElementsByClassName("dropdown-content");
    for (let i = 0; i < dropdowns.length; i++) {
        const openDropdown = dropdowns[i];
        if (openDropdown.style.display === "block" && !event.target.closest(".dropdown")) {
            openDropdown.style.display = "none";
        }
    }
});

function copyMessage(content) {
    navigator.clipboard.writeText(content)
        .then(() => {
            // copied
        })
        .catch(err => {
            console.error("Failed to copy message:", err);
        });
}

function getFileExtension(url) {
    if (!url || typeof url !== 'string') return '';
    const lastDotIndex = url.lastIndexOf('.');
    if (lastDotIndex === -1 || lastDotIndex === url.length - 1) return '';
    return url.substring(lastDotIndex + 1);
}
document.getElementById("chat-header-product").addEventListener("click", function () {
    const link = document.getElementById("linktoproduct").getAttribute("href");
    if (link && link !== "#") {
        window.location.href = link;
    }
});
