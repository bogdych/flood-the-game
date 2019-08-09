import WebSocketService from './web-socket-service';
import PlayerData from './player-data';

export default class MultiplayerService{
    constructor(){
        this.socket = new WebSocketService();
        this.socket.onMessage((event) => this.onMessage(event.data));
        this.socket.init();
    }

    onMessage(data){
        let msg = JSON.parse(data);
        if("type" in msg){
            switch(msg.type){
                case "error":
                    this.onError();
                    break;
                case "gameReady":
                    this.onGameReady(msg);
                    break;
                default:
                    alert("Unexpected message type response from server");
                    break;
            }
        } else{
            if("id" in msg){
                this.playerData = new PlayerData(msg.id);
                console.log("id player set " + this.playerData.id);
                if(this.msgTo !== undefined){ this.findGame(this.msgTo); }
            } else{
                alert("Unexpected response from server");
            }
        }
    }
    onError(){
        alert("Error");
    }
    onGameReady(msg){
        console.log("GameFound");
        this.playerData.isMyTurn = msg.state.next.id === this.playerData.id;
    }
    findGame(msgToServer){
        switch (this.socket.socket.readyState) {
            case WebSocket.OPEN:
                this.socket.send(JSON.stringify(msgToServer));
                console.log(JSON.stringify(msgToServer));
                break;
            case WebSocket.CONNECTING:
                this.msgTo = msgToServer;
                break;
            default:
                this.onError();
        }
    }
    findGameFloodStandart(){
        this.findGame({
            type: "findGame",
            name: "flood",
            gameType:"standard"
        } )
    }
}