package com.summerschool.flood.handler;

import com.summerschool.flood.info.PlayerInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketGameHandler extends TextWebSocketHandler {

    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        session.sendMessage(new TextMessage(new PlayerInfo(session.getId()).serialize()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        /*
            Later we need to ensure, that player leaves his active game session,
            whether it is active (if not - do nothing)
         */
        WebSocketSession sessionToClose = sessions.remove(session.getId());
        if (sessionToClose != null) {
            sessionToClose.close(status);
        }
    }
}
