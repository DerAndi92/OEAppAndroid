package de.caroliwo.hawoe_rallye;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class DownloadJSON extends AsyncTask<String, Integer, List<Task>> {
    private ProgressBar progressBar;
    private Context applicationContext;
    private List<Task> taskList;

    public DownloadJSON (ProgressBar progressBar, Context applicationContext){
        this.progressBar = progressBar;
        this.applicationContext = applicationContext;
    }

    @Override
    protected List<Task> doInBackground(String... urls) {
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

            //https://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
            //https://futurestud.io/tutorials/gson-mapping-of-null-values
            //GSON to JavaObjekt
            Gson gson = new Gson();
            String jsonTest = "[{\"name\":\"Produktionslabor\",\"icon\":\"ic_clapperboard_icon\",\"time\":\"11.15-13.00 Uhr\",\"destination\":\"EG im Neubau\",\"numberOfTasks\":2,\"answerFieldNeeded\":true,\"passwordNeeded\":true,\"buttonNeeded\":true,\"buttonText\":\"Abgeben\"},{\"name\":\"Wettrennen\",\"icon\":\"ic_car_icon\",\"time\":\"12.00-15.00 Uhr\",\"destination\":\"U36 im Altbau\",\"numberOfTasks\":1,\"answerField\":false,\"password\":false,\"button\":true,\"buttonText\":\"Erledigt\"}]";

            //Liste mit JavaObjekten erstellen
            Type taskListType = new TypeToken<ArrayList<Task>>(){}.getType(); //Typ der Liste erkennen
            taskList = gson.fromJson(jsonTest, taskListType);

            return taskList;

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
    protected void onPostExecute(List<Task> s) {
        Intent intent = new Intent(applicationContext, LogInActivity.class);
        //Hier einfügen: Übergabe von TaskList im Intent per Serializable (langsam) oder Parcelable
        applicationContext.startActivity(intent);
    }
}