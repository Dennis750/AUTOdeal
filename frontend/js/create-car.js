const createCarForm = document.getElementById("createCarForm");

async function loadBrandsForCreateForm() {
    const brandSelect = document.getElementById("brandId");
    const modelSelect = document.getElementById("modelId");

    const brands = await apiGet(`${CAR_API_URL}/brands`);

    brandSelect.innerHTML = `<option value="">Select brand</option>`;

    brands.forEach(brand => {
        brandSelect.innerHTML += `<option value="${brand.id}">${brand.name}</option>`;
    });

    brandSelect.addEventListener("change", async () => {
        modelSelect.innerHTML = `<option value="">Select model</option>`;
        modelSelect.disabled = true;

        if (!brandSelect.value) {
            return;
        }

        const models = await apiGet(`${CAR_API_URL}/brands/${brandSelect.value}/models`);

        models.forEach(model => {
            modelSelect.innerHTML += `<option value="${model.id}">${model.name}</option>`;
        });

        modelSelect.disabled = false;
    });
}

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
            document.getElementById("modelId").innerHTML = `<option value="">Select model</option>`;
            document.getElementById("modelId").disabled = true;
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

document.addEventListener("DOMContentLoaded", loadBrandsForCreateForm);