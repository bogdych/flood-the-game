# Message types/formats for client-server communication

## Sent from client to server

### Find new game session for player

After connection established and if player has no active game you can ask for new game 
session for player. Send desired game type and options and server will: add to existing 
game session or create new empty game session and add player to that.

When game get all players, player will receive message with game state and its status READY.
After that players could make the actions.

```json
{
  "type": "findGame",
  "name": "flood",
  "gameType":"standard"
}
```

Note: game names and types are listed in the game.GameName and game.GameType enums

### Make game action (only for players with active game session)

To make a valid action player must satisfy his turn and specify valid (x,y) position and color.
Server validates action and process it. Otherwise player receive error message if action is invalid.

```json
{
  "type": "makeAction",
  "action": {
      "x":1,
      "y":2,
      "color":"red"
  }
}

```

Note: all the valid colors are listed in the game.flood.Color enum

## Sent from server to client

### On connection

After connection established and new player added into internal game service,
server sends message with player id and nickname to the client.
Note: nickname field could be null.

An example of message data.

```json
{
  "type":"playerInfo",
  "player": {
      "id":"3730b279-161c-50de-b0d2-7ad352032cb8",
      "nickname":null,
  },
}
```

### Error message

Server send error messages in this format. 
There could be some useful info for player.

```json
{
  "type": "error",
  "time": "YYYY-MM-DD HH:MM:SS",
  "message": "Some very informative error text description" 
}
```

### Game ready (sent, when game session got all the players and ready be begun)

Server send this message to the all players in current game session after game got READY. 
This message contains game state, which defines next player turn, winner, next player 
position on the field, current color and all the field data with its properties.

Possible state's status: READY. 

```json
{
  "type": "gameReady",
  "state": {
    
  }
}
```

### Game state (sent, when game in progress)

Server send this message to the all players in current game session after each valid action. 
Contains all the game state data, needed to make next action.

Possible state's status: READY, FINISHED. 

After game finished all the players from that game are removed and marked as players, which
do not have active game. Therefore these players can request server to find new game.

```json
{
  "type": "gameState",
  "state": {
  
  }
}
```

### State property format

Note: property nickname could be null
Note: property next could be null (depends on the game status)
Note: property winner could be null (depends on the game status)

Note: cell - next player position of the action

```json
{
  "state": {
    "next": {
      "id": "<Next turn action>",
      "nickname": "<Player optional nickname, could be null>"
    },
    "cell": {
      "x": <coordinate>
      "y": <coordinate>,
      "color": "<Some color, from color enum>"
    },
    "winner": {
      "id": "<winner id>",
      "nickname": "<Winner optional nickname, could be null>"
    },
    "field": {
      "cells": [
        [
          {
            "x": 0,
            "y": 0,
            "color": "<Some color, from color enum>"
          },
          ...
          {
            "x": width-1,
            "y": height-1,
            "color": "<Some color, from color enum>"
          }
        ]
      ],
      "width": 10,
      "height": 10
    },
    "gameStatus": "<Current game status>"
  }
}
```