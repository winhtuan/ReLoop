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
                <h1 class="text-2xl font-semibold text-gray-800">Block List Manager</h1>
            </div>
            
            <!-- Hiển thị thông báo thành công/thất bại -->
            <div class="max-w-7xl mx-auto px-4">
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
            </div>

            <div class="max-w-7xl mx-auto bg-white shadow-lg rounded-xl p-6">
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
                               class="border rounded px-4 py-2 w-64 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                    </div>
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
                            <c:forEach var="user" items="${blockAccounts}">
                                <tr class="border-b">
                                    <td class="px-4 py-2">${user.fullName}</td>
                                    <td class="px-4 py-2">${user.email}</td>
                                    <td class="px-4 py-2">${user.phoneNumber}</td>
                                    <td class="px-4 py-2">${user.role}</td>
                                    <td class="px-4 py-2 text-right pr-[110px]">
                                        <div class="flex justify-end items-center gap-4">
                                            <!-- Unblock button -->
                                            <button onclick="openModal('unblockModal', '${user.userId}')" 
                                                    type="button" class="group relative cursor-pointer text-green-600 hover:text-green-800">
                                                <span class="material-symbols-rounded text-xl">lock_open_right</span>
                                                <div class="absolute bottom-full mb-1 left-1/2 transform -translate-x-1/2 text-xs bg-gray-800 text-white rounded px-2 py-1 opacity-0 group-hover:opacity-100 transition">
                                                    Unblock
                                                </div>
                                            </button>

                                            <!-- Delete button -->
                                            <button onclick="openModal('deleteModal', '${user.userId}')"
                                                    type="button" class="group relative cursor-pointer text-gray-600 hover:text-gray-800">
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
                    <h2 class="text-xl font-semibold mb-4 text-gray-800">Confirm Block</h2>
                    <p class="text-gray-600 mb-6">Are you sure you want to block this account?</p>
                    <div class="flex justify-end gap-3">
                        <button onclick="closeModal('unblockModal')" class="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400">Cancel</button>
                        <form action="UnblockAccountServlet" method="post">
                            <input type="hidden" name="user_id" id="unblockUserId">
                            <button type="submit" class="px-4 py-2 bg-yellow-600 text-white rounded hover:bg-yellow-700">Confirm</button>   
                        </form>
                    </div>
                </div>
            </div>

            <!-- Delete Modal -->
            <div id="deleteModal" class="fixed inset-0 flex items-center justify-center z-50 hidden modal-overlay">
                <div class="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
                    <h2 class="text-xl font-semibold mb-4 text-gray-800">Confirm Delete</h2>
                    <p class="text-gray-600 mb-6">Are you sure you want to delete this account?</p>
                    <div class="flex justify-end gap-3">
                        <!-- Đổi method="port" thành post -->
                        <button onclick="closeModal('deleteModal')" class="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400">Cancel</button>
                        <form action="DeleteAccountServlet" method="post">
                            <input type="hidden" name="origin" value="BlockListAccountServlet" />
                            <input type="hidden" name="user_id" id="deleteUserId">
                            <button type="submit" class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700">Confirm</button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Create Account Modal nhưng để lại vì nếu xóa thì bị bug script -->
            <div id="createModal" class="fixed inset-0 flex items-center justify-center z-50 hidden modal-overlay">
                <div class="bg-white p-6 rounded-lg shadow-lg w-full max-w-lg">
                    <h2 class="text-2xl font-semibold mb-4 text-gray-800">Create New Account</h2>

                    <form id="createAccountForm" class="space-y-4">
                        <div>
                            <label class="block text-gray-700">Username</label>
                            <input type="text" name="username" class="w-full border px-4 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" required />
                        </div>
                        <div>
                            <label class="block text-gray-700">Email</label>
                            <input type="email" name="email" class="w-full border px-4 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" required />
                        </div>
                        <div>
                            <label class="block text-gray-700">Password</label>
                            <input type="password" name="password" class="w-full border px-4 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" required />
                        </div>
                        <div>
                            <label class="block text-gray-700">Role</label>
                            <select name="role" class="w-full border px-4 py-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                                <option value="" disabled selected>Select role</option>
                                <option value="admin">Admin</option>
                                <option value="user">Supporter</option>
                                <!-- Add more roles if needed -->
                            </select>
                        </div>
                        <div class="flex justify-end gap-3 pt-4">
                            <button type="button" onclick="closeModal('createModal')" class="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400">Cancel</button>
                            <button type="submit" class="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700">Create</button>
                        </div>
                    </form>
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
            function openModal(id, userId) {
                document.getElementById(id).classList.remove("hidden");

                // Gán userId vào input hidden tương ứng
                if (id === 'unblockModal') {
                    document.getElementById('unblockUserId').value = userId;
                } else if (id === 'deleteModal') {
                    document.getElementById('deleteUserId').value = userId;
                }
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

            document.getElementById('createAccountForm').addEventListener('submit', function (e) {
                e.preventDefault();
                const formData = new FormData(this);

                // TODO: Gửi dữ liệu tới server hoặc xử lý tại đây
                alert("Account created!\nUsername: " + formData.get('username'));

                closeModal('createModal');
                this.reset(); // reset form sau khi tạo
            });
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
