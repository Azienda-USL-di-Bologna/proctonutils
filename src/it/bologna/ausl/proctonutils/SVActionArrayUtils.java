package it.bologna.ausl.proctonutils;

import it.bologna.ausl.proctonutils.exceptions.SVActionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giuseppe De Marco
 */
public class SVActionArrayUtils {


    /**
     *
     * @param globalSVActionList lista di SVAction globale
     * @param localSVActionList lista di SVAction locale
     * @param taskName il task secondo il quale filtrare la lista globale
     * @return la lista locale filtrata e "merge-ata"
     * @throws SVActionException
     */
     /* filtro lista globale con il nome del task
     * se lista locale è vuota, lista locale = lista globale e fine
     * altrimenti setto tutta la lista locale a delete
     * scorro lista globale filtrata e setto riga della lista locale come quello della lista globale filtrata
     */
    public static ArrayList<String> merge(ArrayList globalSVActionList, ArrayList localSVActionList, String filteringTaskName) throws SVActionException {

        ArrayList<String> filteredGlobalList = filterSVActionList(globalSVActionList, filteringTaskName);
        if (localSVActionList == null || localSVActionList.isEmpty()) {
            return filteredGlobalList;
        }
        else {
            // setto tutte le azioni della lista locale a "delete"
            for (int i=0; i<localSVActionList.size(); i++) {
                SVAction svaction = new SVAction();
                String svactionString = (String)localSVActionList.get(i);
                svaction.parseAction(svactionString);
                svaction.setActionType("delete");
                localSVActionList.set(i, svaction.toString());
            }
            // eseguo l'update delle azioni presenti della lista locale con quella della lista globale filtrata
            for (int i=0; i<filteredGlobalList.size(); i++) {     
                String svactionString = filteredGlobalList.get(i);
                // sostituisco l'azione della lista locale (se esiste) con quella della lista globale, altrimenti la aggiungo
                if (!replaceSVAction(svactionString, localSVActionList))
                    localSVActionList.add(svactionString);
            }
        }
        return localSVActionList;
    }

    private static boolean replaceSVAction(String svActionStringToReplace, ArrayList svActionList) throws SVActionException {
    boolean found = false;
        SVAction svActionToReplace = new SVAction();
        svActionToReplace.parseAction(svActionStringToReplace);
        for (int i=0; i<svActionList.size(); i++) {
            String svActionString = (String)svActionList.get(i);
            SVAction svAction = new SVAction();
            svAction.parseAction(svActionString);
            if (svAction.getIdUtenti().equals(svActionToReplace.getIdUtenti())) {
                svActionList.set(i, svActionStringToReplace);
                found = true;
            }
        }
        return found;
    }
    
    private static int findSVAction(ArrayList svActionList, SVAction svActionToFind) throws SVActionException {
    int pos = -1;
        for (int i=0; i<svActionList.size(); i++) {
            String svActionString = (String)svActionList.get(i);
            SVAction svAction = new SVAction();
            svAction.parseAction(svActionString);
            if (svAction.getIdUtenti().equals(svActionToFind.getIdUtenti()) && svAction.getTaskName().equals(svAction.getTaskName())) {
                pos = i;
                break;
            }
        }
        return pos;
    }
    
    /** Rimuove dalla prima lista passata tutte le SVAction presenti della seconda lista passata (il controllo è effettuato sui campi idUtenti e taskName)
     * 
     * @param svActionList la lista iniziale
     * @param svActionsToDelete la lista di SVAction da rimuovere
     * @return una nuova lista contenente la differenza tra le due liste
     * @throws SVActionException 
     */
    public static ArrayList<String> deleteSVActions(ArrayList svActionList, ArrayList svActionsToDelete) throws SVActionException {
    ArrayList<String> resultSvActionList = new ArrayList<String>(svActionList);
        for (int i=0; i<svActionsToDelete.size(); i++) {
            String svActionString = (String)svActionsToDelete.get(i);
            SVAction svAction = new SVAction();
            svAction.parseAction(svActionString);
            int pos = findSVAction(resultSvActionList, svAction);
            if (pos != -1)
                resultSvActionList.remove(pos);
        }
        return resultSvActionList;
    }

