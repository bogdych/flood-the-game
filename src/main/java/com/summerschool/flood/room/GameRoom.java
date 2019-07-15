package com.summerschool.flood.room;

import com.summerschool.flood.Prefabs.*;
import com.summerschool.flood.field.GameField;
import com.summerschool.flood.field.DepthFirstSearch;
import com.summerschool.flood.field.Pair;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;

public class GameRoom {
    private @Getter int counter = 0;
    private @Getter int playersNumber = 4;
    private @Getter int playersNumberMax = 16;
    private @Getter int colorNumber = 6;
    private @Getter int colorNumberMax = 12;
    private @Getter String status;
    private @Getter List<Player> playerList;
    private Map<Player, Integer> playersIdMap;
    private Map<Player, Integer> playersColorsMap;
    private Map<Player, Pair<Integer, Integer>> startPositionMap;
    private GameField gameField;
    private DepthFirstSearch dfs;
    private @Getter Chat chat;
    public GameRoom(int playersNum, Pair<Integer, Integer> diagCell, int colors ){
        this.playersNumber = playersNum;
        this.colorNumber = colors;
        this.playersIdMap = new ConcurrentHashMap<>();
        this.playersColorsMap = new ConcurrentHashMap<>();
        this.playerList = new CopyOnWriteArrayList<>();
        this.gameField = new GameField(diagCell.getFirst(), diagCell.getSecond(), colorNumber);
        this.assignPositions();
        this.dfs = new DepthFirstSearch(gameField);
        for (int i = 0; i < playersNumber; i++) {
            playerList.add(new Player());
            playersIdMap.put(playerList.get(i), i);
                                                                                /*Because didn't override equals*/
            playersColorsMap.put(playerList.get(i), gameField.getAt(startPositionMap.get(playerList.get(i)).getFirst(),
                                                                 startPositionMap.get(playerList.get(i)).getSecond()));
        }
        chat = new Chat();
    }
    public Boolean isValidMove(Player player, int color){
        int tmpCounter = playersIdMap.get(player);
        return (tmpCounter == counter + 1) && color != playersColorsMap.get(player) && color >= 0 && color < colorNumber;
    }
    public void move(Player player, int color){
        if(!isValidMove(player, color)) {
            status = "Invalid move";
            return;
        }
        counter = (counter + 1) % playersNumber;
        dfs.start(startPositionMap.get(player), color);
        checkLoseAfterMove();
        checkWinAfterMove();
    }
    /*There prefab procedures*/
    /*Some procedure to restart game with same players*/
    public void newRound(){
        gameField = new GameField(gameField.getXSize(), gameField.getYSize(), gameField.getColorNumber());
    }
    /*Some procedure, that assign start positions for each player*/
    private void assignPositions(){

    }
    /*Disable some players*/
    private void playersLosed(){

    }

    /*Game round ended*/
    public Boolean checkWinAfterMove(){
        return true;
    }
    /*Some players losed*/
    public Boolean checkLoseAfterMove(){
        return true;
    }

}
