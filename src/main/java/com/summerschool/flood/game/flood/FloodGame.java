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

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static com.summerschool.flood.game.GameStatus.*;

@Data
public class FloodGame implements IGame {

    private final static Logger LOG = LoggerFactory.getLogger(FloodGame.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private List<Player> players = new CopyOnWriteArrayList<>();
    private IFirstSearch firstSearch;
    private FloodState state;
    private String id;
    private GameType type;
    private Instant creationTime;
    private Instant lastPlayerTime;

    @Value("${flood.session.minPlayers}")
    private static int minPlayers;
    private int maxPlayers;

    public FloodGame(GameType type, int maxPlayersCount) {
        this.creationTime = Instant.now();
        this.type = type;
        this.maxPlayers = maxPlayersCount;
        Field field = createField(type);
        this.state = new FloodState(field);
        this.firstSearch = new DepthFirstSearch(field);
        this.id = UUID.randomUUID().toString();

        LOG.info("Created game session UUID: {} time: {}\nGenerate field: \n{}", id, creationTime, state.getField().getPrettyView());
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
        player.setActiveGame(null);

        if (players.size() == 0) {
            state.setGameStatus(FINISHED);
        } else if (players.size() == 1) {
            changeStateToFinish(players.get(0));
        } else if (state.getNext().equals(player)) {
            changeStateToNext();
        }
    }

    @Override
    public boolean matchType(FindGameMessage findGame) {
        return findGame.getName() == GameName.FLOOD &&
               findGame.getGameType() == type &&
               state.getGameStatus() == NOT_READY;
    }

    @Override
    public boolean addPlayer(Player player) {
        if (players.size() < maxPlayers) {
            synchronized (this) {
                if (players.size() < maxPlayers) {
                    players.add(player);
                    lastPlayerTime = Instant.now();
                    if (players.size() == maxPlayers) {
                        this.state.setGameStatus(READY);
                        setPlayersStartPosition();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean run(Instant now, int waitTime) {
        synchronized (this) {
            if (state.getGameStatus() == NOT_READY
                && players.size() >= minPlayers
                && Duration.between(getLastPlayerTime(), now).getSeconds() >= waitTime) {
                state.setGameStatus(READY);
                setPlayersStartPosition();
                return true;
            }
        }

        return false;
    }

    @Override
    public void makeAction(Player player, MakeActionMessage message) {
        FloodAction action = mapper.convertValue(message.getAction(), FloodAction.class);
        synchronized (this) {
            if (isValidAction(player, action)) {
                makeStep(action);
                LOG.info("Game session UUID: {}\nChange field: \n{}", id, state.getField().getPrettyView());
            } else {
                throw new ServiceException("Wrong action");
            }
        }
    }

    public boolean isValidAction(Player player, FloodAction action) {
        Field field = this.state.getField();

        Cell tmpCell = state.getPositions().get(this.state.getNext());
        return player.equals(this.state.getNext()) &&
                tmpCell.getX() == action.getX() && tmpCell.getY() == action.getY() &&
                tmpCell.getColor() != action.getColor() &&
                field.isInternalAt(action.getX(), action.getY()) &&
                this.state.getGameStatus() == READY;
    }

    private void makeStep(FloodAction action) {
        firstSearch.start(action.getX(), action.getY(), action.getColor());

        if (state.getField().isFilledByOneColor()) {
            changeStateToFinish(this.state.getNext());
        } else {
            changeStateToNext();
        }
    }


    private void changeStateToNext() {
        int index = players.indexOf(state.getNext());
        Player next = players.get((index + 1) % players.size());
        state.setNext(next);
    }

    private void changeStateToFinish(Player winner) {
        state.setWinner(winner);
        state.setNext(null);
        state.setGameStatus(FINISHED);
    }

    public void setPlayersStartPosition() {
        Field field = state.getField();
        Player player = players.get(ThreadLocalRandom.current().nextInt(players.size()));

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

        state.setNext(player);
    }

    @Override
    public boolean isReady() {
        return this.state.getGameStatus() == READY;
    }

    @Override
    public boolean isFinished() {
        return this.state.getGameStatus() == FINISHED;
    }

    @Data
    public class FloodState implements GameState {
        private Player next;
        private Player winner;
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

