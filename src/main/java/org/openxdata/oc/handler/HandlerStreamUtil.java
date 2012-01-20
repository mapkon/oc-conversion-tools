/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openxdata.oc.handler;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;

import org.openxdata.db.util.Persistent;
import org.openxdata.db.util.PersistentHelper;
import org.openxdata.model.ResponseHeader;

/**
 * Simply a utility class
 * @author kay
 */
class HandlerStreamUtil
{
    private ZOutputStream gzip;
    private DataOutputStream zdos;
    private DataInputStream in;
    private String serialiser;

    public HandlerStreamUtil(InputStream is, OutputStream os) throws IOException
    {
        gzip = new ZOutputStream(os, JZlib.Z_BEST_COMPRESSION);
        zdos = new DataOutputStream(gzip);
        in = new DataInputStream(is);
        serialiser = PersistentHelper.readUTF(in);

    }

    public void write(Persistent persistent) throws IOException
    {
        persistent.write(zdos);
    }

    public void read(Persistent persistent) throws IOException, InstantiationException, IllegalAccessException
    {
        persistent.read(in);
    }

    public void flush() throws IOException
    {
        zdos.flush();
        gzip.finish();
    }

    public String getSerialiser()
    {
        return serialiser;
    }

    public void writeSmallVector(Vector vector) throws IOException
    {
        PersistentHelper.write(vector, zdos);
    }

    public void writeBigVector(Vector vector) throws IOException
    {
        PersistentHelper.writeBig(vector, zdos);
    }

    public <E extends Persistent> Vector<E> readBigVector(Class<E> clazz) throws IOException, InstantiationException, IllegalAccessException{
return  PersistentHelper.readBig(in,clazz);
    }

    public <E extends Persistent> Vector<E> readSmallVector(Class<E> clazz) throws InstantiationException, IOException, IllegalAccessException{
            return PersistentHelper.read(in, clazz);
            
    }

    public void writeSucess() throws IOException
    {
        zdos.write(ResponseHeader.STATUS_SUCCESS);
    }

    public int readInt() throws IOException{
            
            return in.readInt();
    }

    public void writeUTF(String string ) throws IOException{
            PersistentHelper.writeUTF(zdos, string);
    }

        void writeByte(byte b) throws IOException {
                zdos.write(b);
        }

    void write(Hashtable<String, String> table) throws IOException {
        PersistentHelper.write(table, zdos);
    }
}
