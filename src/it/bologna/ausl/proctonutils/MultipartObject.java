package it.bologna.ausl.proctonutils;

import it.bologna.ausl.mimetypeutilities.Detector;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.Charsets;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.tika.mime.MediaType;

/**
 *
 * @author Giuseppe De Marco (gdm)
 */
public class MultipartObject {
    private String partName;
    private Object value;
    private String fileName;
    private String mimeType;

    public MultipartObject(String partName, Object value, String fileName, String mimeType) {
        this.partName = partName;
        this.fileName = fileName;
        this.value = value;
        this.mimeType = mimeType;
    }
   
    public MultipartObject(String partName, Object value, String mimeType) {
        this.partName = partName;
        this.value = value;
        this.mimeType = mimeType;
    }
    
    public MultipartObject(String partName, Object value) {
        this.partName = partName;
        this.value = value;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public AbstractContentBody getBody() throws UnsupportedEncodingException {
        AbstractContentBody body = null;
            if (value instanceof File) {
                if (mimeType == null)
                    mimeType = MediaType.OCTET_STREAM.toString();
                body = new FileBody((File)value, mimeType);
            }
            else if (value instanceof InputStream) {
                if (mimeType == null)
                    mimeType = MediaType.OCTET_STREAM.toString();
                body = new InputStreamBody((InputStream)value, mimeType, fileName);
            }
            else if (value instanceof String) {
                if (mimeType == null)
                    mimeType = MediaType.TEXT_PLAIN.toString();
                if (mimeType.equalsIgnoreCase(Detector.MEDIA_TYPE_APPLICATION_JSON.toString()))
                    body = new ByteArrayBody(((String)value).getBytes(), mimeType, fileName);
                else
                    body = new StringBody((String)value, mimeType, Charsets.UTF_8);
            }
        return body;
    }
}
