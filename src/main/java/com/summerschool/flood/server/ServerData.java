package com.summerschool.flood.server;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class ServerData {

    /** Connection session for WS game handler */
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

}
