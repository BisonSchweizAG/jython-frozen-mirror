package ch.obj.commons.core.util.jython;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

abstract class AbstractLineOutputStream extends OutputStream {

  /** Initial buffer size. */
  private static final int INTIAL_SIZE = 132;

  /** Carriage return */
  private static final int CR = 0x0d;

  /** Linefeed */
  private static final int LF = 0x0a;

  private ByteArrayOutputStream _buffer = new ByteArrayOutputStream(INTIAL_SIZE);
  private boolean _skip;

  /**
   * handle a line.
   * 
   * @param line the line to handle.
   */
  public abstract void processLine(String line);

  /**
   * Write the data to the buffer and flush the buffer, if a line separator is detected.
   * 
   * @param cc data (byte).
   */
  @Override
  public void write(int cc) throws IOException {
    final byte c = (byte)cc;
    if ((c == '\n') || (c == '\r')) {
      if (!_skip) {
        processBuffer();
      }
    } else {
      _buffer.write(cc);
    }
    _skip = (c == '\r');
  }

  /**
   * Flush this stream
   */
  @Override
  public void flush() {
    if (_buffer.size() > 0) {
      processBuffer();
    }
  }

  /**
   * Converts the buffer to a string and sends it to <code>processLine</code>
   */
  protected void processBuffer() {
    processLine(_buffer.toString());
    _buffer.reset();
  }

  /**
   * Writes all remaining
   */
  @Override
  public void close() throws IOException {
    if (_buffer.size() > 0) {
      processBuffer();
    }
    // do this for symmetry, but it has no effect on a ByteArrayOutputStream
    _buffer.close();
    super.close();
  }

  /**
   * Write a block of characters to the output stream
   * 
   * @param b the array containing the data
   * @param off the offset into the array where data starts
   * @param len the length of block
   * 
   * @throws IOException if the data cannot be written into the stream.
   */
  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    // find the line breaks and pass other chars through in blocks
    int offset = off;
    int blockStartOffset = offset;
    int remaining = len;
    while (remaining > 0) {
      while (remaining > 0 && b[offset] != LF && b[offset] != CR) {
        offset++;
        remaining--;
      }
      // either end of _buffer or a line separator char
      int blockLength = offset - blockStartOffset;
      if (blockLength > 0) {
        _buffer.write(b, blockStartOffset, blockLength);
      }
      while (remaining > 0 && (b[offset] == LF || b[offset] == CR)) {
        write(b[offset]);
        offset++;
        remaining--;
      }
      blockStartOffset = offset;
    }
  }

}
