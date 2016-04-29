package com.example.jackson.gameuebung_3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MGDExerciseActivity extends Activity {

    private MGDExerciseView mgdExerciseView; //unsere view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mgdExerciseView = new MGDExerciseView(getApplicationContext()); //view erzeugen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mgdExerciseView); //view setzen

        //damit view im fullscreen läuft:

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onPause() {
        mgdExerciseView.onPause(); //erst unsere view pausieren
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume(); //erst die activity starten
        mgdExerciseView.onResume();
    }
}