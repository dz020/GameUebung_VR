package com.example.jackson.gameuebung_3.game;

import com.example.jackson.gameuebung_3.Mesh;
import com.example.jackson.gameuebung_3.collision.Shape3D;
import com.example.jackson.gameuebung_3.collision.Sphere;
import com.example.jackson.gameuebung_3.graphics.CompareFunction;
import com.example.jackson.gameuebung_3.graphics.Material;
import com.example.jackson.gameuebung_3.graphics.Texture;
import com.example.jackson.gameuebung_3.math.Matrix4x4;
import com.example.jackson.gameuebung_3.math.Vector3;

import java.util.Random;

/**
 * Created by Jackson on 31.05.2016.
 */
public class GameObject{

    private Matrix4x4 position_in_world;
    private float[] positions = new float[]{-5f, -10f, 0f, 5f, 10f};
    public Mesh modelMesh;
    public Texture modelTexture;
    public Material modelMaterial;
    public Shape3D shape;
    public boolean destroyed = false;

    public GameObject(String mesh_filname, String texture_filename){
        if(true){
            position_in_world = new Matrix4x4();
            position_in_world.setIdentity(); //skalierung auf 1 statt auf 0 => mal 1
            position_in_world.rotateX(0); //rotiert den stern so, dass er direkt zu uns zeigt
            //bubbleObject.translate(((float) Math.random() * 50 ), ((float) Math.random() * 10 ), ( (float) Math.random() * 10) + translation ); //x,y,z z verschiebt im raum
            //radius der kreisbahn ist 8
            //gute werte sind -10, -5, 0, 5, 10
            //bubbleObject.translate(0.0f, generateRandomPosition(), -8.0f); //x,y,z z verschiebt im raum
            position_in_world.translate(0.0f, 0.0f, -8.0f); //x,y,z z verschiebt im raum
            modelMesh = UtilityMethods.loadMesh(mesh_filname);
            modelTexture = UtilityMethods.loadTexture(texture_filename);
            modelMaterial = new Material();
            modelMaterial.setTexture(modelTexture);
            modelMaterial.setDepthTestFunction(CompareFunction.ALWAYS); //sorgt dafür dass ein z index erzeugt wird

            shape = new Sphere(getMittelpunkt(), 1f);
        }
    }

    public Matrix4x4 getGameObjectPositionInWorldMatrix() {
        return this.position_in_world;

    }

    public void setPosition_in_world(Matrix4x4 position_in_world) {
        this.position_in_world = position_in_world;
        shape.setPosition(getMittelpunkt());
    }

    public Mesh getModelMesh() {
        return modelMesh;
    }

    public Texture getModelTexture() {
        return modelTexture;
    }

    public Shape3D getShape() {
        return shape;
    }

    private float generateRandomPosition(){
        return positions[new Random().nextInt(positions.length)];
    }

    public Material getModelMaterial() {
        return modelMaterial;
    }

    public Vector3 getMittelpunkt(){
        return new Vector3(position_in_world.m[12], position_in_world.m[13], position_in_world.m[14]);
    }

    public void setDestroyed(){
        destroyed = true;
        modelTexture = UtilityMethods.loadTexture("red_shape.png");
        modelMaterial = new Material();
        modelMaterial.setTexture(modelTexture);
    }
}
