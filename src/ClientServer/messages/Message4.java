/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.messages;

import ClientServer.beans.Pair;
import ClientServer.beans.Pairs;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author amirouche
 */
public class Message4 extends Message {

    private List<Pair> list;
    int size;

    public Message4() {
        super(4);
        list = Pairs.getInstance().getListPairs();
        size = list.size();
    }

    public Message4(List<Pair> list) {
        super(4);
        this.list = list;
        this.size = list.size();
    }

    public List<Pair> getList() {
        return list;
    }

    public void setList(List<Pair> list) {
        this.list = list;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void writeMessage() {
        writeId(idMsg);
        writeInt(list.size());

        Iterator<Pair> it = list.iterator();
        while (it.hasNext()) {
            Pair next = it.next();
            writeInt(next.getPort());
            writeString(next.getUrl());
        }

    }

    @Override
    public void readMessage() {
        size = readInt();
        for (int i = 0; i < size; i++) {
            Pair pair = new Pair(readInt(), readString());
            list.add(pair);
        }
        bb.clear();
    }

    @Override
    public String toString() {
        return "Id : " + idMsg + " size : " + size + " list Pair : " + list;
    }

}
