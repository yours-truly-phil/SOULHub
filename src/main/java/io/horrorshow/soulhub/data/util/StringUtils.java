package io.horrorshow.soulhub.data.util;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String toValidFilename(String s) {
        if (s == null) return "filename";
        s = s.trim();
        s = s.replaceAll(" ", "_");
        s = s.replaceAll("[^a-zA-Z0-9\\._]+", "");
        if (s.endsWith(".")) {
            s += "_";
        }
        if (s.startsWith(".")) {
            s = "_" + s;
        }
        if (s.isBlank()) s = "filename";
        return s;
    }
}
