package ch.obj.commons.core.util.jython;

import java.io.PrintStream;

import junit.framework.TestCase;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class JythonSupTest extends TestCase {

  private PrintStream _originalOut;
  private CollectingLineOutputStream _out;

  @Override
  protected void setUp() throws Exception {
    _originalOut = System.out;
    _out = new CollectingLineOutputStream();
    System.setOut(new PrintStream(_out));
  }

  @Override
  protected void tearDown() throws Exception {
    System.setOut(_originalOut);
  }

  /**
   * Test calling the methods of the java super class
   */
  public void testSuperMethods() {
    assertJythonCall(getSub(), getSubOutput());
  }

  /**
   * Test overwriting the non-final methods of the super class
   */
  public void testOverwriteNonFinalMethods() {
    assertJythonCall(getSubOverwrite(), getSubOverwriteOutput());
  }

  /**
   * Tests calling overwritten and overloaded methods of the superclass
   */
  public void testOverwriteOverloadMethods() {
    assertJythonCall(getSubOverwriteOverload(), getSubOverwriteOverloadOutput());
  }

  /**
   * Test calling overwritten and overloaded methods of the superclass (high end version)
   */
  public void testAll() {
    assertJythonCall(getSubAll(), getSubAllOutput());
  }

  private void assertJythonCall(String input, String expectedOutput) {
	PySystemState.initialize();
    PythonInterpreter interpreter = new PythonInterpreter();
    interpreter.exec(input);
    assertEquals(expectedOutput, _out.toString());
  }

  // calling java superclass methods

  private String getSub() {
    StringBuilder b = new StringBuilder(200);
    b.append("from ch.obj.commons.core.util.jython import JythonSup\n");
    b.append('\n');
    b.append("class Sub(JythonSup):\n");
    b.append('\n');
    b.append("  def callSuperMethods(self):\n");
    b.append("    self.publicFinal('foo')\n");
    b.append("    self.publicNonfinal('bar')\n");
    b.append("    self.protectedFinal('baz')\n");
    b.append("    self.super__protectedFinal('baz')\n"); // 'classic' style
    b.append("    self.protectedNonfinal('tar')\n");
    b.append('\n');
    b.append("sub = Sub()\n");
    b.append("sub.callSuperMethods()\n");
    return b.toString();
  }

  private String getSubOutput() {
    StringBuilder b = new StringBuilder(200);
    b.append("java publicFinal(foo)\n");
    b.append("java publicNonfinal(bar)\n");
    b.append("java protectedFinal(baz)\n");
    b.append("java protectedFinal(baz)\n");
    b.append("java protectedNonfinal(tar)\n");
    return b.toString();
  }

  // calling java superclass methods and own overwritten methods

  private String getSubOverwrite() {
    // please not: cannot use 'print' in Jython code
    StringBuilder b = new StringBuilder(200);
    b.append("from ch.obj.commons.core.util.jython import JythonSup\n");
    b.append("from java.lang import System\n");
    b.append('\n');
    b.append("class Sub(JythonSup):\n");
    b.append('\n');
    b.append("  def callFinalSuperMethods(self):\n");
    b.append("    System.out.println('')\n");
    b.append("    self.publicFinal('foo')\n");
    b.append("    self.protectedFinal('baz')\n");
    b.append('\n');
    b.append("  def callOwnNonfinalMethods(self):\n");
    b.append("    System.out.println('')\n");
    b.append("    self.publicNonfinal('bar')\n");
    b.append("    self.protectedNonfinal('tar')\n");
    b.append('\n');
    b.append("  def publicNonfinal(self, arg):\n");
    b.append("    System.out.println('jython publicNonfinal(%s)' % arg)\n");
    b.append("    JythonSup.publicNonfinal(self, arg)\n");
    b.append('\n');
    b.append("  def protectedNonfinal(self, arg):\n");
    b.append("    System.out.println('jython protectedNonfinal(%s)' % arg)\n");
    b.append("    self.super__protectedNonfinal(arg)\n");
    b.append('\n');
    b.append("sub = Sub()\n");
    b.append("sub.callFinalSuperMethods()\n");
    b.append("sub.callOwnNonfinalMethods()\n");
    return b.toString();
  }

  private String getSubOverwriteOutput() {
    StringBuilder b = new StringBuilder(200);
    b.append('\n');
    b.append("java publicFinal(foo)\n");
    b.append("java protectedFinal(baz)\n");
    b.append('\n');
    b.append("jython publicNonfinal(bar)\n");
    b.append("java publicNonfinal(bar)\n");
    b.append("jython protectedNonfinal(tar)\n");
    b.append("java protectedNonfinal(tar)\n");
    return b.toString();
  }

  // call overwritten and overloaded methods of the superclass

  private String getSubOverwriteOverload() {
    StringBuilder b = new StringBuilder(200);
    b.append("from ch.obj.commons.core.util.jython import JythonSup\n");
    b.append("from java.lang import System\n");
    b.append('\n');
    b.append("class Sub(JythonSup):\n");
    b.append('\n');
    b.append("  def callFinalSuperMethods(self):\n");
    b.append("    System.out.println('')\n");
    b.append("    self.publicFinal('foo')\n");
    b.append("    self.protectedFinal('baz')\n");
    b.append("    self.super__protectedFinalAndNonfinal('final')\n");
    b.append('\n');
    b.append("  def callOwnNonfinalMethods(self):\n");
    b.append("    System.out.println('')\n");
    b.append("    self.publicNonfinal('bar')\n");
    b.append("    self.protectedNonfinal('tar')\n");
    b.append("    self.protectedFinalAndNonfinal('nonfinal', 'two_args')\n");
    b.append('\n');
    b.append("  def publicNonfinal(self, arg):\n");
    b.append("    System.out.println('jython publicNonfinal(%s)' % arg)\n");
    b.append("    JythonSup.publicNonfinal(self, arg)\n");
    b.append('\n');
    b.append("  def protectedNonfinal(self, arg):\n");
    b.append("    System.out.println('jython protectedNonfinal(%s)' % arg)\n");
    b.append("    self.super__protectedNonfinal(arg)\n");
    b.append('\n');
    b.append("sub = Sub()\n");
    b.append("sub.callFinalSuperMethods()\n");
    b.append("sub.callOwnNonfinalMethods()\n");
    return b.toString();
  }

  private String getSubOverwriteOverloadOutput() {
    StringBuilder b = new StringBuilder(200);
    b.append('\n');
    b.append("java publicFinal(foo)\n");
    b.append("java protectedFinal(baz)\n");
    b.append("java [final] protectedFinalAndNonfinal(final)\n");
    b.append('\n');
    b.append("jython publicNonfinal(bar)\n");
    b.append("java publicNonfinal(bar)\n");
    b.append("jython protectedNonfinal(tar)\n");
    b.append("java protectedNonfinal(tar)\n");
    b.append("java [nonfinal] protectedFinalAndNonfinal(nonfinal, two_args)\n");
    return b.toString();
  }

  // calling overwritten and overloaded methods of the superclass (high end version)

  private String getSubAll() {
    StringBuilder b = new StringBuilder(200);
    b.append("from ch.obj.commons.core.util.jython import JythonSup\n");
    b.append("from java.lang import System\n");
    b.append('\n');
    b.append("class Sub(JythonSup):\n");
    b.append('\n');
    b.append("  def callFinalSuperMethods(self):\n");
    b.append("    System.out.println('')\n");
    b.append("    self.publicFinal('foo')\n");
    b.append("    self.protectedFinal('baz')\n");
    b.append("    self.super__protectedFinalAndNonfinal('final')\n");
    b.append('\n');
    b.append("  def callOwnNonfinalMethods(self):\n");
    b.append("    System.out.println('')\n");
    b.append("    self.publicNonfinal('bar')\n");
    b.append("    self.protectedNonfinal('tar')\n");
    b.append("    self.protectedFinalAndNonfinal('nonfinal', 'two_args')\n");
    b.append('\n');
    b.append("  def publicNonfinal(self, arg):\n");
    b.append("    System.out.println('jython publicNonfinal(%s)' % arg)\n");
    b.append("    JythonSup.publicNonfinal(self, arg)\n");
    b.append('\n');
    b.append("  def protectedNonfinal(self, arg):\n");
    b.append("    System.out.println('jython protectedNonfinal(%s)' % arg)\n");
    b.append("    self.super__protectedNonfinal(arg)\n");
    b.append('\n');
    b.append("  def protectedFinalAndNonfinal(self, arg1, arg2):\n");
    b.append("    System.out.println('jython [nonfinal] protectedFinalAndNonfinal(%s, %s)' % (arg1, arg2))\n");
    b.append("    self.super__protectedFinalAndNonfinal(arg1, arg2)\n");
    b.append('\n');
    b.append("sub = Sub()\n");
    b.append("sub.callFinalSuperMethods()\n");
    b.append("sub.callOwnNonfinalMethods()\n");
    return b.toString();
  }

  private String getSubAllOutput() {
    StringBuilder b = new StringBuilder(200);
    b.append('\n');
    b.append("java publicFinal(foo)\n");
    b.append("java protectedFinal(baz)\n");
    b.append("java [final] protectedFinalAndNonfinal(final)\n");
    b.append('\n');
    b.append("jython publicNonfinal(bar)\n");
    b.append("java publicNonfinal(bar)\n");
    b.append("jython protectedNonfinal(tar)\n");
    b.append("java protectedNonfinal(tar)\n");
    b.append("jython [nonfinal] protectedFinalAndNonfinal(nonfinal, two_args)\n");
    b.append("java [nonfinal] protectedFinalAndNonfinal(nonfinal, two_args)\n");
    return b.toString();
  }

}
