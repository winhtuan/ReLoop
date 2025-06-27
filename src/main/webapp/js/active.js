(function ($) {
    "use strict";

    var $window = $(window);

    // :: 1.0 Masonary Gallery Active Code

    var proCata = $(".amado-pro-catagory");
    var singleProCata = ".single-products-catagory";

    if ($.fn.imagesLoaded) {
        proCata.imagesLoaded(function () {
            proCata.isotope({
                itemSelector: singleProCata,
                percentPosition: true,
                masonry: {
                    columnWidth: singleProCata,
                },
            });
        });
    }

    // :: 2.1 Search Active Code
    var amadoSearch = $(".search-nav");
    var searchClose = $(".search-close");
    var searchWrapper = $(".search-wrapper");

    // Toggle search wrapper
    amadoSearch.on("click", function (e) {
        e.stopPropagation(); // Ngăn chặn sự kiện lan truyền lên phần tử cha
        $("body").toggleClass("search-wrapper-on");
    });

    // Close search on click of close button
    searchClose.on("click", function () {
        const resultContainer = document.getElementById("productResults");
        $("body").removeClass("search-wrapper-on");
        $("#search").val("");
        resultContainer.innerHTML = "";
        resultContainer.style.display = "none";
    });

    // Close search when clicking outside of it
    $(document).on("click", function (e) {
        if (!searchWrapper.is(e.target) && searchWrapper.has(e.target).length === 0) {
            $("body").removeClass("search-wrapper-on");
        }
    });


    // :: 2.2 Mobile Nav Active Code
    var amadoMobNav = $(".amado-navbar-toggler");
    var navClose = $(".nav-close");

    amadoMobNav.on("click", function () {
        $(".header-area").toggleClass("bp-xs-on");
    });

    navClose.on("click", function () {
        $(".header-area").removeClass("bp-xs-on");
    });

    // :: 3.0 ScrollUp Active Code
    if ($.fn.scrollUp) {
        $.scrollUp({
            scrollSpeed: 1000,
            easingType: "easeInOutQuart",
            scrollText: '<i class="fa fa-angle-up" aria-hidden="true"></i>',
        });
    }

    // :: 4.0 Sticky Active Code
    $window.on("scroll", function () {
        if ($window.scrollTop() > 0) {
            $(".header_area").addClass("sticky");
        } else {
            $(".header_area").removeClass("sticky");
        }
    });

    // :: 5.0 Nice Select Active Code
    if ($.fn.niceSelect) {
        $("select").niceSelect();
    }

    // :: 6.0 Magnific Active Code
    if ($.fn.magnificPopup) {
        $(".gallery_img").magnificPopup({
            type: "image",
        });
    }

    // :: 7.0 Nicescroll Active Code
    if ($.fn.niceScroll) {
        $(".cart-table table").niceScroll();
    }

    // :: 8.0 wow Active Code
    if ($window.width() > 767) {
        new WOW().init();
    }

    // :: 9.0 Tooltip Active Code
    if ($.fn.tooltip) {
        $('[data-toggle="tooltip"]').tooltip();
    }

    // :: 10.0 PreventDefault a Click
    $("a[href='#']").on("click", function ($) {
        $.preventDefault();
    });

    // :: 11.0 Slider Range Price Active Code
    $(".slider-range-price").each(function () {
        var min = jQuery(this).data("min");
        var max = jQuery(this).data("max");
        var unit = jQuery(this).data("unit");
        var value_min = jQuery(this).data("value-min");
        var value_max = jQuery(this).data("value-max");
        var label_result = jQuery(this).data("label-result");
        var t = $(this);
        $(this).slider({
            range: true,
            min: min,
            max: max,
            values: [value_min, value_max],
            slide: function (event, ui) {
                var result =
                        label_result +
                        " " +
                        unit +
                        ui.values[0] +
                        " - " +
                        unit +
                        ui.values[1];
                console.log(t);
                t.closest(".slider-range").find(".range-price").html(result);
            },
        });
    });
})(jQuery);

document.addEventListener("DOMContentLoaded", function () {
    // Xử lý notificationBox
    const notificationLink = document.getElementById("notificationLink");
    const notificationBox = document.getElementById("notificationBox");

    notificationLink.addEventListener("click", function (e) {
        e.preventDefault();
        notificationBox.style.display =
                notificationBox.style.display === "block" ? "none" : "block";
    });

    // Đóng notificationBox nếu click ra ngoài
    document.addEventListener("click", function (e) {
        const container = document.getElementById("notificationContainer");
        if (!container.contains(e.target)) {
            notificationBox.style.display = "none";
        }
    });

    // Giới hạn hiển thị 4 item và xử lý "Xem thêm"
    const items = document.querySelectorAll(".notification-item");
    const seeMore = document.getElementById("seeMoreNotifications");

    if (items.length <= 4) {
        if (seeMore)
            seeMore.style.display = "none";
    } else {
        items.forEach((item, index) => {
            if (index >= 4)
                item.style.display = "none";
        });

        seeMore.addEventListener("click", function (e) {
            e.preventDefault();
            items.forEach((item) => (item.style.display = "block"));
            seeMore.style.display = "none"; // Ẩn nút sau khi mở rộng
        });
    }

    // Xử lý favouriteBox
//    const favouriteLink = document.getElementById("favouriteLink");
//    const favouriteBox = document.getElementById("favouriteBox");
//
//    favouriteLink.addEventListener("click", function (e) {
//        e.preventDefault();
//        favouriteBox.style.display =
//                favouriteBox.style.display === "block" ? "none" : "block";
//    });

    // Đóng favouriteBox nếu click ra ngoài
    document.addEventListener("click", function (e) {
        const container = document.getElementById("favouriteContainer");
        if (!container.contains(e.target)) {
            favouriteBox.style.display = "none";
        }
    });
});

/*=============== SHOW HIDE MODAL ===============*/

const joinInBtn = document.getElementById('joinInBtn');
const joinInBtnDiv = document.getElementById('join-in-btn');
const modal = document.querySelector('.modal');
const menu = document.getElementById('menu');
const isLoggedIn = document.querySelector('.nav-brand') !== null;

// Function to open modal

function openModal() {
    modal.classList.add('show');
}
function closeModal() {
    modal.classList.remove('show');
}

// Handle click for non-logged-in state
if (!isLoggedIn && joinInBtn) {
    joinInBtn.addEventListener('click', (e) => {
        e.preventDefault();
        openModal();
    });
}

// Handle hover for logged-in state
document.addEventListener("DOMContentLoaded", function () {
    const joinBtn = document.getElementById("join-in-btn");
    const menu = document.getElementById("menu");

    joinBtn.addEventListener("mouseenter", () => {
        if (isLoggedIn) {
            menu.classList.add("show");
        }
    });

    joinBtn.addEventListener("mouseleave", () => {
        if (isLoggedIn) {
            menu.classList.remove("show");
        }
    });
});

// Close modal when clicking outside
window.addEventListener('click', (e) => {
    if (e.target === modal) {
        closeModal();
    }
});

