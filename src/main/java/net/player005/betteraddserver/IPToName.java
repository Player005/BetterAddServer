package net.player005.betteraddserver;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public abstract class IPToName {

    private static final String[] alwaysUppercase = {"hd", "yt", "pvp", "mc"};
    private static final List<String> keepEndings = Arrays.asList("land", "club");

    @Contract(pure = true)
    public static @NotNull String toName(@NotNull String ip) {
        if (ip.length() < 3) return "";

        var splitAddress = ip.split("\\.");

        if (splitAddress.length < 1) return "";
        if (!keepEndings.contains(splitAddress[splitAddress.length - 1]) && splitAddress.length > 1)
            splitAddress = Arrays.copyOf(splitAddress, splitAddress.length - 1);

        final var result = new StringBuilder();

        for (var element : splitAddress) {
            if (element.length() < 3) {
                result.append(element.toUpperCase()).append(" ");
                continue;
            }
            for (var upper : alwaysUppercase) {
                var index = element.toLowerCase().indexOf(upper);
                if (index != -1) {
                    var sb = new StringBuilder(element);
                    sb.replace(index, index + upper.length(), upper.toUpperCase());
                    var nextCharIndex = index + upper.length();
                    System.out.println(nextCharIndex);
                    if (element.length() > nextCharIndex)
                        sb.replace(nextCharIndex, nextCharIndex + 1, element.substring(nextCharIndex, nextCharIndex + 1).toUpperCase());
                    element = sb.toString();
                }
            }
            result.append(capitalise(element)).append(" ");
        }

        return result.toString().trim();
    }

    @Contract(pure = true)
    public static @NotNull String capitalise(@NotNull String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
