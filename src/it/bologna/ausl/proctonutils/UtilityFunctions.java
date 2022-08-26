package it.bologna.ausl.proctonutils;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAStamper;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.xml.xmp.PdfProperties;
import com.itextpdf.xmp.XMPException;
import it.bologna.ausl.mimetypeutilities.Detector;
import it.bologna.ausl.proctonutils.PasswordCrypto.EncryptedPassword;
import it.bologna.ausl.proctonutils.exceptions.SVActionException;
import it.bologna.ausl.proctonutils.exceptions.SendHttpMessageException;
import it.bologna.ausl.proctonutils.exceptions.UtilityFunctionException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import it.bologna.ausl.proctonutils.diff_match_patch.Diff;
import it.bologna.ausl.proctonutils.diff_match_patch.Patch;
import java.io.BufferedInputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Multipart;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaCertStoreBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaX509CertSelectorConverter;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.tsp.cms.CMSTimeStampedDataParser;
import org.jsoup.Jsoup;
import org.springframework.util.StringUtils;

import org.w3c.tidy.Tidy;

/**
 *
 * @author Giuseppe De Marco
 */
public class UtilityFunctions {

    // aggiunge il provider BouncyCastle nel contesto statico (nel caso venga chiamato un metodo statico della classe)
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * Restituisce una stringa rappresentante la codifica in base 64 del file
     * passato
     *
     * @param file il file da codificare
     * @return una stringa rappresentante la codifica in base 64 del file
     * passato
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String base64Encode(File file) throws FileNotFoundException, IOException {
        DataInputStream datais = new DataInputStream(new FileInputStream(file));
        byte[] bytes = new byte[(int) file.length()];
        datais.readFully(bytes);
        datais.close();
        return Base64Coder.encodeLines(bytes);
    }

    /**
     * Restituisce un array di bytes rappresentante il file in base64 passato
     * decodificato
     *
     * @param filePath il path del file da codificare
     * @return una stringa rappresentante la codifica in base 64 del file
     * passato
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static byte[] base64Decode(String filePath) throws FileNotFoundException, IOException {
        File file = new File(filePath);
        DataInputStream datais = new DataInputStream(new FileInputStream(file));
        byte[] bytes = new byte[(int) file.length()];
        datais.readFully(bytes);
        datais.close();
        String base64file = new String(bytes);
        return Base64Coder.decodeLines(base64file);
    }

    /**
     * Crea un file a partire dai bytes passati
     *
     * @param fileToCreate il file da creare
     * @param bytes i bytes del file da creare
     * @throws java.io.FileNotFoundException
     */
    public static void writeFileFromBytes(String fileToCreate, byte[] bytes) throws FileNotFoundException, IOException {
        DataOutputStream dataos = null;
        try {
            dataos = new DataOutputStream(new FileOutputStream(fileToCreate));
            dataos.write(bytes);
        } finally {
            try {
                dataos.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Crea un file contente il testo passato
     *
     * @param fileToCreate il file da creare
     * @param text il testo da scrivere nel file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void stringToFile(String fileToCreate, String text) throws FileNotFoundException, IOException {
        writeFileFromBytes(fileToCreate, text.getBytes());
    }

    public static String sendHttpMessage(String targetUrl, String username, String password, Map<String, String> parameters, String method) throws MalformedURLException, IOException, SendHttpMessageException {

        //System.out.println("connessione...");
        String parametersToSend = "";
        if (parameters != null) {
            Set<Entry<String, String>> entrySet = parameters.entrySet();
            Iterator<Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Entry<String, String> param = iterator.next();
                String paramName = param.getKey();
                String paramValue = param.getValue();
                parametersToSend += paramName + "=" + paramValue;
                if (iterator.hasNext()) {
                    parametersToSend += "&";
                }
            }
            parametersToSend = parametersToSend.replace(" ", "%20");
        }
        URL url = new URL(targetUrl);
        method = method.toUpperCase();
        if (method.equals("GET") || method.equals("DELETE")) {
            if (parametersToSend.length() > 0) {
                targetUrl += "?" + parametersToSend;
            }
            url = new URL(targetUrl);
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (username != null && !username.equals("")) {
            String userpassword = null;
            if (password != null) {
                userpassword = username + ":" + password;
            } else {
                userpassword = "la password";
            }
            String encodedAuthorization = Base64Coder.encodeString(userpassword);
            connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
        }

        if (method.equals("POST")) {
            connection.setDoOutput(true);
        }
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        if (method.equals("POST")) {
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parametersToSend.getBytes().length));
        }
        connection.setUseCaches(false);

        if (method.equals("POST")) {
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parametersToSend);
            wr.flush();
            wr.close();
        }

        InputStream resultStream = connection.getInputStream();

        int responseCode = connection.getResponseCode();
        //System.out.println("risposta: " + responseCode + " - " + connection.getResponseMessage());

        String responseCodeToString = String.valueOf(responseCode);

        String resultString = null;
        if (resultStream != null) {
            resultString = inputStreamToString(resultStream);
        }
        if (!responseCodeToString.substring(0, responseCodeToString.length() - 1).equals("20")) {
            throw new SendHttpMessageException(responseCode, resultString);
        }
        IOUtils.closeQuietly(resultStream);
        connection.disconnect();
        return resultString;
    }

    /**
     *
     * @param targetUrl
     * @param parts
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws SendHttpMessageException
     */
    public static String sendHttpMultipartMessage(String targetUrl, List<MultipartObject> parts) throws IOException, SendHttpMessageException {

        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (MultipartObject part : parts) {
            entity.addPart(part.getPartName(), part.getBody());
        }

        HttpPost httpPost = new HttpPost(targetUrl);
        httpPost.setEntity(entity);
        HttpClient http = new DefaultHttpClient();
        HttpResponse response = http.execute(httpPost);
        int responseCode = response.getStatusLine().getStatusCode();
        String responseCodeToString = String.valueOf(responseCode);

        HttpEntity result = response.getEntity();
        InputStream content = result.getContent();
        String res = null;
        if (content != null) {
            res = inputStreamToString(result.getContent());
        }
        if (!responseCodeToString.substring(0, responseCodeToString.length() - 1).equals("20")) {
            throw new SendHttpMessageException(responseCode, res);
        }

        IOUtils.closeQuietly(content);
        return res;
    }

    public static String sendHttpMessage(String targetUrl, String username, String password, Map<String, byte[]> parameters, String method, String contentType) throws MalformedURLException, IOException, SendHttpMessageException {
//        parameters.entrySet().stream().forEach(entry -> {System.out.println(entry + " - " + new String(entry.getValue()));});
        if (contentType == null || contentType.equals("")) {
            contentType = "application/x-www-form-urlencoded";
        }
//        System.out.println("connessione...");
        String textParameters = "";
        byte[] byteParameters = null;
        int contentLength = 0;
        if (parameters != null) {
            if (contentType.equals("application/x-www-form-urlencoded")) {
                Set<Map.Entry<String, byte[]>> entrySet = parameters.entrySet();
                Iterator<Map.Entry<String, byte[]>> iterator = entrySet.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, byte[]> param = iterator.next();
                    String paramName = param.getKey();
                    String paramValue = new String(param.getValue());
                    textParameters += paramName + "=" + paramValue;
                    if (iterator.hasNext()) {
                        textParameters += "&";
                    }
                }
                textParameters = textParameters.replace(" ", "%20");
                contentLength = textParameters.getBytes().length;
            } else {
                byteParameters = parameters.values().iterator().next();
                contentLength = byteParameters.length;
            }
        }
        URL url = new URL(targetUrl);
        method = method.toUpperCase();
        if (method.equals("GET") || method.equals("DELETE")) {
            if (textParameters.length() > 0) {
                targetUrl += "?" + textParameters;
            }
            url = new URL(targetUrl);
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (username != null && !username.equals("")) {
            String userpassword;
            if (password != null) {
                userpassword = username + ":" + password;
            } else {
                userpassword = " la password";
            }
            String encodedAuthorization = Base64Coder.encodeString(userpassword);
            connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
        }

        if (method.equals("POST")) {
            connection.setDoOutput(true);
        }
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", contentType);
        if (contentType.equals("application/x-www-form-urlencoded")) {
            connection.setRequestProperty("charset", "utf-8");
        }
        if (method.equals("POST")) {
            connection.setRequestProperty("Content-Length", "" + Integer.toString(contentLength));
        }
        connection.setUseCaches(false);

        if (method.equals("POST")) {
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            if (!textParameters.equals("")) {
                wr.writeBytes(textParameters);
                wr.flush();
                wr.close();
            } else {
                wr.write(byteParameters);
            }
        }

        int responseCode = connection.getResponseCode();
        String responseCodeToString = String.valueOf(responseCode);
        if (!responseCodeToString.substring(0, responseCodeToString.length() - 1).equals("20")) {
            String error = inputStreamToString(connection.getErrorStream());
//            System.out.println("error: " + error);
            throw new SendHttpMessageException(responseCode, error);
        }

        InputStream resultStream = connection.getInputStream();
//        System.out.println("risposta: " + responseCode + " - " + connection.getResponseMessage());

        String resultString = null;
        if (resultStream != null) {
            resultString = inputStreamToString(resultStream);
        }

        IOUtils.closeQuietly(resultStream);
        connection.disconnect();
        return resultString;
    }

