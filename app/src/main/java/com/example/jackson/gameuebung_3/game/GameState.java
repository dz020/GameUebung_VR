package com.example.jackson.gameuebung_3.game;

/**
 * Created by Jackson on 12.06.2016.
 */
public class GameState {

    public int gameObject_amount;
    public int gameObject_destroyed_counter;
    public int level;

    public GameState() {
    }

    public int getGameObject_amount() {
        return gameObject_amount;
    }

    public void setGameObject_amount(int gameObject_amount) {
        this.gameObject_amount = gameObject_amount;
    }

    public int getGameObject_destroyed_counter() {
        return gameObject_destroyed_counter;
    }

    public void setGameObject_destroyed_counter(int gameObject_destroyed_counter) {
        this.gameObject_destroyed_counter = gameObject_destroyed_counter;
    }
}
