/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class Helper {

    // -------------------------- Stream Data --------------------------
    public static String createClientData(String clientName, String key1, String key2, String key3) {
        return String.join(Constants.SEPARATE_MARKER,
                Constants.CLIENT_DATA_EVENT, // 0
                clientName, // 1
                key1, // 2
                key2, // 3
                key3 // 4
        );
    }

    public static String[] readClientData(String joinedStr) {
        String[] splitted = joinedStr.split(Constants.SEPARATE_MARKER);
        return new String[]{
            splitted[1], // client name
            splitted[2], // key1
            splitted[3], // key2
            splitted[4] // key3
        };
    }

    public static String createChatData(String sender, String receiver, String content) {
        return String.join(Constants.SEPARATE_MARKER,
                Constants.CHAT_EVENT, // 0
                sender, // 1
                receiver, // 2
                content // 3
        );
    }

    public static String[] readChatData(String joinedStr) {
        String[] splitted = joinedStr.split(Constants.SEPARATE_MARKER);
        return new String[]{
            splitted[1], // sender
            splitted[2], // receiver
            splitted[3] // content
        };
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
