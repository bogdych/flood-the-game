import MainMenu from './main-menu';
import FloodSinglePlayer from './flood-single-player';
import FloodMultiPlayer from './flood-multi-player';
import WebSocketService from "./web-socket-service";

import MultiplayerService from './multi-player-service';

var config = {
    type: Phaser.WEBGL,
    width: 800,
    height: 600,
    pixelArt: true,
    parent: 'phaser-example',
    scene: [ MainMenu, FloodMultiPlayer, FloodSinglePlayer ]
};

var game = new Phaser.Game(config);

let mpService = new MultiplayerService();

mpService.findGame();