    /** Ritorna il numero di "ok" ricevuti in una lista di SVAction
     *  Ritorna sempre "-1" se c'è almeno una SVACtion in stato "abort"
     * @param svactionList la lista di SVAction da controllare
     * @param recivedOk il numero di ok già riceuti da sommare a quelli che saranno trovati
     * @return il numero di "ok" ricevuti in una lista di SVAction oppure "-1" se c'è ne almeno una in stato "abort"
     * @throws SVActionException
     */
    public static int okInSVAction(ArrayList svactionList, int recivedOk) throws SVActionException {
    int oksFound = recivedOk;
    if (oksFound == -1)
        return oksFound;
        for (int i=0; i<svactionList.size(); i++) {
            String actionString = (String)svactionList.get(i);
            SVAction action = new SVAction();
            action.parseAction(actionString);
            String actionType = action.getActionType();
            if (actionType != null && !actionType.equals("")) {
//                if (actionType.equalsIgnoreCase("abort")) {
//                    oksFound = -1;
//                    break;
//                }
                if (actionType.equalsIgnoreCase("ok"))
                    oksFound ++;
            }
        }
        return oksFound;
    }

    /** Conta il numero di abort presenti nella lista di SVAction passata
     *
     * @param svactionList la lista di SVAction da controllare
     * @return il numero di abort presenti nella lista di SVAction passata
     * @throws SVActionException
     */
    public static int abortInSVAction(ArrayList svactionList) throws SVActionException {
    int abortFound = 0;
        for (int i=0; i<svactionList.size(); i++) {
            String actionString = (String)svactionList.get(i);
            SVAction action = new SVAction();
            action.parseAction(actionString);
            String actionType = action.getActionType();
            if (actionType != null && !actionType.equals("")) {
                if (actionType.equalsIgnoreCase("abort"))
                    abortFound ++;
            }
        }
        return abortFound;
    }

    /** Ritorna "true" se l'attività è presente nella lista di SVAction passata
     * 
     * @param svactionList la lista di SVAction da controllare
     * @param activityName l'attività da cercare
     * @return "true" se l'attività è presente nella lista di SVAction passata, "false" altrimenti
     * @throws SVActionException
     */
    public static boolean isTaskNameInSVActionList(ArrayList svactionList, String taskName) throws SVActionException {
        for (int i=0; i<svactionList.size(); i++) {
            String actionString = (String)svactionList.get(i);
            SVAction action = new SVAction();
            action.parseAction(actionString);
            String currentTaskName = action.getTaskName();
            if (currentTaskName.equals(taskName))
                return true;
        }
        return false;
    }

    /** Controlla se l'utente è presente nella lista di SVAction passata
     *
     * @param svactionList la lista di SVAction da controllare
     * @param user l'utente da cercare
     * @return "true" se  l'utente è presente nella lista di SVAction passata, "false" altrimenti
     * @throws SVActionException

     */
    public static boolean isUserInSVActionList(ArrayList svactionList, String user) throws SVActionException {
        for (int i=0; i<svactionList.size(); i++) {
            String actionString = (String)svactionList.get(i);
            SVAction action = new SVAction();
            action.parseAction(actionString);
            String currentUser = action.getIdUtenti();
            if (currentUser.equals(user))
                return true;
        }
        return false;
    }

    /** Toglie (setta a null) dalla lista di SVAction passata il campo actionType
     *
     * @param svactionList la lista di SVAction della quale resettare il campo actionType
     * @return la lista di SVAction passata con il campo actionType settato a null
     * @throws SVActionException

     */
    public static ArrayList<String> resetActionsTypeInSVActionList(ArrayList svactionList) throws SVActionException {
        for (int i=0; i<svactionList.size(); i++) {
            String actionString = (String)svactionList.get(i);
            SVAction action = new SVAction();
            action.parseAction(actionString);
            action.setActionType(null);
            svactionList.set(i, action.toString());
        }
        return svactionList;
    }

    /** Filtra una lista di SVAction in base la taskName passato e eventualmente rimuove l'utente passato dalla lista
     * 
     * @param svactionList la lista sdi SVAction da filtrare
     * @param filteringTaskName il task secondo il quale filtrare
     * @param userToExclude il campo "idUtenti" della SVAction dell'utente da eliminare dalla lista una volta che viene filtrata
     * @return lista di SVAction passata filtrata in base la taskName passato e senza l'utente passato
     * @throws SVActionException
     */
    public static ArrayList<String> filterSVActionList(ArrayList<String> svactionList, String filteringTaskName, String userToExclude) throws SVActionException {
        ArrayList<String> filteredGlobalList = new ArrayList<String>();
        //updatedLocalList = new ArrayList<Object>
        for (int i=0; i<svactionList.size(); i++) {
            SVAction svaction = new SVAction();
            String svactionString = svactionList.get(i);
            svaction.parseAction(svactionString);
            String taskName = svaction.getTaskName();
            if (taskName.equalsIgnoreCase(filteringTaskName)) {
                if (userToExclude == null || userToExclude.equals("") || !svaction.getIdUtenti().equals(userToExclude))
                filteredGlobalList.add(svactionString);
            }
        }
        return filteredGlobalList;
    }

