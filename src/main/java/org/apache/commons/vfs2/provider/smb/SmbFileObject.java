package org.apache.commons.vfs2.provider.smb;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.RandomAccessContent;
import org.apache.commons.vfs2.UserAuthenticationData;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileObject;
import org.apache.commons.vfs2.provider.UriParser;
import org.apache.commons.vfs2.util.RandomAccessMode;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

/**
 * A file in an SMB file system.
 */
public class SmbFileObject extends AbstractFileObject implements FileObject {

    private SmbFile file;


    protected SmbFileObject(final AbstractFileName name, final SmbFileSystem fileSystem) throws FileSystemException {
        super(name, fileSystem);
    }

    /**
     * Attaches this file object to its file resource.
     */
    @Override
    protected void doAttach() throws Exception {
        // Defer creation of the SmbFile to here
        if (file == null) {
            file = createSmbFile(getName());
        }
    }

    @Override
    protected void doDetach() throws Exception {
        // File closed through content-streams
        file = null;
    }

    private SmbFile createSmbFile(final FileName fileName) throws MalformedURLException, SmbException, FileSystemException {
        final SmbFileName smbFileName = (SmbFileName) fileName;
        final String path = smbFileName.getUriWithoutAuth();

		UserAuthenticationData authData = null;
		SmbFile file;
		NtlmPasswordAuthentication auth;
		try {
			authData = UserAuthenticatorUtils.authenticate(getFileSystem().getFileSystemOptions(), SmbFileProvider.AUTHENTICATOR_TYPES);

			auth = new NtlmPasswordAuthentication(
				UserAuthenticatorUtils.toString(
					UserAuthenticatorUtils.getData(
						authData,
						UserAuthenticationData.DOMAIN,
						UserAuthenticatorUtils.toChar(smbFileName.getDomain()))),
				UserAuthenticatorUtils.toString(
					UserAuthenticatorUtils.getData(
						authData,
						UserAuthenticationData.USERNAME,
						UserAuthenticatorUtils.toChar(smbFileName.getUserName()))),
				UserAuthenticatorUtils.toString(
					UserAuthenticatorUtils.getData(
						authData,
						UserAuthenticationData.PASSWORD,
						UserAuthenticatorUtils.toChar(smbFileName.getPassword()))));

			file = new SmbFile(path, auth);
		} finally {
			UserAuthenticatorUtils.cleanup(authData);
		}

		if (file.isDirectory() && !file.toString().endsWith("/")) {
			file = new SmbFile(path + "/", auth);
		}

		return file;
    }

    /**
     * Determines the type of the file, returns null if the file does not
     * exist.
     */
    @Override
    protected FileType doGetType() throws Exception {
        if (!file.exists()) {
            return FileType.IMAGINARY;
        } else if (file.isDirectory()) {
            return FileType.FOLDER;
        } else if (file.isFile()) {
            return FileType.FILE;
        }

        throw new FileSystemException("vfs.provider.smb/get-type.error", getName());
    }

    /**
     * Lists the children of the file.  Is only called if {@link #doGetType}
     * returns {@link FileType#FOLDER}.
     */
    @Override
    protected String[] doListChildren() throws Exception {
        return UriParser.encode(file.list());
    }

    /**
     * Determines if this file is hidden.
     */
    @Override
    protected boolean doIsHidden() throws Exception {
        return file.isHidden();
    }

    /**
     * Deletes the file.
     */
    @Override
    protected void doDelete() throws Exception {
        file.delete();
    }

    @Override
    protected void doRename(final FileObject newFile) throws Exception {
        file.renameTo(createSmbFile(newFile.getName()));
    }

    /**
     * Creates this file as a folder.
     */
    @Override
    protected void doCreateFolder() throws Exception {
        file.mkdir();
        file = createSmbFile(getName());
    }

    /**
     * Returns the size of the file content (in bytes).
     */
    @Override
    protected long doGetContentSize() throws Exception {
        return file.length();
    }

    /**
     * Returns the last modified time of this file.
     */
    @Override
    protected long doGetLastModifiedTime() throws Exception {
        return file.getLastModified();
    }

    /**
     * Creates an input stream to read the file content from.
     */

    @Override
    protected InputStream doGetInputStream() throws Exception {
        return new SmbFileInputStream(file);
    }

    /**
     * Creates an output stream to write the file content to.
     */
    @Override
    protected OutputStream doGetOutputStream(final boolean bAppend) throws Exception {
        return new SmbFileOutputStream(file, bAppend);
    }

    /**
     * random access
     */
    @Override
    protected RandomAccessContent doGetRandomAccessContent(final RandomAccessMode mode) throws Exception {
        return new SmbFileRandomAccessContent(file, mode);
    }

}
