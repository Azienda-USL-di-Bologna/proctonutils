package it.bologna.ausl.proctonutils;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.CertificateInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import sun.security.x509.X509CertImpl;
import java.io.ByteArrayInputStream;
import java.security.GeneralSecurityException;
import org.apache.commons.io.IOUtils;
import org.apache.tika.mime.MimeTypeException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.util.Store;

/**
 *
 * @author Giuseppe
 */
public class SignatureVerifier {

private final File file;

private ArrayList signaturesResults;
private String signaturesResultsString;

private boolean allSignOK = false;


    /** Crea un oggetto "SignatureVerifier" per la verifica delle firme con ricerca dei certificati delle C.A. emittenti usando il file TSLXml pubblico
     *
     * @param file il file da controllare
     * @param workDirectory directory di temporanea di lavoro per lo scaricamento del file TSLXml
     */
    public SignatureVerifier(File file) {

        // Aggiungo il provider Bouncy Castle in modo da usarlo per generare l'hash con l'algoritmo SHA256
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        this.file = file;
    }

    /** Verifica lo stato delle firme.
     * Lo stato delle firme si può ottenere dai metodi: getSignsResults() e getSignsResultsString().
     * E' anche possibile conoscere lo stato generale delle firme dal metodo isAllSignOK().
     * @return "true" se tutte le firme e i certificati sono validi, "false" altrimenti.
     * @throws IOException
     * @throws org.apache.tika.mime.MimeTypeException
     */
    public boolean verify() throws IOException, MimeTypeException {
    String temp_signaturesResultsString = "";

        if (UtilityFunctions.isP7m(file)) {

            String sigName = null;
            boolean signRes = false;
            String sign = "";
            SignerInformationStore signers = null;
            Store certStore = null;
            InputStream encodedStream = null;
            ASN1InputStream asn1 = null;
            try {
                encodedStream = new FileInputStream(file);
                asn1 = new ASN1InputStream(encodedStream);
                CMSSignedData cms = new CMSSignedData(asn1);

                certStore = cms.getCertificates();
                signers = cms.getSignerInfos();
            }
            catch (Exception ex) {
                ex.printStackTrace(System.out);
                IOUtils.closeQuietly(asn1);
                IOUtils.closeQuietly(encodedStream);
                return false;
            }

            try {
                Collection c = signers.getSigners();
                Iterator it = c.iterator();

                while (it.hasNext()) {

                    X509Certificate signingCert = null;
                    try {
                        SignerInformation signer = (SignerInformation)it.next();
                        Collection certCollection = certStore.getMatches(signer.getSID());
                        Iterator certIt = certCollection.iterator();
    //                    signingCert = (X509Certificate)certIt.next();

                        X509CertificateHolder certHolder = (X509CertificateHolder)certIt.next();
                        signingCert = UtilityFunctions.readCert(new ByteArrayInputStream(certHolder.getEncoded()));
    //                    signingCert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder); 


                        // estraggo il codice fiscale dal certificato e lo uso come identificativo della firma
                        String subjectField = signingCert.getSubjectDN().getName();
                        String certCodFisc = "";
                        if (subjectField != null) {
                            StringTokenizer tok = new StringTokenizer(subjectField, ",");
                            while (tok.hasMoreTokens()) {
                                String field = tok.nextToken();
                                String[] splittedField = field.split("=");
                                if (splittedField[0].trim().equalsIgnoreCase("SERIALNUMBER")) {
                                    certCodFisc = splittedField[1];
                                    break;
                                }
                            }
                        }

                        sigName = certCodFisc;
    //                    signRes = signer.verify(signingCert.getPublicKey(),"BC");
                        signRes = signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(signingCert));
                        sign = "";
                        if (!signRes) {
                            sign = sigName + ";" + "false" + ";" +  "firma non valida";
                            System.out.println("La firma NON è valida");
                            temp_signaturesResultsString += sign;
                            if (it.hasNext())
                                temp_signaturesResultsString += "\n";
                            continue;
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace(System.out);
                        sign = sigName + ";" + "false" + ";" +  "impossibile verificare la firma";
                        temp_signaturesResultsString += sign;
                        if (it.hasNext())
                            temp_signaturesResultsString += "\n";
                        continue;
                    }

                    System.out.println("Firmatario: " + CertificateInfo.getSubjectFields(signingCert));
                    System.out.println("La firma è valida.");

                    sign = sigName + ";" + "true" + ";" +  "controllo della revoca disattivato";
                    temp_signaturesResultsString += sign;
                    if (it.hasNext())
                        temp_signaturesResultsString += "\n";
                }
            }
            finally {
                IOUtils.closeQuietly(asn1);
                IOUtils.closeQuietly(encodedStream);
            }
        }

        else if (UtilityFunctions.isPdf(file)) {
            PdfReader reader = null;
            try {
                reader = new PdfReader(file.getAbsolutePath());
            }
            catch (Exception ex) {
                ex.printStackTrace(System.out);
                try {
                    reader.close();
                }
                catch (Exception subEx) {
                }
                return false;
            }

            try {
                AcroFields af = reader.getAcroFields();
                ArrayList names = af.getSignatureNames();

                if (names.size() > 0) {
                    temp_signaturesResultsString = "";
                    for (int k = 0; k < names.size(); ++k) {
                        String sign = null;

                        String sigName = (String)names.get(k);

                        PdfPKCS7 pk = null;
                        X509Certificate signingCert = null;
                        try {
                            pk = af.verifySignature(sigName);
                            try {
                                signingCert = new X509CertImpl(pk.getSigningCertificate().getEncoded());
                            }
                            catch (CertificateEncodingException ex) {
                                ex.printStackTrace(System.out);
                                sign = sigName + ";" + "null" + ";" +  "impossibile leggere il certificato di firma";
                                temp_signaturesResultsString += sign;
                                if (k < names.size() - 1)
                                    temp_signaturesResultsString += "\n";
                                continue;
                            }
                            catch (CertificateException ex) {
                                ex.printStackTrace(System.out);
                                sign = sigName + ";" + "null" + ";" +  "impossibile leggere il certificato di firma";
                                temp_signaturesResultsString += sign;
                                if (k < names.size() - 1)
                                    temp_signaturesResultsString += "\n";
                                continue;
                            }

                            // estraggo il codice fiscale dal certificato e lo uso come identificativo della firma
                            String subjectField = signingCert.getSubjectDN().getName();
                            String certCodFisc = "";
                            if (subjectField != null) {
                                StringTokenizer tok = new StringTokenizer(subjectField, ",");
                                while (tok.hasMoreTokens()) {
                                    String field = tok.nextToken();
                                    String[] splittedField = field.split("=");
                                    if (splittedField[0].trim().equalsIgnoreCase("SERIALNUMBER")) {
                                        certCodFisc = splittedField[1];
                                        break;
                                    }
                                }
                            }
                            sigName += "/" + certCodFisc;
                     }
                        catch (Exception ex) {
                            ex.printStackTrace(System.out);
                            sign = sigName + ";" + "null" + ";" +  "impossibile verificare la firma";
                            temp_signaturesResultsString += sign;
                            if (k < names.size() - 1)
                                temp_signaturesResultsString += "\n";
                            continue;
                        }
                        boolean signRes = false;

                        // verifica della firma del PDF
                        try {
                            signRes = pk.verify();

                            if (!signRes) {
                                sign = sigName + ";" + "false" + ";" +  "firma non valida";
                                System.out.println("La firma NON è valida");
                                temp_signaturesResultsString += sign;
                                if (k < names.size() - 1)
                                    temp_signaturesResultsString += "\n";
                                continue;
                            }

                            System.out.println("Firmatario: " + CertificateInfo.getSubjectFields(signingCert));
                            System.out.println("La firma è valida e il documento non è stato modificato.");

                            sign = sigName + ";" + "true" + ";" +  "controllo della revoca disattivato";
                        }
                        catch (GeneralSecurityException ex) {
                            ex.printStackTrace(System.out);
                            sign = sigName + ";" + "null" + ";" +  "impossibile verificare la firma";
                        }
                        temp_signaturesResultsString += sign;
                        if (k < names.size() - 1)
                            temp_signaturesResultsString += "\n";
                    }
                }
            }
            finally {
                try {
                    reader.close();
                }
                catch (Exception ex) {
                }
            }
        }
        signaturesResultsString = temp_signaturesResultsString;
        signaturesResults = parseVerifierResponse(signaturesResultsString);
        return allSignOK;
    }

