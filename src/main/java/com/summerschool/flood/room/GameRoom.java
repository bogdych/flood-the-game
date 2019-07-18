package com.summerschool.flood.room;

import com.summerschool.flood.prefabs.*;
import com.summerschool.flood.field.GameField;
import com.summerschool.flood.field.DepthFirstSearch;
import com.summerschool.flood.field.Pair;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;

public class GameRoom {
    private @Getter int counterNextPlayerId = 0;
    private @Getter int playersNumber = 4;
    private @Getter int playersNumberMax = 16;
    private @Getter int colorNumber = 6;
    private @Getter int colorNumberMax = 12;
    private @Getter String status = "";
    private @Getter List<Player> playerList;
    private Map<Player, Integer> playersIdMap;
    private Map<Player, Integer> playersColorsMap;
    private Map<Player, Pair<Integer, Integer>> startPositionMap;
    private @Getter GameField gameField;
    private DepthFirstSearch dfs;
    private @Getter Chat chat;
    public GameRoom(int playersNum, Pair<Integer, Integer> diagCell, int colors ){
        playersNumber = playersNum;
        colorNumber = colors;
        playersIdMap = new ConcurrentHashMap<>();
        playersColorsMap = new ConcurrentHashMap<>();
        playerList = new CopyOnWriteArrayList<>();
        gameField = new GameField(diagCell.getFirst(), diagCell.getSecond(), colorNumber);
        dfs = new DepthFirstSearch(gameField);
        for (int i = 0; i < playersNumber; i++) {
            playerList.add(new Player());
            playersIdMap.put(playerList.get(i), i);
        }
        chat = new Chat();
        startPositionMap = new ConcurrentHashMap<>(){
            {
                put(playerList.get(0), Pair.of(0, 0));
                put(playerList.get(1), Pair.of(0, diagCell.getSecond() - 1));
                put(playerList.get(2), Pair.of(diagCell.getFirst() - 1, diagCell.getSecond() - 1));
                put(playerList.get(3), Pair.of(diagCell.getFirst() - 1, 0));
            }
        };
        for(Player player : playerList){

            playersColorsMap.put(player, gameField.getAt(startPositionMap.get(player)));
        }
    }
    public Boolean isValidMove(Player player, int color){
        int tmpCounter = playersIdMap.get(player);
        return (tmpCounter == (counterNextPlayerId ) % playersNumber) &&
                color != playersColorsMap.get(player) &&
                color >= 0 && color < colorNumber;
    }
    public void move(Player player, int color){
        if(!isValidMove(player, color)) {
            status = "Invalid move";
            return;
        }
        counterNextPlayerId = (counterNextPlayerId ) % playersNumber;
        dfs.start(startPositionMap.get(player), color);
        playersColorsMap.replace(player, color);
        counterNextPlayerId = (counterNextPlayerId + 1) % playersNumber;
        getGameField().show();
        checkLoseAfterMove();
        checkWinAfterMove();
    }
    /*There prefab procedures*/
    /*Some procedure to restart game with same players*/
    public void newRound(){
        gameField = new GameField(gameField.getXSize(), gameField.getYSize(), gameField.getColorNumber());
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
