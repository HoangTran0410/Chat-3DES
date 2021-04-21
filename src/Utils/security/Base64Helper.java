/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.security;

import java.util.Base64;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class Base64Helper {
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    public static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }
}
