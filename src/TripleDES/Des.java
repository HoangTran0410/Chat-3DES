/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TripleDES;

import java.util.BitSet;
import java.util.Scanner;

/**
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class Des {
    public byte[][] blocks;

    // Constants

    public static final byte[] INITIAL_PERMUTATION = new byte[]{
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7,
    };
    public static final byte[] FINAL_PERMUTATION = new byte[]{
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };
    public static final byte[] EXPANSION = new byte[]{
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };
    public static final byte[][] S_BOX = new byte[][]{
            { // S1
                    14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
                    0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
                    4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
                    15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13
            },
            { // S2
                    15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
                    3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
                    0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
                    13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9
            },
            { // S3
                    10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
                    13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
                    13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
                    1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12
            },
            { // S4
                    7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
                    13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
                    10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
                    3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14
            },
            { // S5
                    2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
                    14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
                    4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
                    11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3
            },
            { // S6
                    12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
                    10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
                    9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
                    4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13
            },
            { // S7
                    4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
                    13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
                    1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
                    6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12
            },
            { // S8
                    13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
                    1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
                    7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
                    2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
            }
    };
    public static final byte[] P_PERMUTATION = new byte[]{
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };
    public static final byte[] PERMUTED_CHOICE_1 = new byte[]{
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };
    public static final byte[] PERMUTED_CHOICE_2 = new byte[]{
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    public static final byte[] SHIFT_DISTANCES = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    public static final int ByteSize = 8;
    public static final int KeySize = 64;

    // Public Methods

    public static String GetInputString(String prompt) {
        System.out.print(prompt);
        return GetInputString();
    }

    public static String GetInputString() {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }

    // Private Logic Methods

    public byte[][] InitializeBlocksFromPlain(String plain) {
        byte[][] blocks = new byte[(int) Math.ceil(plain.length() * 1.0 / ByteSize)][];
        int blockIndex = 0;
        int bitIndex = 0;
        for (int i = 0; i < plain.length(); i++) {
            if (bitIndex == 0) {
                blocks[blockIndex] = new byte[ByteSize];
            }
            blocks[blockIndex][bitIndex++] = (byte) plain.charAt(i);
            if (bitIndex >= ByteSize) {
                blockIndex++;
                bitIndex = 0;
            }
        }
        for (int i = plain.length(); i % ByteSize == 0; i++) {
            // Padding
            blocks[blockIndex][bitIndex++] = 0;
        }

        this.blocks = blocks;
        return blocks;
    }

    public void InitializeAllKeys(byte[] originalKey) {
        BitSet originalKeyBits = BitSet.valueOf(originalKey);
        BitSet permutedKeyBits = BitSet.valueOf(originalKey);

        // Transform into 56-bit key
        for (int i = 1; i <= ByteSize; i++) {
            int fromIndex = i * ByteSize - 1 - (i - 1); // Goes in pattern: 7, 15-1, 23-2, ...
            originalKeyBits = BitSetUtilities.shiftRight(originalKeyBits, 1, fromIndex, KeySize - 1);
        }

        // Permutes original key
        for (int i = 0; i < PERMUTED_CHOICE_1.length; i++) {
            boolean replacingBit = originalKeyBits.get(PERMUTED_CHOICE_1[i]);
            permutedKeyBits.set(i, replacingBit);
        }

        // Split into C0, D0, which is Left half and Right half respectively
        BitSet[] permutedCKeys = new BitSet[17];
        BitSet[] permutedDKeys = new BitSet[17];
        int[] whenToLeftShiftOnce = new int[]{1, 2, 9, 16};

        permutedCKeys[0] = permutedKeyBits.get(32, 63);
        permutedDKeys[0] = permutedKeyBits.get(0, 31);
        for (int i = 1; i <= 16; i++) {
            permutedCKeys[i] = BitSetUtilities.shiftLeft(permutedCKeys[i], 1);
        }
    }

    // TODO: HoangTran's variables, might be used
//    FILE *out;
//    int LEFT[][] = new int[17][32], RIGHT[][] = new int[17][32];
//    int IPtext[] = new int[64];
//    int EXPtext[] = new int[48];
//    int XORtext[] = new int[48];
//    int X[][] = new int [8][6];
//    int X2[] = new int[32];
//    int R[] = new int[32];
//    int key56bit[] = new int[56];
//    int key48bit[][] = new int[17][48];
//    int CIPHER[] = new int[64];
//    int ENCRYPTED[] = new int[64];

    public static void main(String[] args) {
        String plain = GetInputString("Enter plain: ");
        String key = GetInputString("Enter key (8 characters): ");

        Des des = new Des();
        des.InitializeBlocksFromPlain(plain);
        System.out.println("Hello");
        des.InitializeAllKeys(key.getBytes());
    }

}
