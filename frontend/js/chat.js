let socket = null;

function requireLoginForChat() {
    const currentUser = localStorage.getItem("currentUser");

    if (!currentUser) {
        const status = document.getElementById("chatStatus");
        const chatForm = document.getElementById("chatForm");
        const messages = document.getElementById("chatMessages");

        if (status) {
            status.className = "error";
            status.innerHTML = "You must be logged in to access the chat.";
        }

        if (chatForm) {
            chatForm.style.display = "none";
        }

        if (messages) {
            messages.innerHTML = `
                <div class="hidden-contact">
                    Please log in first to use the AUTOdeal real-time chat.
                </div>
            `;
        }

        setTimeout(() => {
            window.location.href = "login.html";
        }, 1500);

        return false;
    }

    return true;
}

function connectToChat() {
    if (!requireLoginForChat()) {
        return;
    }

    const status = document.getElementById("chatStatus");
    const messages = document.getElementById("chatMessages");

    socket = new WebSocket("ws://localhost:8081/ws/chat");

    socket.onopen = function () {
        status.className = "success";
        status.innerHTML = "Connected to chat server.";
    };

    socket.onmessage = function (event) {
        const message = JSON.parse(event.data);

        const messageElement = document.createElement("div");
        messageElement.className = "chat-message";

        messageElement.innerHTML = `
            <strong>${message.sender}</strong>
            <span class="chat-time">${message.timestamp}</span>
            <p>${message.content}</p>
        `;

        messages.appendChild(messageElement);
        messages.scrollTop = messages.scrollHeight;
    };

    socket.onerror = function () {
        status.className = "error";
        status.innerHTML = "Chat connection error.";
    };

    socket.onclose = function () {
        status.className = "error";
        status.innerHTML = "Disconnected from chat server.";
    };
}

const chatForm = document.getElementById("chatForm");

if (chatForm) {
    chatForm.addEventListener("submit", function (event) {
        event.preventDefault();

        if (!socket || socket.readyState !== WebSocket.OPEN) {
            const status = document.getElementById("chatStatus");
            status.className = "error";
            status.innerHTML = "Chat is not connected.";
            return;
        }

        const input = document.getElementById("chatInput");
        const content = input.value.trim();

        if (!content) {
            return;
        }

        const sender = localStorage.getItem("currentUser");

        const message = {
            sender: sender,
            content: content
        };

        socket.send(JSON.stringify(message));
        input.value = "";
    });
}

document.addEventListener("DOMContentLoaded", connectToChat);