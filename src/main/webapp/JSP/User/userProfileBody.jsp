<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- Google Material Symbols -->
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded" rel="stylesheet" />
<link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/core-style.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<div class="max-w-4xl mx-auto bg-white rounded-3xl shadow-2xl border border-yellow-100 mt-10 overflow-hidden">
    <!-- Header -->
    <div class="bg-gradient-to-r from-yellow-50 via-yellow-100 to-yellow-200 px-10 py-8 flex flex-col md:flex-row items-center gap-8 border-b border-yellow-200">
        <div class="relative w-36 h-36 flex-shrink-0">
            <img id="avatar-preview"
                 src="${empty user.srcImg ? 'images/default-avatar.png' : user.srcImg}"
                 alt="Avatar"
                 class="w-36 h-36 rounded-full object-cover border-4 border-yellow-300 shadow-lg transition-transform duration-300 hover:scale-105" />
            <label class="absolute inset-0 bg-black bg-opacity-0 hover:bg-opacity-30 text-white flex items-center justify-center w-36 h-36 rounded-full cursor-pointer transition">
                <i class="fas fa-camera text-2xl"></i>
                <input type="file" class="hidden" id="avatar-input" name="avatar" accept="image/*" form="profile-form">
            </label>
        </div>
        <div class="flex-1 flex flex-col gap-2">
            <h2 class="text-3xl font-extrabold text-gray-800 tracking-tight mb-1">${user.fullName}</h2>
            <div class="flex gap-8 text-center">
                <div>
                    <div class="text-gray-500 text-xs">Followers</div>
                    <div class="font-bold text-yellow-600 text-xl">${follower}</div>
                </div>
                <div>
                    <div class="text-gray-500 text-xs">Following</div>
                    <div class="font-bold text-yellow-600 text-xl">${following}</div>
                </div>
                <div>
                    <div class="text-gray-500 text-xs">Balance</div>
                    <div class="font-bold text-yellow-600 text-xl mb-1">${user.balance}$</div>
                    <button onclick="showWithdrawModal()" class="px-4 py-1.5 bg-yellow-400 text-white rounded-lg shadow hover:bg-yellow-500 transition font-semibold">
                        <i class="fas fa-money-bill-wave mr-1"></i> Withdraw
                    </button>
                </div>
            </div>
            <c:if test="${not empty success}">
                <div class="mt-2 px-4 py-2 bg-green-100 border border-green-300 text-green-800 rounded-xl shadow flex items-center gap-2">
                    <i class="fas fa-check-circle text-lg"></i> <span>${success}</span>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="mt-2 px-4 py-2 bg-red-100 border border-red-300 text-red-800 rounded-xl shadow flex items-center gap-2">
                    <i class="fas fa-exclamation-circle text-lg"></i> <span>${error}</span>
                </div>
            </c:if>
        </div>
    </div>
    <!-- Tabs -->
    <div class="flex space-x-2 px-10 pt-6 border-b border-yellow-200 bg-white">
        <button onclick="showSection('profile')" class="tab-btn px-5 py-2 text-gray-700 font-semibold rounded-t-lg border-b-4 border-transparent hover:border-yellow-400 hover:text-yellow-600 transition">Profile</button>
        <button onclick="showSection('following')" class="tab-btn px-5 py-2 text-gray-700 font-semibold rounded-t-lg border-b-4 border-transparent hover:border-yellow-400 hover:text-yellow-600 transition">Following</button>
        <button onclick="showSection('follower')" class="tab-btn px-5 py-2 text-gray-700 font-semibold rounded-t-lg border-b-4 border-transparent hover:border-yellow-400 hover:text-yellow-600 transition">Follower</button>
        <button onclick="showSection('premium')" class="tab-btn px-5 py-2 text-gray-700 font-semibold rounded-t-lg border-b-4 border-transparent hover:border-yellow-400 hover:text-yellow-600 transition">Premium</button>
    </div>
    <!-- Content -->
    <div class="p-10 bg-white">
        <!-- Profile Section -->
        <div id="section-profile" class="space-y-8">
            <form id="profile-form" action="UpdateUserProfileServlet" method="post" enctype="multipart/form-data" class="space-y-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <div>
                        <label class="block mb-1 text-sm font-medium text-gray-700">Full Name</label>
                        <div class="flex">
                            <input type="text" name="fullName" id="fullname" value="${user.fullName}"
                                   class="flex-1 px-4 py-2 border border-yellow-200 rounded-lg bg-gray-50 text-gray-700 focus:bg-white focus:border-yellow-400 focus:ring-2 focus:ring-yellow-200 transition" readonly>
                            <button type="button" onclick="enableEdit('fullname')" class="ml-2 px-3 py-2 bg-yellow-400 text-white rounded-lg hover:bg-yellow-500 shadow transition">
                                <i class="fas fa-edit"></i>
                            </button>
                        </div>
                    </div>
                    <div>
                        <label class="block mb-1 text-sm font-medium text-gray-700">Email</label>
                        <div class="flex">
                            <input type="email" name="email" id="Email" value="${user.email}"
                                   class="flex-1 px-4 py-2 border border-yellow-200 rounded-lg bg-gray-50 text-gray-700 focus:bg-white focus:border-yellow-400 focus:ring-2 focus:ring-yellow-200 transition" readonly>
                            <button type="button" onclick="enableEdit('Email')" class="ml-2 px-3 py-2 bg-yellow-400 text-white rounded-lg hover:bg-yellow-500 shadow transition">
                                <i class="fas fa-edit"></i>
                            </button>
                        </div>
                    </div>
                    <div>
                        <label class="block mb-1 text-sm font-medium text-gray-700">Phone</label>
                        <div class="flex">
                            <input type="tel" name="phone" id="phone" value="${user.phoneNumber}"
                                   class="flex-1 px-4 py-2 border border-yellow-200 rounded-lg bg-gray-50 text-gray-700 focus:bg-white focus:border-yellow-400 focus:ring-2 focus:ring-yellow-200 transition" readonly>
                            <button type="button" onclick="enableEdit('phone')" class="ml-2 px-3 py-2 bg-yellow-400 text-white rounded-lg hover:bg-yellow-500 shadow transition">
                                <i class="fas fa-edit"></i>
                            </button>
                        </div>
                    </div>
                    <div>
                        <label class="block mb-1 text-sm font-medium text-gray-700">Address</label>
                        <div class="flex">
                            <textarea name="address" id="address" rows="2"
                                      class="flex-1 px-4 py-2 border border-yellow-200 rounded-lg bg-gray-50 text-gray-700 focus:bg-white focus:border-yellow-400 focus:ring-2 focus:ring-yellow-200 transition" readonly>${user.address}</textarea>
                            <button type="button" onclick="enableEdit('address')" class="ml-2 px-3 py-2 bg-yellow-400 text-white rounded-lg hover:bg-yellow-500 shadow transition">
                                <i class="fas fa-edit"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="flex justify-end space-x-3 mt-4">
                    <button type="button" onclick="openPasswordModal()" class="px-4 py-2 bg-yellow-100 text-yellow-700 rounded-lg hover:bg-yellow-200 border border-yellow-300 shadow">
                        <i class="fas fa-key mr-2"></i> Change Password
                    </button>
                    <button type="submit" class="px-4 py-2 bg-yellow-400 text-white rounded-lg hover:bg-yellow-500 shadow font-semibold">
                        <i class="fas fa-save mr-2"></i> Save
                    </button>
                </div>
            </form>
        </div>
        <!-- Following Section -->
        <div id="section-following" class="hidden">
            <div class="flex justify-end items-center mb-4">
                <input type="text" id="search-following" placeholder="Search..." 
                       class="border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-yellow-400" />
            </div>
            <div class="overflow-x-auto rounded-xl shadow">
                <table class="min-w-full text-sm text-left border border-gray-200 bg-white rounded-xl overflow-hidden" id="followingTable">
                    <thead class="bg-yellow-50 text-gray-700 uppercase">
                        <tr>
                            <th class="px-4 py-2 border-b">Avatar</th>
                            <th class="px-4 py-2 border-b">Username</th>
                            <th class="px-4 py-2 border-b text-right">
                                <div class="pr-[29px]">Actions</div>
                            </th>
                        </tr>
                    </thead>
                    <tbody class="text-gray-800">
                        <c:forEach var="user" items="${listfollowing}">
                            <tr class="border-b hover:bg-yellow-50 transition">
                                <td class="px-4 py-2">
                                    <a href="ListProductServlet?userId=${user.userId}">
                                        <img src="${user.srcImg}" alt="avatar"
                                             class="w-10 h-10 rounded-full object-cover border border-yellow-200 shadow-sm hover:scale-105 transition" />
                                    </a>
                                </td>
                                <td class="px-4 py-2">
                                    <a href="ListProductServlet?userId=${user.userId}"
                                       class="block text-[15px] font-medium text-black">
                                        ${user.fullName}
                                    </a>
                                </td>
                                <td class="px-4 py-2 pr-6 text-right">
                                    <form method="post" action="UnFollowServlet">
                                        <input type="hidden" name="followingId" value="${user.userId}" />
                                        <button type="submit"
                                                class="inline-flex items-center gap-1 px-3 py-1 border border-red-400 text-red-600 hover:bg-red-50 hover:border-red-600 rounded-full transition duration-200 text-sm font-medium">
                                            <span class="material-symbols-rounded text-base">person_remove</span>
                                            Unfollow
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div id="pagination-following" class="mt-6 flex justify-center"></div>
            </div>
        </div>
        <!-- Follower Section -->
        <div id="section-follower" class="hidden">
            <div class="flex justify-end items-center mb-4">
                <input type="text" id="search-follower" placeholder="Search..." 
                       class="border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-yellow-400" />
            </div>
            <div class="overflow-x-auto rounded-xl shadow">
                <table class="min-w-full text-sm text-left border border-gray-200 bg-white rounded-xl overflow-hidden" id="followerTable">
                    <thead class="bg-yellow-50 text-gray-700 uppercase">
                        <tr>
                            <th class="px-4 py-2 border-b">Avatar</th>
                            <th class="px-4 py-2 border-b">Username</th>
                        </tr>
                    </thead>
                    <tbody class="text-gray-800">
                        <c:forEach var="user" items="${listfollower}">
                            <tr class="border-b hover:bg-yellow-50 transition">
                                <td class="px-4 py-2">
                                    <img src="${user.srcImg}" alt="avatar" class="w-10 h-10 rounded-full object-cover border border-yellow-200" />
                                </td>
                                <td class="px-4 py-2 font-medium text-gray-900">
                                    ${user.fullName}
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div id="pagination-follower" class="mt-6 flex justify-center"></div>
            </div>
        </div>
        <!-- Premium Section -->
        <div id="section-premium" class="hidden">
            <div class="bg-yellow-50 border border-yellow-200 rounded-2xl p-8 shadow flex items-center gap-6">
                <i class="fas fa-crown text-yellow-400 text-4xl"></i>
                <div>
                    <h3 class="text-xl font-semibold text-yellow-700 mb-2">Your Premium Membership</h3>
                    <p class="text-gray-700">
                        Expiration Date:
                        <span class="font-semibold text-yellow-600">
                            <fmt:formatDate value="${user.premiumExpiry}" pattern="dd/MM/yyyy" />
                        </span>
                    </p>
                    <p class="mt-2 text-sm text-gray-500">
                        Renew to continue enjoying exclusive features!
                    </p>
                </div>
            </div>
        </div>
    </div>
    <!-- Change Password Modal -->
    <div id="password-modal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center hidden z-50">
        <div class="bg-white rounded-2xl p-8 w-full max-w-md shadow-xl">
            <h2 class="text-2xl font-bold mb-4 text-gray-800">Change Password</h2>
            <form id="change-password-form" action="ChangePasswordServlet" method="post" class="space-y-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Current Password</label>
                    <input type="password" name="currentPassword" class="w-full px-4 py-2 border border-yellow-200 rounded-lg focus:border-yellow-400 focus:ring-2 focus:ring-yellow-200 transition" required>
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">New Password</label>
                    <input type="password" name="newPassword" class="w-full px-4 py-2 border border-yellow-200 rounded-lg focus:border-yellow-400 focus:ring-2 focus:ring-yellow-200 transition" required>
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Confirm New Password</label>
                    <input type="password" name="confirmPassword" class="w-full px-4 py-2 border border-yellow-200 rounded-lg focus:border-yellow-400 focus:ring-2 focus:ring-yellow-200 transition" required>
                </div>
                <div class="flex justify-end gap-3 mt-4">
                    <button type="button" onclick="closePasswordModal()" class="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 border border-gray-300">Cancel</button>
                    <button type="submit" class="px-4 py-2 bg-yellow-400 text-white rounded-lg hover:bg-yellow-500 shadow font-semibold">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script>

    function showSection(id) {
        ['profile', 'following', 'follower', 'premium'].forEach(sec => {
            document.getElementById('section-' + sec).classList.add('hidden');
        });
        document.getElementById('section-' + id).classList.remove('hidden');
    }

    function enableEdit(id) {
        const field = document.getElementById(id);
        if (field.hasAttribute('readonly')) {
            field.removeAttribute('readonly');
            field.classList.remove('bg-gray-100');
            field.classList.add('bg-white', 'ring-1', 'ring-yellow-400');
            field.focus();
        }
    }

    function openPasswordModal() {
        document.getElementById('password-modal').classList.remove('hidden');
    }

    function closePasswordModal() {
        document.getElementById('password-modal').classList.add('hidden');
    }

    function openWithdrawModal() {
        if (window.showWithdrawModal)
            window.showWithdrawModal();
    }

    document.getElementById('avatar-input').addEventListener('change', function (e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (event) {
                document.getElementById('avatar-preview').src = event.target.result;
            };
            reader.readAsDataURL(file);
        }
    });

    document.getElementById('fullname').addEventListener('input', function () {
        document.getElementById('fullname-display').textContent = this.value;
    });

    // Validate Password Form
    document.getElementById("change-password-form").addEventListener("submit", function (e) {
        const newPassword = document.getElementById("new-password").value.trim();
        const confirmPassword = document.getElementById("confirm-password").value.trim();
        const errorBox = document.getElementById("change-password-error");

        if (newPassword !== confirmPassword) {
            e.preventDefault();
            errorBox.textContent = "New passwords do not match.";
            errorBox.classList.remove("hidden");
        } else {
            errorBox.classList.add("hidden");
        }
    });

    // Following search
    document.getElementById("search-following").addEventListener("keyup", function () {
        const searchValue = this.value.toLowerCase();
        const rows = document.querySelectorAll("#followingTable tbody tr");

        rows.forEach(row => {
            const name = row.cells[1].innerText.toLowerCase();
            row.style.display = name.includes(searchValue) ? "" : "none";
        });
    });

