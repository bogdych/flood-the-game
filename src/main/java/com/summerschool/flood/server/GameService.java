package com.summerschool.flood.server;

import com.summerschool.flood.game.*;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Data
public class GameService implements IGameService {

    private final static Logger LOG = LoggerFactory.getLogger(GameService.class);
    private final ServiceData serviceData;

    private final List<IGame> games = new CopyOnWriteArrayList<>();

    GameService(ServiceData serviceData) {
        this.serviceData = serviceData;
    }

    @Override
    public void connect(String playerID) {
        Player player = new Player();
        player.setId(playerID);
        serviceData.getPlayerIDMap().put(playerID, player);

        LOG.info("Connect player: " + playerID);
    }

    @Override
    public void setPlayerInfo(String playerID, PlayerInfo info) {
        Player player = findPlayer(playerID);
        String newNickname = info.getNickname();
        player.setNickname(newNickname);

        LOG.info("Set player: " + playerID + " info: " + newNickname);
    }

    @Override
    public IGame findGame(String playerID, GameParams params) {
        Player player = findPlayer(playerID);

        if (player.getActiveGame() == null) {
            IGame game = games.stream()
                    .filter(g -> g.matchType(params) && g.addPlayer(player))
                    .findFirst()
                    .orElseGet(() -> createGame(player, params));
            player.setActiveGame(game);
            return game;
        } else {
            throw new ServiceException("Player has active game session " + playerID);
        }
    }

    @Override
    public boolean reconnect(String playerID, String playerNickname) throws ServiceException {
        return false;
    }

    @Override
    public void process(String playerID, GameAction action) throws ServiceException {
        LOG.info("Process: " + playerID);

        Player player = findPlayer(playerID);
        IGame game = getPlayerGame(playerID);

        synchronized (game) {
            Result result = game.makeAction(player, action);
            // todo: do something with result
        }

        LOG.info("Process player: " + playerID);
    }

    @Override
    public void disconnect(String playerID) throws ServiceException {
        // todo: add player in disconnect timeout list
        Player player = findPlayer(playerID);
        IGame game = getPlayerGameNullable(playerID);

        if (game != null) {
            synchronized (game) {
                game.removePlayer(player);
                // todo: if it was the last player in game: remove game from game list
            }
        }

        removeFromEverywhere(player);

        LOG.info("Disconnect player: " + playerID + " nickname: " + player.getNickname());
    }

    @Override
    public Serializable getPlayerInfo(String playerID) throws ServiceException {
        return serviceData.getPlayerIDMap().get(playerID);
    }

    @Override
    public Serializable getGameState(String playerID) throws ServiceException {
        return getPlayerGame(playerID).getGameState();
    }

    @Override
    public Serializable getGameStatus(String playerID) throws ServiceException {
        return getPlayerGame(playerID).getGameStatus();
    }

    private Player findPlayer(String playerID) {
        Player player = serviceData.getPlayerIDMap().get(playerID);
        if (player == null) {
            throw new ServiceException("Unconnected player access");
        }
        return player;
    }

    private void removeFromEverywhere(Player player) {
        String id = player.getId();

        serviceData.getPlayerIDMap().remove(id);
        serviceData.getPlayerIDGameMap().remove(id);
    }

    private IGame getPlayerGameNullable(String playerID) {
        return serviceData.getPlayerIDGameMap().get(playerID);
    }

    private IGame getPlayerGame(String playerID) {
        IGame game = serviceData.getPlayerIDGameMap().get(playerID);

        if (game == null) {
            throw new ServiceException("No active session for player: " + playerID);
        }

        return game;
    }

    private IGame createGame(Player player, GameParams params) {
        switch (params.getGameName()) {
            case FLOOD:
                IGame game = new FloodGame(params.getGameType(), 4);
                game.addPlayer(player);
                games.add(game);
                LOG.info("Created game session: " + player.getId());
                return game;
            default:
                throw new ServiceException("Invalid game name");
        }
    }

}
