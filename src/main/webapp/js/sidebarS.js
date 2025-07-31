const toggleDropdown = (dropdown, menu, isopen) => {
    dropdown.classList.toggle("sp-open", isopen);
    menu.style.height = isopen ? `${menu.scrollHeight}px` : 0;
};

const closeAllDropdowns = () => {
    document.querySelectorAll(".sp-dropdown-container.sp-open").forEach(openDropdown => {
        toggleDropdown(openDropdown, openDropdown.querySelector(".sp-dropdown-menu"), false);
    });
};

document.querySelectorAll(".sp-dropdown-toggle").forEach(dropdownToggle => {
    dropdownToggle.addEventListener("click", e => {
        e.preventDefault();
        const dropdown = e.target.closest(".sp-dropdown-container");
        const menu = dropdown.querySelector(".sp-dropdown-menu");
        const isopen = dropdown.classList.contains("sp-open");
        toggleDropdown(dropdown, menu, !isopen);
    });
});

document.querySelectorAll(".sp-sidebar-toggler, .sp-sidebar-menu-button").forEach(button => {
    button.addEventListener("click", () => {
        closeAllDropdowns();
        document.querySelector(".sp-sidebar").classList.toggle("sp-collapsed");
    });
});
