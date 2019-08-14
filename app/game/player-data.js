export default class PlayerData{
    constructor(id, nickname, bool){
        if(!bool){
            this.isMyTurn = false;
        } else{
            this.isMyTurn = bool;
        }
        this.id = id;
        this.nickname = nickname;
        this.position;
        this.currentColor;
    }

}