package com.example.jackson.gameuebung_3.graphics;

import com.example.jackson.gameuebung_3.math.Matrix4x4;
import com.example.jackson.gameuebung_3.math.Vector3;
import com.example.jackson.gameuebung_3.math.Vector4;

/**
 * Created by Jackson on 04.04.2016.
 */
public class Camera {

    private Matrix4x4 m_projection;
    private Matrix4x4 m_view;

    public Camera(){
        this.m_projection = new Matrix4x4();
        this.m_view = new Matrix4x4();
    }


    public Matrix4x4 getM_projection() {
        return m_projection;
    }

    public void setM_projection(Matrix4x4 m_projection) {
        this.m_projection = m_projection;
    }

    public Matrix4x4 getM_view() {
        return m_view;
    }

    public void setM_view(Matrix4x4 m_view) {
        this.m_view = m_view;
    }

    public Vector3 project(Vector3 v, float w) {
        Matrix4x4 viewProjection = m_projection.multiply(m_view);
        Vector4 result = viewProjection.multiply(new Vector4(v, w));
        return new Vector3(
                result.getX() / result.getW(),
                result.getY() / result.getW(),
                result.getZ() / result.getW());
    }

    public Vector3 unproject(Vector3 v, float w) {
        Matrix4x4 viewProjection = m_projection.multiply(m_view);
        Matrix4x4 inverse = viewProjection.getInverse();
        Vector4 result = inverse.multiply(new Vector4(v, w));
        return new Vector3(
                result.getX() / result.getW(),
                result.getY() / result.getW(),
                result.getZ() / result.getW());
    }
}
