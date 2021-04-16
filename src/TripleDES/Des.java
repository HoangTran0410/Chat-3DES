/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TripleDES;

import java.util.BitSet;

/**
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class Des {
    public BitSet[] _keys;
    public byte[][] _plainBlocks;
    public byte[][] _cryptBlocks;
    public byte[][] _encryptedBlocks;
    public byte[][] _decryptedBlocks;
    public byte[] _encrypted; // ENCRYPT RESULT
    public byte[] _decrypted; // DECRYPT RESULT

    // Public Getters

    public byte[] getEncrypted() {
        return _encrypted;
    }

    public byte[] getDecrypted() {
        return _decrypted;
    }

    // Public Methods

    /**
     * Create a single key for encryption and decryption.
     * Perform by accepting a plain input key, then permute it through PC-1.
     * Triple-DES needs this to generate 3 keys for each phase.
     *
     * @param originalKey Can be a human-input or instructed key
     * @return Permuted key for use
     */
    public static byte[] CreateSingleKey(byte[] originalKey) {
        // Permutes original key => Becomes 56-bit key
        BitSet originalKeyBits = BitSet.valueOf(originalKey);
        BitSet permutedKeyBits = Des.Permute(originalKeyBits, PERMUTED_CHOICE_1);
        return permutedKeyBits.toByteArray();
    }

    /**
     * Perform DES Encryption.
     *
     * @param plain
     * @param key
     * @return Crypt of DES
     */
    public byte[] Encrypt(byte[] plain, byte[] key) {
        _keys = this.InitializeSubKeys(key);
        _plainBlocks = this.GenerateBlocks(plain);
        _encrypted = this.EncodeBlocks(_plainBlocks, _keys);
        return this._encrypted;
    }

    /**
     * Perform DES Decryption.
     *
     * @param crypt
     * @param key
     * @return Plain after decryption
     */
    public byte[] Decrypt(byte[] crypt, byte[] key) {
        _keys = this.InitializeSubKeys(key);
        _cryptBlocks = this.GenerateBlocks(crypt);
        _decrypted = this.DecodeBlocks(_cryptBlocks, _keys);
        return this._decrypted;
    }

    // Private Constants

    private static final byte[] INITIAL_PERMUTATION = new byte[]{
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7,
    };
    private static final byte[] FINAL_PERMUTATION = new byte[]{
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };
    private static final byte[] EXPANSION = new byte[]{
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };
    private static final byte[][][] S_BOX = new byte[][][]{
            { // S1
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            { // S2
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            { // S3
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            { // S4
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            { // S5
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            { // S6
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            { // S7
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            { // S8
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };
    private static final byte[] P_PERMUTATION = new byte[]{
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };
    private static final byte[] PERMUTED_CHOICE_1 = new byte[]{
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };
    private static final byte[] PERMUTED_CHOICE_2 = new byte[]{
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    private static final int ByteSize = 8;
    private static final int KeySize = 64;
    private static final byte[] KEY_LEFTSHIFT_DISTANCES = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    // Private Logic Methods

    /**
     * Perform Permutation on a BitSet through a permute table.
     * Example: "the 57th bit of the original key K becomes the first bit of the permuted key K+"
     *
     * @param originalBitSet
     * @param permuteTable
     * @return Permuted BitSet
     */
    private static BitSet Permute(BitSet originalBitSet, byte[] permuteTable) {
        BitSet newBitSet = new BitSet();

        for (int i = 0; i < permuteTable.length; i++) {
            boolean replacingBit = originalBitSet.get(permuteTable[i] - 1);
            newBitSet.set(i, replacingBit);
        }

        return newBitSet;
    }

    /**
     * Perform Substitution through a supplied S-Box
     *
     * @param originalBitSet
     * @param subBox
     * @return Substituted BitSet
     */
    private static BitSet Substitute(BitSet originalBitSet, byte[][] subBox) {
        BitSet rowBits = new BitSet();
        BitSet colBits = new BitSet();
        rowBits.set(0, originalBitSet.get(0)); // left-most bit
        rowBits.set(1, originalBitSet.get(5)); // right-most bit
        for (int i = 0; i < 4; i++) {
            colBits.set(i, originalBitSet.get(i + 1)); // the remaining bits
        }

        byte sBoxValue = subBox[BitSetUtilities.getSingleValue(rowBits)][BitSetUtilities.getSingleValue(colBits)];

        BitSet newBitSet = BitSet.valueOf(new byte[]{sBoxValue});
        return newBitSet;
    }

    /**
     * Create 16 keys for 16 rounds from a permuted key
     *
     * @param key A permuted key (56-bit)
     * @return Array of keys stored in BitSet
     */
    private BitSet[] InitializeSubKeys(byte[] key) {
        BitSet keyBits = BitSet.valueOf(key);

        // Split into C0, D0, which is Left half and Right half respectively, into 28-bit Keys
        BitSet[] leftC = new BitSet[17];
        BitSet[] rightD = new BitSet[17];
        leftC[0] = keyBits.get(28, 56);
        rightD[0] = keyBits.get(0, 28);

        // Shift C0 and D0 to the left for 16 times (Ci, Di), each times store as a key
        for (int i = 1; i <= 16; i++) {
            leftC[i] = BitSetUtilities.shiftLeft(leftC[i - 1], KEY_LEFTSHIFT_DISTANCES[i - 1], 28);
            rightD[i] = BitSetUtilities.shiftLeft(rightD[i - 1], KEY_LEFTSHIFT_DISTANCES[i - 1], 28);
        }

        // Finalize 16 of 28-bit Keys from Ci and Di into 48-bit Keys
        BitSet[] finalKeys = new BitSet[17];
        for (int i = 1; i <= 16; i++) {
            BitSet concatCiDi = BitSetUtilities.concatenateBitSets( // Concatenate Ci and Di
                    28, rightD[i], leftC[i]
            );

            finalKeys[i] = Des.Permute(concatCiDi, PERMUTED_CHOICE_2); // Becomes 48-bit key
        }

        return finalKeys;
    }

    /**
     * Divide the initial input into 8-byte (64-bit) blocks.
     * Empty bit will be replaced with Padding Bit, "0"
     *
     * @param inputBits
     * @return List of Blocks generated
     */
    private byte[][] GenerateBlocks(byte[] inputBits) {
        byte[][] blocks = new byte[(int) Math.ceil(inputBits.length * 1.0 / ByteSize)][];
        int blockIndex = 0;
        int bitIndex = 0;
        for (int i = 0; i < inputBits.length; i++) {
            if (bitIndex == 0) {
                blocks[blockIndex] = new byte[ByteSize];
            }
            blocks[blockIndex][bitIndex++] = inputBits[i];
            if (bitIndex >= ByteSize) {
                blockIndex++;
                bitIndex = 0;
            }
        }
        for (int i = inputBits.length; i % ByteSize != 0; i++) {
            // Padding
            blocks[blockIndex][bitIndex++] = 0;
        }

        return blocks;
    }

    /**
     * The "f" function called in Des's Main Flow.
     *
     * @param rightBits
     * @param key
     * @return Result of the function is a 32-bit BitSet
     */
    private BitSet DesFunction(BitSet rightBits, BitSet key) {
        BitSet bitOf48s;
        BitSet bitOf32s = new BitSet();

        // Expand from 32-bit to 48 bit
        bitOf48s = Des.Permute(rightBits, EXPANSION);

        // XOR with key (48-bit)
        bitOf48s.xor(key);

        // Iterate through all S-boxes; Goes from S8 to S1 (inverse) in order to perform BitSet Concat correctly
        for (int i = S_BOX.length - 1; i >= 0; i--) {
            // Goal: Transform each 6 bits of 48-bit into 4 bits of 32-bit, by permuting with S-box[i]
            int fromIndex = i * 6;
            int toIndex = i * 6 + 6;
            BitSet bitOf4s = Des.Substitute(bitOf48s.get(fromIndex, toIndex), S_BOX[i]); // 6-bit into 4-bit

            // Concat into the 32-bit BitSet
            bitOf32s = i == S_BOX.length - 1 // if is at starting point
                    ? bitOf4s
                    : BitSetUtilities.concatenateBitSets(32 - (4 * (i + 1)), bitOf32s, bitOf4s); // size goes in pattern: 4 8 12 16 20 24
        }

        // Permute the 32-bit BitSet with P-Permutation, retains its size
        bitOf32s = Des.Permute(bitOf32s, P_PERMUTATION);

        return bitOf32s;
    }

    /**
     * DES's main flow of encrypting/decrypting a single Block
     * As seen in the graph below:
     * https://www.researchgate.net/profile/Muhammad-Mushtaq-20/publication/321587376/figure/fig4/AS:568581112987648@1512571709096/Data-Encryption-Standard-DES-Algorithm.png
     * @param block
     * @param keys
     * @return A single block of Encrypted/Decrypted
     */
    private byte[] DesMainFlow(byte[] block, BitSet[] keys) {
        BitSet bits = BitSet.valueOf(block);

        // Permute block (retains size of 64-bit)
        bits = Des.Permute(bits, INITIAL_PERMUTATION);

        // Split into Left-half L0 and Right-half R0, 32-bit each
        BitSet[] leftL = new BitSet[17];
        BitSet[] rightR = new BitSet[17];
        leftL[0] = bits.get(32, 64);
        rightR[0] = bits.get(0, 32);

        // Perform 16-rounds loop of function
        for (int i = 1; i <= 16; i++) {
            leftL[i] = rightR[i - 1];
            rightR[i] = BitSet.valueOf(leftL[i - 1].toByteArray());
            rightR[i].xor(DesFunction(rightR[i - 1], keys[i]));
        }

        // Concat into R16L16, then permute through IP-1
        bits = BitSetUtilities.concatenateBitSets(32, leftL[16], rightR[16]);
        bits = Des.Permute(bits, FINAL_PERMUTATION);

        return bits.toByteArray();
    }

    /**
     * Perform encrypt n blocks of input.
     * @param blocks
     * @param keys
     * @return encrypted blocks concatenated into a single byte[]
     */
    private byte[] EncodeBlocks(byte[][] blocks, BitSet[] keys) {
        this._encryptedBlocks = new byte[blocks.length][8];
        BitSet encrypted = new BitSet();

        for (int i = 0; i < blocks.length; i++) {
            this._encryptedBlocks[i] = DesMainFlow(blocks[i], keys);
            encrypted = i == 0
                    ? BitSet.valueOf(_encryptedBlocks[i])
                    : BitSetUtilities.concatenateBitSets(64 * i, encrypted, BitSet.valueOf(_encryptedBlocks[i]));
        }

        return encrypted.toByteArray();
    }

    /**
     * Perform decrypt n blocks of input.
     * @param blocks
     * @param keys
     * @return decrypted blocks concatenated into a single byte[]
     */
    private byte[] DecodeBlocks(byte[][] blocks, BitSet[] keys) {
        this._decryptedBlocks = new byte[blocks.length][8];
        BitSet decrypted = new BitSet();

        // Decode is essentially Encode with SubKeys in reversed order => create reversedKeys
        BitSet[] reversedKeys = new BitSet[17];
        for (int i = 1; i <= 16; i++) {
            reversedKeys[i] = keys[16 - i + 1];
        }

        for (int i = 0; i < blocks.length; i++) {
            this._decryptedBlocks[i] = DesMainFlow(blocks[i], reversedKeys);
            decrypted = i == 0
                    ? BitSet.valueOf(_decryptedBlocks[i])
                    : BitSetUtilities.concatenateBitSets(64 * i, decrypted, BitSet.valueOf(_decryptedBlocks[i]));
        }

        return decrypted.toByteArray();
    }

    public static void main(String[] args) {
        String plain = "Let's go to the beach";
        String key = "mflwkero";

        Des des = new Des();
        byte[] permutedKey = Des.CreateSingleKey(key.getBytes());
        byte[] encrypted = des.Encrypt(plain.getBytes(), permutedKey);
        byte[] decrypted = des.Decrypt(encrypted, permutedKey);
    }

}
