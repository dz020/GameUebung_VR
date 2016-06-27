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

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.jackson.gameuebung_3.audio.SoundMeter;
import com.google.vrtoolkit.cardboard.CardboardActivity;


public class MGDExerciseActivity extends CardboardActivity {

    private static final String TAG = "MGDExerciseActivity";
    private MGDExerciseView view; //unsere view
    static CardboardOverlayView mOverlayView;
    //private MediaPlayer mp;
    private SoundMeter mSensor;
    private int mThreshold = 5;
    private Handler mHandler = new Handler();
    private static final int POLL_INTERVAL = 600; //ist auch die verzögerung bis laser sound erklingt
    private static SoundPool soundPool;
    private int laserSound;
    private static int beepSound;
    private static int finalBeepSound;


    public static SoundPool getSoundPool() {
        return soundPool;
    }

    public static int getBeepSound() {
        return beepSound;
    }

    public static int getFinalBeepSound() {
        return finalBeepSound;
    }

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
        //mp = MediaPlayer.create(getApplicationContext(), R.raw.vogelzwitschern);
        //mp.setVolume(0.7f, 0.7f);
        //mp.setLooping(true);
        mSensor = new SoundMeter();
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        laserSound = soundPool.load(getApplicationContext(), R.raw.laser, 1); // in 2nd param u have to pass your desire ringtone
        beepSound = soundPool.load(getApplicationContext(), R.raw.beep, 2);
        finalBeepSound = soundPool.load(getApplicationContext(), R.raw.final_beep, 3);
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
        //mp.release();
        mSensor.stop();
        soundPool.autoPause();
    }

    int call_counter = 0;
    @Override
    protected void onResume() {
        super.onResume(); //erst die activity starten
        view.onResume();
        showToast();
        if(call_counter == 0){
            //mp.start();
        }else{
            //jetzt läuft zwar hintergrundmusik nicht weiter, aber dafür schmiert die app nicht ab
        }
        call_counter++;
        mSensor.start();
        Thread mythread = new Thread(mPollTask);
        mythread.start();
    }

    public static void showToast(){
        //mOverlayView.show3DToast(0 + " von " + 0 + " Bubbles");
    }

    public static void setToastText(String text){
        //mOverlayView.setText(text);
        mOverlayView.show3DToast("noise detected " + text);
        //setText(text);
    }

    private void setText(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mOverlayView.show3DToast("noise detected " + text);
            }
        });
    }

    private void callForHelp(double amplitude) {
        Log.e("activity", "callForHelp"+ amplitude);
        // Show alert when noise thersold crossed
        //Toast.makeText(getApplicationContext(), "Noise Thersold Crossed, do here your stuff.", Toast.LENGTH_LONG).show();
        mOverlayView.show3DToast("noise detected " + amplitude);
        soundPool.play(laserSound, 0.05f, 0.05f, 0, 0, 1);
    }

    // Create runnable thread to Monitor Voice
    final Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            //Log.e("Noise", "runnable mPollTask " + amp);
            if ((amp > mThreshold)) {
                callForHelp(amp);
            }
            // Runnable(mPollTask) will again execute after POLL_INTERVAL
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };
}
