<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="single-product-area section-padding-0" style="position: absolute; top: 0; right: 0; width: 100%; max-width: 700px; padding: 10px;">
    <div class="container bg-white p-4" style="border-radius: 16px; box-shadow: 0 4px 24px rgba(0,0,0,0.08);">
        <div class="section-heading text-left mb-30">
            <h2>Post a New Product</h2>
            <p class="section-sub mb-15">
                More About <a href="${pageContext.request.contextPath}/JSP/UpPost/postingRule.jsp" style="color:#fbb710;">ReLoop's posting rules</a>
            </p>
        </div>
        <form action="${pageContext.request.contextPath}/savePostServlet" method="post" enctype="multipart/form-data" id="postForm" onsubmit="return false;">
            <div class="upload-and-category row" style="gap: 0;">
                <div class="image-upload-container col-md-5 mb-3 mb-md-0">
                    <label for="productImages" class="image-upload">
                        <input type="file" name="productImages" id="productImages" accept="image/*" multiple>
                        <span>Image must be at least 240x240 in size</span>
                    </label>
                    <div id="imagePreview" class="image-preview"></div>
                    <label for="productImages" class="upload-add" id="uploadAdd" style="display: none;">+</label>
                </div>
                <div class="category-and-fields col-md-7">
                    <div class="custom-dropdown mb-3" id="categoryDropdown">
                        <div class="dropdown-selected" id="dropdownSelected">Choose Category</div>
                    </div>
                    <div id="formFields" class="hidden"></div>
                </div>
            </div>
            <input type="hidden" name="categoryId" id="categoryInput">
            <div class="d-flex justify-content-end mt-4">
                <button type="submit" class="amado-btn" id="submitPostBtn">Post Ad</button>
            </div>
        </form>
        <div id="categoryModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <button id="backButton" class="hidden">
                        <ion-icon name="arrow-back-outline"></ion-icon>
                    </button>
                    <button id="closeButton">
                        <ion-icon name="close-outline"></ion-icon>
                    </button>
                </div>
                <ul id="categoryList" class="category-list"></ul>
            </div>
        </div>
    </div>
</div>

