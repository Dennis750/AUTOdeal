package ro.autodeal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ro.autodeal.dto.ChatMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage receivedMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        if (receivedMessage.getSender() == null || receivedMessage.getSender().isBlank()) {
            receivedMessage.setSender("Anonymous");
        }

        if (receivedMessage.getContent() == null || receivedMessage.getContent().isBlank()) {
            return;
        }

        ChatMessage messageToBroadcast = new ChatMessage(
                receivedMessage.getSender(),
                receivedMessage.getContent(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        String jsonMessage = objectMapper.writeValueAsString(messageToBroadcast);

        for (WebSocketSession connectedSession : sessions) {
            if (connectedSession.isOpen()) {
                connectedSession.sendMessage(new TextMessage(jsonMessage));
            }
        }
    }
}