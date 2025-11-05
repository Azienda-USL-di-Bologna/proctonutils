package it.bologna.ausl.proctonutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import it.bologna.ausl.estrattore.ExtractorResult;
import it.bologna.ausl.estrattore.P7mExtractor;
import it.bologna.ausl.estrattore.exception.ExtractorException;
import static it.bologna.ausl.proctonutils.UtilityFunctions.isP7m;
import static it.bologna.ausl.proctonutils.UtilityFunctions.isPdf;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStoreBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.cms.jcajce.JcaX509CertSelectorConverter;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.tsp.cms.CMSTimeStampedData;
import org.bouncycastle.tsp.cms.CMSTimeStampedDataParser;
import org.bouncycastle.util.Store;

/**
 *
 * @author gdm
 * 
 * Questa classe di occupa di estrarre le informazioni fi firma e marca temporale da file firmati cades e pades
 * Il metodo extractSingsAndTimeStampsInfo torna una mappa nel formato seguente:
 *  [
	{
            "valid": true,
            "tsa": "Timestamping Authority",
            "cert": {
                "subDN": "subjectDN",
                "SerialNumber": "SerialNumber",
                "NotBefore": "2017-11-27T11:55:45.000+0100",
                "NotAfter": "2021-11-27T01:00:00.000+0100",
                "issDN": "issuerDN"
            },
            "time": "2017-12-12T09:50:20.000+0100",
            "type": "ts",
            "policy": "policy" //es. 1.3.76.36.1.1.42
	}, {
            "valid": true,
            "cert": {
                "subDN": "subjectDN",
                "SerialNumber": "SerialNumber",
                "NotBefore": "2016-02-11T17:13:32.000+0100",
                "NotAfter": "2019-03-28T23:59:59.000+0100",
                "issDN": "issuerDN"
            },
            "time": "2017-12-12T09:49:26.000+0100",
            "type": "sign"
	}, {
            "valid": true,
            "cert": {
                "subDN": "subjectDN",
                "SerialNumber": "SerialNumber",
                "NotBefore": "2017-05-31T02:00:00.000+0200",
                "NotAfter": "2020-05-31T01:59:59.000+0200",
                "issDN": "issuerDN"
            },
            "time": "2017-12-12T08:59:05.000+0100",
            "type": "sign_and_ts",
            "ts": {
                "valid": true,
                "tsa": "Timestamping Authority",
                "cert": {
                    "subDN": "subjectDN",
                    "SerialNumber": "18107788154668121572860443733340957980",
                    "NotBefore": "2017-09-15T02:00:00.000+0200",
                    "NotAfter": "2027-09-14T01:59:59.000+0200",
                    "issDN": "issuerDN"
                },
                "time": "2017-12-12T08:59:09.000+0100",
                "type": "ts",
                "policy": "policy" //es. 1.3.76.36.1.1.42
            }
	}
    ]
 */
public class SignsAndTimeStampsInfoExtractor {

    static {
        try {
            java.lang.reflect.Field algorithmNamesField = com.itextpdf.text.pdf.security.EncryptionAlgorithms.class.getDeclaredField("algorithmNames");
            algorithmNamesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, String> algorithmNames = (HashMap<String, String>) algorithmNamesField.get(null);
            algorithmNames.put("1.2.840.10045.4.3.2", "ECDSA");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    private final ObjectMapper objectMapper;
    
    public SignsAndTimeStampsInfoExtractor() {
        this.objectMapper = new ObjectMapper();
    }
    
    private String toISODateString(Calendar date) {
        return toISODateString(date.getTime());
    }
    
    private String toISODateString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String str = df.format(date);
        return str;
    }
    
    private Map<String, Object> buildTSInfoMap(TimeStampToken timeStampToken) {
        Map<String, Object> res = new HashMap<>();
        Date genTime = timeStampToken.getTimeStampInfo().getGenTime();
        String time = toISODateString(genTime);
        boolean valid = false;
        Store storeTt = timeStampToken.getCertificates();
        Map<String, Object> certInfoMap = null;
        if (storeTt != null) {
            Collection collTt = storeTt.getMatches(timeStampToken.getSID());
            if (collTt != null && !collTt.isEmpty()) {
                Iterator certIt = collTt.iterator();
                try {
                    X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
                    certInfoMap = buildCertInfoMap(new JcaX509CertificateConverter().getCertificate(cert));
                    try {
                        timeStampToken.validate(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert));
                        valid = true;
                    } catch (TSPException | IllegalArgumentException ex) {
                        valid = false;
                    } catch (Exception ex) {
                        // lascio valid a false perché non posso determinare se è valida
                    }
                } catch (Exception ex) {
                    // lascio valid a false perché non posso determinare se è valida
                }
            }
        }
        res.put("cert", certInfoMap);
        
        res.put("type", "ts");
        res.put("tsa", timeStampToken.getTimeStampInfo().getTsa() != null? timeStampToken.getTimeStampInfo().getTsa().toString(): null);
        res.put("time", time);
        res.put("policy", timeStampToken.getTimeStampInfo().getPolicy() != null? timeStampToken.getTimeStampInfo().getPolicy().toString(): null);
        res.put("valid", valid);
        return res;
    }
    
