package de.caroliwo.hawoe_rallye;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class LoadingActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.progressBar);

        DownloadJSON downloadJSON = new DownloadJSON(progressBar, getApplicationContext());
        //downloadJSON.execute("https://samples.openweathermap.org/data/2.5/forecast?q=M%C3%BCnchen,DE&appid=b6907d289e10d714a6e88b30761fae22"); //Beispiellink
        downloadJSON.execute("https://haw.t0b1.com/zeiten.json");

    }
}


