import WebSocketService from './web-socket-service';
import PlayerData from './player-data';

export default class MultiplayerService{
    constructor(){
        this.playerData = new PlayerData();
        this.socket = new WebSocketService();
        this.socket.onMessage((data) => this.onMessage(data));
        this.socket.init();
    }
    //TODO: should I each case delegate a corresponding method ( gameReady() )?
    //Нужно ли вынести все case в отдельные методы?
    //SyntaxError: Unexpected token u in JSON at position 0
    //in try block
    onMessage(data){
        try {
            let msg = JSON.parse(data);
        }catch (e) {
            console.log(e + '');
            return;
        }
        if(type in msg){
            switch(msg.type){
                case "error":
                    alert("Error");
                    break;
                case "gameReady":
                    console.log("GameFound");
                    this.playerData.isMyTurn = (msg.state.next.id === this.id);
                    break;
                default:
                    alert("Unexpected message type response from server");
                    break;
            }
        } else{
            if(id in msg){
                this.id = msg.id;
                console.log("id player set");
            } else{
                alert("Unexpected response from server");
            }
        }
    }
    findGame(){
        let msgToServer = {
            type: "findGame",
            name: "flood",
            gameType:"standard"
        };
        if(this.socket.socket.readyState === WebSocket.OPEN){
            this.socket.send(JSON.stringify(msgToServer));
        }
    }
}