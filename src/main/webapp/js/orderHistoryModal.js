document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('orderModal');
    const modalClose = document.querySelector('.modal-close');
    const orderCards = document.querySelectorAll('.order-card');
    const modalOrderId = document.getElementById('modalOrderId');
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
    const orderGrid = document.getElementById('order-grid');
    const filterProduct = document.getElementById('filter-product');
    const filterMinAmount = document.getElementById('filter-min-amount');
    const filterMaxAmount = document.getElementById('filter-max-amount');
    const filterDate = document.getElementById('filter-date');
    const applyFilterBtn = document.getElementById('apply-filter');
    const resetFilterBtn = document.getElementById('reset-filter');
    const paginationControls = document.getElementById('pagination-controls');

    // Phân trang
    const itemsPerPage = 6; // Số đơn hàng mỗi trang
    let currentPage = 1;
    let filteredOrders = Array.from(orderCards);

    // Hàm hiển thị đơn hàng theo trang
    function displayOrders(page) {
        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        filteredOrders.forEach((card, index) => {
            card.style.display = (index >= start && index < end) ? 'block' : 'none';
        });
        updatePaginationControls();
    }

    // Hàm cập nhật các nút phân trang
    function updatePaginationControls() {
        const totalPages = Math.ceil(filteredOrders.length / itemsPerPage);
        paginationControls.innerHTML = '';

        // Nút Previous
        const prevBtn = document.createElement('button');
        prevBtn.textContent = 'Previous';
        prevBtn.disabled = currentPage === 1;
        prevBtn.addEventListener('click', () => {
            if (currentPage > 1) {
                currentPage--;
                displayOrders(currentPage);
            }
        });
        paginationControls.appendChild(prevBtn);

        // Các nút số trang
        for (let i = 1; i <= totalPages; i++) {
            const pageBtn = document.createElement('button');
            pageBtn.textContent = i;
            pageBtn.className = i === currentPage ? 'active' : '';
            pageBtn.addEventListener('click', () => {
                currentPage = i;
                displayOrders(currentPage);
            });
            paginationControls.appendChild(pageBtn);
        }

        // Nút Next
        const nextBtn = document.createElement('button');
        nextBtn.textContent = 'Next';
        nextBtn.disabled = currentPage === totalPages;
        nextBtn.addEventListener('click', () => {
            if (currentPage < totalPages) {
                currentPage++;
                displayOrders(currentPage);
            }
        });
        paginationControls.appendChild(nextBtn);
    }

    // Hàm lọc đơn hàng
    function filterOrders() {
    // 1. Ẩn toàn bộ order trước
    orderCards.forEach(card => card.style.display = 'none');

    // 2. Đọc giá trị filter
    const productQuery = filterProduct.value.toLowerCase().trim();
    const minAmount = parseFloat(filterMinAmount.value.replace(/\./g, '')) || 0;
    const maxAmount = parseFloat(filterMaxAmount.value.replace(/\./g, '')) || Infinity;
    const filterDateValue = filterDate.value ? new Date(filterDate.value) : null;

    // 3. Lọc
    filteredOrders = Array.from(orderCards).filter(card => {
        const totalAmount = parseFloat(card.dataset.totalAmount);
        const createdDate = new Date(card.dataset.createdDate);
        const productNames = card.dataset.productNames.toLowerCase();

        const matchesProduct = productQuery ? productNames.includes(productQuery) : true;
        const matchesAmount = totalAmount >= minAmount && totalAmount <= maxAmount;
        const matchesDate = filterDateValue ?
            createdDate.toDateString() === filterDateValue.toDateString() : true;

        return matchesProduct && matchesAmount && matchesDate;
    });

    // 4. Reset về trang đầu
    currentPage = 1;
    displayOrders(currentPage);
}


    // Xử lý nút Apply Filter
    applyFilterBtn.addEventListener('click', filterOrders);

    // Xử lý nút Reset Filter
    resetFilterBtn.addEventListener('click', () => {
        filterProduct.value = '';
        filterMinAmount.value = '';
        filterMaxAmount.value = '';
        filterDate.value = '';
        filteredOrders = Array.from(orderCards);
        currentPage = 1;
        displayOrders(currentPage);
    });

    // Khởi tạo hiển thị trang đầu tiên
    displayOrders(currentPage);

    // Giữ nguyên các sự kiện hiện có cho modal
    orderCards.forEach(card => {
        const actionBtn = card.querySelector('.order-action-btn');
        if (actionBtn) {
            actionBtn.addEventListener('click', function () {
                const orderId = this.getAttribute('data-order-id');
                modalOrderId.textContent = orderId;
                modal.style.display = 'block';
                const status = card.querySelector('.info-row:last-child').textContent.split(': ')[1];
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

    modalClose.addEventListener('click', function () {
        modal.style.display = 'none';
    });

    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

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
        if (this.dataset.clicked) return;
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
                    location.reload();
                }
                delete receivedBtn.dataset.clicked;
            },
            error: function (xhr, status, error) {
                console.log('Received error:', { status, error, response: xhr.responseText });
                alert('Failed to confirm receipt for order ' + orderId + '. Status: ' + status + ', Error: ' + error + ', Response: ' + xhr.responseText);
                delete receivedBtn.dataset.clicked;
            }
        });
    });
});
