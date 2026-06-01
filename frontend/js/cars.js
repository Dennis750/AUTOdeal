function updateExportButtonsVisibility() {
    const exportButtons = document.getElementById("exportButtons");
    const currentUser = localStorage.getItem("currentUser");

    if (!exportButtons) {
        return;
    }

    exportButtons.style.display = currentUser ? "block" : "none";
}

function formatPrice(price) {
    if (price === null || price === undefined) {
        return "Price unavailable";
    }

    return Number(price).toLocaleString("de-DE") + " €";
}

function formatMileage(mileage) {
    if (mileage === null || mileage === undefined) {
        return "N/A";
    }

    return Number(mileage).toLocaleString("de-DE") + " km";
}

async function loadBrandFilters() {
    const brandFilter = document.getElementById("brandFilter");

    if (!brandFilter) {
        return;
    }

    const brands = await apiGet(`${CAR_API_URL}/brands`);

    brandFilter.innerHTML = `<option value="">All brands</option>`;

    brands.forEach(brand => {
        brandFilter.innerHTML += `<option value="${brand.id}">${brand.name}</option>`;
    });
}

async function loadModelFilters() {
    const brandFilter = document.getElementById("brandFilter");
    const modelFilter = document.getElementById("modelFilter");

    modelFilter.innerHTML = `<option value="">All models</option>`;
    modelFilter.disabled = true;

    if (!brandFilter.value) {
        return;
    }

    const models = await apiGet(`${CAR_API_URL}/brands/${brandFilter.value}/models`);

    models.forEach(model => {
        modelFilter.innerHTML += `<option value="${model.id}">${model.name}</option>`;
    });

    modelFilter.disabled = false;
}

async function loadCars() {
    const carsContainer = document.getElementById("carsContainer");
    const message = document.getElementById("message");

    carsContainer.innerHTML = "";
    message.innerHTML = "Loading cars...";

    const brandId = document.getElementById("brandFilter").value;
    const modelId = document.getElementById("modelFilter").value;
    const minPrice = document.getElementById("minPrice").value;
    const maxPrice = document.getElementById("maxPrice").value;
    const year = document.getElementById("year").value;
    const fuelType = document.getElementById("fuelType").value;
    const sortBy = document.getElementById("sortBy").value;

    let url = `${CAR_API_URL}/cars?page=0&size=20`;

    if (brandId) {
        url += `&brandId=${brandId}`;
    }

    if (modelId) {
        url += `&modelId=${modelId}`;
    }

    if (minPrice) {
        url += `&minPrice=${minPrice}`;
    }

    if (maxPrice) {
        url += `&maxPrice=${maxPrice}`;
    }

    if (year) {
        url += `&year=${year}`;
    }

    if (fuelType) {
        url += `&fuelType=${fuelType}`;
    }

    if (sortBy) {
        url += `&sortBy=${sortBy}`;
    }

    try {
        const cars = await apiGet(url);

        message.innerHTML = "";

        if (cars.length === 0) {
            carsContainer.innerHTML = "<p>No cars found.</p>";
            return;
        }

        for (const car of cars) {
            const card = document.createElement("div");
            card.className = "card car-card";

            const currentUser = localStorage.getItem("currentUser");
            const currentRole = localStorage.getItem("currentRole");

            let actionButtons = "";

            const isAdmin = currentRole === "ADMIN";
            const isOwner = currentUser && car.sellerUsername === currentUser;

            if (isAdmin || isOwner) {
                actionButtons = `
                    <div class="card-actions">
                        <a class="secondary-button" href="edit-car.html?id=${car.id}">
                            Edit
                        </a>

                        <button class="danger-button" onclick="deleteCar(${car.id})">
                            Delete
                        </button>
                    </div>
                `;
            }

            const contactId = `contact-${car.id}`;

            const imageHtml = car.imageUrl
                ? `<img class="car-card-image" src="${car.imageUrl}" alt="${car.brand} ${car.model}">`
                : `<div class="car-card-placeholder">
                        <span>${car.brand}</span>
                   </div>`;

            card.innerHTML = `
                ${imageHtml}

                <div class="car-card-body">
                    <div class="car-card-top">
                        <div>
                            <p class="car-brand">${car.brand}</p>
                            <h3>${car.brand} ${car.model}</h3>
                        </div>

                        <span class="car-id">#${car.id}</span>
                    </div>

                    <div class="car-price">
                        ${formatPrice(car.price)}
                    </div>

                    <div class="car-spec-grid">
                        <div class="car-spec">
                            <span>Year</span>
                            <strong>${car.year}</strong>
                        </div>

                        <div class="car-spec">
                            <span>Mileage</span>
                            <strong>${formatMileage(car.mileage)}</strong>
                        </div>

                        <div class="car-spec">
                            <span>Fuel</span>
                            <strong>${car.fuelType}</strong>
                        </div>

                        <div class="car-spec">
                            <span>Power</span>
                            <strong>${car.horsepower} HP</strong>
                        </div>
                    </div>

                    <div class="car-location">
                        <span>Location</span>
                        <strong>${car.city}</strong>
                    </div>

                    <div class="car-seller">
                        Seller: <strong>${car.sellerUsername}</strong>
                    </div>

                    <div id="${contactId}" class="contact-box"></div>

                    ${actionButtons}
                </div>
            `;

            carsContainer.appendChild(card);

            displaySellerContact(car.sellerUsername, contactId);
        }
    } catch (error) {
        console.error(error);
        message.innerHTML = `
            <div class="error">
                ${error.message}
            </div>
        `;
    }
}

