package com.summerschool.flood.server;

import com.summerschool.flood.game.*;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;

@Data
@Service
public class GameService implements IGameService {

    private final static Logger LOG = LoggerFactory.getLogger(GameService.class);
    private final ServerData serverData;

    GameService(ServerData serverData) {
        this.serverData = serverData;
    }

    @Override
    public void findGame(String playerID, GameParams params) {
        Optional<IGame> game = serverData.getGames().stream()
                .filter(g -> g.getPlayers() != null && g.getPlayers().size() < 4)
                .findFirst();

        if (game.isPresent()) {
            // todo: some response
        } else {
            // todo: pass game params
            // todo: game via type [switch]
            IGame gameSession = new FloodGame(params.getGameType(), 4);
            gameSession.getPlayers().add(new Player());
            serverData.getGames().add(gameSession);
            LOG.info("Created game session: " + playerID);
        }
    }

    @Override
    public boolean reconnect(String playerID, String playerNickname) throws ServiceException {
        return false;
    }

    @Override
    public void process(String playerID, Action action) throws ServiceException {
        try {
            Player player = serverData.getPlayerIDMap().get(playerID);
            IGame game = player.getCurrentGame();
            Result result = game.makeAction(player, action);

            // todo: do something with result

        } catch (NullPointerException e) {

        }
    }

    @Override
    public void disconnect(String playerID) throws ServiceException {
        // todo: add player in disconnect timeout list
    }

    @Override
    public Serializable getPlayerInfo(String playerID) throws ServiceException {
        return serverData.getPlayerIDMap().get(playerID);
    }

    @Override
    public Serializable getGameState(String playerID) throws ServiceException {
        return null;
    }

    @Override
    public Serializable getGameStatus(String playerID) throws ServiceException {
        return null;
    }

}
