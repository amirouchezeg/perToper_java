/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer;

import ClientServer.beans.Fragment;
import ClientServer.beans.MyFile;
import ClientServer.beans.Pairs;
import ClientServer.beans.SharedFolder;
import ClientServer.messages.Message;
import ClientServer.messages.Message1;
import ClientServer.messages.Message2;
import ClientServer.messages.Message3;
import ClientServer.messages.Message4;
import ClientServer.messages.Message5;
import ClientServer.messages.Message6;
import ClientServer.messages.Message7;
import ClientServer.messages.Message8;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author amirouche
 */
public class Node {

    public static int SIZE_ByteBuffer = 500000;
    protected ByteBuffer bb = ByteBuffer.allocate(SIZE_ByteBuffer);
    protected SocketChannel socketChannel;
    private boolean isConnected;
    protected String name = this.getClass().getSimpleName() + " : ";
    ReentrantLock lock = new ReentrantLock();

    protected boolean isConnected() {
        return isConnected;
    }

    protected void isConnected(boolean bool) {
        this.isConnected = bool;
    }

    protected void sendMessage1(String str) {
        Message message = new Message1(str);
        sendMessage(message);
    }

    protected void sendMessage2(int port) {
        Message message = new Message2(port);
        sendMessage(message);
    }

    protected void sendMessage3() {
        Message message = new Message3();
        sendMessage(message);
    }

    protected void sendMessage4() {
        Message message = new Message4(Pairs.getInstance().getListPairs());
        sendMessage(message);
    }

    protected void sendMessage5() {
        Message message = new Message5();
        sendMessage(message);
    }

    protected void sendMessage6() {
        Message message = new Message6(SharedFolder.getInstance().getListMyFiles());
        sendMessage(message);
    }

    protected void sendMessage7(Fragment fragment) {
        Message message = new Message7(fragment);
        sendMessage(message);
    }

    protected void sendMessage8(Fragment fragment) throws IOException {
        Message message = new Message8(fragment);
        sendMessage(message);
    }

    private void sendMessage(Message message) {
        try {
            lock.lock();
            message.writeMessage();
            bb = message.getBb();
            bb.flip();
            try {
                socketChannel.write(bb);
                System.out.println(name + "Send Message To -------> " + socketChannel.getRemoteAddress()
                        + "\n\t ::> " + message
                        + "\n\t To             ::> " + socketChannel.getRemoteAddress());

            } catch (Exception e) {
                System.err.println(name + "The Server is closed");
            }
            bb.clear();

            System.out.println("------------------------");
        } finally {
            lock.unlock();
        }
    }
}
