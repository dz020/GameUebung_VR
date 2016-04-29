package com.example.jackson.gameuebung_3;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.jackson.gameuebung_3.game.MGDExerciseGame;

/**
 * Created by Jackson on 29.03.2016.
 */
public class MGDExerciseView extends GLSurfaceView{

    private MGDExerciseGame mgdExerciseGame;

    public MGDExerciseView(Context context) {
        super(context);
        mgdExerciseGame = new MGDExerciseGame(context);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0); //kp warum aber sonst schmiert die app ab
        setRenderer(mgdExerciseGame);
        setRenderMode(RENDERMODE_CONTINUOUSLY); //macht das rendern flüssiger

    }
}