    /**
     * Legge un file di testo e ritorna il suo contenuto in una stringa
     *
     * @param filePath il file da leggere
     * @return una stringa contenente il contenuto del file passato
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String fileToString(String filePath) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileInputStream fis = new FileInputStream(filePath);
        String inputStreamToString = inputStreamToString(fis);
        fis.close();
        return inputStreamToString;
    }

    /**
     * Converte un InputStream in una stringa
     *
     * @param is l'InputStream da convertire
     * @return L'inputStream convertito in stringa
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String inputStreamToString(InputStream is) throws UnsupportedEncodingException, IOException {
        Writer stringWriter = new StringWriter();
        Reader reader = null;
        char[] buffer = new char[1024];
        String stringToReturn;
        try {
            reader = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                stringWriter.write(buffer, 0, n);
            }
            stringToReturn = stringWriter.toString();
        } finally {
            stringWriter.close();
            reader.close();
        }
        return stringToReturn;
    }

    /**
     * Torna un inputStream della stringa passata
     *
     * @param str
     * @return
     */
    public static InputStream stringToInputStream(String str) {
        try {
            InputStream is = new ByteArrayInputStream(str.getBytes(Charsets.UTF_8));
            return is;
        } catch (Exception ex) {
            //ex.printStackTrace(System.out);
            return null;
        }
    }

//    /** Scrive un InputStream in un file
//     *
//     * @param inputStream l'InpurStream da scrivere
//     * @param fileToCreate il file da creare
//     * @throws FileNotFoundException
//     * @throws IOException
//     */
//    public static void inputStreamToFile(MyInputStream inputStream, String fileToCreate) throws FileNotFoundException, IOException {
//        OutputStream os = new FileOutputStream(fileToCreate);
//        byte[] buffer = new byte[1024];
//        int bytesRead;
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            os.write(buffer, 0, bytesRead);
//        }
//        os.close();
//    }
    /**
     * Scrive un InputStream in un file
     *
     * @param inputStream l'InpurStream da scrivere
     * @param fileToCreate il file da creare
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void inputStreamToFile(InputStream inputStream, String fileToCreate) throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(fileToCreate);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
    }

    public static HttpCallResponse httpCall(String url, String httpMethodOverride, String contentType, byte[] body, int timeoutSec) throws IOException {

        System.out.println(timeoutSec * 1000);
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        OkHttpClient client = clientBuilder
                .readTimeout(timeoutSec, TimeUnit.SECONDS)
                .build();

        okhttp3.MediaType mediaType = okhttp3.MediaType.parse(String.format("%s; charset=utf-8", contentType));

        RequestBody requestBody = RequestBody.create(mediaType, body);
        Request.Builder requestBuilder = new Request.Builder();

        //requestBuilder.addHeader("Content-Type", contentType)
        requestBuilder.url(url)
                .post(requestBody);

        if (StringUtils.hasText(httpMethodOverride)) {
            requestBuilder.addHeader("X-HTTP-Method-Override", httpMethodOverride);
        }

        Request request = requestBuilder.build();
        okhttp3.Response response = client.newCall(request).execute();
        return new HttpCallResponse(response);
    }

    public static HttpCallResponse httpCallWithHeaders(String url, Map<String, Object> queryParams, Map<String, String> headers, String contentType, byte[] body, int timeoutSec, String requestMethod) throws IOException {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        OkHttpClient client = clientBuilder
                .readTimeout(timeoutSec, TimeUnit.SECONDS)
                .build();

        okhttp3.MediaType mediaType = okhttp3.MediaType.parse(String.format("%s; charset=utf-8", contentType));

        Request.Builder requestBuilder = new Request.Builder();

        if (headers != null) {
            headers.entrySet().stream().forEach((entry) -> {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            });
        }

        StringBuilder urlBuilder = new StringBuilder(url);
        if (queryParams != null && !queryParams.isEmpty()) {
            urlBuilder.append("?");
            queryParams.forEach((k, v) -> {
                try {
                    if (List.class.isAssignableFrom(v.getClass())) {
                        List valueList = (List) v;
                        valueList.stream().forEach(vl -> {
                            try {
                                urlBuilder.append(k).append("=").append(URLEncoder.encode(vl.toString(), "UTF-8")).append("&");
                            } catch (UnsupportedEncodingException ex) {
                            }
                        });
                    } else {
                        urlBuilder.append(k).append("=").append(URLEncoder.encode(v.toString(), "UTF-8")).append("&");
                    }
                } catch (UnsupportedEncodingException ex) {
                }
            });
        }

        String finalUrl = urlBuilder.toString();
        if (finalUrl.endsWith("&")) {
            finalUrl = finalUrl.substring(0, finalUrl.length() - 1);
        }
        requestBuilder.url(finalUrl);

        switch (requestMethod.toUpperCase()) {
            case "POST":
                RequestBody requestBodyPost = RequestBody.create(mediaType, body);
                requestBuilder.post(requestBodyPost);
                break;
                
            case "PATCH":
                RequestBody requestBodyPatch = RequestBody.create(mediaType, body);
                requestBuilder.patch(requestBodyPatch);
                break;

            case "GET":
                requestBuilder.get();
                break;

            case "DELETE":
                if (body != null) {
                    RequestBody requestBodyDelete = RequestBody.create(mediaType, body);
                    requestBuilder.delete(requestBodyDelete);
                } else {
                    requestBuilder.delete();
                }
                break;
        }

        Request request = requestBuilder.build();
        okhttp3.Response response = client.newCall(request).execute();
        return new HttpCallResponse(response);
    }

    /**
     * indica se il file è un pdf
     *
     * @param file il file da controllare
     * @return "true" se il file è un pdf, "false" altrimenti
     * @throws java.io.IOException
     * @throws org.apache.tika.mime.MimeTypeException
     */
    public static boolean isPdf(File file) throws IOException, MimeTypeException {
        InputStream is = null;
        boolean isPdf = false;
        try {
            is = new FileInputStream(file);
            isPdf = isPdf(is);
            is.close();
        } finally {
            IOUtils.closeQuietly(is);
        }
        return isPdf;
    }

    /**
     * indica se il file è un pdf
     *
     * @param is l'InputStream del file da controllare
     * @return "true" se il file è un pdf, "false" altrimenti
     * @throws java.io.IOException
     * @throws org.apache.tika.mime.MimeTypeException
     */
    public static boolean isPdf(InputStream is) throws IOException, MimeTypeException {
        Detector detector = new Detector();
        String mimeType = detector.getMimeType(is);
        MediaType mediaType = MediaType.parse(mimeType);
        return mediaType == Detector.MEDIA_TYPE_APPLICATION_PDF;
    }

