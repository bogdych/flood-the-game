import Phaser from 'phaser';
import Icon from "./icon";
import MultiplayerService from './multi-player-service';
import {colorOfIndex, valueOfColor} from './colors';
import FloodScene from "./flood-scene";
import Arrow from './arrow';
import Avatar from './avatar';

export const GRID_COODS = {
	X: 166,
	Y: 66,
	OFFSET: -600
}

export const DELTA = {
    X: GRID_COODS.X - 107, 
    Y: GRID_COODS.Y - 66
}

export default class FloodMultiPlayer extends FloodScene {

	constructor() {
		super('FloodMultiPlayer');
		this.currentColor = -1;
		this.icons = [];
		this.playersCorner = 1;
		this.isGameStarted = false;
	}

	preload() {
		super.preload();
		this.load.multiatlas('avatars', 'assets/games/flood/avatars.json', 'assets/games/flood');
	}

	create() {
		super.create();
		this.initMPlayerSession();
	}

	update() {
		if (this.isGameStarted) {
			this.children.bringToTop(this.playerArrow.arrow);
			// this.children.bringToTop(this.enemyArrow.arrow);
		}
	}

	initMPlayerSession() {
		this.mpService = new MultiplayerService(() => {
			this.mpService.findFloodGameStandard();
			this.searchText = this.add.bitmapText(230, 300, 'atari', 'Search game', 30).setAlpha(0);
			this.tweens.add({
				targets: [this.searchText],
				alpha: 1,
				ease: 'Power3',
			});
		});

		this.mpService.onGameReady = (msg) => {
			console.log("Game found");
			console.log(JSON.stringify(msg));
			this.searchText.destroy();
			this.onGameFound(msg.state);
		};

		this.mpService.onGameState = (msg) => this.onStateChange(msg.state);

		this.mpService.onServerError = (msg) => alert(msg.message);
		
		this.mpService.socket.onClose(() => {
			this.scene.stop('FloodMultiPlayer');
            this.scene.start('MainMenu');
		});
	}

	onGameFound(state) {
		this.icons[0] = new Icon(this, 'grey', 16, 156);
		this.icons[1] = new Icon(this, 'red', 16, 312);
		this.icons[2] = new Icon(this, 'green', 16, 458);
		this.icons[3] = new Icon(this, 'yellow', 688, 156);
		this.icons[4] = new Icon(this, 'blue', 688, 312);
		this.icons[5] = new Icon(this, 'purple', 688, 458);

		this.cursor = this.add.image(
			16,
			156,
			'flood',
			'cursor-over').setOrigin(0).setVisible(false);

		const gameField = state.field;
		const playerPositions = state.positions;
		const nextPlayerId = state.next.id;
		const playerData = this.mpService.playerData;

		this.width = gameField.width;
		this.height = gameField.height;
		
		this.createGrid(state);
		
		playerData.isMyTurn = nextPlayerId === playerData.id;
		playerData.position = playerPositions[playerData.id];

		this.currentColor = valueOfColor(playerPositions[nextPlayerId].color);

		this.mpService.nextPlayerId = nextPlayerId;
		
		playerData.corner = this.getCorner(playerData.position.x, playerData.position.y);

		this.playersCorner = this.getCorner(
			playerPositions[nextPlayerId].x,
			playerPositions[nextPlayerId].y
		);

		//const turnMessage = playerData.isMyTurn ? 'Yes' : 'No';
		
		this.text3 = this.add.bitmapText(180, 200, 'atari', 'So close!\n\nClick to\ntry again', 48).setAlpha(0);
		
		//this.createArrow(this.playersCorner % 2 === 0 ? '-=24' : '+=24');
		//this.setArrow(this.playersCorner);
		
		
		// Creating arrows
		const myCorner = this.getCorner(
			playerPositions[playerData.id].x,
			playerPositions[playerData.id].y
			);
		this.playerArrow = new Arrow(
			this, 
			playerPositions[nextPlayerId].x,
			playerPositions[nextPlayerId].y,
			this.playersCorner, 
			myCorner,
			true);
		// this.enemyArrow = new Arrow(
		// 	this,
		// 	playerPositions[nextPlayerId].x,
		// 	playerPositions[nextPlayerId].y,
		// 	this.playersCorner, 
		// 	false);
		if (playerData.isMyTurn) {
			//this.enemyArrow.hideArrow();
			this.playerArrow.startTween();
			this.playerArrow.turnOn();
			this.playerArrow.arrow.setAlpha(0);
		}
		else {
			this.playerArrow.stopTween();
			this.playerArrow.turnOff();
			this.playerArrow.arrow.setAlpha(0);
		}

		// Creating avatars
		this.avatarArray = [];
		for (let propt in playerPositions) {
			let temp = new Avatar(
					this,
					playerPositions[propt].x,
					playerPositions[propt].y, 
					propt,
					propt == playerData.id	
				);
			this.avatarArray.push(temp);
		}
		
		if (playerData.isMyTurn) {
			this.text1 = this.add.bitmapText(
				674, 
				90, 
				'atari', 
				'Your', 20).setAlpha(0);
		}
		else {
			this.text1 = this.add.bitmapText(
				674, 
				90, 
				'atari', 
				"Enemy", 20).setAlpha(0);
		}
		this.text2 = this.add.bitmapText(685, 115, 'atari', 'turn', 20).setAlpha(0);

		for (let i = 0; i < this.matched.length; i++) {
			let block = this.matched[i];
			block.setFrame(colorOfIndex(block.getData('color')));
		}

		this.particles = this.add.particles('flood');

		for (let i = 0; i < 6; i++) {
			this.createEmitter(colorOfIndex(i));
		}
		
		this.isGameStarted = true;
		this.inputEnabled = playerData.isMyTurn;
		this.revealGrid();
	}

