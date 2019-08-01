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

    public void connect(String playerID) {
        Player player = new Player();
        player.setId(playerID);
        players.put(playerID, player);

        LOG.info("Connect player: " + playerID);
    }

    public IGame findGame(String playerID, FindGameMessage message) {
        Player player = findPlayer(playerID);

        if (player.getActiveGame() == null) {
            IGame game = games.stream()
                    .filter(g -> g.matchType(message.getGameParams()) && g.addPlayer(player))
                    .findFirst()
                    .orElse(createGame(player, message));
            player.setActiveGame(game);
            return game;
        } else {
            throw new ServiceException("Player has active game session " + playerID);
        }
    }

    public IGame process(String playerID, MakeActionMessage action) throws ServiceException {
        LOG.info("Process: " + playerID);

        Player player = findPlayer(playerID);
        IGame game = player.getActiveGame();
        LOG.info("Process player: " + playerID);
        game.makeAction(player, action);
        return game;
    }

    public void disconnect(String playerID) throws ServiceException {
        Player player = findPlayer(playerID);
        if (player == null) {
            LOG.info("Can't find player with id: {}", playerID);
            return;
        }
        IGame game = player.getActiveGame();
        if (game != null) {
            game.removePlayer(player);
            if (game.isFinished()) {
                LOG.info("Deleted the game with id: {}", game.getId());
                games.remove(game);
            }
        }
        players.remove(playerID);
        LOG.info("Disconnect player: " + playerID + " nickname: " + player.getNickname());
    }

    private Player findPlayer(String playerID) {
        Player player = players.get(playerID);
        if (player == null) {
            throw new ServiceException("Unconnected player access");
        }
        return player;
    }

    private IGame createGame(Player player, FindGameMessage message) {
        switch (message.getName()) {
            case FLOOD:
                IGame game = new FloodGame(message.getGameType(), 4);
                game.addPlayer(player);
                games.add(game);
                return game;
            default:
                throw new ServiceException("Invalid game name");
        }
    }

}
