package com.example.jackson.gameuebung_3.game;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

import com.example.jackson.gameuebung_3.Mesh;
import com.example.jackson.gameuebung_3.graphics.Camera;
import com.example.jackson.gameuebung_3.graphics.CompareFunction;
import com.example.jackson.gameuebung_3.graphics.Texture;
import com.example.jackson.gameuebung_3.math.Matrix4x4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jackson on 29.03.2016.
 */
public class MGDExerciseGame extends Game{
    private static String TAG = "MGDExerciseGame";
    private  long lastTime = 0;
    private Mesh cube;
    private Mesh road;
    private Camera camera;
    private Matrix4x4 world_cube;
    private Matrix4x4 word_road;

    public MGDExerciseGame(Context context) {
        super(context);
    }


    @Override
    public void initialize() {
        try{
            cube = Mesh.loadFromOBJ(context.getAssets().open("box.obj"));
            road = Mesh.loadFromOBJ(context.getAssets().open("road.obj"));
        }catch(IOException e){
            e.printStackTrace();
        }
        camera = new Camera();
        Matrix4x4 projectionMatrix = new Matrix4x4();
        projectionMatrix.setPerspectiveProjection(0.1f, 0.1f, -0.1f, -0.1f, 0.1f, 100.0f); //wird in resize überschrieben
        camera.setM_projection(projectionMatrix);
        Matrix4x4 viewMatrix = new Matrix4x4();

        /* cube */
            world_cube = new Matrix4x4();
            world_cube.setIdentity(); //skalierung auf 1 statt auf 0 => mal 1
            world_cube.rotateX(0); //rotiert den stern so, dass er direkt zu uns zeigt
            world_cube.rotateY(0);
            world_cube.translate(0.0f, 5.0f, -20.0f); //x,y,z z verschiebt im raum
            world_cube.scale(4.0f);

        /* straße */
            word_road = new Matrix4x4();
            word_road.setIdentity();
            word_road.rotateX(0); //-90 heißt wir gucken genau drauf
            word_road.translate(0.0f, -50.0f, 0.0f); //x,y,z z verschiebt im raum
            word_road.scale(40.0f, 1.0f, 70.0f); //x,y,z, 25 mach die straße breit, wir gucken von oben drauf
        //---------------------------------------------------
        viewMatrix.translate(0.0f, 10.0f, -30.0f);
        camera.setM_view(viewMatrix);
    }

    @Override
    public void update(float deltaSeconds) {
        //world_cube = Matrix4x4.multiply(world_cube, Matrix4x4.createRotationY(deltaSeconds * 180.0f)); // Bogenmaß, 1 drehung pro deltaSecond
    }

    @Override
    public void draw(float deltaSeconds) {
        AssetManager assetManager = context.getAssets();
        graphicsDevice.setCamera(this.camera);
        //graphicsDevice.clear(1.0f, 0.5f, 0.0f, 1.0f, 1.0f); //hintergrund farbe ändern

        GLES20.glClearDepthf(1.0f);
        //GLES20.glClearColor(1.0f, 0, 0, 1);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        /* cube */
            world_cube.rotateY(4); //dreht den stern um sich selbst mit ... grad pro deltaSecond
            Log.d(TAG, "draw: CubeMatrix " + world_cube.toString());
            graphicsDevice.setWorldMatrix(world_cube);
            //graphicsDevice.unbindTexture();
            try { //TODO problem bei jedem draw  wird die textur neu geladen
                InputStream inputStream_box = assetManager.open("box.png");
                 Texture box_texture = graphicsDevice.createTexture(inputStream_box);
                graphicsDevice.bindTexture(box_texture);
            } catch (IOException e) {
                e.printStackTrace();
            }
            graphicsDevice.bindVertexBuffer(cube.getVertexBuffer());
            //graphicsDevice.setColor(1.0f, 0.0f, 0.0f, 1.0f);
            graphicsDevice.setDepthWrite(true);
            graphicsDevice.setDepthTest(CompareFunction.LESS_OR_EQUAL);
            graphicsDevice.draw(cube.getMode(), 0, cube.getVertexBuffer().getVertex_amount());


       /* straße */
            graphicsDevice.setWorldMatrix(word_road);
            //graphicsDevice.setColor(0.0f, 1.0f, 0.0f, 1.0f);
            try {
                InputStream inputStream_road = assetManager.open("road.png");
                Texture road_texture = graphicsDevice.createTexture(inputStream_road);
                graphicsDevice.bindTexture(road_texture);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            graphicsDevice.bindVertexBuffer(road.getVertexBuffer());
            graphicsDevice.draw(road.getMode(), 0, road.getVertexBuffer().getVertex_amount());
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
}
