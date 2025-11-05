package it.bologna.ausl.proctonutils.exceptions;

/**
 *
 * @author gdm
 */
public class SVActionException extends Exception {

  /**
   *
   */
  public SVActionException() {
  }


  /**
   * @param message
   */
  public SVActionException(String message) {
    super(message);
  }


  /**
   * @param cause
   */
  public SVActionException(Throwable cause) {
    super(cause);
  }


  /**
   * @param message
   * @param cause
   */
  public SVActionException(String message, Throwable cause) {
    super(message, cause);
  }

}
