// AI Image Moderation Integration for UpPostPage - ENHANCED VERSION
// This script sends ALL selected images to the GoogleVision servlet whenever the image selection changes

(function () {
    console.log('🚀 AI Moderation Script Loaded - Enhanced Version');
    
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
        let allFiles = [];
        
        // Configuration
        const MAX_RETRY_ATTEMPTS = 3;
        const RETRY_DELAY_MS = 2000;
        const REQUEST_TIMEOUT_MS = 30000; // 30 seconds

        function getOrCreatePopup() {
            let popup = document.getElementById('aiModerationPopup');
            if (!popup) {
                console.log('➕ Creating AI moderation popup');
                popup = document.createElement('div');
                popup.id = 'aiModerationPopup';
                popup.style.cssText = `
                    position: fixed; top: 20px; right: 20px; z-index: 9999;
                    padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                    max-width: 400px; max-height: 80vh; overflow-y: auto;
                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                    font-size: 14px; line-height: 1.4;
                    display: none;
                `;
                
                const closeBtn = document.createElement('button');
                closeBtn.innerHTML = '&times;';
                closeBtn.style.cssText = `
                    position: absolute; top: 8px; right: 12px;
                    background: none; border: none; font-size: 1.3rem;
                    cursor: pointer; color: #666;
                `;
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
            closeBtn.style.cssText = `
                position: absolute; top: 8px; right: 12px;
                background: none; border: none; font-size: 1.3rem;
                cursor: pointer; color: ${isError ? '#721c24' : '#856404'};
            `;
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

        // Test network connectivity
        async function testNetworkConnectivity() {
            try {
                console.log('🌐 Testing network connectivity...');
                const testUrl = window.contextPath + '/googlevision-test';
                const response = await fetch(testUrl, {
                    method: 'GET',
                    timeout: 10000
                });
                
                if (response.ok) {
                    const data = await response.json();
                    console.log('📊 Network test results:', data);
                    return data;
                } else {
                    console.warning('⚠️ Network test failed:', response.status);
                    return null;
                }
            } catch (error) {
                console.error('💥 Network test error:', error);
                return null;
            }
        }

        // Moderate a single image with retry mechanism
        async function moderateSingleImage(file, index, retryCount = 0) {
            console.log(`🔍 Moderating image ${index + 1}/${moderationResults.length} (attempt ${retryCount + 1}):`, {
                name: file.name,
                size: file.size,
                type: file.type
            });
            
            const formData = new FormData();
            formData.append('file', file);
            
            const apiUrl = window.contextPath + '/googlevision';
            console.log(`📡 Sending request to: ${apiUrl}`);
            
            try {
                const controller = new AbortController();
                const timeoutId = setTimeout(() => controller.abort(), REQUEST_TIMEOUT_MS);
                
                const startTime = Date.now();
                const res = await fetch(apiUrl, {
                    method: 'POST',
                    body: formData,
                    signal: controller.signal
                });
                
                clearTimeout(timeoutId);
                const duration = Date.now() - startTime;
                console.log(`⏱️ API Response time: ${duration}ms, Status: ${res.status}`);
                
                if (!res.ok) {
                    const errorText = await res.text();
                    console.error(`❌ API Error ${res.status}:`, errorText);
                    
                    // Retry logic for network errors
                    if (retryCount < MAX_RETRY_ATTEMPTS && (res.status >= 500 || res.status === 0)) {
                        console.log(`🔄 Retrying in ${RETRY_DELAY_MS}ms... (${retryCount + 1}/${MAX_RETRY_ATTEMPTS})`);
                        await new Promise(resolve => setTimeout(resolve, RETRY_DELAY_MS));
                        return await moderateSingleImage(file, index, retryCount + 1);
                    }
                    
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
                    fileName: file.name,
                    retryCount: retryCount
                });
                
                // Retry logic for network errors
                if (retryCount < MAX_RETRY_ATTEMPTS && (err.name === 'AbortError' || err.message.includes('network'))) {
                    console.log(`🔄 Retrying in ${RETRY_DELAY_MS}ms... (${retryCount + 1}/${MAX_RETRY_ATTEMPTS})`);
                    await new Promise(resolve => setTimeout(resolve, RETRY_DELAY_MS));
                    return await moderateSingleImage(file, index, retryCount + 1);
                }
                
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
                // First, test network connectivity
                const networkTest = await testNetworkConnectivity();
                if (networkTest && !networkTest.summary.allTestsPassed) {
                    console.warning('⚠️ Network connectivity issues detected:', networkTest);
                    showPopup(`
                        <div style="margin-bottom: 10px;"><strong>⚠️ Network Issues Detected:</strong></div>
                        <div style="font-size: 0.9em; color: #856404;">
                            Google Vision API connectivity test failed. This may affect image moderation.
                            <br><br>Please check your internet connection and try again.
                        </div>
                    `, false);
                }
                
                const results = [];
                
                // Process images sequentially to avoid overwhelming the server
                for (let i = 0; i < files.length; i++) {
                    const result = await moderateSingleImage(files[i], i);
                    results.push(result);
                    
                    // Add small delay between requests to avoid rate limiting
                    if (i < files.length - 1) {
                        await new Promise(resolve => setTimeout(resolve, 500));
                    }
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
        console.log('🎯 AI Moderation Script Initialized Successfully - Enhanced Version');
    });
})();