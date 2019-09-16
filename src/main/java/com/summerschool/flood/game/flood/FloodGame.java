package com.summerschool.flood.game.flood;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summerschool.flood.game.*;
import com.summerschool.flood.message.FindGameMessage;
import com.summerschool.flood.message.MakeActionMessage;
import com.summerschool.flood.server.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static com.summerschool.flood.game.GameStatus.*;

@Data
public class FloodGame implements IGame {

    private final static int DEFAULT_PLAYERS_COUNT = 4;
    private final static Logger LOG = LoggerFactory.getLogger(FloodGame.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private List<Player> players = new CopyOnWriteArrayList<>();
    private List<Player> activePlayers = new ArrayList<>();
    private IFirstSearch firstSearch;
    private FieldTraverse fieldTraverse;
    private FloodState state;
    private String id;
    private GameType type;
    private Instant createTime;
    private Instant updateTime;

    @Value("${flood.session.minPlayers}")
    private static int minPlayers;
    private int maxPlayers;

    public FloodGame(GameType type, Map<String,String> params) {
        this.createTime = Instant.now();
        this.type = type;
        this.maxPlayers = getMaxPlayersFromParams(params);
        Field field = createField(type);
        this.state = new FloodState(field);
        this.firstSearch = new DepthFirstSearch(field);
        this.fieldTraverse = new FieldTraverse(field);
        this.id = UUID.randomUUID().toString();

        LOG.info("Created game session UUID: {} time: {}\nGenerate field: \n{}", id, createTime, state.getField().getPrettyView());
    }

    private int getMaxPlayersFromParams(Map<String,String> params) {
        return Optional
                .ofNullable(params.get("maxPlayers"))
                .map(Integer::valueOf)
                .orElse(DEFAULT_PLAYERS_COUNT);
    }

    private Field createField(GameType type) {
        switch (type) {
            case STANDARD:
                return new Field(14, 14);
            case FAST:
                return new Field(5, 5);
            default:
                throw new IllegalArgumentException("Unknown game type: " + type);
        }
    }

    @Override
    public synchronized void removePlayer(Player player) {
        players.remove(player);
        state.getPositions().remove(player);
        state.getPlayersStatus().remove(player);
        activePlayers.remove(player);
        
        player.setActiveGame(null);

        if (players.size() == 0) {
            state.setGameStatus(FINISHED);
        } else if (players.size() == 1) {
            changeStateToFinish(players.get(0));
        } else if (state.getNext() == player) {
            changeStateToNext();
        }
    }

    @Override
    public synchronized void removePlayers() {
        players.forEach(player -> player.setActiveGame(null));
    }

    @Override
    public boolean matchType(FindGameMessage findGame) {
        return findGame.getName() == GameName.FLOOD &&
                findGame.getGameType() == type &&
                state.getGameStatus() == NOT_READY &&
                maxPlayers == getMaxPlayersFromParams(findGame.getGameParams());
    }

    @Override
    public boolean addPlayer(Player player) {
        if (players.size() < maxPlayers && this.isNotReady()) {
            synchronized (this) {
                if (players.size() < maxPlayers && this.isNotReady()) {
                    players.add(player);
                    updateTime = Instant.now();
                    if (players.size() == maxPlayers) {
                        start();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void start() {
        if (this.isNotReady())
            synchronized (this) {
                if (this.isNotReady()) {
                    getState().setGameStatus(READY);
                    setPlayersStartPosition();
                }
            }
    }

    @Override
    public void makeAction(Player player, MakeActionMessage message) {
        FloodAction action = mapper.convertValue(message.getAction(), FloodAction.class);
        synchronized (this) {
            if (isValidAction(player, action)) {
                makeStep(action);
                //LOG.info("Game session UUID: {}\nChange field: \n{}", id, state.getField().getPrettyView());
            } else {
                throw new ServiceException("Wrong action");
            }
        }
    }

    public boolean isValidAction(Player player, FloodAction action) {
        Field field = this.state.getField();

        Cell tmpCell = state.getPositions().get(this.state.getNext());

        return !state.getPlayersStatus().getOrDefault(player, PlayerStatus.IN_GAME).equals(PlayerStatus.LOSER) &&
                player.equals(this.state.getNext()) &&
                tmpCell.getX() == action.getX() && tmpCell.getY() == action.getY() &&
                tmpCell.getColor() != action.getColor() &&
                field.isInternalAt(action.getX(), action.getY()) &&
                this.state.getGameStatus() == READY;
    }

    private void makeStep(FloodAction action) {
        int x = action.getX();
        int y = action.getY();
        Color color = action.getColor();

        firstSearch.start(x, y, color);
        fieldTraverse.traverse(x, y, cell -> {
            activePlayers.removeIf(player -> {
                Cell pos = state.getPositions().get(player);
                if (pos.equals(cell)) {
                    state.getPlayersStatus().put(player, PlayerStatus.LOSER);
                    return true;
                }

                return false;
            });
        });

        LOG.info("Active players: {}", activePlayers.size());

        if (state.getField().isFilledByOneColor() || activePlayers.size() == 1) {
            changeStateToFinish(this.state.getNext());
        } else {
            changeStateToNext();
        }
    }

    private void changeStateToNext() {
        Player prevPlayer = state.getNext();
        int prevPlayerIndex = activePlayers.indexOf(prevPlayer);

        if (prevPlayerIndex == -1) {
            int i = ThreadLocalRandom.current().nextInt(activePlayers.size());
            state.setNext(activePlayers.get(i));
        } else {
            int i = (prevPlayerIndex + 1) % activePlayers.size();
            state.setNext(activePlayers.get(i));
        }
    }

    private void changeStateToFinish(Player winner) {
        state.getPlayersStatus().replaceAll((player, playerStatus) -> PlayerStatus.LOSER);
        state.getPlayersStatus().put(winner, PlayerStatus.WINNER);

        LOG.info("Winner: {} ", winner.getId());

        state.setNext(null);
        state.setGameStatus(FINISHED);
    }

    public void setPlayersStartPosition() {
        Field field = state.getField();

        switch (players.size()) {
            case 4:
                state.getPositions().put(players.get(3), field.getCells()[0][field.getHeight() - 1]);
            case 3:
                state.getPositions().put(players.get(2), field.getCells()[field.getWidth() - 1][0]);
            case 2:
                state.getPositions().put(players.get(1), field.getCells()[field.getWidth() - 1][field.getHeight() - 1]);
            case 1:
                state.getPositions().put(players.get(0), field.getCells()[0][0]);
                break;
        }

        players.forEach(p -> state.getPlayersStatus().put(p, PlayerStatus.IN_GAME));
        activePlayers.addAll(players);

        Player player = players.get(ThreadLocalRandom.current().nextInt(players.size()));
        state.setNext(player);
    }

    @Override
    public boolean isReady() {
        return this.state.getGameStatus() == READY;
    }

    @Override
    public boolean isNotReady() {
        return this.state.getGameStatus() == NOT_READY;
    }

    @Override
    public boolean isFinished() {
        return this.state.getGameStatus() == FINISHED;
    }

    @Data
    public class FloodState implements GameState {
        private Player next;
        private Map<Player, PlayerStatus> playersStatus = new HashMap<>();
        private Field field;
        private Map<Player, Cell> positions = new HashMap<>();
        private GameStatus gameStatus = NOT_READY;

        FloodState(Field field) {
            this.field = field;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FloodAction {
        private int x;
        private int y;
        private Color color;
    }
}

