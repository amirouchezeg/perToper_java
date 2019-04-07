package ClientServer;

import static ClientServer.Node.SIZE_ByteBuffer;
import ClientServer.messages.Message;
import ClientServer.messages.MessageFactory;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class RepeatNetwork implements Runnable {

    private ByteBuffer bb;
    private Client client;
    static final Charset charset = Charset.forName("UTF-8");

    public RepeatNetwork(Client client) {
        bb = ByteBuffer.allocateDirect(SIZE_ByteBuffer);
        this.client = client;
    }

    @Override
    public void run() {
        try {
            int n;
            while (client.isConnected() && (n = client.socketChannel.read(bb)) >= 0) {
                MessageFactory factory = new MessageFactory();
                bb.flip();
                int id = bb.get();
                Message message = factory.getMessage(id);
                if (message == null) {
                    bb.clear();
                    return;
                }
                message.setBb(bb);
                message.readMessage();
                System.out.println(client.name + "Recived Message From  <--------- " + client.socketChannel.getRemoteAddress()
                        + " \n\t " + message);
                Request request = new Request(message, client.socketChannel, client.name);
                request.response();
                bb.clear();
            }
            client.isConnected(false);
            System.out.println("Client leave ");
        } catch (IOException e) {
            client.isConnected(false);
            e.printStackTrace();
        }
    }

}
