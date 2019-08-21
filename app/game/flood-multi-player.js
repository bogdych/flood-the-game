import Phaser from 'phaser';
import Icon from "./icon";
import MultiplayerService from './multi-player-service';
import {valueOfColor, colorOfIndex, COLORS} from './colors';
export default class FloodMultiPlayer extends Phaser.Scene {
    constructor() {
        super({ key: 'FloodMultiPlayer'});

        this.allowClick = true;

        this.currentColor = -1;

        this.emitters = {};

        this.grid = [];
        this.matched = [];

        this.moves = 25;

        this.icon = [];

        this.playersCorner = 1;
    }

    preload() {
        this.load.bitmapFont('atari', 'assets/fonts/bitmap/atari-smooth.png', 'assets/fonts/bitmap/atari-smooth.xml');
        this.load.atlas('flood', 'assets/games/flood/blobs.png', 'assets/games/flood/blobs.json');
    }
    
    create() {
        this.add.image(400, 300, 'flood', 'background');
        this.gridBG = this.add.image(400, 600 + 300, 'flood', 'grid');

        this.mpService = new MultiplayerService(() => {
            this.mpService.findFloodGameStandard();
            this.messege = this.add.text(300, 300, 'Search game', {fill: '#000000', fontSize: '20px'});
        });

        this.mpService.onGameReady = (msg) => {
            console.log("Game found");
            this.mpService.playerData.isMyTurn = msg.state.next.id === this.mpService.playerData.id;
            console.log(JSON.stringify(msg));
            this.messege.destroy();

            this.messege = this.add.text(300, 300, 'Game found!', {fill: '#000000', fontSize: '20px'});
            setTimeout(() => this.messege.destroy(), 2000);

            this.mpService.nextPlayerId = msg.state.next.id;
            this.createAfterGameSearch(msg.state);
        };

        this.mpService.onServerError = (msg) => {
            this.stopInputEvents();
            console.log(msg);
        }

    }

    createAfterGameSearch(state) {
        this.icon[0] = new Icon(this, 'grey', 16, 156);
        this.icon[1] = new Icon(this, 'red', 16, 312);
        this.icon[2] = new Icon(this, 'green', 16, 458);
        this.icon[3] = new Icon(this, 'yellow', 688, 156);
        this.icon[4] = new Icon(this, 'blue', 688, 312);
        this.icon[5] = new Icon(this, 'purple', 688, 458);

        this.cursor = this.add.image(16, 156, 'flood', 'cursor-over').setOrigin(0).setVisible(false);

        this.width = state.field.width;
        this.height = state.field.height;

        this.text1 = this.add.bitmapText(684, 30, 'atari', 'Moves', 20).setAlpha(0);
        this.text2 = this.add.bitmapText(694, 60, 'atari', '00', 40).setAlpha(0);
        this.text3 = this.add.bitmapText(180, 200, 'atari', 'So close!\n\nClick to\ntry again', 48).setAlpha(0);

        this.createGrid(state);
        this.createArrow();

        this.allowClick = this.mpService.playerData.isMyTurn;

        for (let i = 0; i < this.matched.length; i++)
        {
            let block = this.matched[i];

            block.setFrame(colorOfIndex(block.getData('color')));
        }

        this.particles = this.add.particles('flood');

        for (let i = 0; i < 6; i++)
        {
            this.createEmitter(colorOfIndex(i));
        }

        this.revealGrid();
    }

