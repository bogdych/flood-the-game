package com.summerschool.flood.gameRoom;

import com.summerschool.flood.Prefabs.*;
import com.summerschool.flood.gameSquare.Square;
import com.summerschool.flood.gameSquare.DFS;
import com.summerschool.flood.gameSquare.Pair;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;

public class gameRoom {
    private @Getter int counter = 0;
    private @Getter int playersNumber = 4;
    private @Getter int playersNumberMax = 16;
    private @Getter int colorNumber = 6;
    private @Getter int colorNumberMax = 12;
    private @Getter String status;
    private @Getter List<Player> playerList;
    private Map<Player, Integer> playersIdMap;
    private Map<Player, Integer> playersColorsMap;
    private Map<Player, Pair<Integer, Integer>> startPosMap;
    private Square square;
    private DFS dfs;
    private @Getter Chat chat;
    public gameRoom(int playersnumber, Pair<Integer, Integer> diagCell, int colors ){
        this.playersNumber = playersnumber;
        this.colorNumber = colors;
        playersIdMap = new ConcurrentHashMap<>();
        playersColorsMap = new ConcurrentHashMap<>();
        playerList = new CopyOnWriteArrayList<>();
        this.square = new Square(diagCell.getFirst(), diagCell.getSecond(), colorNumber);
        this.AssignPositions();
        for (int i = 0; i < playersNumber; i++) {
            playerList.add(new Player());
            playersIdMap.put(playerList.get(i), i);
                                                                                /*Because didn't override equals*/
            playersColorsMap.put(playerList.get(i), square.getAt(startPosMap.get(playerList.get(i)).getFirst(),
                                                                 startPosMap.get(playerList.get(i)).getSecond()));
        }
        chat = new Chat();
    }
    public Boolean IsValidMove(Player player, int color){
        int tmpCounter = playersIdMap.get(player);
        return (tmpCounter == counter + 1) && color != playersColorsMap.get(player) && color >= 0 && color < colorNumber;
    }
    public void move(Player player, int color){
        if(!IsValidMove(player, color)) {
            status = "Invalid move";
            return;
        }
        counter = (counter + 1) % playersNumber;
        dfs.Start(startPosMap.get(player), color);
        CheckLoseAfterMove();
        CheckWinAfterMove();
    }
    /*There prefab procedures*/
    /*Some procedure to restart game with same players*/
    public void newRound(){
        square = new Square(square.getXSize(), square.getYSize(), square.getColorNumber());
    }
    /*Some procedure, that assign start positions for each player*/
    private void AssignPositions(){

    }
    /*Disable some players*/
    private void PlayersLosed(){

    }

    /*Game round ended*/
    public Boolean CheckWinAfterMove(){
        return true;
    }
    /*Some players losed*/
    public Boolean CheckLoseAfterMove(){
        return true;
    }

}
