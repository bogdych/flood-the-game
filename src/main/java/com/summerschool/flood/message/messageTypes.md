# Message types/formats for client-server communication

## Sent from client to server

### Find new game session for player

```json
{
  "type": "findGame",
  "name": "flood",
  "gameType":"standard",
}
```

Note: game names and types are listed in the game.GameName and game.GameType enums

### Make game action (only for players with active game session)

```json
{
  "type": "makeAction",
  "action": {
      "x":1,
      "y":2,
      "color":"RED"
  }
}

```

Note: all the valid colors are listed in the game.flood.Color enum

## Sent from server to client

### Error message

```json
{
  "type": "error",
  "time": "YYYY-MM-DD HH:MM:SS",
  "message": "Some very informative error text description" 
}
```

### Game ready (sent, when game session got all the players and ready be begun)

```json
{
  "type": "gameReady",
  "state": {
    
  }
}
```

### Game end (sent, when game session is finished)

```json
{
  "type": "turnEnd",
  "state": {
  
  }
}
```

### State property format

Note: property nickname could be null
Note: property next could be null (depends on the game status)
Note: property winner could be null (depends on the game status)

```json
{
  "state": {
    "next": {
      "id": "<Next turn action>",
      "nickname": "<Player optional nickname, could be null>"
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