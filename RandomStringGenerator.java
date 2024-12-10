package com.janaushadhi.adminservice.util;

import java.util.Date;
import java.util.Random;

public class RandomStringGenerator {
    private static Random random = new Random((new Date()).getTime());
    public static String getRandomNumberString(int length) {

        char[] values = {'0', '1', '2', '3',
                '4', '5', '6', '7', '8', '9'};
        String out = "";
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(values.length);
            out += values[idx];
        }
        return out;
    }
}