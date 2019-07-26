package com.summerschool.flood.server;

import com.summerschool.flood.game.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

@Service
public class GameService implements IGameService {

    private final static Logger LOG = LoggerFactory.getLogger(GameService.class);
    private final ServiceData serviceData;

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
    public void findGame(String playerID, GameParams params) {
        /** ? what about concurrent access to player ? */

        Player player = findPlayer(playerID);

        if (getPlayerGameNullable(playerID) == null) {
            Optional<IGame> game = findAndAddIntoGame(playerID, player, params);

            if (game.isPresent()) {
                serviceData.getPlayerIDGameMap().put(playerID, game.get());
            } else {
                createGame(playerID, player, params);
            }
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

    private void createGame(String playerID, Player player, GameParams params) {
        switch (params.getGameName()) {
            case FLOOD:
                IGame gameSession = new FloodGame(params.getGameType(), 4);
                gameSession.addPlayer(player);
                IGame prevMapping = serviceData.getPlayerIDGameMap().putIfAbsent(playerID, gameSession);

                /* It means, that player has no current active game session */
                if (prevMapping == null) {
                    serviceData.getGames().add(gameSession);
                }
                LOG.info("Created game session: " + player.getId());

                break;
            default:
                throw new ServiceException("Invalid game name");
        }
    }

    private Optional<IGame> findAndAddIntoGame(String playerID, Player player, GameParams params) {
        Map<String,IGame> gameMap = serviceData.getPlayerIDGameMap();

        return serviceData.getGames().stream()
                .filter(g -> {
                    if (g.matchType(params) && g.canAddPlayer()) {
                        synchronized (g) {
                            if (g.canAddPlayer() && gameMap.putIfAbsent(playerID, g) == null) {
                                g.addPlayer(player);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                    return false;
                })
                .findFirst();
    }


}
