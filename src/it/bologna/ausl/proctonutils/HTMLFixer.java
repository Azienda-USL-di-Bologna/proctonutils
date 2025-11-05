/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.bologna.ausl.proctonutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contiene funzioni per correggere l'html da passare poi al masterchef.
 *
 * @author Salo
 */
public class HTMLFixer {

    // Questa regex trova sequenze di date dove l'anno può essere all'inizio o alla fine, 
    //  avere il delimitatore / o -, giorni e messi possono avere una o due cifre
    // L'importante è che non ci siano altre numeri prima e dopo la stringa sia breakata in qualche modo (spazio, a capo, punto...)
    final static Pattern DATE_PATTERN = Pattern.compile("(?<![0-9\\-])" // [che non sia preceduto da altri numeri]
            + "(([0-9]{1,2})[\\/]([0-9]{1,2})[\\/]([0-9]{1,4})|" // [1/2/2021 o 01/02/2020]
            + "(([0-9]{1,4})[\\/]([0-9]{1,2})[\\/]([0-9]{1,2})|" // [2020/1/2 o 2020/01/02]
            + "([0-9]{1,2})[\\-]([0-9]{1,2})[\\-]([0-9]{1,4}))|" // [1-2-2020 o 01/02/2020]
            + "([0-9]{1,4})[\\-]([0-9]{1,2})[\\-]([0-9]{1,2}))" // [2020-1-1 o 2020-01-02]
            + "(?![^ \\.\\s\\n\\r\\)])");   // seguiti da spazio a capo ) .  [ma forse anche no]
    final static String SPAN_TAG_WRAP_DATES_OPEN = "<span class=\"wrap-dates\">";
    final static String SPAN_TAG_CLOSE = "</span>";

    private static Matcher getDateMatcher(String text) {
        return DATE_PATTERN.matcher(text);
    }

    public static boolean containsDate(String text) {
        Matcher m = getDateMatcher(text);
        boolean matches = m.find();
        return matches;
    }

    public static String wrapTextWithSpanWrapDateClassTagBetweenPositions(String text, int startPos, int endPos) {
        return text.substring(0, startPos) + SPAN_TAG_WRAP_DATES_OPEN + text.substring(startPos, endPos) + SPAN_TAG_CLOSE + text.substring(endPos);
    }

    public static String wrapAllTextWithSpanWrapDateClassTag(String text) {
        Matcher dateMatcher = getDateMatcher(text);
        int lastPos = 0;
        while (dateMatcher.find(lastPos)) {
            int startPos = dateMatcher.start();
            int endPos = dateMatcher.end();
            System.out.println("startPos " + startPos);
            System.out.println("endPos " + endPos);
            text = wrapTextWithSpanWrapDateClassTagBetweenPositions(text, startPos, endPos);
            dateMatcher = getDateMatcher(text);
        }
        return text;
    }
}
