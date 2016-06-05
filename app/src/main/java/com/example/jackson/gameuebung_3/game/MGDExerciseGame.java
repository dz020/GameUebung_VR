package com.example.jackson.gameuebung_3.game;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;

import com.example.jackson.gameuebung_3.Mesh;
import com.example.jackson.gameuebung_3.graphics.Camera;
import com.example.jackson.gameuebung_3.graphics.CompareFunction;
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
    private Mesh cube;
    private Camera camera;
    private Texture box_texture;
    private static LinkedList<Matrix4x4> boxes;

    public MGDExerciseGame(Context context) {
        super(context);
    }


    @Override
    public void initialize() {
        try{
            cube = Mesh.loadFromOBJ(context.getAssets().open("box.obj"));
        }catch(IOException e){
            e.printStackTrace();
        }
        camera = new Camera();
        Matrix4x4 projectionMatrix = new Matrix4x4();
        projectionMatrix.setPerspectiveProjection(0.1f, 0.1f, -0.1f, -0.1f, 0.1f, 100.0f); //wird in resize überschrieben
        camera.setM_projection(projectionMatrix);
        Matrix4x4 viewMatrix = new Matrix4x4();

        /* cube */
            boxes = new LinkedList<>();
            float translation = 0;
            for(int i=0; i<2; i++){
                boxes.add(new GameObject(translation).getGameObject());
                translation += 100;
            }

        AssetManager assetManager = context.getAssets();
        try { //TODO problem bei jedem draw  wird die textur neu geladen
            InputStream inputStream_box = assetManager.open("box.png");
            box_texture = graphicsDevice.createTexture(inputStream_box);

        } catch (IOException e) {
            e.printStackTrace();
        }

        viewMatrix.translate(0.0f, 10.0f, -30.0f);
        camera.setM_view(viewMatrix);

        // TODO --- TOAST UPDATE GEHT NICHT WEGEN ANDEREM THREAD
        //MGDExerciseActivity.setToastText(Integer.toString( boxes.size() ));
    }

    @Override
    public void update(float deltaSeconds) {
        //world_cube = Matrix4x4.multiply(world_cube, Matrix4x4.createRotationY(deltaSeconds * 180.0f)); // Bogenmaß, 1 drehung pro deltaSecond
    }

    @Override
    public void draw(float deltaSeconds) {
        graphicsDevice.setCamera(this.camera);
        //graphicsDevice.clear(1.0f, 0.5f, 0.0f, 1.0f, 1.0f); //hintergrund farbe ändern

        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        /* cube */
            for(int i=0; i<2; i++){
                boxes.get(i).rotateY(0.2f);
                graphicsDevice.setWorldMatrix(boxes.get(i));
                graphicsDevice.unbindTexture();
                graphicsDevice.bindTexture(box_texture);
                graphicsDevice.bindVertexBuffer(cube.getVertexBuffer());
                graphicsDevice.setDepthWrite(true);
                graphicsDevice.setDepthTest(CompareFunction.LESS_OR_EQUAL);
                graphicsDevice.draw(cube.getMode(), cube.getVertexBuffer().getVertex_amount());
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

    public static int getBubbleAmount(){
        return boxes.size();
    }

}
