package com.example.whenyoucomemerona.lib;

public class Level {


    private static int exp(int n) {
        return (int) ((0.04 * Math.pow(n, 3) + 0.8 * Math.pow(n, 2) + 2 * n) / 2.0);
    }

    public static int[] calcLevel (int my_content, int our_content) {

        int[] result = new int[3];          // 0 -- progress, 1 -- max, 2 -- level

        int i = 1;
        int score = my_content * 1 + our_content * 2;

        while (true) {
            if (exp(i) > score) {
                break;
            } else {
                i ++;
            }
        }

        result[0] = score - exp(i-1);
        result[1] = exp(i) - exp(i-1);
        result[2] = i;

        return result;
    }
}
