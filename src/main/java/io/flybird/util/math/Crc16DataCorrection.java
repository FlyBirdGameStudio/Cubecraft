package io.flybird.util.math;

/**
 * simple crc16 data correction
 *
 * @author <a href="https://blog.csdn.net/hualinger/article/details/125093269">csdn-行者张良 </a>
 */
public class Crc16DataCorrection {
    /**
     * 一个字节包含位的数量 8
     */
    private static final int BITS_OF_BYTE = 8;

    /**
     * 多项式
     */
    private static final int POLYNOMIAL = 0xA001;

    /**
     * 初始值
     */
    private static final int INITIAL_VALUE = 0xFFFF;

    /**
     * CRC16 encode
     * @param bytes src
     * @return result
     */
    public static int crc16(byte[] bytes) {
        int res = INITIAL_VALUE;
        for (int data : bytes) {
            res = res ^ data;
            for (int i = 0; i < BITS_OF_BYTE; i++) {
                res = (res & 0x0001) == 1 ? (res >> 1) ^ POLYNOMIAL : res >> 1;
            }
        }
        return revert(res);
    }

    /**
     * flip higher 8 bit and low 8 bit
     * @param src num needs flip
     * @return result
     */
    private static int revert(int src) {
        int lowByte = (src & 0xFF00) >> 8;
        int highByte = (src & 0x00FF) << 8;
        return lowByte | highByte;
    }
}
