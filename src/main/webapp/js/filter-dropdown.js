document.addEventListener('DOMContentLoaded', function() {
    const dropdowns = document.querySelectorAll('.filter-option.dropdown');

    console.log(`Found ${dropdowns.length} dropdowns with class 'filter-option dropdown'`);

    dropdowns.forEach((dropdown, index) => {
        const button = dropdown.querySelector('.btn');
        if (!button) {
            console.warn(`No button found in dropdown ${index}`);
            return;
        }
        console.log(`Attaching click event to button in dropdown ${index}: ${button.innerText.trim()}`);

        // Gắn sự kiện click
        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log(`Clicked dropdown ${index}, button text: ${button.innerText.trim()}`);
            // Đóng các dropdown khác
            dropdowns.forEach(otherDropdown => {
                if (otherDropdown !== dropdown) {
                    otherDropdown.classList.remove('active');
                    console.log(`Closed dropdown ${otherDropdown.querySelector('.btn')?.innerText.trim() || 'unknown'}`);
                }
            });
            // Toggle dropdown hiện tại
            dropdown.classList.toggle('active');
            console.log(`Toggled dropdown ${index}, active: ${dropdown.classList.contains('active')}`);
        });

        // Đóng dropdown khi click bên ngoài
        document.addEventListener('click', function(e) {
            if (!dropdown.contains(e.target) && !button.contains(e.target)) {
                if (dropdown.classList.contains('active')) {
                    dropdown.classList.remove('active');
                    console.log(`Closed dropdown ${index} due to outside click`);
                }
            }
        });

        // Xử lý dropdown Giá
        if (dropdown.querySelector('.price-slider')) {
            const sliderTrack = document.getElementById('sliderTrack');
            const minThumb = document.getElementById('minThumb');
            const maxThumb = document.getElementById('maxThumb');
            const minPriceInput = document.querySelector('#minPrice');
            const maxPriceInput = document.querySelector('#maxPrice');
            const step = 50000; // Bước nhảy 50,000
            let minVal = Math.floor(parseInt(minPriceInput.value.replace(/\D/g, '')) / step) * step || 0;
            let maxVal = Math.ceil(parseInt(maxPriceInput.value.replace(/\D/g, '')) / step) * step || 20000000;

            // Cập nhật vị trí thumb
            function updateSlider() {
                const trackRect = sliderTrack.getBoundingClientRect();
                const thumbWidth = 15;
                const minPercent = (minVal / 20000000) * 100;
                const maxPercent = (maxVal / 20000000) * 100;

                minThumb.style.left = `calc(${minPercent}% - ${thumbWidth / 2}px)`;
                maxThumb.style.left = `calc(${maxPercent}% - ${thumbWidth / 2}px)`;

                // Cập nhật giao diện thanh trượt
                const range = document.createElement('div');
                range.style.position = 'absolute';
                range.style.left = `calc(${minPercent}% - ${thumbWidth / 2}px)`;
                range.style.width = `calc(${maxPercent - minPercent}% + ${thumbWidth}px)`;
                range.style.height = '8px';
                range.style.background = '#ff6200';
                range.style.top = '50%';
                range.style.transform = 'translateY(-50%)';
                sliderTrack.innerHTML = '';
                sliderTrack.appendChild(minThumb);
                sliderTrack.appendChild(maxThumb);
                sliderTrack.appendChild(range.cloneNode(true));

                // Cập nhật giá trị hiển thị
                minPriceInput.value = minVal.toLocaleString('vi-VN') + ' VND';
                maxPriceInput.value = maxVal.toLocaleString('vi-VN') + ' VND';

                // Đảm bảo giới hạn
                if (minVal < 0) minVal = 0;
                if (maxVal > 20000000) maxVal = 20000000;
                if (minVal > maxVal - step) minVal = maxVal - step;
                if (maxVal < minVal + step) maxVal = minVal + step;
            }

            // Kéo thumb
            let isDraggingMin = false;
            let isDraggingMax = false;

            minThumb.addEventListener('mousedown', function(e) {
                isDraggingMin = true;
                e.preventDefault();
            });

            maxThumb.addEventListener('mousedown', function(e) {
                isDraggingMax = true;
                e.preventDefault();
            });

            document.addEventListener('mousemove', function(e) {
                if (isDraggingMin || isDraggingMax) {
                    const trackRect = sliderTrack.getBoundingClientRect();
                    const clickX = e.clientX - trackRect.left;
                    const trackWidth = trackRect.width;
                    let percent = (clickX / trackWidth) * 100;

                    if (percent < 0) percent = 0;
                    if (percent > 100) percent = 100;

                    const newValue = Math.round((percent / 100) * 20000000 / step) * step; // Điều chỉnh theo bước 50,000

                    if (isDraggingMin) {
                        minVal = Math.min(newValue, maxVal - step);
                        updateSlider();
                    } else if (isDraggingMax) {
                        maxVal = Math.max(newValue, minVal + step);
                        updateSlider();
                    }
                }
            });

            document.addEventListener('mouseup', function() {
                isDraggingMin = false;
                isDraggingMax = false;
            });

            // Xử lý nhập tay
            function updateFromInput() {
                let newMin = Math.floor(parseInt(minPriceInput.value.replace(/\D/g, '')) / step) * step || 0;
                let newMax = Math.ceil(parseInt(maxPriceInput.value.replace(/\D/g, '')) / step) * step || 20000000;
                if (newMin < 0) newMin = 0;
                if (newMax > 20000000) newMax = 20000000;
                if (newMin > newMax - step) newMin = newMax - step;
                if (newMax < newMin + step) newMax = newMin + step;
                minVal = newMin;
                maxVal = newMax;
                updateSlider();
            }
            minPriceInput.addEventListener('change', updateFromInput);
            maxPriceInput.addEventListener('change', updateFromInput);

            // Xử lý nút "Áp dụng"
            const applyPriceBtn = dropdown.querySelector('.apply-price-btn');
            if (applyPriceBtn) {
                applyPriceBtn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const currentSlug = new URLSearchParams(window.location.search).get('slug') || '';
                    const currentState = new URLSearchParams(window.location.search).get('state') || '';
                    let url = `categoryViewServlet?slug=${currentSlug}`;
                    if (minVal) url += `&minPrice=${minVal}`;
                    if (maxVal) url += `&maxPrice=${maxVal}`;
                    if (currentState) url += `&state=${currentState}`;
                    console.log(`Applying price filter: ${url}`);
                    window.location.href = url;
                });
            }

            // Xử lý nút "Xóa lọc"
            const clearPriceBtn = dropdown.querySelector('.clear-price-btn');
            if (clearPriceBtn) {
                clearPriceBtn.addEventListener('click', function(e) {
                    e.preventDefault();
                    minVal = 0;
                    maxVal = 20000000;
                    updateSlider();
                    console.log(`Cleared price filter, reset to 0-20000000`);
                });
            }

            updateSlider();
        }
    });
});