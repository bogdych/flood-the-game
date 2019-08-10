export default class WebSocketService{
    static get URL() { return "ws://localhost:8060/game";}

    constructor() {
        this.callbackOnOpen = () => console.log("Connected");
        this.callbackOnMessage = (event) => console.log("Message from server", event.data);
        this.callbackOnClose = () => console.log("Closed");
    };
    init() {
        if(!this.socket){
            this.socket = new WebSocket(WebSocketService.URL);
            this.socket.onopen =(event) => this.callbackOnOpen();
            this.socket.onclose = (event) => this.callbackOnClose(event.data);
            this.socket.onmessage = (event) => this.callbackOnMessage(event);
        }
    };
    onOpen(callback) {
        this.callbackOnOpen = callback;
    };

    onClose(callback) {
        this.callbackOnClose = callback;
    };
    onMessage(callback) {
        this.callbackOnMessage = callback;
    };
    send(data) {this.socket.send(data);}

}