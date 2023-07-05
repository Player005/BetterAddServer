package net.player005.betteraddserver;

import java.util.Arrays;
import java.util.List;

public class IP2Name {

    public static List<String> upperCaseWords = Arrays.asList("hd", "yt", "eu", "na", "sa", "as", "pvp", "mc");
    public static List<String> dontIgnoreDomainEnding = Arrays.asList("Land", "Club");

    public static String toName(String IP) {
        String IPNoDots = IP.replace(".", "");
        if (IPNoDots.length() < 3) {
            return null;
        }

        try {
            //if it's a numeric ip, don't change the name field
            Integer.parseInt(IPNoDots);
            return null;
        } catch (NumberFormatException e) {
            String[] arr = IP.split("\\.");

            //capitalise every word, words present in upperCase List to complete uppercase
            int i = 0;
            for (String word : arr) {

                for (String s : upperCaseWords) {
                    word = word.replaceAll(s, s.toUpperCase());
                }

                if (word.length() < 2) {
                    continue;
                }
                arr[i] = word.substring(0, 1).toUpperCase() + word.substring(1);
                i++;
            }

            //remove domain ending if existing unless present in dontIgnoreDomainEnding
            String[] newArr = new String[0];
            if (arr.length > 1) {
                newArr = Arrays.copyOf(arr, arr.length - 1);
            }

            String result = String.join(" ", newArr);
            if (dontIgnoreDomainEnding.contains(arr[arr.length - 1])) {
                result = result.concat(arr[arr.length - 1]);
            }
            return result;
        }
    }
}
