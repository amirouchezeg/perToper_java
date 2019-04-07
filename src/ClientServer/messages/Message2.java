/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.messages;

import java.nio.ByteBuffer;

/**
 *
 * @author amirouche
 */
public class Message2 extends Message {

    private int port;

    public Message2() {
        super(2);
    }

    public Message2(int port) {
        super(2);
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void writeMessage() {
        writeId(idMsg);
        writeInt(port);
    }

    @Override
    public void readMessage() {
        port = readInt();
        bb.clear();
    }

    @Override
    public String toString() {
        return "Id : " + idMsg + " Port : " + port;
    }

}