    /** Filtra una lista di SVAction in base la taskName passato
     *
     * @param svactionList la lista di SVAction da filtrare
     * @param filteringTaskName il task secondo il quale filtrare
     * @return lista di SVAction passata filtrata in base la taskName passato
     * @throws SVActionException
     */
    public static ArrayList<String> filterSVActionList(ArrayList<String> svactionList, String filteringTaskName) throws SVActionException {
        return filterSVActionList(svactionList, filteringTaskName, null);
    }
    
    /** Filtra una SVAction in base all'idTipiAttivita
     * 
     * @param svactionList la lista di SVAction da filtrare
     * @param activityType l'idTipiAttività su cui filtrare
     * @return la lista di SVAction passata filtrata in base all'idTipiAttivita
     * @throws SVActionException 
     */
    public static ArrayList<String> filterActivityTypeInSVActionList(ArrayList<String> svactionList, String activityType) throws SVActionException {
        ArrayList<String> filteredActivityTypeSvActionList = new ArrayList<String>();
        for (int i=0; i<svactionList.size(); i++) {
            String actionString = (String)svactionList.get(i);
            SVAction action = new SVAction();
            action.parseAction(actionString);
            String currentActivityType = action.getIdTipiAttivita();
            if (currentActivityType.equalsIgnoreCase(activityType)) {
                filteredActivityTypeSvActionList.add(actionString);
            }
        }
        return filteredActivityTypeSvActionList;
    }
    
    /** Filtra una SVAction in base al servizio di appartenenza
     * 
     * @param svactionList la lista di SVAction da filtrare
     * @param activityType il servizio di apparteneza su cui filtrare
     * @return la lista di SVAction passata filtrata in base al servizio di appartenenza
     * @throws SVActionException 
     */
    public static ArrayList<String> filterServizioAppartenenzaInSVActionList(ArrayList<String> svactionList, String servizioApparteneza) throws SVActionException {
        ArrayList<String> filteredServizioAppartenenzaSvActionList = new ArrayList<String>();
        for (int i=0; i<svactionList.size(); i++) {
            String actionString = (String)svactionList.get(i);
            SVAction action = new SVAction();
            action.parseAction(actionString);
            String currentServizioAppartenenza = action.getServizioAppartenenza();
            if (currentServizioAppartenenza.equalsIgnoreCase(servizioApparteneza)) {
                filteredServizioAppartenenzaSvActionList.add(actionString);
            }
        }
        return filteredServizioAppartenenzaSvActionList;
    }

    /** Filtra la lista di SVAction passata in base agli ok ricevuti, al taskName passato e senza l'utente passato
     *  Ritorna sempre "-1" se c'è almeno una SVACtion in stato "abort"
     * @param SvActionList la lista di SVAction da filtrare
     * @param filteringTaskName il task secondo il quale filtrare la lista prima di filtrare gli ok
     * @param userToExclude il campo "idUtenti" della SVAction dell'utente da eliminare dalla lista una volta che viene filtrata
     * @return la lista di SVAction passata filtrata in base agli ok ricevuti, al taskName passato e senza l'utente passato
     * @throws SVActionException
     */
    public static ArrayList filterOkInSVActionList(ArrayList SvActionList, String filteringTaskName, String userToExclude) throws SVActionException {
    ArrayList<String> filteredOkSvActionList = new ArrayList<String>();
    ArrayList<String> filteredTaskNameSvActionList = new ArrayList<String>();
        if (filteringTaskName == null) {
            filteredTaskNameSvActionList = SvActionList;
        }
        else {
            if (userToExclude == null) {
                filteredTaskNameSvActionList = filterSVActionList(SvActionList, filteringTaskName);
            }        
            else {
                filteredTaskNameSvActionList = filterSVActionList(SvActionList, filteringTaskName, userToExclude);
            }
        }
        for (int i=0; i<filteredTaskNameSvActionList.size(); i++) {
            String actionString = (String)filteredTaskNameSvActionList.get(i);
            SVAction action = new SVAction();
            action.parseAction(actionString);
            String actionType = action.getActionType();
            if (actionType != null && !actionType.equals("")) {
                if (actionType.equalsIgnoreCase("ok")) {
                    filteredOkSvActionList.add(actionString);
                }
            }
        }
        return filteredOkSvActionList;
    }
    