    createGrid(state) {
        //  The game is played in a 14x14 grid with 6 different colors

        this.grid = [];

        for (let x = 0; x < state.field.width; x++)
        {
            this.grid[x] = [];

            for (let y = 0; y < state.field.height; y++)
            {
                let sx = 166 + (x * 36);
                let sy = 66 + (y * 36);
                let color = state.field.cells[x][y].color;

                let block = this.add.image(sx, -600 + sy, 'flood', color);

                block.setData('oldColor', valueOfColor(color));
                block.setData('color', valueOfColor(color));
                block.setData('x', sx);
                block.setData('y', sy);

                this.grid[x][y] = block;
            }
        }


        this.mpService.playerData.position = state.positions[this.mpService.playerData.id];
        this.currentColor = valueOfColor(state.positions[state.next.id].color);

        switch (this.mpService.playerData.position.x) {
            case 0:
                switch (this.mpService.playerData.position.y) {
                    case 0:
                        this.playersCorner = 1;
                        break;
                    case state.field.height - 1:
                        this.playersCorner = 3;
                        break;
                }
                break;
            case state.field.width - 1:
                switch (this.mpService.playerData.position.y) {
                    case 0:
                        this.playersCorner = 2;
                        break;
                    case state.field.height - 1:
                        this.playersCorner =4;
                        break;
                }
                break;
        }
    }

    createArrow() {
        // Creating arrow
        this.arrow = this.add.image(85, 48, 'flood', 'arrow-white').setOrigin(0).setAlpha(0);
        let getArrowTween = () => this.tweens.add({
            targets: this.arrow,
            x: this.playersCorner % 2 == 0 ? '-=24' : '+=24',
            ease: 'Sine.easeInOut',
            duration: 900,
            yoyo: true,
            repeat: -1
        });
        this.arrow.move = getArrowTween();

        // Choosing corner
        this.input.keyboard.on('keydown_Q', () => {
            if (this.playersCorner != 1) {
                this.arrow.move.remove();
                this.playersCorner = 1;
                this.currentColor = this.grid[0][0].getData('color');
                this.arrow.setPosition(85, 48);
                this.arrow.flipX = false;
                this.arrow.move = getArrowTween();
            }
        });
        this.input.keyboard.on('keydown_W', () => {
            if (this.playersCorner != 2) {
                this.arrow.move.stop();
                this.playersCorner = 2;
                this.currentColor = this.grid[13][0].getData('color');
                this.arrow.setPosition(671, 48);
                this.arrow.flipX = true;
                this.arrow.move = getArrowTween();
            }
        });
        this.input.keyboard.on('keydown_E', () => {
            if (this.playersCorner != 3) {
                this.arrow.move.stop();
                this.playersCorner = 3;
                this.currentColor = this.grid[0][13].getData('color');
                this.arrow.setPosition(85, 516);
                this.arrow.flipX = false;
                this.arrow.move = getArrowTween();
            }
        });
        this.input.keyboard.on('keydown_R', () => {
            if (this.playersCorner != 4) {
                this.arrow.move.stop();
                this.playersCorner = 4;
                this.currentColor = this.grid[13][13].getData('color');
                this.arrow.setPosition(671, 516);
                this.arrow.flipX = true;
                this.arrow.move = getArrowTween();
            }
        });
    }

    revealGrid() {
        this.tweens.add({
            targets: this.gridBG,
            y: 300,
            ease: 'Power3'
        });

        let i = 0;

        for (let y = 13; y >= 0; y--)
        {
            for (let x = 0; x < 14; x++)
            {
                let block = this.grid[x][y];

                this.tweens.add({

                    targets: block,

                    y: block.getData('y'),

                    ease: 'Power3',
                    duration: 800,
                    delay: i

                });

                i += 1;
            }
        }

        i = 0;

        //  Icons
        this.tweens.add({
            targets: [ this.icon[0].shadow, this.icon[0].monster ],
            x: this.icon[0].shadow.getData('x'),
            ease: 'Power3',
            delay: i
        });

        this.tweens.add({
            targets: [ this.icon[3].shadow, this.icon[3].monster ],
            x: this.icon[3].shadow.getData('x'),
            ease: 'Power3',
            delay: i
        });

        i += 0;

        this.tweens.add({
            targets: [ this.icon[1].shadow, this.icon[1].monster ],
            x: this.icon[1].shadow.getData('x'),
            ease: 'Power3',
            delay: i
        });

        this.tweens.add({
            targets: [ this.icon[4].shadow, this.icon[4].monster ],
            x: this.icon[4].shadow.getData('x'),
            ease: 'Power3',
            delay: i
        });

        i += 0;

        this.tweens.add({
            targets: [ this.icon[2].shadow, this.icon[2].monster ],
            x: this.icon[2].shadow.getData('x'),
            ease: 'Power3',
            delay: i
        });

        this.tweens.add({
            targets: [ this.icon[5].shadow, this.icon[5].monster ],
            x: this.icon[5].shadow.getData('x'),
            ease: 'Power3',
            delay: i
        });

        //  Text

        this.tweens.add({
            targets: [ this.text1, this.text2, this.text4 ],
            alpha: 1,
            ease: 'Power3',
            delay: i
        });

        this.tweens.add({
            targets: [ this.text5 ],
            alpha: 1,
            ease: 'Power3',
            delay: i
        });

        i += 0;

        let movesTween = this.tweens.addCounter({
            from: 0,
            to: 25,
            ease: 'Power1',
            onUpdate: function (tween, targets, text)
            {
                text.setText(Phaser.Utils.String.Pad(tween.getValue().toFixed(), 2, '0', 1));
            },
            onUpdateParams: [ this.text2 ],
            delay: i
        });

        i += 0;

        this.tweens.add({
            targets: [ this.arrow ],
            alpha: 1,
            ease: 'Power3',
            delay: i
        });

        this.time.delayedCall(i, this.startInputEvents, [], this);
        this.time.delayedCall(i, this.debugBinds, [], this);
    }

