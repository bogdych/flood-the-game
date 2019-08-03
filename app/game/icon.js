export default class Icon {    
    constructor (scene, color, x, y) {
        let sx = (x < 400) ? -200 : 1000;

        this.monster = scene.add.image(sx, y, 'flood', 'icon-' + color).setOrigin(0);

        let shadow = scene.add.image(sx, y, 'flood', 'shadow');

        shadow.setData('color', scene.frames.indexOf(color));

        shadow.setData('x', x);

        shadow.setData('monster', this.monster);

        shadow.setOrigin(0);

        shadow.setInteractive();

        this.shadow = shadow;
    }
}