const toggleDropdown = (dropdown, menu, isopen) => {
        dropdown.classList.toggle("open", isopen);
        menu.style.height = isopen ? `${menu.scrollHeight}px` : 0;
};
    
    const closeAllDropdowns = () => {
        document.querySelectorAll(".dropdown-container.open").forEach(openDropdown => {
        toggleDropdown(openDropdown, openDropdown.querySelector(".dropdown-menu"), false);
        });
};
        document.querySelectorAll(".dropdown-toggle").forEach(dropdownToggle => {
        dropdownToggle.addEventListener("click", e => {
        e.preventDefault();

        const dropdown = e.target.closest(".dropdown-container");
        const menu = dropdown.querySelector(".dropdown-menu");
        const isopen = dropdown.classList.contains("open");
        
        toggleDropdown (dropdown, menu, !isopen);
});
});
        document.querySelectorAll(".sidebar-toggler, .sidebar-menu-button").forEach(button => {
        button.addEventListener("click", () => {
        closeAllDropdowns();
        // Toggle collapsed class on sidebar
        document.querySelector(".sidebar").classList.toggle("collapsed");
        });
});