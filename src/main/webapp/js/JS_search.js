function searchCar() {
    let query = document.getElementById("search").value.trim();
    let resultContainer = document.getElementById("productResults");

    if (query === "") {
        resultContainer.innerHTML = "";
        return;
    }
    fetch(`s_search?query=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(data => {
                console.log(data);
                resultContainer.innerHTML = "";

                if (data.length === 0) {
                    resultContainer.innerHTML = "<p>Can not find any product with your keyword.</p>";
                } else {
                    data.forEach(product => {
                        let imageUrl = "default.jpg";
                        if (product.images && product.images.length > 0 && product.images[0] != null) {
                            imageUrl = product.images[0].imageUrl;
                        }
                        resultContainer.innerHTML += `
                        <div class="product-item">
                            <img src="images/${imageUrl}" alt="${product.title}" style="width: 100%; height:100px; object-fit: cover; margin-left: 3%;">
                            <h3>${product.title}</h3>
                            <p><b>Price:</b> ${parseFloat(product.price).toLocaleString()}$</p>
                            <div>${generateBookNowButton(product)}</div>
                        </div>
                    `;
                    });
                }
            })
            .catch(error => console.error("Lỗi tìm kiếm:", error));
}

function generateBookNowButton(product) {
    return `
        <div class="amado-btn-group mt-15">
            <form action="productDetail" method="GET">
                <input type="hidden" name="productId" value="${product.id}">
                <button class="amado-btn-custom" id="nutmore" type="submit">
                    <ion-icon name="arrow-forward-outline" class="btn-icon"></ion-icon>Xem thêm
                </button>
            </form>
        </div>
    `;
}
