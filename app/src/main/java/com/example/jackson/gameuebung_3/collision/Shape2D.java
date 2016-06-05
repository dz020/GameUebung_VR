package com.example.jackson.gameuebung_3.collision;


import com.example.jackson.gameuebung_3.math.Vector2;

/**
 * Created by Jackson on 04.06.2016.
 */
public interface Shape2D {

    boolean intersects(Shape2D shape);
    boolean intersects(Point point);
    boolean intersects(Circle circle);
    boolean intersects(AABB box);

    Vector2 getPosition();
    void setPosition(Vector2 position);

}
