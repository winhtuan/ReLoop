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
    renderPagination(); // Cập nhật lại nút khi chuyển trang
    document.querySelector(".headPost")?.scrollIntoView({ behavior: "smooth" });

  };

  const renderPagination = () => {
    let html = "";

    // Previous button
    html += `<li class="page-item ${currentPage === 1 ? "disabled" : ""}">
               <a class="page-link" href="#" data-page="${currentPage - 1}">Pre</a>
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
               <a class="page-link" href="#" data-page="${currentPage + 1}">Next</a>
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

  showPage(1);
});
