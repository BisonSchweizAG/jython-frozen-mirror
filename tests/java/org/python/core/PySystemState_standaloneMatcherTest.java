package org.python.core;

import static org.python.core.PySystemState.STANDALONE_PATTERN;

import junit.framework.TestCase;

public class PySystemState_standaloneMatcherTest extends TestCase {

    public void testWildflyUrl() {
        String urlpath;

        // new url with version
        urlpath = "anyRoot/wildfly-18.0.0.Final/standalone/deployments/BisonProcess.ear/lib/jython-2.5.3.3.jar/Lib/os.py";
        assertTrue(STANDALONE_PATTERN.matcher(urlpath).find());

        // old url with version
        urlpath = "anyRoot/wildfly-18.0.0.Final/standalone/deployments/BisonProcess.ear/lib/jython-2.5.3.3.jar!/Lib/os.py";
        assertTrue(STANDALONE_PATTERN.matcher(urlpath).find());

        // new url without verson
        urlpath = "anyRoot/wildfly-18.0.0.Final/standalone/deployments/BisonProcess.ear/lib/jython.jar/Lib/os.py";
        assertTrue(STANDALONE_PATTERN.matcher(urlpath).find());

        // old url without version
        urlpath = "anyRoot/wildfly-18.0.0.Final/standalone/deployments/BisonProcess.ear/lib/jython.jar!/Lib/os.py";
        assertTrue(STANDALONE_PATTERN.matcher(urlpath).find());
    }
}
