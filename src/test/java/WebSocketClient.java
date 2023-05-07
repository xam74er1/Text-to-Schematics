import jakarta.websocket.*;

import java.net.URI;

@ClientEndpoint
public class WebSocketClient {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to server!");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    public static void main(String[] args) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = URI.create("wss://26563fbb41388c09b0.gradio.live/queue/join");
            container.connectToServer(WebSocketClient.class, uri);

            // Send a message to the server
            WebSocketClient client = new WebSocketClient();
            client.sendMessage("Hello, server!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
