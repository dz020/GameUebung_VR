package com.example.jackson.gameuebung_3.graphics;

/**
 * Created by Jackson on 12.04.2016.
 */
public class Texture {

    private int handle;
    private int width, height;

    Texture(int handle, int width, int height) {
        this.handle = handle;
        this.width = width;
        this.height = height;
    }

    int getHandle() {
        return handle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
