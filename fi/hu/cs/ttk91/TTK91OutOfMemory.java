/*
 * See separate documentation in yhteisapi.pdf in the javadoc root.
 */
package fi.hu.cs.ttk91;

public class TTK91OutOfMemory extends TTK91RuntimeException {
  public TTK91OutOfMemory(String message) {
    super(message);
  }
}
