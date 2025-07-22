<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Withdraw Modal -->
<div id="withdrawModal" class="fixed inset-0 z-50 hidden bg-black bg-opacity-40 flex items-center justify-center">
    <div class="bg-white rounded-2xl shadow-2xl border border-yellow-200 w-full max-w-2xl p-10 relative animate-fadeIn">
        <button id="closeWithdrawModal" type="button" class="absolute top-4 right-4 text-gray-400 hover:text-yellow-500 text-2xl focus:outline-none">
            <i class="fas fa-times"></i>
        </button>
        <h2 class="text-3xl font-bold text-yellow-700 mb-8 text-center flex items-center justify-center gap-2">
            <i className="fas fa-money-bill-wave text-yellow-400"></i> Withdraw Request
        </h2>
        <form id="withdrawForm" class="space-y-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="flex flex-col">
                    <label for="withdraw-bank" class="block text-base md:text-lg font-medium text-gray-700 mb-2 text-left">Bank</label>
                    <div class="relative w-full" id="bankDropdownWrapper">
                        <button id="bankSelected"
                                type="button"
                                class="w-full h-10 px-4 border border-yellow-200 rounded-lg bg-gray-50 text-left">
                            -- Select Bank --
                        </button>
                        <div id="bankOptions"
                             class="absolute z-10 w-full max-h-60 overflow-y-auto bg-white border border-gray-200 rounded-md mt-1">
                            <!-- Các tùy chọn ngân hàng sẽ được thêm bằng JavaScript -->
                        </div>
                    </div>
                    <input type="hidden" name="bankCode" id="bankCodeInput" />

                </div>
                <div class="flex flex-col">
                    <label for="withdraw-accountNumber" class="block text-base md:text-lg font-medium text-gray-700 mb-2 text-left">Account Number</label>
                    <input type="text" id="withdraw-accountNumber" name="accountNumber" required
                           class="w-full h-12 px-4 py-3 border border-yellow-200 rounded-lg bg-gray-50 focus:bg-white focus:border-yellow-400 focus:ring-2 focus:ring-yellow-200 transition text-lg">
                </div>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label for="withdraw-accountName" class="block text-base md:text-lg font-medium text-gray-700 mb-2 text-left">Account Name</label>
                    <input type="text" id="withdraw-accountName" name="accountName" required class="w-full h-12 px-4 py-3 border border-yellow-200 rounded-lg bg-gray-50 focus:bg-white focus:border-yellow-400 focus:ring-2 focus:ring-yellow-200 transition text-lg">
                </div>
                <div>
                    <label for="withdraw-amount" class="block text-base md:text-lg font-medium text-gray-700 mb-2 text-left">Amount (VND)</label>
                    <input type="number" id="withdraw-amount" name="amount" required min="1000" class="w-full h-12 px-4 py-3 border border-yellow-200 rounded-lg bg-gray-50 focus:bg-white focus:border-yellow-400 focus:ring-2 focus:ring-yellow-200 transition text-lg">
                </div>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="col-span-1 md:col-span-2">
                    <label for="withdraw-addInfo" class="block text-base md:text-lg font-medium text-gray-700 mb-2 text-left">Transfer content</label>
                    <textarea id="withdraw-addInfo" name="addInfo" rows="3" class="w-full px-4 py-2 border border-yellow-200 rounded-lg bg-gray-50 focus:bg-white focus:border-yellow-400 focus:ring-2 focus:ring-yellow-200 transition text-lg resize-none"></textarea>
                </div>
            </div>
            <button type="submit" class="w-full py-3 bg-yellow-400 text-white rounded-lg shadow hover:bg-yellow-500 font-semibold text-xl transition flex items-center justify-center gap-2">
                <i class="fas fa-paper-plane"></i> Submit
            </button>
        </form>
    </div>
</div>

<!-- Notification Modal -->
<div id="notifyModal" class="fixed inset-0 z-60 hidden bg-black bg-opacity-40 flex items-center justify-center">
    <div class="bg-white rounded-xl shadow-xl border border-yellow-300 w-full max-w-sm p-6 relative animate-fadeIn">
        <button id="closeNotifyModal" type="button" class="absolute top-3 right-3 text-gray-400 hover:text-yellow-500 text-xl focus:outline-none">
            <i class="fas fa-times"></i>
        </button>
        <div id="notifyModalContent" class="text-center text-lg text-gray-800 py-4">
            <!-- Nội dung thông báo sẽ được set bằng JS -->
        </div>
    </div>
</div>

<style>
    #withdraw-bank {
        display: block !important;
    }
    #notifyModal {
        z-index: 9999;
    }
</style>

