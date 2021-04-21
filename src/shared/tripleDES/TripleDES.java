package shared.tripleDES;

import java.util.Base64;

public class TripleDES {
    String[] _strKeys;
    
    public TripleDES(String key1, String key2, String key3) {
        _strKeys = new String[] {key1, key2, key3};
    }
    
    public void setKeys(String key1, String key2, String key3) {
        _strKeys = new String[] {key1, key2, key3};
    }

    /**
     * Perform 3-DES Encryption
     * @param input
     * @return Crypt of 3-DES
     */
    public byte[] encrypt(byte[] input) {
        // Để input và output nó dễ hiểu hơn
        // plain với crypt nhìn hơi rối khi so sánh 2 hàm encrypt và decrypt
        byte[] output;
        output = new DES(_strKeys[0]).encrypt(input);
        output = new DES(_strKeys[1]).decrypt(output);
        output = new DES(_strKeys[2]).encrypt(output);
        return output;
    }

    /**
     * Perform 3-DES Decryption.
     * @param input
     * @return Plain after decryption
     */
    public byte[] decrypt(byte[] input) {
        byte[] output;
        output = new DES(_strKeys[2]).decrypt(input);
        output = new DES(_strKeys[1]).encrypt(output);
        output = new DES(_strKeys[0]).decrypt(output);
        return output;
    }

    public static void main(String[] args) {
        TripleDES tripleDes = new TripleDES("m8oemrkt", "!ms_leot", "2mgl@ao!");
        String plain = "Let's go cờ hó to the beach";
        byte[] encrypted = tripleDes.encrypt(plain.getBytes());
        byte[] decrypted = tripleDes.decrypt(encrypted);
        
        System.out.println(plain);
        System.out.println(new String(encrypted));
        System.out.println(Base64.getEncoder().encodeToString(encrypted));
        System.out.println(new String(decrypted));
    }
}
