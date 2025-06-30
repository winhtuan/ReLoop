// Mỗi trang đều kết nối WebSocket này
const wsNotify = new WebSocket("ws://" + location.host + "/ReLoop/notificate");
wsNotify.onmessage = function (event) {
    var data = JSON.parse(event.data);
    if (data.type === "notification") {
        showToast((data.title ? (data.title + ": ") : "") + data.content);
    }
};

document.addEventListener("DOMContentLoaded", function () {
    const notiLink = document.getElementById("notificationLink");
    const notiBox = document.getElementById("notificationBox");

    if (notiLink) {
        notiLink.addEventListener("click", function (e) {
            e.preventDefault();
            fetchNotifications();
            notiBox.classList.toggle("show");
        });
    }

    function fetchNotifications() {
        fetch(contextPath + "/notification")
                .then(response => {
                    if (!response.ok)
                        throw new Error("Unauthorized or Server Error");
                    return response.json();
                })
                .then(data => {
                    renderNotifications(data);
                })
                .catch(err => {
                    document.getElementById("notificationBox").innerHTML =
                            `<p style="text-align: center; font-size: 17px;">No New <strong>Notifications</strong></p>`;
                });
    }

    function renderNotifications(data) {
        if (data.length === 0) {
            notiBox.innerHTML = `<p style="text-align: center; font-size: 17px;">No New <strong>Notifications</strong></p>`;
            return;
        }
        let html = "";
        data.forEach(n => {
            html += `
                <div class="notification-item${n.read ? "" : " new"}">
                    <div class="notification-text">
                        <p>${n.content}</p>
                        <small>${new Date(n.createdAt).toLocaleString()}</small>
                    </div>
                </div>
            `;
        });
        html += `<div id="seeMoreNotifications" style="text-align: center; margin-top: 5px;">
                    <a href="#" style="font-size: 14px; color: #fbb710; cursor: pointer;">Xem thêm</a>
                </div>`;
        notiBox.innerHTML = html;
    }
});

function showToast(message) {
    var toast = document.getElementById('toastNotification');
    var toastMsg = document.getElementById('toastMessage');
    toastMsg.innerHTML = message;
    toast.classList.add('show');
    // Tự động ẩn sau 5s
    setTimeout(function () {
        hideToast();
    }, 5000);
}
function hideToast() {
    var toast = document.getElementById('toastNotification');
    toast.classList.remove('show');
}
