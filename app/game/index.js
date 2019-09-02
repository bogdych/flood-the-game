import MainMenu from './main-menu';
import FloodSinglePlayer from './flood-single-player';
import FloodMultiPlayer from './flood-multi-player';
import canvas from './canvas.css';
import img from '../assets/games/background/background.png';
import body from './back.css';

var config = {
    type: Phaser.WEBGL,
	width: 800,
	height: 600,
    pixelArt: true,
    parent: 'phaser-example',
    scene: [ MainMenu, FloodMultiPlayer, FloodSinglePlayer ],
};

window.onload = () => {
	var can = document.querySelector("canvas");
	can.id = "canvas";
};

var game = new Phaser.Game(config);