    /** Filtra la lista di SVAction passata rimuovendo le azioni con "ok", al taskName passato e senza l'utente passato
     *  Ritorna sempre "-1" se c'è almeno una SVACtion in stato "abort"
     * @param SvActionList la lista di SVAction da filtrare
     * @param filteringTaskName il task secondo il quale filtrare la lista prima di filtrare gli ok
     * @param userToExclude il campo "idUtenti" della SVAction dell'utente da eliminare dalla lista una volta che viene filtrata
     * @return la lista di SVAction passata rimuovendo le azioni con "ok", filtrata in base al taskName passato e senza l'utente passato
     * @throws SVActionException
     */
    public static ArrayList filterNotOkInSVActionList(ArrayList SvActionList, String filteringTaskName, String userToExclude) throws SVActionException {
    ArrayList<String> filteredNotOkSvActionList = new ArrayList<String>();
    ArrayList<String> filteredTaskNameSvActionList = new ArrayList<String>();
        if (filteringTaskName == null) {
            filteredTaskNameSvActionList = SvActionList;
        }
        else {
            if (userToExclude == null) {
                filteredTaskNameSvActionList = filterSVActionList(SvActionList, filteringTaskName);
            }        
            else {
                filteredTaskNameSvActionList = filterSVActionList(SvActionList, filteringTaskName, userToExclude);
            }
        }
        for (int i=0; i<filteredTaskNameSvActionList.size(); i++) {
            String actionString = (String)filteredTaskNameSvActionList.get(i);
            SVAction action = new SVAction();
            action.parseAction(actionString);
            String actionType = action.getActionType();
            if (actionType == null || actionType.equals("")) {
                filteredNotOkSvActionList.add(actionString);
            }
        }
        return filteredNotOkSvActionList;
    }

    /** Filtra la lista di SVAction passata in base all'utente principale per il task
     * 
     * @param svactionList la lista di SVAction
     * @param taskName Il nome del task
     * @return la lista di SVAction filtrata in base all'utente principale per il task
     * @throws SVActionException
     */
    public static ArrayList<String> filterMainActionsInSVActionList(ArrayList<String> svactionList, String taskName) throws SVActionException {
    ArrayList<String> filteredSVActionList = new ArrayList<String>();

        for (int i=0; i<svactionList.size(); i++) {
            String actionString = svactionList.get(i);
            SVAction svAction = new SVAction();
            svAction.parseAction(actionString);
            if (svAction.getIsMainAction().equalsIgnoreCase("true") && svAction.getTaskName().equalsIgnoreCase(taskName)) {
                filteredSVActionList.add(actionString);
            }
        }
        return filteredSVActionList;
    }

    /** Ritorna la SVAction relativa al primo utente principale per il task
     * 
     * @param svactionList la lista di SVAction
     * @param TaskName il nome del task
     * @return la SVAction relativa al primo utente principale per il task passato
     * @throws SVActionException
     */
    public static SVAction getFirstTaskMainAction(ArrayList svactionList, String taskName) throws SVActionException {
    ArrayList<String> filteredSVActionList = filterMainActionsInSVActionList(svactionList, taskName);

        if (filteredSVActionList.size() > 0) {
            SVAction svAction = new SVAction();
            svAction.parseAction(filteredSVActionList.get(0));
            return svAction;
        }
        else
            return null;
    }

    /** Ritorna la SVAction relativa all'utente passato nel task passato
     *
     * @param svactionList la lista di SVAction
     * @param user l'utente fa cercare
     * @param taskName il nome del task
     * @return la SVAction relativa all'utente passato nel task passato
     * @throws SVActionException
     */
    public static SVAction getSVActionByUserAndTaskName(ArrayList svactionList, String user, String taskName) throws SVActionException {
        for (int i=0; i<svactionList.size(); i++) {
            String svActionString = (String)svactionList.get(i);
            SVAction sVAction = new SVAction();
            sVAction.parseAction(svActionString);
            if (sVAction.getIdUtenti().equals(user))
                return sVAction;
        }
        return null;
    }
    