// Follower search
    document.getElementById("search-follower").addEventListener("keyup", function () {
        const searchValue = this.value.toLowerCase();
        const rows = document.querySelectorAll("#followerTable tbody tr");

        rows.forEach(row => {
            const name = row.cells[1].innerText.toLowerCase();
            row.style.display = name.includes(searchValue) ? "" : "none";
        });
    });
// ---------------------- FOLLOWING PAGINATION -----------------------
    const followingRows = Array.from(document.querySelectorAll("#followingTable tbody tr"));
    const followingRowsPerPage = 5;
    let currentFollowingPage = 1;
    let filteredFollowingRows = [...followingRows];

    function showFollowingPage(page) {
        const start = (page - 1) * followingRowsPerPage;
        const end = start + followingRowsPerPage;

        followingRows.forEach(row => row.style.display = "none");
        filteredFollowingRows.slice(start, end).forEach(row => row.style.display = "");

        currentFollowingPage = page;
        updateFollowingPagination();
    }

    function updateFollowingPagination() {
        const totalPages = Math.ceil(filteredFollowingRows.length / followingRowsPerPage);
        const pagination = document.getElementById("pagination-following");
        pagination.innerHTML = "";

        if (totalPages === 0) {
            pagination.innerHTML = "<p class='text-gray-500'>No results found.</p>";
            return;
        }

        if (currentFollowingPage > 1) {
            addButton("<<", 1, pagination, showFollowingPage);
            addButton("<", currentFollowingPage - 1, pagination, showFollowingPage);
        }

        for (let i = 1; i <= totalPages; i++) {
            const btn = document.createElement("button");
            btn.textContent = i;
            btn.className = `px-3 py-1 border rounded mx-1 ${i == currentFollowingPage ? "bg-blue-500 text-white" : "bg-white text-blue-500 hover:bg-blue-100"}`;
            btn.addEventListener("click", () => showFollowingPage(i));
            pagination.appendChild(btn);
        }

        if (currentFollowingPage < totalPages) {
            addButton(">", currentFollowingPage + 1, pagination, showFollowingPage);
            addButton(">>", totalPages, pagination, showFollowingPage);
        }
    }