    private Map<String, Object> buildCertInfoMap(X509Certificate cert) {
        Map<String, Object> res = new HashMap<>();
        try {
//            X500Name subjectFields = new JcaX509CertificateHolder(cert).getSubject();
            res.put("subDN", cert.getSubjectDN().getName());
            res.put("issDN", cert.getIssuerDN().getName());
            res.put("SerialNumber", cert.getSerialNumber().toString());
            res.put("NotBefore", toISODateString(cert.getNotBefore()));
            res.put("NotAfter", toISODateString(cert.getNotAfter()));
        } catch (Exception ex) {
        }
        return res;
    }
    
    private List<Map<String, Object>> extractPdfInfos(File file) throws CertStoreException, CertificateEncodingException, ParseException, FileNotFoundException, CMSException, GeneralSecurityException, IOException, TSPException, OperatorCreationException {
        List<Map<String, Object>> res = new ArrayList<>();
        PdfReader reader = null;
        try {
            reader = new PdfReader(file.getAbsolutePath());
            List<String> signatureNames = reader.getAcroFields().getSignatureNames();
            if (signatureNames != null && !signatureNames.isEmpty()){
                for (String signatureName : signatureNames) {
                    Map<String, Object> infoMap = new HashMap<>();
                    AcroFields af = reader.getAcroFields();
                    PdfPKCS7 pk = af.verifySignature(signatureName);
                    if (pk.isTsp()) {
                        if (pk.getTimeStampToken() != null) {
                            infoMap = buildTSInfoMap(pk.getTimeStampToken());
                        }
                    } else {
                        if (pk.getTimeStampToken() != null) {
                            infoMap.put("type", "sign_and_ts");
                            infoMap.put("ts", buildTSInfoMap(pk.getTimeStampToken()));
                        } else {
                            infoMap.put("type", "sign");
                        }
                        Calendar signDate = pk.getSignDate();
                        infoMap.put("time", toISODateString(signDate));
                        X509Certificate signingCertificate = pk.getSigningCertificate();
                        if (signingCertificate != null) {
                            Map<String, Object> certInfoMap = buildCertInfoMap(signingCertificate);
                            if (certInfoMap != null && !certInfoMap.isEmpty()) {
                                infoMap.put("cert", certInfoMap);
                            }
                        }
                        boolean valid = false;
                        try {
                            valid = pk.verify();
                        } catch (Exception ex) {}
                        infoMap.put("valid", valid);
                    }
                    res.add(infoMap);
                }
            }
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
        return res;
    }
    
    /**
     * Estrare le informazione dallo stream passato (pades e cades)
     * @param file
     * @return una mappa delle firma e delle marche temporali, dove una firma può a sua volta contenere delle marche temporali. 
     * Un esempio della mappa è presente nella descrizione della classe
     * @throws java.io.IOException
     *
     */
    public List<Map<String, Object>> extractSingsAndTimeStampsInfo(InputStream file) throws IOException {
        File tempFile = File.createTempFile("extractSingsAndTimeStampsInfo_", ".tmp");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            IOUtils.copy(file, fos);
            return extractSingsAndTimeStampsInfo(tempFile);
        } finally {
            if (tempFile != null) {
                tempFile.delete();
            }
        }
    }
    
    /**
     * Estrare le informazione dal file passato (pades e cades)
     * @param file
     * @return una mappa delle firma e delle marche temporali, dove una firma può a sua volta contenere delle marche temporali. 
     * Un esempio della mappa è presente nella descrizione della classe
     *
     */
    public List<Map<String, Object>> extractSingsAndTimeStampsInfo(File file) {
        List<Map<String, Object>> res = new ArrayList<>();
        try {
            if (isP7m(file)) {
                res = extractP7mInfos(file);
                
                P7mExtractor p7mExtractor = new P7mExtractor(file);
                try {
                    ArrayList<ExtractorResult> extractedResults = p7mExtractor.extract(new File(FileUtils.getTempDirectoryPath()), null);
                    if (extractedResults != null && !extractedResults.isEmpty()) {
                        try {
                            ExtractorResult extractedResult = extractedResults.get(0);
                            File extractedFile = new File(extractedResult.getPath());
                            res.addAll(extractSingsAndTimeStampsInfo(extractedFile));
                        } catch (Exception ex) {
                            System.out.println("error on extracting file");
                            ex.printStackTrace(System.out);
                        }
                        finally {
                            for (ExtractorResult extractedResult : extractedResults) {
                                new File(extractedResult.getPath()).delete();
                            }
                        }
                    }
                } catch (ExtractorException ex) {
                }
            } else if (isPdf(file)) {
            res = extractPdfInfos(file);
            }
        } catch (Exception ex) {
            System.out.println("error extracting informations");
            ex.printStackTrace(System.out);
        }
        
        return res;
    }
    
