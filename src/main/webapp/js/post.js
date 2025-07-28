document.addEventListener("DOMContentLoaded", function () {

    const cards = document.querySelectorAll(".product-card-wrap");
    const perPage = 8;
    const total = cards.length;
    const totalPages = Math.ceil(total / perPage);
    let currentPage = 1;

    const pagination = document.getElementById("pagination");

    const showPage = (page) => {
        currentPage = page;
        cards.forEach((card) => (card.style.display = "none"));
        let start = (page - 1) * perPage;
        let end = start + perPage;
        for (let i = start; i < end && i < total; i++) {
            cards[i].style.display = "";
        }
        renderPagination();
        document.querySelector(".headPost")?.scrollIntoView({behavior: "smooth"});

        // Cập nhật currentPage vào URL
        const url = new URL(window.location);
        url.searchParams.set("page", page);
        window.history.pushState({}, "", url);
        const urlInputs = document.querySelectorAll(".currentPageInput");
        urlInputs.forEach(input => {
            input.value = page;
        });

    };


    const renderPagination = () => {
        let html = "";

        html += `<li class="page-item ${currentPage === 1 ? "disabled" : ""}">
               <a class="page-link" href="#" data-page="1">First</a>
             </li>`;
        // Previous button
        html += `<li class="page-item ${currentPage === 1 ? "disabled" : ""}">
               <a class="page-link" href="#" data-page="${currentPage - 1}"><ion-icon name="arrow-back-outline"></ion-icon></a>
             </li>`;

        // Page numbers (hiển thị từ currentPage - 2 đến currentPage + 2)
        let startPage = Math.max(1, currentPage - 2);
        let endPage = Math.min(totalPages, currentPage + 2);

        for (let i = startPage; i <= endPage; i++) {
            html += `<li class="page-item ${i === currentPage ? "active" : ""}">
                 <a class="page-link" href="#" data-page="${i}">${i}</a>
               </li>`;
        }

        // Next button
        html += `<li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
               <a class="page-link" href="#" data-page="${currentPage + 1}"><ion-icon name="arrow-forward-outline"></ion-icon></a>
             </li>`;

        html += `<li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
               <a class="page-link" href="#" data-page="${totalPages}">Last</a>
             </li>`;

        pagination.innerHTML = html;
    };

    pagination.addEventListener("click", function (e) {
        if (e.target.tagName === "A" && !e.target.parentElement.classList.contains("disabled")) {
            e.preventDefault();
            const pageNum = parseInt(e.target.getAttribute("data-page"));
            if (!isNaN(pageNum)) {
                showPage(pageNum);
            }
        }
    });

//    const params = new URLSearchParams(window.location.search);
//    const initialPage = parseInt(params.get("page")) || 1;
//    showPage(initialPage);
});

document.addEventListener("DOMContentLoaded", function () {
    const categoryBar = document.getElementById("categoryBar");
    const btnLeft = document.getElementById("catBtnLeft");
    const btnRight = document.getElementById("catBtnRight");
    const scrollAmount = 150; // Số px cuộn mỗi lần (tùy chỉnh nếu muốn)

    btnLeft.addEventListener("click", function () {
        categoryBar.scrollBy({left: -scrollAmount, behavior: 'smooth'});
    });
    btnRight.addEventListener("click", function () {
        categoryBar.scrollBy({left: scrollAmount, behavior: 'smooth'});
    });
});


document.querySelectorAll('.add-to-cart').forEach(btn => {
    btn.addEventListener('click', function (e) {
        e.preventDefault();
        const productId = this.dataset.productid;

        fetch('s_addToCart', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                postID: productId,
                quantity: 1
            })
        })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Thêm vào giỏ hàng',
                            text: "Thêm vào giỏ hàng thành công",
                            confirmButtonText: 'OK'
                        });
                    } else {
                        console.error('Lỗi khi thêm giỏ hàng');
                    }
                });
    });
});

