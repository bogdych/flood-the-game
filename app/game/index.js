import FloodSinglePlayer from './flood-single-player';
import MultiplayerService from './multi-player-service';

var config = {
    type: Phaser.WEBGL,
    width: 800,
    height: 600,
    pixelArt: true,
    parent: 'phaser-example',
    scene: [ FloodSinglePlayer ]
};

var game = new Phaser.Game(config);

let mpService = new MultiplayerService();

mpService.findGame();