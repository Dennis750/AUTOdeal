const editCarForm = document.getElementById("editCarForm");
const message = document.getElementById("message");
const currentCarInfo = document.getElementById("currentCarInfo");

const params = new URLSearchParams(window.location.search);
const carId = params.get("id");

async function loadCarForEdit() {
    if (!carId) {
        message.innerHTML = `
            <div class="error">
                No car ID was provided.
            </div>
        `;
        editCarForm.style.display = "none";
        return;
    }

    const currentUser = localStorage.getItem("currentUser");
    const currentRole = localStorage.getItem("currentRole");

    if (!currentUser) {
        message.innerHTML = `
            <div class="error">
                You must log in before editing a car post.
            </div>
        `;
        editCarForm.style.display = "none";
        return;
    }

    try {
        const car = await apiGet(`${CAR_API_URL}/cars/${carId}`);

        const isAdmin = currentRole === "ADMIN";
        const isOwner = car.sellerUsername === currentUser;

        if (!isAdmin && !isOwner) {
            message.innerHTML = `
                <div class="error">
                    You can edit only your own posts.
                </div>
            `;
            editCarForm.style.display = "none";
            return;
        }

        currentCarInfo.innerHTML = `Editing: ${car.brand} ${car.model}, owned by ${car.sellerUsername}`;

        document.getElementById("brandId").value = car.brandId;
        document.getElementById("modelId").value = car.modelId;
        document.getElementById("year").value = car.year;
        document.getElementById("price").value = car.price;
        document.getElementById("mileage").value = car.mileage;
        document.getElementById("fuelType").value = car.fuelType;
        document.getElementById("horsepower").value = car.horsepower;
        document.getElementById("city").value = car.city;
        document.getElementById("imageUrl").value = car.imageUrl || "";
    } catch (error) {
        console.error(error);

        message.innerHTML = `
            <div class="error">
                ${error.message}
            </div>
        `;

        editCarForm.style.display = "none";
    }
}

if (editCarForm) {
    editCarForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const currentUser = localStorage.getItem("currentUser");
        const currentRole = localStorage.getItem("currentRole");

        if (!currentUser) {
            message.innerHTML = `
                <div class="error">
                    You must log in before editing a car post.
                </div>
            `;
            return;
        }

        if (currentRole !== "SELLER" && currentRole !== "ADMIN") {
            message.innerHTML = `
                <div class="error">
                    Only sellers or admins can edit car posts.
                </div>
            `;
            return;
        }

        const data = {
            sellerUsername: currentUser,
            brandId: Number(document.getElementById("brandId").value),
            modelId: Number(document.getElementById("modelId").value),
            year: Number(document.getElementById("year").value),
            price: Number(document.getElementById("price").value),
            mileage: Number(document.getElementById("mileage").value),
            fuelType: document.getElementById("fuelType").value,
            horsepower: Number(document.getElementById("horsepower").value),
            city: document.getElementById("city").value,
            imageUrl: document.getElementById("imageUrl").value
        };

        try {
            const updatedCar = await apiPut(`${CAR_API_URL}/cars/${carId}`, data);

            message.innerHTML = `
                <div class="success">
                    Car post updated successfully: ${updatedCar.brand} ${updatedCar.model}.
                </div>
            `;
        } catch (error) {
            console.error(error);

            message.innerHTML = `
                <div class="error">
                    ${error.message}
                </div>
            `;
        }
    });
}

document.addEventListener("DOMContentLoaded", loadCarForEdit);