
import ClientServer.messages.Message;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class FirstConnection {

    SocketChannel socketChannel;
    private boolean isConnected;
    public Message message;
    ByteBuffer bb = ByteBuffer.allocateDirect(512);
    static final Charset charset = Charset.forName("UTF-8");

    public FirstConnection(String url, int port) throws IOException {
        SocketAddress sa = new InetSocketAddress(url, port);
        socketChannel = SocketChannel.open();
        socketChannel.connect(sa);
        isConnected = true;
        System.out.println("Client... started");

        readWelcomMsg();
        System.out.println("");
        send3();
        receive4();
    }

    private void send3() throws IOException {
        bb.put((byte) 3);
        bb.flip();
        socketChannel.write(bb);
        bb.clear();
    }

    private void receive4() throws IOException {
        //ByteBuffer bb = ByteBuffer.allocateDirect(512);
        socketChannel.read(bb);
        bb.flip();
        System.out.println("Id Message : " + bb.get());
        int size = bb.getInt();
        System.out.println("nombre de pairs  : " + size);
        //lier les paire
        for (int i = 0; i < size; i++) {
            System.out.println("int : " + bb.getInt());
            System.out.println("string : " + readString());
        }

    }

    public String readString() {
        int n = bb.getInt();
        int lim = bb.limit();
        bb.limit(bb.position() + n);
        String s = charset.decode(bb).toString();
        bb.limit(lim);
        return s;
    }

    private void readWelcomMsg() throws IOException {
        socketChannel.read(bb);
        readString(bb);
        socketChannel.read(bb);
        readString(bb);
    }

    private String readString(ByteBuffer bb) {
        bb.flip();
        CharBuffer charBuffer = charset.decode(bb);
        System.out.print(charBuffer.toString());
        bb.clear();
        return charBuffer.toString();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void isConnected(boolean bool) {
        this.isConnected = bool;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        FirstConnection client = new FirstConnection("prog-reseau-m1.lacl.fr", 5486);

    }
}
