export default class PlayerData{
    constructor(id, bool){
        if(!bool){
            this.isMyTurn = false;
        } else{
            this.isMyTurn = bool;
        }
        this.id = id;
    }
}