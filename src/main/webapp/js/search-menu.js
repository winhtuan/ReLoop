document.querySelectorAll('.category-nav').forEach(function(btn) {
    btn.addEventListener('click', function(e) {
        e.preventDefault();
        var container = btn.closest('.category-dropdown-container');
        container.classList.toggle('open');
        // Đóng dropdown nếu click ra ngoài
        document.addEventListener('click', function handler(ev) {
            if (!container.contains(ev.target)) {
                container.classList.remove('open');
                document.removeEventListener('click', handler);
            }
        });
    });
});