/*
 * Created on Mar 17, 2004
 */
package fi.hu.cs.ttk91;

/*
 * See separate documentation in yhteisapi.pdf in the javadoc root.
 */
public class TTK91FailedWrite extends TTK91RuntimeException {
  public TTK91FailedWrite(String message) {
    super(message);
  }
}
