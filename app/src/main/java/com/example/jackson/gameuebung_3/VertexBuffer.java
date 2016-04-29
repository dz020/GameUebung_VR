package com.example.jackson.gameuebung_3;

import java.nio.ByteBuffer;

/**
 * Created by Jackson on 05.04.2016.
 */
public class VertexBuffer {

    private VertexElement[] vertexElements;
    private ByteBuffer byteBuffer;
    private int vertex_amount;


    /* getter und setter*/
    public VertexElement[] getVertexElements() {
        return vertexElements;
    }

    public void setVertexElements(VertexElement[] vertexElements) {
        this.vertexElements = vertexElements;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public void setByteBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public int getVertex_amount() {
        return vertex_amount;
    }

    public void setVertex_amount(int vertex_amount) {
        this.vertex_amount = vertex_amount;
    }
}
