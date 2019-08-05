import FloodSinglePlayer from './flood-single-player';
import WebSocketService from "./web-socket-service";


var config = {
    type: Phaser.WEBGL,
    width: 800,
    height: 600,
    pixelArt: true,
    parent: 'phaser-example',
    scene: [ FloodSinglePlayer ]
};

var game = new Phaser.Game(config);

let socket = new WebSocketService();
let onopen = function(){
    console.log("connected")
};
socket.onOpen(onopen);
socket.init();