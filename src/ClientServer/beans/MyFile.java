/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.beans;

import static ClientServer.beans.SharedFolder.EXTANTION;
import static ClientServer.beans.SharedFolder.FOLDER_NAME;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * size (long),numberFragment(long),
 * [position(long),sizeFramgnet(long),block(byte:sizeFramgnet)]*
 *
 * @author amirouche
 */
public class MyFile {

    private long size;
    private String name;
    private long position;
    private int sizeFragment;//todo: delete this attribuet
    //private byte[] block;
    private List<Fragment> fragments = new ArrayList<>();

    public MyFile() {
    }

    public MyFile(String name, long size) {
        this.size = size;
        this.name = name;
    }

    public MyFile(long size, String name, long position, int sizeFragment) {
        this.size = size;
        this.name = name;
        this.position = position;
        this.sizeFragment = sizeFragment;
    }

    /*
    public void initializeFileBytes() throws IOException {
        block = convertFileContentToBlob(name);
    }
     */
    public byte[] getAllFileAsBytes() throws IOException {
        return readFileContentToBlob(name);
    }

    /*
    public void saveBytesToFile(byte[] block) {
        if (position == 0 && size == sizeFragment) {
            writeByte(block);
        }

        this.block = block;
    }
     */
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public int getSizeFragment() {
        return sizeFragment;
    }

    public void setSizeFragment(int sizeFragment) {
        this.sizeFragment = sizeFragment;
    }

    @Override
    public String toString() {
        if (sizeFragment != 0) {
            return " [Name : " + name + " (Size : " + size
                    + "| Position : " + position + "| Size Fragment : " + sizeFragment + ")]";
        }
        return "[ Name : " + name + " (Size : " + size + ") ]";

    }

