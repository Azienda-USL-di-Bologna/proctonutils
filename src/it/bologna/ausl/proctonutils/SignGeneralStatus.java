/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.bologna.ausl.proctonutils;

/**
 *
 * @author Giuseppe
 */
public class SignGeneralStatus {

    public static final SignGeneralStatus VALID = new SignGeneralStatus(0);
    public static final SignGeneralStatus UNKNOWN = new SignGeneralStatus(1);
    public static final SignGeneralStatus NOTVALID = new SignGeneralStatus(2);

    private final int status;

    private SignGeneralStatus(int status) {
        this.status = status;
    }

    public String toString() {
        String returnString = null;
        switch (status) {
            case 0:
                returnString = "VALID";
                break;
            case 1:
                returnString = "UNKNOWN";
                break;
            case 2:
                returnString = "NOTVALID";
        }
        return returnString;
    }
}
