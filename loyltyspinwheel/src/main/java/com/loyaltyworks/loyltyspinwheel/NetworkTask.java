package com.loyaltyworks.loyltyspinwheel;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/** Created by Sujeet on 13/06/2023. */

public class NetworkTask extends AsyncTask<Void, Void, Void> {

    private String url;
    private JSONObject data;

    public NetworkTask(String url, JSONObject data) {
        this.url = url;
        this.data = data;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

            // Set request method to POST
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            // Enable SSL if required
            // ...

            // Convert data to JSON string
            String query = data.toString();

            // Send request
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write(query);
            writer.flush();
            writer.close();

            // Get response
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();

            // Read response
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();
            String response = responseBuilder.toString();

            // Print request and response
            String requestLog = "Request: " + url + "\n" + query;
            String responseLog = "Response: " + responseCode + " " + responseMessage + "\n"  + response;
            Log.d("NetworkTask", requestLog);
            Log.d("NetworkTask", responseLog);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}



