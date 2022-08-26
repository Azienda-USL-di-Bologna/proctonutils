package it.bologna.ausl.proctonutils;

import it.bologna.ausl.proctonutils.exceptions.UtilityFunctionException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpRequestsCollector{

    private final ExecutorService pool;
    private final int timeoutSec;
    private final ConcurrentHashMap<String, HttpCallResponse> resMap;
    private Set<Request> requests;

    public HttpRequestsCollector(int threadsNumber, int timeoutSec, ConcurrentHashMap<String, HttpCallResponse> resMap) {
        this.timeoutSec = timeoutSec;
        pool = Executors.newFixedThreadPool(threadsNumber);
        this.resMap = resMap;
    }

    public void addRequest(String resKey, String url, byte[] body, String httpMethodOverride, String contentType) {
        if (requests == null)
            requests = new HashSet<>();
        requests.add(new Request(resKey, url, body, httpMethodOverride, contentType, timeoutSec));
    }

    public void run() throws InterruptedException, UtilityFunctionException {
        if (requests != null) {
            requests.stream().
                    forEach(request -> 
                        pool.execute(
                            new HttpRequestsCollectorThread(
                                    resMap, 
                                    request.getResKey(), 
                                    request.getUrl(), 
                                    request.getBody(),
                                    request.getHttpMethodOverride(),
                                    request.getContentType(),
                                    request.timeoutSec
                            )
                        )
                    );
    //        System.out.println("accodati");
            pool.shutdown();
            boolean timedOut = !pool.awaitTermination(timeoutSec, TimeUnit.SECONDS);
            if (timedOut)
                throw new UtilityFunctionException("timeout");
        }
        else {
            System.out.println("nothing to do");
        }
//        System.out.println("finiti");
    }

    private class Request {
        private final String resKey;
        private final String url;
        private final byte[] body;
        private final String httpMethodOverride;
        private final String contentType;
        private final int timeoutSec;

        public Request(String resKey, String url, byte[] body, String httpMethodOverride, String contentType, int timeoutSec) {
            this.resKey = resKey;
            this.url = url;
            this.body = body;
            this.httpMethodOverride = httpMethodOverride;
            this.contentType = contentType;
            this.timeoutSec = timeoutSec;
        }

        public String getResKey() {
            return resKey;
        }

        public String getUrl() {
            return url;
        }

        public byte[] getBody() {
            return body;
        }

        public String getHttpMethodOverride() {
            return httpMethodOverride;
        }
        
        public String getContentType() {
            return contentType;
        }

        public int getTimeoutSec() {
            return timeoutSec;
        }
    }
}