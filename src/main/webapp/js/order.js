const addressInput = document.getElementById('address');
const feeInput = document.getElementById('shippingFeeInput');
const statusIcon = document.getElementById('shippingStatus');
const suggestionsContainer = document.getElementById('suggestions');

let shippingFee = 0;
let voucherDiscount = 0;
let shipTimeout = null;
let isSelectingSuggestion = false;

// AUTOCOMPLETE SUGGESTION
const debounce = (func, delay) => {
    let timeout;
    return (...args) => {
        clearTimeout(timeout);
        timeout = setTimeout(() => func(...args), delay);
    };
};

const fetchSuggestions = async (query) => {
    if (query.length < 2) {
        suggestionsContainer.innerHTML = "";
        suggestionsContainer.classList.add("d-none");
        return;
    }
    try {
        const response = await fetch(`https://rsapi.goong.io/Place/AutoComplete?api_key=${goongApiKey}&input=${encodeURIComponent(query)}`);
        const data = await response.json();
        if (data.status === "OK") {
            displaySuggestions(data.predictions);
        } else {
            suggestionsContainer.innerHTML = "";
            suggestionsContainer.classList.add("d-none");
            console.warn("Autocomplete API returned status:", data.status, data);
        }
    } catch (error) {
        console.error("Autocomplete API error:", error);
    }
};

const displaySuggestions = (predictions) => {
    suggestionsContainer.innerHTML = "";
    suggestionsContainer.classList.remove("d-none");
    predictions.forEach((prediction) => {
        const div = document.createElement("div");
        div.className = "suggestion px-4 py-2";
        div.style.cursor = "pointer";
        div.textContent = prediction.description;
        div.addEventListener("click", () => selectSuggestion(prediction.description));
        suggestionsContainer.appendChild(div);
    });
};

const selectSuggestion = (description) => {
    isSelectingSuggestion = true;
    addressInput.value = description;
    suggestionsContainer.innerHTML = "";
    suggestionsContainer.classList.add("d-none");
    addressInput.dispatchEvent(new Event('input'));
    setTimeout(() => {
        isSelectingSuggestion = false;
    }, 500);
};

addressInput.addEventListener("input", debounce((e) => {
    if (!isSelectingSuggestion) {
        fetchSuggestions(e.target.value);
    }
}, 300));

document.addEventListener("click", (e) => {
    if (!suggestionsContainer.contains(e.target) && e.target !== addressInput) {
        suggestionsContainer.innerHTML = "";
        suggestionsContainer.classList.add("d-none");
    }
});

