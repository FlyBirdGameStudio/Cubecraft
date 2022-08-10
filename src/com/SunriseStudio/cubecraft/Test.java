package com.sunrisestudio.cubecraft;

import com.sunrisestudio.util.math.MathHelper;

public class Test {
    public static void main(String[] args) {
        for (int x=-32;x<32;x++) {
            System.out.println(MathHelper.getRelativePosInChunk(x, 16));
        }
    }


}
