package com.github.vbauer.vfs2.provider.smb;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbRandomAccessFile;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.provider.AbstractRandomAccessContent;
import org.apache.commons.vfs2.util.RandomAccessMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

/**
 * RandomAccess for smb files.
 *
 * @author Vladislav Bauer
 */

public class SmbFileRandomAccessContent extends AbstractRandomAccessContent {

    public static final char MODE_READ = 'r';
    public static final char MODE_WRITE = 'w';
    
    private static final String ERROR_OPEN_FAILED = "vfs.provider/random-access-open-failed.error";


    private final SmbRandomAccessFile raf;
    private final InputStream rafis;


    public SmbFileRandomAccessContent(final SmbFile smbFile, final RandomAccessMode mode) throws FileSystemException {
        super(mode);

        final StringBuilder modes = new StringBuilder(2);
        if (mode.requestRead()) {
            modes.append(MODE_READ);
        }
        if (mode.requestWrite()) {
            modes.append(MODE_WRITE);
        }

        try {
            raf = new SmbRandomAccessFile(smbFile, modes.toString());
            rafis = new SmbFileInputStream(raf);
        } catch (final MalformedURLException ex) {
            throw new FileSystemException(ERROR_OPEN_FAILED, smbFile, ex);
        } catch (final SmbException ex) {
            throw new FileSystemException(ERROR_OPEN_FAILED, smbFile, ex);
        } catch (final UnknownHostException ex) {
            throw new FileSystemException(ERROR_OPEN_FAILED, smbFile, ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getFilePointer() throws IOException {
        return raf.getFilePointer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void seek(final long pos) throws IOException {
        raf.seek(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long length() throws IOException {
        return raf.length();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        raf.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte readByte() throws IOException {
        return raf.readByte();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char readChar() throws IOException {
        return raf.readChar();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double readDouble() throws IOException {
        return raf.readDouble();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float readFloat() throws IOException {
        return raf.readFloat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readInt() throws IOException {
        return raf.readInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readUnsignedByte() throws IOException {
        return raf.readUnsignedByte();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readUnsignedShort() throws IOException {
        return raf.readUnsignedShort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long readLong() throws IOException {
        return raf.readLong();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short readShort() throws IOException {
        return raf.readShort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean readBoolean() throws IOException {
        return raf.readBoolean();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int skipBytes(final int n) throws IOException {
        return raf.skipBytes(n);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readFully(final byte[] b) throws IOException {
        raf.readFully(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readFully(final byte[] b, final int off, final int len) throws IOException {
        raf.readFully(b, off, len);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String readUTF() throws IOException {
        return raf.readUTF();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return rafis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeDouble(final double v) throws IOException {
        raf.writeDouble(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFloat(final float v) throws IOException {
        raf.writeFloat(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final int b) throws IOException {
        raf.write(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeByte(final int v) throws IOException {
        raf.writeByte(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeChar(final int v) throws IOException {
        raf.writeChar(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeInt(final int v) throws IOException {
        raf.writeInt(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeShort(final int v) throws IOException {
        raf.writeShort(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeLong(final long v) throws IOException {
        raf.writeLong(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeBoolean(final boolean v) throws IOException {
        raf.writeBoolean(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final byte[] b) throws IOException {
        raf.write(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        raf.write(b, off, len);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeBytes(final String s) throws IOException {
        raf.writeBytes(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeChars(final String s) throws IOException {
        raf.writeChars(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeUTF(final String str) throws IOException {
        raf.writeUTF(str);
    }

}
