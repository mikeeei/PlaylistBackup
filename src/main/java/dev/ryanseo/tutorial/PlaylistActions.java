package dev.ryanseo.tutorial;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;

import static com.google.gson.JsonParser.parseString;

public class PlaylistActions {
    public static ArrayList<Video> savePlaylist(String playlistId, String DEVELOPER_KEY) throws GeneralSecurityException, IOException {

        YouTube youtubeService = Main.getService();

        YouTube.PlaylistItems.List request = youtubeService.playlistItems().list("status, snippet");
        request.setMaxResults(50L);

        PlaylistItemListResponse response = request.setKey(DEVELOPER_KEY)
                .setPlaylistId(playlistId)
                .execute();

        JsonArray itemsArray = parseString(response.toString()).getAsJsonObject().getAsJsonArray("items");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Video.class, (JsonDeserializer<Video>) (json, typeOfT, context) -> {
            // json is an element of itemsArray
            JsonObject itemsObject = json.getAsJsonObject();
            JsonObject snippet = itemsObject.getAsJsonObject("snippet");

            return new Video(snippet.getAsJsonObject("resourceId").get("videoId").getAsString(),
                    snippet.get("title").getAsString(),
                    snippet.get("videoOwnerChannelId").getAsString(),
                    snippet.get("videoOwnerChannelTitle").getAsString(),
                    PrivacyStatus.valueOf(itemsObject.getAsJsonObject("status").get("privacyStatus").getAsString().toUpperCase()),
                    snippet.getAsJsonObject("thumbnails").getAsJsonObject("high").get("url").getAsString());
        });

        TypeToken<Collection<Video>> collectionType = new TypeToken<>(){};
        ArrayList<Video> playlistVideos = new ArrayList<>(gsonBuilder.create().fromJson(itemsArray.toString(), collectionType));

        while (response.getNextPageToken() != null) {
            request.setPageToken(response.getNextPageToken());
            response = request.execute();
            itemsArray = parseString(response.toString()).getAsJsonObject().getAsJsonArray("items");
            playlistVideos.addAll(gsonBuilder.create().fromJson(itemsArray.toString(), collectionType));
        }

        return playlistVideos;
    }

}
