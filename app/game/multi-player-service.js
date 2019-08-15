import WebSocketService from './web-socket-service';
import PlayerData from './player-data';

export default class MultiplayerService {
	constructor(callbackOnOpen) {
		this.socket = new WebSocketService();
		this.socket.onMessage((event) => this.onMessage(event.data));
		if(callbackOnOpen) {
			this.socket.onOpen(callbackOnOpen);
		}
		this.socket.init();
	}

	onMessage(data) {
		let msg = JSON.parse(data);
		console.log(data);
		if ("type" in msg) {
			switch (msg.type) {
				case "playerInfo":
					this.onPlayerInfo(msg);
					break;
				case "error":
					this.onServerError(msg);
					break;
				case "gameReady":
					this.onGameReady(msg);
					break;
				case "gameState":
					this.onGameState(msg);
					break;
				default:
					alert("Unexpected message type response from server");
					break;
			}
		} else {
			alert("Unexpected response from server");
		}
	}

	onPlayerInfo(msg) {
		this.playerData = new PlayerData(msg.player.id, msg.player.nickname);
		console.log("id player set " + this.playerData.id);
		if(this.msgTo)
			this.findGame(this.msgTo);
	}
	onServerError(msg) {
		console.log(JSON.stringify(msg));
	}
	onClientError() {
		alert("Error");
	}
	onGameState(msg) {
		console.log("Got gameState");
		this.state = msg.state;
	}
	onGameReady(msg) {
		console.log("GameFound");
		this.playerData.isMyTurn = msg.state.next.id === this.playerData.id;
		this.state = msg.state;
	}

	findGame(msgToServer) {
		switch (this.socket.socket.readyState) {
			case WebSocket.OPEN:
				this.socket.send(JSON.stringify(msgToServer));
				console.log(JSON.stringify(msgToServer));
				break;
			case WebSocket.CONNECTING:
				this.msgTo = msgToServer;
				break;
			default:
				this.onClientError();
		}
	}

	findFloodGameStandard() {
		this.findGame({
			type: "findGame",
			name: "flood",
			gameType: "standard"
		})
	}
}
