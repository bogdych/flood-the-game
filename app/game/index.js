import MainMenu from './main-menu';
import FloodSinglePlayer from './flood-single-player';
import FloodMultiPlayer from './flood-multi-player';
import canvas from './canvas.css';

let wratio = window.innerWidth / window.innerHeight;
let w = window.innerWidth;
let h = window.innerHeight;
let ratio = 4 / 3;
if (wratio < ratio) {
	w = Math.min(800, w) + "px";
	h = (w / ratio) + "px";
} else {
	h = Math.min(600, h) + "px";
	w = (h * ratio) + "px";
}

var config = {
    type: Phaser.WEBGL,
	//width: canvas.width;
    class: "canvas",
    pixelArt: true,
    parent: 'phaser-example',
    scene: [ MainMenu, FloodMultiPlayer, FloodSinglePlayer ],
};
let as = '<canvas class="canvas" id="can"></canvas>';
var div = document.getElementsByTagName("body");
div.innerHTML = as;
//document.getElementsByTagName("body")[0].appendChild(div);*/
var game = new Phaser.Game(config);
