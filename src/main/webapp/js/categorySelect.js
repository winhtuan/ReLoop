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

    // AI Moderation variables
    let titleModerationStatus = null;
    let descriptionModerationStatus = null;
    let isModerating = false;
    let titleModerationTimeout = null;
    let descriptionModerationTimeout = null;

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
            closeButton.style.display = "none"; // Hide close button at level 1 or higher
        } else {
            backButton.classList.add("hidden");
            closeButton.style.display = "block"; // Show close button at level 0
        }

        const categories = (parentId === null) ? window.categoryTree[-1] || [] : window.categoryTree[parentId] || [];

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
                if (hasChildren) {
                    categoryStack.push(category.categoryId);
                    renderCategories(category.categoryId); // Just render next level, don't close modal
                } else {
                    selectedCategoryId = category.categoryId;
                    categoryInput.value = selectedCategoryId;
                    dropdownSelected.textContent = category.name;
                    modal.style.display = "none"; // Close modal when selecting last category
                    loadFormFields(selectedCategoryId);
                    submitButton.classList.remove("hidden");
                }
            };
            categoryList.appendChild(item);
        });
    }

    function loadFormFields(categoryId) {
        formFields.innerHTML = "";

        // State field with placeholder
        let stateField = `
            <select name="productState" id="productState" placeholder="Condition">
                <option value="" selected disabled hidden>Condition</option>
                ${getStateOptions(categoryId)}
            </select>
        `;
        formFields.innerHTML += stateField;

        const attributes = window.categoryAttributes && window.categoryAttributes[categoryId] ? window.categoryAttributes[categoryId] : [];
        if (attributes.length > 0) {
            attributes.forEach(attr => {
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

        // Add AI moderation event listeners after fields are loaded
        setupAIModeration();
    }

    // AI Moderation functions
    function setupAIModeration() {
        const titleField = document.getElementById("productTitle");
        const descriptionField = document.getElementById("productDescription");

        if (titleField) {
            titleField.addEventListener("blur", function() {
                const text = this.value.trim();
                if (text.length > 0) {
                    // Clear any existing timeout
                    if (titleModerationTimeout) {
                        clearTimeout(titleModerationTimeout);
                    }
                    // Debounce moderation for 500ms
                    titleModerationTimeout = setTimeout(() => {
                        moderateField("title", text, titleField);
                    }, 500);
                } else {
                    // Clear status if field is empty
                    window.clearModerationStatus(titleField);
                    titleModerationStatus = null;
                }
            });

            // Clear status when user starts typing again
            titleField.addEventListener("input", function() {
                if (titleModerationStatus !== null) {
                    window.clearModerationStatus(titleField);
                    titleModerationStatus = null;
                }
                // Clear any pending moderation timeout
                if (titleModerationTimeout) {
                    clearTimeout(titleModerationTimeout);
                    titleModerationTimeout = null;
                }
            });
        }

        if (descriptionField) {
            descriptionField.addEventListener("blur", function() {
                const text = this.value.trim();
                if (text.length > 0) {
                    // Clear any existing timeout
                    if (descriptionModerationTimeout) {
                        clearTimeout(descriptionModerationTimeout);
                    }
                    // Debounce moderation for 500ms
                    descriptionModerationTimeout = setTimeout(() => {
                        moderateField("description", text, descriptionField);
                    }, 500);
                } else {
                    // Clear status if field is empty
                    window.clearModerationStatus(descriptionField);
                    descriptionModerationStatus = null;
                }
            });

            // Clear status when user starts typing again
            descriptionField.addEventListener("input", function() {
                if (descriptionModerationStatus !== null) {
                    window.clearModerationStatus(descriptionField);
                    descriptionModerationStatus = null;
                }
                // Clear any pending moderation timeout
                if (descriptionModerationTimeout) {
                    clearTimeout(descriptionModerationTimeout);
                    descriptionModerationTimeout = null;
                }
            });
        }
    }

    // Sử dụng hàm từ Hive.js
    async function moderateField(fieldType, text, fieldElement) {
        if (isModerating) {
            console.log("Moderation already in progress, skipping...");
            return;
        }

        // Check if text has changed since last moderation
        const currentText = fieldElement.value.trim();
        if (currentText !== text) {
            console.log("Text changed during moderation, skipping...");
            return;
        }

        isModerating = true;

        // Update moderation status variables
        await window.moderateField(fieldType, text, fieldElement, (status) => {
            if (fieldType === "title") {
                titleModerationStatus = status;
            } else {
                descriptionModerationStatus = status;
            }
            
            // Update submit button state based on moderation results
            updateSubmitButtonState();
        });

        isModerating = false;
    }

    // Function to check if moderation is required before submit
    function checkModerationBeforeSubmit() {
        const titleField = document.getElementById("productTitle");
        const descriptionField = document.getElementById("productDescription");
        
        if (titleField && titleField.value.trim().length > 0 && titleModerationStatus === null) {
            alert("Please wait for title moderation to complete or click outside the title field.");
            return false;
        }
        
        if (descriptionField && descriptionField.value.trim().length > 0 && descriptionModerationStatus === null) {
            alert("Please wait for description moderation to complete or click outside the description field.");
            return false;
        }
        
        return true;
    }

    // Update submit button state based on moderation results
    function updateSubmitButtonState() {
        const submitBtn = document.getElementById('submitPostBtn');
        if (!submitBtn) return;
        
        // Check if any field has high confidence unsafe content (rejected)
        const hasRejectedContent = titleModerationStatus === "rejected" || 
                                 descriptionModerationStatus === "rejected";
        
        // Check if any field has low confidence unsafe content (warn)
        const hasWarnContent = titleModerationStatus === "warn" || 
                             descriptionModerationStatus === "warn";
        
        if (hasRejectedContent) {
            // Disable submit button for rejected content
            submitBtn.disabled = true;
            submitBtn.style.background = '#dc3545';
            submitBtn.style.color = '#fff';
            submitBtn.style.borderColor = '#dc3545';
            submitBtn.title = 'Cannot submit: High confidence inappropriate content detected';
        } else if (hasWarnContent) {
            // Allow submit but with warning styling
            submitBtn.disabled = false;
            submitBtn.style.background = '#ffc107';
            submitBtn.style.color = '#000';
            submitBtn.style.borderColor = '#ffc107';
            submitBtn.title = 'Warning: Some potentially inappropriate content detected. Review before submitting.';
        } else {
            // Normal state
            submitBtn.disabled = false;
            submitBtn.style.background = '';
            submitBtn.style.color = '';
            submitBtn.style.borderColor = '';
            submitBtn.title = '';
        }
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
                    : ['Young', 'Adult'];
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
            modal.style.display = "block"; // Open modal
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

    // sendImage function
    async function sendImage() {
        const files = document.getElementById("productImages").files;
        if (files.length === 0)
            return [];

        const formData = new FormData();
        for (const f of files) {
            formData.append("file", f);
        }
        try {
    
            const res = await fetch("/ReLoop/api/files", {
                method: "POST", 
                body: formData
            });


            
            if (!res.ok) {
                const errTxt = await res.text();
                console.error('FilesServlet error response:', errTxt);
                throw new Error(`HTTP ${res.status}: ${errTxt}`);
            }

            const responseData = await res.json();

            
            const {uploaded} = responseData;
            if (!(Array.isArray(uploaded) && uploaded.length)) {
                console.warn("No images returned", uploaded);
                return [];
            }

            const imageUrls = uploaded.map(img => img.shareLink);

            return imageUrls;
        } catch (err) {
            console.error("Image upload failed", err);
            alert("Image upload failed, please try again.");
            return [];
        }
    }

    // Form submit handler - Modified to work with AI moderation
    form.addEventListener("submit", function (e) {
        // Check if AI moderation is still processing
        if (window.isAIModerationProcessing) {
            console.warn('⚠️ AI moderation still processing, blocking submit');
            e.preventDefault();
            alert('Please wait for image moderation to complete.');
            return false;
        }

        // Check if AI moderation failed
        if (window.aiModerationFailed) {
            console.warn('Form submission blocked due to AI moderation failure');
            e.preventDefault();
            if (submitButton) {
                submitButton.disabled = true;
            }
            return false;
        }

        // Use async IIFE to handle async operations
        (async () => {
        // Always get the latest selectedCategoryId
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

        // Check for moderation_status hidden input
        let moderationStatusInput = form.querySelector('input[name="moderation_status"]');
        let moderationStatus = moderationStatusInput ? moderationStatusInput.value : undefined;

        // Validate required fields
        const requiredFields = ['productState', 'productPrice', 'productTitle', 'productDescription', 'productLocation'];
        const missingFields = requiredFields.filter(field => !formData.get(field) || formData.get(field).trim() === '');
        
        if (missingFields.length > 0) {
            alert(`Please fill in all required fields: ${missingFields.join(', ')}`);
            return;
        }
        // --- AI Moderation: Check if content is safe ---
        const title = formData.get('productTitle').trim();
        const description = formData.get('productDescription').trim();

        // Check if moderation has been performed
        if (titleModerationStatus === "unsafe") {
            alert("Your ad title contains inappropriate content. Please revise.");
            return;
        }

        if (descriptionModerationStatus === "unsafe") {
            alert("Your ad description contains inappropriate content. Please revise.");
            return;
        }

        // If moderation hasn't been performed yet, perform it now
        if (titleModerationStatus === null || descriptionModerationStatus === null) {
            try {
                const [titleResult, descResult] = await Promise.all([
                    moderateText(title),
                    moderateText(description)
                ]);

                if (isUnsafe(titleResult)) {
                    alert("Your ad title contains inappropriate content. Please revise.");
                    return;
                }

                if (isUnsafe(descResult)) {
                    alert("Your ad description contains inappropriate content. Please revise.");
                    return;
                }
            } catch (err) {
                console.error("Hive text moderation failed:", err);
                alert("Could not verify content safety. Please try again later.");
                return;
            }
        }

        // Check moderation status before submitting
        if (titleModerationStatus === "rejected" || descriptionModerationStatus === "rejected") {
            alert("Cannot submit: High confidence inappropriate content detected. Please revise your text.");
            return false;
        }
        
        if (titleModerationStatus === "unsafe" || descriptionModerationStatus === "unsafe") {
            alert("Cannot submit: Inappropriate content detected. Please revise your text.");
            return false;
        }

        // Build payload
        let payload = {
            categoryId: parseInt(selectedCategoryId),
            productState: formData.get('productState'),
            productPrice: parseFloat(formData.get('productPrice')) || 0,
            productTitle: formData.get('productTitle').trim(),
            productDescription: formData.get('productDescription').trim(),
            productLocation: formData.get('productLocation').trim(),
            attributeValues: attributeValues,
            imageUrls: imageUrls
        };
        
        // Validate payload
        if (!payload.categoryId || isNaN(payload.categoryId)) {
            alert('Please select a valid category');
            return;
        }
        if (!payload.productPrice || payload.productPrice <= 0) {
            alert('Please enter a valid price');
            return;
        }
        if (!payload.productTitle || payload.productTitle.length < 3) {
            alert('Please enter a valid title (at least 3 characters)');
            return;
        }
        if (!payload.productDescription || payload.productDescription.length < 10) {
            alert('Please enter a valid description (at least 10 characters)');
            return;
        }
        if (!payload.productLocation || payload.productLocation.trim() === '') {
            alert('Please enter a valid location');
            return;
        }
        if (!payload.productState || payload.productState.trim() === '') {
            alert('Please select a valid state');
            return;
        }
        
        if (moderationStatus) {
            payload.moderation_status = moderationStatus;
        }
        


        try {
            const response = await fetch('/ReLoop/savePostServlet', {
                method: 'POST',
                credentials: 'include',
                body: JSON.stringify(payload),
                headers: {
                  'Content-Type': 'application/json',
                  'Accept': 'application/json'
                }
            });
            

            
            if (!response.ok) {
                const errorText = await response.text();
                console.error('Server error response:', errorText);
                throw new Error(`HTTP ${response.status} - ${errorText}`);
            }
            
            const data = await response.json();

            
            if (data && (data.success === true || data.success === "true" || data.status === "success")) {
                alert('Ad posted successfully!');
                window.location.href = '/ReLoop/home';
            } else {
                const errorMessage = data && data.message ? data.message : 'Failed to post ad';
                console.error('Server returned error:', errorMessage);
                alert(errorMessage);
            }
        } catch (error) {
            console.error('Error posting ad:', error);
            alert('An error occurred while posting the ad. Please check the server log.');
        }
        })();
    });
});