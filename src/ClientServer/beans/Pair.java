/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author amirouche
 */
public class Pair {

    private int port;
    private String url;
    List<MyFile> files = new ArrayList<>();

    public Pair(int port, String url) {
        this.port = port;
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MyFile> getFiles() {
        return files;
    }
    
    public List<String> getFilesByName(){
        return files.stream().map(f -> f.getName()).collect(Collectors.toList());
    }

    public void addAllFiles(List<MyFile> files) {
        this.files = files;
    }

    public void setFiles(List<MyFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return " Url : " + url + " (Port : " + port + ") : list Files " + files + "\n\t\t";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.port;
        hash = 19 * hash + Objects.hashCode(this.url);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair other = (Pair) obj;
        if (this.port != other.port) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        return true;
    }

}
