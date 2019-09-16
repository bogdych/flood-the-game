import { GRID_COODS } from "./flood-multi-player";
export const AVATAR_NAMES = ['crown', 'star', 'ghost', 'robot'];

export default class Avatar {
    constructor (scene, x, y, id, isMe) {
        this.id = id;
        this.isMe = isMe;
        if (isMe) {
            this.image = scene.add.image(
                GRID_COODS.X + x * 36,
                GRID_COODS.Y + y * 36,
                'avatars', 'star.png').setAlpha(0);
            this.name = 'star';
        } 
        else {
            this.image = scene.add.image(
                GRID_COODS.X + x * 36,
                GRID_COODS.Y + y * 36,
                'avatars', 'ghost.png').setAlpha(0);
            this.name = 'ghost';
        }
    }
}