package org.python.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import junit.framework.TestCase;

import org.jruby.ext.posix.util.Platform;

public class PySystemStateTest extends TestCase {

    public void testGetJarFileNameFromURL_null() throws Exception {
        assertNull(PySystemState.getJarFileNameFromURL(null));
    }

    public void testGetJarFileNameFromURL_plain_jar_url() throws Exception {
        String urlString = "file:/some_dir/some.jar";
        URL url = new URL(urlString);
        assertEquals("/some_dir/some.jar", PySystemState.getJarFileNameFromURL(url));
    }

    public void testGetJarFileNameFromURL_plain_jar_url_with_spaces() throws Exception {
        String urlString = "file:/home/huo/workspace/base/.metadata/.ivy-cache/Bison%20Schweiz%20AG/ExternalLibraries-scripting/artifacts/jython.jar";
        URL url = new URL(urlString);
        assertEquals("/home/huo/workspace/base/.metadata/.ivy-cache/Bison Schweiz AG/ExternalLibraries-scripting/artifacts/jython.jar",
                        PySystemState.getJarFileNameFromURL(url));
    }

    public void testGetJarFileNameFromURL_url_with_plus() throws Exception {
        String urlString = "file:/some+dir/some.jar";
        URL url = new URL(urlString);
        assertEquals("/some+dir/some.jar", PySystemState.getJarFileNameFromURL(url));
    }

    public void testGetJarFileNameFromURL_jboss5_windows_plain() throws Exception {
        // TODO:check protection domain URL
        if (Platform.IS_WINDOWS) {
            String file = "C:/some_dir/some.jar";
            // tests with jboss on windows gave URL's like this:
            assertJarFilenameURL_jboss5("C:/some_dir/some.jar", file);
        }
    }

    public void testGetJarFileNameFromURL_jboss5_windows_with_space() throws Exception {
        // TODO:check protection domain URL
        if (Platform.IS_WINDOWS) {
            String filename = "C:/some%20dir/some.jar";
            String expectedJarFilename = "C:/some dir/some.jar";
            assertJarFilenameURL_jboss5(expectedJarFilename, filename);
        }
    }

    public void testGetJarFileNameFromURL_jboss5_windows_with_plus() throws Exception {
        // TODO:check protection domain URL
        if (Platform.IS_WINDOWS) {
            String filename = "C:/some+dir/some.jar";
            String expectedJarFilename = "C:/some+dir/some.jar";
            assertJarFilenameURL_jboss5(expectedJarFilename, filename);
        }
    }

    public void testGetJarFileNameFromURL_jboss5_unix_plain() throws Exception {
        // TODO:check protection domain URL
        if (!Platform.IS_WINDOWS) {
            String file = "/some_dir/some.jar";
            // tests with jboss on linux gave URL's like this:
            assertJarFilenameURL_jboss5("/some_dir/some.jar", file);
        }
    }

    public void testGetJarFileNameFromURL_jboss5_unix_with_space() throws Exception {
        // TODO:check protection domain URL
        if (!Platform.IS_WINDOWS) {
            String filename = "/some%20dir/some.jar";
            String expectedJarFilename = "/some dir/some.jar";
            assertJarFilenameURL_jboss5(expectedJarFilename, filename);
        }
    }

    public void testGetJarFileNameFromURL_jboss5_unix_with_plus() throws Exception {
        // TODO:check protection domain URL
        if (!Platform.IS_WINDOWS) {
            String filename = "/some+dir/some.jar";
            String expectedJarFilename = "/some+dir/some.jar";
            assertJarFilenameURL_jboss5(expectedJarFilename, filename);
        }
    }

    public void testGetJarFileNameFromURL_jboss8_unix_plain() throws Exception {
        // TODO:check protection domain URL
        if (!Platform.IS_WINDOWS) {
            String file = "/some_dir/some.jar";
            // tests with jboss on linux gave URL's like this:
            assertJarFilenameURL_jboss8("/some_dir/some.jar", file);
        }
    }

    public void testGetJarFileNameFromURL_jboss8_unix_with_space() throws Exception {
        if (!Platform.IS_WINDOWS) {
            String filename = "/some%20dir/some.jar";
            String expectedJarFilename = "/some dir/some.jar";
            assertJarFilenameURL_jboss8(expectedJarFilename, filename);
        }
    }

    public void testGetJarFileNameFromURL_jboss8_unix_with_plus() throws Exception {
        if (!Platform.IS_WINDOWS) {
            String filename = "/some+dir/some.jar";
            String expectedJarFilename = "/some+dir/some.jar";
            assertJarFilenameURL_jboss8(expectedJarFilename, filename);
        }
    }

    private void assertJarFilenameURL_jboss8(String expectedJarFilename, String filename) throws MalformedURLException {
        final String protocol = "vfs";
        assertJarFilenameURL(expectedJarFilename, filename, protocol);
    }

    private void assertJarFilenameURL_jboss5(String expectedJarFilename, String filename) throws MalformedURLException {
        final String protocol = "vfszip";
        assertJarFilenameURL(expectedJarFilename, filename, protocol);
    }

    private void assertJarFilenameURL(String expectedJarFilename, String filename, final String protocol)
                    throws MalformedURLException {
        URL url = extractUrl(filename, protocol);
        assertEquals(protocol + ":" + filename, url.toString());
        assertEquals(expectedJarFilename, PySystemState.getJarFileNameFromURL(url));
    }

    private URL extractUrl(String file, String protocol) throws MalformedURLException {
        final String host = "";
        final int port = -1;
        URLStreamHandler handler = new TestJBossURLStreamHandler();
        URL url = new URL(protocol, host, port, file, handler);
        return url;
    }

    protected static class TestJBossURLStreamHandler extends URLStreamHandler {
        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            throw new RuntimeException("unexpected call to openConnection " + u.toString());
        }
    }
}
