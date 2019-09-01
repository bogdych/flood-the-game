package com.summerschool.flood.server;

import com.summerschool.flood.game.*;
import com.summerschool.flood.game.flood.FloodGame;
import com.summerschool.flood.message.FindGameMessage;
import com.summerschool.flood.message.MakeActionMessage;

import lombok.Data;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Data
public class GameService {

    private final static Logger LOG = LoggerFactory.getLogger(GameService.class);

    private final List<IGame> games = new CopyOnWriteArrayList<>();
    private final Map<String, Player> players = new ConcurrentHashMap<>();

    public Player connect(String playerID) {
        Player player = new Player();
        player.setId(playerID);
        players.put(playerID, player);

        LOG.info("Connect player: " + playerID);

        return player;
    }

    public IGame findGame(String playerID, FindGameMessage message) {
        Player player = findPlayer(playerID);

        if (player.getActiveGame() == null) {
            IGame game = games.stream()
                    .filter(g -> g.matchType(message) && g.addPlayer(player))
                    .findFirst()
                    .orElseGet(() -> createGame(player, message));
            player.setActiveGame(game);

            LOG.info("Add player to game session: {} add time: {}", game.getId(), game.getUpdateTime());

            return game;
        } else {
            throw new ServiceException("Player has active game session " + playerID);
        }
    }

    public IGame process(String playerID, MakeActionMessage action) throws ServiceException {
        Player player = findPlayer(playerID);
        IGame game = player.getActiveGame();

        if (game != null) {
            game.makeAction(player, action);
            LOG.info("Player: {} made action", playerID);
        } else {
            throw new ServiceException("Player: " + playerID + " has no active game session");
        }

        return game;
    }

    public void finishGame(IGame game) {
        games.remove(game);

        LOG.info("Finish game session: {}", game.getId());
    }

    public void disconnect(String playerID) throws ServiceException {
        Player player = findPlayer(playerID);

        IGame game = player.getActiveGame();
        if (game != null) {
            game.removePlayer(player);
            if (game.isFinished()) {
                LOG.info("Deleted the game with id: {}", game.getId());
                finishGame(game);
            }
        }
        players.remove(playerID);
        LOG.info("Disconnect player: {} nickname: {}", player.getId(), player.getNickname());
    }

    private Player findPlayer(String playerID) {
        Player player = players.get(playerID);
        if (player == null) {
            throw new ServiceException("Unconnected player access with id" + playerID);
        }
        return player;
    }

    private IGame createGame(Player player, FindGameMessage message) {
        switch (message.getName()) {
            case FLOOD:
                IGame game = new FloodGame(message.getGameType(),  message.getGameParams());
                game.addPlayer(player);
                games.add(game);
                return game;
            default:
                throw new ServiceException("Invalid game name");
        }
    }

}
