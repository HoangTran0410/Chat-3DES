package TripleDES;

public class TripleDes {
    Des[] _desInstances = new Des[]{new Des(), new Des(), new Des()};
    byte[][] _desKeys = new byte[3][56];
    byte[][] _crypts = new byte[3][64];

    public void Encrypt(byte[] plain, byte[][] inputKeys) {
        for (int i = 0; i < 3; i++) {
            _desKeys[i] = Des.CreateSingleKey(inputKeys[i]);
        }

        _crypts[0] = _desInstances[0].Encrypt(plain, _desKeys[0]);
    }

    public static void main(String[] args) {
        TripleDes tripleDes = new TripleDes();
        byte[][] inputKeys = new byte[][] {"m8oemrkt".getBytes(), "!ms_leot".getBytes(), "2mgl@ao!".getBytes()};
        String plain = "Let's go to the beach";
        tripleDes.Encrypt(plain.getBytes(), inputKeys);
    }
}
