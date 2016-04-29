package com.example.jackson.gameuebung_3.graphics;

import com.example.jackson.gameuebung_3.math.Matrix4x4;

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
}
