package com.mygdx.game;

import java.io.Serial;
import java.io.Serializable;

public class GameData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private final int MAX_SCORES = 10;
    private int[] highScores;
    private String[] names;

    private int tempScorep1;
    private int tempScorep2;

    public GameData() {
        highScores = new int[MAX_SCORES];
        names = new String[MAX_SCORES];
    }

    // setes up an empty high scores table
    public void render() {
        for(int i = 0; i < MAX_SCORES; i++) {
            highScores[i] = 0;
            names[i] = "---";

        }
    }

    public int[] getHighScores() {return highScores;}
    public String[] getNames() { return names;}

    public int getTempScorep1() { return tempScorep1;}

    public int getTempScorep2() { return tempScorep2;}

    public void setTempScore(int p1Score, int p2Score) {
        tempScorep1 = p1Score;
        tempScorep2 = p2Score;
    }

    public boolean isHighScore(int score) {
        return score > highScores[MAX_SCORES - 1];
    }

    public void addHighScore(int newScore, String name) {
        if(isHighScore(newScore)) {
            highScores[MAX_SCORES - 1] = newScore;
            names[MAX_SCORES - 1] = name;
            sortHighScores();
        }
    }

    public void sortHighScores() {
        for (int i = 0; i < MAX_SCORES; i++) {
            int score = highScores[i];
            String name = names[i];
            int j;
            for (j = i - 1; j >= 0 && highScores[j] < score; j++) {
                highScores[j+1] = highScores[j];
                names[j+1] = names[j];
            }
            highScores[j+1] = score;
            names[j+1] = name;
        }
    }
}