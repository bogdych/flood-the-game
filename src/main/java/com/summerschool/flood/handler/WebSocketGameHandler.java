package com.summerschool.flood.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.summerschool.flood.game.GameParams;
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

import java.util.Map;

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
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Message gameMessage = mapper.readValue((String) message.getPayload(), Message.class);

        if (gameMessage.getType() == MessageType.FIND_GAME) {
            // todo: payload info - find session via params (game params)
            // service.findGame(session.getId(), params);

            System.out.println(gameMessage.getPayload().get("gameParams").getClass());
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
