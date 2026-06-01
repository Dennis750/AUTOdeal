function updateNavbar() {
    const nav = document.querySelector("nav");

    if (!nav) {
        return;
    }

    const currentUser = localStorage.getItem("currentUser");
    const currentRole = localStorage.getItem("currentRole");

    const links = nav.querySelectorAll("a");

    links.forEach(link => {
        const href = link.getAttribute("href");

        if (!currentUser) {
            // Visitor: can see Home, Cars, Login, Register
            // Visitor cannot see Create Car or Chat
            if (href === "create-car.html" || href === "chat.html") {
                link.style.display = "none";
            } else {
                link.style.display = "inline";
            }
        } else {
            // Logged-in user: hide Login and Register
            if (href === "login.html" || href === "register.html") {
                link.style.display = "none";
            } else {
                link.style.display = "inline";
            }

            // Only SELLER and ADMIN can see Create Car
            if (href === "create-car.html" && currentRole !== "SELLER" && currentRole !== "ADMIN") {
                link.style.display = "none";
            }

            // Any logged-in user can see Chat
            if (href === "chat.html") {
                link.style.display = "inline";
            }
        }
    });

    const existingAuthBox = document.getElementById("authBox");

    if (existingAuthBox) {
        existingAuthBox.remove();
    }

    const authBox = document.createElement("div");
    authBox.id = "authBox";
    authBox.className = "auth-box";

    if (currentUser) {
        authBox.innerHTML = `
            <span class="logged-user">Logged in as ${currentUser} (${currentRole})</span>
            <button onclick="logoutUser()">Logout</button>
        `;
    } else {
        authBox.innerHTML = `
            <span class="logged-user">Not logged in</span>
        `;
    }

    nav.appendChild(authBox);

    const heroAuthLinks = document.querySelectorAll(".hero-auth-link");

    heroAuthLinks.forEach(link => {
        if (currentUser) {
            link.style.display = "none";
        } else {
            link.style.display = "inline-block";
        }
    });
}

function logoutUser() {
    localStorage.removeItem("currentUser");
    localStorage.removeItem("currentRole");

    window.location.href = "index.html";
}

document.addEventListener("DOMContentLoaded", updateNavbar);