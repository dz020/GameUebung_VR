package com.example.jackson.gameuebung_3;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;

import com.example.jackson.gameuebung_3.game.MGDExerciseGame;
import com.example.jackson.gameuebung_3.math.Matrix4x4;
import com.example.jackson.gameuebung_3.math.Vector3;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.EyeTransform;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jackson on 29.03.2016.
 */
public class MGDExerciseView extends CardboardView implements CardboardView.StereoRenderer, SurfaceTexture.OnFrameAvailableListener {

    private MGDExerciseGame mgdExerciseGame;

    private static final String TAG = "MGDExerciseView";
    private static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    private int mProgram;
    private int mPositionHandle2;


    private FloatBuffer vertexBuffer, textureVerticesBuffer, vertexBuffer2;
    private ShortBuffer drawListBuffer, buf2;


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 2;
    static float squareVertices[] = { // in counterclockwise order:
            -1.0f, -1.0f,   // 0.left - mid
            1.0f, -1.0f,   // 1. right - mid
            -1.0f, 1.0f,   // 2. left - top
            1.0f, 1.0f,   // 3. right - top
    };


    private short drawOrder[] =  {0, 2, 1, 1, 2, 3 }; // order to draw vertices
    private short drawOrder2[] = {2, 0, 3, 3, 0, 1}; // order to draw vertices

    static float textureVertices[] = {
            0.0f, 1.0f,  // A. left-bottom
            1.0f, 1.0f,  // B. right-bottom
            0.0f, 0.0f,  // C. left-top
            1.0f, 0.0f   // D. right-top
    };

    private ByteBuffer indexBuffer;    // Buffer for index-array
    private int texture;

    private SurfaceTexture surface;
    private float[] mView;
    private float[] mCamera;
    public static Camera camera;

    public void startCamera(int texture) {
        surface = new SurfaceTexture(texture);
        surface.setOnFrameAvailableListener(this);

        camera = Camera.open();

        try {
            camera.setPreviewTexture(surface);
            camera.startPreview();
            Log.e(TAG, "start camera");
        }
        catch (Exception e) {
            Log.w("MainActivity","CAM LAUNCH FAILED + e: "+e);
        }
    }

    static private int createTexture() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);

        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture[0]);

        Log.e(TAG, "create texture aufgerufen");

        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);

        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);

        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public MGDExerciseView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        mgdExerciseGame = new MGDExerciseGame(context);
        float[] mModelCube = new float[16];
        mCamera = new float[16];
        mView = new float[16];
        this.setZPlanes(1.0f, 1000.0f);
    }

    int i = 0;
    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.requestRender();
        //Log.e(TAG, "on frame availabe: "+i);
        i++;
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        float[] mtx = new float[16];
        //Log.e(TAG, "on new frame: "+i);
        //i++;
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        surface.updateTexImage();
        surface.getTransformMatrix(mtx);
        headTransform.getHeadView(headView.m, 0);
        mgdExerciseGame.setHeadView(headView);
        mgdExerciseGame.update(0);
        //System.gc();
    }

    private Matrix4x4 headView = new Matrix4x4();
    private Vector3 forwardVector = new Vector3();

    @Override
    public void onDrawEye(EyeTransform eyeTransform) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);

        //Log.e(TAG, "on draw eye");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture);

        //GLES20.glActiveTexture(GL_TEXTURE_EXTERNAL_OES); hier könnte eventuell GL_TEXTURE0 stehen
        //GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture);
        //GLES20.glActiveTexture(GLES20.GL_ACTIVE_TEXTURE);
        //GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture);

        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        int vertexStride = COORDS_PER_VERTEX * 4;
        GLES20.glVertexAttribPointer(
                                        mPositionHandle,
                                        COORDS_PER_VERTEX,
                                        GLES20.GL_FLOAT,
                                        false,
                                        vertexStride,
                                        vertexBuffer);

        int mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(
                                        mTextureCoordHandle,
                                        COORDS_PER_VERTEX,
                                        GLES20.GL_FLOAT,
                                        false,
                                        vertexStride,
                                        textureVerticesBuffer);

        int mColorHandle = GLES20.glGetAttribLocation(mProgram, "s_texture");

        /* zeichnet die kamera in den hintergrund */
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);

        Matrix.multiplyMM(mView, 0, eyeTransform.getEyeView(), 0, mCamera, 0);

        mgdExerciseGame.setCameraParameters(eyeTransform.getPerspective(), eyeTransform.getEyeView());
        mgdExerciseGame.draw(0);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
        mgdExerciseGame.onSurfaceChanged(null, i, i1);
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        Log.e(TAG, "onSurfaceCreated");
        //GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well

        ByteBuffer bb = ByteBuffer.allocateDirect(squareVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareVertices);
        vertexBuffer.position(0);


        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


        ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureVerticesBuffer = bb2.asFloatBuffer();
        textureVerticesBuffer.put(textureVertices);
        textureVerticesBuffer.position(0);

        String vertexShaderCode = "attribute vec4 position;" +
                "attribute vec2 inputTextureCoordinate;" +
                "varying vec2 textureCoordinate;" +
                "void main()" +
                "{" +
                "gl_Position = position;" +
                "textureCoordinate = inputTextureCoordinate;" +
                "}";

        int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);

        //macht das rendering von den kamera bildern in den hintergrund
        String fragmentShaderCode =
                "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;" +
                "uniform vec4 vColor; \n" +
                "varying vec2 textureCoordinate;                            \n" +
                "uniform samplerExternalOES s_texture;               \n" +
                "void main(void) {" +
                "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n" +
                //"  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                "}";

        int fragmentShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);

        texture = createTexture();
        startCamera(texture);

        mgdExerciseGame.onSurfaceCreated(null, eglConfig);
    }

    @Override
    public void onRendererShutdown() {

    }


    /**
     * Converts a raw text file, saved as a resource, into an OpenGL ES shader
     * @param type The type of shader we will be creating.
     * @return
     */
    private int loadGLShader(int type, String code) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    /**
     * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
     * @param func
     */
    private static void checkGLError(String func) {
        int error;
        while ( ( error = GLES20.glGetError() ) != GLES20.GL_NO_ERROR ) {
            Log.e(TAG, func + ": glError " + error);
            throw new RuntimeException(func + ": glError " + error);
        }
    }

}
