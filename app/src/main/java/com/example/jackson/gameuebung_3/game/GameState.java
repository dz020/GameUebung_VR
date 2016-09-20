package com.example.jackson.gameuebung_3.game;

/**
 * Created by Jackson on 12.06.2016.
 */
public class GameState {

    public int gameObject_amount;
    public int gameObject_destroyed_counter;
    public int level;
    public float current_score;
    public String status = "in game";
    public int current_ammo = 8;
    public int max_ammo = 8;
    public int increase_ammo = 3;
    public int max_game_object_amount = 7;
    public static boolean empty_ammo = false;
    public int currentHighScorePosition;
    public int[] highScore;

    public void resetAmmo(){
        this.current_ammo = max_ammo;
        this.empty_ammo = false;
        String amor = "";
        for (int ii = 0; ii < current_ammo; ii++) {
            amor += " I";
        }
        MGDExerciseGame.amorText.setText(amor);
    }

    public int getCurrentHighScorePosition() {
        return currentHighScorePosition;
    }

    public void setCurrentHighScorePosition(int currentHighScorePosition) {
        this.currentHighScorePosition = currentHighScorePosition;
    }

    public int[] getHighScore() {
        return highScore;
    }

    public void setHighScore(int[] highScore) {
        this.highScore = highScore;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void resetHighscore(){
        this.current_score = 0f;
        MGDExerciseGame.scoreText.setText("0");
    }
}