    debugBinds() {
        this.input.keyboard.on('keydown_M', function () {

            this.moves++;
            this.text2.setText(Phaser.Utils.String.Pad(this.moves, 2, '0', 1));

        }, this);

        this.input.keyboard.on('keydown_X', function () {

            this.moves--;
            this.text2.setText(Phaser.Utils.String.Pad(this.moves, 2, '0', 1));

        }, this);
    }

    startInputEvents() {
        this.input.on('gameobjectover', this.onIconOver, this);
        this.input.on('gameobjectout', this.onIconOut, this);
        this.input.on('gameobjectdown', this.onIconDown, this);
    }

    stopInputEvents() {
        this.input.off('gameobjectover', this.onIconOver);
        this.input.off('gameobjectout', this.onIconOut);
        this.input.off('gameobjectdown', this.onIconDown);
    }

    onIconOver(pointer, gameObject) {
        let icon = gameObject;

        let newColor = icon.getData('color');

        //  Valid color?
        if (newColor !== this.currentColor)
        {
            this.cursor.setFrame('cursor-over');
        }
        else
        {
            this.cursor.setFrame('cursor-invalid');
        }

        this.cursor.setPosition(icon.x, icon.y);

        if (this.cursorTween)
        {
            this.cursorTween.stop();
        }

        this.cursor.setAlpha(1);
        this.cursor.setVisible(true);

        //  Change arrow color
        this.arrow.setFrame('arrow-' + colorOfIndex(newColor));

        //  Jiggle the monster :)
        let monster = icon.getData('monster');

        this.children.bringToTop(monster);

        this.monsterTween = this.tweens.add({
            targets: monster,
            y: '-=24',
            yoyo: true,
            repeat: -1,
            duration: 300,
            ease: 'Power2'
        });
    }

    onIconOut(pointer, gameObject) {
        this.monsterTween.stop(0);

		gameObject.getData('monster').setY(gameObject.y);

        this.cursorTween = this.tweens.add({
            targets: this.cursor,
            alpha: 0,
            duration: 300
        });

        this.arrow.setFrame('arrow-white');
    }

