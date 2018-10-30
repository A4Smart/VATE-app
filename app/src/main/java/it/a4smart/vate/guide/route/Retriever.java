package it.a4smart.vate.guide.route;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Retriever extends AsyncTask<String, Void, String> {

    public static String getBlocking(String url) {
        Retriever get = new Retriever();
        get.execute(url);
        String out = "";
        try {
            out = get.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    protected String doInBackground(String... urls) {
        String json = "";
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            json = readStream(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    private String readStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        return sb.toString();
    }
}
