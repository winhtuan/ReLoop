let allProducts = [];
let productsPerLoad = 5;
let currentIndex = 0;

function searchItem() {
    let query = document.getElementById("search").value.trim();
    let resultContainer = document.getElementById("productResults");

    if (query === "") {
        resultContainer.innerHTML = "";
        resultContainer.style.display = "none";
        allProducts = [];
        currentIndex = 0;
        return;
    }

    fetch(`s_search?query=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(data => {
                allProducts = data;
                currentIndex = 0;
                resultContainer.innerHTML = "";

                if (allProducts.length === 0) {
                    resultContainer.innerHTML = "<p>No products found.</p>";
                } else {
                    resultContainer.style.display = "block";
                    loadMoreProducts();
                }
            })
            .catch(error => console.error("Search error:", error));
}


function loadMoreProducts() {
    let resultContainer = document.getElementById("productResults");
    let nextBatch = allProducts.slice(currentIndex, currentIndex + productsPerLoad);

    nextBatch.forEach(product => {
        const item = document.createElement("div");
        item.className = "search-item";

        item.innerHTML = `
            <img src="img/product-img/pro-big-1.jpg" alt="Product" class="img-thumbnail">
            <div class="search-left-content">
                <div class="fav-tittle">
                    <span><strong>${product.title}</strong></span>
                    <p><b>Price:</b> ${parseFloat(product.price).toLocaleString()}$</p>
                </div>
            </div>
            <div class="search-actions">
                <ion-icon name="arrow-forward-outline" class="btn-icon"></ion-icon>View More
            </div>
        `;

        resultContainer.appendChild(item);
    });

    currentIndex += productsPerLoad;
}

document.addEventListener("DOMContentLoaded", function() {
    document.querySelector(".clear-btn").addEventListener("click", function () {
        const input = document.getElementById("search");
        const resultContainer = document.getElementById("productResults");
        input.value = "";
        resultContainer.innerHTML = "";
        resultContainer.style.display = "none";
    });
});



