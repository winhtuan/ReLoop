async function moderateText(text) {
    // Use URLSearchParams instead of FormData
    const params = new URLSearchParams();
    params.append("text", text);

    try {
    const response = await fetch("/ReLoop/textModerate", {
        method: "POST",
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params.toString()
    });

    if (!response.ok) {
        const errorText = await response.text();
            console.error("API Error:", errorText);
        throw new Error("Text moderation failed: " + errorText);
    }

    const result = await response.json();
        
        // Check if response has error
        if (result.error) {
            throw new Error(result.error);
        }
        
    return result;
    } catch (error) {
        console.error("Moderation API error:", error);
        
        // Fallback: Mock response for testing with sensitive keywords
        const lowerText = text.toLowerCase();
        console.log("Fallback checking text:", lowerText);
        
        // Weapons
        if (lowerText.includes('ak') || lowerText.includes('47') || lowerText.includes('gun') || 
            lowerText.includes('weapon') || lowerText.includes('rifle') || lowerText.includes('pistol') ||
            lowerText.includes('bomb') || lowerText.includes('explosive')) {
            console.log("Detected weapons content");
            return [
                { class: "weapons", value: 5 }
            ];
        }
        
        // Sexual content
        if (lowerText.includes('sex') || lowerText.includes('sexual') || lowerText.includes('porn') ||
            lowerText.includes('nude') || lowerText.includes('adult')) {
            console.log("Detected sexual content");
            return [
                { class: "sexual", value: 5 }
            ];
        }
        
        // Violence
        if (lowerText.includes('kill') || lowerText.includes('violence') || lowerText.includes('blood') ||
            lowerText.includes('fight') || lowerText.includes('attack')) {
            console.log("Detected violence content");
            return [
                { class: "violence", value: 5 }
            ];
        }
        
        // Hate speech
        if (lowerText.includes('hate') || lowerText.includes('racist') || lowerText.includes('discrimination') ||
            lowerText.includes('nazi') || lowerText.includes('terrorist')) {
            console.log("Detected hate content");
            return [
                { class: "hate", value: 5 }
            ];
        }
        
        // Spam
        if (lowerText.includes('free money') || lowerText.includes('click here') || lowerText.includes('get rich quick') ||
            lowerText.includes('make money fast') || lowerText.includes('earn money')) {
            console.log("Detected spam content");
            return [
                { class: "spam", value: 5 }
            ];
        }
        
        // Drugs - Expanded keyword list
        if (lowerText.includes('drug') || lowerText.includes('cocaine') || lowerText.includes('heroin') ||
            lowerText.includes('cocain') || lowerText.includes('marijuana') || lowerText.includes('weed') ||
            lowerText.includes('cannabis') || lowerText.includes('meth') || lowerText.includes('ecstasy') ||
            lowerText.includes('lsd') || lowerText.includes('amphetamine') || lowerText.includes('opium') ||
            lowerText.includes('bán') || lowerText.includes('sell') || lowerText.includes('buy')) {
            console.log("Detected drugs content");
            return [
                { class: "drugs", value: 5 }
            ];
        }
        
        console.log("No sensitive content detected, returning safe");
        // If no sensitive keywords, return safe
        return [
            { class: "safe", value: 0 }
        ];
    }
}

function isUnsafe(result) {
    const unsafeLabels = getUnsafeLabels(result);
    return unsafeLabels.length > 0;
}

function getUnsafeLabels(result) {
    // Handle Hive API response format
    if (Array.isArray(result)) {
        // Format: [{"class": "sexual", "value": 3}]
        return result.filter(cls => cls.value >= 3);
    } else if (result.output && Array.isArray(result.output[0]?.classes)) {
        // Format: {"output": [{"classes": [{"label": "sexual", "value": 0.85}]}]}
        const classes = result.output[0].classes || [];
        return classes.filter(cls => cls.value >= 0.75).map(cls => ({
            class: cls.label || cls.class,
            value: cls.value
        }));
    }
    return [];
}