	createGrid(state) {
		//  The game is played in a 14x14 grid with 6 different colors

		this.grid = [];

		for (let x = 0; x < state.field.width; x++) {
			this.grid[x] = [];

			for (let y = 0; y < state.field.height; y++) {
				let sx = GRID_COODS.X + (x * 36);
				let sy = GRID_COODS.Y + (y * 36);
				let color = state.field.cells[x][y].color;

				let block = this.add.image(sx, GRID_COODS.OFFSET + sy, 'flood', color);

				block.setData('oldColor', valueOfColor(color));
				block.setData('color', valueOfColor(color));
				block.setData('x', sx);
				block.setData('y', sy);

				this.grid[x][y] = block;
			}
		}

	}

	getCorner(x, y) {
		switch (x) {
			case 0:
				switch (y) {
					case 0:
						return 1;
					case this.height - 1:
						return 3;
				}
				break;
			case this.width - 1:
				switch (y) {
					case 0:
						return 2;
					case this.height - 1:
						return 4;
				}
				break;
			default:
				console.log("Wrong! (${x},${y}) corner");
		}
	}

	revealGrid() {
		this.tweens.add({
			targets: this.gridBG,
			y: 300,
			ease: 'Power3'
		});

		let i = 0;

		for (let y = 13; y >= 0; y--) {
			for (let x = 0; x < 14; x++) {
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
			targets: [this.icons[0].shadow, this.icons[0].monster],
			x: this.icons[0].shadow.getData('x'),
			ease: 'Power3',
			delay: i
		});

		this.tweens.add({
			targets: [this.icons[3].shadow, this.icons[3].monster],
			x: this.icons[3].shadow.getData('x'),
			ease: 'Power3',
			delay: i
		});

		this.tweens.add({
			targets: [this.icons[1].shadow, this.icons[1].monster],
			x: this.icons[1].shadow.getData('x'),
			ease: 'Power3',
			delay: i
		});

		this.tweens.add({
			targets: [this.icons[4].shadow, this.icons[4].monster],
			x: this.icons[4].shadow.getData('x'),
			ease: 'Power3',
			delay: i
		});

		this.tweens.add({
			targets: [this.icons[2].shadow, this.icons[2].monster],
			x: this.icons[2].shadow.getData('x'),
			ease: 'Power3',
			delay: i
		});

		this.tweens.add({
			targets: [this.icons[5].shadow, this.icons[5].monster],
			x: this.icons[5].shadow.getData('x'),
			ease: 'Power3',
			delay: i
		});

		//  Text

		this.tweens.add({
			targets: [this.text1, this.text2],
			alpha: 1,
			ease: 'Power3',
			delay: i
		});

		this.tweens.add({
			targets: [this.text5],
			alpha: 1,
			ease: 'Power3',
			delay: i
		});

		this.tweens.add({
			targets: [this.playerArrow.arrow],
			alpha: 1,
			ease: 'Power3',
			delay: i
		});

		// this.tweens.add({
		// 	targets: [this.enemyArrow.arrow],
		// 	alpha: 0.75,
		// 	ease: 'Power3',
		// 	delay: i
		// });


		// for (let i = 0; i < this.avatarArray.length; i++) {
		// 	this.tweens.add({
		// 		targets: [this.avatarArray[i]],
		// 		alpha: 1,
		// 		ease: 'Power3',
		// 		delay: i * 2
		// 	});
		// }

		this.time.delayedCall(i, this.startInputEvents, [], this);
		this.time.delayedCall(i, function () {
			for (let i = 0; i < this.avatarArray.length; i++) {
				this.tweens.add({
					targets: [this.avatarArray[i].image],
					alpha: 1,
					ease: 'Power3',
					delay: i + 1000
				});
			}
		}, [], this);
		// setTimeout(function () {
		// 	for (let i = 0; i < this.avatarArray.length; i++) {
		// 		this.tweens.add({
		// 			targets: [this.avatarArray[i]],
		// 			alpha: 1,
		// 			ease: 'Power3',
		// 			delay: i
		// 		})
		// 	}
		// }, i);
	}

