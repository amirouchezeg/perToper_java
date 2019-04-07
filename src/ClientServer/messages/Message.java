/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer.messages;

import static ClientServer.Node.SIZE_ByteBuffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 *
 * @author amirouche
 */
public abstract class Message {

    final static Charset charset = Charset.forName("UTF-8");
    protected static int idMsg;
    protected ByteBuffer bb;

    public Message(ByteBuffer bb) {
        this.bb = bb;
        bb.flip();
        idMsg = bb.getInt();
    }

    public Message(int idMsg) {
        this.idMsg = idMsg;
        bb = ByteBuffer.allocate(SIZE_ByteBuffer);
    }

    public abstract void readMessage();

    public abstract void writeMessage();

    @Override
    public abstract String toString();

    public int getIdMsg() {
        return idMsg;
    }

    public void setIdMsg(int idMsg) {
        this.idMsg = idMsg;
    }

    public ByteBuffer getBb() {
        //don't make flip here
        return bb;
    }

    public void setBb(ByteBuffer bb) {
        this.bb = bb;
    }

    public void writeByteArray(byte[] bytes) {
        bb.put(bytes);
    }

    public byte[] readBytesArray(int size) {
        byte[] b = new byte[size];
        for (int i = 0; i < size; i++) {
            b[i] = bb.get();
        }
        return b;
    }

    public void writeId(int i) {
        bb.put((byte) i);
    }

    public void writeInt(int i) {
        bb.putInt(i);
    }

    public int readInt() {
        return bb.getInt();
    }

    public void writeDouble(double d) {
        bb.putDouble(d);
    }

    public double readDouble() {
        return bb.getDouble();
    }

    public void writeLong(Long d) {
        bb.putLong(d);
    }

    public long readLong() {
        return bb.getLong();
    }

    public void writeString(String s) {
        ByteBuffer bs = charset.encode(s);
        bb.putInt(bs.remaining());
        bb.put(bs);
    }

    public String readString() {
        int n = bb.getInt();
        int lim = bb.limit();
        bb.limit(bb.position() + n);
        String s = charset.decode(bb).toString();
        bb.limit(lim);
        return s;
    }
}
