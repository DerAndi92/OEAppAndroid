package de.caroliwo.hawoe_rallye;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadingActivity extends AppCompatActivity {
private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

            progressBar= findViewById(R.id.progressBar);

            DownloadJSON downloadJSON = new DownloadJSON();
            downloadJSON.execute("https://samples.openweathermap.org/data/2.5/forecast?q=M%C3%BCnchen,DE&appid=b6907d289e10d714a6e88b30761fae22"); //Beispiellink

    }

    class DownloadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection= null;

            try {
                //Verbindung aufbauen zu Seite, die JSON zur Verfügung stellt
                URL url = new URL (urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET"); //GET oder POST-Request? noch klären --> eher PostRequest mit IdentifizierungsDaten
                //connection.setReadTimeout(10000 /* milliseconds */ );
                //connection.setConnectTimeout(15000 /* milliseconds */ );
               connection.connect();

                //POST-Request Daten senden

                //GET-Daten verarbeiten
                //https://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                int counter=0;
                StringBuffer response = new StringBuffer();
                while((line = reader.readLine()) != null) {
                    counter++;
                    response.append(line);
                    response.append('\r');
                    //Anzahl der Lines rausbekommen, dann publishProgress(counter/AnzahlLines*100);
                    publishProgress((int) ((counter+1) / 1*100));
                }
                reader.close();
                return response.toString();
            }catch (Exception e){
                e.printStackTrace();
            }finally{
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
            startActivity(new Intent(LoadingActivity.this, LogInActivity.class));
        }
    }
}


