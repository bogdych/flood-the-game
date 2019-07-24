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
    }

    @Override
    public void setPlayerInfo(String playerID, PlayerInfo info) {
        Player player = findPlayer(playerID);

        String oldNickname = player.getNickname();
        String newNickname = info.getNickname();

        serverData.getPlayerMap().remove(oldNickname);
        player.setNickname(newNickname);
        serverData.getPlayerMap().put(newNickname, player);
    }

    @Override
    public void findGame(String playerID, GameParams params) {
        /** ? what about concurrent access to player ? */

        Player player = findPlayer(playerID);

        /* Player does not belong to any current active game */
        if (player.getCurrentGame() == null) {

            Optional<IGame> game = serverData.getGames().stream()
                    .filter(g -> g.addPlayer(player))
                    .findFirst();

            if (game.isPresent()) {
                // todo: some response
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
            throw new ServiceException("Player has active game session");
        }
    }

    @Override
    public boolean reconnect(String playerID, String playerNickname) throws ServiceException {
        return false;
    }

    @Override
    public void process(String playerID, Action action) throws ServiceException {
        Player player = findPlayer(playerID);
        IGame game = player.getCurrentGame();

        if (game != null) {
            Result result = game.makeAction(player, action);

            // todo: do something with result
        } else {
            throw new ServiceException("Player " + playerID + " has no active game");
        }
    }

    @Override
    public void disconnect(String playerID) throws ServiceException {
        // todo: add player in disconnect timeout list
        Player player = findPlayer(playerID);
        IGame game = player.getCurrentGame();

        if (game != null) {
            // Immediately remove from game
            game.removePlayer(player);
            removeFromEverywhere(player);
        }
    }

    @Override
    public Serializable getPlayerInfo(String playerID) throws ServiceException {
        return serverData.getPlayerIDMap().get(playerID);
    }

    @Override
    public Serializable getGameState(String playerID) throws ServiceException {
        return serverData.getPlayerIDMap().get(playerID).getCurrentGame().getGameState();
    }

    @Override
    public Serializable getGameStatus(String playerID) throws ServiceException {
        return serverData.getPlayerIDMap().get(playerID).getCurrentGame().getGameStatus();
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
    }

}
