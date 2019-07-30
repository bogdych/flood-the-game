package com.summerschool.flood.game;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.summerschool.flood.game.GameStatus.FINISHED;
import static com.summerschool.flood.game.GameStatus.READY;

public class FloodGame implements IGame {

    private @Getter @Setter List<Player> players = new ArrayList<>();
    private @Getter Field field;
    private @Getter @Setter long id;
    private @Getter Map<Player, Cell> playersStartPosition = new ConcurrentHashMap<>();
    private @Getter int counter = 0;
    private IFirstSearch firstSearch;
    private @Getter @Setter int maxPlayers;
    private @Getter @Setter GameStatus gameStatus;

    public FloodGame(GameType type, int maxPlayersCount) {
        this.maxPlayers = maxPlayersCount;

        switch (type) {
            case STANDARD:
                this.field = new Field(10, 10);
                return;

            case FAST:
                this.field = new Field(5, 5);
        }
        this.firstSearch = new DepthFirstSearch(field);
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player);
        if (players.size() == 0) {
            gameStatus = FINISHED;
        }
    }

    @Override
    public boolean matchType(GameParams params) {
        return true;
    }

    @Override
    public boolean addPlayer(Player player) {
        if (players.size() < maxPlayers) {
            synchronized (this) {
                if (players.size() < maxPlayers) {
                    players.add(player);
                    if (players.size() == maxPlayers) {
                        gameStatus = READY;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Result makeAction(Player player, GameAction action) {
        switch (action.getType()){
            case MAKE_STEP:
                    int x = Integer.parseInt(action.getPayload().get("X"));
                    int y = Integer.parseInt(action.getPayload().get("Y"));
                    Color color = Color.valueOf(action.getPayload().get("Color"));

                    if(isValidMakeStep(player, x, y, color)) {
                        makeStep(x, y, color);
                        return new Result(ResultType.ACTION_PROCESSED);
                    }
                    else{
                        return new Result(ResultType.INVALID_ACTION);
                    }

            default:
                return null;
        }
    }

    public void makeStep(int x, int y, Color color){
        Cell tmpCell = new Cell(x, y);
        tmpCell.setColor(color);
        firstSearch.start(tmpCell);
        counter = (counter + 1) % playersStartPosition.size();
    }
    public Boolean isValidMakeStep(Player player, int x, int y, Color color){
        Cell tmpCell = playersStartPosition.get(players.get(counter));
        return player == players.get(counter) &&
                tmpCell.getX() == x && tmpCell.getY() == y &&
                tmpCell.getColor() != color &&
                field.isInternalAt(x, y);
    }
    public void setPlayersStartPosition(){
        switch (players.size()){
            case 4:
                playersStartPosition = new ConcurrentHashMap<>();
                playersStartPosition.put(players.get(0), field.getCells()[0][0]);
                playersStartPosition.put(players.get(1), field.getCells()[field.getWidth() - 1][0]);
                playersStartPosition.put(players.get(2), field.getCells()[field.getWidth() - 1][field.getHeight() - 1]);
                playersStartPosition.put(players.get(3), field.getCells()[0][field.getHeight() - 1]);

        }
    }
}