    /** Crea un ArrayList di SignStatus a partire dal risultato della stringa "signaturesResultsString".
     *  Un oggetto SignStatus rappresenta lo stato di una firma e mette a disposizione dei medoti per facilitarne il controllo.
     * @param signStatusString la stringa risultato della funzione verifiySignatureStatus()
     * @return un ArrayList di oggetti SignStatus, ogniuno dei quali rappresenta una firma
     * @throws IOException
     */
    private ArrayList parseVerifierResponse(String signStatusString) throws IOException {
    ArrayList signsStatus = new ArrayList();

        BufferedReader reader = new BufferedReader(new StringReader(signStatusString));
        String line = reader.readLine();
        while (line != null) {
            SignGeneralStatus generalStatus = SignGeneralStatus.VALID;
            StringTokenizer tok = new StringTokenizer(line, ";");
            String signName = tok.nextToken();
            String signResultReaded = tok.nextToken();
            String signMessage = tok.nextToken();

            String signResult = null;
            if (signResultReaded.equalsIgnoreCase("null")) {
                if (signMessage.equalsIgnoreCase("impossibile verificare la firma")) {
                    signResult = "Sconosciuta";
                    generalStatus = SignGeneralStatus.UNKNOWN;
                }
                else {
                    signResult = "Valida";
                }
            }
            else if (signResultReaded.equalsIgnoreCase("true")) {
                signResult = "Valida";
            }
            else if (signResultReaded.equalsIgnoreCase("false")) {
                signResult = "NON valida";
                generalStatus = SignGeneralStatus.NOTVALID;
            }

            if (signMessage.equalsIgnoreCase("null"))
                signMessage = "";
            SignStatus status = new SignStatus(signName, signResult, signMessage, generalStatus);
            allSignOK = status.getGeneralStatus() == SignGeneralStatus.VALID;
            signsStatus.add(status);
            line = reader.readLine();
        }
        return signsStatus;
    }

