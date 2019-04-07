package ClientServer;

import ClientServer.beans.MyFile;
import ClientServer.beans.Pair;
import ClientServer.beans.Pairs;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;

public class Client extends Node {

    public Client(String url, int port) throws IOException {
        SocketAddress sa = new InetSocketAddress(url, port);
        socketChannel = SocketChannel.open();
        socketChannel.connect(sa);
        isConnected(true);
        System.out.println("CLIENT CONNECTED");
        System.out.println("------------------------");
        Pairs.getInstance().addOne(new Pair(socketChannel.socket().getPort(),
                socketChannel.socket().getInetAddress().getHostAddress()));
    }

    public List<MyFile> getFilsServer() {
        Pair pair = new Pair(socketChannel.socket().getPort(),
                socketChannel.socket().getInetAddress().getHostAddress());
        return Pairs.getInstance().getFilesOfPair(pair);
    }
}
