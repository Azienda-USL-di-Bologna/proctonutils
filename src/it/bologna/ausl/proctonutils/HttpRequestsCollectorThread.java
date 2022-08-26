package it.bologna.ausl.proctonutils;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author gdm
 */
public class HttpRequestsCollectorThread implements Runnable {

    public static final String ID_UTENTE = "idutente";
    public static final String REQUEST_ERROR = "request_error";
    
    private final ConcurrentHashMap resMap;
    private final String resKey;
    private final String url;
    private final byte[] body;
    private final String httpMethodOverride;
    private final String contentType;
    private final int timeoutSec;
    
    public HttpRequestsCollectorThread(ConcurrentHashMap<String, HttpCallResponse> resMap, String resKey, String url, byte[] body, String httpMethodOverride, String contentType, int timeoutSec) {
        this.resMap = resMap;
        this.resKey = resKey;
        this.url = url;
        this.body = body;
        this.httpMethodOverride = httpMethodOverride;
        this.contentType = contentType;
        this.timeoutSec = timeoutSec;
    }

    @Override
    public void run() {
        try {
//            Map<String, byte[]> params = new HashMap<>();
//            params.put(ID_UTENTE, params.getBytes());
            //resMap.put(resKey, UtilityFunctions.sendHttpMessage(url, null, null, body, "POST", contentType));
            resMap.put(resKey, UtilityFunctions.httpCall(url, httpMethodOverride, contentType, body, timeoutSec));
        }
        catch (Exception ex) {
           ex.printStackTrace(System.out);
           resMap.put(resKey, REQUEST_ERROR + ": " + ex.toString());
        }
    }
}