    onIconDown(pointer, gameObject) {
        if (!this.allowClick)
        {
            return;
        }

        let icon = gameObject;

        let newColor = icon.getData('color');

        //  Valid color?
        if (newColor === this.currentColor)
        {
            return;
        }

        let oldColor;
        switch (this.playersCorner) {
            case 1:
                oldColor = this.grid[0][0].getData('color');
                break;
            case 2:
                oldColor = this.grid[13][0].getData('color');
                break;
            case 3:
                oldColor = this.grid[0][13].getData('color');
                break;
            case 4:
                oldColor = this.grid[13][13].getData('color');
                break;
            default:
                console.log('Error at onIconDown');
        }

        if (oldColor !== newColor)
        {
            this.currentColor = newColor;

            this.matched = [];

            if (this.monsterTween)
            {
                this.monsterTween.stop(0);
            }

            this.cursor.setVisible(false);

            this.moves--;

            this.text2.setText(Phaser.Utils.String.Pad(this.moves, 2, '0', 1));



            this.floodFillFromCorner(oldColor, newColor);

            if (this.matched.length > 0)
            {
                this.allowClick = false;

                this.startFlow();
            }
        }
    }

    createEmitter(color) {
        this.emitters[color] = this.particles.createEmitter({
            frame: color,
            lifespan: 1000,
            speed: { min: 300, max: 400 },
            alpha: { start: 1, end: 0 },
            scale: { start: 0.5, end: 0 },
            rotate: { start: 0, end: 360, ease: 'Power2' },
            blendMode: 'ADD',
            on: false
        });
    }

    startFlow() {
        this.matched.sort(function (a, b) {

            let aDistance = Phaser.Math.Distance.Between(a.x, a.y, 166, 66);
            let bDistance = Phaser.Math.Distance.Between(b.x, b.y, 166, 66);

            return aDistance - bDistance;

        });

        //  Swap the sprites

        let t = 0;
        let inc = 2; //(this.matched.length > 98) ? 6 : 12;

        for (let i = 0; i < this.matched.length; i++)
        {
            let block = this.matched[i];

            let blockColor = colorOfIndex(block.getData('color'));
            let oldBlockColor = colorOfIndex(block.getData('oldColor'));

            let emitter = this.emitters[oldBlockColor];

            this.time.delayedCall(t, function (block, blockColor) {

                block.setFrame(blockColor);

                emitter.explode(6, block.x, block.y);

            }, [ block, blockColor, emitter ]);

            t += inc;
        }

        this.time.delayedCall(t, function () {

            this.allowClick = true;

            if (this.checkWon())
            {
                this.gameWon();
            }
            else if (this.moves === 0)
            {
                this.gameLost();
            }

        }, [], this);
    }

    checkWon() {
        let topLeft = this.grid[0][0].getData('color');

        for (let x = 0; x < 14; x++)
        {
            for (let y = 0; y < 14; y++)
            {
                if (this.grid[x][y].getData('color') !== topLeft)
                {
                    return false;
                }
            }
        }

        return true;
    }

    clearGrid() {
        //  Hide everything :)

        this.tweens.add({
            targets: [
                this.icon[0].monster, this.icon[0].shadow,
                this.icon[1].monster, this.icon[1].shadow,
                this.icon[2].monster, this.icon[2].shadow,
                this.icon[3].monster, this.icon[3].shadow,
                this.icon[4].monster, this.icon[4].shadow,
                this.icon[5].monster, this.icon[5].shadow,
                this.arrow,
                this.cursor
            ],
            alpha: 0,
            duration: 500,
            delay: 500
        });

        let i = 500;

        for (let y = 13; y >= 0; y--)
        {
            for (let x = 0; x < 14; x++)
            {
                let block = this.grid[x][y];

                this.tweens.add({

                    targets: block,

                    scaleX: 0,
                    scaleY: 0,

                    ease: 'Power3',
                    duration: 800,
                    delay: i

                });

                i += 10;
            }
        }

        return i;
    }

    gameLost() {
        this.stopInputEvents();

        this.text1.setText("Lost!");
        this.text2.setText(':(');

        let i = this.clearGrid();

        this.text3.setAlpha(0);
        this.text3.setVisible(true);

        this.tweens.add({
            targets: this.text3,
            alpha: 1,
            duration: 1000,
            delay: i
        });

        this.input.once('pointerdown', this.resetGame, this);
    }

