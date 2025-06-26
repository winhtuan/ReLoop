document.addEventListener("DOMContentLoaded", function () {
        const cards = document.querySelectorAll(".product-card-wrap");
        const perPage = 8;
        const total = cards.length;
        const totalPages = Math.ceil(total / perPage);

        const pagination = document.getElementById("pagination");
        const showPage = (page) => {
          // Hide all cards
          cards.forEach((card) => (card.style.display = "none"));
          // Show only cards of this page
          let start = (page - 1) * perPage;
          let end = start + perPage;
          for (let i = start; i < end && i < total; i++) {
            cards[i].style.display = "";
          }
        };

        // Render pagination
        const renderPagination = () => {
          let html = "";
          for (let i = 1; i <= totalPages; i++) {
            html += `<li class="page-item"><a class="page-link" href="#">${i}</a></li>`;
          }
          pagination.innerHTML = html;

          // Add active class to the first page
          pagination.querySelector("li").classList.add("active");
        };

        renderPagination();
        showPage(1);

        // Listen page click
        pagination.addEventListener("click", function (e) {
          if (e.target.tagName === "A") {
            e.preventDefault();
            let pageNum = parseInt(e.target.textContent);
            showPage(pageNum);

            // Set active class
            Array.from(pagination.children).forEach((li, idx) => {
              li.classList.toggle("active", idx === pageNum - 1);
            });
          }
        });
      });

      document.querySelector(".cat-btn-left").onclick = function () {
        document
          .getElementById("categoryBar")
          .scrollBy({ left: -220, behavior: "smooth" });
      };
      document.querySelector(".cat-btn-right").onclick = function () {
        document
          .getElementById("categoryBar")
          .scrollBy({ left: 220, behavior: "smooth" });
      };