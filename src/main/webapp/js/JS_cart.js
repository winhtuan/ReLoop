
const updatedCart = new Map(); // Lưu các thay đổi quantity

// Cập nhật số lượng khi bấm nút + hoặc -
document.addEventListener('click', function (e) {
    if (e.target.classList.contains('qty-btn')) {
        const btn = e.target;
        const productId = btn.dataset.id;
        const delta = parseInt(btn.dataset.change);
        const qtyInput = document.getElementById('q-' + productId);
        const hidden = document.querySelector(`input[name="qty_${productId}"]`);
        let newQty = Math.max(1, parseInt(qtyInput.value) + delta);
        qtyInput.value = newQty;
        hidden.value = newQty;
        updatedCart.set(productId, newQty);
        updateSidebarTotal();
    }
});

function updateSidebarTotal() {
    let subtotal = 0;
    let count = 0;

    document.querySelectorAll('.cart-card').forEach((card, index) => {
        const checkbox = card.querySelector('.item-checkbox');
        const qtyInput = card.querySelector('.cart-qty-input');
        const priceEl = card.querySelector('.cart-card-price');

        if (checkbox && checkbox.checked) {
            const qty = parseInt(qtyInput.value);
            const priceRaw = priceEl.innerText;
            const priceText = priceRaw.replace(/\./g, '').replace(/[^0-9]/g, '');
            const price = parseInt(priceText);

            if (!isNaN(qty) && !isNaN(price)) {
                subtotal += qty * price;
                count++;
            }
        } else {
            console.log(`Item ${index + 1} KHÔNG được chọn`);
        }
    });

    document.querySelectorAll('.cart-sidebar-total-value').forEach(el => {
        el.innerText = subtotal.toLocaleString('en-US');
    });
    document.querySelectorAll('.cart-sidebar-count').forEach(el => {
        el.innerText = count;
    });
}


// Checkbox logic: chọn tất cả, chọn từng sản phẩm
function setupCheckboxLogic() {
    const selectAll = document.getElementById('sidebar-select-all');
    const itemCheckboxes = document.querySelectorAll('.item-checkbox');

    if (selectAll) {
        selectAll.addEventListener('change', function () {
            itemCheckboxes.forEach(cb => cb.checked = this.checked);
            updateSidebarTotal();
        });
    }
    itemCheckboxes.forEach(cb => {
        cb.addEventListener('change', function () {
            if (selectAll) {
                selectAll.checked = Array.from(itemCheckboxes).every(c => c.checked);
            }
            updateSidebarTotal();
        });
    });
}

document.addEventListener('DOMContentLoaded', function () {
    setupCheckboxLogic();
    updateSidebarTotal();
});

// Xóa sản phẩm khỏi giỏ
function removeFromCart(productId, btn) {
    if (!confirm("Remove this item from your cart?"))
        return;
    fetch(`${contextPath}/s_cart?productId=${productId}`, {
        method: "DELETE"
    })
        .then(response => {
            if (response.ok) {
                const card = btn.closest('.cart-card');
                card.remove();
                updateSidebarTotal();
            } else {
                alert("Failed to remove item.");
            }
        });
}

// Gửi cập nhật số lượng lên server nếu có thay đổi
const cartUpdateURL = `${contextPath}/s_cart`;
function sendCartUpdatesIfNeeded() {
    if (updatedCart.size === 0)
        return;
    const updates = [];
    updatedCart.forEach((qty, productId) => {
        updates.push({ productId, quantity: qty });
    });
    navigator.sendBeacon(
        cartUpdateURL,
        new Blob([JSON.stringify(updates)], { type: "application/json" })
    );
    updatedCart.clear();
}

// Gửi khi user rời trang
window.addEventListener("beforeunload", function () {
    sendCartUpdatesIfNeeded();
});
// Gửi khi click link hoặc submit form
// Đảm bảo chỉ gửi khi thực sự chuyển trang hoặc submit
function setupLinkAndFormListeners() {
    document.querySelectorAll("a").forEach(el => {
        el.addEventListener("click", () => {
            sendCartUpdatesIfNeeded();
        });
    });
    document.querySelectorAll("form").forEach(form => {
        form.addEventListener("submit", () => {
            sendCartUpdatesIfNeeded();
        });
    });
}
setupLinkAndFormListeners();

// Khi submit form chỉ gửi các sản phẩm được chọn
const cartForm = document.getElementById('cartForm');
if (cartForm) {
    cartForm.addEventListener('submit', function (e) {
        // Xóa input của các sản phẩm không được chọn
        document.querySelectorAll('.cart-card').forEach(card => {
            const checkbox = card.querySelector('.item-checkbox');
            if (checkbox && !checkbox.checked) {
                // Xóa input khỏi form để không gửi lên server
                card.querySelectorAll('input').forEach(input => {
                    input.disabled = true;
                });
            }
        });
    });
}
