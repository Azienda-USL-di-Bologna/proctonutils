package it.bologna.ausl.proctonutils.exceptions;

/**
 *
 * @author gdm
 */
public class UtilityFunctionException extends Exception {

  public UtilityFunctionException() {
  }

  /**
   * @param message
   */
  public UtilityFunctionException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public UtilityFunctionException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public UtilityFunctionException(String message, Throwable cause) {
    super(message, cause);
  }
  
}
