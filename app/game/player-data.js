export default class PlayerData{
    constructor(bool){
        if(bool === undefined){ this.isMyTurn = false; }
        else{ this.isMyTurn = bool; }
    }
}