async function displaySellerContact(sellerUsername, contactElementId) {
    const contactBox = document.getElementById(contactElementId);
    const currentUser = localStorage.getItem("currentUser");

    if (!contactBox) {
        return;
    }

    if (!currentUser) {
        contactBox.innerHTML = `
            <div class="hidden-contact">
                Log in to view seller phone number and email.
            </div>
        `;
        return;
    }

    try {
        const seller = await apiGet(`${USER_API_URL}/users/${sellerUsername}`);

        contactBox.innerHTML = `
            <div class="seller-contact">
                <p><strong>Email:</strong> ${seller.email || "Not available"}</p>
                <p><strong>Phone:</strong> ${seller.phoneNumber || "Not available"}</p>
            </div>
        `;
    } catch (error) {
        contactBox.innerHTML = `
            <div class="seller-contact">
                <p><strong>Contact:</strong> Not available</p>
            </div>
        `;
    }
}

async function deleteCar(carId) {
    const message = document.getElementById("message");

    const currentUser = localStorage.getItem("currentUser");

    if (!currentUser) {
        message.innerHTML = `
            <div class="error">
                You must be logged in to delete a car post.
            </div>
        `;
        return;
    }

    const confirmed = confirm("Are you sure you want to delete this car post?");

    if (!confirmed) {
        return;
    }

    try {
        await apiDelete(`${CAR_API_URL}/cars/${carId}?sellerUsername=${encodeURIComponent(currentUser)}`);

        message.innerHTML = `
            <div class="success">
                Car post deleted successfully.
            </div>
        `;

        loadCars();
    } catch (error) {
        console.error(error);

        message.innerHTML = `
            <div class="error">
                ${error.message}
            </div>
        `;
    }
}

function clearFilters() {
    document.getElementById("brandFilter").value = "";
    document.getElementById("modelFilter").innerHTML = `<option value="">All models</option>`;
    document.getElementById("modelFilter").disabled = true;
    document.getElementById("minPrice").value = "";
    document.getElementById("maxPrice").value = "";
    document.getElementById("year").value = "";
    document.getElementById("fuelType").value = "";
    document.getElementById("sortBy").value = "";

    loadCars();
}

function exportCars(format) {
    const currentUser = localStorage.getItem("currentUser");

    if (!currentUser) {
        const message = document.getElementById("message");

        message.innerHTML = `
            <div class="error">
                You must be logged in to export car data.
            </div>
        `;

        return;
    }

    const brandId = document.getElementById("brandFilter").value;
    const modelId = document.getElementById("modelFilter").value;
    const minPrice = document.getElementById("minPrice").value;
    const maxPrice = document.getElementById("maxPrice").value;
    const year = document.getElementById("year").value;
    const fuelType = document.getElementById("fuelType").value;
    const sortBy = document.getElementById("sortBy").value;

    let url = `http://localhost:8081/cars/export/${format}`;

    const params = new URLSearchParams();

    if (brandId) {
        params.append("brandId", brandId);
    }

    if (modelId) {
        params.append("modelId", modelId);
    }

    if (minPrice) {
        params.append("minPrice", minPrice);
    }

    if (maxPrice) {
        params.append("maxPrice", maxPrice);
    }

    if (year) {
        params.append("year", year);
    }

    if (fuelType) {
        params.append("fuelType", fuelType);
    }

    if (sortBy) {
        params.append("sortBy", sortBy);
    }

    const queryString = params.toString();

    if (queryString) {
        url += `?${queryString}`;
    }

    window.location.href = url;
}

document.addEventListener("DOMContentLoaded", async () => {
    updateExportButtonsVisibility();
    await loadBrandFilters();

    document.getElementById("brandFilter").addEventListener("change", async () => {
        await loadModelFilters();
        loadCars();
    });

    document.getElementById("modelFilter").addEventListener("change", loadCars);

    loadCars();
});