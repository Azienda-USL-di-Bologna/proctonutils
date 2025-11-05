package it.bologna.ausl.proctonutils;

import it.bologna.ausl.proctonutils.exceptions.SVActionException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giuseppe De Marco
 */
public class SVAction {

private String idAttivita;
private String ordinale;
private String idUtenti;
private String servizioAppartenenza;
private String idTipiAttivita;
private String descrizioneAttivita;
private String oggettoAttivita;
private String labelUrlCommand;
private String urlCommand;
private String uuidProcesso;
private String noteAttivita;

private String actionType; // possibili valori: "ok" o "abort"
private String taskName;
private String taskUUID;

// indica se l'azione appartiene al primo firmatario designato per il processo
private String isMainAction;

    public SVAction() {
        this.idAttivita = null;
        this.idAttivita = null;
        this.idUtenti = null;
        this.servizioAppartenenza = null;
        this.idTipiAttivita = null;
        this.descrizioneAttivita = null;
        this.oggettoAttivita = null;
        this.labelUrlCommand = null;
        this.urlCommand = null;
        this.uuidProcesso = null;
        this.noteAttivita = null;
        this.actionType = null;
        this.taskName = null;
        this.taskUUID = null;
        this.isMainAction = null;
    }

    public SVAction(String idUtenti, String labelUrlCommand, String urlCommand, String oggettoAttivita) {
        this.idAttivita = null;
        this.idUtenti = idUtenti;
        this.servizioAppartenenza = null;
        this.idTipiAttivita = null;
        this.descrizioneAttivita = null;
        this.oggettoAttivita = oggettoAttivita;
        this.labelUrlCommand = labelUrlCommand;
        this.urlCommand = urlCommand;
        this.uuidProcesso = null;
        this.noteAttivita = null;
        this.actionType = null;
        this.taskName = null;
        this.taskUUID = null;
        this.isMainAction = null;
    }

    public SVAction(String idAttivita) {
        this.idAttivita = idAttivita;
        this.idUtenti = null;
        this.servizioAppartenenza = null;
        this.idTipiAttivita = null;
        this.oggettoAttivita = null;
        this.descrizioneAttivita = null;
        this.labelUrlCommand = null;
        this.urlCommand = null;
        this.uuidProcesso = null;
        this.noteAttivita = null;
        this.actionType = null;
        this.taskName = null;
        this.taskUUID = null;
        this.isMainAction = null;
    }

    public void parseAction(String data) throws SVActionException {
        StringTokenizer tok = new StringTokenizer(data, ";");
        while (tok.hasMoreTokens()) {
            String namevalueString = tok.nextToken();
            String[] namevalueArray = namevalueString.split("=");
            try {
                Field field = this.getClass().getDeclaredField(namevalueArray[0]);
                field.set(this, URLDecoder.decode(namevalueArray[1], "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new SVActionException(ex.getCause());
            }
            catch (NoSuchFieldException ex) {
                throw new SVActionException(ex.getCause());
            }
            catch (IllegalArgumentException ex) {
                throw new SVActionException(ex.getCause());
            }
            catch (IllegalAccessException ex) {
                throw new SVActionException(ex.getCause());
            }

        }
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getOggettoAttivita() {
        return oggettoAttivita;
    }

    public void setOggettoAttivita(String oggettoAttivita) {
        this.oggettoAttivita = oggettoAttivita;
    }

    public String getDescrizioneAttivita() {
        return descrizioneAttivita;
    }

    public void setDescrizioneAttivita(String descrizioneAttivita) {
        this.descrizioneAttivita = descrizioneAttivita;
    }

    public String getIdAttivita() {
        return idAttivita;
    }

    public void setIdAttivita(String idAttivita) {
        this.idAttivita = idAttivita;
    }

    public String getUuidProcesso() {
        return uuidProcesso;
    }

    public void setUuidProcesso(String idDocumento) {
        this.uuidProcesso = idDocumento;
    }

    public String getIdTipiAttivita() {
        return idTipiAttivita;
    }

    public void setIdTipiAttivita(String idTipiAttivita) {
        this.idTipiAttivita = idTipiAttivita;
    }

    public String getIdUtenti() {
        return idUtenti;
    }

    public void setIdUtenti(String idUtenti) {
        this.idUtenti = idUtenti;
    }

    public String getServizioAppartenenza() {
        return servizioAppartenenza;
    }

    public void setServizioAppartenenza(String servizioAppartenenza) {
        this.servizioAppartenenza = servizioAppartenenza;
    }

    public String getNoteAttivita() {
        return noteAttivita;
    }

    public void setNoteAttivita(String noteAttivita) {
        this.noteAttivita = noteAttivita;
    }

    public String getLabelUrlCommand() {
        return labelUrlCommand;
    }

    public void setLabelUrlCommand(String labelUrlCommand) {
        this.labelUrlCommand = labelUrlCommand;
    }
    
    public String getUrlCommand() {
        return urlCommand;
    }

    public void setUrlCommand(String urlCommand) {
        this.urlCommand = urlCommand;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskUUID() {
        return taskUUID;
    }

    public void setTaskUUID(String taskUUID) {
        this.taskUUID = taskUUID;
    }

    public String getIsMainAction() {
        return isMainAction;
    }

    public void setIsMainAction(String isMainAction) {
        this.isMainAction = isMainAction;
    }
    
    public String getOrdinale() {
        return ordinale;
    }

    public void setOrdinale(String ordinale) {
        this.ordinale = ordinale;
    }
    
    @Override
    public String toString() {
    String result = "";
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (int i=0; i<declaredFields.length; i++) {
            String fieldName = declaredFields[i].getName();
            try {
                String fieldValue = null;
                try {
                    fieldValue = this.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(this).toString();
                }
                catch (Exception ex) {
                }
                if (fieldValue != null) {
                    result += fieldName + "=" + URLEncoder.encode(fieldValue, "UTF-8") + ";";
                }
            }
            catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }
        if (result.length() > 0)
            result = result.substring(0, result.length() - 1);
        return result;
    }
}
