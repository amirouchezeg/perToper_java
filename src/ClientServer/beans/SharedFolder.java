/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amirouche
 */
public class SharedFolder {

    public static final String EXTANTION = ".pap";
    public static final String ALL_EXTANTION = ".*";
    public static final String FOLDER_NAME = "Share";
    private static List<MyFile> listMyFiles = null;
    private static List<MyFile> listMyFilesPAP = null;
    private static SharedFolder singletonSharedFolder = null;

    private SharedFolder() {
        if ((new File(FOLDER_NAME)).mkdirs()) {
            System.out.println("Create Folder '" + FOLDER_NAME + "'");
        }
        initListMyFile();
    }

    public static SharedFolder getInstance() {
        if (singletonSharedFolder == null) {
            singletonSharedFolder = new SharedFolder();
        }
        return singletonSharedFolder;
    }

    public static List<MyFile> getListMyFiles() {
        return listMyFiles;
    }

    public static List<MyFile> getListMyFilesPAP() {
        return listMyFilesPAP;
    }

    public static void addFile(MyFile file) {
        listMyFiles.add(file);
    }

    public static void addFilePAP(MyFile file) {
        listMyFilesPAP.add(file);
    }

    private void initListMyFile() {
        List<MyFile> listMyFiles = new ArrayList<>();
        List<MyFile> listMyFilesPAP = new ArrayList<>();
        File listFiles[] = new File(FOLDER_NAME).listFiles();

        for (int i = 0; i < listFiles.length; i++) {
            MyFile myFile = new MyFile();
            File file = listFiles[i];
            myFile.setName((file.getName()));

            if (myFile.getExtensionByString(file.getName()).equals(EXTANTION)) {
                myFile.setSize((long) getOriginSizeFilePap(file.getName()));
                myFile.getFragments().addAll(myFile.getListFragments());
                listMyFilesPAP.add(myFile);
            } else {

                myFile.setSize((long) file.length());
                listMyFiles.add(myFile);
            }
        }

        this.listMyFiles = listMyFiles;
        this.listMyFilesPAP = listMyFilesPAP;

        //System.out.println("list >>>>>>>>>>>>>>>>>>>>> PAP " + listMyFilesPAP + "\n"+ "fragments>>>>:  " + listMyFilesPAP.get(0).getFragments());
    }

    public void addToFragments(Fragment fragment) {
        MyFile outher = new MyFile(fragment.getMyFile().getName() + EXTANTION,
                fragment.getMyFile().getSize());

        for (Iterator<MyFile> iterator = listMyFilesPAP.iterator(); iterator.hasNext();) {
            MyFile next = iterator.next();
            if (next.equals(outher)) {
                next.addFragment(fragment);
                checkFilePAP(next);
                return;
            }
        }
        outher.addFragment(fragment);
        listMyFilesPAP.add(outher);
        checkFilePAP(outher);
    }

    private long getOriginSizeFilePap(String name) {
        File file = new File(FOLDER_NAME + "/" + name);
        byte[] fileContentSize = new byte[Long.BYTES];
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            inputStream.read(fileContentSize);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SharedFolder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SharedFolder.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close input stream
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(SharedFolder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return bytesToLong(fileContentSize);
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    private void checkFilePAP(MyFile myFile) {
        if (getOriginSizeFilePap(myFile.getName()) == myFile.getSizeFragments()) {
            myFile.saveFromPAPtoFile();
            deleteFile(myFile);
            deleteFromListPAP(myFile);
            listMyFiles.add(myFile);
        }
    }

    private boolean deleteFile(MyFile myFile) {
        File file = new File(FOLDER_NAME + "/" + myFile.getName());
        return file.delete();
    }

    private void deleteFromListPAP(MyFile myFile) {
        for (Iterator<MyFile> iterator = listMyFilesPAP.iterator(); iterator.hasNext();) {
            MyFile next = iterator.next();
            if (next.equals(myFile)) {
                iterator.remove();
            }
        }
    }

}
