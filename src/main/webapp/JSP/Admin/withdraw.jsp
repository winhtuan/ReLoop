<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
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
        <c:import url="sidebar.jsp" />
        <main class="flex-1">
            <div class="header bg-white flex items-center px-6 py-4 gap-3 pb-[100px]">
                <span class="material-symbols-rounded text-gray-600 text-3xl">account_balance_wallet</span>
                <h1 class="text-2xl font-semibold text-gray-800">Withdrawal Manager</h1>
            </div>

            <div class="max-w-7xl mx-auto bg-white shadow-lg rounded-xl p-6">
                <!-- Search -->
                <div class="flex items-center justify-end mb-4">
                    <input type="text" id="searchInput" placeholder="Search by User ID or Bank..." class="border rounded px-4 py-2 w-64 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>

                <!-- Table -->
                <div class="overflow-x-auto">
                    <table class="min-w-full text-sm text-left border border-gray-200">
                        <thead class="bg-gray-100 text-gray-700 uppercase">
                            <tr>
                                <th class="px-4 py-2 border-b">ID</th>
                                <th class="px-4 py-2 border-b">User ID</th>
                                <th class="px-4 py-2 border-b">Amount</th>
                                <th class="px-4 py-2 border-b">Bank</th>
                                <th class="px-4 py-2 border-b">Account No</th>
                                <th class="px-4 py-2 border-b">Name</th>
                                <th class="px-4 py-2 border-b">Created At</th>
                                <th class="px-4 py-2 border-b">Status</th>
                                <th class="px-4 py-2 border-b text-right pr-[100px]">Actions</th>
                            </tr>
                        </thead>
                        <tbody id="withdrawTableBody" class="text-gray-800">
                            <c:forEach var="withdraw" items="${withdrawList}">
                                <tr class="border-b">
                                    <td class="px-4 py-2">${withdraw.id}</td>
                                    <td class="px-4 py-2">${withdraw.userId}</td>
                                    <td class="px-4 py-2">${withdraw.amount} đ</td>
                                    <td class="px-4 py-2">${withdraw.bankCode}</td>
                                    <td class="px-4 py-2">${withdraw.accountNumber}</td>
                                    <td class="px-4 py-2">${withdraw.accountName}</td>
                                    <td class="px-4 py-2">
                                        <fmt:formatDate value="${withdraw.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                    </td>
                                    <td class="px-4 py-2">${withdraw.status}</td>
                                    <td class="px-4 py-2 text-right pr-[80px]">
                                        <c:if test="${withdraw.status == 'PENDING'}">
                                            <div class="flex justify-end items-center gap-4">
                                                <button onclick="confirmApprove('${withdraw.id}', '${withdraw.bankCode}', '${withdraw.accountNumber}', '${withdraw.accountName}', '${withdraw.amount}', '${withdraw.addInfo}', '${withdraw.userId}')" class="group text-green-600 hover:text-green-800 relative">
                                                    <span class="material-symbols-rounded text-xl">check_circle</span>
                                                    <div class="tooltip">Approve</div>
                                                </button>
                                                <button onclick="confirmReject('${withdraw.id}')" class="group text-red-600 hover:text-red-800 relative">
                                                    <span class="material-symbols-rounded text-xl">cancel</span>
                                                    <div class="tooltip">Reject</div>
                                                </button>
                                            </div>
                                        </c:if>
                                    </td>

                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div id="pagination" class="mt-6 flex justify-center"></div>
                </div>
            </div>

        </main>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <script>
                                                    const searchInput = document.getElementById("searchInput");
                                                    const rows = Array.from(document.querySelectorAll("#withdrawTableBody tr"));
                                                    const rowsPerPage = 10;
                                                    let currentPage = 1;
                                                    let filteredRows = [...rows];
                                                    function showPage(page) {
                                                        const start = (page - 1) * rowsPerPage;
                                                        const end = start + rowsPerPage;
                                                        rows.forEach(row => row.style.display = "none");
                                                        filteredRows.slice(start, end).forEach(row => row.style.display = "");
                                                        currentPage = page;
                                                        updatePaginationButtons();
                                                    }

                                                    function updatePaginationButtons() {
                                                        const pagination = document.getElementById("pagination");
                                                        pagination.innerHTML = "";
                                                        const totalPages = Math.ceil(filteredRows.length / rowsPerPage);
                                                        if (totalPages === 0) {
                                                            pagination.innerHTML = "<p class='text-gray-500'>No results found.</p>";
                                                            return;
                                                        }
                                                        for (let i = 1; i <= totalPages; i++) {
                                                            const btn = document.createElement("button");
                                                            btn.textContent = i;
                                                            btn.className = "px-3 py-1 border rounded mx-1 " + (i === currentPage ? "bg-blue-500 text-white" : "bg-white text-blue-500 hover:bg-blue-100");
                                                            btn.onclick = () => showPage(i);
                                                            pagination.appendChild(btn);
                                                        }
                                                    }

                                                    // Modal logic
                                                    function openModal(id, withdrawId) {
                                                        document.getElementById(id).classList.remove("hidden");
                                                        if (id === 'approveModal') {
                                                            document.getElementById('approveWithdrawalId').value = withdrawId;
                                                        } else if (id === 'rejectModal') {
                                                            document.getElementById('rejectWithdrawalId').value = withdrawId;
                                                        }
                                                    }

                                                    function closeModal(id) {
                                                        document.getElementById(id).classList.add("hidden");
                                                    }

                                                    // Search logic
                                                    searchInput.addEventListener("keyup", () => {
                                                        const keyword = searchInput.value.trim().toLowerCase();
                                                        filteredRows = rows.filter(row => {
                                                            const user = row.children[1]?.textContent.toLowerCase() || "";
                                                            const bank = row.children[3]?.textContent.toLowerCase() || "";
                                                            return user.includes(keyword) || bank.includes(keyword);
                                                        });
                                                        showPage(1);
                                                    });
                                                    showPage(1);
                                                    function confirmReject(withdrawId) {
                                                        Swal.fire({
                                                            title: 'Reject Withdrawal?',
                                                            text: "Are you sure you want to reject this withdrawal request?",
                                                            icon: 'warning',
                                                            showCancelButton: true,
                                                            confirmButtonColor: '#d33',
                                                            cancelButtonColor: '#3085d6',
                                                            confirmButtonText: 'Yes, reject it!'
                                                        }).then((result) => {
                                                            if (result.isConfirmed) {
                                                                // Submit a hidden form using JS
                                                                const form = document.createElement('form');
                                                                form.method = 'GET';
                                                                form.action = 'CreateWithrawQR';
                                                                const idInput = document.createElement('input');
                                                                idInput.type = 'hidden';
                                                                idInput.name = 'withdrawalId';
                                                                idInput.value = withdrawId;
                                                                const actionInput = document.createElement('input');
                                                                actionInput.type = 'hidden';
                                                                actionInput.name = 'action';
                                                                actionInput.value = 'reject';
                                                                form.appendChild(idInput);
                                                                form.appendChild(actionInput);
                                                                document.body.appendChild(form);
                                                                form.submit();
                                                            }
                                                        });
                                                    }
                                                    function confirmApprove(withdrawalId, bankCode, accountNumber, accountName, amount, addInfo, userID) {
                                                        Swal.fire({
                                                            title: 'Approve Withdrawal?',
                                                            text: "Are you sure you want to approve this withdrawal request?",
                                                            icon: 'question',
                                                            showCancelButton: true,
                                                            confirmButtonColor: '#16a34a',
                                                            cancelButtonColor: '#64748b',
                                                            confirmButtonText: 'Yes, approve it!',
                                                            cancelButtonText: 'Cancel'
                                                        }).then((result) => {
                                                            if (result.isConfirmed) {
                                                                fetch('CreateWithrawQR', {
                                                                    method: 'POST',
                                                                    headers: {
                                                                        'Content-Type': 'application/json'
                                                                    },
                                                                    body: JSON.stringify({
                                                                        withdrawalId,
                                                                        bankCode,
                                                                        accountNumber,
                                                                        accountName,
                                                                        amount,
                                                                        addInfo,
                                                                        userID
                                                                    })
                                                                })
                                                                        .then(res => res.text())
                                                                        .then(html => {
                                                                            const modalContainer = document.createElement('div');
                                                                            modalContainer.innerHTML = html;
                                                                            document.body.appendChild(modalContainer);

                                                                        })
                                                                        .catch(err => {
                                                                            Swal.fire('Error!', 'Failed to approve withdrawal.', 'error');
                                                                            console.error('QR Modal Error:', err);
                                                                        });
                                                            }
                                                        });
                                                    }



        </script>

    </body>
</html>
