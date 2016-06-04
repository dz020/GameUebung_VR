package com.example.jackson.gameuebung_3.collision;


import com.example.jackson.gameuebung_3.math.Vector2;

/**
 * Created by Jackson on 04.06.2016.
 */
public interface Shape2D {

    public boolean intersects(Shape2D shape);
    public boolean intersects(Point point);
    public boolean intersects(Circle circle);
    public boolean intersects(AABB box);

    public Vector2 getPosition();
    public void setPosition(Vector2 position);

}
