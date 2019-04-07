/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.messages;

/**
 *
 * @author amirouche
 */
public class MessageFactory {

    public Message getMessage(int type) {
        switch (type) {
            case 1:
                return new Message1();
            case 2:
                return new Message2();
            case 3:
                return new Message3();
            case 4:
                return new Message4();
            case 5:
                return new Message5();
            case 6:
                return new Message6();
            case 7:
                return new Message7();
            case 8:
                return new Message8();
            default:
                return null;
        }
    }
}