    private List<Map<String, Object>> extractP7mInfos(File file) throws CertStoreException, CertificateEncodingException, ParseException, FileNotFoundException, CMSException, GeneralSecurityException, IOException, TSPException, OperatorCreationException {
        List<Map<String, Object>> res = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file)) {
            CMSTimeStampedData cmsTimeStampedData = new CMSTimeStampedData(fis);
            TimeStampToken[] timeStampTokens = cmsTimeStampedData.getTimeStampTokens();
            if (timeStampTokens != null && timeStampTokens.length > 0) {
                for (TimeStampToken timeStampToken : timeStampTokens) {
                    Map<String, Object> tsInfoMap = buildTSInfoMap(timeStampToken);
                    if (tsInfoMap != null && !tsInfoMap.isEmpty()) {
                        res.add(tsInfoMap);
                    }
                }
            }
        } catch (Exception ex) {
        }
        
        SignerInformationStore signers;
        CertStore certs;
        try (FileInputStream fis = new FileInputStream(file)) {
            CMSSignedData cms;
            try (ASN1InputStream asn1 = new ASN1InputStream(fis)) {
                cms = new CMSSignedData(asn1);
            } catch (CMSException ex) {
                try (FileReader reader = new FileReader(file); PEMParser pemParser = new PEMParser(reader)) {
                    byte[] bytes = pemParser.readPemObject().getContent();
                    try(ByteArrayInputStream bais = new ByteArrayInputStream(bytes); ASN1InputStream asn1 = new ASN1InputStream(bais)) {
                        cms = new CMSSignedData(asn1);
                    }
                } catch (Exception subEx) {
                        CMSTimeStampedDataParser tsd = null;
                    try (FileInputStream newFis = new FileInputStream(file);) {
                        tsd = new CMSTimeStampedDataParser(newFis);
                        try (InputStream is = tsd.getContent(); ASN1InputStream asn1 = new ASN1InputStream(is);) {
                            cms = new CMSSignedData(asn1);
                        }
                    } finally {
                        if (tsd != null) {
                            tsd.close();
                        }
                    }
                }
            }
            JcaCertStoreBuilder builder = new JcaCertStoreBuilder();
            certs = builder.addCertificates(cms.getCertificates()).build();
            signers = cms.getSignerInfos();
        }
        Collection c = signers.getSigners();
        if (!c.isEmpty()) {
            Iterator it = c.iterator();
            while (it.hasNext()) {
                Map<String, Object> signInfoMap = new HashMap<>();
                boolean hasTS = false;
                SignerInformation signer = (SignerInformation) it.next();

                AttributeTable unsignedAttributes = signer.getUnsignedAttributes();
                if (unsignedAttributes != null) {
                    Attribute att = unsignedAttributes.get(PKCSObjectIdentifiers.id_aa_signatureTimeStampToken);
                    if (att != null && att.getAttrValues() != null && att.getAttrValues().size() > 0) {
                        ASN1Encodable dob = att.getAttrValues().getObjectAt(0);
                        byte[] encodedTsp = dob.toASN1Primitive().getEncoded();
                        if(encodedTsp != null) {
                            hasTS = true;
                            // TODO: può essere in formato PEM? nel caso andrebbe gestito anche quello
                            CMSSignedData cms = new CMSSignedData(encodedTsp);
                            TimeStampToken tsToken = new TimeStampToken(cms);
                            Map<String, Object> tsInfoMap = buildTSInfoMap(tsToken);
                            if (tsInfoMap != null && !tsInfoMap.isEmpty()) {
                                signInfoMap.put("ts", tsInfoMap);
                            }
                        }
                    }
                }
                if (hasTS) {
                    signInfoMap.put("type", "sign_and_ts");
                } else {
                    signInfoMap.put("type", "sign");
                }
                Collection<? extends Certificate> certCollection = certs.getCertificates(new JcaX509CertSelectorConverter().getCertSelector(signer.getSID()));
                Iterator certIt = certCollection.iterator();
                X509Certificate signingCertificate = (X509Certificate) certIt.next();
                Map<String, Object> certInfoMap = buildCertInfoMap(signingCertificate);
                signInfoMap.put("cert", certInfoMap);
                SignerInformationVerifier signerInformationVerifier = new JcaSimpleSignerInfoVerifierBuilder().build(signingCertificate);
                try {
                    signInfoMap.put("valid", signer.verify(signerInformationVerifier));
                } catch (Exception ex) {
                    signInfoMap.put("valid", false);
                }
                AttributeTable signedAttr = signer.getSignedAttributes();
                Attribute signingTime = signedAttr.get(CMSAttributes.signingTime);
                if (signingTime != null) {
                    Enumeration en = signingTime.getAttrValues().getObjects();
                    Date date = null;
                    while (en.hasMoreElements()) {
                        Object obj = en.nextElement();
                        if (obj instanceof ASN1UTCTime) {
                            ASN1UTCTime asn1Time = (ASN1UTCTime) obj;
                            date = asn1Time.getDate();
                            break;
                        } else if (obj instanceof DERUTCTime) {
                            DERUTCTime derTime = (DERUTCTime) obj;
                            date = derTime.getDate();
                            break;
                        }
                    }
                    signInfoMap.put("time", toISODateString(date));
                }
                res.add(signInfoMap);
            }
        }
        return res;
    }
    
    /**
     * Converte in una stringa JSON il risultato del metodo extractSingsAndTimeStampsInfo
     * @param singsAndTimeStampsInfo la mappa tornata dal metodo extractSingsAndTimeStampsInfo
     * @return 
     * @throws JsonProcessingException 
     */
    public String stringifySingsAndTimeStampsInfo(List<Map<String, Object>> singsAndTimeStampsInfo) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(singsAndTimeStampsInfo);
    }
    
    /**
     * conta il numero di firme su un file. Richiama semplicemente la funzione extractSingsAndTimeStampsInfo,
     * poi conta solo gli oggetti ti tipo sign e sign_and_ts in modo da escludere le sole marche temporali
     * @param file
     * @return il numero di firme su un file
     */
    public int getSignsNumber(File file) {
        List<Map<String, Object>> signers = extractSingsAndTimeStampsInfo(file);
        long signs = signers.stream().filter(s -> s.get("type").equals("sign") || s.get("type").equals("sign_and_ts")).count();
        return (int) signs;
    }
    
    /**
     * conta il numero di firme su un file. Richiama semplicemente la funzione extractSingsAndTimeStampsInfo,
     * poi conta solo gli oggetti ti tipo sign e sign_and_ts in modo da escludere le sole marche temporali
     * @param file
     * @return il numero di firme su un file
     * @throws java.io.IOException
     */
    public int getSignsNumber(InputStream file) throws IllegalArgumentException, IOException {
        File tempFile = File.createTempFile("getSignsNumber_", ".tmp");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            IOUtils.copy(file, fos);
            return getSignsNumber(tempFile);
        } finally {
            if (tempFile != null) {
                tempFile.delete();
            }
        } 
    }
    
    public static void main(String[] args) throws JsonProcessingException, FileNotFoundException, IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
