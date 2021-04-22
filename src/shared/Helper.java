/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class Helper {

    // -------------------------- Stream Data --------------------------
    /* 
        "CLIENT_DATA_EVENT;clientName;key1;key2;key3"
        "CHAT_EVENT;senderName;receiverName;chatContent"
        "ONLINE_LIST_EVENT;onlineName_1;onlineName_2;....;onlineName_n"
     */
    // https://viettuts.vn/java-new-features/varargs-trong-java
    public static String pack(String eventName, String... data) {
        return eventName + Constants.SEPARATE_MARKER + String.join(Constants.SEPARATE_MARKER, data);
    }

    public static String pack(String eventName, ArrayList<String> data) {
        return eventName + Constants.SEPARATE_MARKER + String.join(Constants.SEPARATE_MARKER, data);
    }

    public static ArrayList<String> unpack(String packed) {
        return new ArrayList<>(Arrays.asList(packed.split(Constants.SEPARATE_MARKER)));
    }

    // -------------------------- String --------------------------
    // https://stackoverflow.com/a/4212726/11898496
    public static String wrapString(String str, int maxLen) {
        StringBuilder sb = new StringBuilder(str);

        int i = 0;
        while (i + maxLen < sb.length() && (i = sb.lastIndexOf(" ", i + maxLen)) != -1) {
            sb.replace(i, i + 1, "\n");
        }

        return sb.toString();
    }

    public static String limitString(String str, int maxLen) {
        String ret = str.substring(0, Math.min(str.length(), maxLen));
        ret += str.length() > maxLen ? "..." : "";
        return ret;
    }

    // -------------------------- Date Time --------------------------
    // https://www.javatpoint.com/java-get-current-date
    static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    public static String getCurrentDateTime() {
        Date date = new Date();
        return DATETIME_FORMATTER.format(date);
    }

    // -------------------------- Random String --------------------------
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static final SecureRandom RND = new SecureRandom();

    // https://stackoverflow.com/a/157202/11898496
    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(RND.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public static String randomString(int minLen, int maxLen) {
        int len = (int) (minLen + RND.nextDouble() * (maxLen - minLen));
        return randomString(len);
    }

    // -------------------------- Base64 --------------------------
    public static String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] base64Decode(String data) {
        return Base64.getDecoder().decode(data);
    }
}
