package org.python.core;

import static org.python.core.PySystemState.STANDALONE_PATTERN;

import junit.framework.TestCase;

public class PySystemState_standaloneMatcherTest extends TestCase {

    public void testWildflyUrl() {
        String urlpath = "/C:/workspace/eclipse_bp_BASE_GRADLE/srv/wildfly-18.0.0.Final/standalone/deployments/BisonProcess.ear/lib/jython-2.5.3.3.jar/Lib/os.py";
        String oldUrlpath = "/C:/workspace/eclipse_bp_BASE_GRADLE/srv/wildfly-18.0.0.Final/standalone/deployments/BisonProcess.ear/lib/jython-2.5.3.3.jar!/Lib/os.py";

        assertTrue(STANDALONE_PATTERN.matcher(urlpath).find());
        assertTrue(STANDALONE_PATTERN.matcher(oldUrlpath).find());
    }
}
