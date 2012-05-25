/*
 * Created on Mar 17, 2004
 */
package fi.helsinki.cs.ttk91;

/*
 * See separate documentation in yhteisapi.pdf in the javadoc root.
 */
public class TTK91InvalidDevice extends TTK91RuntimeException {
  public TTK91InvalidDevice(String message) {
    super(message);
  }
}
