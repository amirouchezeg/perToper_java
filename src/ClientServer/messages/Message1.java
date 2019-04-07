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
public class Message1 extends Message {

    private String text;

    public Message1(String text) {
        super(1);
        this.text = text;
    }

    public Message1() {
        super(1);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void writeMessage() {
        writeId(idMsg);
        writeString(text);
    }

    @Override
    public void readMessage() {
        text = readString();
        bb.clear();
    }

    @Override
    public String toString() {
        return "Id : " + idMsg + " Text : " + text;
    }

}
