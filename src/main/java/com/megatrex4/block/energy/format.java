package com.megatrex4.block.energy;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class format {
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

    public static void fotmattedTooltips(List<Text> tooltip, String details) {
        String[] words = details.split(" ");
        StringBuilder currentLine = new StringBuilder();
        int wordCount = 0;

        for (String word : words) {
            if (wordCount == 7) {
                tooltip.add(Text.literal(currentLine.toString().trim()).formatted(Formatting.GRAY));
                currentLine = new StringBuilder();
                wordCount = 0;
            }
            currentLine.append(word).append(" ");
            wordCount++;
        }

        if (currentLine.length() > 0) {
            tooltip.add(Text.literal(currentLine.toString().trim()).formatted(Formatting.GRAY));
        }
    }
}
