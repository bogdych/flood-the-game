export function WebSocketService(){
    this.url = 'ws://localhost:8060/game';
    this.callbackOnOpen = function () {   };
    this.socket = new WebSocket(this.url);

    this.init = function () {
        this.socket.onopen = (event) => { this.callbackOnOpen();};
    };
    this.onOpen = function(callback){
        if(!callback) {
            callback = () => {};
        }
        this.callbackOnOpen = callback;
        this.socket.onopen =  (event) => {this.callbackOnOpen()};

        }
    };
    this.onClose = function (callback) {
        if(!callback) {
            callback = () => {};
        }
        this.socket.onclose = function(event){ callback() };
    };
    this.onMessage = function (callback) {
        if(!callback) {
            callback = () => {return "SomeMessage"};
        }
        this.socket.onmessage = function (event) {
            console.log('Message from server ', callback());
        };
    };
    this.send = function(data){
        this.socket.send(data);
    }
}