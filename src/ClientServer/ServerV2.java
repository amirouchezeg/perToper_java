package ClientServer;

import ClientServer.messages.Message;
import ClientServer.messages.MessageFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerV2 extends Node {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private List<SocketChannel> clients;
    private int nbrClient = 0;
    private ReentrantLock lock;
    ExecutorService executor = Executors.newFixedThreadPool(20);

    public ServerV2(int port) throws IOException {
        //name = "Server : ";
        serverSocketChannel = ServerSocketChannel.open();
        SocketAddress socketAddress = new InetSocketAddress(port);
        serverSocketChannel.bind(socketAddress);
        bb.order(ByteOrder.BIG_ENDIAN);// optional, the initial order of a byte buffer is always BIG_ENDIAN.
        clients = new ArrayList<>();
        lock = new ReentrantLock();
    }

    private void accept() throws IOException {
        socketChannel = serverSocketChannel.accept();
        System.out.println(name + "Client accepted with @= " + socketChannel.getRemoteAddress()
                + "\n\t Number of Client : " + ++nbrClient);
        while (true) {
            repeat();
        }

    }

    private void repeat() throws IOException {

        int n;
        if ((n = socketChannel.read(bb)) < 0) {
            System.out.println(name + "Client Leave");
            System.out.println(name + "Number of Client : " + --nbrClient);
            socketChannel.close();
            return;
        }
        executor.submit(() -> {

            lock.lock();
            try {
                bb.flip();
                MessageFactory factory = new MessageFactory();
                int idMessage = bb.get();
                Message message = factory.getMessage(idMessage);
                message.setBb(bb);
                message.readMessage();

                System.out.println(name + "Recived Message From  <--------- "
                        + socketChannel.getRemoteAddress()
                        + " \n\t " + message);
                Request request = new Request(message, socketChannel, name);
                request.response();
                bb.clear();
            } catch (IOException ex) {
                Logger.getLogger(ServerV2.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                lock.unlock();
            }
        });

    }

    public void run() throws IOException {
        accept();
    }

}