    /** Ritorna l'elenco di tutti i servizi di appartenenza della SBVActionList passata
     *
     * @param svactionList la lista di SVAction
     * @return l'elenco di tutti i servizi di appartenenza della SBVActionList passata
     * @throws SVActionException
     */
    public static String[] getAllServiziAppartenenza(ArrayList svactionList) throws SVActionException {
    Set<String> serviziAppartenenza = new HashSet();
    String[] ret = new String[0];
        for (int i=0; i<svactionList.size(); i++) {
            String svActionString = (String)svactionList.get(i);
            SVAction sVAction = new SVAction();
            sVAction.parseAction(svActionString);
            serviziAppartenenza.add(sVAction.getServizioAppartenenza());
        }
        ret = serviziAppartenenza.toArray(ret);
        return ret;
    }

    /** Controlla se c'è almeno un utente afferente al servizio di apparteneza passato in un determinato task
     *
     * @param svactionList la lista di SVAction
     * @param servizioAppartenenza il servizio di appartenenza da cercare
     * @param taskName il nome del task nel quale cercare
     * @return "true" se esiste almeno un utente nel task passato afferente al servizio di appartenenza passato, "false" altrimenti
     * @throws SVActionException
     */
    public static boolean isNomeServizioInSVActionList(ArrayList svactionList, String servizioAppartenenza, String taskName) throws SVActionException {
        if (taskName != null && !taskName.equals("")) {
            svactionList = filterSVActionList(svactionList, taskName);
        }

        for (int i=0; i<svactionList.size(); i++) {
            String svActionString = (String)svactionList.get(i);
            SVAction sVAction = new SVAction();
            sVAction.parseAction(svActionString);
            if (sVAction.getServizioAppartenenza().equals(servizioAppartenenza))
                return true;
        }
        return false;
    }

    /** Controlla se c'è almeno un utente afferente al servizio di apparteneza passato in tutti i task
     *
     * @param svactionList la lista di SVAction
     * @param servizioAppartenenza il servizio di appartenenza da cercare
     * @return "true" se esiste almeno un utente in tutti i task afferente al servizio di appartenenza passato, "false" altrimenti
     * @throws SVActionException
     */
    public static boolean isNomeServizioInSVActionList(ArrayList svactionList, String servizioAppartenenza) throws SVActionException {
        return isNomeServizioInSVActionList(svactionList, servizioAppartenenza, null);
    }
    
    /** torna l'SVActionString più prossimo all'ordinale passato
     * 
     * @param svActionList la lista di SVActionString
     * @param ordinale l'ordinale
     * @return l'SVActionString più prossimo all'ordinale passato
     * @throws it.bologna.ausl.proctonutils.exceptions.SVActionException
     */
    public static String getNextSVActionByOrdinale(ArrayList svActionList, int ordinale) throws SVActionException {
        String svActionFound = null;
        orderSVActionListByOrdinale(svActionList);
        for (Object svActionObj : svActionList) {
            String SvActionString = (String)svActionObj;
            SVAction svAction = new SVAction();
            svAction.parseAction(SvActionString);
            if (ordinale <= Integer.parseInt(svAction.getOrdinale())) {
                svActionFound = SvActionString;
                break;
            }
        }
        return svActionFound;
    }
    
    /** Ordina una lista di SVActionString in ordine di ordinale crescente
     * 
     * @param svactionList la lista di SVActionString da ordinare
     * @return la lista di SVActionString ordinata in ordine di ordinale crescente
     * @throws SVActionException 
     */
    public static ArrayList<String> orderSVActionListByOrdinale(ArrayList svactionList) throws SVActionException{ 
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
               String svaString1 = (String)o1;
               String svaString2 = (String)o2;
               
                SVAction sva1 = new SVAction();
                try {
                    sva1.parseAction(svaString1);
                } catch (SVActionException ex) {
                    return 0;
                }
                
                SVAction sva2 = new SVAction();
                try {
                    sva2.parseAction(svaString2);
                } catch (SVActionException ex) {
                    return 0;
                }
                
               if (Integer.parseInt(sva1.getOrdinale()) < Integer.parseInt(sva2.getOrdinale()))
                   return -1;
               else if (Integer.parseInt(sva1.getOrdinale()) == Integer.parseInt(sva2.getOrdinale()))
                   return 0;
               else
                   return 1;
            }
        };
        Collections.sort(svactionList, comparator);
        return svactionList;
    }
}
