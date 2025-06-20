
const updatedCart = new Map(); // Lưu các thay đổi quantity

function updateQty(btn, delta) {
    const productId = btn.dataset.id;
    const qtySpan   = document.getElementById("q-" + productId);
    const newQty    = Math.max(1, parseInt(qtySpan.innerText) + delta);

    qtySpan.innerText = newQty;
    updatedCart.set(productId, newQty);

    updateSubtotal();
}


function updateSubtotal() {
    let subtotal = 0;

    document.querySelectorAll(".item").forEach(item => {
        const checkbox = item.querySelector(".item-checkbox");
        if (!checkbox || !checkbox.checked) return; // chỉ tính nếu được chọn

        const qty = parseInt(item.querySelector(".qty span").innerText);
        const price = parseFloat(item.querySelector(".price").dataset.price);

        if (!isNaN(qty) && !isNaN(price)) {
            subtotal += qty * price;
        }
    });

    const formatted = `$${subtotal.toFixed(2)}`;
    document.querySelectorAll(".line span:nth-child(2)").forEach(el => {
        el.innerText = formatted;
    });
}

document.querySelectorAll(".item-checkbox").forEach(cb => {
    cb.addEventListener("change", updateSubtotal);
});

window.addEventListener("beforeunload", function (e) {
        if (updatedCart.size === 0) return;

        const updates = [];
        updatedCart.forEach((qty, productId) => {
            updates.push({ productId, quantity: qty });
        });

        navigator.sendBeacon(
            `${contextPath}/s_cart`,
            new Blob([JSON.stringify(updates)], { type: "application/json" })
        );
    });

function removeFromCart(productId, btn) {
    console.log("asdasd"+productId);
    if (!confirm("Remove this item from your cart?")) return;

    fetch(`${contextPath}/s_cart?productId=${productId}`, {
        method: "DELETE"
    })
    .then(response => {
        if (response.ok) {
            // Xoá phần tử khỏi giao diện
            const item = btn.closest(".item");
            item.remove();
            updateSubtotal();
        } else {
            alert("Failed to remove item.");
        }
    });
}
