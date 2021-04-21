/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.security;

import java.io.File;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
// https://stackjava.com/demo/code-java-vi-du-ma-hoa-giai-ma-voi-rsa.html
public class RSA {

    PublicKey publicKey;
    PrivateKey privateKey;
    Cipher cipher;

    public RSA() {
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public byte[] encrypt(byte[] original) {
        if(publicKey == null) {
            System.err.println("Không thể mã hóa! Chưa có public key.");
        }
        
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] byteEncrypted = cipher.doFinal(original);
            return byteEncrypted;
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new byte[0];
    }

    public byte[] decrypt(byte[] encrypted) {
        if(privateKey == null) {
            System.err.println("Không thể giải mã! Chưa có private key.");
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] byteDecrypted = cipher.doFinal(encrypted);
            return byteDecrypted;
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new byte[0];
    }

    public RSA preparePublicKey(String publicKeyPath) {
        try {
            publicKey = getPublicKeyFromFile(publicKeyPath);
        } catch (Exception ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public RSA preparePrivateKey(String privateKeyPath) {
        try {
            privateKey = getPrivateKeyFromFile(privateKeyPath);
        } catch (Exception ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        System.out.println("Encrypted (Base64 format): " + Base64Helper.encode(encrypted));

        byte[] decrypted = serverSide.decrypt(encrypted);
        System.out.println("Decrypted: " + new String(decrypted));
    }
}
