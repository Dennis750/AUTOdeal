const CAR_API_URL = "http://localhost:8081/api";
const USER_API_URL = "http://localhost:8082/api";

async function apiGet(url) {
    const response = await fetch(url);

    if (!response.ok) {
        await handleApiError(response);
    }

    return response.json();
}

async function apiPost(url, data) {
    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        await handleApiError(response);
    }

    return response.json();
}

async function apiPut(url, data) {
    const response = await fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        await handleApiError(response);
    }

    return response.json();
}

async function apiDelete(url) {
    const response = await fetch(url, {
        method: "DELETE"
    });

    if (!response.ok) {
        await handleApiError(response);
    }
}

async function handleApiError(response) {
    let errorMessage = "Request failed with status " + response.status;

    try {
        const error = await response.json();

        if (error.message) {
            errorMessage = error.message;
        }
    } catch (parseError) {
        // If the backend did not return JSON, keep the default message.
    }

    throw new Error(errorMessage);
}