	onStateChange(state) {
		this.inputEnabled = false;
		const playerData = this.mpService.playerData;

		if (state.positions[this.mpService.nextPlayerId]) {
			let oldColor = this.grid[this.getCoords(this.playersCorner).x][this.getCoords(this.playersCorner).y].getData('color');
			let newColor = valueOfColor(state.positions[this.mpService.nextPlayerId].color);
		//check if player which should make step are still in game

			if (oldColor !== newColor) {
				this.currentColor = newColor;
				
				this.matched = [];
				
				if (this.monsterTween) {
					this.monsterTween.stop(0);
				}
				
				this.cursor.setVisible(false);
				
				this.floodFillFromCorner(oldColor, newColor);
				
				if (this.matched.length > 0) {
					this.startFlow();
				}
			}
		} else {
			//alert("Player at position " + this.getCorner())
		}

		if (state.gameStatus === "finished") {
			this.onFinished(state, this.mpService.playerData);
			return;
		}

		playerData.isMyTurn = state.next.id === playerData.id;
		
		this.mpService.nextPlayerId = state.next.id;
		
		this.playersCorner = this.getCorner(state.positions[this.mpService.nextPlayerId].x, state.positions[this.mpService.nextPlayerId].y);
		
		this.currentColor = valueOfColor(state.positions[playerData.id].color);

		//this.setArrow(this.playersCorner);

		if (playerData.isMyTurn) {
			this.playerArrow.moveArrow(
				state.positions[this.mpService.nextPlayerId],
				this.playersCorner);
			this.playerArrow.startTween();
			this.playerArrow.turnOn();
			this.text1.setText('Your');
			//this.enemyArrow.hideArrow();
		} else {
			this.playerArrow.stopTween();
			this.playerArrow.moveArrow(
				state.positions[this.mpService.nextPlayerId],
				this.playersCorner);
			this.playerArrow.arrow.setFrame('arrow-white');
			this.playerArrow.turnOff();
			this.text1.setText("Enemy");
			//this.enemyArrow.showArrow();
		}

		this.inputEnabled = playerData.isMyTurn;
		
		if (state.playersStatus[playerData.id] === "loser") {
			this.stopInputEvents();
			this.text1.setText("Lost!");
			this.text2.setText(':(');
            this.time.delayedCall(6000,
                                () => this.input.once('pointerup', this.resetGame, this),
                                [],
                                this);
								
		}
	}

	onFinished(state, playerData) {
		
		if (state.playersStatus[playerData.id] === "winner") {
			this.gameWon();
		} else {
			this.gameLost();
		}
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

		if (this.isInputEnabled()) {
		//  Valid color?
			if (newColor !== this.currentColor) {
				this.cursor.setFrame('cursor-over');
			} else {
				this.cursor.setFrame('cursor-invalid');
			}
		}
		else {
			this.cursor.setFrame('cursor-over');
		}

		this.cursor.setPosition(icon.x, icon.y);

		if (this.cursorTween) {
			this.cursorTween.stop();
		}

		this.cursor.setAlpha(1);
		this.cursor.setVisible(true);

		//  Change arrow color
		if (this.isInputEnabled()) this.playerArrow.arrow.setFrame('arrow-' + colorOfIndex(newColor));

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

		this.playerArrow.arrow.setFrame('arrow-white');
	}

