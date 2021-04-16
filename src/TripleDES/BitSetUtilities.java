package TripleDES;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

public class BitSetUtilities {
    /**
     * Shift a BitSet n digits to the right in specified range. With the left-most bit in range being 0
     *
     * @param bits
     * @param n         the shift distance.
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
     * https://stackoverflow.com/questions/37199396/bitset-leftshift-wrap-around-issue
     *
     * @param b
     * @param amount the shift distance.
     * @param size   Size of the BitSet
     * @return
     */
    public static BitSet shiftLeft(BitSet b, int amount, int size) {
        BitSet result = new BitSet(size);

        for (int i = b.nextSetBit(0); i != -1; i = b.nextSetBit(i + 1)) {
            int j = (i + size - amount) % size;  // new index after wrapping
            result.set(j);
        }
        return result;
    }

    /**
     * Concatenate a list of BitSet.
     * The first of the bunch will be at the right-most, so be cautious with the order of params.
     * https://stackoverflow.com/questions/10495953/java-bitset-which-allows-easy-concatenation-of-bitsets
     * @param size Size of the existing BitSet (the first BitSet in the list)
     * @return
     */
    public static BitSet concatenateBitSets(int size, BitSet... bitSets) {
        BitSet finalBitSet = new BitSet();

        int loopCount = 0;
        for (BitSet bitSet : bitSets) {
            if (loopCount == 0) {
                finalBitSet = BitSet.valueOf(bitSet.toByteArray());
            } else {
                for (int i = 0; i < size; i++) {
                    finalBitSet.set((loopCount * size) + i, bitSet.get(i));
                }
            }

            loopCount++;
        }

        return finalBitSet;
    }

    public static byte getSingleValue(BitSet bitSet) {
        byte[] byteArr = bitSet.toByteArray();
        if (byteArr.length == 0) {
            return 0;
        }
        return byteArr[0];
    }

    /**
     * http://www.java2s.com/example/java/java.util/bitset-to-binary-string.html
     * @param bitSet
     * @param gapAt When to add space
     * @return
     */
    public static String getBinaryString(BitSet bitSet, int gapAt) {
        StringBuilder sb = new StringBuilder(bitSet.length());
        for (int i = bitSet.length() - 1; i >= 0; i--) {
            sb.append(bitSet.get(i) ? 1 : 0);
            if (i % gapAt == 0) {
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }
}
