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

import android.app.ActivityManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.jackson.gameuebung_3.audio.SoundMeter;
import com.example.jackson.gameuebung_3.game.MGDExerciseGame;
import com.google.vrtoolkit.cardboard.CardboardActivity;


public class MGDExerciseActivity extends CardboardActivity {

    private static final String TAG = "MGDExerciseActivity";
    private MGDExerciseView view; //unsere view
    static CardboardOverlayView mOverlayView;
    private MediaPlayer mp;
    private static SoundMeter mSensor;
    private static int mThreshold = 5;
    private static Handler mHandler = new Handler();
    private static final int POLL_INTERVAL = 500; //ist auch die verzögerung bis laser sound erklingt
    private static int GAME_DURATION = 120000; // 120 sek bzw 2 min
    private static SoundPool soundPool;
    private static int laserSound;
    private static int beepSound;
    private static int finalBeepSound;
    private static int coinSound;
    private static int reloadSound;
    public static boolean soundPoolLoadingFinished = false;
    public static ActivityManager activityManager;
    public static ActivityManager.MemoryInfo memoryInfo;
    public static int max_highscore_entries = 5;


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
        Log.e("oncreate", "begin");
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
        mp = MediaPlayer.create(getApplicationContext(), R.raw.vogelzwitschern);
        mp.setVolume(0.7f, 0.7f);
        mp.setLooping(true);
        mSensor = new SoundMeter();

        activityManager =  (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        soundPool = new SoundPool.Builder().setMaxStreams(5).build();
        laserSound = soundPool.load(getApplicationContext(), R.raw.laser, 1); // in 2nd param u have to pass your desire ringtone
        coinSound = soundPool.load(getApplicationContext(), R.raw.coin, 2);
        reloadSound = soundPool.load(getApplicationContext(), R.raw.reload, 2);
//        beepSound = soundPool.load(getApplicationContext(), R.raw.beep, 5);
//        finalBeepSound = soundPool.load(getApplicationContext(), R.raw.final_beep, 5);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoadingFinished = true;
            }
        });

        Log.e("oncreate", "end");
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
        Log.e("onpause", "begin");
        view.onPause(); //erst unsere view pausieren
        super.onPause();
        mp.release();
        mSensor.stop();
        soundPool.release();
        MGDExerciseView.camera.release();
        Log.e("onpause", "end");
    }

    int call_counter = 0;
    @Override
    protected void onResume() {
        super.onResume(); //erst die activity starten
        Log.e("onresume", "begin");
        view.onResume();
        //showToast();
        if(call_counter == 0){
            mp.start();
        }else{
            //jetzt läuft zwar hintergrundmusik nicht weiter, aber dafür schmiert die app nicht ab
        }
        call_counter++;
        mSensor.start();
        Log.e("onresume", "end");
    }

    public static void startPollTask(){
        Thread mythread = new Thread(mPollTask);
        mythread.start();
    }

    public static void showToast(){
        //mOverlayView.show3DToast(0 + " von " + 0 + " Bubbles");
    }

    public static void setToastText(String text){
        //mOverlayView.setText(text);
        //mOverlayView.show3DToast("noise detected " + text);
        //setText(text);
    }

    public static boolean noise_deteced;
    public static void callForHelp(double amplitude) {
        Log.e("activity", "callForHelp "+ amplitude);
        // Show alert when noise thersold crossed
        //Toast.makeText(getApplicationContext(), "Noise Thersold Crossed, do here your stuff.", Toast.LENGTH_LONG).show();
        //mOverlayView.show3DToast("noise detected " + amplitude);
        soundPool.play(laserSound, 0.05f, 0.05f, 2, 0, 1);
    }

    public static void setCollision(boolean active, boolean reload){
        if(active == true && noise_deteced == true){
            if(!reload){
                soundPool.play(coinSound, 0.1f, 0.1f, 1, 0, 1);
            }else{
                soundPool.play(reloadSound, 0.15f, 0.15f, 1, 0, 1);
            }
        }
    }

    public static boolean gameIsReady = false;

    // Create runnable thread to Monitor Voice
    static final Runnable mPollTask = new Runnable() {

        private volatile boolean mIsStopped = false;
        private boolean increaseTimeAllowed = false;

        public void run() {
            double amp = mSensor.getAmplitude();
            //Log.e("Noise", "runnable mPollTask " + amp);
            if(mIsStopped == false && GAME_DURATION > 0){
                if (amp > mThreshold ) { //wenn schuss abgegeben und munition verfügbar
                    if(MGDExerciseGame.gameState.current_ammo == 0){
                        MGDExerciseGame.gameState.empty_ammo = true;
                        noise_deteced = true;
                    }else{
                        callForHelp(amp);
                        noise_deteced = true;
                        MGDExerciseGame.gameState.current_ammo--; //muss hier passieren !!
                        MGDExerciseGame.gameState.empty_ammo = false;
                    }
                }
                else{
                    noise_deteced = false;
                }
                // Runnable(mPollTask) will again execute after POLL_INTERVAL
                mHandler.postDelayed(mPollTask, POLL_INTERVAL);
                GAME_DURATION = GAME_DURATION - POLL_INTERVAL;
                if(increaseTimeAllowed == false){
                    increaseTimeAllowed = true;
                    int sec = (GAME_DURATION/1000)%60;
                    int min = (GAME_DURATION/1000)/60;
                    if( 0 <= sec && sec < 10 ){
                        MGDExerciseGame.timeText.setText(""+min+":0"+sec);
                        //if(GAME_DURATION == 0){
                          //  MGDExerciseGame.gameState.setStatus("game over");
                        //}
                    }else{
                        MGDExerciseGame.timeText.setText(""+min+":"+sec);
                    }
                    //Log.e("übrige zeit", ""+(GAME_DURATION/1000));
                }else{
                    increaseTimeAllowed = false;
                }
            }
            else{
                //stop();
                MGDExerciseGame.gameState.setStatus("game over");

                int current_score = (int) MGDExerciseGame.gameState.current_score;
                //UtilityMethods.saveCurrentScore(getPreferences(MODE_PRIVATE), current_score);
                //int[] highscore_array = UtilityMethods.loadAndSortHighScore(getPreferences(MODE_PRIVATE));
                //int pos = UtilityMethods.checkHighScorePosition(highscore_array, current_score);
                //Log.e("DEINE HIGHSCORE POSI: ", ""+pos);

            }
        }

        private void setStopped(boolean isStop) {
            if (mIsStopped != isStop)
                mIsStopped = isStop;
        }

        public void stop() {
            setStopped(true);
            Log.e("stop", "zeit vorbei-------------------------------------");
        }
    };
}