    /**
     * indica se il file è un p7m
     *
     * @param file il file da controllare
     * @return "true" se il file è un p7m, "false" altrimenti
     */
    public static boolean isP7m(File file) throws FileNotFoundException, IOException, MimeTypeException {
        Detector detector = new Detector();
        String mimeType = detector.getMimeType(file.getAbsolutePath());
        MediaType mediaType = MediaType.parse(mimeType);
        if (mediaType == Detector.MEDIA_TYPE_PKCS_7_MIME || mediaType == Detector.MEDIA_TYPE_PKCS_7_SIGNATURE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * indica se il file è un p7m
     *
     * @param mis l'InputStream del file da controllare
     * @return "true" se il file è un p7m, "false" altrimenti
     * @throws org.apache.tika.mime.MimeTypeException
     * @throws java.io.IOException
     */
    public static boolean isP7m(MyInputStream mis) throws MimeTypeException, IOException {
        Detector detector = new Detector();
        String mimeType = detector.getMimeType(mis);
        MediaType mediaType = MediaType.parse(mimeType);
        return mediaType == Detector.MEDIA_TYPE_PKCS_7_MIME || mediaType == Detector.MEDIA_TYPE_PKCS_7_SIGNATURE;
    }

    /**
     * restituisce un ArrayList contentente il nome dei campi firma, visibili e
     * non visibili, firmati (nel caso di pdf firmati) o i codici fiscali dei
     * firmatari (nel caso di p7m)
     *
     * @param file il file che si vuole analizzare
     * @return un ArrayList di stringe contenente i nomi dei campi firma/codici
     * fiscali
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     * @throws org.apache.tika.mime.MimeTypeException
     */
    public static ArrayList<String> getSignedFields(File file) throws FileNotFoundException, IOException, UnsupportedEncodingException, MimeTypeException {
        ArrayList<String> signsList = new ArrayList<String>();

        if (UtilityFunctions.isP7m(file)) {

            String sigName;
            SignerInformationStore signers;
            CertStore certs;
            ASN1InputStream asn1 = null;
            InputStream fis = null;
            FileInputStream newFis = null;
            try {
                fis = new FileInputStream(file);
                CMSSignedData cms;
                FileReader reader;
                PEMParser pemParser;
                try {
                    asn1 = new ASN1InputStream(fis);
                    cms = new CMSSignedData(asn1);
                } catch (CMSException ex) {
                    try {
                        IOUtils.closeQuietly(fis);
                        reader = new FileReader(file);
                        pemParser = new PEMParser(reader);
                        byte[] bytes = pemParser.readPemObject().getContent();
                        IOUtils.closeQuietly(reader);
                        IOUtils.closeQuietly(pemParser);
                        fis = new ByteArrayInputStream(bytes);
                    } catch (Exception subEx) {
                        IOUtils.closeQuietly(fis);
                        newFis = new FileInputStream(file);
                        CMSTimeStampedDataParser tsd = new CMSTimeStampedDataParser(newFis);
                        fis = tsd.getContent();
                    }
                    asn1 = new ASN1InputStream(fis);
                    cms = new CMSSignedData(asn1);
                }
//                certs = cms.getCertificatesAndCRLs("Collection", "BC");
                JcaCertStoreBuilder builder = new JcaCertStoreBuilder();
                certs = builder.addCertificates(cms.getCertificates()).build();
                signers = cms.getSignerInfos();
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                IOUtils.closeQuietly(asn1);
                return signsList;
            } finally {
                IOUtils.closeQuietly(asn1);
                IOUtils.closeQuietly(fis);
                IOUtils.closeQuietly(newFis);
            }

            Collection c = signers.getSigners();
            Iterator it = c.iterator();

            while (it.hasNext()) {

                X509Certificate signingCert = null;
                try {
                    SignerInformation signer = (SignerInformation) it.next();
                    Collection certCollection = certs.getCertificates(new JcaX509CertSelectorConverter().getCertSelector(signer.getSID()));
                    Iterator certIt = certCollection.iterator();
                    signingCert = (X509Certificate) certIt.next();

                    // estraggo il codice fiscale dal certificato e lo uso come identificativo della firma
                    X500Name subjectFields = new JcaX509CertificateHolder(signingCert).getSubject();
                    RDN sn = subjectFields.getRDNs(BCStyle.SN)[0];
                    String valueToString = IETFUtils.valueToString(sn.getFirst().getValue());
                    String[] snSplitted = valueToString.split(":");
                    String codiceFiscale = snSplitted[1];

                    sigName = codiceFiscale;
                } catch (Exception ex) {
                    continue;
                }
                signsList.add(sigName);
            }

            return signsList;
        } else if (UtilityFunctions.isPdf(file)) {
            PdfReader tempReader = null;
            // se il file e' un pdf
            try {
                tempReader = new PdfReader(file.getPath());

                // torna tutti i campi firma firmati (sia visibili che invisibili)
                signsList = tempReader.getAcroFields().getSignatureNames();
                return signsList;
            } finally {
                try {
                    tempReader.close();
                } catch (Exception e) {
                }
            }
        } else {
            return signsList;
        }
    }

    /**
     * restituisce un ArrayList contentente il nome dei campi firma, visibili e
     * non visibili, firmati (nel caso di pdf firmati) o i codici fiscali dei
     * firmatari (nel caso di p7m)
     *
     * @param is l'InputStream del file che si vuole analizzare
     * @return un ArrayList di stringe contenente i nomi dei campi firma/codici
     * fiscali
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws MimeTypeException
     */
    public static ArrayList<String> getSignedFields(InputStream is) throws IOException, FileNotFoundException, UnsupportedEncodingException, MimeTypeException {
        File tempFile = File.createTempFile("temp_getSignedField_", ".temp");
        copy(is, tempFile);
        IOUtils.closeQuietly(is);
        try {
            return getSignedFields(tempFile);
        } finally {
            tempFile.delete();
        }
    }

    /**
     * ritorna "true" se il file (pdf o p7m) ha almeno una firma, "false"
     * altrimenti
     *
     * @param file il file da controllare (pdf o p7m)
     * @return "true" se il file (pdf o p7m) ha almeno una firma, "false"
     * altrimenti
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     * @throws org.apache.tika.mime.MimeTypeException
     */
    public static boolean isSigned(File file) throws FileNotFoundException, IOException, UnsupportedEncodingException, MimeTypeException {
        return !getSignedFields(file).isEmpty();
    }

    /**
     * ritorna "true" se il file (pdf o p7m) ha almeno una firma, "false"
     * altrimenti
     *
     * @param is l'InputStream del file da controllare (pdf o p7m)
     * @return "true" se il file (pdf o p7m) ha almeno una firma, "false"
     * altrimenti
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     * @throws org.apache.tika.mime.MimeTypeException
     */
    public static boolean isSigned(InputStream is) throws FileNotFoundException, IOException, UnsupportedEncodingException, MimeTypeException {
        return !getSignedFields(is).isEmpty();
    }

    /**
     * Rimuove tutte le firme dal reader, per salvarlo occorre usare un oggetto
     * PDFStamper
     *
     * @param reader il reader dal quale rimuovere le firme
     */
    public static void removeSigns(PdfReader reader) {
        ArrayList<String> signatureNames = reader.getAcroFields().getSignatureNames();
        AcroFields acroFields = reader.getAcroFields();
        for (String signatureName : signatureNames) {
            acroFields.removeField(signatureName);
        }
    }

    /**
     * Rimuove tutte la firma dal reader, per salvarlo occorre usare un oggetto
     * PDFStamper
     *
     * @param reader il reader dal quale rimuovere le firme
     */
    public static void removeSign(PdfReader reader, String signName) {
        AcroFields acroFields = reader.getAcroFields();
        acroFields.removeField(signName);
    }

    /**
     * Restituisce l'elenco dei codici fiscali dei certificati con cui il
     * documento è stato firmato
     *
     * @param file il file da controllare
     * @return una mappa contenente l'elenco dei codici fiscali dei certificati
     * con cui il documento è stato firmato con la relativa ora di firma
     * @throws it.bologna.ausl.proctonutils.exceptions.UtilityFunctionException
     */
    public static Map<String, Object>[] getSigners(File file) throws UtilityFunctionException {

        TreeSet<HashMap<String, Object>> res = new TreeSet<>(new Comparator<HashMap<String, Object>>() {

            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                if (o1 == null || o2 == null) {
                    return 1;
                } else {
                    Calendar data1 = (Calendar) o1.get("dataFirma");
                    Calendar data2 = (Calendar) o2.get("dataFirma");
                    if (data1 == null || data2 == null) {
                        return 1;
                    } else {
                        return data1.compareTo(data2);
                    }
                }
            }
        });
        try {
            if (isP7m(file)) {
                SignerInformationStore signers = null;
                CertStore certs = null;
                InputStream fis = null;
                ASN1InputStream asn1 = null;
                FileInputStream newFis = null;
                try {
                    fis = new FileInputStream(file);
                    CMSSignedData cms;
                    FileReader reader;
                    PEMParser pemParser;
                    try {
                        asn1 = new ASN1InputStream(fis);
                        cms = new CMSSignedData(asn1);
                    } catch (CMSException ex) {
                        try {
                            IOUtils.closeQuietly(fis);
                            reader = new FileReader(file);
                            pemParser = new PEMParser(reader);
                            byte[] bytes = pemParser.readPemObject().getContent();
                            IOUtils.closeQuietly(reader);
                            IOUtils.closeQuietly(pemParser);
                            fis = new ByteArrayInputStream(bytes);
                        } catch (Exception subEx) {
                            IOUtils.closeQuietly(fis);
                            newFis = new FileInputStream(file);
                            CMSTimeStampedDataParser tsd = new CMSTimeStampedDataParser(newFis);
                            fis = tsd.getContent();
                        }
                        asn1 = new ASN1InputStream(fis);
                        cms = new CMSSignedData(asn1);
                    }
//                    certs = cms.getCertificatesAndCRLs("Collection", "BC");
                    JcaCertStoreBuilder builder = new JcaCertStoreBuilder();
                    certs = builder.addCertificates(cms.getCertificates()).build();
                    signers = cms.getSignerInfos();
                } finally {
                    IOUtils.closeQuietly(asn1);
                    IOUtils.closeQuietly(fis);
                    IOUtils.closeQuietly(newFis);
                }

                Collection c = signers.getSigners();
                Iterator it = c.iterator();

                while (it.hasNext()) {
                    SignerInformation signer = (SignerInformation) it.next();
                    Collection certCollection = certs.getCertificates(new JcaX509CertSelectorConverter().getCertSelector(signer.getSID()));
                    Iterator certIt = certCollection.iterator();
                    X509Certificate signingCertificate = (X509Certificate) certIt.next();
                    X500Name subjectFields = new JcaX509CertificateHolder(signingCertificate).getSubject();
                    RDN sn = subjectFields.getRDNs(BCStyle.SN)[0];
                    String snString = IETFUtils.valueToString(sn.getFirst().getValue());
//                    String[] snSplitted = snString.split(":");
//                    String codiceFiscale = snSplitted[1];
                    String codiceFiscale = snString.substring(snString.length() - 16, snString.length());

                    Calendar signDate = null;
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
                        signDate = Calendar.getInstance();
                        signDate.setTime(date);
                    }
                    HashMap<String, Object> vals = new HashMap<>();
                    vals.put("codiceFiscale", codiceFiscale);
                    vals.put("dataFirma", signDate);
                    res.add(vals);
                }
            } else if (isPdf(file)) {
                PdfReader reader = null;
                try {
                    reader = new PdfReader(file.getAbsolutePath());
                    List<String> signatureNames = reader.getAcroFields().getSignatureNames();
                    PdfPKCS7 pk = null;
                    AcroFields af = reader.getAcroFields();
                    for (String signatureName : signatureNames) {
                        pk = af.verifySignature(signatureName);
                        Calendar signDate = pk.getSignDate();

                        HashMap<String, Object> vals = new HashMap<>();
                        vals.put("dataFirma", signDate);

                        X509Certificate signingCertificate = pk.getSigningCertificate();
                        X500Name subjectFields = new JcaX509CertificateHolder(signingCertificate).getSubject();
                        RDN[] snArray = subjectFields.getRDNs(BCStyle.SN);
                        if (snArray.length != 0) {
                            RDN sn = snArray[0];
                            String snString = IETFUtils.valueToString(sn.getFirst().getValue());
                            //                        PdfPKCS7.X509Name subjectFields = PdfPKCS7.getSubjectFields(signingCertificate);

//                            String[] snSplitted = snString.split(":");
//                            String codiceFiscale = snSplitted[1];
                            String codiceFiscale;
                            if (snString.length() >= 16) {
                                codiceFiscale = snString.substring(snString.length() - 16, snString.length());
                            } else {
                                codiceFiscale = snString;
                            }
                            vals.put("codiceFiscale", codiceFiscale);
                        } else {
                            snArray = subjectFields.getRDNs(BCStyle.CN);
                            if (snArray.length != 0) {
                                RDN cn = snArray[0];
                                vals.put("codiceFiscale", IETFUtils.valueToString(cn.getFirst().getValue()));
                            } else {
                                vals.put("codiceFiscale", "unknown");
                            }
                        }

                        res.add(vals);

//                        String nomeCognome = subjectFields.getField("CN");
//                        String professione = subjectFields.getField("T");
                        //System.out.println(codiceFiscale + "\n" + nomeCognome + "\n" + professione + "\n" + signDate.toString());
                    }
                } finally {
                    try {
                        reader.close();
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (Exception ex) {
            throw new UtilityFunctionException(ex);
        }
        HashMap<String, Object>[] a = new HashMap[0];
        return res.toArray(a);
    }

    /**
     * Restituisce l'elenco dei codici fiscali dei certificati con cui il
     * documento è stato firmato
     *
     * @param is l'InputStream del file da controllare
     * @return una mappa contenente l'elenco dei codici fiscali dei certificati
     * con cui il documento è stato firmato con la relativa ora di firma
     * @throws UtilityFunctionException
     * @throws IOException
     */
    public static Map<String, Object>[] getSigners(InputStream is) throws UtilityFunctionException, IOException {
        File tempFile = File.createTempFile("temp_getSignedField_", ".temp");
        copy(is, tempFile);
        IOUtils.closeQuietly(is);
        try {
            return getSigners(tempFile);
        } finally {
            tempFile.delete();
        }
    }

    /**
     * Indica se un file pdf è protetto da password
     *
     * @param file da verificare
     * @return "true" sè il file è protetto da password, "false" altrimenti
     */
    public static boolean isProtectedPdf(File file) throws FileNotFoundException, IOException {
//       return isProtectedPdf(new MyInputStream(new FileInputStream(file)));
        PdfReader reader = null;
        boolean isProtectedPdf = false;
        try {
            reader = new PdfReader(file.getAbsolutePath());
            isProtectedPdf = reader.isEncrypted();
        } catch (BadPasswordException ex) {
            isProtectedPdf =  true;
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
            }
        }
        return isProtectedPdf;
    }

    /**
     * Indica se un file pdf è protetto da password
     *
     * @param is inputStram da verificare
     * @return "true" sè il file è protetto da password, "false" altrimenti
     * @throws java.io.IOException
     */
    public static boolean isProtectedPdf(InputStream is) throws IOException {
        PdfReader reader;
        try {
            reader = new PdfReader(is);
            return reader.isEncrypted();
        } catch (BadPasswordException ex) {
            return true;
        } finally {
            is.close();
        }
    }

    /**
     * Unisce due file pdf
     *
     * @param inputFile1 file 1
     * @param inputFile2 file 2
     * @param outputFile file da creare
     * @throws FileNotFoundException
     * @throws DocumentException
     * @throws IOException
     */
    public static void pdfMerger(String inputFile1, String inputFile2, String outputFile) throws FileNotFoundException, DocumentException, IOException {
        List<String> files = new ArrayList<String>();
        files.add(inputFile1);
        files.add(inputFile2);
        pdfMergerMultiFilesToFile(files, outputFile);
    }

    /**
     * Unisce n file pdf e crea un File con l'unione
     *
     * @param inputFiles files da unire
     * @param outputFile file da creare
     * @throws FileNotFoundException
     * @throws DocumentException
     * @throws IOException
     */
    public static void pdfMergerMultiFilesToFile(List<String> inputFiles, String outputFile) throws FileNotFoundException, IOException, DocumentException {
//        MyInputStream pdfMergerMultiToInputStream = pdfMergerMultiFilesToInputStream(inputFiles);
//        inputStreamToFile(pdfMergerMultiToInputStream, outputFile);
//        pdfMergerMultiToInputStream.close();

        FileInputStream fis = null;
        PdfReader reader = null;
        Document document = null;
        PdfCopy cp = null;
        FileOutputStream tempos = null;
        try {
            tempos = new FileOutputStream(outputFile);
            fis = new FileInputStream(inputFiles.get(0));
            reader = new PdfReader(fis);
            document = new Document(reader.getPageSizeWithRotation(1));
            cp = new PdfCopy(document, tempos);
            document.open();
            fis.close();
            for (String filePath : inputFiles) {
                fis = new FileInputStream(filePath);
                PdfReader r = new PdfReader(fis);
                for (int k = 1; k <= r.getNumberOfPages(); ++k) {
                    cp.addPage(cp.getImportedPage(r, k));
                }
                cp.freeReader(r);
            }
            cp.close();
            document.close();
        } finally {
            try {
                fis.close();
            } catch (Exception ex) {
            }
            try {
                reader.close();
            } catch (Exception ex) {
            }
            try {
                document.close();
            } catch (Exception ex) {
            }
            try {
                cp.close();
            } catch (Exception ex) {
            }
            try {
                tempos.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Unisce n file pdf e crea un File con l'unione
     *
     * @param inputFiles InputStream dei files da unire
     * @param outputFile file da creare
     * @throws FileNotFoundException
     * @throws DocumentException
     * @throws IOException
     */
    public static void pdfMergerMultiInputStreamsToFile(ArrayList<MyInputStream> inputFiles, String outputFile) throws DocumentException, IOException {
        MyInputStream pdfMergerMultiToInputStream = pdfMergerMultiInputStreamsToInputStream(inputFiles);
        inputStreamToFile(pdfMergerMultiToInputStream, outputFile);
        pdfMergerMultiToInputStream.realClose();
    }

    /**
     * Unisce n file stream di pdf e ritorna l'InputStream dell'unione
     *
     * @param inputFiles InputStream dei files da unire
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public static MyInputStream pdfMergerMultiInputStreamsToInputStream(ArrayList<MyInputStream> inputFiles) throws DocumentException, IOException {
        ByteArrayOutputStream tempos = new ByteArrayOutputStream();
        PdfCopyFields copy = new PdfCopyFields(tempos);
        for (MyInputStream is : inputFiles) {
            PdfReader inputReader = new PdfReader(is);
            removeSigns(inputReader);
            copy.addDocument(inputReader);
            is.close();
        }
        copy.close();
        return new MyInputStream(new ByteArrayInputStream(tempos.toByteArray()));
    }

    /**
     * Unisce n file pdf e ritorna l'InputStream dell'unione
     *
     * @param inputFiles files da unire
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public static MyInputStream pdfMergerMultiFilesToInputStream(ArrayList<String> inputFiles) throws DocumentException, IOException {
        ByteArrayOutputStream tempos = new ByteArrayOutputStream();
        PdfCopyFields copy = new PdfCopyFields(tempos);
        for (String inputFile : inputFiles) {
            PdfReader inputReader = new PdfReader(inputFile);
            removeSigns(inputReader);
            copy.addDocument(inputReader);
            inputReader.close();
        }
        copy.close();
        return new MyInputStream(new ByteArrayInputStream(tempos.toByteArray()));
    }

    /**
     * Restituisce una stringa rappresentante l'hash del file in esadecimale
     * secondo l'algoritmo specificato
     *
     * @param is input stream del file del quale calcolare l'hash
     * @param algorithmName il nome dell'algoritmo da utilizzare per l'hash (Es.
     * SHA1, SHA256, SHA512, MD5, ecc.)
     * @return una stringa rappresentante l'hash del file in esadecimale secondo
     * l'algoritmo specificato
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String getHashFromFile(InputStream is, String algorithmName) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        //    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        MessageDigest algorithm = MessageDigest.getInstance(algorithmName);
        DigestInputStream dis = new DigestInputStream(is, algorithm);

        byte[] buffer = new byte[8192];
        while ((dis.read(buffer)) != -1) {
        }
        dis.close();
        byte[] messageDigest = algorithm.digest();

        Formatter fmt = new Formatter();
        for (byte b : messageDigest) {
            fmt.format("%02X", b);
        }
        String hashString = fmt.toString();

//        BigInteger hashInt = new BigInteger(1, messageDigest);
//        String hashString = hashInt.toString(16);
//        int digestLength = algorithm.getDigestLength();
//        while (hashString.length() < digestLength * 2) {
//            hashString = "0" + hashString;
//        }
        return hashString;
    }

    /**
     * Restituisce una stringa rappresentante l'hash del file in esadecimale
     * secondo l'algoritmo specificato
     *
     * @param filePath il file del quale calcolare l'hash
     * @param algorithmName il nome dell'algoritmo da utilizzare per l'hash (Es.
     * SHA1, SHA256, SHA512, MD5, ecc.)
     * @return una stringa rappresentante l'hash del file in esadecimale secondo
     * l'algoritmo specificato
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String getHashFromFile(String filePath, String algorithmName) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        String hashFromFile = getHashFromFile(fileInputStream, algorithmName);
        fileInputStream.close();
        return hashFromFile;
    }

    /**
     * Restituisce una stringa rappresentante l'hash della string passata
     * secondo l'algoritmo specificato
     *
     * @param string la stringa del quale calcolare l'hash
     * @param algorithmName il nome dell'algoritmo da utilizzare per l'hash (Es.
     * SHA1, SHA256, SHA512, MD5, ecc.)
     * @return una stringa rappresentante l'hash del file in esadecimale secondo
     * l'algoritmo specificato
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String getHashFromString(String string, String algorithmName) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(string.getBytes("UTF8"));
        String hashFromFile = getHashFromFile(byteArrayInputStream, algorithmName);
        byteArrayInputStream.close();
        return hashFromFile;
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = is.read(buffer);
        while (read != -1) {
            out.write(buffer, 0, read);
            read = is.read(buffer);
        }
        byte[] toByteArray = out.toByteArray();
        out.close();
        return toByteArray;
    }

    /* pulisce la stringa contenente caratteri HTML come &grave; ecc.. e ritorna la Stringa con i caratteri codificati
    *  (funzione utilizzata perchè HTMLDecode non dava alcun effetto)
     */
    public static String getClearStringFromHTMLString(String string) {

        return Jsoup.parse(string).text();

    }

    /**
     * Stampa un pdf
     *
     * @param filename il percorso completo del pdf da stampare
     * @throws FileNotFoundException
     * @throws PrintException
     */
    public static void printPdf(String filename) throws FileNotFoundException, PrintException {
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        // DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        //PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
        //PrintService[] printServices = PrinterJob.lookupPrintServices();
        //PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        //PrintService service = ServiceUI.printDialog(null, 200, 200, printServices, defaultService, flavor, pras);
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        boolean printConfirmed = printerJob.printDialog();
        PrintService service = printerJob.getPrintService();
        if (printConfirmed && service != null) {
            DocPrintJob job = service.createPrintJob();
            FileInputStream fis = new FileInputStream(filename);
            DocAttributeSet das = new HashDocAttributeSet();
            Doc doc = new SimpleDoc(fis, flavor, das);
//            Doc doc = new SimpleDoc(fis, flavor, das);
            job.print(doc, pras);
            //Thread.sleep(10000);
        }
    }

    /**
     * restituisce l'estensione di un file (prendendolo dal nome, non centra
     * niente con il mimeType)
     *
     * @param f file
     * @return l'estensione del file
     */
    public static String getFileExtension(File f) {
        String ext = "";
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1);
        }
        return ext;
    }

    /**
     * restituisce l'estensione il nome di un file senza l'estensione
     * (prendendolo dal nome, non centra niente con il mimeType)
     *
     * @param f file
     * @return il nome del file senza l'estensione
     */
    public static String getFileNameNoExtension(File f) {
        String fileName = f.getName();
        String ext = getFileExtension(f);
        return fileName.replace("." + ext, "");
    }

    /**
     * converte un pdf in swf utilizzando il convertitore "pdf2swf" di SWFTools
     *
     * @param pdfPath path del pdf da convertire
     * @param pdfToswfExecutablePath path dell'eseguibile "pdf2swf"
     * @return il path dell'swf creato
     * @throws IOException
     * @throws InterruptedException
     */
    public static String pdfToSwf(String pdfPath, String pdfToswfExecutablePath) throws IOException, InterruptedException {
        File pdfFile = new File(pdfPath);
        String fileNameNoExt = getFileNameNoExtension(pdfFile);
        File swfFile = new File(pdfFile.getParent(), fileNameNoExt + ".swf");
        String[] argArray = new String[]{pdfToswfExecutablePath, pdfFile.getAbsolutePath(), "-o", swfFile.getAbsolutePath(), "-G", "-f", "-T", "9", "-t", "-s", "storeallcharacters"};
        String errStream = executeCommand(argArray);
        return swfFile.getAbsolutePath();
    }

    /**
     * esegue un comando dal sistema operativo
     *
     * @param command comando da eseguire
     * @return l'eventuale errorStream del comando lanciato
     * @throws IOException
     * @throws InterruptedException
     */
    public static String executeCommand(String[] command) throws IOException, InterruptedException {
//        Process process = Runtime.getRuntime().exec("/bin/systemctl restart masterchef");
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        return inputStreamToString(process.getErrorStream());
    }

//    public static void provastampaconIcePDF(String filePath) throws PDFException, PDFSecurityException, IOException, PrintException {
//    Document pdf = new Document();
//    pdf.setFile(filePath);
//    SwingController sc = new SwingController();
//    DocumentViewController vc = new DocumentViewControllerImpl(sc);
//    vc.setDocument(pdf);
//    // create a new print helper with a specified paper size and print
//    // quality
//
//    PrinterJob printerJob = PrinterJob.getPrinterJob();
//        boolean printConfirmed = printerJob.printDialog();
//        PrintService service = printerJob.getPrintService();
//
//    PrintHelper printHelper = new PrintHelper(vc, pdf.getPageTree(),
//            MediaSizeName.NA_LEGAL, PrintQuality.DRAFT);
//    // try and print pages 1 - 10, 1 copy, scale to fit paper.
//    printHelper.setupPrintService(service, 0, 9, 1, true);
//    // print the document
//    printHelper.print();
//    }
    // Returns the contents of the file in a byte array.
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    /**
     * Controlla se la stringa passata contiene caratteri che potrebbero causare
     * problemi con le RegEx o al salvataggio del repository.
     *
     * @param fileName E' il nome del file (con estensione)
     * @return "true" se la stringa contiene uno dei caratteri non validi.
     */
    public static boolean isFileNameWithInvalidCharacter(String fileName) {
        String[] invalidCharachters = new String[]{"+", "*", "^"};
        for (int i = 0; i < invalidCharachters.length; i++) {
            if (fileName.contains(invalidCharachters[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Controlla se la stringa passata è un nome file valido
     *
     * @param fileName la stringa da controllare
     * @return "true" se la stringa passata è un nome file valido, "false"
     * altrimenti
     */
    public static boolean isValidFileName(String fileName) {

        Pattern pattern = Pattern.compile(
                "# Match a valid Windows filename (unspecified file system).          \n"
                + "^                                # Anchor to start of string.        \n"
                + "(?!                              # Assert filename is not: CON, PRN, \n"
                + "  (?:                            # AUX, NUL, COM1, COM2, COM3, COM4, \n"
                + "    CON|PRN|AUX|NUL|             # COM5, COM6, COM7, COM8, COM9,     \n"
                + "    COM[1-9]|LPT[1-9]            # LPT1, LPT2, LPT3, LPT4, LPT5,     \n"
                + "  )                              # LPT6, LPT7, LPT8, and LPT9...     \n"
                + "  (?:\\.[^.]*)?                  # followed by optional extension    \n"
                + "  $                              # and end of string                 \n"
                + ")                                # End negative lookahead assertion. \n"
                + "[^'&()\\[\\]{}<>:\"/\\\\|?*\\x00-\\x1F]*     # Zero or more valid filename chars.\n"
                + "[^&()\\[\\]{}<>:\"/\\\\|?*\\x00-\\x1F\\ .]  # Last char is not a space or dot.  \n"
                + "$                                # Anchor to end of string.            ",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
        Matcher matcher = pattern.matcher(fileName);
        boolean valid = matcher.matches();
        return valid;
    }

    /**
     * Verifica la corretteza di un indirizzo email
     *
     * @param email la mail da verificare
     * @return "true" se l'indirizzo è corretto, "false" altrimenti
     */
    public static boolean isValidEmail(String email) {
        // Create the Pattern using the regex
        Pattern p = Pattern.compile("^[a-zA-Z0-9_+\'&.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");

        // Match the given string with the pattern
        Matcher m = p.matcher(email);

        // check whether match is found
        boolean matchFound = m.matches();

        String[] emailSplitted = email.split("\\.");
        String lastToken = emailSplitted[emailSplitted.length - 1];

        // validate the country code
        if (matchFound && lastToken.length() >= 2
                && email.length() - 1 != lastToken.length()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Cancella una directory e tutto il suo contenuto
     *
     * @param dir la directory da eliminare
     */
    public static void deepDeleteDirectory(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deepDeleteDirectory(file);
                } else {
                    file.delete();
                }
            }
            dir.delete();
        }
    }

    /**
     * crea un pdf contenente il testo e l'immagine passati. Da usare per la
     * creazione di un pdf per gli allegati non convertibili da inserire nella
     * stampa unica
     *
     * @param pdfPath il file pdf da creare
     * @param imagePath l'immagine da inserire (verrà aggiunta al centro della
     * pagina)
     * @param text il testo da inserire (verrà aggiunto in alto nella pagina)
     * @throws DocumentException
     * @throws FileNotFoundException
     * @throws BadElementException
     * @throws MalformedURLException
     * @throws IOException
     */
    public static void createNotConvertedAttachmentPdf(String pdfPath, String imagePath, String fontFilePath, String iccProfileFilePath, String text) throws DocumentException, FileNotFoundException, BadElementException, MalformedURLException, IOException {

        FontFactory.register(fontFilePath);

        Font fontTest = FontFactory.getFont("Arial", BaseFont.WINANSI, BaseFont.EMBEDDED, 22);
        BaseFont timesbd = fontTest.getBaseFont();
        Font BOLD = new Font(timesbd, 22);
        Font NORMAL = new Font(timesbd, 22);

        // creo il documento che rappresenta pdf
        Document document = new Document();
        FileOutputStream fileOutputStream = new FileOutputStream(pdfPath);
        PdfAWriter writer = PdfAWriter.getInstance(document, fileOutputStream, PdfAConformanceLevel.PDF_A_1A);
        document.open();

//        BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
//        Font font = new Font(helvetica, 22, Font.NORMAL);
        // creo un paragrafo per inserire il testo
//        Paragraph p = new Paragraph(text, FontFactory.getFont("ARIAL", BaseFont.WINANSI, BaseFont.EMBEDDED, 22));
        Paragraph p = new Paragraph(text, fontTest);

        p.setAlignment(Element.ALIGN_CENTER);
        p.setLeading(1.0f, 1.0f);

        // aggiungo il paragrafo al documento
        document.add(p);

        Rectangle dim = document.getPageSize();

        // istanzio un'immagine con l'immagine da aggiungere con (com.itextpdf.text.Image)
        Image img = Image.getInstance(imagePath);

        // imposto la posizione dell'immagine
        img.setAbsolutePosition((dim.getLeft() + img.getWidth() + 50) / 2, (dim.getTop() - img.getHeight()) / 2);

        // aggiungo l'immagine al documento
        document.add(img);

        PdfDictionary structureTreeRoot = new PdfDictionary();
        structureTreeRoot.put(PdfName.TYPE, PdfName.STRUCTTREEROOT);
        writer.getExtraCatalog().put(PdfName.STRUCTTREEROOT, structureTreeRoot);

        PdfDictionary markInfo = new PdfDictionary(PdfName.MARKINFO);
        markInfo.put(PdfName.MARKED, new PdfBoolean(true));
        writer.getExtraCatalog().put(PdfName.MARKINFO, markInfo);

        PdfDictionary l = new PdfDictionary(PdfName.LANG);
        l.put(PdfName.LANG, new PdfBoolean("true"));
        writer.getExtraCatalog().put(PdfName.LANG, l);

        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream(iccProfileFilePath));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        writer.createXmpMetadata();

        document.close();
        writer.close();
        fileOutputStream.close();
    }

    /**
     * torna la differenza tra due Liste
     *
     * @param list1
     * @param list2
     * @return
     */
    public static <T> List<T> subtractList(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<T>();
        Set<T> set2 = new HashSet<T>(list2);
        for (T t1 : list1) {
            if (!set2.contains(t1)) {
                result.add(t1);
            }
        }
        return result;
    }

    public static String fixXhtml(String text) {
        Tidy a = new Tidy();
        StringReader rd = new StringReader(text);
        StringWriter w = new StringWriter();
        a.setPrintBodyOnly(true);
        a.setXHTML(true);
        a.parse(rd, w);
        String stringToReturn = w.toString();
        try {
            w.close();
        } catch (IOException ex) {
            Logger.getLogger(UtilityFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringToReturn;
    }

    /**
     * unisce più pdf/a e mantiene il formato
     *
     * @param inputFiles elenco di InputStream da unire
     * @param outputFile il file pdf da creare. Se null scrive in un
     * ByteArrayOutputStream
     * @param keywords keywords da aggiundegere al pdf (separate da ",")
     * @return OutputStream del file/bytes risultante
     * @throws DocumentException
     * @throws IOException
     */
    private static OutputStream _mergePdfA(ArrayList<InputStream> inputFiles, File outputFile, String ICCFilePath, String keywords) throws DocumentException, IOException, XMPException {

        // PERCHE' QUESTO NON VIENE CHIUSO???
        InputStream profile = new FileInputStream(new File(ICCFilePath));       
//        UtilityFunctions.class.getResourceAsStream(ICCFilePath);
        System.out.println("aperto inputStream profile");
        List<File> inputTempFiles = new ArrayList<>();

        String currentFilesKeywords = "";
        if (keywords != null && !keywords.isEmpty()) {
            FileOutputStream fileOutputStream;
            for (InputStream inputFile : inputFiles) {
                File tempFile = Files.createTempFile("_mergePdfA_" + UUID.randomUUID().toString(), null).toFile();
                fileOutputStream = new FileOutputStream(tempFile);
                IOUtils.copy(inputFile, fileOutputStream);
                inputFile.close();
                fileOutputStream.close();
                inputTempFiles.add(tempFile);
                PdfReader reader = null;
                try {
                    reader = new PdfReader(tempFile.getAbsolutePath());
                    String currentFileKeywords = reader.getInfo().get(PdfProperties.KEYWORDS);
                    if (currentFileKeywords != null && !currentFileKeywords.isEmpty()) {
                        currentFilesKeywords += "," + currentFileKeywords;
                    }
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                    IOUtils.closeQuietly(inputFile);
                    IOUtils.closeQuietly(fileOutputStream);
                }
            }
            currentFilesKeywords += "," + keywords;
            if (currentFilesKeywords.startsWith(",")) {
                currentFilesKeywords = currentFilesKeywords.substring(1);
            }
        }
        System.out.println("currentFilesKeywords: " + currentFilesKeywords);

        File file = null;
        PdfReader reader = null;
        Document document = null;
        PdfAWriter writer = null;
        OutputStream out;
        if (outputFile == null) {
            out = new ByteArrayOutputStream();
        } else {
            out = new FileOutputStream(outputFile);
        }
        try {
            file = inputTempFiles.get(0);
            reader = new PdfReader(file.getAbsolutePath());
            document = new Document();
            writer = PdfAWriter.getInstance(document, out, PdfAConformanceLevel.PDF_A_1A);
            writer.setPdfVersion(PdfWriter.PDF_VERSION_1_4);

            if (!currentFilesKeywords.isEmpty()) {
                document.addKeywords(currentFilesKeywords);
            }

            document.open();
//            document.add(new Header("Test", "gdm"));

            PdfDictionary structureTreeRoot = new PdfDictionary();
            structureTreeRoot.put(PdfName.TYPE, PdfName.STRUCTTREEROOT);
            writer.getExtraCatalog().put(PdfName.STRUCTTREEROOT, structureTreeRoot);
            PdfDictionary markInfo = new PdfDictionary(PdfName.MARKINFO);
            markInfo.put(PdfName.MARKED, new PdfBoolean("true"));
            writer.getExtraCatalog().put(PdfName.MARKINFO, markInfo);
            PdfDictionary l = new PdfDictionary(PdfName.LANG);
            l.put(PdfName.LANG, new PdfBoolean("true"));
            writer.getExtraCatalog().put(PdfName.LANG, l);

            ICC_Profile icc = ICC_Profile.getInstance(profile);
            writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
            writer.createXmpMetadata();
            for (int i = 0; i < inputTempFiles.size(); i++) {

                try {
                    if (i > 0) {
                        file = inputTempFiles.get(i);
                        reader = new PdfReader(file.getAbsolutePath());
                    }
                    removeSigns(reader);
                    PdfImportedPage page;
                    Rectangle pageSize;
                    PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data
                    for (int k = 1; k <= reader.getNumberOfPages(); k++) {
                        pageSize = reader.getPageSize(k);
                        int rot = reader.getPageRotation(k);
                        document.setPageSize(pageSize);
                        document.newPage();
                        writer.addPageDictEntry(PdfName.ROTATE, new PdfNumber(rot));
                        page = writer.getImportedPage(reader, k);
                        cb.addTemplate(page, 0, 0);
                    }
                } finally {
                    writer.freeReader(reader);
                    reader.close();
                    file.delete();
                }
            }
            document.close();

        } catch (Throwable tf) {
            System.out.println("aslasjkldsaklasdlaja porcatroiaccia");
            tf.printStackTrace();
            throw tf;

        } finally {
            try {
                document.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                writer.close();
            } catch (Exception ex) {
            }
            try {
                reader.close();
            } catch (Exception ex) {
            }
            if (file != null) {
                file.delete();
            }
        }
        return out;
    }

    /**
     * unisce più pdf/a e mantiene il formato
     *
     * @param inputFiles elenco di File da unire
     * @param keywords da aggiungere al pdf (separate da ",")
     * @return bytes del pdf risultante
     * @throws DocumentException
     * @throws IOException
     */
    public static byte[] mergePdfAFilesToByte(ArrayList<String> inputFiles, String ICCFilePath, String keywords) throws DocumentException, IOException, XMPException {
        ArrayList<InputStream> inputFilesStream = new ArrayList<>();
        for (String file : inputFiles) {
            inputFilesStream.add(new FileInputStream(file));
        }
        return ((ByteArrayOutputStream) _mergePdfA(inputFilesStream, null, ICCFilePath, keywords)).toByteArray();
    }

    /**
     * unisce più pdf/a e mantiene il formato
     *
     * @param inputFiles elenco di File da unire
     * @param outputFile file da creare
     * @param keywords keywords da aggiundere al pdf (separate da ",")
     * @throws DocumentException
     * @throws IOException
     */
    public static void mergePdfAFilesToFile(ArrayList<String> inputFiles, File outputFile, String ICCFilePath, String keywords) throws DocumentException, IOException, XMPException {
        ArrayList<InputStream> inputFilesStream = new ArrayList<>();
        for (String file : inputFiles) {
            inputFilesStream.add(new FileInputStream(file));
        }
        _mergePdfA(inputFilesStream, outputFile, ICCFilePath, keywords);
    }

    /**
     * unisce più pdf/a e mantiene il formato
     *
     * @param inputFiles elenco Stream da unire
     * @param keywords da aggiungere al pdf (separate da ",")
     * @return bytes del pdf risultante
     * @throws DocumentException
     * @throws IOException
     */
    public static byte[] mergePdfAStreamsToByte(ArrayList<InputStream> inputFiles, String ICCFilePath, String keywords) throws DocumentException, IOException, XMPException {
        return ((ByteArrayOutputStream) _mergePdfA(inputFiles, null, ICCFilePath, keywords)).toByteArray();
    }

    /**
     * unisce più pdf/a e mantiene il formato
     *
     * @param inputFiles elenco Stream da unire
     * @param outputFile file da creare
     * @param keywords da aggiungere al pdf (separate da ",")
     * @throws DocumentException
     * @throws IOException
     */
    public static void mergePdfAStreamsToFile(ArrayList<InputStream> inputFiles, File outputFile, String ICCFilePath, String keywords) throws DocumentException, IOException, XMPException {
        _mergePdfA(inputFiles, outputFile, ICCFilePath, keywords);
    }

    /**
     * unisce più pdf/a e mantiene il formato
     *
     * @param inputFiles elenco array di byte da unire
     * @param keywords da aggiungere al pdf (separate da ",")
     * @return i bytes del file creato
     * @throws DocumentException
     * @throws IOException
     */
    public static byte[] mergePdfABytesToBytes(ArrayList<byte[]> inputFiles, String ICCFilePath, String keywords) throws DocumentException, IOException, XMPException {
        ArrayList<InputStream> streams = new ArrayList<>();
        for (byte[] bytes : inputFiles) {
            streams.add(new ByteArrayInputStream(bytes));
        }
        return ((ByteArrayOutputStream) _mergePdfA(streams, null, ICCFilePath, null)).toByteArray();
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int readCount = in.read(buffer);
            if (readCount < 0) {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

    private static void copy(File file, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            copy(in, out);
        } finally {
            in.close();
        }
    }

    private static void copy(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
            copy(in, out);
        } finally {
            out.close();
        }
    }

    public static void createZip(String directoryInput, String zipFileOutput) throws IOException {

        File directory = new File(directoryInput);
        File zipFile = new File(zipFileOutput);

        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<File>();
        queue.push(directory);
        OutputStream out = new FileOutputStream(zipFile);
        Closeable res = out;
        try {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File kid : directory.listFiles()) {

                    String name = base.relativize(kid.toURI()).getPath();
                    if (name == null ? zipFile.getName() == null : name.equals(zipFile.getName())) {
                        //System.out.println("TROVATO: " + name);
                        continue;
                    }

                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = name.endsWith("/") ? name : name + "/";
                        System.out.println("Directory: " + name);
                        zout.putNextEntry(new ZipEntry(name));
                    } else {
                        System.out.println("File: " + name);
                        zout.putNextEntry(new ZipEntry(name));
                        copy(kid, zout);
                        zout.closeEntry();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            //directory.delete();
            //zipFile.delete();
            res.close();
        }

//                /* Finish addition of entries to the file */
//                logical_zip.finish();
//                /* Close output stream, our files are zipped */
//                zip_output.close();
//                /* Create Output Stream that will have final zip files */
//                OutputStream zip_output = new FileOutputStream(zipFile);
//                /* Create Archive Output Stream that attaches File Output Stream / and specifies type of compression */
//                ArchiveOutputStream logical_zip = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zip_output);
//                /* Create Archieve entry - write header information*/
//                ZipArchiveEntry archiveEntry = new ZipArchiveEntry("cartella_1");
//                ZipEntry zipEntry =  new ZipEntry("cartella_1");
//
//                logical_zip.putArchiveEntry(new ZipArchiveEntry(directory, "nome"));
////                logical_zip.putArchiveEntry(new ZipArchiveEntry("test_file_1.xml"));
////                /* Copy input file */
//                IOUtils.copy(new FileInputStream(directory), logical_zip);
////                /* Close Archieve entry, write trailer information */
////                logical_zip.closeArchiveEntry();
////                /* Repeat steps for file - 2 */
////                logical_zip.putArchiveEntry(new ZipArchiveEntry("test_file_2.xml"));
////                IOUtils.copy(new FileInputStream(new File("test_file_2.xml")), logical_zip);
//                logical_zip.closeArchiveEntry();
//                /* Finish addition of entries to the file */
//                logical_zip.finish();
//                /* Close output stream, our files are zipped */
//                zip_output.close();
    }

    /**
     * Ritorna un hash SHA-256 nel cosi' composto
     * [caratteri_casuali]$sha256([caratteri_casuali][password])
     *
     * @param password
     * @return hash generato
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getPasswordHash(String password) throws NoSuchAlgorithmException, IOException {

        SecureRandom random = new SecureRandom();
        String randomChars = new BigInteger(130, random).toString(32);

        //randomChars = randomChars.substring(0, 7);
        String hash = getHashFromString(randomChars + password, "SHA-256");
        return randomChars + "$" + hash;
    }

    /**
     *
     * @param password password da controllare
     * @param inhash hash da controllare (tipicamente letta da db)
     * @return true se la password macha con l'hash, false altrimenti
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws it.bologna.ausl.proctonutils.exceptions.UtilityFunctionException
     */
    public static boolean checkPasswordHash(String password, String inhash) throws NoSuchAlgorithmException, IOException, UtilityFunctionException {
        if (password == null || inhash == null) {
            throw new UtilityFunctionException("some argument is null!");
        }
        if (!inhash.contains("$")) {
            return false;
        }
        String randomChars = inhash.substring(0, inhash.indexOf("$"));
        String hash = inhash.substring(inhash.indexOf("$") + 1);
        return getHashFromString(randomChars + password, "SHA-256").equals(hash);
    }

    /**
     * Scarica un file
     *
     * @param downloadUrl
     * @param filePath
     * @throws MalformedURLException
     * @throws IOException
     */
    public static void downloadFile(String downloadUrl, String filePath) throws MalformedURLException, IOException {
        URL url = new URL(downloadUrl);

        InputStream inputStream = url.openStream();
        OutputStream fileOutputStream = new FileOutputStream(filePath);
        int length = -1;
        byte[] buffer = new byte[1024];// buffer for portion of data from connection
        try {
            while ((length = inputStream.read(buffer)) > -1) {
                fileOutputStream.write(buffer, 0, length);
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    public static String sendMail(String from, String to, String bcc, String host, String localHost, String port, String subject, String body, String fromsName) throws MessagingException, UnsupportedEncodingException {

        Properties properties = new Properties();
        properties.setProperty("mail.host", host);
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.localhost", localHost);
//        properties.setProperty("mail.smtp.localaddress", localHost);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.class", "com.sun.mail.smtp.SMTPTransport");
        Session session = Session.getInstance(properties, null);

        MimeMessage message = new MimeMessage(session);
        if (fromsName != null && !fromsName.isEmpty()) {
            message.setFrom(new InternetAddress(from, fromsName));
        } else {
            message.setFrom(new InternetAddress(from));
        }

//        Address address = new InternetAddress();
        if (to != null && !to.equals("")) {
            String[] toArray = to.split(";");
            for (String toElement : toArray) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toElement));
            }
        }

        if (bcc != null && !bcc.equals("")) {
            String[] bccArray = bcc.split(";");
            for (String bccElement : bccArray) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccElement));
            }
        }
        message.setSubject(subject, "UTF-8");
        message.setContent(body, "text/html; charset=utf-8");
//        message.
//        message.setText(body);
        Transport transport = session.getTransport();
//        System.out.println(session.getProperties().get("mail.smtp.host"));
        transport.connect(host, port);
        message.saveChanges();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

//        try {
//            message.writeTo(new FileOutputStream("c:/message.eml"));
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(UtilityFunctions.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(UtilityFunctions.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return message.getMessageID();
    }

    public static String sendMailWithAttachments(String from, String to, String bcc, String host, String localHost, String port, String subject, String body, List<InputStream> attachments, String fromsName) throws MessagingException, UnsupportedEncodingException, IOException, MimeTypeException {

        Properties properties = new Properties();
        properties.setProperty("mail.host", host);
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.localhost", localHost);
//        properties.setProperty("mail.smtp.localaddress", localHost);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.class", "com.sun.mail.smtp.SMTPTransport");
        Session session = Session.getInstance(properties, null);

        MimeMessage message = new MimeMessage(session);
        if (fromsName != null && !fromsName.isEmpty()) {
            message.setFrom(new InternetAddress(from, fromsName));
        } else {
            message.setFrom(new InternetAddress(from));
        }

//        Address address = new InternetAddress();
        if (to != null && !to.equals("")) {
            String[] toArray = to.split(";");
            for (String toElement : toArray) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toElement));
            }
        }

        if (bcc != null && !bcc.equals("")) {
            String[] bccArray = bcc.split(";");
            for (String bccElement : bccArray) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccElement));
            }
        }
        message.setSubject(subject, "UTF-8");
        Multipart multipartContent = new MimeMultipart();

        // attacco la parte di body
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body);
        multipartContent.addBodyPart(textPart);

        Detector d = new Detector();
        TikaConfig config = TikaConfig.getDefaultConfig();
        // ciclo gli attachments come inputStream
        int i = 0;
        for (InputStream is : attachments) {
            ByteArrayInputStream byteArrayInputStream = null;
            try {
                byte[] fileByteArray = IOUtils.toByteArray(is);
                byte[] fileBase64ByteArray = java.util.Base64.getEncoder().encode(fileByteArray);

                InternetHeaders fileHeaders = new InternetHeaders();
                byteArrayInputStream = new ByteArrayInputStream(fileByteArray);
                String fileContentType = d.getMimeType(byteArrayInputStream);
                MimeType mt = config.getMimeRepository().forName(fileContentType);
                String fileName = String.format("Allegato_%d%s", ++i, mt.getExtension());
                fileHeaders.setHeader("Content-Type", String.format("%s;name=\"%s\"", fileContentType, fileName));
                fileHeaders.setHeader("Content-Transfer-Encoding", "base64");
                fileHeaders.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));

                MimeBodyPart bodyPart = new MimeBodyPart(fileHeaders, fileBase64ByteArray);
                bodyPart.setFileName(fileName);
                multipartContent.addBodyPart(bodyPart);
            } finally {
                IOUtils.closeQuietly(byteArrayInputStream);
                IOUtils.closeQuietly(is);
            }
        }

        message.setContent(multipartContent);
//        message.
//        message.setText(body);
        Transport transport = session.getTransport();
//        System.out.println(session.getProperties().get("mail.smtp.host"));
        transport.connect(host, port);
        message.saveChanges();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();


        return message.getMessageID();
    }

