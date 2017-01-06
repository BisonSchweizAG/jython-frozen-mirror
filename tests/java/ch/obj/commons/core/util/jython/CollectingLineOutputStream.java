package ch.obj.commons.core.util.jython;

import java.util.ArrayList;
import java.util.List;

class CollectingLineOutputStream extends AbstractLineOutputStream {
  private List<String> _lines;

  public CollectingLineOutputStream() {
    _lines = new ArrayList<String>(60);
  }

  @Override
  public void processLine(String line) {
    _lines.add(line);
  }

  public List<String> getLines() {
    return _lines;
  }

  @Override
  public String toString() {
    List<String> lines = getLines();
    StringBuilder b = new StringBuilder(lines.size());
    for (String line : lines) {
      b.append(line);
      b.append('\n');
    }
    return b.toString();
  }

}
