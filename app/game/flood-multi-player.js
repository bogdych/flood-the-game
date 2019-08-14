import Phaser from 'phaser';
import Icon from "./icon";
import MultiplayerService from './multi-player-service';

export default class FloodMultiPlayer extends Phaser.Scene {
    constructor() {
        super({ key: 'FloodMultiPlayer'});
        this.emitters = {};

        this.grid = [];
        this.matched = [];

        this.moves = 25;

        this.frames = [ 'blue', 'green', 'grey', 'purple', 'red', 'yellow' ];
    }

    preload() {
        this.load.atlas('flood', 'assets/games/flood/blobs.png', 'assets/games/flood/blobs.json');
    }

    create() {
        this.mpService = new MultiplayerService(() => {
            this.mpService.findFloodGameStandard();
            this.messege = this.add.text(400, 300, 'Search game', {fill: '#000000', fontSize: '20px'});
        });
        this.mpService.onGameReady = (msg) => {
            console.log("Game found");
            this.mpService.playerData.isMyTurn = msg.state.next.id === this.mpService.playerData.id;
            if(this.message) {
                setTimeout(() => this.messege.setText('Game found!'), 3000);
            } else {
                this.messege = this.add.text(400, 300, 'Game found!', {fill: '#000000', fontSize: '20px'});
            }
            setTimeout(() => this.messege.destroy(), 3000);

            this.mpService.state = msg.state;
            this.createGrid();
            this.drawGrid();
        };



    }
    createGrid(){
        this.add.image(400, 300, 'flood', 'background');
        this.gridBG = this.add.image(400, 600 + 300, 'flood', 'grid');

        this.icon1 = new Icon(this, 'grey', 16, 156);
        this.icon2 = new Icon(this, 'red', 16, 312);
        this.icon3 = new Icon(this, 'green', 16, 458);
        this.icon4 = new Icon(this, 'yellow', 688, 156);
        this.icon5 = new Icon(this, 'blue', 688, 312);
        this.icon6 = new Icon(this, 'purple', 688, 458);

        this.cursor = this.add.image(16, 156, 'flood', 'cursor-over').setOrigin(0).setVisible(false);

        //  The game is played in a 14x14 grid with 6 different colors

        this.grid = [];
        let state =  this.mpService.state;
        for (let x = 0; x < state.field.width; x++)
        {
            this.grid[x] = [];

            for (let y = 0; y < state.field.height; y++)
            {
                let sx = 166 + (x * 36);
                let sy = 66 + (y * 36);
                let color = state.field.cells[x * state.field.height + y];

                let block = this.add.image(sx, -600 + sy, 'flood', this.frames[color]);

                block.setData('oldColor', color);
                block.setData('color', color);
                block.setData('x', sx);
                block.setData('y', sy);

                this.grid[x][y] = block;
            }
        }
    }
    drawGrid() {

    }
    createArrow() {
        this.arrow = this.add.image(109 - 24, 48, 'flood', 'arrow-white').setOrigin(0).setAlpha(0);

        this.tweens.add({

            targets: this.arrow,
            x: '+=24',
            ease: 'Sine.easeInOut',
            duration: 900,
            yoyo: true,
            repeat: -1

        });
    }
}