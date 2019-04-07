/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer;

import ClientServer.beans.Pair;
import ClientServer.beans.Pairs;
import ClientServer.beans.SharedFolder;
import ClientServer.messages.Message;
import ClientServer.messages.Message1;
import ClientServer.messages.Message2;
import ClientServer.messages.Message4;
import ClientServer.messages.Message6;
import ClientServer.messages.Message7;
import ClientServer.messages.Message8;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author amirouche
 */
public class Request {

    private Message messageRequest;
    private Message messageResponse;
    private SocketChannel socketChannel;
    private String name;

    public Request(Message messageRequest, SocketChannel socketChannel, String name) {
        this.messageRequest = messageRequest;
        this.socketChannel = socketChannel;
        this.name = name;

        //if (name.equals("Server : ")) {//Add the paire to the list if not exist
        //Pairs.getInstance().addOne(new Pair(socketChannel.socket().getLocalPort(), socketChannel.socket().getLocalSocketAddress().toString()));
        //}
    }

    public void response() throws IOException {

        switch (messageRequest.getIdMsg()) {
            case 1:
                Message1 message1 = (Message1) messageRequest;
                break;
            case 2:
                Message2 message2 = (Message2) messageRequest;
                Pairs.getInstance().addOne(new Pair(message2.getPort(),
                        socketChannel.socket().getInetAddress().getHostAddress()));
                break;
            case 3:
                messageResponse = new Message4();
                sendTheResponse();
                break;
            case 4:
                Message4 message4 = (Message4) messageRequest;
                Pairs.getInstance().addAll(message4.getList());
                break;
            case 5:
                messageResponse = new Message6();
                sendTheResponse();
                break;
            case 6:
                Message6 message6 = (Message6) messageRequest;
                message6.getList();

                Pairs.getInstance().addFilsToPair(socketChannel.socket().getInetAddress().getHostAddress(),
                        socketChannel.socket().getPort(), message6.getList());
                message6.getList();
                break;
            case 7:
                Message7 message7 = (Message7) messageRequest;
                messageResponse = new Message8(message7);
                sendTheResponse();
                break;
            case 8:
                Message8 message8 = (Message8) messageRequest;
                SharedFolder.getInstance().addToFragments(message8.getFragment());

                break;
            default:
                System.out.println("--------------------recive OUTHER Message---------------------");
        }

    }

    private void sendTheResponse() throws IOException {
        messageResponse.writeMessage();
        messageResponse.getBb().flip();
        System.out.println(name + "Send Response to -----> " + socketChannel.getRemoteAddress().toString()
                + " \n\t " + messageResponse + ""
                + "\n-----------------------");
        socketChannel.write(messageResponse.getBb());
    }

}
