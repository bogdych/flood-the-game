package com.summerschool.flood.server;

import com.summerschool.flood.game.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;

@Service
public class GameService implements IGameService {

    private final static Logger LOG = LoggerFactory.getLogger(GameService.class);
    private final ServerData serverData;

    GameService(ServerData serverData) {
        this.serverData = serverData;
    }

    @Override
    public void connect(String playerID) {
        Player player = new Player();
        player.setId(playerID);
        serverData.getPlayerIDMap().put(playerID, player);

        LOG.info("Connect player: " + playerID);
    }

    @Override
    public void setPlayerInfo(String playerID, PlayerInfo info) {
        Player player = findPlayer(playerID);

        String oldNickname;
        String newNickname;

        synchronized (player) {
            oldNickname = player.getNickname();
            newNickname = info.getNickname();

            if (oldNickname != null) serverData.getPlayerMap().remove(oldNickname);

            player.setNickname(newNickname);

            // todo: check nickname [must be unique]
            serverData.getPlayerMap().put(newNickname, player);
        }

        LOG.info("Set player: " + playerID + " info: " + newNickname);
    }

    @Override
    public void findGame(String playerID, GameParams params) {
        /** ? what about concurrent access to player ? */

        Player player = findPlayer(playerID);

        synchronized (player) {

            if (getPlayerGameNullable(playerID) == null) {

                Optional<IGame> game = serverData.getGames().stream()
                        .filter(g -> {
                            if (g.matchType(params) && g.canAddPLayer()) {
                                synchronized (g) {
                                    if (g.canAddPLayer()) {
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

                if (game.isPresent()) {
                    serverData.getPlayerIDGameMap().put(playerID, game.get());
                } else {
                    switch (params.getGameName()) {
                        case FLOOD:
                            IGame gameSession = new FloodGame(params.getGameType(), 4);
                            gameSession.addPlayer(player);
                            serverData.getGames().add(gameSession);
                            LOG.info("Created game session: " + playerID);
                            break;
                        default:
                            throw new ServiceException("Invalid game name");
                    }
                }

            } else {
                throw new ServiceException("Player has active game session " + playerID);
            }
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

        synchronized (player) {
            IGame game = getPlayerGame(playerID);

            synchronized (game) {
                if (game != null) {

                    Result result = game.makeAction(player, action);

                    // todo: do something with result
                } else {
                    throw new ServiceException("Player " + playerID + " has no active game");
                }
            }
        }

    }

    @Override
    public void disconnect(String playerID) throws ServiceException {
        // todo: add player in disconnect timeout list
        Player player = findPlayer(playerID);

        synchronized (player) {
            IGame game = getPlayerGameNullable(playerID);
            if (game != null) {
                synchronized (game) {
                    game.removePlayer(player);
                }
                removeFromEverywhere(player);
            }
        }

        LOG.info("Disconnect player: " + playerID + " nickname: " + player.getNickname());
    }

    @Override
    public Serializable getPlayerInfo(String playerID) throws ServiceException {
        return serverData.getPlayerIDMap().get(playerID);
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
        Player player = serverData.getPlayerIDMap().get(playerID);

        if (player == null) {
            throw new ServiceException("Unconnected player access");
        }

        return player;
    }

    private void removeFromEverywhere(Player player) {
        String id = player.getId();
        String nickname = player.getNickname();

        serverData.getPlayerIDMap().remove(id);
        serverData.getPlayerMap().remove(nickname);
        serverData.getPlayerIDGameMap().remove(id);
    }

    private IGame getPlayerGameNullable(String playerID) {
        return serverData.getPlayerIDGameMap().get(playerID);
    }

    private IGame getPlayerGame(String playerID) {
        IGame game = serverData.getPlayerIDGameMap().get(playerID);

        if (game == null) {
            throw new ServiceException("No active session for player: " + playerID);
        }

        return game;
    }

}
