<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Edit Product</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editProduct.css" />
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.33.0/js/vendor/jquery.ui.widget.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.33.0/js/jquery.iframe-transport.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.33.0/js/jquery.fileupload.js"></script>
        <script>
            window.contextPath = "${pageContext.request.contextPath}";
            window.productJson = ${productJson};
        </script>
        <script src="${pageContext.request.contextPath}/js/editProduct.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
    </head>
    <body class="bg-gray-50 min-h-screen">
        <div class="container mx-auto p-6">
            <h1 class="text-2xl font-bold mb-4">Edit Product</h1>
            <form id="editForm" class="space-y-4">
                <div>
                    <label for="productTitle" class="block text-sm font-medium">Title</label>
                    <input type="text" id="productTitle" name="productTitle" class="w-full p-2 border rounded" required value="${product.title}">
                </div>
                <div>
                    <label for="productPrice" class="block text-sm font-medium">Price ($)</label>
                    <input type="number" id="productPrice" name="productPrice" step="0.01" class="w-full p-2 border rounded" required value="${product.price}">
                </div>
                <div>
                    <label for="productDescription" class="block text-sm font-medium">Description</label>
                    <textarea id="productDescription" name="productDescription" class="w-full p-2 border rounded" required>${product.description}</textarea>
                </div>
                <div>
                    <label for="productLocation" class="block text-sm font-medium">Location</label>
                    <input type="text" id="productLocation" name="productLocation" class="w-full p-2 border rounded" required value="${product.location}">
                </div>
                <div>
                    <label for="productState" class="block text-sm font-medium">State</label>
                    <select id="productState" name="productState" class="w-full p-2 border rounded" required>
                        <c:choose>
                            <c:when test="${product.categoryId >= 41 && product.categoryId <= 46}">
                                <option value="con non" ${product.state == 'con non' ? 'selected' : ''}>Young</option>
                                <option value="trưởng thành" ${product.state == 'trưởng thành' ? 'selected' : ''}>Adult</option>
                            </c:when>
                            <c:otherwise>
                                <option value="mới" ${product.state == 'mới' ? 'selected' : ''}>New</option>
                                <option value="cũ" ${product.state == 'cũ' ? 'selected' : ''}>Used</option>
                                <option value="hư hỏng nhẹ" ${product.state == 'hư hỏng nhẹ' ? 'selected' : ''}>Slightly Damaged</option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                </div>
                <div>
                    <label for="productQuantity" class="block text-sm font-medium">Quantity</label>
                    <input type="number" id="productQuantity" name="productQuantity" class="w-full p-2 border rounded" required value="${not empty product.quantity ? product.quantity : 1}">
                </div>
                <!-- Hiển thị động các thuộc tính của category -->
                <c:if test="${not empty categoryAttributes}">
                    <c:forEach var="attr" items="${categoryAttributes}">
                        <div>
                            <label for="attr_${attr.attributeId}" class="block text-sm font-medium">${attr.name}</label>
                            <c:set var="attrValue" value="" />
                            <c:forEach var="attrVal" items="${attributeValues}">
                                <c:if test="${attrVal.attributeId == attr.attributeId}">
                                    <c:set var="attrValue" value="${attrVal.value}" />
                                </c:if>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${attr.inputType == 'select'}">
                                    <select id="attr_${attr.attributeId}" name="attr_${attr.attributeId}" class="w-full p-2 border rounded">
                                        <c:if test="${empty attrValue}">
                                            <option value="" selected disabled style="color: #888;">No information</option>
                                        </c:if>
                                        <%-- Loại bỏ dấu ngoặc và ngoặc kép từ options --%>
                                        <c:set var="cleanOptions" value="${fn:replace(attr.options, '[', '')}" />
                                        <c:set var="cleanOptions" value="${fn:replace(cleanOptions, ']', '')}" />
                                        <c:set var="cleanOptions" value="${fn:replace(cleanOptions, '\"', '')}" />
                                        <c:forEach var="option" items="${fn:split(cleanOptions, ',')}">
                                            <c:set var="trimmedOption" value="${fn:trim(option)}" />
                                            <c:if test="${not empty trimmedOption}">
                                                <option value="${trimmedOption}" ${trimmedOption == attrValue ? 'selected' : ''}>${trimmedOption}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <input type="${attr.inputType}" id="attr_${attr.attributeId}" name="attr_${attr.attributeId}" class="w-full p-2 border rounded" value="${attrValue}">
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${empty categoryAttributes}">
                    <p class="text-red-500">No attributes available for this category. Category ID: ${product.categoryId}</p>
                </c:if>
                <div>
                    <label class="block text-sm font-medium">Images</label>
                    <input type="file" id="fileupload" name="file" multiple>
                    <div id="imageContainer" class="flex flex-wrap mt-2">
                        <c:forEach var="image" items="${images}">
                            <div class="image-item relative">
                                <img src="${image.imageUrl}" alt="Product Image" class="w-24 h-24 object-cover mr-2 mb-2">
                                <button class="remove-image absolute top-0 right-0 bg-red-500 text-white px-1 py-1 rounded" data-url="${image.imageUrl}">x</button>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div class="flex justify-end gap-4 mt-6">
                    <button type="button" id="cancelButton" class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400">Cancel</button>
                    <button type="button" id="saveButton" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Save</button>
                </div>
            </form>
        </div>
    </body>
</html>