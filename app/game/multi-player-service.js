import WebSocketService from './web-socket-service';
import MyTurn from './my-turn';

export default class MultiplayerService{
    constructor(){
        this.socket = new WebSocketService();
        this.socket.onMessage((str) => this.lastMessage = str);
        this.socket.init();
        this.idWithBranch = this.lastMessage.substring(6, this.lastMessage.length - 1);
        this.idWithBranch = this.idWithBranch.substring(1, this.idWithBranch.length - 1);
    }
    findGame(){
        let msgToServer = {
            type: "findGame",
            name: "flood",
            gameType:"standard"
        };
        this.socket.send(JSON.stringify(msgToServer));
        let msgFromServer = JSON.parse(this.lastMessage);

        switch(msgFromServer.type){
            case "error":
                alert("Error");
                break;
            case "gameReady":
                console.log("GameFound");
                this.myTurn = new MyTurn(msgFromServer.state.next.id === this.idWithBranch);
                break;
                
        }
    }
}