document.addEventListener('DOMContentLoaded', function () {
    const container = document.querySelector('.category-dropdown-container');
    const dropdown = document.querySelector('.category-dropdown');

    container.addEventListener('mouseenter', () => {
        const rect = container.getBoundingClientRect();
        const dropdownHeight = dropdown.offsetHeight;
        const spaceBelow = window.innerHeight - rect.bottom;
        const spaceAbove = rect.top;

        // Reset class
        dropdown.classList.remove('upward');

        if (spaceBelow < dropdownHeight && spaceAbove > dropdownHeight) {
            dropdown.classList.add('upward');
        }
    });
});
document.querySelectorAll('.main-category-item').forEach(item => {
    item.addEventListener('mouseenter', () => {
        const submenu = item.querySelector('.sub-category-list');
        if (!submenu) return;

        submenu.classList.remove('upward'); // Reset trước

        const rect = submenu.getBoundingClientRect();
        const viewportHeight = window.innerHeight;

        // Nếu submenu sẽ vượt khỏi khung màn hình
        if (rect.bottom > viewportHeight) {
            submenu.classList.add('upward'); // xổ lên
        }
    });
});
