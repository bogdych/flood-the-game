import { GRID_COODS } from "./flood-multi-player";
export const AVATAR_NAMES = ['crown', 'star', 'ghost', 'robot'];

export default class Avatar {
    constructor (scene, x, y, id, count, isMe) {
        if (count >= 0 && count <= 4) {
            this.image = scene.add.image(
                GRID_COODS.X + x * 36,
                GRID_COODS.Y + y * 36,
                'avatars', AVATAR_NAMES[count] + '.png').setAlpha(0);
                this.id = id;
                this.isMe = isMe;
                if (isMe) console.log('Ama ' + AVATAR_NAMES[count]);
                this.name = AVATAR_NAMES[count];
        }
        else {
            console.log('unexpected count in Avatar constructor');
        }
    }
}