    /** Ritorna lo stato di una firma specifica
     *
     * @param signName il nome della firma della quale si desidera conoscere lo stato
     * @return un oggetto "SignStatus" che rappresenta lo stato della firma
     */
    public SignStatus getSignResult(String signName) {
        for (int i=0; i<signaturesResults.size(); i++) {
            SignStatus sign = (SignStatus)signaturesResults.get(i);
            if (sign.getSignatureName().equals(signName)) {
               return sign;
            }
        }
        return null;
    }

    /** Ritorna lo stato delle firme del file
     *
     * @return un "ArrayList" di oggetti "SignStatus" che rappresentano lo stato delle firme
     */
    public ArrayList getSignsResults() {
        return signaturesResults;
    }

    /** Ritorna lo stato delle firme del file in formato stringa
     *
     *  @return una stringa di più righe separate da un "\n" rappresentante lo stato delle firme. Ogni riga rappresenta una firma. La stringa è così formata:
     *  se il file è un pdf: "nome_campo_firma/codice_fiscale;booleano_indicante_la_validità_della_firma;tipo_di_errore";
     *  se il file è un p7m: "codice_fiscale;booleano_indicante_la_validità_della_firma;tipo_di_errore";
     *  il campo "booleano_indicante_la_validità_della_firma" vale:
     *      "true",  se la firma è valida e il certificato è valido;
     *      "false", se la firma non è valida (ad esempio, perché il documento è stato modificato dopo la firma);
     *      "null",  se non è stato possibile controllare la firma o il certificato, o se il certificato non è valido (ad esempio perché revocato);
     *  il campo "tipo_di_errore" può essere:
     *      "null" se la firma è valida e il certificato è valido;
     *      "firma non valida" se la firma non è valida (ad esempio, perché il documento è stato modificato dopo la firma);
     *      "impossibile verificare la firma" se non è stato possibile controllare la firma a causa di un errore;
     *      "impossibile leggere il certificato di firma" se non è stato possibile leggere il certificato della firma;
     *      "impossibile verificare lo stato di revoca del certificato" se non è stato possibile eseguire il controllo di verifica on-line del certificato (ad esempio per indisponibilità del server, oppure perché non è stato passato l'indirizzo della servlet di verifica);
     *      "certificato revocato" se il certificato è revocato
     */
    public String getSignsResultString() {
        return signaturesResultsString;
    }

    /** Ritorna i nomi di tutte le firma presenti nel file
     *
     * @return un "ArrayList" di stringhe contente i nomi delle firme
     */
    public ArrayList getSignatureNames() {
        ArrayList namesList = new ArrayList();
        for (int i=0; i<signaturesResults.size(); i++) {
            SignStatus sign = (SignStatus)signaturesResults.get(i);
            namesList.add(sign.getSignatureName());
        }
        return namesList;
    }

    /** Indica se tutte le firme sono valide
     *
     * @return "true" se tutte le firme e i relativi certificati sono valide, "false" altrimenti
     */
    public boolean isAllSignOK() {
        return allSignOK;
    }

}
