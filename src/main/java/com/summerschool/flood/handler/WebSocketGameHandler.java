package com.summerschool.flood.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.summerschool.flood.game.*;
import com.summerschool.flood.message.*;
import com.summerschool.flood.server.GameService;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class WebSocketGameHandler extends TextWebSocketHandler {

    private final static Logger LOG = LoggerFactory.getLogger(WebSocketGameHandler.class);
    private final GameService service;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public WebSocketGameHandler(GameService service) {
        this.service = service;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        sessions.put(session.getId(), session);
        service.connect(session.getId());
        session.sendMessage(new TextMessage(String.format("{\"id\":\"%s\"}", session.getId())));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String playerID = session.getId();
        try {
            Message gameMessage = mapper.readValue((String) message.getPayload(), Message.class);
            MessageType gameType = gameMessage.getType();

            switch (gameType) {

                case FIND_GAME: {
                    IGame game = service.findGame(playerID, (FindGameMessage) gameMessage);
                    if (game.isReady()) {
                        String readyMessage = mapper.writeValueAsString(new GameStateMessage(game.getState(), MessageType.GAME_READY));
                        sendToAll(game, readyMessage);

                        LOG.info("Game is ready UUID: {}", game.getId());
                    }
                }
                break;

                case MAKE_ACTION: {
                    IGame game = service.process(playerID, (MakeActionMessage) gameMessage);
                    String turnEndMessage = mapper.writeValueAsString(new GameStateMessage(game.getState(), MessageType.TURN_END));
                    sendToAll(game, turnEndMessage);
                }
                break;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            Message err = new ErrorMessage(e.getMessage());
            String errorMessage = mapper.writeValueAsString(err);
            session.sendMessage(new TextMessage(errorMessage));
        }
    }

    private void sendToAll(IGame game, String message) throws IOException {
        for (Player player : game.getPlayers()) {
            WebSocketSession playerSession = sessions.get(player.getId());
            playerSession.sendMessage(new TextMessage(message));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketSession sessionToClose = sessions.remove(session.getId());
        if (sessionToClose != null) {
            sessionToClose.close(status);
            service.disconnect(session.getId());
        }
    }
}
