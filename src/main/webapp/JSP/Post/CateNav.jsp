<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="category-bar-container bg-light p-3 rounded mb-4 position-relative">
    <div class="category-bar-title mb-3"><strong>Explore the catalog</strong></div>
    <button class="cat-btn-left btn btn-light rounded-circle shadow-sm" type="button" aria-label="Prev">
        <svg width="20" height="20" fill="currentColor" viewBox="0 0 20 20"><path d="M13 17l-5-5 5-5" stroke="currentColor" stroke-width="2" fill="none"/></svg>
    </button>
    <div class="category-bar d-flex align-items-center" id="categoryBar" style="gap: 32px; overflow-x: auto; scroll-behavior: smooth; padding: 0 48px 8px 48px; white-space: nowrap;">
        <a href="products?category=Electronics" class="category-item text-center text-decoration-none text-dark">
            <img src="img/cate-img/Electronics.jpg" class="rounded-circle mb-1" width="64" height="64" alt="" />
            <div style="font-size: 14px">Electronics</div>
        </a>
        <a href="products?category=Vehicles" class="category-item text-center text-decoration-none text-dark">
            <img src="img/cate-img/Vehicles.jpeg" class="rounded-circle mb-1" width="64" height="64" alt="" />
            <div style="font-size: 14px">Vehicles</div>
        </a>
        <a href="products?category=Home_Appliances" class="category-item text-center text-decoration-none text-dark">
            <img src="img/cate-img/Home_Appliances.png" class="rounded-circle mb-1" width="64" height="64" alt="" />
            <div style="font-size: 14px">Home Appliances</div>
        </a>
        <a href="products?category=Furniture" class="category-item text-center text-decoration-none text-dark">
            <img src="img/cate-img/Furniture.png" class="rounded-circle mb-1" width="64" height="64" alt="" />
            <div style="font-size: 14px">Furniture</div>
        </a>
        <a href="products?category=Pet" class="category-item text-center text-decoration-none text-dark">
            <img src="img/cate-img/Pet.png" class="rounded-circle mb-1" width="64" height="64" alt="" />
            <div style="font-size: 14px">Pet</div>
        </a>
        <a href="products?category=Fashion" class="category-item text-center text-decoration-none text-dark">
            <img src="img/cate-img/Fashion.jpg" class="rounded-circle mb-1" width="64" height="64" alt="" />
            <div style="font-size: 14px">Fashion</div>
        </a>
        <a href="products?category=Jewelry" class="category-item text-center text-decoration-none text-dark">
            <img src="img/cate-img/Jewelry.png" class="rounded-circle mb-1" width="64" height="64" alt="" />
            <div style="font-size: 14px">Jewelry</div>
        </a>
        <a href="products?category=Digital_Goods" class="category-item text-center text-decoration-none text-dark">
            <img src="img/cate-img/Digital_Goods.jpg" class="rounded-circle mb-1" width="64" height="64" alt="" />
            <div style="font-size: 14px">Digital Goods</div>
        </a>
        <a href="products?category=Books" class="category-item text-center text-decoration-none text-dark">
            <img src="img/cate-img/Book.jpg" class="rounded-circle mb-1" width="64" height="64" alt="" />
            <div style="font-size: 14px">Books</div>
        </a>
        <a href="products?category=Giveaway" class="category-item text-center text-decoration-none text-dark">
            <img src="img/cate-img/Free.png" class="rounded-circle mb-1" width="64" height="64" alt="" />
            <div style="font-size: 14px">Giveaway</div>
        </a>
    </div>
    <button class="cat-btn-right btn btn-light rounded-circle shadow-sm" type="button" aria-label="Next">
        <svg width="20" height="20" fill="currentColor" viewBox="0 0 20 20"><path d="M7 17l5-5-5-5" stroke="currentColor" stroke-width="2" fill="none"/></svg>
    </button>
</div>
