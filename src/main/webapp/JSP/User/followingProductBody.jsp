<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Owner Product Dashboard</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
            .animate-fadeIn {
                animation: fadeIn 0.3s ease-out forwards;
            }
            ::-webkit-scrollbar {
                width: 8px;
            }
            ::-webkit-scrollbar-thumb {
                background-color: rgba(234, 179, 8, 0.7);
                border-radius: 8px;
            }
            ::-webkit-scrollbar-track {
                background-color: rgba(254, 252, 232, 0.5);
            }
            .tab-active {
                border-bottom: 3px solid #facc15;
                color: #facc15;
                font-weight: 600;
            }
            .gradient-bg {
                background: linear-gradient(135deg, #fef9c3 0%, #fff 100%);
            }
            .product-card {
                transition: transform 0.3s ease;
            }
            .product-card:hover {
                cursor: pointer;
                transform: scale(1.02);
            }
            .hover-overlay {
                position: absolute;
                inset: 0;
                background-color: rgba(0, 0, 0, 0.4);
                color: white;
                display: flex;
                justify-content: center;
                align-items: center;
                opacity: 0;
                transition: opacity 0.3s ease;
                font-weight: bold;
                font-size: 1rem;
            }
            .product-card:hover .hover-overlay {
                opacity: 1;
            }
            .modal-buy-form button {
                display: flex;
                align-items: center;
                gap: 0.5rem;
                background-color: #facc15;
                color: white;
                padding: 0.5rem 1.5rem;
                border-radius: 0.5rem;
                font-weight: 500;
                transition: background-color 0.3s ease;
            }
            .modal-buy-form button:hover {
                background-color: #eab308;
            }
            .modal-close-button {
                display: flex;
                align-items: center;
                gap: 0.5rem;
                background-color: #fff;
                color: #f59e0b;
                border: 1px solid #fcd34d;
                padding: 0.5rem 1.5rem;
                border-radius: 0.5rem;
                font-weight: 500;
                transition: all 0.3s ease;
            }
            .modal-close-button:hover {
                background-color: #fef3c7;
                color: #d97706;
                border-color: #fbbf24;
            }
        </style>
    </head>
    <body class="gradient-bg min-h-screen text-yellow-900">
        <div class="container mx-auto px-4 py-8">
            <header class="mb-8">
                <h1 class="text-4xl font-bold text-yellow-800 mb-2">${user.fullName}'s Products</h1>
                <p class="text-yellow-700 opacity-80">Manage and view your products</p>
            </header>

            <div class="bg-white rounded-xl shadow-md p-6 mb-8">
                <div class="flex flex-col md:flex-row items-center gap-6">
                    <div class="relative">
                        <img id="avatar-preview" 
                             src="${empty user.srcImg ? 'images/default-avatar.png' : user.srcImg}" 
                             alt="Avatar"
                             class="w-24 h-24 md:w-32 md:h-32 rounded-full object-cover border-4 border-yellow-100 shadow">
                    </div>
                    <div class="flex-1 md:text-left">
                        <h2 id="fullname-display" class="text-2xl font-bold text-yellow-900 mb-2">${user.fullName}</h2>
                        <div class="flex space-x-6">
                            <div class="text-center">
                                <div class="text-yellow-600 text-sm">Followers</div>
                                <div class="font-bold text-yellow-700 text-xl">${follower}</div>
                            </div>
                            <div class="text-center">
                                <div class="text-yellow-600 text-sm">Product</div>
                                <div class="font-bold text-yellow-700 text-xl">${fn:length(followingProducts)}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="flex border-b border-yellow-200 mb-6">
                <button class="tab-btn tab-active px-6 py-3 text-yellow-800">Products</button>
            </div>

            <section id="section-products" class="animate-fadeIn">
                <div class="flex justify-between items-center mb-6">
                    <div class="relative w-full md:w-96">
                        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <i class="fas fa-search text-yellow-500"></i>
                        </div>
                        <input type="text" id="searchInput" placeholder="Search products..."
                               class="w-full pl-10 pr-4 py-3 border border-yellow-300 rounded-xl bg-white text-yellow-900 shadow-sm focus:ring-yellow-400 focus:border-yellow-400">
                    </div>
                </div>

                <div id="product-container" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6"></div>
                <div id="pagination" class="flex justify-center mt-8"></div>
            </section>
        </div>

        <div id="product-detail-modal" class="fixed inset-0 bg-black bg-opacity-40 hidden z-50 flex items-center justify-center p-4 transition-opacity duration-300">
            <div class="bg-white rounded-2xl p-6 w-full max-w-2xl relative overflow-y-auto max-h-[90vh] shadow-2xl animate-fadeIn">
                <button onclick="closeProductDetail()" class="absolute top-4 right-4 text-yellow-500 hover:text-yellow-700 text-xl">
                    <i class="fas fa-times"></i>
                </button>
                <div id="product-detail-content"></div>
            </div>
        </div>

        <template id="product-card-template">
            <div class="product-card bg-white border border-yellow-200 rounded-xl overflow-hidden shadow hover:shadow-lg transition-all duration-300 relative">
                <div class="relative">
                    <img class="product-img w-full h-48 object-cover" src="" alt="">
                    <div class="hover-overlay rounded-xl">View Details</div>
                    <div class="absolute top-2 right-2 bg-yellow-400 text-white text-xs font-bold px-2 py-1 rounded">
                        <span class="product-price"></span>
                    </div>
                </div>
                <div class="p-4">
                    <h3 class="product-title text-lg font-semibold text-yellow-900 mb-1 line-clamp-2"></h3>
                    <p class="product-location text-sm text-yellow-700"></p>
                </div>
            </div>
        </template>

        <template id="product-detail-template">
            <div class="space-y-6">
                <h2 class="modal-title text-2xl font-bold text-yellow-900"></h2>
                <img class="modal-img w-full h-64 object-cover rounded shadow" src="" alt="">
                <p class="modal-price text-yellow-800 font-semibold"></p>
                <p class="modal-desc text-yellow-700 font-semibold"></p>
                <p class="modal-location text-yellow-700 font-semibold"></p>
                <p class="modal-qty text-yellow-700 font-semibold"></p>
                <div class="flex justify-end gap-4 mt-6">
                    <button onclick="closeProductDetail()" type="button" class="modal-close-button">
                        <i class="fas fa-times"></i> Close
                    </button>
                    <form action="s_productDetail" method="post" class="modal-buy-form">
                        <input type="hidden" name="productId">
                        <button type="submit">
                            <i class="fas fa-shopping-cart"></i> Buy Now
                        </button>
                    </form>
                </div>
            </div>
        </template>

        <div id="product-data" data-json='${fn:escapeXml(followingProductsJson)}'></div>
        <script>
            document.addEventListener("DOMContentLoaded", () => {
                const rawJson = document.getElementById("product-data").dataset.json;
                const allProducts = JSON.parse(rawJson);
                let currentPage = 1;
                const itemsPerPage = 8;

                const searchInput = document.getElementById("searchInput");
                const container = document.getElementById("product-container");
                const pagination = document.getElementById("pagination");
                const productTemplate = document.getElementById("product-card-template");
                const detailTemplate = document.getElementById("product-detail-template");

                function renderProducts(products) {
                    const start = (currentPage - 1) * itemsPerPage;
                    const end = start + itemsPerPage;
                    const paginatedItems = products.slice(start, end);
                    container.innerHTML = "";

                    paginatedItems.forEach((product) => {
                        const clone = productTemplate.content.cloneNode(true);
                        const imgUrl = (product.images?.[0]?.imageUrl) ? product.images[0].imageUrl : "default.png";

                        const card = clone.querySelector(".product-card");
                        card.querySelector(".product-img").src = imgUrl;
                        card.querySelector(".product-title").textContent = product.title;
                        card.querySelector(".product-price").textContent = product.price + " $";
                        card.querySelector(".product-location").textContent = product.location;
                        card.addEventListener("click", () => showProductDetail(product.productId));

                        container.appendChild(clone);
                    });

                    renderPagination(products.length);
                }

                function renderPagination(totalItems) {
                    const pageCount = Math.ceil(totalItems / itemsPerPage);
                    pagination.innerHTML = "";

                    for (let i = 1; i <= pageCount; i++) {
                        const btn = document.createElement("button");
                        btn.textContent = i;
                        btn.className = `mx-1 px-3 py-1 rounded-full text-sm font-medium transition ${i == currentPage ? 'bg-yellow-500 text-white' : 'bg-white border border-yellow-300 text-yellow-700 hover:bg-yellow-50'}`;
                        btn.onclick = () => {
                            currentPage = i;
                            updateView();
                        };
                        pagination.appendChild(btn);
                    }
                }

                function updateView() {
                    const query = searchInput.value.toLowerCase();
                    const filtered = allProducts.filter(p => p.title.toLowerCase().includes(query));
                    renderProducts(filtered);
                }

                window.showProductDetail = function (productId) {
                    const product = allProducts.find(p => p.productId === productId);
                    if (!product)
                        return;

                    const imgUrl = product.images?.[0]?.imageUrl || "default.png";

                    const clone = detailTemplate.content.cloneNode(true);
                    clone.querySelector(".modal-title").textContent = product.title;
                    clone.querySelector(".modal-img").src = imgUrl;
                    clone.querySelector(".modal-price").textContent = "Price: " + product.price + " $";
                    clone.querySelector(".modal-desc").textContent = "Description: " + product.description;
                    clone.querySelector(".modal-location").textContent = "Location: " + product.location;
                    clone.querySelector(".modal-qty").textContent = "Quantity: " + product.quantity;
                    clone.querySelector(".modal-buy-form input[name='productId']").value = product.productId;

                    const detailContent = document.getElementById("product-detail-content");
                    detailContent.innerHTML = "";
                    detailContent.appendChild(clone);

                    document.getElementById("product-detail-modal").classList.remove("hidden");
                };

                window.closeProductDetail = function () {
                    document.getElementById("product-detail-modal").classList.add("hidden");
                };

                searchInput.addEventListener("input", () => {
                    currentPage = 1;
                    updateView();
                });

                updateView();
            });
        </script>
    </body>
</html>