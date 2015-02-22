package com.github.vbauer.vfs2.provider.smb;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticationData;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * A provider for SMB (Samba, Windows share) file systems.
 *
 * @author Vladislav Bauer
 */

public class SmbFileProvider extends AbstractOriginatingFileProvider {

    /*package*/ static final UserAuthenticationData.Type[] AUTHENTICATOR_TYPES = new UserAuthenticationData.Type[] {
        UserAuthenticationData.USERNAME,
        UserAuthenticationData.PASSWORD,
        UserAuthenticationData.DOMAIN,
    };

    /*package*/ static final Collection<Capability> CAPABILITIES = Collections.unmodifiableCollection(Arrays.asList(
        Capability.CREATE,
        Capability.DELETE,
        Capability.RENAME,
        Capability.GET_TYPE,
        Capability.GET_LAST_MODIFIED,
        Capability.LIST_CHILDREN,
        Capability.READ_CONTENT,
        Capability.URI,
        Capability.WRITE_CONTENT,
        Capability.APPEND_CONTENT,
        Capability.RANDOM_ACCESS_READ,
        Capability.RANDOM_ACCESS_WRITE
    ));


    public SmbFileProvider() {
        setFileNameParser(SmbFileNameParser.getInstance());
    }

    /**
     * Creates the filesystem.
     */
    @Override
    protected FileSystem doCreateFileSystem(
        final FileName name, final FileSystemOptions fileSystemOptions
    ) throws FileSystemException {
        return new SmbFileSystem(name, fileSystemOptions);
    }

    public Collection<Capability> getCapabilities() {
        return CAPABILITIES;
    }

}
