const registerForm = document.getElementById("registerForm");

if (registerForm) {
    registerForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const message = document.getElementById("message");

        const data = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value,
            confirmPassword: document.getElementById("confirmPassword").value,
            name: document.getElementById("name").value,
            phoneNumber: document.getElementById("phoneNumber").value,
            email: document.getElementById("email").value,
            role: document.getElementById("role").value
        };

        try {
            const user = await apiPost(`${USER_API_URL}/users/register`, data);

            localStorage.setItem("currentUser", user.username);
            localStorage.setItem("currentRole", user.role);

            message.innerHTML = `
            <div class="success">
                User registered successfully. Logged in as ${user.username}. Redirecting to cars...
            </div>
            `;

            registerForm.reset();

            setTimeout(() => {
                window.location.href = "cars.html";
            }, 1000);
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

const loginForm = document.getElementById("loginForm");

if (loginForm) {
    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const message = document.getElementById("message");

        const data = {
            username: document.getElementById("loginUsername").value,
            password: document.getElementById("loginPassword").value
        };

        try {
            const user = await apiPost(`${USER_API_URL}/users/login`, data);

            localStorage.setItem("currentUser", user.username);
            localStorage.setItem("currentRole", user.role);

            message.innerHTML = `
            <div class="success">
                Logged in successfully as ${user.username} (${user.role}). Redirecting to cars...
            </div>
            `;

            loginForm.reset();

            setTimeout(() => {
                window.location.href = "cars.html";
            }, 1000);
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