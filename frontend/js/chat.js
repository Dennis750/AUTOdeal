let socket = null;

function connectToChat() {
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

        const input = document.getElementById("chatInput");
        const content = input.value.trim();

        if (!content) {
            return;
        }

        const sender = localStorage.getItem("currentUser") || "Anonymous";

        const message = {
            sender: sender,
            content: content
        };

        socket.send(JSON.stringify(message));
        input.value = "";
    });
}

document.addEventListener("DOMContentLoaded", connectToChat);