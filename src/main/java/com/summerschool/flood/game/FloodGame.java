package com.summerschool.flood.game;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;



public class FloodGame implements IGame {

    private @Getter @Setter List<Player> players = new CopyOnWriteArrayList<>();
    private @Getter Field field;
    private @Getter Map<Player, Cell> playersStartPosition = new ConcurrentHashMap<>();
    private @Getter int counter = 0;
    private IFirstSearch firstSearch;
    public FloodGame(GameType type) {

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
    public Result makeAction(Player player, Action action) {
        switch (action.getType()){
            case MAKE_STEP:
                if(action.getParams().get("X1") instanceof Integer &&
                        action.getParams().get("X2") instanceof Integer &&
                        action.getParams().get("color") instanceof  Color) {

                    if(isValidMakeStep(player, (Integer)action.getParams().get("X1"),
                            (Integer) action.getParams().get("X1"),
                            (Color) action.getParams().get("Color"))) {

                        makeStep(player, (Integer) action.getParams().get("X1"),
                                (Integer) action.getParams().get("X2"),
                                (Color) action.getParams().get("Color"));
                    }
                }

                break;
            default:
                return null;
        }
        return null;
    }

    @Override
    public boolean addPlayer(Player player) {
        return false;
    }
    @Override
    public void removePlayer(Player player){

    }

    public void makeStep(Player player, int x, int y, Color color){
        Cell tmpCell = new Cell(x, y);
        tmpCell.setColor(color);
        firstSearch.start(tmpCell);
        counter = (counter + 1) % playersStartPosition.size();
    }
    public Boolean isValidMakeStep(Player player, int x, int y, Color color){
        Cell tmpCell = playersStartPosition.get(counter);
        return player == players.get(counter) &&
                tmpCell.getX() == x && tmpCell.getY() == y &&
                tmpCell.getColor() != color &&
                field.isInternalAt(x, y);
    }
}

