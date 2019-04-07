/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.messages;

import ClientServer.beans.Fragment;
import ClientServer.beans.MyFile;
import java.lang.annotation.ElementType;

/**
 *
 * @author amirouche
 */
public class Message7 extends Message {

    private Fragment fragment;

    public Message7(Fragment fragment) {
        super(7);
        this.fragment = fragment;
    }

    public Message7() {
        super(7);
        fragment = new Fragment();
    }

    @Override
    public void writeMessage() {
        writeId(idMsg);
        writeString(fragment.getMyFile().getName());
        writeLong(fragment.getMyFile().getSize());
        writeLong(fragment.getPosition());
        writeInt(fragment.getSize());
    }

    @Override
    public void readMessage() {
        MyFile myFile = new MyFile(readString(), readLong());
        fragment.setMyFile(myFile);
        fragment.setPosition(readLong());
        fragment.setSize(readInt());
        bb.clear();
    }

    @Override
    public String toString() {
        return "Id : " + idMsg + " : " + fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

}
