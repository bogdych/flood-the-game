import WebSocketService from './web-socket-service';

export default class MultiplayerService{
    constructor(){
        this.socket = new WebSocketService();
        this.socket.onOpen(() => {console.log("connected") });
        this.socket.onMessage((str) => {this.lastMessage = str});
        this.init();
        this.id = this.lastMessage.substring(7, this.lastMessage.length - 1);
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
                break;
                
        }
    }
}