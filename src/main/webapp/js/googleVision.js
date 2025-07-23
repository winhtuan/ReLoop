// AI Image Moderation Integration for UpPostPage - DEBUG VERSION
// This script sends ALL selected images to the GoogleVision servlet whenever the image selection changes

(function () {
    console.log('🚀 AI Moderation Script Loaded');
    
    document.addEventListener('DOMContentLoaded', function () {
        console.log('📄 DOM Content Loaded');
        
        const imageInput = document.getElementById('productImages');
        const form = document.getElementById('postForm');
        const submitBtn = document.getElementById('submitPostBtn');
        
        // Debug: Check if elements exist
        console.log('🔍 Element Check:', {
            imageInput: !!imageInput,
            form: !!form,
            submitBtn: !!submitBtn,
            contextPath: window.contextPath
        });
        
        if (!imageInput) {
            console.error('❌ productImages input not found!');
            return;
        }
        
        let aiModerationPassed = true;
        let moderationResults = []; // Lưu kết quả kiểm duyệt của tất cả ảnh
        let isProcessing = false; // Prevent multiple simultaneous requests
        let allFiles = []; // Lưu toàn bộ file đã chọn (merge cũ và mới)

        // Create or get popup warning element
        function getOrCreatePopup() {
            let popup = document.getElementById('aiModerationPopup');
            if (!popup) {
                console.log('🎨 Creating new popup element');
                popup = document.createElement('div');
                popup.id = 'aiModerationPopup';
                popup.style.position = 'fixed';
                popup.style.top = '32px';
                popup.style.right = '32px';
                popup.style.zIndex = '9999';
                popup.style.background = '#fff3cd';
                popup.style.color = '#856404';
                popup.style.border = '1.5px solid #ffeeba';
                popup.style.borderRadius = '8px';
                popup.style.padding = '18px 28px 18px 18px';
                popup.style.fontWeight = '500';
                popup.style.fontSize = '1rem';
                popup.style.boxShadow = '0 4px 16px rgba(0,0,0,0.13)';
                popup.style.maxWidth = '350px';
                popup.style.lineHeight = '1.5';
                popup.style.display = 'none';
                popup.style.transition = 'all 0.3s';
                popup.style.minWidth = '220px';
                popup.innerHTML = '';
                
                // Close button
                const closeBtn = document.createElement('button');
                closeBtn.innerHTML = '&times;';
                closeBtn.style.position = 'absolute';
                closeBtn.style.top = '8px';
                closeBtn.style.right = '12px';
                closeBtn.style.background = 'none';
                closeBtn.style.border = 'none';
                closeBtn.style.fontSize = '1.3rem';
                closeBtn.style.color = '#856404';
                closeBtn.style.cursor = 'pointer';
                closeBtn.onclick = function () {
                    console.log('🎯 Popup close button clicked');
                    popup.style.display = 'none';
                };
                popup.appendChild(closeBtn);
                document.body.appendChild(popup);
            }
            return popup;
        }

        function showPopup(html, isError) {
            console.log('📢 Showing popup:', { isError, htmlLength: html.length });
            const popup = getOrCreatePopup();
            popup.innerHTML = '';
            
            // Close button
            const closeBtn = document.createElement('button');
            closeBtn.innerHTML = '&times;';
            closeBtn.style.position = 'absolute';
            closeBtn.style.top = '8px';
            closeBtn.style.right = '12px';
            closeBtn.style.background = 'none';
            closeBtn.style.border = 'none';
            closeBtn.style.fontSize = '1.3rem';
            closeBtn.style.color = isError ? '#721c24' : '#856404';
            closeBtn.style.cursor = 'pointer';
            closeBtn.onclick = function () {
                console.log('🎯 Popup close button clicked');
                popup.style.display = 'none';
            };
            popup.appendChild(closeBtn);
            
            // Message
            const msg = document.createElement('div');
            msg.innerHTML = html;
            popup.appendChild(msg);
            
            // Style
            if (isError) {
                popup.style.background = '#f8d7da';
                popup.style.color = '#721c24';
                popup.style.border = '1.5px solid #f5c6cb';
            } else {
                popup.style.background = '#fff3cd';
                popup.style.color = '#856404';
                popup.style.border = '1.5px solid #ffeeba';
            }
            popup.style.display = 'block';
        }

        function hidePopup() {
            console.log('🙈 Hiding popup');
            const popup = getOrCreatePopup();
            popup.style.display = 'none';
        }

        // Moderate a single image
        async function moderateSingleImage(file, index) {
            console.log(`🔍 Moderating image ${index + 1}/${moderationResults.length}:`, {
                name: file.name,
                size: file.size,
                type: file.type
            });
            
            const formData = new FormData();
            formData.append('file', file);
            
            const apiUrl = window.contextPath + '/googlevision';
            console.log(`📡 Sending request to: ${apiUrl}`);
            
            try {
                const startTime = Date.now();
                const res = await fetch(apiUrl, {
                    method: 'POST',
                    body: formData
                });
                
                const duration = Date.now() - startTime;
                console.log(`⏱️ API Response time: ${duration}ms, Status: ${res.status}`);
                
                if (!res.ok) {
                    const errorText = await res.text();
                    console.error(`❌ API Error ${res.status}:`, errorText);
                    throw new Error(`AI moderation failed: ${res.status} - ${errorText}`);
                }
                
                const data = await res.json();
                console.log(`✅ Image ${index + 1} API Response:`, {
                    isUnsafe: data.isUnsafe,
                    reasons: data.reasons,
                    safeSearch: data.safeSearch,
                    weaponLabels: data.weaponLabels,
                    drugEntities: data.drugEntities
                });
                
                return {
                    file: file,
                    fileName: file.name,
                    isUnsafe: !!data.isUnsafe,
                    reasons: data.reasons || [],
                    error: null,
                    rawResponse: data
                };
                
            } catch (err) {
                console.error(`💥 Error moderating image ${index + 1}:`, {
                    error: err.message,
                    fileName: file.name
                });
                
                return {
                    file: file,
                    fileName: file.name,
                    isUnsafe: true,
                    reasons: [err.message],
                    error: err.message,
                    rawResponse: null
                };
            }
        }

        // Moderate all images at once
        async function moderateAllImages(files) {
            console.log(`🚀 Starting moderation for ${files.length} images`);
            console.log('📝 Files to moderate:', files.map(f => ({name: f.name, size: f.size, type: f.type})));
            
            if (isProcessing) {
                console.warn('⚠️ Already processing images, skipping...');
                return moderationResults;
            }
            
            isProcessing = true;
            
            try {
                const results = [];
                
                // Process images sequentially to avoid overwhelming the server
                for (let i = 0; i < files.length; i++) {
                    const result = await moderateSingleImage(files[i], i);
                    results.push(result);
                }
                
                console.log('📊 Moderation Results Summary:', {
                    totalImages: results.length,
                    safeImages: results.filter(r => !r.isUnsafe).length,
                    unsafeImages: results.filter(r => r.isUnsafe).length,
                    errorImages: results.filter(r => r.error).length
                });
                
                return results;
                
            } finally {
                isProcessing = false;
            }
        }

        // Helper: set submit button color
        function setSubmitBtnColor(isWarn) {
            console.log('🎨 Setting submit button color:', { isWarn });
            if (submitBtn) {
                if (isWarn) {
                    submitBtn.style.background = '#dc3545'; // Bootstrap red
                    submitBtn.style.color = '#fff';
                    submitBtn.style.borderColor = '#dc3545';
                } else {
                    submitBtn.style.background = '';
                    submitBtn.style.color = '';
                    submitBtn.style.borderColor = '';
                }
            }
        }

        // Helper: update moderation status and UI
        function updateModerationStatusAndButton() {
            console.log('🔄 Updating moderation status and UI');
            
            const hasUnsafe = moderationResults.some(result => result.isUnsafe);
            aiModerationPassed = !hasUnsafe;
            setModerationStatus(hasUnsafe ? 'warn' : 'passed');
            setSubmitBtnColor(hasUnsafe);

            console.log('📈 Moderation Summary:', {
                totalImages: moderationResults.length,
                unsafeImages: moderationResults.filter(r => r.isUnsafe).length,
                safeImages: moderationResults.filter(r => !r.isUnsafe).length,
                aiModerationPassed: aiModerationPassed,
                details: moderationResults.map(r => ({
                    name: r.fileName,
                    isUnsafe: r.isUnsafe,
                    reasons: r.reasons
                }))
            });

            // Show popup if there are unsafe images
            const unsafeImages = moderationResults.filter(result => result.isUnsafe);
            if (unsafeImages.length > 0) {
                console.log('⚠️ Showing warning popup for unsafe images:', 
                    unsafeImages.map(img => img.fileName));
                
                let html = '<div style="margin-bottom: 10px;"><strong>⚠️ Unsafe images detected:</strong></div>';
                html += '<div style="display:flex;flex-direction:column;gap:10px;margin:10px 0 0 0;">';
                
                for (const result of unsafeImages) {
                    let reasonHtml = '';
                    if (Array.isArray(result.reasons) && result.reasons.length > 0) {
                        reasonHtml = result.reasons.join('<br>');
                    } else if (result.error) {
                        reasonHtml = result.error;
                    } else {
                        reasonHtml = 'Inappropriate content detected';
                    }
                    
                    html += `<div style="display:flex;align-items:center;gap:12px;margin-bottom:8px;">
                                <img src="" alt="unsafe" 
                                     style="width:60px;height:60px;object-fit:cover;border-radius:6px;
                                            border:1.5px solid #ffeeba;box-shadow:0 1px 4px rgba(0,0,0,0.07);background:#fff;" />
                                <div style="font-size:0.97em;color:#b8860b;line-height:1.4;word-break:break-word;">
                                    <strong>${result.fileName}</strong><br>
                                    ${reasonHtml}
                                </div>
                            </div>`;
                }
                
                html += '</div>';
                html += '<div style="margin-top:12px; font-size:0.9em; color:#b8860b; line-height:1.4;">Please remove or replace the highlighted images.</div>';
                
                showPopup(html, false);
                
                // Set preview images for unsafe images
                setTimeout(() => {
                    const popup = document.getElementById('aiModerationPopup');
                    const imgs = popup ? popup.querySelectorAll('img') : [];
                    console.log(`🖼️ Setting ${imgs.length} preview images`);
                    
                    unsafeImages.forEach((result, idx) => {
                        if (imgs[idx]) {
                            const reader = new FileReader();
                            reader.onload = function (e) {
                                imgs[idx].src = e.target.result;
                                console.log(`✅ Preview set for image ${idx + 1}: ${result.fileName}`);
                            };
                            reader.readAsDataURL(result.file);
                        }
                    });
                }, 100);
                
            } else {
                console.log('✅ All images are safe, hiding popup');
                hidePopup();
            }
        }

        // Helper: set moderation_status hidden input
        function setModerationStatus(status) {
            console.log('📝 Setting moderation status:', status);
            
            if (!form) {
                console.warn('⚠️ Form not found, cannot set moderation status');
                return;
            }
            
            let statusInput = form.querySelector('input[name="moderation_status"]');
            if (!statusInput) {
                console.log('➕ Creating moderation_status input');
                statusInput = document.createElement('input');
                statusInput.type = 'hidden';
                statusInput.name = 'moderation_status';
                form.appendChild(statusInput);
            }
            statusInput.value = status;
            console.log('✅ Moderation status set to:', status);
        }

        // Main event listener for image selection changes
        imageInput.addEventListener('change', async function (e) {
            const newFiles = Array.from(imageInput.files);
            // Merge file cũ và mới, loại trùng (theo name, size, lastModified)
            for (const f of newFiles) {
                if (!allFiles.some(old => old.name === f.name && old.size === f.size && old.lastModified === f.lastModified)) {
                    allFiles.push(f);
                }
            }
            // Nếu không còn ảnh nào
            if (allFiles.length === 0) {
                console.log('🗑️ No images selected, resetting moderation state');
                hidePopup();
                moderationResults = [];
                setModerationStatus('pending');
                aiModerationPassed = true;
                if (submitBtn) submitBtn.disabled = false;
                setSubmitBtnColor(false);
                return;
            }
            // Tạo lại input.files từ allFiles
            const dt = new DataTransfer();
            allFiles.forEach(f => dt.items.add(f));
            imageInput.files = dt.files;

            // Show loading state
            setModerationStatus('checking');
            console.log('⏳ Starting AI moderation for all images...');

            try {
                // Moderate all images
                moderationResults = await moderateAllImages(allFiles);
                // Update UI based on results
                updateModerationStatusAndButton();
                console.log('✅ AI moderation completed successfully');
            } catch (error) {
                console.error('💥 Fatal error during moderation:', error);
                showPopup(`<span style="color:#721c24;font-weight:bold;">❌ Moderation failed: ${error.message}</span>`, true);
                setModerationStatus('error');
                aiModerationPassed = false;
                setSubmitBtnColor(true);
            }
        });

        // Hàm hỗ trợ xoá file khỏi allFiles và input.files (gọi từ preview)
        window.removeImageFromAllFiles = function(fileToRemove) {
            allFiles = allFiles.filter(f => !(f.name === fileToRemove.name && f.size === fileToRemove.size && f.lastModified === fileToRemove.lastModified));
            // Tạo lại input.files từ allFiles
            const dt = new DataTransfer();
            allFiles.forEach(f => dt.items.add(f));
            imageInput.files = dt.files;
            // Trigger lại kiểm duyệt
            imageInput.dispatchEvent(new Event('change'));
        }

        // Prevent form submission if moderation failed
        if (form) {
            form.addEventListener('submit', function (e) {
                console.log('📤 Form submission attempt:', {
                    aiModerationPassed,
                    moderationResults: moderationResults.length,
                    isProcessing
                });
                
                if (isProcessing) {
                    console.warn('⚠️ Still processing images, blocking submit');
                    e.preventDefault();
                    showPopup('<span style="color:#721c24;font-weight:bold;">⏳ Please wait for image moderation to complete.</span>', true);
                    return false;
                }
                
                if (!aiModerationPassed) {
                    console.warn('🚫 Form submission blocked due to unsafe images');
                    e.preventDefault();
                    
                    // Ensure moderation status is set to warn
                    setModerationStatus('warn');
                    
                    showPopup('<span style="color:#721c24;font-weight:bold;">⚠️ Please select only appropriate images. The current selection is not allowed.</span>', true);
                    setSubmitBtnColor(true);
                    
                    return false;
                }
                
                console.log('✅ Form submission allowed - all images passed moderation');
            });
        }
        
        // Initialize moderation status
        setModerationStatus('pending');
        console.log('🎯 AI Moderation Script Initialized Successfully');
    });
})();