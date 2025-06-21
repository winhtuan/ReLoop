<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
    <a href="${pageContext.request.contextPath}/create-post" class="item-btn-custom">
        <ion-icon name="document-text-outline" class="btn-icon"></ion-icon>
        <span>Up Post</span>
    </a>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/createPost.css">

</head>
<body>
    <div class="post-container">
        <h2 class="section-title">Products Images and Videos</h2>
        <p class="section-sub">
            More About <a href="#" style="color:#007bff;">ReLoop's posting rules</a>
        </p>

        <form action="${pageContext.request.contextPath}/upPost" method="post" enctype="multipart/form-data">
            <!-- Upload ảnh -->
            <label for="image-upload" class="image-upload">
                <input type="file" name="productImage" id="image-upload" accept="image/*" multiple>
                <span>Image must be at least 240x240 in size</span>
            </label>

            <!-- Danh mục -->
            <div class="custom-dropdown" id="categoryDropdown">
                <div class="dropdown-selected" id="dropdownSelected">Chọn danh mục</div>
                <div class="dropdown-menu hidden" id="dropdownMenu">
                    <div class="dropdown-header">
                        <button id="backButton" class="hidden">← Back</button>
                        <button id="closeButton">×</button>
                    </div>
                    <ul id="categoryList"></ul>
                </div>
            </div>
            <input type="hidden" name="categoryId" id="categoryInput">
        </form>
    </div>
    <script>
        window.categoryTree = JSON.parse('${categoriesJson}');
         console.log("Category Tree:", window.categoryTree);
    </script>
    <script src="${pageContext.request.contextPath}/js/categorySelect.js"></script>
</body>
</html>