//                File file = new File("D:\\tmp\\tmp\\fime_marca_5.tsd");
                File file = new File("2025-10934_Allegato_orig1.pdf");
        FileInputStream fileInputStream = new FileInputStream(file);
//        File file = new File("D:\\tmp\\tmp\\marca_1.pdf");
//        File file = new File("D:\\tmp\\tmp\\rima_e_marca_3.pdf.p7m.p7m");
//        File file = new File("D:\\tmp\\tmp\\rima_e_marca_2.pdf");
//        File file = new File("D:\\tmp\\tmp\\rima_e_marca_4_rotta.pdf");
//        File file = new File("D:\\tmp\\tmp\\a.tsr");
        
        SignsAndTimeStampsInfoExtractor singsAndTimeStampsInfoExtractor = new SignsAndTimeStampsInfoExtractor();
        int signsNumber = singsAndTimeStampsInfoExtractor.getSignsNumber(fileInputStream);
        System.out.println("signsNumber: "  + signsNumber);
        System.exit(0);
        List<Map<String, Object>> extractSingsAndTimeStampsInfo = singsAndTimeStampsInfoExtractor.extractSingsAndTimeStampsInfo(fileInputStream);
        System.out.println(singsAndTimeStampsInfoExtractor. stringifySingsAndTimeStampsInfo(extractSingsAndTimeStampsInfo));
    }
}
