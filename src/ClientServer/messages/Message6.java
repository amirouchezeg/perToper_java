/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.messages;

import ClientServer.beans.MyFile;
import ClientServer.beans.SharedFolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author amirouche
 */
public class Message6 extends Message {

    private List<MyFile> list;
    private int size;

    public Message6() {
        super(6);
        list = new ArrayList<>();
        //list = SharedFolder.getInstance().getListMyFiles();
        //size = list.size();
    }

    public Message6(List<MyFile> list) {
        super(6);
        this.list = list;
        size = list.size();
    }

    @Override
    public void writeMessage() {
        list = SharedFolder.getInstance().getListMyFiles();
        size = list.size();

        writeId(idMsg);
        writeInt(size);

        Iterator<MyFile> it = list.iterator();
        while (it.hasNext()) {
            MyFile next = it.next();
            writeString(next.getName());
            writeLong(next.getSize());
        }
    }

    @Override
    public void readMessage() {
        size = readInt();
        try {
            for (int i = 0; i < size - 1; i++) {
                String name = readString();
                MyFile file = new MyFile(name, readLong());
                list.add(file);
            }
        } catch (Exception e) {
        } finally {
            bb.clear();
        }
    }

    @Override
    public String toString() {
        return "Id : " + idMsg + " size : " + size + " list Files : " + list;
    }

    public List<MyFile> getList() {
        return list;
    }

    public void setList(List<MyFile> list) {
        this.list = list;
    }

}
