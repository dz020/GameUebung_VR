package com.example.jackson.gameuebung_3.game;

import android.content.res.AssetManager;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.jackson.gameuebung_3.MGDExerciseActivity;
import com.example.jackson.gameuebung_3.Mesh;
import com.example.jackson.gameuebung_3.graphics.Texture;
import com.example.jackson.gameuebung_3.math.Matrix4x4;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jackson on 27.06.2016.
 */
public class UtilityMethods {

    public static Mesh loadMesh(String filename){
        try{
            return Mesh.loadFromOBJ(MGDExerciseGame.context.getAssets().open(filename));
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Texture loadTexture(String filename){
        AssetManager assetManager = MGDExerciseGame.context.getAssets();
        try {
            InputStream inputStream_box = assetManager.open(filename);
            return Game.graphicsDevice.createTexture(inputStream_box);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void countDown(){
        //countdown läuft 5 sekunden und zählt alle 1 sek
        Log.e(MGDExerciseGame.TAG, "countDown gestartet");
        ((MGDExerciseActivity) MGDExerciseGame.context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int beepSound = MGDExerciseActivity.getBeepSound();
                        if(millisUntilFinished>1000){
                            Log.e(MGDExerciseGame.TAG, "Sekunde: "+millisUntilFinished);
                            MGDExerciseActivity.getSoundPool().play(beepSound, 0.05f, 0.05f, 0, 0, 1);
                        }
                    }

                    @Override
                    public void onFinish() {
                        int finalBeepSound = MGDExerciseActivity.getFinalBeepSound();
                        MGDExerciseActivity.getSoundPool().play(finalBeepSound, 0.05f, 0.05f, 0, 0, 1);
                        Log.e("TAG", "countdown abgelaufen");
                        repositionGameObject(new GameObject("box.obj", "box.png"));
                        //createGameObject("box.obj", "box.png");
//                        renderer.drawMesh(modelMesh, modelMaterial, gameObjectList.get(1));
                        // draw(0);
                    }
                }.start();
            }
        });
    }

    public static float rotation = 45;
    public static int gameObjectItertor = 0;

    public static void repositionGameObject(GameObject gameObject){
        Matrix4x4 box = gameObject.getGameObjectPositionInWorldMatrix();
        Matrix4x4 rotated_box = Matrix4x4.createRotationY(rotation);
        Log.e(MGDExerciseGame.TAG, "game object erzeugt mit rotation winkel: " + rotation);
        Matrix4x4 tmp = Matrix4x4.multiply(rotated_box, box);
        if(rotation == 360){
            rotation = 45;
        }else{
            rotation += 45;
        }
        MGDExerciseGame.gameObjectList.get(gameObjectItertor).setPosition_in_world(tmp);
        UtilityMethods.countDown();
    }

    public void createScoreAlert(final int amount){
        ((MGDExerciseActivity) MGDExerciseGame.context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MGDExerciseActivity.setToastText(Integer.toString((amount)));
            }
        });
    }
}
