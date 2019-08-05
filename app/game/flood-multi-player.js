import Phaser from 'phaser';

export default class FloodMultiPlayer extends Phaser.Scene {
    constructor() {
        super({ key: 'FloodMultiPlayer'})
    }

    preload() {
        this.load.atlas('flood', 'assets/games/flood/blobs.png', 'assets/games/flood/blobs.json');
    }

    create() {
        this.add.image(400, 300, 'flood', 'background');
        
        this.messege = this.add.text(400, 300, 'Coming soon! :)', {fill: '#000000', fontSize: '20px'})
        this.messege.setInteractive();
        this.messege.on('pointerup', () => {
            this.scene.stop('FloodMultiPlayer');
            this.scene.start('MainMenu');
        })
    }
}