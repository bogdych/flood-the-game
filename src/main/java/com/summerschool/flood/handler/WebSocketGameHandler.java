package com.summerschool.flood.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.summerschool.flood.game.FloodGame;
import com.summerschool.flood.game.GameType;
import com.summerschool.flood.game.IGame;
import com.summerschool.flood.game.Player;
import com.summerschool.flood.message.Message;
import com.summerschool.flood.message.MessageType;
import com.summerschool.flood.server.ServerData;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

@Data
@Component
public class WebSocketGameHandler extends TextWebSocketHandler {

    private final static Logger LOG = LoggerFactory.getLogger(WebSocketGameHandler.class);
    private final ServerData serverData;
    private final ObjectMapper mapper = new ObjectMapper();

    public WebSocketGameHandler(ServerData serverData) {
        this.serverData = serverData;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        serverData.getSessions().put(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        Message gameMessage = mapper.readValue((String) message.getPayload(), Message.class);

        if (gameMessage.getType() == MessageType.FIND_GAME) {
            // todo: payload info - find session via params (game params)

            Optional<IGame> game = serverData.getGames().stream()
                    .filter(g -> g.getPlayers() != null && g.getPlayers().size() < 4)
                    .findFirst();

            if (game.isPresent()) {
                game.get().getPlayers().add(new Player());
                LOG.info("Added player: " + session.getId());
            } else {
                // todo: pass game params
                // todo: game via type [switch]
                IGame gameSession = new FloodGame(GameType.STANDARD);
                gameSession.getPlayers().add(new Player());
                serverData.getGames().add(gameSession);
                LOG.info("Created game session: " + session.getId());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketSession sessionToClose = serverData.getSessions().remove(session.getId());
        if (sessionToClose != null) {
            sessionToClose.close(status);
        }
    }
}
