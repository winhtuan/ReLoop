document.addEventListener('DOMContentLoaded', function () {
    console.log('feedbackModal.js loaded');
    const feedbackModal = document.getElementById('feedbackModal');
    const modalClose = document.querySelector('.feedback-modal-close');
    const feedbackTriggers = document.querySelectorAll('.feedback-trigger-btn');
    console.log('Feedback triggers found:', feedbackTriggers.length);
    if (feedbackTriggers.length === 0) {
        console.log('No elements with class .feedback-trigger-btn found in DOM');
    }

    feedbackTriggers.forEach(trigger => {
        trigger.addEventListener('click', function () {
            console.log('Trigger clicked for order:', this.getAttribute('data-order-id'));
            const orderId = this.getAttribute('data-order-id');
            const feedbackOrderIdElement = document.getElementById('feedbackOrderId');
            if (feedbackOrderIdElement) {
                // Kiểm tra xem đã feedback chưa
                $.ajax({
                    url: contextPath + '/SaveFeedbackServlet',
                    type: 'POST',
                    data: { orderId: orderId, checkOnly: true }, // Thêm tham số checkOnly
                    dataType: 'json',
                    success: function (response) {
                        if (response.success) {
                            feedbackOrderIdElement.textContent = orderId;
                            feedbackModal.style.display = 'block';
                            document.body.classList.add('modal-open'); // Chặn scroll
                        } else {
                            alert(response.message); // Hiển thị "You have already provided feedback for this order."
                        }
                    },
                    error: function (xhr, status, error) {
                        console.log('AJAX check error:', { status, error, response: xhr.responseText });
                        alert('Error checking feedback status.');
                    }
                });
            } else {
                console.log('Element with id "feedbackOrderId" not found');
            }
        });
    });

    // Close modal when clicking the close button
    modalClose.addEventListener('click', function () {
        feedbackModal.style.display = 'none';
        document.body.classList.remove('modal-open'); // Bỏ chặn scroll
    });

    // Close modal when clicking outside
    window.addEventListener('click', function (event) {
        if (event.target === feedbackModal) {
            feedbackModal.style.display = 'none';
            document.body.classList.remove('modal-open'); // Bỏ chặn scroll
        }
    });

    // Handle submit feedback
    const feedbackForm = document.getElementById('feedbackForm');
    console.log('Feedback form found:', feedbackForm); // Debug form
    if (feedbackForm) {
        feedbackForm.addEventListener('submit', function (event) {
            event.preventDefault();
            console.log('Form submitted'); // Debug submit
            const rating = document.querySelector('input[name="rating"]:checked');
            const comment = document.getElementById('feedbackComment').value;
            const orderId = document.getElementById('feedbackOrderId').textContent;

            if (rating) {
                console.log('Sending AJAX:', { orderId, rating: rating.value, comment }); // Debug data
                $.ajax({
                    url: contextPath + '/SaveFeedbackServlet',
                    type: 'POST',
                    data: { orderId: orderId, rating: rating.value, comment: comment },
                    dataType: 'json',
                    success: function (response) {
                        console.log('AJAX success:', response); // Debug response
                        alert(response.message);
                        if (response.success) {
                            feedbackModal.style.display = 'none';
                            document.body.classList.remove('modal-open');
                            location.reload();
                        }
                    },
                    error: function (xhr, status, error) {
                        console.log('AJAX error:', { status, error, response: xhr.responseText }); // Debug chi tiết
                        alert('Failed to save feedback. Status: ' + status + ', Error: ' + error + ', Response: ' + xhr.responseText);
                    }
                });
            } else {
                alert('Please select a rating.');
            }
        });
    } else {
        console.log('Feedback form not found in DOM');
    }
});