package com.example.jackson.gameuebung_3.game;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.jackson.gameuebung_3.MGDExerciseActivity;
import com.example.jackson.gameuebung_3.Mesh;
import com.example.jackson.gameuebung_3.graphics.Texture;
import com.example.jackson.gameuebung_3.math.Matrix4x4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                            //Log.e(MGDExerciseGame.TAG, "Sekunde: "+millisUntilFinished);
                            MGDExerciseActivity.getSoundPool().play(beepSound, 0.05f, 0.05f, 0, 0, 1);
                        }
                    }

                    @Override
                    public void onFinish() {
                        int finalBeepSound = MGDExerciseActivity.getFinalBeepSound();
                        MGDExerciseActivity.getSoundPool().play(finalBeepSound, 0.05f, 0.05f, 0, 0, 1);
                        //Log.e("TAG", "countdown abgelaufen");
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
        //Log.e(MGDExerciseGame.TAG, "game object erzeugt mit rotation winkel: " + rotation);
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

    public static List<List<String>> getListFromCSV(String csvFileName) throws IOException {

        String line = null;
        BufferedReader stream = null;
        List<List<String>> csvData = new ArrayList<List<String>>();

        try {
            AssetManager assetManager = MGDExerciseGame.context.getAssets();
            stream = new BufferedReader(new InputStreamReader(assetManager.open(csvFileName)));
            while ((line = stream.readLine()) != null) {
                String[] splitted = line.split(";");
                System.out.println(line);
                //Log.e("linieee aus csv", line);
                List<String> dataLine = new ArrayList<String>(splitted.length);
                for (String data : splitted)
                    dataLine.add(data);
                csvData.add(dataLine);
            }
        } finally {
            if (stream != null)
                stream.close();
        }

        return csvData;

    }

    public static int checkHighScorePosition(int[] highscore_array, int current_score){
        int rang = 0;
        for(int i = 0; i < highscore_array.length; i++){
            if(highscore_array[i] == current_score){
                rang = i;
            }
        }
        return (highscore_array.length - rang);
    }

    public static int[] loadAndSortHighScore(SharedPreferences sharedPref){
        final SharedPreferences sharedPreferences = sharedPref;
        Map<String, ?> prefMap = sharedPreferences.getAll();
        Set<String> keySet = prefMap.keySet();
        int[] highscore_array = new int[prefMap.size()];
        int i = 0;
        for(String s : keySet){
            Log.e("wert(i):", "i: "+i+ " wert: " + prefMap.get(s).toString());
            highscore_array[i] = Integer.valueOf(prefMap.get(s).toString());
            i++;
        }

        Log.e("jetzt", "wird sortiert");

        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                return 0;
            }
        };
        Arrays.sort(highscore_array);
        if(sharedPreferences.edit().clear().commit()){
            SharedPreferences.Editor save_sorted_editor = sharedPreferences.edit();
            for(int y = 0; y<highscore_array.length; y++){
                Log.e("sortiert: ", "i: "+y+" wert: "+highscore_array[y]);
                save_sorted_editor.putInt(""+y, highscore_array[y]).commit();
            }
        }
        return highscore_array;
    }

    public static void saveCurrentScore(SharedPreferences sharedPref, int current_score){
        final SharedPreferences sharedPreferences = sharedPref;
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        Map allPrefs = sharedPreferences.getAll();
        Set<String> set = allPrefs.keySet();
        int set_size = set.size();
        Log.e("zufallszahl und zz: ", "setsize "+set_size+" zz: "+current_score);
        editor.putInt(""+set_size, current_score);
        Boolean commitWorkedFine = editor.commit();
        Log.e("gespeichert", "alle werte gespeichert "+ commitWorkedFine);
        Log.e("groesse: ", ""+set.size());
    }
}