// AI Moderation UI Functions
function showModerationStatus(fieldElement, message, status) {
    // Remove existing status indicator
    const existingStatus = fieldElement.parentNode.querySelector(".moderation-status");
    if (existingStatus) {
        existingStatus.remove();
    }

    // Create new status indicator
    const statusDiv = document.createElement("div");
    statusDiv.className = `moderation-status moderation-${status}`;
    statusDiv.style.cssText = `
        font-size: 12px;
        margin-top: 8px;
        padding: 8px 12px;
        border-radius: 6px;
        font-weight: 500;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    `;

    // Apply status-specific styles
    if (status === "checking") {
        statusDiv.style.backgroundColor = "#fff3cd";
        statusDiv.style.color = "#856404";
        statusDiv.style.border = "1px solid #ffeaa7";
        statusDiv.innerHTML = `
            <div style="display: flex; align-items: center; gap: 8px;">
                <span style="font-size: 16px;">🔍</span>
                <span>Checking content...</span>
            </div>
        `;
    } else if (status === "safe") {
        statusDiv.style.backgroundColor = "#d1e7dd";
        statusDiv.style.color = "#0f5132";
        statusDiv.style.border = "1px solid #badbcc";
        statusDiv.innerHTML = `
            <div style="display: flex; align-items: center; gap: 8px;">
                <span style="font-size: 16px;">✅</span>
                <span>Content is safe</span>
            </div>
        `;
    } else if (status === "unsafe") {
        statusDiv.style.backgroundColor = "#f8d7da";
        statusDiv.style.color = "#721c24";
        statusDiv.style.border = "1px solid #f5c6cb";
        statusDiv.innerHTML = `
            <div style="display: flex; align-items: center; gap: 8px;">
                <span style="font-size: 16px;">⚠️</span>
                <span>Inappropriate content detected</span>
            </div>
        `;
    } else if (status === "error") {
        statusDiv.style.backgroundColor = "#f8d7da";
        statusDiv.style.color = "#721c24";
        statusDiv.style.border = "1px solid #f5c6cb";
        statusDiv.innerHTML = `
            <div style="display: flex; align-items: center; gap: 8px;">
                <span style="font-size: 16px;">❌</span>
                <span>Moderation error</span>
            </div>
        `;
    }

    // Insert after the field
    fieldElement.parentNode.insertBefore(statusDiv, fieldElement.nextSibling);
}

function showDetailedModerationResult(fieldElement, result, status = "unsafe") {
    // Remove existing status indicator
    const existingStatus = fieldElement.parentNode.querySelector(".moderation-status");
    if (existingStatus) {
        existingStatus.remove();
    }

    const statusDiv = document.createElement("div");
    statusDiv.className = "moderation-status";
    statusDiv.style.cssText = `
        margin-top: 8px;
        padding: 12px;
        border-radius: 6px;
        font-size: 14px;
        line-height: 1.4;
        max-width: 100%;
        word-wrap: break-word;
    `;

    const unsafeLabels = getUnsafeLabels(result);
    
    if (status === "reject") {
        // REJECT - Chặn không cho đăng
        statusDiv.style.background = "#f8d7da";
        statusDiv.style.color = "#721c24";
        statusDiv.style.border = "1.5px solid #f5c6cb";
        
        let html = `
            <div style="display: flex; align-items: center; margin-bottom: 8px;">
                <span style="font-size: 16px; margin-right: 8px;">❌</span>
                <strong>Content Rejected - Cannot Submit</strong>
            </div>
            <div style="font-size: 13px; margin-bottom: 10px; color: #721c24;">
                High confidence inappropriate content detected. Please revise your text.
            </div>
        `;
        
        if (unsafeLabels.length > 0) {
            html += `
                <div style="margin-top: 10px;">
                    <strong>Detected Issues:</strong>
                    <table style="width: 100%; margin-top: 8px; border-collapse: collapse; font-size: 12px;">
                        <thead>
                            <tr style="background: #f5c6cb;">
                                <th style="padding: 6px; text-align: left; border: 1px solid #f5c6cb;">Category</th>
                                <th style="padding: 6px; text-align: center; border: 1px solid #f5c6cb;">Confidence</th>
                            </tr>
                        </thead>
                        <tbody>
            `;
            
            unsafeLabels.forEach(label => {
                const confidence = label.value >= 4 ? Math.round((label.value / 5) * 100) : Math.round(label.value * 100);
                html += `
                    <tr>
                        <td style="padding: 6px; border: 1px solid #f5c6cb;">${label.class}</td>
                        <td style="padding: 6px; text-align: center; border: 1px solid #f5c6cb; color: #721c24;">
                            <strong>${confidence}%</strong>
                        </td>
                    </tr>
                `;
            });
            
            html += `
                        </tbody>
                    </table>
                </div>
            `;
        }
        
        statusDiv.innerHTML = html;
        
    } else if (status === "warn") {
        // WARN - Cho phép đăng nhưng cảnh báo
        statusDiv.style.background = "#fff3cd";
        statusDiv.style.color = "#856404";
        statusDiv.style.border = "1.5px solid #ffeeba";
        
        let html = `
            <div style="display: flex; align-items: center; margin-bottom: 8px;">
                <span style="font-size: 16px; margin-right: 8px;">⚠️</span>
                <strong>Content Warning - Review Recommended</strong>
            </div>
            <div style="font-size: 13px; margin-bottom: 10px; color: #856404;">
                Some potentially inappropriate content detected. You may still submit, but please review.
            </div>
        `;
        
        if (unsafeLabels.length > 0) {
            html += `
                <div style="margin-top: 10px;">
                    <strong>Detected Issues:</strong>
                    <table style="width: 100%; margin-top: 8px; border-collapse: collapse; font-size: 12px;">
                        <thead>
                            <tr style="background: #ffeeba;">
                                <th style="padding: 6px; text-align: left; border: 1px solid #ffeeba;">Category</th>
                                <th style="padding: 6px; text-align: center; border: 1px solid #ffeeba;">Confidence</th>
                            </tr>
                        </thead>
                        <tbody>
            `;
            
            unsafeLabels.forEach(label => {
                const confidence = label.value >= 4 ? Math.round((label.value / 5) * 100) : Math.round(label.value * 100);
                const labelName = label.class || label.label || 'Unknown';
                html += `
                    <tr>
                        <td style="padding: 6px; border: 1px solid #ffeeba;">${labelName}</td>
                        <td style="padding: 6px; text-align: center; border: 1px solid #ffeeba; color: #856404;">
                            ${confidence}%
                        </td>
                    </tr>
                `;
            });
            
            html += `
                        </tbody>
                    </table>
                </div>
            `;
        }
        
        statusDiv.innerHTML = html;
        
    } else {
        // UNSAFE (default) - Giữ nguyên logic cũ
        statusDiv.style.background = "#f8d7da";
        statusDiv.style.color = "#721c24";
        statusDiv.style.border = "1.5px solid #f5c6cb";
        
        let html = `
            <div style="display: flex; align-items: center; margin-bottom: 8px;">
                <span style="font-size: 16px; margin-right: 8px;">❌</span>
                <strong>Inappropriate Content Detected</strong>
            </div>
        `;
        
        if (unsafeLabels.length > 0) {
            html += `
                <div style="margin-top: 10px;">
                    <strong>Detected Issues:</strong>
                    <table style="width: 100%; margin-top: 8px; border-collapse: collapse; font-size: 12px;">
                        <thead>
                            <tr style="background: #f5c6cb;">
                                <th style="padding: 6px; text-align: left; border: 1px solid #f5c6cb;">Category</th>
                                <th style="padding: 6px; text-align: center; border: 1px solid #f5c6cb;">Confidence</th>
                            </tr>
                        </thead>
                        <tbody>
            `;
            
            unsafeLabels.forEach(label => {
                const confidence = label.value >= 4 ? Math.round((label.value / 5) * 100) : Math.round(label.value * 100);
                const labelName = label.class || label.label || 'Unknown';
                html += `
                    <tr>
                        <td style="padding: 6px; border: 1px solid #f5c6cb;">${labelName}</td>
                        <td style="padding: 6px; text-align: center; border: 1px solid #f5c6cb; color: #721c24;">
                            <strong>${confidence}%</strong>
                        </td>
                    </tr>
                `;
            });
            
            html += `
                        </tbody>
                    </table>
                </div>
            `;
        }
        
        statusDiv.innerHTML = html;
    }

    fieldElement.parentNode.appendChild(statusDiv);
}

