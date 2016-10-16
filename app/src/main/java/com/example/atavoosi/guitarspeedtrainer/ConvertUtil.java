package com.example.atavoosi.guitarspeedtrainer;

public class ConvertUtil {
    public static int ConvertMsToBpm(int ms) {
        return 60000 / ms;
    }

    public static int ConvertBpmToMs(int bpm) {
        return 60000 / bpm;
    }
}
