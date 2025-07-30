$(document).ready(function () {
    let productData = window.productJson;

    // Điền thông tin sản phẩm vào form
    $('#productTitle').val(productData.title || '');
    $('#productPrice').val(productData.price || 0);
    $('#productDescription').val(productData.description || '');
    $('#productLocation').val(productData.location || '');
    if (productData.state) {
        $('#productState').val(productData.state); // Gán nếu có giá trị
    }
    $('#productQuantity').val(productData.quantity);

    console.log("productData.state: ", productData.state); // Debug
    console.log("Selected state: ", $('#productState').val()); // Debug sau khi gán
    $('#productQuantity').val(productData.quantity); // Loại bỏ || 1, lấy trực tiếp từ database

    // Cấu hình upload ảnh sử dụng FilesServlet
    $('#fileupload').fileupload({
        url: window.contextPath + '/api/files',
        dataType: 'json',
        paramName: 'file',
        add: function (e, data) {
            data.submit();
        },
        done: function (e, data) {
            if (data.result.uploaded && data.result.uploaded.length > 0) {
                const uploadedImage = data.result.uploaded[0];
                $('#imageContainer').append(`
                    <div class="image-item relative">
                        <img src="${uploadedImage.shareLink}" alt="Product Image" class="w-24 h-24 object-cover mr-2 mb-2">
                        <button class="remove-image absolute top-0 right-0 bg-red-500 text-white px-1 py-1 rounded" data-url="${uploadedImage.shareLink}">x</button>
                    </div>
                `);
                productData.images.push({imageUrl: uploadedImage.shareLink, isPrimary: false});
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Upload Failed',
                    text: 'Failed to upload image.',
                    confirmButtonText: 'OK',
                    customClass: {
                        confirmButton: 'bg-blue-500 text-white hover:bg-blue-600'
                    }
                });
            }
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Upload Error',
                text: 'Error uploading image. Please try again.',
                confirmButtonText: 'OK',
                customClass: {
                    confirmButton: 'bg-blue-500 text-white hover:bg-blue-600'
                }
            });
        }
    });

    // Xóa ảnh khi nhấp vào nút "x"
    $(document).on('click', '.remove-image', function () {
        const url = $(this).data('url');
        $(this).parent().remove();
        productData.images = productData.images.filter(img => img.imageUrl !== url);
    });

    // Xử lý nút Cancel
    $('#cancelButton').click(function () {
        window.location.href = window.contextPath + '/s_manageProduct';
    });

    // Xử lý nút Save
    $('#saveButton').click(function () {
        const updatedProduct = {
            productId: productData.productId,
            userId: productData.userId,
            categoryId: productData.categoryId,
            title: $('#productTitle').val() || '',
            price: parseInt($('#productPrice').val()) || 0,
            description: $('#productDescription').val() || '',
            location: $('#productLocation').val() || '',
            state: $('#productState').val() || '',
            quantity: parseInt($('#productQuantity').val()) || 0,
            images: productData.images,
            attributeValues: []
        };

        // Thu thập tất cả các trường attribute từ DOM
        $('[id^="attr_"]').each(function () {
            const attrId = $(this).attr('id').replace('attr_', '');
            const value = $(this).val() || '';
            if (value.trim() !== '') {
                updatedProduct.attributeValues.push({
                    attributeId: parseInt(attrId),
                    value: value.trim()
                });
            }
        });

        console.log("Updated attributeValues: ", updatedProduct.attributeValues); // Debug

        $.ajax({
            url: window.contextPath + '/UpdateProductServlet',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(updatedProduct),
            success: function (response) {
                if (response.success) {
                    // Hiển thị thông báo thành công với SweetAlert2
                    Swal.fire({
                        icon: 'success',
                        title: 'Success!',
                        text: 'Product updated successfully!',
                        confirmButtonText: 'OK',
                        timer: 2000, // Tự động đóng sau 2 giây
                        timerProgressBar: true,
                        customClass: {
                            confirmButton: 'bg-green-500 text-white hover:bg-green-600',
                            popup: 'rounded-lg shadow-lg'
                        },
                        didClose: () => {
                            // Chuyển hướng sau khi đóng thông báo
                            window.location.href = window.contextPath + '/s_manageProduct';
                        }
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = window.contextPath + '/s_manageProduct';
                        }
                    });
                    // Cập nhật danh sách (nếu cần)
                    updateProductInList(updatedProduct);
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Update Failed',
                        text: 'Failed to update product: ' + response.message,
                        confirmButtonText: 'OK',
                        customClass: {
                            confirmButton: 'bg-blue-500 text-white hover:bg-blue-600'
                        }
                    });
                }
            },
            error: function (xhr, status, error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Update Error',
                    text: 'Error updating product. Status: ' + status + ', Error: ' + error,
                    confirmButtonText: 'OK',
                    customClass: {
                        confirmButton: 'bg-blue-500 text-white hover:bg-blue-600'
                    }
                });
                console.error('AJAX Error:', xhr.responseText);
            }
        });
    });

    // Hàm cập nhật sản phẩm trong danh sách mà không reload
    function updateProductInList(product) {
        const productList = window.parent.productList || window.opener.productList;
        if (productList) {
            const index = productList.findIndex(p => p.productId === product.productId);
            if (index !== -1) {
                productList[index] = product;
                if (window.parent.updateView) {
                    window.parent.updateView();
                } else if (window.opener && window.opener.updateView) {
                    window.opener.updateView();
                }
            }
        }
    }
});