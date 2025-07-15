<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Create Post</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/createPost.css">
    </head>
    <body>
        <div class="post-container">
            <h2 class="section-title">Products Images and Videos</h2>
            <p class="section-sub">
                More About <a href="#" style="color:#007bff;">ReLoop's posting rules</a>
            </p>

            <form action="${pageContext.request.contextPath}/savePostServlet" method="post" enctype="multipart/form-data" id="postForm">
                <!-- Container flex cho upload và dropdown -->
                <div class="upload-and-category">
                    <!-- Upload ảnh và preview -->
                    <div class="image-upload-container">
                        <label for="productImages" class="image-upload">
                            <input type="file" name="productImages" id="productImages" accept="image/*" multiple>
                            <span>Image must be at least 240x240 in size</span>
                        </label>
                        <div id="imagePreview" class="image-preview"></div>
                        <label for="productImages" class="upload-add" id="uploadAdd" style="display: none;">+</label>
                    </div>

                    <!-- Container cho danh mục và form fields -->
                    <div class="category-and-fields">
                        <div class="custom-dropdown" id="categoryDropdown">
                            <div class="dropdown-selected" id="dropdownSelected">Choose</div>
                        </div>
                        <div id="formFields" class="hidden"></div>
                    </div>
                </div>
                <input type="hidden" name="categoryId" id="categoryInput">
            </form>

            <!-- Modal cho menu danh mục -->
            <div id="categoryModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <button id="backButton" class="hidden">←</button>
                        <button id="closeButton">×</button>
                    </div>
                    <ul id="categoryList" class="category-list"></ul>
                </div>
            </div>
        </div>

        <script>
            // Truyền contextPath qua JavaScript
            window.contextPath = "${pageContext.request.contextPath}";
            console.log("Raw categoriesJson:", '${categoriesJson}');
            console.log("Raw categoryAttributesJson:", '${categoryAttributesJson}');
            console.log("Raw categoryStateOptionsJson:", '${categoryStateOptionsJson}');

            try {
                window.categoryTree = JSON.parse('${categoriesJson}' || '{}');
                window.categoryAttributes = JSON.parse('${categoryAttributesJson}' || '{}');
                window.categoryStateOptions = JSON.parse('${categoryStateOptionsJson}' || '{}');
            } catch (e) {
                console.error("JSON parse error:", e);
                window.categoryTree = {};
                window.categoryAttributes = {};
                window.categoryStateOptions = {};
            }

            console.log("Parsed window.categoryTree:", window.categoryTree);
            console.log("Parsed window.categoryAttributes:", window.categoryAttributes);
            console.log("Parsed window.categoryStateOptions:", window.categoryStateOptions);
            if (Object.keys(window.categoryTree).length === 0) {
                console.error("No category tree loaded!");
            }
            if (Object.keys(window.categoryAttributes).length === 0) {
                console.warn("No category attributes loaded! Using empty object.");
            }
            if (Object.keys(window.categoryStateOptions).length === 0) {
                console.warn("No category state options loaded! Using default options.");
            }
        </script>
        <script src="${pageContext.request.contextPath}/js/categorySelect.js"></script>
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>

        <script>
            // Thêm preview ảnh và xử lý upload
            document.getElementById('productImages').addEventListener('change', function (e) {
                const files = e.target.files;
                if (files.length > 0) {
                    displayImages(files);
                }
            });

            let draggedIndex = null;

            function displayImages(files) {
                const preview = document.getElementById('imagePreview');
                const uploadLabel = document.querySelector('.image-upload');
                const uploadAdd = document.getElementById('uploadAdd');

                if (preview.children.length === 0) {
                    uploadLabel.style.display = 'none'; // Ẩn label lớn khi có ảnh đầu tiên
                    uploadAdd.style.display = 'flex'; // Hiển thị label nhỏ khi có ảnh
                }

                for (let i = 0; i < files.length; i++) {
                    const file = files[i];
                    if (file.type.startsWith('image/')) {
                        const reader = new FileReader();
                        reader.onload = function (e) {
                            const imgContainer = document.createElement('div');
                            imgContainer.style.position = 'relative';
                            imgContainer.draggable = preview.children.length >= 2; // Chỉ cho phép kéo thả khi có 2 ảnh trở lên

                            const img = document.createElement('img');
                            img.src = e.target.result;
                            img.style.maxWidth = '200px';
                            img.style.margin = '0';

                            const deleteBtn = document.createElement('button');
                            deleteBtn.className = 'image-delete';
                            deleteBtn.innerHTML = '×';
                            deleteBtn.onclick = function () {
                                imgContainer.remove();
                                if (preview.children.length === 0) {
                                    uploadLabel.style.display = 'flex'; // Hiển thị lại label lớn nếu không còn ảnh
                                    uploadAdd.style.display = 'none'; // Ẩn label nhỏ khi không còn ảnh
                                } else if (preview.children.length < 2) {
                                    // Vô hiệu hóa kéo thả nếu còn ít hơn 2 ảnh
                                    preview.querySelectorAll('div').forEach(child => {
                                        child.draggable = false;
                                    });
                                }
                                // Cập nhật input file
                                const dt = new DataTransfer();
                                const input = document.getElementById('productImages');
                                const { files } = input;
                                for (let i = 0; i < files.length; i++) {
                                    const file = files[i];
                                    if (!imgContainer.contains(img)) {
                                        dt.items.add(file);
                                    }
                                }
                                input.files = dt.files;
                            };

                            // Kéo thả
                            imgContainer.addEventListener('dragstart', function (e) {
                                draggedIndex = Array.from(preview.children).indexOf(imgContainer);
                                e.dataTransfer.effectAllowed = 'move';
                                e.dataTransfer.setData('text/plain', draggedIndex);
                            });

                            imgContainer.addEventListener('dragover', function (e) {
                                e.preventDefault();
                                e.dataTransfer.dropEffect = 'move';
                            });

                            imgContainer.addEventListener('drop', function (e) {
                                e.preventDefault();
                                const targetIndex = Array.from(preview.children).indexOf(imgContainer);
                                if (draggedIndex !== null && draggedIndex !== targetIndex) {
                                    const previewChildren = Array.from(preview.children);
                                    const draggedElement = previewChildren[draggedIndex];
                                    const targetElement = previewChildren[targetIndex];
                                    // Hoán đổi vị trí bằng cách chèn trước hoặc sau
                                    if (draggedIndex < targetIndex) {
                                        preview.insertBefore(draggedElement, targetElement.nextSibling);
                                    } else {
                                        preview.insertBefore(draggedElement, targetElement);
                                    }
                                    // Cập nhật index
                                    draggedIndex = null;
                                }
                            });

                            imgContainer.appendChild(img);
                            imgContainer.appendChild(deleteBtn);
                            preview.appendChild(imgContainer);

                            // Kích hoạt kéo thả nếu có 2 ảnh trở lên
                            if (preview.children.length >= 2) {
                                preview.querySelectorAll('div').forEach(child => {
                                    child.draggable = true;
                                });
                            }
                        };
                        reader.readAsDataURL(file);
                    }
                }
            }
        </script>
    </body>
</html>