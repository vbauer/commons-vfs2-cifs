package com.github.vbauer.vfs2.provider.smb;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.GenericFileName;

/**
 * An SMB URI.  Adds a share name to the generic URI.
 *
 * @author Vladislav Bauer
 */

public class SmbFileName extends GenericFileName {

    public static final int DEFAULT_PORT = 139;


    private final String share;
    private final String domain;
    private String uriWithoutAuth;


    protected SmbFileName(
        final String scheme,
        final String hostName, final int port,
        final String userName, final String password,
        final String domain, final String share, final String path,
        final FileType type
    ) {
        super(scheme, hostName, port, DEFAULT_PORT, userName, password, path, type);
        this.share = share;
        this.domain = domain;
    }

    /**
     * Returns the share name.
     * @return share name
     */
    public String getShare() {
        return share;
    }

    /**
     * Builds the root URI for this file name.
     */
    @Override
    protected void appendRootUri(final StringBuilder buffer, final boolean addPassword) {
        super.appendRootUri(buffer, addPassword);
        buffer.append('/');
        buffer.append(share);
    }

    /**
     * put domain before username if both are set
     */
    @Override
    protected void appendCredentials(final StringBuilder buffer, final boolean addPassword) {
        final String domain = getDomain();
        final String userName = getUserName();

        if (domain != null && domain.length() != 0 && userName != null && userName.length() != 0) {
            buffer.append(domain);
            buffer.append("\\");
        }
        super.appendCredentials(buffer, addPassword);
    }

    /**
     * Factory method for creating name instances.
     */
    public FileName createName(final String path, final FileType type) {
        return new SmbFileName(
            getScheme(),
            getHostName(),
            getPort(),
            getUserName(),
            getPassword(),
            domain,
            share,
            path,
            type
        );
    }

    /**
     * Construct the path suitable for SmbFile when used with NtlmPasswordAuthentication
     * @return uri without auth
     * @throws org.apache.commons.vfs2.FileSystemException on error
     */
    public String getUriWithoutAuth() throws FileSystemException {
        if (uriWithoutAuth != null) {
            return uriWithoutAuth;
        }

        final StringBuilder sb = new StringBuilder(120);
        sb.append(getScheme());
        sb.append("://");
        sb.append(getHostName());
        if (getPort() != DEFAULT_PORT) {
            sb.append(":");
            sb.append(getPort());
        }
        sb.append("/");
        sb.append(getShare());
        sb.append(getPathDecoded());
        uriWithoutAuth = sb.toString();
        return uriWithoutAuth;
    }

    /**
     * returns the domain name
     * @return domain name
     */
    public String getDomain() {
        return domain;
    }

}
