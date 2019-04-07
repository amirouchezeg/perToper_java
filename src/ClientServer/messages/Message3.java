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
public class Message3 extends Message {

    public Message3() {
        super(3);
    }

    @Override
    public void writeMessage() {
        writeId(idMsg);
    }

    @Override
    public void readMessage() {
    }

    @Override
    public String toString() {
        return "Id : " + idMsg;
    }

}
