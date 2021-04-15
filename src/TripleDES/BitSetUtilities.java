package TripleDES;

import java.util.BitSet;

public class BitSetUtilities {
    /**
     * Shift a BitSet n digits to the right in specified range. With the left-most bit in range being 0
     * @param bits
     * @param n the shift distance.
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public static BitSet shiftRight(BitSet bits, int n, int fromIndex, int toIndex) {
        BitSet newBitSet = BitSet.valueOf(bits.toByteArray());
        for (int i = fromIndex; i < toIndex; i++) {
            bits.set(i, bits.get(i + 1));
        }
        bits.set(toIndex, false);
        return bits;
    }

    /**
     * Shifts a BitSet n digits to the left. For example, 0b0110101 with n=2 becomes 0b10101.
     * https://stackoverflow.com/questions/9008150/shifting-a-java-bitset
     *
     * @param bits
     * @param n the shift distance.
     * @return
     */
    public static BitSet shiftLeft(BitSet bits, int n) {
        if (n < 0)
            throw new IllegalArgumentException("'n' must be >= 0");
        if (n >= 64)
            throw new IllegalArgumentException("'n' must be < 64");

        long[] words = bits.toLongArray();

        // Do the shift
        for (int i = 0; i < words.length - 1; i++) {
            words[i] >>>= n; // Shift current word
            words[i] |= words[i + 1] << (64 - n); // Do the carry
        }
        words[words.length - 1] >>>= n; // shift [words.length-1] separately, since no carry

        return BitSet.valueOf(words);
    }

    /**
     * Shifts a BitSet n digits to the right. For example, 0b0110101 with n=2 becomes 0b000110101.
     * https://stackoverflow.com/questions/9008150/shifting-a-java-bitset
     *
     * @param bits
     * @param n the shift distance.
     * @return
     */
    public static BitSet shiftRight(BitSet bits, int n) {
        if (n < 0)
            throw new IllegalArgumentException("'n' must be >= 0");
        if (n >= 64)
            throw new IllegalArgumentException("'n' must be < 64");

        long[] words = bits.toLongArray();

        // Expand array if there will be carry bits
        if (words[words.length - 1] >>> (64 - n) > 0) {
            long[] tmp = new long[words.length + 1];
            System.arraycopy(words, 0, tmp, 0, words.length);
            words = tmp;
        }

        // Do the shift
        for (int i = words.length - 1; i > 0; i--) {
            words[i] <<= n; // Shift current word
            words[i] |= words[i - 1] >>> (64 - n); // Do the carry
        }
        words[0] <<= n; // shift [0] separately, since no carry

        return BitSet.valueOf(words);
    }
}
