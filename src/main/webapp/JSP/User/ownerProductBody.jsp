<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Owner Product Dashboard</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
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
            .modal-action-button {
                display: flex;
                align-items: center;
                gap: 0.5rem;
                font-weight: 500;
                padding: 0.5rem 1.5rem;
                border-radius: 0.5rem;
                transition: all 0.3s ease;
            }

            .modal-edit-button {
                background-color: #4ade80; /* xanh lá */
                color: white;
            }
            .modal-edit-button:hover {
                background-color: #22c55e;
            }

            .modal-delete-button {
                background-color: #f87171; /* đỏ */
                color: white;
            }
            .modal-delete-button:hover {
                background-color: #ef4444;
            }
        </style>
    </head>
    <body class="gradient-bg min-h-screen text-yellow-900">
        <c:if test="${param.msg == 'deleted'}">
            <div class="text-green-600 bg-green-100 p-2 rounded mb-4">Product deleted successfully!</div>
        </c:if>
        <c:if test="${param.msg == 'error'}">
            <div class="text-red-600 bg-red-100 p-2 rounded mb-4">Failed to delete product. Please try again.</div>
        </c:if>
        <!-- Profile Section -->
        <div class="bg-white rounded-xl shadow-md p-6 mb-8">
            <div class="flex flex-col md:flex-row items-center gap-6">
                <img src="${empty user.srcImg ? 'images/default-avatar.png' : user.srcImg}" class="w-24 h-24 md:w-32 md:h-32 rounded-full object-cover border-4 border-yellow-100 shadow" />
                <div class="flex-1 md:text-left">
                    <h2 class="text-2xl font-bold mb-2">${user.fullName}</h2>
                    <div class="flex space-x-6 mb-4">
                        <div class="text-center">
                            <div class="text-yellow-600 text-sm">Followers</div>
                            <div class="font-bold text-yellow-700 text-xl">${follower}</div>
                        </div>
                        <div class="text-center">
                            <div class="text-yellow-600 text-sm">Products</div>
                            <div class="font-bold text-yellow-700 text-xl">${fn:length(ownerProducts)}</div>
                        </div>
                    </div>
                    <a href="upPostServlet" class="inline-flex items-center gap-2 px-4 py-2 bg-yellow-400 text-white rounded hover:bg-yellow-500 font-semibold">
                        <i class="fas fa-plus"></i> Create Product
                    </a>
                </div>
            </div>
        </div>

        <!-- Product Search + List -->
        <div class="px-6">
            <div class="mb-4">
                <input id="searchInput" placeholder="Search products..." class="w-full md:w-96 px-4 py-2 border border-yellow-300 rounded shadow-sm focus:outline-none focus:ring-2 focus:ring-yellow-400" />
            </div>
            <div id="product-container" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6"></div>
            <div id="pagination" class="flex justify-center mt-6"></div>
        </div>

        <!-- Product Modal -->
        <div id="productModal" class="fixed inset-0 bg-black bg-opacity-50 hidden z-50 flex items-center justify-center p-4 transition-opacity duration-300">
            <div class="bg-white rounded-2xl p-6 w-full max-w-2xl relative overflow-y-auto max-h-[90vh] shadow-2xl animate-fadeIn">
                <button onclick="closeModal()" class="absolute top-4 right-4 text-yellow-500 hover:text-yellow-700 text-xl">
                    <i class="fas fa-times"></i>
                </button>
                <div id="product-detail-content"></div>
            </div>
        </div>

        <template id="product-detail-template">
            <div class="space-y-6">
                <h2 class="modal-title text-2xl font-bold text-yellow-900"></h2>
                <img class="modal-img w-full h-64 object-cover rounded shadow" src="" alt="">
                <div class="space-y-2">
                    <p class="modal-price text-yellow-800 font-semibold text-lg"></p>
                    <p class="modal-desc text-yellow-700 font-medium"></p>
                    <p class="modal-location text-yellow-700 font-medium"></p>
                    <p class="modal-qty text-yellow-700 font-medium"></p>
                </div>
                <div class="flex justify-end gap-3 pt-4 border-t">
                    <button onclick="closeModal()" type="button" class="modal-close-button">
                        <i class="fas fa-times"></i> Close
                    </button>
                    <form action="EditProductServlet" method="post" class="modal-buy-form">
                        <input type="hidden" name="productId">
                        <button type="submit" class="modal-action-button modal-edit-button">
                            <i class="fas fa-pen"></i> Edit
                        </button>
                    </form>
                    <form action="DeleteProductServlet" method="post" class="modal-buy-form" onsubmit="return confirm('Are you sure you want to delete this product?');">
                        <input type="hidden" name="productId">
                        <button type="submit" class="modal-action-button modal-delete-button">
                            <i class="fas fa-trash"></i> Delete
                        </button>
                    </form>
                </div>
            </div>
        </template>


        <!-- Product Card Template -->
        <template id="product-card-template">
            <div class="product-card bg-white border border-yellow-200 rounded-xl overflow-hidden shadow relative">
                <div class="relative">
                    <img class="product-img w-full h-48 object-cover" src="" alt="Product" />
                    <div class="hover-overlay rounded-xl">View Details</div>
                    <div class="absolute top-2 right-2 bg-yellow-400 text-white text-xs font-bold px-2 py-1 rounded">
                        <span class="product-price"></span>
                    </div>
                </div>
                <div class="p-4">
                    <h3 class="product-title text-lg font-semibold mb-1"></h3>
                    <p class="product-location text-sm text-yellow-700"></p>
                </div>
            </div>
        </template>

        <!-- JSON Data -->
        <div id="product-data" data-json='${fn:escapeXml(ownerProductsJson)}'></div>

        <script>
            let productList = [];

            document.addEventListener("DOMContentLoaded", () => {
                const rawJson = document.getElementById("product-data").dataset.json;
                productList = JSON.parse(rawJson);
                let currentPage = 1;
                const itemsPerPage = 8;

                const searchInput = document.getElementById("searchInput");
                const container = document.getElementById("product-container");
                const pagination = document.getElementById("pagination");
                const productTemplate = document.getElementById("product-card-template");
                const detailTemplate = document.getElementById("product-detail-template");

                function renderProducts(products) {
                    const start = (currentPage - 1) * itemsPerPage;
                    const paginated = products.slice(start, start + itemsPerPage);
                    container.innerHTML = "";
                    paginated.forEach((product) => {
                        const clone = productTemplate.content.cloneNode(true);
                        const imgUrl = product.images?.[0]?.imageUrl || "default.png";
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

                function renderPagination(total) {
                    const pageCount = Math.ceil(total / itemsPerPage);
                    pagination.innerHTML = "";
                    for (let i = 1; i <= pageCount; i++) {
                        const btn = document.createElement("button");
                        btn.textContent = i;
                        btn.className = `mx-1 px-3 py-1 rounded-full text-sm font-medium transition ${
            i == currentPage ? "bg-yellow-500 text-white" : "bg-white border border-yellow-300 text-yellow-700 hover:bg-yellow-50"
            }`;
                        btn.onclick = () => {
                            currentPage = i;
                            updateView();
                        };
                        pagination.appendChild(btn);
                    }
                }

                function updateView() {
                    const query = searchInput.value.toLowerCase();
                    const filtered = productList.filter(p => p.title.toLowerCase().includes(query));
                    renderProducts(filtered);
                }

                window.showProductDetail = function (productId) {
                    const product = productList.find(p => p.productId === productId);
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
//                    clone.querySelector(".modal-buy-form input[name='productId']").value = product.productId;
                    const editForm = clone.querySelector("form[action='EditProductServlet']");
                    const deleteForm = clone.querySelector("form[action='DeleteProductServlet']");
                    editForm.querySelector("input[name='productId']").value = product.productId;
                    deleteForm.querySelector("input[name='productId']").value = product.productId;


                    const detailContent = document.getElementById("product-detail-content");
                    detailContent.innerHTML = "";
                    detailContent.appendChild(clone);

                    document.getElementById("productModal").classList.remove("hidden");
                };

                window.closeModal = function () {
                    document.getElementById("productModal").classList.add("hidden");
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
