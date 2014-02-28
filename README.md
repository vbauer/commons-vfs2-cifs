Introduction
============

Project "commons-vfs2-cifs" is a SMB/CIFS provider for Commons VFS.

Official Commons VFS site is here: http://commons.apache.org/proper/commons-vfs/


Requirements
============

Project "commons-vfs2-cifs" requires:
* Java 5 (Apache Commons VFS 2.0 uses the same Java version)
* JCIFS library (http://jcifs.samba.org)


Example
=======

```java
// Retrieve file system manager
final FileSystemManager fileManager = VFS.getManager();

// Configure authenticator
final FileSystemOptions fileSystemOptions = new FileSystemOptions();
final StaticUserAuthenticator userAuthenticator =
    new StaticUserAuthenticator(domain, login, password);

DefaultFileSystemConfigBuilder.getInstance()
    .setUserAuthenticator(fileSystemOptions, userAuthenticator);

// Resolve file object file from virtual file system
final String uri = "smb://fs/Documents";
final FileObject fileObject = fileManager.resolveFile(uri, fileSystemOptions);
```


Known Issues
============

To date, JCIFS has always tried NetBIOS broadcast lookups in favor of DNS which frequently resulted in a 6 second
delay  if the jcifs.resolveOrder property was not adjusted. This behavior has been changed to try  DNS before NetBIOS
broadcast lookups which should result in much less frequent delays when using default settings. To restore the old
behavior, simply set **jcifs.resolveOrder=LMHOSTS,BCAST,DNS**.


License
=======

```
Copyright 2014 Vladislav Bauer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
