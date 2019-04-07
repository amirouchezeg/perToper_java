
import ClientServer.Server;
import ClientServer.beans.MyFile;
import ClientServer.messages.Message;
import ClientServer.messages.Message1;
import ClientServer.messages.Message2;
import ClientServer.messages.Message3;
import ClientServer.beans.Pair;
import ClientServer.beans.SharedFolder;
import ClientServer.messages.Message4;
import ClientServer.messages.Message6;
import ClientServer.messages.Message7;
import ClientServer.messages.Message8;
import ClientServer.messages.MessageFactory;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author amirouche
 */
public class Test {

    public static void main(String[] args) throws IOException {

        //System.out.println(buildNameToSave("wiki.txt.pap"));
        //System.out.println("ex : " + getExtensionByString("nam.pap"));
    }

    private static String buildNameToSave(String name) {
        return name.replaceFirst("[.][^.]+$", "");
    }

    void luncheServer() throws IOException {
        int port = 2002;
        Server server = new Server(port);
        System.out.println("Server *** opened");
        server.run();
        System.out.println("Server Closed");
    }

    private static String getExtensionByString(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i);
        }
        return extension;
    }

    public void testMessage() throws IOException {

        List<Pair> listPair = new ArrayList<Pair>();
        listPair.add(new Pair(200, "my_url1"));
        listPair.add(new Pair(201, "my_url2"));
        listPair.add(new Pair(202, "my_url3"));

        List<MyFile> listFiles = new ArrayList<>();
        listFiles.add(new MyFile("file1.txt", 10));
        listFiles.add(new MyFile("file2.txt", 15));

        //Test message 1--------------
        Message message1 = new Message1("my Text");
        message1.writeMessage();

        //Test message 2--------------
        Message message2 = new Message2(1310);
        message2.writeMessage();

        //Test message 3--------------
        Message message3 = new Message3();
        message3.writeMessage();

        //Test message 4--------------
        Message message4 = new Message4();
        message4.writeMessage();

        //Test message 6--------------
        Message message6 = new Message6(listFiles);
        message6.writeMessage();

        //Test message 7--------------
        MyFile file = new MyFile(573, "wiki.txt", 0, 573);
        Message message7 = new Message7();
        message7.writeMessage();

        //Test message 8--------------
        //MyFile file2 = new MyFile(416000, "algeria.jpg", 0, 208000 /*416000*/, true);
        MyFile file3 = new MyFile(573, "wiki.txt", 0, 286);
        Message message8 = new Message8();
        message8.writeMessage();

        ByteBuffer bb = message8.getBb();
        bb.flip();
        MessageFactory factory = new MessageFactory();
        Message message = factory.getMessage(bb.getInt());

        message.setBb(bb);
        message.readMessage();
        System.out.println(message);
    }
}
