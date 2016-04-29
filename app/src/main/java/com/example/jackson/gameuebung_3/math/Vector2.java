package com.example.jackson.gameuebung_3.math;

/**
 * Created by Jackson on 04.04.2016.
 */
public class Vector2 {

    public float[] v = new float[2];

    /* Konstruktoren */
        public Vector2() {
            v[0] = 0.0f;
            v[1] = 0.0f;
        }

        public Vector2(float x, float y) {
            v[0] = x;
            v[1] = y;
        }



                /* Klassen Methoden */

                            /* getter und setter */
                            public float get(int index) {
                                return v[index];
                            }

                            public float getX() {
                                return v[0];
                            }

                            public float getY() {
                                return v[1];
                            }

                            public void set(int index, float value) {
                                v[index] = value;
                            }

                            public void setX(float x) {
                                v[0] = x;
                            }

                            public void setY(float y) {
                                v[1] = y;
                            }

                    public float getLength() {
                        return (float) Math.sqrt(
                                v[0] * v[0] +
                                        v[1] * v[1]);
                    }

                    public float getLengthSqr() {
                        return (v[0] * v[0] +
                                v[1] * v[1]);
                    }

                    public Vector2 normalize() {
                        float l = getLength();
                        for (int i = 0; i < 2; ++i)
                            v[i] /= l;
                        return this;
                    }





    /* statische Methoden */
        public static Vector2 add(Vector2 v1, Vector2 v2) {
            return new Vector2(
                    v1.v[0] + v2.v[0],
                    v1.v[1] + v2.v[1]);
        }

        public static Vector2 subtract(Vector2 v1, Vector2 v2) {
            return new Vector2(
                    v1.v[0] - v2.v[0],
                    v1.v[1] - v2.v[1]);
        }

        public static Vector2 multiply(float s, Vector2 v) { //für was steht s ???
            return new Vector2(
                    s * v.v[0],
                    s * v.v[1]);
        }

        public static Vector2 divide(Vector2 v, float s) { //für was steht s ???
            return new Vector2(
                    v.v[0] / s,
                    v.v[1] / s);
        }


        /**
         * Vektor auf Länge 1 bringen, dann ist er ein Einheitsvektor
         * @return
         */
        public static Vector2 normalize(Vector2 v) { //was nacht die methode ??
            Vector2 result = new Vector2();
            float length = v.getLength();
            for (int i = 0; i < 2; ++i)
                result.v[i] /= length;
            return result;
        }

        /**
         * Skalarprodukt, wenn return value = 0, dann sind Vektoren orthogonal zueinander, also im rechten Winkel
         * @param v1
         * @param v2
         * @return
         */
        public static float scalar(Vector2 v1, Vector2 v2) { //was soll die methode bringen ??
            return v1.v[0] * v2.v[0] + v1.v[1] * v2.v[1];
        }
}
