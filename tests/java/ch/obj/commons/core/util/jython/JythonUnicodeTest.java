package ch.obj.commons.core.util.jython;

import junit.framework.TestCase;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class JythonUnicodeTest extends TestCase {

    public void testUnicodeConcatenation() {
        PySystemState.initialize();
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec(getUnicodeConcatScript());
    }

    private String getUnicodeConcatScript() {
        StringBuilder b = new StringBuilder(2000);
        b.append("from java.lang import String\n");
        b.append("\n");
        b.append("ae = u'\u00E4'\n");
        b.append("oe = u'\u00F6'\n");
        b.append("ue = u'\u00FC'\n");
        b.append("umlautString = String(ue)\n");
        b.append("\n");
        b.append("body = 'start'\n");
        b.append("print type(body) # <type 'str'>\n");
        b.append("\n");
        b.append("body += ' ' + ae\n");
        b.append("print type(body) # <type 'unicode'>\n");
        b.append("\n");
        b.append("# this used to fail with [UnicodeDecodeError: 'ascii' codec can't decode byte 0xfc in position 1: ordinal not in range(128)]:\n");
        b.append("body += ' %s' % (umlautString)\n");
        b.append("print type(body)\n");
        return b.toString();
    }

}
