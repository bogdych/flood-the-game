package com.summerschool.flood.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.summerschool.flood.game.FloodGame;
import com.summerschool.flood.game.GameType;
import com.summerschool.flood.game.IGame;
import com.summerschool.flood.game.Player;
import com.summerschool.flood.server.Message;
import com.summerschool.flood.server.MessageType;
import com.summerschool.flood.server.ServerData;

import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Optional;

@Data
@Component
public class WebSocketGameHandler extends TextWebSocketHandler {

    @Autowired
    private ServerData serverData;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        serverData.getSessions().put(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        Message gameMessage;

        try {
            gameMessage = mapper.readValue((String) message.getPayload(), Message.class);
        } catch (IOException e) {
            return;
        }

        if (gameMessage.getType() == MessageType.FIND_GAME) {
            // todo: payload info - find session via params (game params)

            Optional<IGame> game = serverData.getGames().stream()
                    .filter(g -> g.getPlayers() != null && g.getPlayers().size() < 4)
                    .findFirst();

            if (game.isPresent()) {
                game.get().getPlayers().add(new Player());
                System.out.println("Add player: " + session.getId());
            } else {
                // todo: pass game params
                // todo: game via type [switch]
                IGame gameSession = new FloodGame(GameType.STANDARD);
                gameSession.getPlayers().add(new Player());
                serverData.getGames().add(gameSession);
                System.out.println("Create game session: " + session.getId());
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
