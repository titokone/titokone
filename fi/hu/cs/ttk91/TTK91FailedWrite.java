/*
 * Created on Mar 17, 2004
 *
 * See separate documentation in yhteisapi.pdf in the javadoc root.
 */
package fi.hu.cs.ttk91;

public class TTK91FailedWrite extends TTK91RuntimeException {
  public TTK91FailedWrite(String message) {
    super(message);
  }
}