    //Restituisce le differenze tra le stringhe passate in ingresso.
    //textFin è il testo dopo la modifica
    //textOrig è il testo prima della modifica
    public static String getDifferenceString(String textFin, String textOrig) {
        String difference = null;

        diff_match_patch diff = new diff_match_patch();

        LinkedList<Diff> diffs = diff.diff_main(textFin, textOrig);
        diff.diff_cleanupSemantic(diffs);
        LinkedList<Patch> patches = diff.patch_make(diffs);//patch_make(diffs) => patches

        //Patch delle robe modificate
        difference = diff.patch_toText(patches);

        return difference;
    }

    //diffText è la stringa contenente tutte le modifiche tra le due stringhe
    public static String rebuildingOriginalString(String diffText, String textFin) {
        diff_match_patch diff = new diff_match_patch();
        Object[] patch_apply = diff.patch_apply((LinkedList<Patch>) diff.patch_fromText(diffText), textFin);

        String testoOriginale = (String) patch_apply[0];
        return testoOriginale;
    }

    public static String rebuildingOriginalStringToHTML(String textFin, String textOrig) {
        diff_match_patch diff = new diff_match_patch();
        LinkedList<Diff> diffs = diff.diff_main(textFin, textOrig);
        diff.diff_cleanupSemantic(diffs);

        return diff.diff_prettyHtml(diffs);
    }

