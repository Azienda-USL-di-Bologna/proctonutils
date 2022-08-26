/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.bologna.ausl.proctonutils;

/**
 *
 * @author Giuseppe
 */
public class SignStatus {

private String signatureName;
private String signatureValidityStatus;
private String errorMessage;
private SignGeneralStatus generalStatus;

    public SignStatus (String signatureName, String signatureValidityStatus, String errorMessage, SignGeneralStatus generalStatus) {
        this.signatureName = signatureName;
        this.signatureValidityStatus = signatureValidityStatus;
        this.errorMessage = errorMessage;
        this.generalStatus = generalStatus;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSignatureValidityStatus() {
        return signatureValidityStatus;
    }

    public void setSignatureValidityStatus(String signatureValidityStatus) {
        this.signatureValidityStatus = signatureValidityStatus;
    }

    public SignGeneralStatus getGeneralStatus() {
        return generalStatus;
    }

    public void setGeneralStatus(SignGeneralStatus generalStatus) {
        this.generalStatus = generalStatus;
    }
}