    private byte[] readFileContentToBlob(String fileName) throws IOException {
        File file = new File(FOLDER_NAME + "/" + fileName);
        byte[] fileContent = new byte[(int) sizeFragment /*file.length()*/];

        FileInputStream inputStream = null;
        try {
            // create an input stream pointing to the file
            inputStream = new FileInputStream(file);
            // read the contents of file into byte array
            inputStream.read(fileContent);

        } catch (IOException e) {
            throw new IOException("Unable to convert file to byte array. " + e.getMessage());
        } finally {
            // close input stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return fileContent;
    }

    public long getSizeFragments() {
        return this.fragments.stream().mapToLong(f -> f.getSize()).sum();
    }

    /**
     * Method which write the bytes into a file
     *
     * @param bytes
     */
    private void writeByte() {
        Collections.sort(fragments);
        int sizeFileContent = (int) /*size fragment */ getSizeFragments()
                +/*size of file*/ Long.BYTES
                +/*how much of fragments*/ Long.BYTES
                + fragments.size() * ( /*position fragment*/Long.BYTES +/*size fragment*/ Long.BYTES);
        byte[] fileContent = new byte[sizeFileContent];

        //add size of origin file
        byte[] bytesSize = SharedFolder.longToBytes(size);
        System.arraycopy(bytesSize, 0, fileContent, 0, Long.BYTES);
        int j = Long.BYTES;

        //add how much of fragment
        byte[] bytesHowMuchF = SharedFolder.longToBytes(fragments.size());
        System.arraycopy(bytesHowMuchF, 0, fileContent, j, Long.BYTES);
        j += Long.BYTES;

        for (Iterator<Fragment> iterator = fragments.iterator(); iterator.hasNext();) {
            Fragment next = iterator.next();

            //add position of fragment
            byte[] bytesPositionFragment = SharedFolder.longToBytes(next.getPosition());//convert position to byte[]
            System.arraycopy(bytesPositionFragment, 0, fileContent, j, bytesPositionFragment.length);
            j += bytesPositionFragment.length;

            //add size of fragment
            byte[] bytesSizeFragment = SharedFolder.longToBytes(next.getSize());//convert Size to byte[] (make it in Long)
            System.arraycopy(bytesSizeFragment, 0, fileContent, j, bytesSizeFragment.length);
            j += bytesSizeFragment.length;

            //add byte (block) of fragment
            byte[] myblock = next.getBlock();
            System.arraycopy(myblock, 0, fileContent, j, myblock.length);
            j += myblock.length;
        }

        writeByteToFile(fileContent, this.getName());

    }

    public List<Fragment> getListFragments() {
        List<Fragment> fragments = new ArrayList<>();
        File file = new File(FOLDER_NAME + "/" + this.getName());
        byte[] fileContent = new byte[(int) file.length()];

        byte[] bytesLong = new byte[Long.BYTES];
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            inputStream.read(fileContent);

            int j = Long.BYTES;//because we already read the size file
            //read how mush of fragment
            System.arraycopy(fileContent, j, bytesLong, 0, Long.BYTES);
            long countFragment = SharedFolder.bytesToLong(bytesLong);
            j += Long.BYTES;
            for (int i = 0; i < countFragment; i++) {
                //position
                System.arraycopy(fileContent, j, bytesLong, 0, Long.BYTES);
                long position = SharedFolder.bytesToLong(bytesLong);
                j += Long.BYTES;

                //size Fragment
                System.arraycopy(fileContent, j, bytesLong, 0, Long.BYTES);
                long sizeFragment = SharedFolder.bytesToLong(bytesLong);
                j += Long.BYTES;

                //block
                byte[] bytesBlock = new byte[(int) sizeFragment];
                System.arraycopy(fileContent, j, bytesBlock, 0, (int) sizeFragment);
                j += (int) sizeFragment;
                fragments.add(new Fragment(position, (int) sizeFragment, bytesBlock, this));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close input stream
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return fragments;
    }

    public void saveFromPAPtoFile() {

        File file = new File(FOLDER_NAME + "/" + this.getName());
        byte[] fileContent = new byte[(int) file.length()];
        int fileSize = fragments.stream().mapToInt(f -> f.getBlock().length).sum();
        byte[] result = new byte[fileSize];
        //FileInputStream inputStream = null;
        //inputStream = new FileInputStream(file);
        //inputStream.read(fileContent);
        int j = 0;
        for (Iterator<Fragment> iterator = fragments.iterator(); iterator.hasNext();) {
            Fragment next = iterator.next();
            System.arraycopy(next.getBlock(), 0, result, j, next.getSize());
            j += next.getSize();
        }

        this.writeByteToFile(result, buildNameToSave(this.getName()));
        //deleteFile(myFile);
        //return bytesToLong(fileContentSize);
    }

    /**
     * Method which write the bytes into a file
     *
     * @param bytes
     */
    private void writeByteToFile(byte[] fileContent, String fileName) {

        try {
            File file = new File(FOLDER_NAME + "/" + fileName);
            OutputStream os = new FileOutputStream(file);
            // Starts writing the bytes in it
            os.write(fileContent);
            os.close();
            System.out.println(fragments.size() + "  Successfully byte inserted"
                    + "  " + file.getAbsolutePath());

            // Close the file
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyFile.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(MyFile.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Fragment getRandomFragment() {
        return new Fragment(0, (int) size / 2, this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (int) (this.size ^ (this.size >>> 32));
        hash = 61 * hash + Objects.hashCode(this.name);
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
        final MyFile other = (MyFile) obj;
        if (this.size != other.size) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    /**
     * Add fragment to list fragments AND add block bytes to file
     *
     * @param fragment
     */
    void addFragment(Fragment fragment) {
        //add extenstion PAP to compare with fragment
        String name = fragment.getMyFile().getName() + EXTANTION;
        fragment.getMyFile().setName(name);

        //to not dublicate when read from file PAP
        if (!fragments.contains(fragment)) {
            fragments.add(fragment);
            writeByte();
        }
    }

    /**
     * -Not used. this method is to check the defrent fragment if their equal
     * then collect or delete dublicated
     *
     * @return
     */
    private List<Fragment> checkFragment() {
        List<Fragment> result = new ArrayList<>();

        for (int i = 0; i < fragments.size() - 1; i++) {
            Fragment current = fragments.get(i);
            Fragment next = fragments.get(i + 1);
            if (current.getPosition() + current.getSize() < next.getPosition()) {
                result.add(current);
            } else if (current.getPosition() + current.getSize() == next.getPosition()) {
                //result.add(collect(current,next));
            }
        }
        return result;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    private String buildNameToSave(String name) {
        String result = getNameWithoutExtension(name);
        for (Iterator<MyFile> iterator = SharedFolder.getListMyFiles().iterator(); iterator.hasNext();) {
            MyFile next = iterator.next();
            if (next.getName().equals(result)) {
                //todo : don't make "1" it shold be denamicely
                return getNameWithoutExtension(result) + "1" + getExtensionByString(result);
            }
        }
        return result;
    }

    public String getNameWithoutExtension(String name) {
        return name.replaceFirst("[.][^.]+$", "");
    }

    public String getExtensionByString(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i);
        }
        return extension;
    }

}
