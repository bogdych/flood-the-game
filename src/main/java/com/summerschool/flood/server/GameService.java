package com.summerschool.flood.server;

import com.summerschool.flood.game.*;
import com.summerschool.flood.message.GameActionMessage;
import com.summerschool.flood.message.FindGameMessage;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.summerschool.flood.game.GameStatus.FINISHED;

@Service
@Data
public class GameService implements IGameService {

    private final static Logger LOG = LoggerFactory.getLogger(GameService.class);

    private final List<IGame> games = new CopyOnWriteArrayList<>();
    private final Map<String, Player> players = new ConcurrentHashMap<>();

    @Override
    public void connect(String playerID) {
        Player player = new Player();
        player.setId(playerID);
        players.put(playerID, player);

        LOG.info("Connect player: " + playerID);
    }

    @Override
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

    @Override
    public void process(String playerID, GameActionMessage action) throws ServiceException {
        LOG.info("Process: " + playerID);

        Player player = findPlayer(playerID);
        IGame game = player.getActiveGame();
        Result result = game.makeAction(player, action);

        LOG.info("Process player: " + playerID);
    }

    @Override
    public void disconnect(String playerID) throws ServiceException {
        Player player = findPlayer(playerID);
        IGame game = player.getActiveGame();
        game.removePlayer(player);
        LOG.info("Disconnect player: " + playerID + " nickname: " + player.getNickname());
        if (game.getGameStatus() == FINISHED) {
            LOG.info("The game with id: {}, deleted.", game.getId());
            games.remove(game);
        }
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
                LOG.info("Created game session: " + player.getId());
                return game;
            default:
                throw new ServiceException("Invalid game name");
        }
    }

}