    resetGame() {
        this.text1.setText("Moves");
        this.text2.setText("00");
        this.text3.setVisible(false);

        //  Show everything :)

        this.arrow.setFrame('arrow-white');

        this.tweens.add({
            targets: [
                this.icon[0].monster, this.icon[0].shadow,
                this.icon[1].monster, this.icon[1].shadow,
                this.icon[2].monster, this.icon[2].shadow,
                this.icon[3].monster, this.icon[3].shadow,
                this.icon[4].monster, this.icon[4].shadow,
                this.icon[5].monster, this.icon[5].shadow,
                this.arrow,
                this.cursor
            ],
            alpha: 1,
            duration: 500,
            delay: 500
        });

        let i = 500;

        for (let y = 13; y >= 0; y--)
        {
            for (let x = 0; x < 14; x++)
            {
                let block = this.grid[x][y];

                //  Set a new color
                let color = Phaser.Math.Between(0, 5);

                block.setFrame(colorOfIndex(color));

                block.setData('oldColor', color);
                block.setData('color', color);

                this.tweens.add({

                    targets: block,

                    scaleX: 1,
                    scaleY: 1,

                    ease: 'Power3',
                    duration: 800,
                    delay: i

                });

                i += 10;
            }
        }

        for (let i = 0; i < this.matched.length; i++)
        {
            let block = this.matched[i];

            block.setFrame(colorOfIndex(block.getData('color')));
        }

        this.currentColor = this.grid[0][0].getData('color');

        let movesTween = this.tweens.addCounter({
            from: 0,
            to: 25,
            ease: 'Power1',
            onUpdate: function (tween, targets, text)
            {
                text.setText(Phaser.Utils.String.Pad(tween.getValue().toFixed(), 2, '0', 1));
            },
            onUpdateParams: [ this.text2 ],
            delay: i
        });

        this.moves = 25;

        this.time.delayedCall(i, this.startInputEvents, [], this);
    }

    gameWon() {
        this.stopInputEvents();

        this.text1.setText("Won!!");
        this.text2.setText(':)');

        let i = this.clearGrid();

        //  Put the winning monster in the middle

        let monster = this.add.image(400, 300, 'flood', 'icon-' + colorOfIndex(this.currentColor));

        monster.setScale(0);

        this.tweens.add({
            targets: monster,
            scaleX: 4,
            scaleY: 4,
            angle: 360 * 4,
            duration: 1000,
            delay: i
        });

        this.time.delayedCall(2000, this.boom, [], this);
    }

    boom() {
        let color = colorOfIndex(Phaser.Math.Between(0, 5));

        this.emitters[color].explode(8, Phaser.Math.Between(128, 672), Phaser.Math.Between(28, 572))

        color = colorOfIndex(Phaser.Math.Between(0, 5));

        this.emitters[color].explode(8, Phaser.Math.Between(128, 672), Phaser.Math.Between(28, 572))

        this.time.delayedCall(100, this.boom, [], this);
    }
    fromCornerToCoords() {
        switch (this.playersCorner) {
            case 1:
                return {x: 0, y: 0};
            case 2:
                return {x: 13, y: 0};
            case 3:
                return {x: 0, y: 13};
            case 4:
                return {x: 13, y: 13};
            default:
                console.log("How did you just do this?! o_0")
        }
    }
    floodFillFromCorner(oldColor, newColor) {
        this.floodFill(oldColor, newColor, this.fromCornerToCoords().x, this.fromCornerToCoords().y);
    }

    floodFill(oldColor, newColor, x, y) {
        if (oldColor === newColor || this.grid[x][y].getData('color') !== oldColor) {
            return;
        }

        this.grid[x][y].setData('oldColor', oldColor);
        this.grid[x][y].setData('color', newColor);

        if (this.matched.indexOf(this.grid[x][y]) === -1) {
            this.matched.push(this.grid[x][y]);
        }

        if (x > 0) {
            this.floodFill(oldColor, newColor, x - 1, y);
        }

        if (x < 13) {
            this.floodFill(oldColor, newColor, x + 1, y);
        }

        if (y > 0) {
            this.floodFill(oldColor, newColor, x, y - 1);
        }

        if (y < 13) {
            this.floodFill(oldColor, newColor, x, y + 1);
        }
    }
}