/*
 * Copyright 2014 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jackson.gameuebung_3;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.vrtoolkit.cardboard.CardboardActivity;


public class MGDExerciseActivity extends CardboardActivity {

    private static final String TAG = "MGDExerciseActivity";
    private MGDExerciseView view; //unsere view
    static CardboardOverlayView mOverlayView;
    private MediaPlayer mp;

    /**
     * Sets the view to our CardboardView and initializes the transformation matrices we will use
     * to render our scene.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.common_ui);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = (MGDExerciseView) findViewById(R.id.cardboard_view);
        view.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        view.setRenderer(view);
        setCardboardView(view);
        mOverlayView = (CardboardOverlayView) findViewById(R.id.overlay);
    }

    /**
     * Increment the score, hide the object, and give feedback if the user pulls the magnet while
     * looking at the object. Otherwise, remind the user what to do.
     */
    @Override
    public void onCardboardTrigger() {
    }

    @Override
    protected void onPause() {
        view.onPause(); //erst unsere view pausieren
        super.onPause();
        mp.release();
    }

    @Override
    protected void onResume() {
        super.onResume(); //erst die activity starten
        view.onResume();
        showToast();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.vogelzwitschern);
        mp.setLooping(true);
        mp.start();
    }

    public static void showToast(){
        mOverlayView.show3DToast(0 + " von " + 0 + " Bubbles");
    }

    public static void setToastText(String text){
        mOverlayView.setText(text);
    }

}
