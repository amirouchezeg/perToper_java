/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.beans;

import static ClientServer.beans.SharedFolder.FOLDER_NAME;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amirouche
 */
public class Fragment implements Comparable<Fragment> {

    private long position;
    private int size;
    private byte[] block;
    private MyFile myFile;

    public Fragment() {
    }

    public Fragment(long position, int size, byte[] block, MyFile myFile) {
        this.position = position;
        this.size = size;
        this.block = block;
        this.myFile = myFile;
    }

    public Fragment(long position, int size, byte[] block) {
        this.position = position;
        this.size = size;
        this.block = block;
    }

    public Fragment(long position, int size, MyFile myFile) {
        this.position = position;
        this.size = size;
        this.myFile = myFile;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getBlock() {
        return block;
    }

    public void setBlock(byte[] block) {
        this.block = block;
    }

    public MyFile getMyFile() {
        return myFile;
    }

    public void setMyFile(MyFile myFile) {
        this.myFile = myFile;
    }

    @Override
    public String toString() {
        return "Fragment : position = " + position + " |Size " + size + " |File: " + myFile + " ";
    }

    public void initializeFragmentBytes() {
        try {
            block = convertFragmentContentToBlob();
        } catch (IOException ex) {
            Logger.getLogger(Fragment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private byte[] convertFragmentContentToBlob() throws IOException {
        // create file object
        File file = new File(FOLDER_NAME + "/" + myFile.getName());
        byte[] result = new byte[(int) size];
        // initialize a byte array of size of the file
        byte[] fileContent = new byte[(int) file.length()];

        FileInputStream inputStream = null;
        try {
            // create an input stream pointing to the file
            inputStream = new FileInputStream(file);
            // read the contents of file into byte array
            inputStream.read(fileContent);
            //get only the fragment from position to position+size
            int j = 0;
            for (int i = (int) position; i < (int) position + size; i++) {
                result[j] = fileContent[i];
                j++;
            }
        } catch (IOException e) {
            throw new IOException("Unable to convert file to byte array. " + e.getMessage());
        } finally {
            // close input stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return result;
    }

    private void writeByte(byte[] bytes) {
        //if file exist
        //
        try {
            File file = new File(FOLDER_NAME + myFile.getName() + ".pap");
            // in file using OutputStream
            OutputStream os = new FileOutputStream(file);
            // Starts writing the bytes in it
            os.write(bytes);
            System.out.println("Successfully byte inserted");

            // Close the file
            os.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    @Override
    public int compareTo(Fragment o) {
        if (this.getPosition() > o.getPosition()) {
            return 1;
        } else if (this.getPosition() == o.getPosition()) {
            return 0;
        }
        return -1;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (int) (this.position ^ (this.position >>> 32));
        hash = 37 * hash + this.size;
        hash = 37 * hash + Objects.hashCode(this.myFile);
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
        final Fragment other = (Fragment) obj;
        if (this.position != other.position) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        if (!Objects.equals(this.myFile, other.myFile)) {
            return false;
        }
        return true;
    }

}
