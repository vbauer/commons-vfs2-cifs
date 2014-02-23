package org.apache.commons.vfs2.provider.smb;

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

/*package*/ class SmbFileRandomAccessContent extends AbstractRandomAccessContent {

    private final SmbRandomAccessFile raf;
    private final InputStream rafis;


    /*package*/ SmbFileRandomAccessContent(
        final SmbFile smbFile, final RandomAccessMode mode
    ) throws FileSystemException {
        super(mode);

        final StringBuilder modes = new StringBuilder(2);
        if (mode.requestRead()) {
            modes.append('r');
        }
        if (mode.requestWrite()) {
            modes.append('w');
        }

        try {
            raf = new SmbRandomAccessFile(smbFile, modes.toString());
            rafis = new SmbFileInputStream();
        } catch (MalformedURLException e) {
            throw new FileSystemException("vfs.provider/random-access-open-failed.error", smbFile, e);
        } catch (SmbException e) {
            throw new FileSystemException("vfs.provider/random-access-open-failed.error", smbFile, e);
        } catch (UnknownHostException e) {
            throw new FileSystemException("vfs.provider/random-access-open-failed.error", smbFile, e);
        }
    }

    @Override
    public long getFilePointer() throws IOException {
        return raf.getFilePointer();
    }

    @Override
    public void seek(final long pos) throws IOException {
        raf.seek(pos);
    }

    @Override
    public long length() throws IOException {
        return raf.length();
    }

    @Override
    public void close() throws IOException {
        raf.close();
    }

    @Override
    public byte readByte() throws IOException {
        return raf.readByte();
    }

    @Override
    public char readChar() throws IOException {
        return raf.readChar();
    }

    @Override
    public double readDouble() throws IOException {
        return raf.readDouble();
    }

    @Override
    public float readFloat() throws IOException {
        return raf.readFloat();
    }

    @Override
    public int readInt() throws IOException {
        return raf.readInt();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return raf.readUnsignedByte();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return raf.readUnsignedShort();
    }

    @Override
    public long readLong() throws IOException {
        return raf.readLong();
    }

    @Override
    public short readShort() throws IOException {
        return raf.readShort();
    }

    @Override
    public boolean readBoolean() throws IOException {
        return raf.readBoolean();
    }

    @Override
    public int skipBytes(final int n) throws IOException {
        return raf.skipBytes(n);
    }

    @Override
    public void readFully(final byte[] b) throws IOException {
        raf.readFully(b);
    }

    @Override
    public void readFully(final byte[] b, final int off, final int len) throws IOException {
        raf.readFully(b, off, len);
    }

    @Override
    public String readUTF() throws IOException {
        return raf.readUTF();
    }

    @Override
    public void writeDouble(final double v) throws IOException {
        raf.writeDouble(v);
    }

    @Override
    public void writeFloat(final float v) throws IOException {
        raf.writeFloat(v);
    }

    @Override
    public void write(final int b) throws IOException {
        raf.write(b);
    }

    @Override
    public void writeByte(final int v) throws IOException {
        raf.writeByte(v);
    }

    @Override
    public void writeChar(final int v) throws IOException {
        raf.writeChar(v);
    }

    @Override
    public void writeInt(final int v) throws IOException {
        raf.writeInt(v);
    }

    @Override
    public void writeShort(final int v) throws IOException {
        raf.writeShort(v);
    }

    @Override
    public void writeLong(final long v) throws IOException {
        raf.writeLong(v);
    }

    @Override
    public void writeBoolean(final boolean v) throws IOException {
        raf.writeBoolean(v);
    }

    @Override
    public void write(final byte[] b) throws IOException {
        raf.write(b);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        raf.write(b, off, len);
    }

    @Override
    public void writeBytes(final String s) throws IOException {
        raf.writeBytes(s);
    }

    @Override
    public void writeChars(final String s) throws IOException {
        raf.writeChars(s);
    }

    @Override
    public void writeUTF(final String str) throws IOException {
        raf.writeUTF(str);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return rafis;
    }


    /**
     * @author Vladislav Bauer
     */

    private class SmbFileInputStream extends InputStream {

        @Override
        public int read() throws IOException {
            return raf.readByte();
        }

        @Override
        public long skip(final long n) throws IOException {
            raf.seek(raf.getFilePointer() + n);
            return n;
        }

        @Override
        public void close() throws IOException {
            raf.close();
        }

        @Override
        public int read(final byte[] b) throws IOException {
            return raf.read(b);
        }

        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            return raf.read(b, off, len);
        }

        @Override
        public int available() throws IOException {
            final long available = raf.length() - raf.getFilePointer();
            if (available > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            return (int) available;
        }
    }

}
