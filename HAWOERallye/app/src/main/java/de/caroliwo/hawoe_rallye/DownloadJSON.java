package de.caroliwo.hawoe_rallye;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadJSON extends AsyncTask<String, Integer, String> {
    private ProgressBar progressBar;
    private Task [] task;
    private Context applicationContext;

    public DownloadJSON (ProgressBar progressBar, Context applicationContext){
        this.progressBar = progressBar;
        this.applicationContext = applicationContext;
    }

    @Override
    protected String doInBackground(String... urls) {
        HttpURLConnection connection = null;

        try {
            //Verbindung aufbauen zu Seite, die JSON zur Verfügung stellt
            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); //GET oder POST-Request? noch klären --> eher PostRequest mit IdentifizierungsDaten
            //connection.setReadTimeout(10000 /* milliseconds */ );
            //connection.setConnectTimeout(15000 /* milliseconds */ );
            connection.connect();

            //POST-Request IdentifizierungsDaten senden

            //GET-Daten verarbeiten
            //https://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            int counter = 0;
            StringBuffer response = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                counter++;
                response.append(line);
                response.append('\r');
                //Anzahl der Lines rausbekommen, dann publishProgress(counter/AnzahlLines*100);
                publishProgress((int) ((counter + 1) / 1 * 100));
            }
            reader.close();


            //GSON
            Gson gson = new Gson();
            // JSON to Java object
            //Task task = gson.fromJson(response.toString(), Task.class);


            String jsonStringTest = " [\n" +
                    "    {\n" +
                    "    \"name\": \"Produktionslabor\",\n" +
                    "    \"icon\": \"clapperboard\",\n" +
                    "    \"time\": \"11.15-13.00 Uhr\",\n" +
                    "    \"destination\": \"EG im Neubau\",\n" +
                    "    \"numberOfTasks\": 2,\n" +
                    "    \"task\": \n" +
                    "    [\n" +
                    "      {\"task1\": \"Eine kleine Aufgabe wartet auf euch. Lasst eure Punkte von den Mitarbeitern des Produktionslabors eintragen.\"},\n" +
                    "      {\"task2\": \"Punkte\"}\n" +
                    "    ],\n" +
                    "    \"answerFieldNeeded\":true,\n" +
                    "    \"passwordNeeded\":true,\n" +
                    "    \"buttonNeeded\": true,\n" +
                    "    \"buttonText\":\"Abgeben\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"Wettrennen\",\n" +
                    "    \"icon\": \"car\",\n" +
                    "    \"time\": \"12.00-15.00 Uhr\",\n" +
                    "    \"destination\": \"U36 im Altbau\",\n" +
                    "    \"numberOfTasks\": 1,\n" +
                    "    \"task\": \n" +
                    "    [\n" +
                    "      {\"task1\": \"Tretet in einem spannenden Wettrennen gegen uns OE-Tutoren an!\"}\n" +
                    "    ],\n" +
                    "    \"answerField\":false,\n" +
                    "    \"password\":false,\n" +
                    "    \"button\": true,\n" +
                    "    \"buttonText\":\"Erledigt\"\n" +
                    "  }\n" +
                    "]\n";

            task = gson.fromJson(jsonStringTest, Task[].class);


            return response.toString();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        applicationContext.startActivity(new Intent(applicationContext, LogInActivity.class));
    }
}