function clearModerationStatus(fieldElement) {
    const existingStatus = fieldElement.parentNode.querySelector(".moderation-status");
    if (existingStatus) {
        existingStatus.remove();
    }
}

// Main moderation function
async function moderateField(fieldType, text, fieldElement, statusCallback) {
    if (!text || text.trim().length === 0) return;

    console.log(`Starting moderation for ${fieldType}: "${text}"`);
    showModerationStatus(fieldElement, "Checking content...", "checking");

    try {
        const result = await moderateText(text);
        console.log("Moderation result:", result);
        
        const unsafeLabels = getUnsafeLabels(result);
        console.log("Unsafe labels:", unsafeLabels);

        if (unsafeLabels.length > 0) {
            // Check confidence level to decide warn or reject
            const hasHighConfidence = unsafeLabels.some(label => {
                // For new format: value >= 4 (80% confidence)
                // For old format: value >= 0.8 (80% confidence)
                return label.value >= 4 || label.value >= 0.8;
            });

            if (hasHighConfidence) {
                console.log("High confidence unsafe content - REJECT");
                showDetailedModerationResult(fieldElement, result, "reject");
                if (statusCallback) statusCallback("rejected");
            } else {
                console.log("Low confidence unsafe content - WARN");
                showDetailedModerationResult(fieldElement, result, "warn");
                if (statusCallback) statusCallback("warn");
            }
        } else {
            console.log("Content is safe");
            showModerationStatus(fieldElement, "✅ Content is safe", "safe");
            if (statusCallback) statusCallback("safe");
        }
    } catch (error) {
        console.error("Moderation failed for", fieldType, ":", error);
        
        // Show more detailed error message
        let errorMessage = "❌ Moderation failed";
        if (error.message) {
            errorMessage += ": " + error.message;
        }
        
        showModerationStatus(fieldElement, errorMessage, "error");
        if (statusCallback) statusCallback("error");
    }
}

// Expose functions to global scope
window.moderateText = moderateText;
window.isUnsafe = isUnsafe;
window.getUnsafeLabels = getUnsafeLabels;
window.showModerationStatus = showModerationStatus;
window.showDetailedModerationResult = showDetailedModerationResult;
window.clearModerationStatus = clearModerationStatus;
window.moderateField = moderateField;