    public static EncryptedPassword encrypt(String clearTextPassword, String key) throws UnsupportedEncodingException {
        return PasswordCrypto.encrypt(clearTextPassword, key);
    }

    public static String decrypt(String EncryptedPassword, String key, String salt) {
        return PasswordCrypto.decrypt(EncryptedPassword, key, salt);
    }

    public static X509Certificate readCert(InputStream is) throws FileNotFoundException, CertificateException, NoSuchProviderException {
        BufferedInputStream bis = null;
        X509Certificate cert = null;
        try {
            bis = new BufferedInputStream(is);
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "SUN");
            cert = (X509Certificate) cf.generateCertificate(bis);
        } finally {
            IOUtils.closeQuietly(bis);
        }
        return cert;
    }

    /**
     * Verifica la correttezza crittografica della firma di un file
     *
     * @param signedFile
     * @return InputStream del file da controllare
     * @throws IOException
     * @throws FileNotFoundException
     * @throws MimeTypeException
     */
    public static boolean isAllSignCryttographycallyOK(InputStream signedFile) throws IOException, FileNotFoundException, MimeTypeException {
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = File.createTempFile("cryptographyc_check_", ".file", new File(tempDir));
        try (FileOutputStream tempFileOs = new FileOutputStream(tempFile);) {

            IOUtils.copy(signedFile, tempFileOs);
            SignatureVerifier signatureVerifier = new SignatureVerifier(tempFile);
            boolean verify = signatureVerifier.verify();
//            System.out.println(signatureVerifier.getSignsResultString());
            return verify;
        } finally {
//            IOUtils.closeQuietly(signedFile);
//            IOUtils.closeQuietly(tempFileOs);
            tempFile.delete();
        }
    }

    /**
     * Verifica la correttezza crittografica della firma di un file
     *
     * @param signedFile
     * @return il file da controllare
     * @throws IOException
     * @throws FileNotFoundException
     * @throws MimeTypeException
     */
    public static boolean isAllSignCryttographycallyOK(File signedFile) throws IOException, FileNotFoundException, MimeTypeException {
        FileInputStream inputStream = new FileInputStream(signedFile);
        try {
            return isAllSignCryttographycallyOK(inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * aggiunge keywords al pdf
     *
     * @param pdf
     * @param keywords le keywords da aggiungere (separate da ",")
     * @param outputFile il nuovo file da creare
     * @throws com.itextpdf.text.DocumentException
     * @throws IOException
     */
    public static void addKeywords(File pdf, String keywords, File outputFile) throws DocumentException, IOException {
        try (FileInputStream fis = new FileInputStream(pdf)) {
            addKeywords(fis, keywords, outputFile);
        }
    }

    /**
     * aggiunge keywords al pdf
     *
     * @param pdf
     * @param keywords le keywords da aggiungere (separate da ",")
     * @param outputFile il nuovo file da creare
     * @throws com.itextpdf.text.DocumentException
     * @throws IOException
     */
    public static void addKeywords(InputStream pdf, String keywords, File outputFile) throws IOException, DocumentException {
        PdfReader reader = null;
        FileOutputStream fileOutputStream = null;
        PdfAStamper stamper = null;
        try {
            reader = new PdfReader(pdf);
            fileOutputStream = new FileOutputStream(outputFile);
            stamper = new PdfAStamper(reader, fileOutputStream, PdfAConformanceLevel.PDF_A_1A);
            HashMap<String, String> info = reader.getInfo();
            String currentKeywords = info.get(PdfProperties.KEYWORDS);
            if (currentKeywords != null && !currentKeywords.isEmpty()) {
                currentKeywords += "," + keywords;
            } else {
                currentKeywords = keywords;
            }
            info.put(PdfProperties.KEYWORDS, currentKeywords);

            stamper.setMoreInfo(info);

        } finally {
            if (stamper != null) {
                stamper.close();
            }
            IOUtils.closeQuietly(fileOutputStream);
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * torna true se la keyword passata è presente nel pdf
     *
     * @param pdf
     * @param keyword
     * @return true se la keyword passata è presente nel pdf, false altrimenti
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean hasKeyword(File pdf, String keyword) throws FileNotFoundException, IOException {
        try (FileInputStream fis = new FileInputStream(pdf)) {
            return hasKeyword(fis, keyword);
        }
    }

    /**
     * torna true se la keyword passata è presente nel pdf
     *
     * @param pdf
     * @param keyword
     * @return true se la keyword passata è presente nel pdf, false altrimenti
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean hasKeyword(InputStream pdf, String keyword) throws IOException {
        String pdfKeywords = getPdfKeywords(pdf);
        if (pdfKeywords != null && !pdfKeywords.isEmpty()) {
            String[] keywordsSplitted = pdfKeywords.split(",");
            return Arrays.stream(keywordsSplitted).anyMatch(e -> e.equalsIgnoreCase(keyword));
        } else {
            return false;
        }
    }

    /**
     * torna le keywords di un pdf
     *
     * @param pdf
     * @return le keywords di un pdf
     * @throws IOException
     */
    public static String getPdfKeywords(File pdf) throws IOException {
        String pdfKeywordsToReturn;
        try (FileInputStream fis = new FileInputStream(pdf)) {
            pdfKeywordsToReturn = getPdfKeywords(fis);
            fis.close();
            System.out.println("FileInputStream Correctly Closed!");
        }
        return pdfKeywordsToReturn;
    }

    /**
     * torna le keywords di un pdf
     *
     * @param pdf
     * @return le keywords di un pdf
     * @throws IOException
     */
    public static String getPdfKeywords(InputStream pdf) throws IOException {
        PdfReader reader = null;
        String stringToReturn = null;
        try {
            reader = new PdfReader(pdf);
            HashMap<String, String> info = reader.getInfo();
            if (info != null && !info.isEmpty()) {
                stringToReturn = info.get(PdfProperties.KEYWORDS);
            } else {
                //return null;      //NO! devo prima chiuderer il reader
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return stringToReturn;
    }

    public static boolean isPdfCorrupted(String filePath) {
        PdfReader reader = null;
        try {
            if (isPdf(new File(filePath))) {
                reader = new PdfReader(filePath);
            }
            return false;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return true;
        }
        finally{
            if(reader!=null)
                reader.close();
        }
    }

    /**
     * Serve per creare la lettera mergiata con il firmispizio nei casi in cui
     * troviamo documenti protocollati senza la lettera firmata Il file poi va
     * fatto firmare al firmatario e caricato a mano su mongo
     *
     * @throws DocumentException
     * @throws IOException
     */
    public static void creaLetteraPerFirmaRiparatrice() throws DocumentException, IOException, XMPException {
        String file1 = "C:/tmp/work/PG0075360_2017_Lettera.pdf";
        String file2 = "C:/tmp/work/firmispizio_pico.pdf";
        String ICCFilePath = "";
        ArrayList<String> toMerge = new ArrayList<>();
        toMerge.add(file1);
        toMerge.add(file2);

        mergePdfAFilesToFile(toMerge, new File("c:/tmp/work/PG0075360_2017_Lettera_merged_firmispizio.pdf"), ICCFilePath, "firmispizio");
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, MalformedURLException, SendHttpMessageException, NoSuchAlgorithmException, DocumentException, PrintException, InterruptedException, UnsupportedEncodingException, MimeTypeException, SVActionException, UtilityFunctionException, Exception {

        System.out.println(isSigned(new FileInputStream("segnaposto.pdf")));

    }

    public static byte[] inputStreamToByteArray(InputStream is) throws IOException {
        return IOUtils.toByteArray(is);
    }

}
