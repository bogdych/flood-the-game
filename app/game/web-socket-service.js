export default class WebSocketService {
	static get URL() {
		let port = window.location.port;
		if (!port) {
			port = window.location.protocol === "http:" ? 80 : 443;
		}
		let protocol = window.location.protocol === "http:" ? 'ws' : 'wss';
		return `${protocol}://${window.location.hostname}:${port}/game`;
	}

	constructor() {
		this.callbackOnOpen = () => console.log("Connected");
		this.callbackOnMessage = (event) => console.log("Message from server", event.data);
		this.callbackOnClose = () => console.log("Closed");
	};

	init() {
		if (!this.socket) {
			this.socket = new WebSocket(WebSocketService.URL);
			this.socket.onopen = (event) => this.callbackOnOpen();
			this.socket.onclose = (event) => this.callbackOnClose(event.data);
			this.socket.onmessage = (event) => this.callbackOnMessage(event);
		}
	};

	onOpen(callback) {
		this.callbackOnOpen = callback;
	};

	onClose(callback) {
		this.callbackOnClose = callback;
	};

	onMessage(callback) {
		this.callbackOnMessage = callback;
	};

	send(data) {
		this.socket.send(data);
	}

}
