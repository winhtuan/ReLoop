
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
        const realQuantity = document.querySelector(`input[name="qtyr_${productId}"]`).value;
        if (newQty > realQuantity)
        {
            alert("The product is out of stock. Current inventory quantity is: " + realQuantity);
            return;
        }
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
            const priceText = priceRaw.toString().substring(0,priceRaw.toLocaleString().indexOf(',')).replace(/\./g, '').replace(/[^0-9]/g, '');
            const price = parseInt(priceText);
            console.log("priceRaw"+priceRaw);
            console.log("priceText"+priceText);
            console.log("price"+price);
            console.log("qty"+qty);
            if (!isNaN(qty) && !isNaN(price)) {
                subtotal += qty * price;
                count++;
            }
        } else {
            console.log(`Item ${index + 1} KHÔNG được chọn`);
        }
    });
console.log(subtotal);
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
    setupSellerSelectAllLogic();
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
                    const sellerBlock = card.closest('.seller-block');
                    card.remove();
                    const remainingCards = sellerBlock.querySelectorAll('.cart-card');
                    if (remainingCards.length === 0) {
                        sellerBlock.remove();
                    }
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
        updates.push({productId, quantity: qty});
    });

    try {
        const data = JSON.stringify(updates);
        const success = navigator.sendBeacon(cartUpdateURL, new Blob([data], {
            type: "application/json"
        }));

        if (success) {
            console.log("Cart updated via beacon");
            updatedCart.clear();
        } else {
            console.warn("Beacon failed");
        }
    } catch (err) {
        console.error("sendBeacon error:", err);
    }
}


// Gửi khi user rời trang
window.addEventListener("beforeunload", function () {
    sendCartUpdatesIfNeeded();
});
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
//setupLinkAndFormListeners();

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
function setupSellerSelectAllLogic() {
    const sellerCheckboxes = document.querySelectorAll('.seller-select-all');

    sellerCheckboxes.forEach(sellerCheckbox => {
        const sellerIndex = sellerCheckbox.dataset.sellerIndex;

        // Khi click vào checkbox "Chọn tất cả của người bán"
        sellerCheckbox.addEventListener('change', function () {
            const itemCheckboxes = document.querySelectorAll(`.cart-card[data-seller-index="${sellerIndex}"] .item-checkbox`);
            itemCheckboxes.forEach(cb => {
                cb.checked = this.checked;
            });
            updateSidebarTotal();
        });
    });

    // Khi từng item được chọn/deselect => cập nhật trạng thái của seller checkbox
    const allItemCheckboxes = document.querySelectorAll('.item-checkbox');
    allItemCheckboxes.forEach(itemCb => {
        itemCb.addEventListener('change', function () {
            const card = itemCb.closest('.cart-card');
            const sellerIndex = card.dataset.sellerIndex;
            const sellerCb = document.querySelector(`.seller-select-all[data-seller-index="${sellerIndex}"]`);

            const items = document.querySelectorAll(`.cart-card[data-seller-index="${sellerIndex}"] .item-checkbox`);
            const allChecked = Array.from(items).every(cb => cb.checked);

            if (sellerCb) {
                sellerCb.checked = allChecked;
            }

            updateSidebarTotal();
        });
    });
}
