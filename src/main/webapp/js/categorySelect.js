document.addEventListener("DOMContentLoaded", function () {
    const dropdownSelected = document.getElementById("dropdownSelected");
    const dropdownMenu = document.getElementById("dropdownMenu");

    let selectedCategoryId = null;
    let categoryStack = []; // Lưu stack categoryId cha

    function renderCategories(parentId) {
        dropdownMenu.innerHTML = "";

        // Thêm nút trở về nếu đang không ở cấp root
        if (categoryStack.length > 0) {
            const backButton = document.createElement("div");
            backButton.classList.add("dropdown-item");
            backButton.textContent = "← Trở về";
            backButton.onclick = function () {
                categoryStack.pop();
                const previousParentId = categoryStack[categoryStack.length - 1] || null;
                renderCategories(previousParentId);
            };
            dropdownMenu.appendChild(backButton);
        }

        const categories = window.categoryTree[parentId];
        if (!categories || categories.length === 0) {
            dropdownMenu.innerHTML += "<div class='dropdown-item'>Không có danh mục con</div>";
            return;
        }

        categories.forEach(function (category) {
            const item = document.createElement("div");
            item.classList.add("dropdown-item");
            item.textContent = category.name;
            item.onclick = function () {
                const hasChildren = window.categoryTree.hasOwnProperty(category.categoryId);
                if (hasChildren) {
                    categoryStack.push(category.categoryId);
                    renderCategories(category.categoryId);
                } else {
                    selectedCategoryId = category.categoryId;
                    dropdownSelected.textContent = category.name;
                    dropdownMenu.classList.add("hidden");

                    // Gán vào input ẩn nếu có
                    const input = document.getElementById("categoryInput");
                    if (input) input.value = selectedCategoryId;
                }
            };
            dropdownMenu.appendChild(item);
        });
    }

    dropdownSelected.addEventListener("click", function () {
        dropdownMenu.classList.toggle("hidden");
        if (!dropdownMenu.classList.contains("hidden")) {
            // Luôn load từ đầu nếu chưa có stack
            const startId = categoryStack[categoryStack.length - 1] || null;
            renderCategories(startId);
        }
    });

    // Ẩn menu nếu click ngoài
    document.addEventListener("click", function (e) {
        if (!dropdownSelected.contains(e.target) && !dropdownMenu.contains(e.target)) {
            dropdownMenu.classList.add("hidden");
        }
    });
});
