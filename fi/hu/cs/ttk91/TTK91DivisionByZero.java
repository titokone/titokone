/*
 * Created on Mar 17, 2004
 *
 * See separate documentation in yhteisapi.pdf in the javadoc root.
 */
package fi.hu.cs.ttk91;

public class TTK91DivisionByZero extends TTK91RuntimeException {
  public TTK91DivisionByZero(String message) {
    super(message);
  }
}
