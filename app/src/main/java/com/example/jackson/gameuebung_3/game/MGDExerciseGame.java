package com.example.jackson.gameuebung_3.game;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;

import com.example.jackson.gameuebung_3.MGDExerciseActivity;
import com.example.jackson.gameuebung_3.Mesh;
import com.example.jackson.gameuebung_3.graphics.Camera;
import com.example.jackson.gameuebung_3.graphics.Material;
import com.example.jackson.gameuebung_3.graphics.Renderer;
import com.example.jackson.gameuebung_3.graphics.Texture;
import com.example.jackson.gameuebung_3.math.Matrix4x4;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * Created by Jackson on 29.03.2016.
 */
public class MGDExerciseGame extends Game{
    private static String TAG = "MGDExerciseGame";
    private Mesh modelMesh;
    private Camera camera;
    private static LinkedList<Matrix4x4> gameObjectList = new LinkedList<>();
    private GameState gameState;
    private Matrix4x4 viewMatrix;
    private Texture modelTexture;
    private Renderer renderer;
    private Material modelMaterial;
    private Matrix4x4 sphereMatrix;
    private Mesh sphereMesh;
    private Texture sphereTexture;
    private Material sphereMaterial;

    public MGDExerciseGame(Context context) {
        super(context);
    }

    @Override
    public void initialize() {
        gameState = new GameState();
        gameState.level = 1;
        createWorld();
        gameState.setGameObject_amount(8);
        createScoreAlert(gameState.getGameObject_amount());
        createGameObjects("box.obj", "box.png", gameState.getGameObject_amount());
        modelMaterial = new Material();
        modelMaterial.setTexture(modelTexture);


        sphereMesh = loadMesh("box.obj");
        sphereTexture = loadTexture("bubble.png");
        sphereMatrix = new GameObject(0).getGameObject();
        sphereMatrix.scale(3);
        sphereMaterial = new Material();
        sphereMaterial.setTexture(sphereTexture);

        renderer = new Renderer(graphicsDevice);
        //hier könnte dann gamelevel inkrementiert werden und mit gameobject amount multipliziert werden
    }

    @Override
    public void update(float deltaSeconds) {
    }

    @Override
    public void draw(float deltaSeconds) {
        graphicsDevice.setCamera(this.camera);
        graphicsDevice.clear(1.0f, 0.5f, 0.0f, 1.0f, 1.0f); //hintergrund farbe ändern
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        for(int i=0; i<8; i++){
            gameObjectList.get(i).rotateY(0.2f);
            renderer.drawMesh(modelMesh, modelMaterial, gameObjectList.get(i));
        }
        renderer.drawMesh(sphereMesh, sphereMaterial, sphereMatrix);
    }

    @Override
    public void resize(int width, int height) {
        //ständig bildschirm seitenverhältnis ausrechnen
        float aspect_ratio = ((float) width)/ ((float) height); //aspect ratio = seitenverhältnis
        Matrix4x4 projectionMatrix = new Matrix4x4();
        projectionMatrix.setPerspectiveProjection(0.1f, 0.1f * aspect_ratio, -0.1f, -0.1f * aspect_ratio, 0.1f, 1000.0f); //wenn handy gedreht, dann wird bildschirmausschnitt breiter um aspect ratio
        camera.setM_projection(projectionMatrix);
    }

    @Override
    public void loadContent() {
        //TODO texture neu laden wenn app aus hintergrund wieder in den vordergrund kommt
    }

    public void setCameraParameters(float[] perspective, float[] eyeView) {
        camera.setM_projection(new Matrix4x4(perspective));
        camera.setM_view(new Matrix4x4(eyeView));
    }

    public static int getBubbleAmount(){
        return gameObjectList.size();
    }

    public void createGameObjects(String obj_filename, String texture_filename, int amount){
        modelMesh = loadMesh(obj_filename);
        modelTexture = loadTexture(texture_filename);
        float rotation = 0;
        float translation = 0;
        for(int i=0; i<amount; i++){
            Matrix4x4 box = new GameObject(translation).getGameObject();
            Matrix4x4 rotated_box = Matrix4x4.createRotationY(rotation);
            Matrix4x4 tmp = Matrix4x4.multiply(rotated_box, box);
            rotation += 45;
            gameObjectList.add(tmp);
            translation += 100;
        }
    }

    public Mesh loadMesh(String filename){
        try{
            return Mesh.loadFromOBJ(context.getAssets().open(filename));
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void createWorld(){
        camera = new Camera();
        Matrix4x4 projectionMatrix = new Matrix4x4();
        projectionMatrix.setPerspectiveProjection(0.1f, 0.1f, -0.1f, -0.1f, 0.1f, 100.0f); //wird in resize überschrieben
        camera.setM_projection(projectionMatrix);
        viewMatrix = new Matrix4x4();
        viewMatrix.translate(0.0f, 10.0f, -30.0f);
        camera.setM_view(viewMatrix);
    }

    public Texture loadTexture(String filename){
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream_box = assetManager.open(filename);
             return graphicsDevice.createTexture(inputStream_box);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createScoreAlert(final int amount){
        ((MGDExerciseActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MGDExerciseActivity.setToastText(Integer.toString((amount)));
            }
        });
    }

}
