function WebSocketService(){
    this.socket;
    this.url = 'ws://localhost:8060/game';
    this.callbackOnOpen;
    this.onOpen = function(callback){
        this.socket.addEventListener('open', (event) => {
            callback();
        });
        this.callbackOnOpen = callback;
    };
    this.onClose = function (event, callback) {
        if(!callback) callback = (event) => {};
        this.socket.addEventListener('close', callback);
    };
    this.onMessage = function (event) {
        this.socket.addEventListener('message', function (event) {
            console.log('Message from server ', event.data);
        });
    };

    this.init = function () {
        this.socket = new WebSocket(this.url);
    };
}