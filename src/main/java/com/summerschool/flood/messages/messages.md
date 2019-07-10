# Client-Server communication interface

## Overall

Game handel is supposed to process (v.1) messages in game session:
game step, events, player actions. Server provides response to this kind
of messages: error messages, invalid actions, valid actions, game state info.

### Messages Format

Client-side player action message. 
Supposed to be sent when: player wants to start new game,
play do something in time of current game session.

```json
{
    "messageType" : "<message type>"
    "param1" : "<param value1"
    "param2" : "<param value2"

    "paramN" : "<param valueN"
}
```

Server-side response.

```json
{
    "messageType" : "<message type>"
    "serialized param1" : "<param value1>"
    "serialized param2" : "<param value2>"

    "serialized paramN" : "<param valueN>"
}
```

### UI Messages' types

Common messages types to be sent from UI (not exhaustive content):

1. Get user info - score, number of attempts, stat, etc.
2. User profile setup: set nickname
3. User find session action  (with optional game mode)

User game session actions:

1. setup game session - load map, enemies info
2. action (choose color - actual step)
3. get full game state (map, players info)

### Server Messages' types
   
Server side response: common messages

1. Error message: some kind of fatal error? (bad practice)
2. Player info response
3. Player nickname setup response
4. Create new session response (when new game session is created: server create new game, 
   add players, generates new map, collects players info, sends it to players)

Server game session messages:

1. Invalid action (so, server ignores this step)
2. Game finished (someone won)
3. Game stopped (someone disconnected)
4. Game action accepted
5. Game state response (map, players info)

### Messages types list

All the types of the messages for client-server communication.
Type of messages: string (in java: enum? string? int?)

1.  setPlayerNickname 
2.  getPlayerInfo
3.  findSession
4.  makeAction
5.  getGameState
6.  errorMessage - some unhandled error from server
7.  invalidGameAction - if player made some invalide action in time of the game
8.  gameStateInfo - returns all the info about curent game state
9.  actionResponse - returns status of the action (only for correct actions in time of the game)
10. gameState - return game state (disconnected, won, lost)
10. playerInfo - returns player info
11. playerNicknameUpdated - returns updated player nickname





