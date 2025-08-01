(function () {
            console.log('AI Moderation Script Loaded');
    
    document.addEventListener('DOMContentLoaded', function () {

        
        const imageInput = document.getElementById('productImages');
        const form = document.getElementById('postForm');
        const submitBtn = document.getElementById('submitPostBtn');
        

        
        if (!imageInput) {
            return;
        }
        
        let aiModerationPassed = true;
        let moderationResults = []; // Lưu kết quả kiểm duyệt của tất cả ảnh
        let isProcessing = false; // Prevent multiple simultaneous requests
        let allFiles = [];
        
        // Expose variables globally for categorySelect.js to access
        window.isAIModerationProcessing = false;
        window.aiModerationFailed = false;
        
        // Configuration
        const MAX_RETRY_ATTEMPTS = 3;
        const RETRY_DELAY_MS = 2000;
        const REQUEST_TIMEOUT_MS = 30000; // 30 seconds

        function getOrCreatePopup() {
            let popup = document.getElementById('aiModerationPopup');
            if (!popup) {
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
                    popup.style.display = 'none';
                };
                popup.appendChild(closeBtn);
                document.body.appendChild(popup);
            }
            return popup;
        }

        function showPopup(html, isError) {
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
            const popup = getOrCreatePopup();
            popup.style.display = 'none';
        }

        // Test network connectivity
        async function testNetworkConnectivity() {
            try {
                const testUrl = '/ReLoop/googlevision-test';
                const response = await fetch(testUrl, {
                    method: 'GET',
                    timeout: 10000
                });
                
                if (response.ok) {
                    const data = await response.json();
                    return data;
                } else {
                    return null;
                }
            } catch (error) {
                return null;
            }
        }

        // Moderate a single image with retry mechanism
        async function moderateSingleImage(file, index, retryCount = 0) {
            const formData = new FormData();
            formData.append('file', file);
            
            const apiUrl = '/ReLoop/googlevision';
            
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
                
                if (!res.ok) {
                    const errorText = await res.text();
                    
                    // Retry logic for network errors
                    if (retryCount < MAX_RETRY_ATTEMPTS && (res.status >= 500 || res.status === 0)) {
                        await new Promise(resolve => setTimeout(resolve, RETRY_DELAY_MS));
                        return await moderateSingleImage(file, index, retryCount + 1);
                    }
                    
                    throw new Error(`AI moderation failed: ${res.status} - ${errorText}`);
                }
                
                const data = await res.json();
                
                return {
                    file: file,
                    fileName: file.name,
                    isUnsafe: !!data.isUnsafe,
                    reasons: data.reasons || [],
                    error: null,
                    rawResponse: data
                };
                
            } catch (err) {
                // Retry logic for network errors
                if (retryCount < MAX_RETRY_ATTEMPTS && (err.name === 'AbortError' || err.message.includes('network'))) {
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
            
            if (isProcessing) {
                console.warn('⚠️ Already processing images, skipping...');
                return moderationResults;
            }
            
            isProcessing = true;
            window.isAIModerationProcessing = true;
            
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
                window.isAIModerationProcessing = false;
            }
        }

        // Helper: set submit button color
        function setSubmitBtnColor(isWarn) {
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
        
        function parseWeaponLabelConfidence(labels) {
            if (!Array.isArray(labels)) return [];
        
            return labels.map(label => {
                const match = label.match(/score:\s*(\d+(\.\d+)?)/);
                return match ? parseFloat(match[1]) : 0;
            });
        }
        
        function parseDrugEntityConfidence(labels) {
            if (!Array.isArray(labels)) return [];
        
            return labels.map(label => {
                const match = label.match(/score:\s*(\d+(\.\d+)?)/);
                return match ? parseFloat(match[1]) : 0;
            });
        }
        
        // Helper: update moderation status and UI
        function updateModerationStatusAndButton() {
            const hasHighConfidenceWeapon = moderationResults.some(result => {
                const confidences = parseWeaponLabelConfidence(result.rawResponse?.weaponLabels || []);
                return confidences.some(score => score >= 0.8);
            });
        
            const hasHighConfidenceDrug = moderationResults.some(result => {
                const confidences = parseDrugEntityConfidence(result.rawResponse?.drugEntities || []);
                return confidences.some(score => score >= 0.8);
            });
        
            const hasUnsafeGeneral = moderationResults.some(result => result.isUnsafe);
        
            if (hasHighConfidenceWeapon || hasHighConfidenceDrug) {
                aiModerationPassed = false;
                window.aiModerationFailed = true;
                setModerationStatus('rejected');
                setSubmitBtnColor(true);
        
                showPopup(`
                    <strong>❌ Rejected:</strong> Image(s) detected containing <b>${hasHighConfidenceWeapon ? 'weapon' : 'drug'}-related content</b> with high confidence (&gt; 80%).<br>
                    <br>Please remove these and try again.`, true);
                return;
            }       
        
            else if (hasUnsafeGeneral) {
                // ⚠️ Warn nếu ảnh chỉ ở mức nghi ngờ
                aiModerationPassed = true; // Cho phép post
                window.aiModerationFailed = false;
                setModerationStatus('warn');
                setSubmitBtnColor(true);
        
                const unsafeImages = moderationResults.filter(r => r.isUnsafe);
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
                html += '<div style="margin-top:12px; font-size:0.9em; color:#b8860b; line-height:1.4;">You may still submit, but review the highlighted images.</div>';
        
                showPopup(html, false);
        
                // Load preview thumbnails
                setTimeout(() => {
                    const popup = document.getElementById('aiModerationPopup');
                    const imgs = popup ? popup.querySelectorAll('img') : [];
                    unsafeImages.forEach((result, idx) => {
                        if (imgs[idx]) {
                            const reader = new FileReader();
                            reader.onload = function (e) {
                                imgs[idx].src = e.target.result;
                            };
                            reader.readAsDataURL(result.file);
                        }
                    });
                }, 100);
        
            } else {
                // ✅ An toàn hoàn toàn
                aiModerationPassed = true;
                window.aiModerationFailed = false;
                setModerationStatus('approved');
                setSubmitBtnColor(false);
                hidePopup();
            }
        
            console.log('[Moderation status] aiModerationPassed:', aiModerationPassed);
        }
        

        // Helper: set moderation_status hidden input
        function setModerationStatus(status) {
            if (!form) {
                return;
            }
            
            // Map status to database enum values
            let dbStatus;
            switch(status) {
                case 'passed':
                case 'approved':
                    dbStatus = 'approved';
                    break;
                case 'warn':
                    dbStatus = 'warn';
                    break;
                case 'rejected':
                    dbStatus = 'rejected';
                    break;
                case 'checking':
                case 'pending':
                default:
                    dbStatus = 'pending';
                    break;
            }
            
            let statusInput = form.querySelector('input[name="moderation_status"]');
            if (!statusInput) {
                statusInput = document.createElement('input');
                statusInput.type = 'hidden';
                statusInput.name = 'moderation_status';
                form.appendChild(statusInput);
            }
            statusInput.value = dbStatus;
            console.log('Moderation status:', dbStatus);
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


            try {
                // Moderate all images
                moderationResults = await moderateAllImages(allFiles);
                // Update UI based on results
                updateModerationStatusAndButton();
    
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
        
        // Initialize moderation status
        setModerationStatus('pending');

    });
})();