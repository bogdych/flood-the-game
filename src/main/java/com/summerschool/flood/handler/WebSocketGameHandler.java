package com.summerschool.flood.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.summerschool.flood.game.GameAction;
import com.summerschool.flood.game.GameParams;
import com.summerschool.flood.game.PlayerInfo;
import com.summerschool.flood.message.Message;
import com.summerschool.flood.message.MessageType;
import com.summerschool.flood.server.GameService;
import com.summerschool.flood.server.IGameService;
import com.summerschool.flood.server.ServerData;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Data
@Component
public class WebSocketGameHandler extends TextWebSocketHandler {

    private final static Logger LOG = LoggerFactory.getLogger(WebSocketGameHandler.class);
    private final ServerData serverData;
    private final IGameService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public WebSocketGameHandler(ServerData serverData, GameService service) {
        this.serverData = serverData;
        this.service = service;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        serverData.getSessions().put(session.getId(), session);
        service.connect(session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String playerID = session.getId();
        Message gameMessage = mapper.readValue((String) message.getPayload(), Message.class);
        MessageType gameType = gameMessage.getType();

        switch (gameType) {

            case SET_PLAYER_INFO: {
                PlayerInfo playerInfo = mapper.convertValue(gameMessage.getPayload(), PlayerInfo.class);
                service.setPlayerInfo(playerID, playerInfo);
            } break;

            case FIND_GAME: {
                GameParams gameParams = mapper.convertValue(gameMessage.getPayload(), GameParams.class);
                service.findGame(playerID, gameParams);
            } break;

            case MAKE_ACTION: {
                GameAction gameAction = mapper.convertValue(gameMessage.getPayload(), GameAction.class);
                service.process(playerID, gameAction);
            } break;

        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketSession sessionToClose = serverData.getSessions().remove(session.getId());
        if (sessionToClose != null) {
            sessionToClose.close(status);
            service.disconnect(session.getId());
        }
    }
}
