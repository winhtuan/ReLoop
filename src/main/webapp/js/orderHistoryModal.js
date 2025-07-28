document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('orderModal');
    const modalClose = document.querySelector('.modal-close');
    const orderCards = document.querySelectorAll('.order-card');
    const modalOrderId = document.getElementById('modalOrderId');
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));

    // Open modal when clicking the action button
    orderCards.forEach(card => {
        const actionBtn = card.querySelector('.order-action-btn');
        if (actionBtn) {
            actionBtn.addEventListener('click', function () {
                const orderId = this.getAttribute('data-order-id');
                modalOrderId.textContent = orderId;
                modal.style.display = 'block';
                // Ẩn/hiện nút dựa trên trạng thái (giả định trạng thái được lấy từ server hoặc DOM)
                const status = card.querySelector('.info-row:last-child').textContent.split(': ')[1]; // Lấy status từ DOM
                const cancelBtn = document.querySelector('.cancel-order');
                const returnBtn = document.querySelector('.return-order');
                const receivedBtn = document.querySelector('.received-order');
                if (status === 'pending') {
                    cancelBtn.style.display = 'block';
                    returnBtn.style.display = 'none';
                    receivedBtn.style.display = 'none';
                } else if (status === 'paid') {
                    cancelBtn.style.display = 'none';
                    returnBtn.style.display = 'none';
                    receivedBtn.style.display = 'block';
                } else if (status === 'shipping' || status === 'delivered') {
                    cancelBtn.style.display = 'none';
                    returnBtn.style.display = 'none';
                    receivedBtn.style.display = 'block';
                } else {
                    cancelBtn.style.display = 'none';
                    returnBtn.style.display = 'none';
                    receivedBtn.style.display = 'none';
                }
            });
        }
    });

    // Close modal when clicking the close button
    modalClose.addEventListener('click', function () {
        modal.style.display = 'none';
    });

    // Close modal when clicking outside
    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    // Handle modal buttons with AJAX
    const cancelBtn = document.querySelector('.cancel-order');
    const returnBtn = document.querySelector('.return-order');
    const receivedBtn = document.querySelector('.received-order');

    cancelBtn.addEventListener('click', function () {
        const orderId = modalOrderId.textContent;
        if (confirm(`Are you sure you want to cancel order ${orderId}?`)) {
            console.log('Sending cancel request for order:', orderId);
            $.ajax({
                url: contextPath + '/updateOrder',
                type: 'POST',
                data: { action: 'cancel', orderId: orderId },
                dataType: 'json',
                success: function (response) {
                    console.log('Cancel response:', JSON.stringify(response));
                    alert(response.message);
                    if (response.success || response.message.includes("has been cancelled")) {
                        modal.style.display = 'none';
                        location.reload();
                    }
                },
                error: function (xhr, status, error) {
                    console.log('Cancel error:', { status, error, response: xhr.responseText });
                    alert('Failed to cancel order ' + orderId + '. Status: ' + status + ', Error: ' + error + ', Response: ' + xhr.responseText);
                }
            });
        }
    });

    returnBtn.addEventListener('click', function () {
        const orderId = modalOrderId.textContent;
        if (confirm(`Are you sure you want to return order ${orderId}?`)) {
            console.log('Sending return request for order:', orderId);
            $.ajax({
                url: contextPath + '/updateOrder',
                type: 'POST',
                data: { action: 'return', orderId: orderId },
                dataType: 'json',
                success: function (response) {
                    console.log('Return response:', JSON.stringify(response));
                    alert(response.message);
                    if (response.success || response.message.includes("has been returned")) {
                        modal.style.display = 'none';
                        location.reload();
                    }
                },
                error: function (xhr, status, error) {
                    console.log('Return error:', { status, error, response: xhr.responseText });
                    alert('An error occurred while processing return for order ' + orderId + '. Status: ' + status + ', Error: ' + error + ', Response: ' + xhr.responseText);
                }
            });
        }
    });

    receivedBtn.addEventListener('click', function () {
        const orderId = modalOrderId.textContent;
        if (this.dataset.clicked) return; // Ngăn lặp
        this.dataset.clicked = "true";
        console.log('Sending received request for order:', orderId);
        $.ajax({
            url: contextPath + '/updateOrder',
            type: 'POST',
            data: { action: 'received', orderId: orderId },
            dataType: 'json',
            success: function (response) {
                console.log('Received response:', JSON.stringify(response));
                alert(response.message);
                if (response.success || response.message.includes("has been marked as received")) {
                    modal.style.display = 'none';
                    location.reload(); // Tải lại trang
                }
                delete receivedBtn.dataset.clicked; // Reset sau khi hoàn tất
            },
            error: function (xhr, status, error) {
                console.log('Received error:', { status, error, response: xhr.responseText });
                alert('Failed to confirm receipt for order ' + orderId + '. Status: ' + status + ', Error: ' + error + ', Response: ' + xhr.responseText);
                delete receivedBtn.dataset.clicked; // Reset nếu lỗi
            }
        });
    });
});