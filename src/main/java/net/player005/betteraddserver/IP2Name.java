package net.player005.betteraddserver;

import java.util.Arrays;

public abstract class IP2Name {
    public static String IP2Name(String IP){
        if (IP.length() < 3){
            return null;
        }
        String IPNoDots = IP.replace(".", "");
        try {
            Integer.parseInt(IPNoDots);
            return null;
        }catch (NumberFormatException e){
            String[] arr = IP.split("\\.");
            int i = 0;
            for(String word : arr){
                arr[i] = word.substring(0,1).toUpperCase() + word.substring(1);
                i++;
            }
            if(arr.length > 1){
                arr = Arrays.copyOf(arr, arr.length - 1);
            }
            return String.join(" ", arr);
        }
    }
}
