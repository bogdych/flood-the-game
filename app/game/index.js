import FloodSinglePlayer from './flood-single-player';

var config = {
    type: Phaser.WEBGL,
    width: 800,
    height: 600,
    pixelArt: true,
    parent: 'phaser-example',
    scene: [ FloodSinglePlayer ]
};

var game = new Phaser.Game(config);

function WebSocketService(){
    this.socket;
    this.url = 'redirect:/index.html';
    this.callbackOnOpen;
    this.onOpen = function(callback){
        this.addEventListener('open', (event) => {
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
        this.socket.addEventListener('open', (event)=>{
            this.callbackOnOpen();
    });
    };
}
var socket = new WebSocketService();
var onOpen = function() {
    console.log("connected")
}
socket.onOpen(onOpen);
