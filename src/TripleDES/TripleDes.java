package TripleDES;

import java.nio.charset.StandardCharsets;

public class TripleDes {
    Des[] _desInstances = new Des[]{new Des(), new Des(), new Des()};
    byte[][] _desKeys = new byte[3][56];

    /**
     * Perform 3-DES Encryption
     * @param plain
     * @param inputKeys
     * @return Crypt of 3-DES
     */
    public byte[] Encrypt(byte[] plain, byte[][] inputKeys) {
        for (int i = 0; i < 3; i++) {
            _desKeys[i] = Des.CreateSingleKey(inputKeys[i]);
        }

        byte[] crypt;
        crypt = _desInstances[0].Encrypt(plain, _desKeys[0]);
        crypt = _desInstances[1].Decrypt(crypt, _desKeys[1]);
        crypt = _desInstances[2].Encrypt(crypt, _desKeys[2]);

        return crypt;
    }

    /**
     * Perform 3-DES Decryption.
     * @param crypt
     * @param inputKeys
     * @return Plain after decryption
     */
    public byte[] Decrypt(byte[] crypt, byte[][] inputKeys) {
        for (int i = 0; i < 3; i++) {
            _desKeys[i] = Des.CreateSingleKey(inputKeys[i]);
        }

        byte[] plain;
        plain = _desInstances[0].Decrypt(crypt, _desKeys[2]);
        plain = _desInstances[1].Encrypt(plain, _desKeys[1]);
        plain = _desInstances[2].Decrypt(plain, _desKeys[0]);

        return plain;
    }

    public static void main(String[] args) {
        TripleDes tripleDes = new TripleDes();
        byte[][] inputKeys = new byte[][] {"m8oemrkt".getBytes(), "!ms_leot".getBytes(), "2mgl@ao!".getBytes()};
        String plain = "Let's go cờ hó to the beach";
        byte[] encrypted = tripleDes.Encrypt(plain.getBytes(), inputKeys);
        byte[] decrypted = tripleDes.Decrypt(encrypted, inputKeys);
        
        System.out.println(plain);
        System.out.println(new String(encrypted, StandardCharsets.UTF_8));
        System.out.println(new String(decrypted, StandardCharsets.UTF_8));
    }
}
