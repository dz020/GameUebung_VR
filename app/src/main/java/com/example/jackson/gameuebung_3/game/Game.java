package com.example.jackson.gameuebung_3.game;

import android.content.Context;
import android.util.Log;

import com.example.jackson.gameuebung_3.graphics.GraphicsDevice;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jackson on 29.03.2016.
 */
public abstract class Game implements android.opengl.GLSurfaceView.Renderer {

    protected GraphicsDevice graphicsDevice;
    private  long lastTime = 0;
    protected Context context;

    /* Konstruktor */
    public Game(Context context){
        this.context = context;
        this.graphicsDevice = new GraphicsDevice();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        graphicsDevice.onSurfaceCreated(gl);
        this.initialize();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        graphicsDevice.resize(width, height);
        this.resize(width, height);
        loadContent();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Long currentTime = System.currentTimeMillis();
        Long deltaTime = currentTime - lastTime;
        Log.i("deltaTime", deltaTime.toString()); //zeit die nötig war um einen frame zu erzeugen
        update(deltaTime / 1000.0f);
        draw(deltaTime / 1000.0f);
        lastTime = currentTime;
    }

    public abstract void initialize();
    public abstract void update(float deltaSeconds);
    public abstract void draw(float deltaSeconds);
    public abstract void resize(int width, int height);

    //damit falls app in hintergrund gerät und ram der grafikkarte durch andere app ausgetauscht wird, die textur neu geladen wird
    public abstract void loadContent();
}
