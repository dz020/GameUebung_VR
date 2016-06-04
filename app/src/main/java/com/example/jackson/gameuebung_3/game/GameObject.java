package com.example.jackson.gameuebung_3.game;

import com.example.jackson.gameuebung_3.math.Matrix4x4;

/**
 * Created by Jackson on 31.05.2016.
 */
public class GameObject {

    private boolean isBubble = false;
    private Matrix4x4 bubbleObject;

    public GameObject(boolean isBubble, float translation){
        this.isBubble = isBubble;
        if(isBubble == true){
            bubbleObject = new Matrix4x4();
            bubbleObject.setIdentity(); //skalierung auf 1 statt auf 0 => mal 1
            bubbleObject.rotateX(0); //rotiert den stern so, dass er direkt zu uns zeigt

            //erster para verschiebt mit + nach hinten in den raum
            //zweiter para verschiebt mit + nach oben
            //dritter para verschiebt mit + nach
            //bubbleObject.translate(((float) Math.random() * 50 ), ((float) Math.random() * 10 ), ( (float) Math.random() * 10) + translation ); //x,y,z z verschiebt im raum
            //radius der kreisbahn ist 8
            bubbleObject.translate(0.0f, 2.0f, -8.0f); //x,y,z z verschiebt im raum
        }
    }

    public Matrix4x4 getGameObject(){
        return this.bubbleObject;
    }

}
