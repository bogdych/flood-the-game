package com.summerschool.flood.config;

import com.summerschool.flood.handler.WebSocketGameHandler;
import com.summerschool.flood.handler.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;
    private final WebSocketGameHandler webSocketGameHandler;

    public WebSocketConfig(WebSocketHandler webSocketHandler,
                           WebSocketGameHandler webSocketGameHandler) {
        this.webSocketHandler = webSocketHandler;
        this.webSocketGameHandler = webSocketGameHandler;
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/chat").setAllowedOrigins("*") ;
        registry.addHandler(webSocketGameHandler, "/game").setAllowedOrigins("*");
    }
}
