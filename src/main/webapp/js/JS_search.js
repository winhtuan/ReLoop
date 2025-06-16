let allProducts = [];
let productsPerLoad = 5;
let currentIndex = 0;

function searchItem() {
    let query = document.getElementById("search").value.trim();
    let resultContainer = document.getElementById("productResults");

    // Clear previous results if query is empty
    if (query === "") {
        resultContainer.innerHTML = "";
        resultContainer.style.display = "none";
        allProducts = [];
        currentIndex = 0;
        return;
    }

    // Fetch data from server
    fetch(`s_search?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(data => {
            allProducts = data;
            currentIndex = 0;
            resultContainer.innerHTML = "";

            // Check if no products found
            if (allProducts.length === 0) {
                resultContainer.innerHTML = "<p>No products found.</p>";
            } else {
                resultContainer.style.display = "block";
                loadMoreProducts();
            }
        })
        .catch(error => {
            console.error("Search error:", error);
            alert("An error occurred while searching. Please try again later.");
        });
}

function loadMoreProducts() {
    let resultContainer = document.getElementById("productResults");
    let nextBatch = allProducts.slice(currentIndex, currentIndex + productsPerLoad);

    // Ensure that products are loaded correctly
    if (nextBatch.length > 0) {
        nextBatch.forEach(product => {
            console.log("Product Image URL:", product.imageUrl); // Ensure that product details are printed

            const item = document.createElement("div");
            item.className = "search-item";
            item.innerHTML = `
                <img src="${product.imageUrl}" alt="Product" class="img-thumbnail">
                <div class="search-left-content">
                    <div class="fav-tittle">
                        <span><strong>${product.title}</strong></span>
                        <p><b>Price:</b> ${parseFloat(product.price).toLocaleString()}$</p>
                    </div>
                </div>
                <div class="search-actions">
                    <ion-icon name="arrow-forward-outline" class="btn-icon"></ion-icon><a href="s_productDetail?productId=${product.product_id}">View More</a>
                </div>
            `;
            
            resultContainer.appendChild(item);
        });

        // Update currentIndex for next load
        currentIndex += productsPerLoad;
    } else {
        console.log("No more products to load.");
    }
}

// Clear search input and results when button is clicked
document.addEventListener("DOMContentLoaded", function() {
    document.querySelector(".clear-btn").addEventListener("click", function () {
        const input = document.getElementById("search");
        const resultContainer = document.getElementById("productResults");
        input.value = "";
        resultContainer.innerHTML = "";
        resultContainer.style.display = "none";
        allProducts = [];
        currentIndex = 0;
    });
});
