package com.example.jackson.gameuebung_3.game;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.example.jackson.gameuebung_3.collision.AABB;
import com.example.jackson.gameuebung_3.graphics.Camera;
import com.example.jackson.gameuebung_3.graphics.Renderer;
import com.example.jackson.gameuebung_3.math.Matrix4x4;
import com.example.jackson.gameuebung_3.math.Vector2;

import java.util.LinkedList;

/**
 * Created by Jackson on 29.03.2016.
 */
public class MGDExerciseGame extends Game{
    public static String TAG = "MGDExerciseGame";

    private Camera camera;
    public static LinkedList<GameObject> gameObjectList = new LinkedList<>();
    private GameState gameState;
    private Renderer renderer;
    public static Context context;
    public AABB shape;
    public Vector2 shape_position_in_world;

    public MGDExerciseGame(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void initialize() {
        gameState = new GameState();
        gameState.level = 1;
        createWorld();

        gameState.setGameObject_amount(1);
        gameObjectList.add(new GameObject("box.obj", "box.png"));
        UtilityMethods.countDown();

        shape = new AABB();
        shape_position_in_world = shape.getPosition();



        renderer = new Renderer(graphicsDevice);
        //hier könnte dann gamelevel inkrementiert werden und mit gameobject amount multipliziert werden
    }


    @Override
    public void update(float deltaSeconds) {
        /*Vector3 screenTouchPosition = new Vector3( screenWidth/2-1, screenHeight/2-1, 0 );
        Vector3 worldTouchPosition = camera.unproject(screenTouchPosition, 1);

        Point touchPoint = new Point(0, 0);
        if(touchPoint.intersects(g)

        for (int i = 0; i < aabbMenu.length; ++i) {
            AABB aabb = aabbMenu[i];
            if (touchPoint.intersects(aabb)) {
                Log.e(TAG, "COLLISION DETECTED !!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }*/
        Matrix4x4.createTranslation(shape_position_in_world.getX(), shape_position_in_world.getY(), -8f);
        //Log.e(TAG, "position von shape X: " + shape_position_in_world.getX() + " Y: " + shape_position_in_world.getY());
        //Log.e(TAG, "position von gameObject X: "+gameObjectList.get(UtilityMethods.gameObjectItertor).getShape().getPosition().getX()+" Y: "+gameObjectList.get(UtilityMethods.gameObjectItertor).getShape().getPosition().getY());
        if(shape.intersects(gameObjectList.get(UtilityMethods.gameObjectItertor).getShape())){
            Log.e(TAG, "collsion detected !!!!!");
        }
    }

    int i = 0;
    @Override
    public void draw(float deltaSeconds) {
        graphicsDevice.setCamera(this.camera);
        //graphicsDevice.clear(1.0f, 0.5f, 0.0f, 1.0f, 1.0f); //hintergrund farbe ändern
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        gameObjectList.get(UtilityMethods.gameObjectItertor).getGameObjectPositionInWorldMatrix().rotateY(1f);

        renderer.drawMesh(gameObjectList.get(UtilityMethods.gameObjectItertor).getModelMesh(),
                          gameObjectList.get(UtilityMethods.gameObjectItertor).getModelMaterial(),
                          gameObjectList.get(UtilityMethods.gameObjectItertor).getGameObjectPositionInWorldMatrix());
        //Log.e(TAG, "draw aufruf: " + i);
        i++;
        //Log.e("tag", "screen width, height: " +Integer.toString(getScreenWidth())+" , "+Integer.toString(getScreenHeight()) );
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

}
