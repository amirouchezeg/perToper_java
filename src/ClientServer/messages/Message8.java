/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.messages;

import ClientServer.beans.Fragment;
import ClientServer.beans.MyFile;
import java.io.IOException;

/**
 *
 * @author amirouche
 */
public class Message8 extends Message {

    private Fragment fragment;

    public Message8(Fragment fragment) throws IOException {
        super(8);
        this.fragment = fragment;
        //file.initializeFileBytes();
    }

    public Message8() {
        super(8);
        fragment = new Fragment();
    }

    public Message8(Message7 message7) {
        super(8);
        fragment = message7.getFragment();
        fragment.initializeFragmentBytes();
    }

    @Override
    public void writeMessage() {
        writeId(idMsg);
        writeString(fragment.getMyFile().getName());
        writeLong(fragment.getMyFile().getSize());
        writeLong(fragment.getPosition());
        writeInt(fragment.getSize());
        writeByteArray(fragment.getBlock());
    }

    @Override
    public void readMessage() {
        MyFile myFile = new MyFile(readString(), readLong());
        fragment.setMyFile(myFile);
        fragment.setPosition(readLong());
        int size = readInt();
        fragment.setSize(size);
        fragment.setBlock(readBytesArray(size));
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
