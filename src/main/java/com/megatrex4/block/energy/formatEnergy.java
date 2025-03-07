package com.megatrex4.block.energy;

public class formatEnergy {
    public static String format(long energy) {
        double value = energy;
        String[] suffixes = {" E", "kE", "ME", "GE", "TE", "PE", "EE", "ZE", "YE"};
        int suffixIndex = 0;

        while (value >= 1000 && suffixIndex < suffixes.length - 1) {
            value /= 1000;
            suffixIndex++;
        }
        return String.format("%.2f%s", value, suffixes[suffixIndex]);
    }
}