// ---------------------- FOLLOWER PAGINATION -----------------------
    const followerRows = Array.from(document.querySelectorAll("#followerTable tbody tr"));
    const followerRowsPerPage = 5;
    let currentFollowerPage = 1;
    let filteredFollowerRows = [...followerRows];

    function showFollowerPage(page) {
        const start = (page - 1) * followerRowsPerPage;
        const end = start + followerRowsPerPage;

        followerRows.forEach(row => row.style.display = "none");
        filteredFollowerRows.slice(start, end).forEach(row => row.style.display = "");

        currentFollowerPage = page;
        updateFollowerPagination();
    }

    function updateFollowerPagination() {
        const totalPages = Math.ceil(filteredFollowerRows.length / followerRowsPerPage);
        const pagination = document.getElementById("pagination-follower");
        pagination.innerHTML = "";

        if (totalPages === 0) {
            pagination.innerHTML = "<p class='text-gray-500'>No results found.</p>";
            return;
        }

        if (currentFollowerPage > 1) {
            addButton("<<", 1, pagination, showFollowerPage);
            addButton("<", currentFollowerPage - 1, pagination, showFollowerPage);
        }

        for (let i = 1; i <= totalPages; i++) {
            const btn = document.createElement("button");
            btn.textContent = i;
            btn.className = `px-3 py-1 border rounded mx-1 ${i == currentFollowerPage ? "bg-blue-500 text-white" : "bg-white text-blue-500 hover:bg-blue-100"}`;
            btn.addEventListener("click", () => showFollowerPage(i));
            pagination.appendChild(btn);
        }

        if (currentFollowerPage < totalPages) {
            addButton(">", currentFollowerPage + 1, pagination, showFollowerPage);
            addButton(">>", totalPages, pagination, showFollowerPage);
        }
    }

// Reusable addButton function
    function addButton(label, page, container, handler) {
        const btn = document.createElement("button");
        btn.textContent = label;
        btn.className = "px-3 py-1 border rounded mx-1 bg-white text-blue-500 hover:bg-blue-100";
        btn.addEventListener("click", () => handler(page));
        container.appendChild(btn);
    }

// Initialize pages on load
    showFollowingPage(1);
    showFollowerPage(1);

// Update filtered rows when searching
    document.getElementById("search-following").addEventListener("keyup", function () {
        const keyword = this.value.trim().toLowerCase();
        filteredFollowingRows = followingRows.filter(row =>
            row.cells[1].innerText.toLowerCase().includes(keyword)
        );
        showFollowingPage(1);
    });

    document.getElementById("search-follower").addEventListener("keyup", function () {
        const keyword = this.value.trim().toLowerCase();
        filteredFollowerRows = followerRows.filter(row =>
            row.cells[1].innerText.toLowerCase().includes(keyword)
        );
        showFollowerPage(1);
    });

</script>
