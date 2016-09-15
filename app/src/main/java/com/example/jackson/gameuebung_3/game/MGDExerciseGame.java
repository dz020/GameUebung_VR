package com.example.jackson.gameuebung_3.game;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.example.jackson.gameuebung_3.MGDExerciseActivity;
import com.example.jackson.gameuebung_3.graphics.Camera;
import com.example.jackson.gameuebung_3.graphics.Renderer;
import com.example.jackson.gameuebung_3.math.Matrix4x4;
import com.example.jackson.gameuebung_3.math.Vector3;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jackson on 29.03.2016.
 */
public class MGDExerciseGame extends Game{
    public static String TAG = "MGDExerciseGame";

    private Camera camera;
    public static LinkedList<GameObject> gameObjectList = new LinkedList<>();
    public static GameState gameState;
    private Renderer renderer;
    public static Context context;
    private Vector3 forwardVector = new Vector3();
    private Matrix4x4 headView;

    public MGDExerciseGame(Context context) {
        super(context);
        this.context = context;
    }

    GameObject fadenkreuz;
    GameObject menu_bg;
    GameObject menu_btn;
    GameObject munitions_box;
    Matrix4x4 fadenkreuzMatrix;

    @Override
    public void initialize() {
        gameState = new GameState();
        gameState.level = 1;
        createWorld();

        Log.e(TAG, "test loggg");

        try {
            List<List<String>> list = UtilityMethods.getListFromCSV("mixed.csv");
            for(int i = 0; i< list.size(); i++){
                GameObject gameObject = new GameObject("box.obj", "box.png");
                gameObject.addData(list.get(i));
                gameObjectList.add(gameObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameState.setGameObject_amount(1);

        fadenkreuz = new GameObject("quad.obj", "fadenkreuz.png");
        fadenkreuz.makeShapeVisible("sphere.obj", "yellow.png");

        menu_bg = new GameObject("quad.obj", "menu.png");
        menu_btn = new GameObject("quad.obj", "highscore.png");

        Matrix4x4 tmp = Matrix4x4.createTranslation(-0.5f, 0.5f, -8f);
        Matrix4x4 test = new Matrix4x4(tmp);
        menu_bg.setPosition_in_world(tmp.scale(6,10,1));
        menu_btn.setPosition_in_world(test.scale(1.3f, 0.7f, 1.0f ));

        munitions_box = new GameObject("box.obj", "munition.png");
        munitions_box.setPosition_in_world(test.scale(1.3f, 0.7f, 1.0f ));
        munitions_box.setType("munitions_box");
        gameObjectList.add(munitions_box);

        renderer = new Renderer(graphicsDevice);
        //hier könnte dann gamelevel inkrementiert werden und mit gameobject amount multipliziert werden
    }

    public void setForwardVector(Vector3 forwardVector) {
        this.forwardVector = forwardVector;
    }

    @Override
    public void update(float deltaSeconds) {

        //Matrix4x4.createTranslation(shape_position_in_world.getX(), shape_position_in_world.getY(), -8f);
        //Log.e(TAG, "position von shape X: " + shape_position_in_world.getX() + " Y: " + shape_position_in_world.getY());
        //Log.e(TAG, "position von gameObject X: "+gameObjectList.get(UtilityMethods.gameObjectItertor).getShape().getPosition().getX()+" Y: "+gameObjectList.get(UtilityMethods.gameObjectItertor).getShape().getPosition().getY());

        /*
        Log.e(TAG, "forward vector: x: " + forwardVector.getX() + " y: " + forwardVector.getY() + " z: " + forwardVector.getZ());
        Vector3 fadenkreuzPos = Vector3.multiply(-8, forwardVector);
        fadenkreuz.setPosition_in_world(Matrix4x4.createTranslation(fadenkreuzPos.getX(), fadenkreuzPos.getY(), fadenkreuzPos.getZ()));
        */
        Matrix4x4 fadenkreuzWorldMatrix = Matrix4x4.multiply(headView.getInverse(), Matrix4x4.createTranslation(-0.5f, 0.5f, -8f));
        fadenkreuz.setPosition_in_world(fadenkreuzWorldMatrix);

        if(gameState.game_over == false) {

            for (int i = 0; i < gameObjectList.size(); i++) {
                if (fadenkreuz.getShape().intersects(gameObjectList.get(i).getShape(), gameObjectList.get(i).getOrbit())) {
                    //Log.e(TAG, "collsion detected !!!!!");
                    MGDExerciseActivity.setCollision(true);
                    if (MGDExerciseActivity.noise_deteced == true && gameObjectList.get(i).destroyed == false) {
                        //Log.e(TAG, "noise deteced und abgeschossen");
                        gameObjectList.get(i).setDestroyed();
                        if(gameObjectList.get(i).getType().equals("munitions_box")){
                            gameState.current_ammo = gameState.current_ammo + gameState.increase_ammo; //aktuell um 3 erhöhen
                            if(gameState.current_ammo > gameState.max_ammo){
                                gameState.current_ammo = gameState.max_ammo;
                            }
                        }else{
                            gameState.setCurrent_score(gameState.getCurrent_score() + gameObjectList.get(i).points);
                            Log.e("aktueller score:", "" + gameState.getCurrent_score());
                        }
                    }
                } else {
                    //gameObjectList.get(i).setModelTexture("box.png");
                    MGDExerciseActivity.setCollision(false);
                }
            }
        }else{
            menu_btn.setModelTexture("highscore.png");
            if(  fadenkreuz.getShape().intersects(menu_btn.getShape(), 0.5f )){
                Log.e(TAG, "menu button kollision");
                menu_btn.setModelTexture("highscore_hovered.png");
            }
        }
    }

    @Override
    public void draw(float deltaSeconds) {
        graphicsDevice.setCamera(this.camera);
        if(gameState.game_over == false){
            GLES20.glClearDepthf(1.0f);
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);

            for(int i=0; i< gameObjectList.size(); i++){
                GameObject gameObject = gameObjectList.get(i);
                //if(gameObject.destroyed == false){
                gameObject.getGameObjectPositionInWorldMatrix().rotateY(1f);
                renderer.drawMesh(gameObject.getModelMesh(), gameObject.getModelMaterial(), gameObject.getGameObjectPositionInWorldMatrix());
                //renderer.drawMesh(gameObject.shape.getMesh(), gameObject.shape.getMaterial(), gameObject.getGameObjectPositionInWorldMatrix());
                //}
            }

//            renderer.drawMesh(munitions_box.getModelMesh(), munitions_box.getModelMaterial(), munitions_box.getGameObjectPositionInWorldMatrix());

            renderer.drawMesh(fadenkreuz.getModelMesh(), fadenkreuz.getModelMaterial(), fadenkreuz.getGameObjectPositionInWorldMatrix());
        }
        else{
            graphicsDevice.clear(1.0f, 0.5f, 0.0f, 1.0f, 1.0f); //hintergrund farbe ändern
            renderer.drawMesh(menu_bg.getModelMesh(), menu_bg.getModelMaterial(), menu_bg.getGameObjectPositionInWorldMatrix());
            renderer.drawMesh(menu_btn.getModelMesh(), menu_btn.getModelMaterial(), menu_btn.getGameObjectPositionInWorldMatrix());
            renderer.drawMesh(fadenkreuz.getModelMesh(), fadenkreuz.getModelMaterial(), fadenkreuz.getGameObjectPositionInWorldMatrix());
        }
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

    public void createWorld(){
        camera = new Camera();
        Matrix4x4 projectionMatrix = new Matrix4x4();
        projectionMatrix.setPerspectiveProjection(0.1f, 0.1f, -0.1f, -0.1f, 0.1f, 100.0f); //wird in resize überschrieben
        camera.setM_projection(projectionMatrix);
        Matrix4x4 viewMatrix = new Matrix4x4();
        viewMatrix.translate(0.0f, 10.0f, -30.0f);
        camera.setM_view(viewMatrix);
    }

    public void setHeadView(Matrix4x4 headView) {
        this.headView = headView;
        forwardVector.setX(headView.m[2]);
        forwardVector.setY(headView.m[6]);
        forwardVector.setZ(headView.m[10]);
    }
}
