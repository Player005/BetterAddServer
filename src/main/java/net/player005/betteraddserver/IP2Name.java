package net.player005.betteraddserver;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class IP2Name {

    public static List<String> alwaysUppercaseWords = Arrays.asList("hd", "yt", "eu", "na", "sa", "as", "pvp", "mc");
    public static List<String> alwaysKeepEndings = Arrays.asList("Land", "Club");

    public static @Nullable String toName(@NotNull String ip) {
        String ipNoDots = ip.replace(".", "");

        if (ipNoDots.length() < 3) return null;

        try {
            //if it's a numeric ip, don't change the name field
            Integer.parseInt(ipNoDots);
            return null;
        } catch (NumberFormatException ignored) {}

        ip = removeAfterColon(ip);
        String[] splitIP = ip.split("\\.");
        if (splitIP.length < 1) return null;

        //capitalise every word, words present in upperCase List to complete uppercase
        for (int i = 0; i < splitIP.length; i++) {
            var word = splitIP[i];

            for (String s : alwaysUppercaseWords)
                if (Objects.equals(word, s)) word = word.toUpperCase();

            if (!word.isEmpty()) word = capitalise(word);

            splitIP[i] = word;
        }

        splitIP[splitIP.length - 1] = (splitIP.length == 1 || alwaysKeepEndings.contains(splitIP[splitIP.length - 1])) ?
                capitalise(splitIP[splitIP.length - 1]) : "";

        return String.join(" ", splitIP);
    }

    public static @NotNull String capitalise(@NotNull String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    @Contract(value = "_ -> !null", pure = true)
    public static @NotNull String removeAfterColon(@NotNull String s) {
        return s.split(":")[0];
    }
}
