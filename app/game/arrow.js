export default class Arrow {
    constructor (scene, corner, isActive) {
        this.getCoods = function (arg) {
            switch (arg) {
                case 1:
                    return {x: 85, y: 48};
                case 2:
                    return {x: 671, y: 48};
                case 3:
                    return {x: 85, y: 516};
                case 4:
                    return {x: 671, y: 516};
                default:
                    console.log("Nah, man... That's impossible");
                    return {x: -1, y: -1};
            }
        }
        this.x = this.getCoods(corner).x;
        this.y = this.getCoods(corner).y;
        this.scene = scene;

        this.isActive = isActive;
        this.isMirrored = corner % 2 === 0;

        this.arrow = scene.add.image(
            this.x, 
            this.y, 
            'flood', 
            'arrow-white').setOrigin(0);
        this.arrow.flipX = this.isMirrored;


        if (this.isActive) {
            this.arrow.move = this.scene.tweens.add({
                targets: this.arrow,
                x: this.isMirrored ? '-=24' : '+=24',
                ease: 'Sine.easeInOut',
                duration: 900,
                yoyo: true,
                repeat: -1
            });
            this.arrow.move.pause();
        } 
        else {
            this.arrow.setTint(0x777777).setAlpha(0.75);
        }
    }

    startTween() {
        if (this.isActive && this.arrow.move.isPaused()) {
            this.arrow.move.restart();
            this.arrow.move.pause();
            this.arrow.move.resume();
        }
    }

    stopTween() {
        if (this.isActive && this.arrow.move.isPlaying()) {
            this.arrow.move.restart();
            this.arrow.move.pause();
            this.arrow.setPosition(this.x, this.y);
        }
    }

    hideArrow() {
        this.arrow.setVisible(false);
    }

    showArrow() {
        this.arrow.setVisible(true);
    }

    moveArrow(corner) {
        this.arrow.setPosition(
            this.getCoods(corner).x,
            this.getCoods(corner).y
            );
        this.isMirrored = corner % 2 === 0;
        this.arrow.flipX = this.isMirrored;
    }
}