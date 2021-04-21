/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.security;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import javax.crypto.Cipher;
import shared.Helper;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
// https://stackjava.com/demo/code-java-vi-du-ma-hoa-giai-ma-voi-rsa.html
public class RSA {

    PublicKey publicKey;
    PrivateKey privateKey;
    Cipher cipher;

    public RSA() throws Exception {
        cipher = Cipher.getInstance("RSA");
    }

    public byte[] encrypt(byte[] original) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] byteEncrypted = cipher.doFinal(original);
        return byteEncrypted;
    }

    public byte[] decrypt(byte[] encrypted) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] byteDecrypted = cipher.doFinal(encrypted);
        return byteDecrypted;
    }

    public RSA preparePublicKey(String publicKeyPath) throws Exception {
        publicKey = getPublicKeyFromFile(publicKeyPath);
        return this;
    }

    public RSA preparePrivateKey(String privateKeyPath) throws Exception {
        privateKey = getPrivateKeyFromFile(privateKeyPath);
        return this;
    }

    private PublicKey getPublicKeyFromFile(String filePath) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filePath).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private PrivateKey getPrivateKeyFromFile(String filePath) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filePath).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("~Create RSA server side...");
        RSA serverSide = new RSA()
                .preparePrivateKey("src/rsa_keypair/privateKey")
                .preparePublicKey("src/rsa_keypair/publicKey");

        System.out.println("~Create RSA client side...");
        RSA clientSide = new RSA()
                .preparePublicKey("src/rsa_keypair/publicKey");

        String original = "stackjava.com";
        System.out.println("Original: " + original);

        byte[] encrypted = clientSide.encrypt(original.getBytes());
        System.out.println("Encrypted (byte format): " + Arrays.toString(encrypted));
        System.out.println("Encrypted (Base64 format): " + Helper.base64Encode(encrypted));

        byte[] decrypted = serverSide.decrypt(encrypted);
        System.out.println("Decrypted: " + new String(decrypted));
    }
}
