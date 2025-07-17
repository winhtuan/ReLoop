document.addEventListener("DOMContentLoaded", function () {
    const dropdownSelected = document.getElementById("dropdownSelected");
    const categoryInput = document.getElementById("categoryInput");
    const form = document.getElementById("postForm");
    const modal = document.getElementById("categoryModal");
    const categoryList = document.getElementById("categoryList");
    const backButton = document.getElementById("backButton");
    const closeButton = document.getElementById("closeButton");
    let selectedCategoryId = null;
    let categoryStack = [];

    // Create container for form fields
    const formFieldsContainer = document.querySelector(".category-and-fields");
    const formFields = document.getElementById("formFields");

    const submitButton = document.getElementById("submitPostBtn");
    submitButton.classList.add("hidden");

    // Initial log to check data
    console.log("Initial window.categoryTree:", window.categoryTree);
    console.log("Initial window.categoryAttributes:", window.categoryAttributes);
    console.log("Initial window.categoryStateOptions:", window.categoryStateOptions);
    console.log("Initial window.contextPath:", window.contextPath); // Debug contextPath
    if (!window.categoryTree) {
        console.error("window.categoryTree is undefined or not loaded!");
    }
    if (!window.categoryAttributes) {
        console.error("window.categoryAttributes is undefined or not loaded!");
    }
    if (!window.categoryStateOptions) {
        console.error("window.categoryStateOptions is undefined or not loaded!");
    }

    function renderCategories(parentId) {
        categoryList.innerHTML = "";

        if (categoryStack.length > 0) {
            backButton.classList.remove("hidden");
            closeButton.style.display = "none"; // Ẩn nút × ở cấp 1 trở lên
        } else {
            backButton.classList.add("hidden");
            closeButton.style.display = "block"; // Hiển thị nút × ở cấp 0
        }

        const categories = (parentId === null) ? window.categoryTree[-1] || [] : window.categoryTree[parentId] || [];
        console.log("Rendering categories for parentId:", parentId, "Categories:", categories);
        if (!categories || categories.length === 0) {
            categoryList.innerHTML += "<li class='category-item'>No subcategories available</li>";
            return;
        }

        categories.forEach(function (category) {
            const item = document.createElement("li");
            item.classList.add("category-item");
            item.textContent = category.name;
            item.onclick = function () {
                const hasChildren = window.categoryTree.hasOwnProperty(category.categoryId) && window.categoryTree[category.categoryId].length > 0;
                console.log("Category:", category.name, "categoryId:", category.categoryId, "hasChildren:", hasChildren);
                if (hasChildren) {
                    categoryStack.push(category.categoryId);
                    renderCategories(category.categoryId); // Không đóng modal, chỉ render cấp tiếp theo
                } else {
                    selectedCategoryId = category.categoryId;
                    categoryInput.value = selectedCategoryId;
                    dropdownSelected.textContent = category.name;
                    modal.style.display = "none"; // Đóng modal khi chọn danh mục cuối
                    loadFormFields(selectedCategoryId);
                    submitButton.classList.remove("hidden");
                }
            };
            categoryList.appendChild(item);
        });
    }

    function loadFormFields(categoryId) {
        formFields.innerHTML = "";
        console.log("Loading form fields for categoryId:", categoryId);

        // State field with placeholder
        let stateField = `
            <select name="productState" id="productState" placeholder="Condition">
                <option value="" selected disabled hidden>Condition</option>
                ${getStateOptions(categoryId)}
            </select>
        `;
        formFields.innerHTML += stateField;

        const attributes = window.categoryAttributes && window.categoryAttributes[categoryId] ? window.categoryAttributes[categoryId] : [];
        console.log("Attributes for categoryId", categoryId, ":", attributes);
        if (attributes.length > 0) {
            attributes.forEach(attr => {
                console.log("Processing attribute:", attr);
                let inputHtml = '';
                if (attr.input_type === "select" && attr.options) {
                    let options = attr.options;
                    if (typeof options === 'string' && options) {
                        try {
                            options = JSON.parse(options);
                        } catch (e) {
                            console.error("Failed to parse options:", options, e);
                            options = [];
                        }
                    } else if (!Array.isArray(options)) {
                        options = [];
                    }
                    console.log("Options for", attr.name, ":", options);
                    inputHtml += `<select name="${attr.name}" id="${attr.name}" placeholder="${attr.name}" ${attr.is_required ? "required" : ""}>`;
                    inputHtml += `<option value="" selected disabled hidden>${attr.name}</option>`;
                    options.forEach(option => {
                        inputHtml += `<option value="${option}">${option}</option>`;
                    });
                    inputHtml += '</select>';
                } else {
                    inputHtml += `<input type="${attr.input_type || 'text'}" name="${attr.name}" id="${attr.name}" placeholder="${attr.name}" ${attr.is_required ? "required" : ""}>`;
                }
                formFields.innerHTML += inputHtml;
            });
        } else {
            console.warn("No attributes found for categoryId:", categoryId);
        }

        // Remaining fields with placeholder
        let remainingFields = `
            <input type="number" name="productPrice" id="productPrice" placeholder="Price" min="0" max="999999999.99" step="1000" required>
            <input type="text" name="productTitle" id="productTitle" placeholder="Ad Title" required>
            <textarea name="productDescription" id="productDescription" placeholder="Description" required></textarea>
            <input type="text" name="productLocation" id="productLocation" placeholder="Location" required>
        `;
        formFields.innerHTML += remainingFields;

        formFields.classList.remove("hidden");
    }

    function getStateOptions(categoryId) {
        let optionsHtml = '';
        let stateOptions = ['new', 'used', 'damaged'];

        if (window.categoryStateOptions && window.categoryStateOptions[categoryId]) {
            stateOptions = window.categoryStateOptions[categoryId];
        }
        if ([41, 42, 43, 44, 45, 46].includes(parseInt(categoryId))) {
            stateOptions = window.categoryStateOptions && window.categoryStateOptions[categoryId]
                    ? window.categoryStateOptions[categoryId]
                    : ['con non', 'trưởng thành'];
        }

        stateOptions.forEach(option => {
            const displayText = {
                'new': 'New',
                'used': 'Used',
                'damaged': 'Slightly Damaged',
                'con non': 'Young',
                'trưởng thành': 'Adult'
            }[option] || option;
            optionsHtml += `<option value="${option}">${displayText}</option>`;
        });
        return optionsHtml;
    }

    dropdownSelected.addEventListener("click", function () {
        if (modal.style.display !== "block") {
            modal.style.display = "block"; // Mở modal
            const startId = categoryStack[categoryStack.length - 1] || null;
            renderCategories(startId);
        }
    });

    backButton.addEventListener("click", function () {
        categoryStack.pop();
        const previousParentId = categoryStack[categoryStack.length - 1] || null;
        renderCategories(previousParentId);
        if (categoryStack.length === 0) {
            formFields.classList.add("hidden");
            submitButton.classList.add("hidden");
        }
    });

    closeButton.addEventListener("click", function () {
        if (categoryStack.length === 0) {
            modal.style.display = "none";
            categoryStack = [];
        }
    });

    document.addEventListener("click", function (e) {
        if (!dropdownSelected.contains(e.target) && !modal.contains(e.target) && categoryStack.length === 0) {
            modal.style.display = "none";
            categoryStack = [];
        }
    });

    // Hàm sendImage
    async function sendImage() {
        const files = document.getElementById("productImages").files;
        console.log("Files selected:", files.length);
        if (files.length === 0)
            return [];

        const formData = new FormData();
        for (const f of files) {
            formData.append("file", f);
        }
        try {
            const res = await fetch("/ReLoop/api/files", {method: "POST", body: formData});

            if (!res.ok) {
                const errTxt = await res.text();
                throw new Error(`HTTP ${res.status}: ${errTxt}`);
            }

            const {uploaded} = await res.json();
            if (!(Array.isArray(uploaded) && uploaded.length)) {
                console.warn("No images returned", uploaded);
                return [];
            }

            return uploaded.map(img => img.shareLink);
        } catch (err) {
            console.error("Upload failed", err);
            alert("Image upload failed, please try again.");
            return [];
        }
    }

    // Hàm submit form
    form.addEventListener("submit", async function (e) {
        e.preventDefault();
        // Đảm bảo luôn lấy selectedCategoryId mới nhất
        selectedCategoryId = categoryInput.value || selectedCategoryId;
        if (!selectedCategoryId) {
            alert("Please select a category!");
            return;
        }

        const formData = new FormData(form);
        const attributes = window.categoryAttributes && window.categoryAttributes[selectedCategoryId] ? window.categoryAttributes[selectedCategoryId] : [];

        const attributeValues = {};
        attributes.forEach(attr => attributeValues[attr.attr_id] = formData.get(attr.name));

        const imageUrls = await sendImage();
        if (imageUrls.length === 0 && document.getElementById("productImages").files.length > 0) {
            return;
        }

        fetch(window.contextPath + '/savePostServlet', {
            method: 'POST',
            credentials: 'include',
            body: JSON.stringify({
                categoryId: parseInt(selectedCategoryId),
                productState: formData.get('productState'),
                productPrice: formData.get('productPrice'),
                productTitle: formData.get('productTitle'),
                productDescription: formData.get('productDescription'),
                productLocation: formData.get('productLocation'),
                attributeValues: attributeValues,
                imageUrls: imageUrls
            }),
            headers: {'Content-Type': 'application/json'}
        }).then(response => {
            if (!response.ok)
                throw new Error(`HTTP ${response.status} - ${response.statusText}`);
            return response.json();
        }).then(data => {
            if (data && (data.success === true || data.success === "true" || data.status === "success")) {
                alert('Ad posted successfully!');
                window.location.href = window.contextPath + '/home';
            } else {
                alert(data && data.message ? data.message : 'Failed to post ad');
            }
        }).catch(error => {
            alert('An error occurred while posting the ad. Please check the server log.');
        });
    });
});