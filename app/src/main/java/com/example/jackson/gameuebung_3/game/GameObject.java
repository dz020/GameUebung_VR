package com.example.jackson.gameuebung_3.game;

import com.example.jackson.gameuebung_3.math.Matrix4x4;

import java.util.Random;

/**
 * Created by Jackson on 31.05.2016.
 */
public class GameObject /*implements Shape2D*/{

    private Matrix4x4 bubbleObject;
    private float[] positions = new float[]{-5f, -10f, 0f, 5f, 10f};

    public GameObject(){
        if(true){
            bubbleObject = new Matrix4x4();
            bubbleObject.setIdentity(); //skalierung auf 1 statt auf 0 => mal 1
            bubbleObject.rotateX(0); //rotiert den stern so, dass er direkt zu uns zeigt
            //bubbleObject.translate(((float) Math.random() * 50 ), ((float) Math.random() * 10 ), ( (float) Math.random() * 10) + translation ); //x,y,z z verschiebt im raum
            //radius der kreisbahn ist 8
            //gute werte sind -10, -5, 0, 5, 10
            //bubbleObject.translate(0.0f, generateRandomPosition(), -8.0f); //x,y,z z verschiebt im raum
            bubbleObject.translate(0.0f, 0.0f, -8.0f); //x,y,z z verschiebt im raum
        }
    }

    public Matrix4x4 getGameObject() {
        return this.bubbleObject;
    }

    private float generateRandomPosition(){
        return positions[new Random().nextInt(positions.length)];
    }

}
