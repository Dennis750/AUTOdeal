let currentLanguage = localStorage.getItem("language") || "en";

async function loadLanguage(language) {
    try {
        const response = await fetch(`lang/${language}.json`);
        const translations = await response.json();

        document.querySelectorAll("[data-i18n]").forEach(element => {
            const key = element.getAttribute("data-i18n");

            if (translations[key]) {
                element.textContent = translations[key];
            }
        });

        localStorage.setItem("language", language);
        currentLanguage = language;
    } catch (error) {
        console.error("Language file could not be loaded", error);
    }
}

function setLanguage(language) {
    loadLanguage(language);
}

document.addEventListener("DOMContentLoaded", () => {
    loadLanguage(currentLanguage);
});