// ======== GOONG DIRECTIONS API WITH FALLBACK ========
async function calculateShippingGoong(userAddress, shopAddresses) {
    const geocodeUrl = `https://rsapi.goong.io/Geocode`;
    const directionUrl = `https://rsapi.goong.io/Direction`;

    async function getCoordinates(address) {
        try {
            const res = await fetch(`${geocodeUrl}?address=${encodeURIComponent(address)}&api_key=${goongApiKey}`);
            const data = await res.json();
            if (data.status !== "OK" || !data.results.length) {
                throw new Error("Địa chỉ không hợp lệ hoặc không tìm được vị trí: " + address);
            }
            return data.results[0].geometry.location;
        } catch (e) {
            throw e;
        }
    }

    async function getDirections(shopCoord, userCoord, vehicle = 'bike') {
        const url = `${directionUrl}?origin=${shopCoord.lat},${shopCoord.lng}&destination=${userCoord.lat},${userCoord.lng}&vehicle=${vehicle}&api_key=${goongApiKey}`;
        const res = await fetch(url);
        return await res.json();
    }
    async function getORSRouteDistance(fromCoord, toCoord) {
        const orsKey = "5b3ce3597851110001cf624894341d3541ad47ab870b688573f624c3";
        const url = `https://api.openrouteservice.org/v2/directions/driving-car?api_key=${orsKey}&start=${fromCoord.lng},${fromCoord.lat}&end=${toCoord.lng},${toCoord.lat}`;
        const res = await fetch(url);
        if (!res.ok)
            throw new Error("ORS API error");
        const data = await res.json();
        if (
                data &&
                data.features &&
                data.features.length > 0 &&
                data.features[0].properties &&
                data.features[0].properties.summary &&
                typeof data.features[0].properties.summary.distance === "number"
                ) {
            return data.features[0].properties.summary.distance / 1000;
        } else {
            throw new Error("No route found from ORS");
        }
    }

    function haversineDistance(coord1, coord2) {
        const toRad = (x) => x * Math.PI / 180;
        const R = 6371; // Earth radius (km)
        const dLat = toRad(coord2.lat - coord1.lat);
        const dLng = toRad(coord2.lng - coord1.lng);
        const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(toRad(coord1.lat)) * Math.cos(toRad(coord2.lat)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    let shipFees = [];
    let distances = [];
    let methodNotes = [];
    const userCoord = await getCoordinates(userAddress);

    for (let shopAddress of shopAddresses) {
        const shopCoord = await getCoordinates(shopAddress);
        let directionData = await getDirections(shopCoord, userCoord, 'bike');
        let method = "bike";
        // Thử lại với vehicle khác nếu không có route
        if (!directionData.routes || !directionData.routes.length) {
            directionData = await getDirections(shopCoord, userCoord, 'car');
            method = "car";
        }
        // Nếu vẫn không có route, dùng Haversine fallback
        if (!directionData.routes || !directionData.routes.length) {
            const haversineDist = haversineDistance(shopCoord, userCoord);
            distances.push(haversineDist);
            let fee = calculateShipFee(haversineDist);
            shipFees.push(fee);
            methodNotes.push(`Không tìm được tuyến đường. Đã ước tính khoảng cách theo đường thẳng (Haversine)!`);
            continue;
        }

        const leg = directionData.routes[0].legs[0];
        const distanceValue = Number(leg.distance.value) / 1000;
        distances.push(distanceValue);
        let fee = calculateShipFee(distanceValue);
        shipFees.push(fee);
        methodNotes.push(method === "car" ? "Đã chuyển sang xe ô tô vì không tìm được tuyến đường xe máy." : "");
    }
    return {shipFees, distances, methodNotes};
}

function calculateShipFee(distanceKm) {
    const baseFee = 15000;
    const extraFee_2_5 = 5000;
    const extraFee_5plus = 4000;
    if (distanceKm <= 2) {
        return baseFee;
    } else if (distanceKm <= 5) {
        return baseFee + Math.ceil(distanceKm - 2) * extraFee_2_5;
    } else {
        return baseFee + (3 * extraFee_2_5) + Math.ceil(distanceKm - 5) * extraFee_5plus;
    }
}

function updateFinalTotalUI() {
    productTotal = Number(productTotal) || 0;
    shippingFee = Number(shippingFee) || 0;
    voucherDiscount = Number(voucherDiscount) || 0;
    let total = productTotal + shippingFee - voucherDiscount;
    if (total < 0)
        total = 0; // Không cho phép tổng âm tiền
    document.getElementById('final_total_detail').innerText = total.toLocaleString('en-US') + ' VND';
}


// ====== SỰ KIỆN TÍNH PHÍ SHIP ======
addressInput.addEventListener('input', function () {
    if (shipTimeout)
        clearTimeout(shipTimeout);
    feeInput.value = 'Calculating...';
    statusIcon.innerHTML = '';
    shipTimeout = setTimeout(async () => {
        const address = addressInput.value.trim();
        if (address.length < 5) {
            feeInput.value = '';
            statusIcon.innerHTML = '';
            updateShippingFee(0);
            document.getElementById('shippingDetailDebug').innerText = '';
            return;
        }
        try {
            // Giả sử shopAddresses là mảng địa chỉ shop đã có sẵn
            const {shipFees, distances, methodNotes} = await calculateShippingGoong(address, shopAddresses);
            shopAddresses.forEach((address, idx) => {
                console.log(`Khoảng cách từ Shop ${idx + 1} (${address}) đến địa chỉ nhận: ${distances[idx].toFixed(2)} km`);
            });

            const totalShipFee = shipFees.reduce((sum, fee) => sum + fee, 0);
            shippingFee = totalShipFee;
            feeInput.value = totalShipFee.toLocaleString('en-US') + ' VND';
            statusIcon.innerHTML = '';
            document.getElementById('fee_ship_detail').innerText = totalShipFee.toLocaleString('en-US') + ' VND';
            updateFinalTotalUI();

        } catch (e) {
            console.error("Error calculating shipping fee:", e);
            feeInput.value = 'Cannot calculate';
            statusIcon.innerHTML = '';
            document.getElementById('fee_ship_detail').innerText = '--';
            shippingFee = 0;
            updateFinalTotalUI();
            document.getElementById('shippingDetailDebug').innerText = e.message || e;
        }
    }, 700);
});

function updateShippingFee(fee) {
    shippingFee = Number(fee) || 0;
    document.getElementById('fee_ship_detail').innerText = shippingFee ? shippingFee.toLocaleString('en-US') + ' VND' : '--';
    updateFinalTotalUI();
}

// ========== VOUCHER ==============
document.getElementById('applyVoucherBtn').onclick = function (e) {
    e.preventDefault();
    let code = document.getElementById('voucherInput').value.trim();
    let found = vouchers.find(v => v.code.toUpperCase() === code.toUpperCase());
    if (found) {
        updateVoucherDiscount(found.discountValue);
        showVoucherMessage("Voucher applied successfully!", true);
    } else {
        updateVoucherDiscount(0);
        showVoucherMessage("Invalid voucher code!", false);
    }
};

function updateVoucherDiscount(discount) {
    voucherDiscount = discount;
    document.getElementById('voucher_discount_detail').innerText = discount ? '-' + discount.toLocaleString('en-US') + ' VND' : '--';
    updateFinalTotalUI();
}

function showVoucherMessage(message, isSuccess) {
    const msgDiv = document.getElementById('voucherMessage');
    msgDiv.innerHTML = message;
    msgDiv.className = isSuccess ? 'alert alert-success mt-2' : 'alert alert-danger mt-2';
    setTimeout(() => {
        msgDiv.innerHTML = '';
        msgDiv.className = 'mt-2';
    }, 2500);
}

window.addEventListener("DOMContentLoaded", () => {
    updateFinalTotalUI();

    // Tự động tính ship nếu có địa chỉ sẵn
    const addressValue = addressInput.value.trim();
    if (addressValue.length >= 5) {
        // Gọi giống như sự kiện input nhưng không debounce, không timeout
        feeInput.value = 'Calculating...';
        statusIcon.innerHTML = '';
        (async () => {
            try {
                const {shipFees, distances, methodNotes} = await calculateShippingGoong(addressValue, shopAddresses);
                shopAddresses.forEach((address, idx) => {
                    console.log(`Khoảng cách từ Shop ${idx + 1} (${address}) đến địa chỉ nhận: ${distances[idx].toFixed(2)} km`);
                });

                const totalShipFee = shipFees.reduce((sum, fee) => sum + fee, 0);
                shippingFee = totalShipFee;
                feeInput.value = totalShipFee.toLocaleString('en-US') + ' VND';
                statusIcon.innerHTML = '';
                document.getElementById('fee_ship_detail').innerText = totalShipFee.toLocaleString('en-US') + ' VND';
                updateFinalTotalUI();

            } catch (e) {
                console.error("Error calculating shipping fee:", e);
                feeInput.value = 'Cannot calculate';
                statusIcon.innerHTML = '';
                document.getElementById('fee_ship_detail').innerText = '--';
                shippingFee = 0;
                updateFinalTotalUI();
                document.getElementById('shippingDetailDebug').innerText = e.message || e;
            }
        })();
    }
});
