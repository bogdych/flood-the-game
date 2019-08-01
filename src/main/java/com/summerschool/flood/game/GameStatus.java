package com.summerschool.flood.game;

public enum GameStatus {

    /** Wait for players */
    NOT_READY,

    /** When all players were added, but game was not started yet */
    READY,

    /** Game in progress, players make an actions */
    IN_PROGRESS,

    /** Game is finished, has a winner/or no winner */
    FINISHED

}
