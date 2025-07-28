document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("withdrawModal");
    const closeBtn = document.getElementById("closeWithdrawModal");
    const form = document.getElementById("withdrawForm");
    const bankSelected = document.getElementById("bankSelected");
    const bankOptions = document.getElementById("bankOptions");
    const bankCodeInput = document.getElementById("bankCodeInput");
    const notifyModal = document.getElementById("notifyModal");
    const notifyModalContent = document.getElementById("notifyModalContent");
    const closeNotifyModal = document.getElementById("closeNotifyModal");

    // Show/hide modal
    window.showWithdrawModal = function () {
        modal.classList.remove("hidden");
    };
    closeBtn.onclick = () => modal.classList.add("hidden");
    window.onclick = function (event) {
        if (event.target === modal) {
            modal.classList.add("hidden");
        }
        if (event.target === notifyModal) {
            notifyModal.classList.add("hidden");
        }
    };
    if (closeNotifyModal) {
        closeNotifyModal.onclick = () => notifyModal.classList.add("hidden");
    }

    // Show notification modal
    function showNotifyModal(message) {
        notifyModalContent.innerHTML = message;
        notifyModal.classList.remove("hidden");
    }

    // Render bank options
    function renderBankOptions(banks) {
        let html = "";
        banks.forEach(bank => {
            html += `
                <div class="px-4 py-2 hover:bg-gray-100 cursor-pointer" data-code="${bank.code}">
                    (${bank.code}) ${bank.shortName}
                </div>
            `;
        });
        bankOptions.innerHTML = html;
    }

    // Attach click events to bank options
    function attachBankOptionEvents() {
        bankOptions.querySelectorAll("div[data-code]").forEach(option => {
            option.addEventListener("click", function () {
                bankSelected.textContent = this.textContent;
                bankCodeInput.value = this.dataset.code;
                bankOptions.classList.add("hidden");
            });
        });
    }

    // Fetch bank list and render
    async function loadBanks() {
        try {
            const response = await fetch('/ReLoop/GetBanksServlet');
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            if (!data.data || !Array.isArray(data.data) || data.data.length === 0) {
                bankOptions.innerHTML = '<div class="px-4 py-2 text-red-500">No banks found.</div>';
                return;
            }
            renderBankOptions(data.data);
            attachBankOptionEvents();
        } catch (error) {
            bankOptions.innerHTML = '<div class="px-4 py-2 text-red-500">Error loading banks.</div>';
            console.error('Error fetching banks:', error);
        }
    }

    // Toggle dropdown
    bankSelected.addEventListener("click", () => {
        bankOptions.classList.toggle("hidden");
    });
    document.addEventListener("click", function (e) {
        if (!bankSelected.contains(e.target) && !bankOptions.contains(e.target)) {
            bankOptions.classList.add("hidden");
        }
    });

    // Validate form
    function validateForm() {
        let valid = true;
        let messages = [];
        const bankCode = bankCodeInput.value.trim();
        const accountNumber = document.getElementById("withdraw-accountNumber").value.trim();
        const accountName = document.getElementById("withdraw-accountName").value.trim();
        const amount = parseFloat(document.getElementById("withdraw-amount").value);
        const addInfo = document.getElementById("withdraw-addInfo").value.trim();
        if (!bankCode) {
            valid = false;
            messages.push("Please select a bank.");
        }
        if (!/^[0-9]+$/.test(accountNumber) || accountNumber.length < 6 || accountNumber.length > 20) {
            valid = false;
            messages.push("Account number must be numeric and between 6 to 20 digits.");
        }
        if (accountName.length < 2) {
            valid = false;
            messages.push("Account name is required and must be at least 2 characters.");
        }
        if (isNaN(amount) || amount < 1000) {
            valid = false;
            messages.push("Amount must be at least 1,000 VND.");
        }
        if (addInfo.length === 0) {
            valid = false;
            messages.push("Transfer content must not be empty.");
        }
        if (addInfo.length > 255) {
            valid = false;
            messages.push("Transfer content must not exceed 255 characters.");
        }
        return { valid, messages };
    }

    form.addEventListener("submit", function (e) {
        const { valid, messages } = validateForm();
        if (!valid) {
            e.preventDefault();
            showNotifyModal(messages.join("<br>"));
        }
    });

    // Initial load
    loadBanks();
});
