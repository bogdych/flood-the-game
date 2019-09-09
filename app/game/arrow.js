export default class Arrow {
    constructor (scene, corner, isActive) {
        this.getCoods = function (arg) {
            switch (arg) {
                case 1:
                    this.x = 85;
                    this.y = 48;
                    return {x: 85, y: 48};
                case 2:
                    this.x = 671;
                    this.y = 48;
                    return {x: 671, y: 48};
                case 3:
                    this.x = 85;
                    this.y = 516;
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
        this.isStopped = false;

        this.arrow = scene.add.image(
            this.x, 
            this.y, 
            'flood', 
            'arrow-white').setOrigin(0);
        this.arrow.flipX = this.isMirrored;


        if (this.isActive) {
            this.getArrowTween = () => this.scene.tweens.add({
                targets: this.arrow,
                x: this.isMirrored ? '-=24' : '+=24',
                ease: 'Sine.easeInOut',
                duration: 900,
                yoyo: true,
                repeat: -1
            });
            this.arrow.move = this.getArrowTween();
        } 
        else {
            this.arrow.setTint(0x777777);
        }
    }

    restartTween() {
        if (this.isActive && !this.isStopped) {
            this.arrow.move.remove();
            this.arrow.move = this.getArrowTween();
            this.isStopped = false;
        }
    }

    stopTween() {
        if (this.isActive && !this.isStopped) {
            this.arrow.move.remove();
            this.arrow.setPosition(this.x, this.y);
            this.isStopped = true;
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