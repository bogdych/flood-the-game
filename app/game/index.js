import MainMenu from './main-menu';
import FloodSinglePlayer from './flood-single-player';
import FloodMultiPlayer from './flood-multi-player';
import canvas from './canvas.css';
import img from '../assets/games/background/background.png';
import body from './back.css';
//import favicon from '../assets/games/flood/favicon'

/*
(function() {
    var link = document.querySelector("link[rel*='icon']") || document.createElement('link');
    link.type = 'image/x-icon';
    link.rel = 'shortcut icon';
    link.href = '../assets/games/flood/favicon';
    document.getElementsByTagName('head')[0].appendChild(link);
})();
*/
function resize() {
    let canvas = document.querySelector("canvas");
    let width = window.innerWidth;
    let height = window.innerHeight;
    let wratio = width / height;
    let ratio = config.width / config.height;
    if (wratio < ratio) {
        canvas.style.width = Math.min(800, width) + "px";
        canvas.style.height = (width / ratio) + "px";
    } else {
		canvas.style.height = Math.min(600, height) + "px";
        canvas.style.width = (height * ratio) + "px";
    }
}

var config = {
    type: Phaser.WEBGL,
	width: 800,
	height: 600,
    pixelArt: true,
    parent: 'phaser-example',
    scene: [ MainMenu, FloodMultiPlayer, FloodSinglePlayer ],
};

document.title = "Flood";

window.onload = () => {
	const can = document.querySelector("canvas");
	can.id = "canvas";
    resize();
};

window.onresize﻿ = resize;

var game = new Phaser.Game(config);

