export default class WebSocketService{
    constructor() {
        this.socket;
        this.url = 'ws://localhost:8060/game';
        this.socket = new WebSocket(this.url);
        this.callbackOnOpen = () => {console.log("Connected")};
        this.callbackOnMessage = (data) => {console.log("Message from server", event.data)};
        this.callbackOnClose = () => {console.log("Closed")};
    }

    init() {
        if(!this.socket){ this.socket = new WebSocket(this.url)}
        this.socket.onopen =(event) => { this.callbackOnOpen();}
        this.socket.onclose = (event) => {this.callbackOnClose(event.data);}
        this.socket.onmessage = (event) => {this.callbackOnMessage();}
    };
    onOpen(callback) {
        if(!callback){
            callback = this.callbackOnOpen; }
        else {
            this.callbackOnOpen = callback;}

        this.socket.onopen = (event) => {callback();};
    };

    onClose(callback) {
        if(!callback){
            callback = this.callbackOnClose; }
        else{
            this.callbackOnClose = callback};
        this.socket.onclose = (event) => { callback() };
    };
    onMessage(callback) {
        if(!callback){
            callback = this.callbackOnMessage; }
        else{
            this.callbackOnMessage = callback;}
        this.socket.onmessage = (event) => { callback(event.data); };
    };
}