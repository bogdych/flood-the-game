export default class FloodScene extends Phaser.Scene {

	constructor(sceneName) {
		super({key: sceneName});
		this._inputEnabled = true;
		this.emitters = {};
		this.grid = [];
		this.matched = [];
	}

	set inputEnabled(enable) {
		this._inputEnabled = enable;
	}

	/**
	 * @returns {boolean} true on input is turned on
	 */
	isInputEnabled() {
		return this._inputEnabled;
	}

	preload() {
		this.load.bitmapFont(
			'atari',
			'assets/fonts/bitmap/atari-smooth.png',
			'assets/fonts/bitmap/atari-smooth.xml');
		this.load.atlas(
			'flood',
			'assets/games/flood/blobs.png',
			'assets/games/flood/blobs.json');
	}

	create() {
		this.add.image(400, 300, 'flood', 'background');
		this.gridBG = this.add.image(400, 600 + 300, 'flood', 'grid');
	}

	createArrow(tweenX) {
		this.arrow = this.add.image(
			85,
			48,
			'flood',
			'arrow-white').setOrigin(0).setAlpha(0);
		this.getArrowTween(tweenX);
	}

	getArrowTween(tweenX) {
		this.arrow.move = this.tweens.add({
			targets: this.arrow,
			x: tweenX,
			ease: 'Sine.easeInOut',
			duration: 900,
			yoyo: true,
			repeat: -1
		});
	}

}
