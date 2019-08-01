export function WebSocketService(){
    this.url = 'ws://localhost:8060/game';
    this.callbackOnOpen;
    this.socket = new WebSocket(this.url);

    this.init = function () {
        this.socket.close();
        this.socket = new WebSocket(this.url);
        this.socket.onopen = (event) => { return this.callbackOnOpen();};
    };
    this.onOpen = function(callback){
        this.callbackOnOpen = callback;
    };
    this.onClose = function (event, callback) {
        if(!callback) callback = (event) => {};
        this.socket.onclose = function(event){ callback };
    };
    this.onMessage = function (event) {
        this.socket.onmessage = function (event) {
            console.log('Message from server ', event.data);
        };
    };
}