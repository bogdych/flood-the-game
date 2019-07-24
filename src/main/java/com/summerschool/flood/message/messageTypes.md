# Message types/formats for client-server communication

## Set player info

```json
{
  "type": "setPlayerInfo",
  "payload": {
    "nickname": "<some nickname>"
  }
}
```

## Find new game session for player

```json
{
  "type": "findGame",
  "payload": {
    "name": "<name of the game. Should be from GameName>",
    "type": "<type of the game. Should be from GameType>"
  }
}
```
## Make game action (only for players with active game session)

Note: non-exhaustive patter. Need meditation

```json
{
  "type": "makeAction",
  "payload": {
    "type": "<type of action. Should be from ActionType>",
  }
}
```