package dev.ryanseo.tutorial;

/**
 * Sample Java code for youtube.playlistItems.list
 * See instructions for running these code samples locally:
 * https://developers.google.com/explorer-help/code-samples#java
 */

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;

import static com.google.gson.JsonParser.parseString;

public class Main {
    private static final String DEVELOPER_KEY = "AIzaSyAA_VUqOVdF9z2gujF84petXl4GuNlo5-E";
    private static final String APPLICATION_NAME = "Playlist Backup";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    public static void main(String[] args)
            throws GeneralSecurityException, IOException, GoogleJsonResponseException {

        try {
            ArrayList<Video> playlistVideos = PlaylistActions.savePlaylist("PL6Y2H3WgxO8EaAein4qjh0omQ81AtJJc2", DEVELOPER_KEY);

            int counter = 0;
            for (Video v : playlistVideos) {
                System.out.println(v.getTitle() + " ID: " + v.getVideoId());
                counter++;
            }
            System.out.println(counter);
            System.out.println(playlistVideos.get(0).getTitle());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}