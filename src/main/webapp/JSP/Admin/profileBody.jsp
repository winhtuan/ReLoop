<!-- Font Awesome 5 -->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="max-w-4xl mx-auto bg-white rounded-xl profile-container overflow-hidden">
    <div class="bg-gradient-to-r from-yellow-100 to-yellow-200 p-6">
        <h1 class="text-3xl font-bold text-gray-800">Profile Settings</h1>

        <c:if test="${not empty success}">
            <div class="mt-4 px-4 py-3 bg-green-100 border border-green-300 text-green-800 rounded">
                <i class="fas fa-check-circle mr-2"></i> ${success}
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="mt-4 px-4 py-3 bg-red-100 border border-red-300 text-red-800 rounded">
                <i class="fas fa-exclamation-circle mr-2"></i> ${error}
            </div>
        </c:if>

    </div>

    <div class="p-6 md:p-8">
        <div class="flex flex-col gap-8">
            <!-- Avatar -->
            <div class="flex flex-col md:flex-row md:items-center gap-6">
                <div class="relative w-32 h-32">
                    <img id="avatar-preview"
                         src="${empty user.srcImg ? 'images/default-avatar.png' : user.srcImg}"
                         alt="Avatar"
                         class="w-32 h-32 rounded-full object-cover" />
                    <label class="absolute inset-0 bg-black bg-opacity-40 text-white flex items-center justify-center w-32 h-32 rounded-full cursor-pointer hover:bg-opacity-60 transition">
                        <i class="fas fa-camera"></i>
                        <input type="file" class="hidden" id="avatar-input" name="avatar" accept="image/*" form="profile-form">
                    </label>
                </div>
                <div class="flex-1 space-y-2">
                    <!-- Full Name -->
                    <div class="flex items-center gap-2">
                        <i class="fas fa-user text-gray-600"></i>
                        <h2 id="fullname-display" class="text-2xl font-bold text-gray-800">${user.fullName}</h2>
                    </div>

                    <!-- Role -->
                    <div class="flex items-center gap-2">
                        <i class="fas fa-user-tag text-yellow-600"></i>
                        <span class="inline-block px-3 py-1 text-sm font-semibold text-yellow-700 bg-yellow-100 rounded-full">
                            Role: ${user.role}
                        </span>
                    </div>
                </div>
            </div>



            <!-- Profile Section -->
            <div id="section-profile" class="pt-6">
                <form id="profile-form" action="UpdateUserProfileServlet" method="post" enctype="multipart/form-data">
                    <!-- Full Name -->
                    <div class="mb-4">
                        <label class="block mb-1 text-sm font-medium text-gray-700">Full Name</label>
                        <div class="flex">
                            <input type="text" name="fullName" id="fullname" value="${user.fullName}"
                                   class="flex-1 px-4 py-2 border rounded-lg bg-gray-100 text-gray-700 focus:bg-white" readonly>
                            <button type="button" onclick="enableEdit('fullname')" class="ml-2 px-3 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600">
                                <i class="fas fa-edit"></i>
                            </button>
                        </div>
                    </div>

                    <!-- Email -->
                    <div class="mb-4">
                        <label class="block mb-1 text-sm font-medium text-gray-700">Email</label>
                        <div class="flex">
                            <input type="email" name="email" id="Email" value="${user.email}"
                                   class="flex-1 px-4 py-2 border rounded-lg bg-gray-100 text-gray-700 focus:bg-white" readonly>
                            <button type="button" onclick="enableEdit('Email')" class="ml-2 px-3 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600">
                                <i class="fas fa-edit"></i>
                            </button>
                        </div>
                    </div>

                    <!-- Phone -->
                    <div class="mb-4">
                        <label class="block mb-1 text-sm font-medium text-gray-700">Phone</label>
                        <div class="flex">
                            <input type="tel" name="phone" id="phone" value="${user.phoneNumber}"
                                   class="flex-1 px-4 py-2 border rounded-lg bg-gray-100 text-gray-700 focus:bg-white" readonly>
                            <button type="button" onclick="enableEdit('phone')" class="ml-2 px-3 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600">
                                <i class="fas fa-edit"></i>
                            </button>
                        </div>
                    </div>

                    <!-- Address -->
                    <div class="mb-6">
                        <label class="block mb-1 text-sm font-medium text-gray-700">Address</label>
                        <div class="flex">
                            <textarea name="address" id="address" rows="2"
                                      class="flex-1 px-4 py-2 border rounded-lg bg-gray-100 text-gray-700 focus:bg-white" readonly>${user.address}</textarea>
                            <button type="button" onclick="enableEdit('address')" class="ml-2 px-3 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600">
                                <i class="fas fa-edit"></i>
                            </button>
                        </div>
                    </div>

                    <div class="flex justify-end space-x-3">
                        <button type="button" onclick="openPasswordModal()" class="px-4 py-2 bg-yellow-100 text-yellow-700 rounded hover:bg-yellow-200">
                            <i class="fas fa-key mr-2"></i> Change Password
                        </button>
                        <button type="submit" class="px-4 py-2 bg-yellow-100 text-yellow-700 rounded hover:bg-yellow-200">
                            <i class="fas fa-save mr-2"></i> Save
                        </button>
                    </div>
                </form>

            </div>

            <!-- Change Password Modal -->
            <div id="password-modal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center hidden z-50">
                <div class="bg-white rounded-xl p-6 w-full max-w-md">
                    <h2 class="text-xl font-bold mb-4 text-gray-800">Change Password</h2>
                    <form id="change-password-form" action="ChangePasswordServlet" method="post">
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700 mb-1">Current Password</label>
                            <input type="password" name="currentPassword" id="current-password" class="w-full px-4 py-2 border rounded" required>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700 mb-1">New Password</label>
                            <input type="password" name="newPassword" id="new-password" class="w-full px-4 py-2 border rounded" required>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700 mb-1">Confirm New Password</label>
                            <input type="password" name="confirm-password" id="confirm-password" class="w-full px-4 py-2 border rounded" required>
                        </div>
                        <div id="change-password-error" class="text-red-600 text-sm mb-3 hidden"></div>
                        <div class="flex justify-end space-x-3">
                            <button type="button" onclick="closePasswordModal()" class="px-4 py-2 border rounded hover:bg-gray-100">Cancel</button>
                            <button type="submit" class="px-4 py-2 bg-yellow-500 text-white rounded hover:bg-yellow-600">Change</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- JavaScript -->
        <script>
            function showSection(id) {
                ['profile', 'following', 'premium'].forEach(sec => {
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
        </script>
