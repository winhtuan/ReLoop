<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Post Table Manager</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded" rel="stylesheet" />
        <style>
            .material-symbols-rounded {
                font-variation-settings: 'FILL' 1, 'wght' 400, 'GRAD' 0, 'opsz' 24;
            }
            main {
                background: #F3F5F9;
                height: 100%;
            }
            main .header {
                background: #F3F5F9;
                display: flex;
                align-items: center;
                padding: 16px 24px;
                font-family: 'Poppins', sans-serif;
                gap: 12px;
                border-bottom: 1px solid #ddd;
                margin-bottom: 50px;
            }
            main .header h1 {
                font-size: 30px;
                font-weight: 600;
                color: #2c3e50;
                margin: 0;
            }
            .modal-overlay {
                background-color: rgba(0, 0, 0, 0.4);
            }
        </style>
    </head>
    <body>

        <!-- Sidebar nếu có -->
        <c:import url="sidebar.jsp" />

        <main class="flex-1">
            <div class="header bg-white flex items-center px-6 py-4 gap-3 pb-[100px]">
                <span class="material-symbols-rounded text-gray-600 text-3xl">no_accounts</span>
                <h1 class="text-2xl font-semibold text-gray-800">Account Manager</h1>
            </div>

            <div class="max-w-7xl mx-auto bg-white shadow-lg rounded-xl p-6">
                <!-- Search -->
                <div class="flex items-center gap-2 mb-4 justify-end">
                    <input type="text" id="searchInput" placeholder="Search by title..."
                           class="border rounded px-4 py-2 w-64 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
                <!-- Table -->
                <div class="overflow-x-auto">
                    <table class="min-w-full text-sm text-left border border-gray-200">
                        <thead class="bg-gray-100 text-gray-700 uppercase">
                            <tr>
                                <th class="px-4 py-2 border-b">Username</th>
                                <th class="px-4 py-2 border-b">Email</th>
                                <th class="px-4 py-2 border-b">Phone</th>
                                <th class="px-4 py-2 border-b">Role</th>
                                <th class="px-4 py-2 border-b text-right pr-[110px]">Actions</th>
                            </tr>
                        </thead>
                        <tbody id="postTableBody" class="text-gray-800">
                            <c:forEach var="i" begin="1" end="1000">
                                <tr class="border-b">
                                    <td class="px-4 py-2">UserName ${i}</td>
                                    <td class="px-4 py-2">Email ${i}</td>
                                    <td class="px-4 py-2">Phone ${i} goes here...</td>
                                    <td class="px-4 py-2">Role</td>
                                    <td class="px-4 py-2 text-right pr-[110px]">
                                        <div class="flex justify-end items-center gap-4">
                                            <!-- Block button -->
                                            <button button onclick="openModal('unblockModal')" 
                                                    type="button" class="group relative cursor-pointer text-green-600 hover:text-green-800">
                                                <span class="material-symbols-rounded text-xl">person</span>
                                                <div class="absolute bottom-full mb-1 left-1/2 transform -translate-x-1/2 text-xs bg-gray-800 text-white rounded px-2 py-1 opacity-0 group-hover:opacity-100 transition">
                                                    Unblock
                                                </div>
                                            </button>

                                            <!-- Delete button -->
                                            <button button onclick="openModal('deleteModal')"
                                                    type="button" class="group relative cursor-pointer text-red-600 hover:text-red-800">
                                                <span class="material-symbols-rounded text-xl">delete</span>
                                                <div class="absolute bottom-full mb-1 left-1/2 transform -translate-x-1/2 text-xs bg-gray-800 text-white rounded px-2 py-1 opacity-0 group-hover:opacity-100 transition">
                                                    Delete
                                                </div>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div id="pagination" class="mt-6 flex justify-center"></div>
                </div>
            </div>
            <!-- Unblock Modal -->
            <div id="unblockModal" class="fixed inset-0 flex items-center justify-center z-50 hidden modal-overlay">
                <div class="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
                    <h2 class="text-xl font-semibold mb-4 text-gray-800">Confirm Unblock</h2>
                    <p class="text-gray-600 mb-6">Are you sure you want to unblock this account?</p>
                    <div class="flex justify-end gap-3">
                        <button onclick="closeModal('unblockModal')" class="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400">Cancel</button>
                        <button onclick="confirmBlock()" class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">Confirm</button>
                    </div>
                </div>
            </div>

            <!-- Delete Modal -->
            <div id="deleteModal" class="fixed inset-0 flex items-center justify-center z-50 hidden modal-overlay">
                <div class="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
                    <h2 class="text-xl font-semibold mb-4 text-gray-800">Confirm Delete</h2>
                    <p class="text-gray-600 mb-6">Are you sure you want to delete this account?</p>
                    <div class="flex justify-end gap-3">
                        <button onclick="closeModal('deleteModal')" class="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400">Cancel</button>
                        <button onclick="confirmDelete()" class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700">Confirm</button>
                    </div>
                </div>
            </div>

        </main>

        <!-- Pagination buttons -->

        <script>
            const searchInput = document.getElementById("searchInput");
            const rows = Array.from(document.querySelectorAll("#postTableBody tr"));
            const rowsPerPage = 10;
            let currentPage = 1;
            let filteredRows = [...rows]; // lưu tất cả dòng ban đầu

            function showPage(page) {
                const start = (page - 1) * rowsPerPage;
                const end = start + rowsPerPage;

                // Ẩn tất cả dòng
                rows.forEach(row => row.style.display = "none");

                // Hiện các dòng trong trang hiện tại
                filteredRows.slice(start, end).forEach(row => {
                    row.style.display = "";
                });

                currentPage = page;
                updatePaginationButtons();
            }

            function updatePaginationButtons() {
                const totalPages = Math.ceil(filteredRows.length / rowsPerPage);
                const pagination = document.getElementById("pagination");
                pagination.innerHTML = "";

                if (totalPages === 0) {
                    pagination.innerHTML = "<p class='text-gray-500'>No results found.</p>";
                    return;
                }

                const maxVisible = 3; // Số lượng nút số trang được hiển thị
                let startPage = Math.max(1, currentPage - Math.floor(maxVisible / 2));
                let endPage = startPage + maxVisible - 1;

                if (endPage > totalPages) {
                    endPage = totalPages;
                    startPage = Math.max(1, endPage - maxVisible + 1);
                }

                // << và < nút điều hướng đầu
                if (currentPage > 1) {
                    addButton("<<", 1);
                    addButton("<", currentPage - 1);
                }

                // Nút các trang
                for (let i = startPage; i <= endPage; i++) {
                    const btn = document.createElement("button");
                    btn.textContent = i;
                    let btnClass = "px-3 py-1 border rounded mx-1 ";
                    btnClass += (i === currentPage)
                            ? "bg-blue-500 text-white"
                            : "bg-white text-blue-500 hover:bg-blue-100";
                    btn.className = btnClass;
                    btn.addEventListener("click", () => showPage(i));
                    pagination.appendChild(btn);
                }

                // > và >> nút điều hướng cuối
                if (currentPage < totalPages) {
                    addButton(">", currentPage + 1);
                    addButton(">>", totalPages);
                }

                function addButton(label, targetPage) {
                    const btn = document.createElement("button");
                    btn.textContent = label;
                    btn.className = "px-3 py-1 border rounded mx-1 bg-white text-blue-500 hover:bg-blue-100";
                    btn.addEventListener("click", () => showPage(targetPage));
                    pagination.appendChild(btn);
                }
            }
            //Modal
            function openModal(id) {
                document.getElementById(id).classList.remove("hidden");
            }

            function closeModal(id) {
                document.getElementById(id).classList.add("hidden");
            }

            function confirmBlock() {
                // TODO: Gọi API hoặc logic block tại đây
                alert("Account blocked!");
                closeModal('blockModal');
            }

            function confirmDelete() {
                // TODO: Gọi API hoặc logic xóa tại đây
                alert("Account deleted!");
                closeModal('deleteModal');
            }

            // Tìm kiếm
            searchInput.addEventListener("keyup", () => {
                const keyword = searchInput.value.trim().toLowerCase();
                filteredRows = rows.filter(row => {
                    const title = row.children[1]?.textContent.toLowerCase() || "";
                    const content = row.children[2]?.textContent.toLowerCase() || "";
                    return title.includes(keyword) || content.includes(keyword);
                });

                showPage(1); // reset về trang đầu
            });

            // Hiển thị ban đầu
            showPage(1);
        </script>

    </body>
</html>
