import { GRID_COODS, DELTA } from "./flood-multi-player";



export default class Arrow {
    constructor (scene, x, y, corner, myCorner, isActive) {
        console.log(scene);
        console.log(x);
        console.log(y);
        console.log(corner);
        console.log(myCorner);
        console.log(isActive);
        this.isActive = isActive;
        this.isMirrored = corner % 2 === 0;
        this.isRightSide = myCorner % 2 === 0;


        // this.getCoods = function (arg) {
        //     switch (arg) {
        //         case 1:
        //             return {x: 107, y: 66};
        //         case 2:
        //             return {x: 693, y: 66};
        //         case 3:
        //             return {x: 107, y: 534};
        //         case 4:
        //             return {x: 693, y: 534};
        //         default:
        //             console.log("Nah, man... That's impossible");
        //             return {x: -1, y: -1};
        //     }
        // }

        this.getCoods = function(sx, sy) {
            if (this.isMirrored) {
                let res = {
                    x: GRID_COODS.X + sx * 36 + DELTA.X,
                    y: GRID_COODS.Y + sy * 36 + DELTA.Y
                }
                console.log(res);
                return res;
            }
            else {
                let res = {
                    x: GRID_COODS.X + sx * 36 - DELTA.X,
                    y: GRID_COODS.Y + sy * 36 - DELTA.Y
                }
                console.log(res);
                return res;
            }
        }

        this.x = this.getCoods(x, y).x;
        this.y = this.getCoods(x, y).y;
        this.scene = scene;


        this.arrow = scene.add.image(
            this.x, 
            this.y, 
            'flood', 
            'arrow-white').setAlpha(0);
            
            
            if (this.isActive) {
                this.arrow.move = this.scene.tweens.add({
                    targets: this.arrow,
                    x: this.isRightSide ? '-=24' : '+=24',
                    ease: 'Sine.easeInOut',
                duration: 900,
                yoyo: true,
                repeat: -1
            });
            this.arrow.move.pause();
        } 
        else {
            this.arrow.setTint(0x777777);
        }
        this.isMirrored = corner % 2 === 0;
        this.arrow.flipX = this.isMirrored;
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

    moveArrow(coods, corner) {
        this.isMirrored = corner % 2 === 0;
        console.log(corner);
        this.arrow.flipX = this.isMirrored;
        this.arrow.setPosition(
            this.getCoods(coods.x, coods.y).x,
            this.getCoods(coods.x, coods.y).y
            );
    }
}