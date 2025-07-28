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
                background: #FFFFFF;
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
                <span class="material-symbols-rounded text-gray-600 text-3xl">person_search</span>
                <h1 class="text-2xl font-semibold text-gray-800">Post Manager</h1>
            </div>

            <!-- Hiển thị thông báo thành công/thất bại -->
            <div class="max-w-7xl mx-auto px-4 mt-4">
                <c:if test="${param.msg == 'delete_success'}">
                    <div class="bg-green-100 text-green-800 px-4 py-2 rounded mb-4 shadow">
                        ✅ Account deleted successfully!
                    </div>
                </c:if>
                <c:if test="${param.msg == 'delete_failed'}">
                    <div class="bg-red-100 text-red-800 px-4 py-2 rounded mb-4 shadow">
                        ❌ Failed to delete account. Please try again.
                    </div>
                </c:if>
                <c:if test="${param.msg == 'created'}">
                    <div class="bg-green-100 text-green-800 px-4 py-2 rounded mb-4 shadow">
                        ✅ Account created successfully!
                    </div>
                </c:if>
                <c:if test="${param.msg == 'create_acc_fail'}">
                    <div class="bg-red-100 text-red-800 px-4 py-2 rounded mb-4 shadow">
                        ❌ Failed to create account. Please try again.
                    </div>
                </c:if>
                <c:if test="${param.msg == 'create_user_fail'}">
                    <div class="bg-red-100 text-red-800 px-4 py-2 rounded mb-4 shadow">
                        ❌ Failed to create user record. Please try again.
                    </div>
                </c:if>
                <c:if test="${param.msg == 'role_updated'}">
                    <div class="bg-green-100 text-green-800 px-4 py-2 rounded mb-4 shadow">
                        ✅ Role updated successfully!
                    </div>
                </c:if>
                <c:if test="${param.msg == 'role_update_failed'}">
                    <div class="bg-red-100 text-red-800 px-4 py-2 rounded mb-4 shadow">
                        ❌ Failed to update role. Please try again.
                    </div>
                </c:if>
            </div>


            <div class="w-[90%] max-w-[1600px] mx-auto bg-white shadow-lg rounded-xl p-6">

                <!-- Create Account  -->
                <div class="flex items-center justify-end mb-4">
                    <!-- Button Create Account -->
                    <!--                    <button onclick="openModal('createModal')"
                                                class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg flex items-center gap-2 transition-colors">
                                            <span class="material-symbols-rounded">add</span>
                                            <span>Create Account</span>
                                        </button>-->

                    <!-- Search -->
                    <div class="flex items-center gap-2">
                        <input type="text" id="searchInput" placeholder="Search by title..."
                               class="border rounded px-4 py-2 w-[420px] focus:outline-none focus:ring-2 focus:ring-gray-300" />
                    </div>
                </div>

                <!-- Table -->
                <div class="overflow-x-auto">
                    <table class="min-w-full text-sm text-left border border-gray-200">
                        <thead class="bg-gray-100 text-gray-700 uppercase">
                            <tr>
                                <th class="px-4 py-2 border-b">Image</th>
                                <th class="px-4 py-2 border-b">productId</th>
                                <th class="px-4 py-2 border-b">userId</th>
                                <th class="px-4 py-2 border-b">title</th>
                                <th class="px-4 py-2 border-b">description</th>
                                <th class="px-4 py-2 border-b">price</th>
                            </tr>
                        </thead>
                        <tbody id="postTableBody" class="text-gray-800">
                            <c:forEach var="product" items="${approvalPosts}">
                                <tr class="border-b">
                                    <td class="px-4 py-2">
                                        <img src="${imageMap[product.productId]}" class="rounded w-[60px] h-auto" />
                                    </td>
                                    <td class="px-4 py-2">${product.productId}</td>
                                    <td class="px-4 py-2">${product.userId}</td>
                                    <td class="px-4 py-2">${product.title}</td>
                                    <td class="px-4 py-2">${product.description}</td>
                                    <td class="px-4 py-2">${product.price}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div id="pagination" class="mt-6 flex justify-center"></div>
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
            function openModal(id, productID, userId) {
                document.getElementById(id).classList.remove("hidden");

                // Gán userId vào input hidden tương ứng
                if (id === 'approveModal') {
                    document.getElementById('approveProductId').value = productID;
                    document.getElementById('rejectUserId').value = userId;
                } else if (id === 'rejectModal') {
                    document.getElementById('rejectProductId').value = productID;
                    document.getElementById('rejectUserId').value = userId;
                }
            }

            function closeModal(id) {
                document.getElementById(id).classList.add("hidden");
            }

            function openEditModal(userId, fullName, email, phone, role) {
                document.getElementById('editUserId').value = userId;
                document.getElementById('editFullName').value = fullName;
                document.getElementById('editEmail').value = email;
                document.getElementById('editPhone').value = phone;
                document.getElementById('editRole').value = role;
                document.getElementById('editModal').classList.remove('hidden');
            }

            function closeModaledit(modalId) {
                document.getElementById(modalId).classList.add('hidden');
            }
            // Tìm kiếm
            searchInput.addEventListener("keyup", () => {
                const keyword = searchInput.value.trim().toLowerCase();
                filteredRows = rows.filter(row => {
                    const title = row.children[4]?.textContent.toLowerCase() || "";
                    const content = row.children[5]?.textContent.toLowerCase() || "";
                    return title.includes(keyword) || content.includes(keyword);
                });

                showPage(1); // reset về trang đầu
            });

            // Hiển thị ban đầu
            showPage(1);
        </script>

    </body>
</html>
