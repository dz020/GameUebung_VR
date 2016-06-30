package com.example.jackson.gameuebung_3.collision;


import com.example.jackson.gameuebung_3.math.Vector3;

/**
 * Created by Jackson on 04.06.2016.
 */
public interface Shape3D {

    boolean intersects(Shape3D shape);
    boolean intersects(Sphere sphere);

    Vector3 getPosition();
    void setPosition(Vector3 position);

}
