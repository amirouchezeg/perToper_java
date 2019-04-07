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
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Node {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private List<SocketChannel> clients;
    private int nbrClient = 0;
    private ReentrantLock lock;

    public Server(int port) throws IOException {
        //name = "Server : ";
        serverSocketChannel = ServerSocketChannel.open();
        SocketAddress socketAddress = new InetSocketAddress(port);
        serverSocketChannel.bind(socketAddress);
        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        bb.order(ByteOrder.BIG_ENDIAN);// optional, the initial order of a byte buffer is always BIG_ENDIAN.
        clients = new ArrayList<>();
        lock = new ReentrantLock();
    }

    private void accept() throws IOException {
        socketChannel = serverSocketChannel.accept();
        System.out.println(name + "Client accepted with @= " + socketChannel.getRemoteAddress()
                + "\n\t Number of Client : " + ++nbrClient);
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

    }

    private void repeat(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isReadable()) {

            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            int n;

            if ((n = socketChannel.read(bb)) < 0) {
                System.out.println(name + "Client Leave");
                System.out.println(name + "number of Client : " + --nbrClient);
                selectionKey.cancel();
                socketChannel.close();
                return;
            }

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
        }
    }

    public void run() throws IOException {
        while (true) {
            selector.select();
            for (SelectionKey selectionKey : selector.selectedKeys()) {
                if (selectionKey.isAcceptable()) {
                    accept();
                } else if (selectionKey.isReadable()) {
                    repeat(selectionKey);
                    //System.out.println("RRRRRRRRRRRRrrrrrrrrrrrrrrrrRRRRRRRRRRRRRR");
                }
            }
            selector.selectedKeys().clear();

        }
    }

}
