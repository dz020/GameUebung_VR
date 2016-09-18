package com.example.jackson.gameuebung_3;

import java.nio.ByteBuffer;

/**
 * Created by Jackson on 05.04.2016.
 */
public class VertexBuffer {

    private int numVertices;
    private VertexElement[] elements;
    private ByteBuffer buffer;



    public int getNumVertices() {
        return numVertices;
    }
    public VertexElement[] getElements() {
        return elements;
    }
    public ByteBuffer getBuffer() {
        return buffer;
    }
    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }
    public void setElements(VertexElement[] elements) {
        this.elements = elements;
    }
    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

}