package com.summerschool.flood.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summerschool.flood.game.IGame;
import com.summerschool.flood.handler.WebSocketGameHandler;
import com.summerschool.flood.message.ErrorMessage;
import com.summerschool.flood.message.GameStateMessage;
import com.summerschool.flood.message.Message;
import com.summerschool.flood.message.MessageType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.time.Duration;
import java.time.Instant;

@AllArgsConstructor
@Component
public class CheckGamesTask implements Runnable {

    private final static Logger LOG = LoggerFactory.getLogger(GameService.class);
    private final GameService gameService;
    private final WebSocketGameHandler gameHandler;

    @Override
    @Scheduled(fixedDelay = 5000)
    public void run() {
        final Instant time = Instant.now();

        Object ready[] = gameService
                .getGames()
                .stream()
                .filter(game -> checkWaiting(game, time) && game.run(time, gameService.getWaitTime()))
                .toArray();

        ObjectMapper mapper = gameHandler.getMapper();

        for (Object object : ready) {
            IGame game = (IGame) object;
            LOG.info("Game: {} ready", game.getId());
            try {
                String readyMessage = mapper.writeValueAsString(new GameStateMessage(game.getState(), MessageType.GAME_READY));
                gameHandler.sendToAll(game, readyMessage);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private boolean checkWaiting(IGame game, Instant time) {
        return Duration.between(game.getLastPlayerTime(), time).getSeconds() >= gameService.getWaitTime();
    }

}
