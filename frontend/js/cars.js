function updateExportButtonsVisibility() {
    const exportButtons = document.getElementById("exportButtons");
    const currentUser = localStorage.getItem("currentUser");

    if (!exportButtons) {
        return;
    }

    if (currentUser) {
        exportButtons.style.display = "flex";
    } else {
        exportButtons.style.display = "none";
    }
}

async function loadCars() {
    const carsContainer = document.getElementById("carsContainer");
    const message = document.getElementById("message");

    carsContainer.innerHTML = "";
    message.innerHTML = "Loading cars...";

    const minPrice = document.getElementById("minPrice").value;
    const maxPrice = document.getElementById("maxPrice").value;
    const year = document.getElementById("year").value;
    const fuelType = document.getElementById("fuelType").value;
    const sortBy = document.getElementById("sortBy").value;

    let url = `${CAR_API_URL}/cars?page=0&size=20`;

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
            card.className = "card";

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

            card.innerHTML = `
                <h3>${car.brand} ${car.model}</h3>
                <p><strong>ID:</strong> ${car.id}</p>
                <p><strong>Year:</strong> ${car.year}</p>
                <p><strong>Price:</strong> ${car.price} EUR</p>
                <p><strong>Mileage:</strong> ${car.mileage} km</p>
                <p><strong>Fuel:</strong> ${car.fuelType}</p>
                <p><strong>Horsepower:</strong> ${car.horsepower} HP</p>
                <p><strong>City:</strong> ${car.city}</p>
                <p><strong>Seller:</strong> ${car.sellerUsername}</p>

                <div id="${contactId}" class="contact-box"></div>

                ${actionButtons}
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

    const minPrice = document.getElementById("minPrice").value;
    const maxPrice = document.getElementById("maxPrice").value;
    const year = document.getElementById("year").value;
    const fuelType = document.getElementById("fuelType").value;
    const sortBy = document.getElementById("sortBy").value;

    let url = `http://localhost:8081/cars/export/${format}`;

    const params = new URLSearchParams();

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

document.addEventListener("DOMContentLoaded", () => {
    updateExportButtonsVisibility();
    loadCars();
});