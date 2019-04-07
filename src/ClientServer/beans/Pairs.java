/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author amirouche
 */
public class Pairs {

    private static List<Pair> listPairs = null;
    private static Pairs singletonPairs = null;

    private Pairs() {
        listPairs = new ArrayList<>();
    }

    public static Pairs getInstance() {
        if (singletonPairs == null) {
            singletonPairs = new Pairs();
        }
        return singletonPairs;
    }

    public List<Pair> getListPairs() {
        return listPairs;
    }
    
    public Pair getPair(){
        return listPairs.stream().findAny().get();
    }
    
    public List<MyFile> getAllFiles(){
        return listPairs.stream().flatMap(p -> p.getFiles().stream()).collect(Collectors.toList());
    }
    
    public MyFile getFile(){
        return getAllFiles().stream().findAny().get();
    }

    /**
     * To add to list we should use this function
     *
     * @param pairs list of pairs
     */
    public void addAll(List<Pair> pairs) {
        for (Iterator<Pair> iterator = pairs.iterator(); iterator.hasNext();) {
            Pair pair = iterator.next();
            if (!isExist(pair)) {
                listPairs.add(pair);
            }
            /*if (listPairs.contains(pair)) {//we can use this too

            }*/
        }
    }

    public void addOne(Pair pair) {
        if (!isExist(pair)) {
            listPairs.add(pair);
        }
    }

    private boolean isExist(Pair pair) {
        for (Iterator<Pair> iterator = listPairs.iterator(); iterator.hasNext();) {
            Pair next = iterator.next();
            if (next.equals(pair)) {
                return true;

            }
        }
        return false;
    }

    public void addFilsToPair(String url, int port, List<MyFile> list) {
        Pair pair = new Pair(port, url);
        for (Iterator<Pair> iterator = listPairs.iterator(); iterator.hasNext();) {
            Pair next = iterator.next();
            if (next.equals(pair)) {
                next.addAllFiles(list);
                return;
            }
        }
    }

    @Override
    public String toString() {
        return listPairs.toString();
    }

    public List<MyFile> getFilesOfPair(Pair pair) {
        for (Iterator<Pair> iterator = listPairs.iterator(); iterator.hasNext();) {
            Pair next = iterator.next();
            if (next.equals(pair)) {
                return next.getFiles();
            }
        }
        return null;
    }
    
}
