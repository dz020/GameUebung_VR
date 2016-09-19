package com.example.jackson.gameuebung_3.game;

/**
 * Created by Jackson on 12.06.2016.
 */
public class GameState {

    public int gameObject_amount;
    public int gameObject_destroyed_counter;
    public int level;
    public float current_score;
    public boolean game_over = false;
    public int current_ammo = 8;
    public int max_ammo = 8;
    public int increase_ammo = 3;
    public int max_game_object_amount = 10;
    public static boolean empty_ammo = false;

    public GameState() {
    }

    public int getIncrease_ammo() {
        return increase_ammo;
    }

    public void setIncrease_ammo(int increase_ammo) {
        this.increase_ammo = increase_ammo;
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

    public float getCurrent_score() {
        return current_score;
    }

    public int getCurrent_ammo() {
        return current_ammo;
    }

    public void setCurrent_ammo(int current_ammo) {
        this.current_ammo = current_ammo;
    }

    public int getMax_ammo() {
        return max_ammo;
    }

    public void setMax_ammo(int max_ammo) {
        this.max_ammo = max_ammo;
    }

    public void setCurrent_score(float current_score) {
        this.current_score = current_score;
    }

    public boolean isGame_over() {
        return game_over;
    }

    public void setGame_over(boolean game_over) {
        this.game_over = game_over;
    }
}
