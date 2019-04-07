/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer;

import ClientServer.beans.Fragment;
import ClientServer.beans.MyFile;
import ClientServer.beans.Pair;
import ClientServer.beans.Pairs;
import ClientServer.beans.SharedFolder;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amirouche
 */
public class Luncher {

    public static void main(String[] args) {
        //todo : readMe
        //todo : get Pairs.forEachOne->getFiles and pairs -> chose one pair -> chose file ->get file
        //todo : get missed fragment and dowload it
        //todo : don't allow the bigs fragments
        //todo : disign interface
        //todo : design the UML for app
        SharedFolder.getInstance();//call this to initialize the shared Folder
        int port = 2002;
        Runnable serverRun = () -> {
            try {
                Thread.sleep(0);
                Server server = new Server(port);
                System.out.println("Server * opened");
                server.run();
                System.out.println("Server Closed");
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (InterruptedException ex) {
                Logger.getLogger(Luncher.class.getName()).log(Level.SEVERE, null, ex);
            }
        };

        Runnable clientRun = () -> {
            try {

                Client client = new Client("prog-reseau-m1.lacl.fr", 5486);

                RepeatNetwork repeatNetwork = new RepeatNetwork(client);
                Thread t1 = new Thread(repeatNetwork);
                t1.start();
                client.sendMessage1("Hello from Client 1");
                Thread.sleep(700);
                client.sendMessage2(port);
                Thread.sleep(700);
                client.sendMessage3();
                Thread.sleep(700);
                client.sendMessage5();
                Thread.sleep(700);
                
                //client.sendMessage7(new Fragment(0, 15000, new MyFile("1Table.png", 15873)));
                
                client.sendMessage7(new Fragment(15000, 873, new MyFile("1Table.png", 15873)));
                
                MyFile fi = new MyFile("naim.mp4", 1570024);
                //getAllFragment(fi);
                
                /*client.sendMessage1("Hello from Client 1");
                Thread.sleep(700);
                client.sendMessage2(port);
                Thread.sleep(700);
                client.sendMessage3();
                Thread.sleep(700);
                client.sendMessage5();
                Thread.sleep(700);*/
 /*
                Thread.sleep(700);
                client.sendMessage7(new Fragment(0, 1000, new MyFile(".DS_Store", 6174)));
                Thread.sleep(700);
                client.sendMessage7(new Fragment(1000, 1000, new MyFile(".DS_Store", 6174)));
                Thread.sleep(700);
                client.sendMessage7(new Fragment(2000, 1000, new MyFile(".DS_Store", 6174)));
                Thread.sleep(700);
                client.sendMessage7(new Fragment(3000, 1000, new MyFile(".DS_Store", 6174)));
                Thread.sleep(700);
                client.sendMessage7(new Fragment(4000, 1000, new MyFile(".DS_Store", 6174)));
                Thread.sleep(700);
                client.sendMessage7(new Fragment(5000, 1000, new MyFile(".DS_Store", 6174)));
                Thread.sleep(700);
                client.sendMessage7(new Fragment(6000, 174, new MyFile(".DS_Store", 6174)));
                 */
 /*
                Thread.sleep(700);
                //client.sendMessage7(new Fragment(0, 1000, new MyFile("vedaghediti.c", 1815)));
                Thread.sleep(700);
                //client.sendMessage7(new Fragment(1800, 15, new MyFile("vedaghediti.c", 1815)));
                Thread.sleep(700);
                client.sendMessage7(new Fragment(1000, 800, new MyFile("vedaghediti.c", 1815)));
                 */

            } catch (IOException ex) {
                Logger.getLogger(Luncher.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Luncher.class.getName()).log(Level.SEVERE, null, ex);
            }
        };

        Thread threadServer = new Thread(serverRun);
        Thread threadClient = new Thread(clientRun);

        threadClient.start();
        threadServer.start();
        
    }
    
    public static void getAllFragment(MyFile fi) throws IOException, InterruptedException{
            int from = 0, to = 15000; boolean until = true;
                if(fi.getSize()<15000){
                    to = (int) fi.getSize();
                }
                while(until){
                    for (Pair pair : Pairs.getInstance().getListPairs()) {
                        if(pair.getFiles().contains(fi)){
                            System.out.println("from "+from+" to "+to );
                            Client client = new Client(pair.getUrl(), pair.getPort());
                            RepeatNetwork repeatNetwork2 = new RepeatNetwork(client);
                            Thread t2 = new Thread(repeatNetwork2);
                            t2.start();
                            Fragment f = new Fragment(from, to, fi);
                            if(fi.getSize()<15000){
                                fi.getListFragments().add(f);
                                client.sendMessage7(f);
                                until = false;
                                break;
                            }
                            from+=to;
                            to = ((int) fi.getSize()-from>15000)? 15000: (int) fi.getSize()%15000;
                            System.out.println("from + to "+(from+to) +"%%"+fi.getSize()%15000 );
                            if(to!=15000){
                                f = new Fragment(from, to, fi);
                                fi.getListFragments().add(f);
                                client.sendMessage7(f);
                                System.out.println("false ");
                                until = false;
                                break;
                            }else{
                                fi.getListFragments().add(f);
                                client.sendMessage7(f);
                            }
                            Thread.sleep(700);
                        }
                        if(fi.getSize()<15000 || to!=15000){
                            until = false;
                            break;
                        }
                    }
                }
                int size=fi.getFragments().stream().mapToInt(f->f.getSize()).sum();
                System.out.println("size "+size);
        }
}