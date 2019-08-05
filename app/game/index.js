import FloodSinglePlayer from './flood-single-player';
import WebSocketService from './web-socket-service';

var config = {
    type: Phaser.WEBGL,
    width: 800,
    height: 600,
    pixelArt: true,
    parent: 'phaser-example',
    scene: [ FloodSinglePlayer ]
};

var game = new Phaser.Game(config);


var socket = new WebSocketService();
var onOpen = function() {
    console.log("connected");
}
socket.onOpen(onOpen);