	onIconDown(pointer, gameObject) {
		if (this.isInputEnabled()) {
			let newColor = gameObject.getData('color');

			//  Valid color?
			if (newColor === this.currentColor) {
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

			if (oldColor !== newColor) {
				this.currentColor = newColor;

				this.matched = [];

				if (this.monsterTween) {
					this.monsterTween.stop(0);
				}

				this.cursor.setVisible(false);

				this.mpService.socket.send(JSON.stringify({
					type: "makeAction",
					action: {
						x: this.getCoords(this.playersCorner).x.toString(),
						y: this.getCoords(this.playersCorner).y.toString(),
						color: colorOfIndex(newColor)
					}
				}));
				

			}
		}
	}

	createEmitter(color) {
		this.emitters[color] = this.particles.createEmitter({
			frame: color,
			lifespan: 1000,
			speed: {min: 300, max: 400},
			alpha: {start: 1, end: 0},
			scale: {start: 0.5, end: 0},
			rotate: {start: 0, end: 360, ease: 'Power2'},
			blendMode: 'ADD',
			on: false
		});
	}

	startFlow() {
		this.matched.sort(function (a, b) {

			let aDistance = Phaser.Math.Distance.Between(a.x, a.y, GRID_COODS.X, GRID_COODS.Y);
			let bDistance = Phaser.Math.Distance.Between(b.x, b.y, GRID_COODS.Y, GRID_COODS.Y);

			return aDistance - bDistance;

		});

		//  Swap the sprites

		let t = 0;
		let inc = 2; //(this.matched.length > 98) ? 6 : 12;

		for (let i = 0; i < this.matched.length; i++) {
			let block = this.matched[i];

			let blockColor = colorOfIndex(block.getData('color'));
			let oldBlockColor = colorOfIndex(block.getData('oldColor'));

			let emitter = this.emitters[oldBlockColor];

			this.time.delayedCall(t, function (block, blockColor) {

				block.setFrame(blockColor);

				emitter.explode(6, block.x, block.y);

			}, [block, blockColor, emitter]);

			t += inc;
		}
	}

	checkWon() {
		let topLeft = this.grid[0][0].getData('color');

		for (let x = 0; x < this.width; x++) {
			for (let y = 0; y < this.height; y++) {
				if (this.grid[x][y].getData('color') !== topLeft) {
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
				this.icons[0].monster, this.icons[0].shadow,
				this.icons[1].monster, this.icons[1].shadow,
				this.icons[2].monster, this.icons[2].shadow,
				this.icons[3].monster, this.icons[3].shadow,
				this.icons[4].monster, this.icons[4].shadow,
				this.icons[5].monster, this.icons[5].shadow,
				this.playerArrow.arrow,
				// this.enemyArrow.arrow,
				this.cursor
			],
			alpha: 0,
			duration: 500,
			delay: 500
		});

		// for (let i = 0; i < this.avatarArray.length; i++) {
		// 	this.tweens.add({
		// 		targets: [this.avatarArray[i]],
		// 		alpha: 0,
		// 		duration: 500,
		// 		delay: 500
		// 	});
		// }

		let i = 500;

		for (let i = 0; i < this.avatarArray.length; i++) {
			this.tweens.add({
				targets: [this.avatarArray[i].image],
				alpha: 0,
				ease: 'Power3',
				delay: 500
			});
		}

		for (let y = 13; y >= 0; y--) {
			for (let x = 0; x < 14; x++) {
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
		this.isGameStarted = false;

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

		this.input.once('pointerup', this.resetGame, this);
	}

	resetGame() {
		this.mpService.socket.socket.close();
		this.scene.stop('FloodMultiPlayer');
		this.scene.start('MainMenu');
	}

	gameWon() {
		this.stopInputEvents();
		this.isGameStarted = false;

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
		this.time.delayedCall(6000, () => this.input.once('pointerup', this.resetGame, this), [], this);
	}

	boom() {
		let color = colorOfIndex(Phaser.Math.Between(0, 5));

		this.emitters[color].explode(8, Phaser.Math.Between(128, 672), Phaser.Math.Between(28, 572));

		color = colorOfIndex(Phaser.Math.Between(0, 5));

		this.emitters[color].explode(8, Phaser.Math.Between(128, 672), Phaser.Math.Between(28, 572));

		this.time.delayedCall(100, this.boom, [], this);
	}

	getCoords(corner) {
		switch (corner) {
			case 1:
				return {x: 0, y: 0};
			case 2:
				return {x: 13, y: 0};
			case 3:
				return {x: 0, y: 13};
			case 4:
				return {x: 13, y: 13};
			default:
				console.log("How did you just do this?! o_0");
		}
	}

	floodFillFromCorner(oldColor, newColor) {
		this.floodFill(oldColor, newColor, this.getCoords(this.playersCorner).x, this.getCoords(this.playersCorner).y);
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
