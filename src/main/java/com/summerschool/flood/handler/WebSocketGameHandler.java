package com.summerschool.flood.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.summerschool.flood.game.*;
import com.summerschool.flood.message.Message;
import com.summerschool.flood.message.MessageType;
import com.summerschool.flood.server.GameService;
import com.summerschool.flood.server.IGameService;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.summerschool.flood.game.GameStatus.READY;

@Data
@Component
public class WebSocketGameHandler extends TextWebSocketHandler {

    private final static Logger LOG = LoggerFactory.getLogger(WebSocketGameHandler.class);
    private final IGameService service;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();


    public WebSocketGameHandler(GameService service) {
        this.service = service;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        service.connect(session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String playerID = session.getId();
        try {
            Message gameMessage = mapper.readValue((String) message.getPayload(), Message.class);
            MessageType gameType = gameMessage.getType();

            switch (gameType) {

                case FIND_GAME: {
                    GameParams gameParams = mapper.convertValue(gameMessage.getPayload(), GameParams.class);
                    IGame game = service.findGame(playerID, gameParams);
                    if (game.getGameStatus() == READY) {
                        for (Player player : game.getPlayers()) {
                            WebSocketSession playerSession = sessions.get(player.getId());
                            playerSession.sendMessage(new TextMessage("ready")); // TODO add message start game message
                        }
                    }
                }
                break;

                case MAKE_ACTION: {
                    GameAction gameAction = mapper.convertValue(gameMessage.getPayload(), GameAction.class);
                    service.process(playerID, gameAction);
                }
                break;
            }
        } catch (Exception e) {
            String errorMessage = handleException(e);
            session.sendMessage(new TextMessage(errorMessage));
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

    private String handleException(Throwable ex) throws JsonProcessingException {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> details = new HashMap<>();
        details.put("time", getCurrentTimeStamp());
        details.put("message", ex.getMessage());
        Message message = new Message(MessageType.ERROR, details);
        return mapper.writeValueAsString(message);
    }

    private String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        return sdfDate.format(now);
    }
}
