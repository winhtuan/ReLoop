<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Create Post</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/createPost.css">
        <link rel="icon" href="img/core-img/favicon.ico">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core-style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jsp_css/loader.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/category-menu.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/avatar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/notification.css">
    </head>
    <body>

        <!-- Page Preloder -->
        <div id="preloader">
            <div class="loader"></div>
        </div>

        <c:import url="/JSP/Home/Search.jsp" />

        <!-- ##### Main Content Wrapper Start ##### -->
        <div class="main-content-wrapper d-flex clearfix" style="display: flex; flex-direction: row; align-items: flex-start; width: 100vw;">
            <c:import url="/JSP/Home/Nav.jsp" />
            <div class="single-product-area section-padding-100-0">
                <c:import url="/JSP/UpPost/createPost.jsp" />
            </div>
        </div>

        <script>
            // Truyền contextPath qua JavaScript
            window.contextPath = "${pageContext.request.contextPath}";

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
        <script src="${pageContext.request.contextPath}/js/dropdown-handler.js"></script>
        <script src="${pageContext.request.contextPath}/js/notification.js"></script>
        <script src="${pageContext.request.contextPath}/js/search-menu.js"></script>
        <script src="${pageContext.request.contextPath}/js/googleVision.js"></script>
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
        <script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
        <script>
            const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
            window.addEventListener("load", function () {
                const preloader = document.getElementById("preloader");
                preloader.style.opacity = "0";
                preloader.style.pointerEvents = "none";
                setTimeout(() => preloader.style.display = "none", 500); // Ẩn hẳn sau fade out
            });
        </script>
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
                                const {files} = input;
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