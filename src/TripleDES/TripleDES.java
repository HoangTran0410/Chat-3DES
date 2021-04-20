package TripleDES;

import java.nio.charset.StandardCharsets;

public class TripleDES {
    String[] _strKeys;
    
    public TripleDES(String key1, String key2, String key3) {
        _strKeys = new String[] {key1, key2, key3};
    }

    /**
     * Perform 3-DES Encryption
     * @param plain
     * @param inputKeys
     * @return Crypt of 3-DES
     */
    public byte[] encrypt(byte[] plain) {
        byte[] crypt;
        crypt = new DES(_strKeys[0]).encrypt(plain);
        crypt = new DES(_strKeys[1]).decrypt(crypt);
        crypt = new DES(_strKeys[2]).encrypt(crypt);
        return crypt;
    }

    /**
     * Perform 3-DES Decryption.
     * @param crypt
     * @param inputKeys
     * @return Plain after decryption
     */
    public byte[] decrypt(byte[] crypt) {
        byte[] plain;
        plain = new DES(_strKeys[2]).decrypt(crypt);
        plain = new DES(_strKeys[1]).encrypt(plain);
        plain = new DES(_strKeys[0]).decrypt(plain);

        return plain;
    }

    public static void main(String[] args) {
        TripleDES tripleDes = new TripleDES("m8oemrkt", "!ms_leot", "2mgl@ao!");
        String plain = "Let's go cờ hó to the beach";
        byte[] encrypted = tripleDes.encrypt(plain.getBytes());
        byte[] decrypted = tripleDes.decrypt(encrypted);
        
        System.out.println(plain);
        System.out.println(new String(encrypted, StandardCharsets.UTF_8));
        System.out.println(new String(decrypted, StandardCharsets.UTF_8));
    }
}
