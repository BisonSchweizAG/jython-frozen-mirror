package ch.obj.commons.core.util.jython;

public class JythonSup {

  public final void publicFinal(String arg) {
    log("java publicFinal(" + arg + ")");
  }

  public void publicNonfinal(String arg) {
    log("java publicNonfinal(" + arg + ")");
  }

  protected final void protectedFinal(String arg) {
    log("java protectedFinal(" + arg + ")");
  }

  protected void protectedNonfinal(String arg) {
    log("java protectedNonfinal(" + arg + ")");
  }

  /** same name but different signature */
  protected final void protectedFinalAndNonfinal(String arg) {
    log("java [final] protectedFinalAndNonfinal(" + arg + ")");
  }

  /** same name but different signature */
  protected void protectedFinalAndNonfinal(String arg1, String arg2) {
    log("java [nonfinal] protectedFinalAndNonfinal(" + arg1 + ", " + arg2 + ")");
  }

  private void log(String msg) {
    System.out.println(msg);
  }

}
