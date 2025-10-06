package net.player005.betteraddserver;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class LunarClientServerMappings {

    private static final String MAPPINGS_URL = "https://servermappings.lunarclientcdn.com/servers.json";
    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    private static final Map<String, String> ADDRESS_TO_NAME = new ConcurrentHashMap<>();

    static {
        CompletableFuture.runAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder(URI.create(MAPPINGS_URL)).GET().build();
                HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
                JsonArray json = GSON.fromJson(response.body(), JsonArray.class);
                for (JsonElement element : json) {
                    final JsonObject obj = element.getAsJsonObject();
                    final JsonArray addresses = obj.getAsJsonArray("addresses");
                    final String primaryAddress = obj.get("primaryAddress").getAsString();
                    final String name = obj.get("name").getAsString();

                    for (JsonElement address : addresses) {
                        ADDRESS_TO_NAME.put(address.getAsString(), name);
                    }
                    ADDRESS_TO_NAME.put(primaryAddress, name);
                }
            } catch (Exception e) {
                System.out.println("Failed to fetch Lunar Client server mappings: " + e.getMessage());
            }
        });
    }

    public static @Nullable String getServerName(@NotNull String address) {
        address = address.trim().toLowerCase(Locale.ROOT);
        return ADDRESS_TO_NAME.get(address);
    }
}
