const createCarForm = document.getElementById("createCarForm");

if (createCarForm) {
    createCarForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const message = document.getElementById("message");

        const currentUser = localStorage.getItem("currentUser");
        const currentRole = localStorage.getItem("currentRole");

        if (!currentUser) {
            message.innerHTML = `
                <div class="error">
                    You must log in before creating a car post.
                </div>
            `;
            return;
        }

        if (currentRole !== "SELLER" && currentRole !== "ADMIN") {
            message.innerHTML = `
                <div class="error">
                    Only sellers or admins can create car posts.
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
            const createdCar = await apiPost(`${CAR_API_URL}/cars`, data);

            message.innerHTML = `
                <div class="success">
                    Car post created successfully: ${createdCar.brand} ${createdCar.model}.
                </div>
            `;

            createCarForm.reset();
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