package com.example.tictactoe;

import java.io.Serializable;

public class SaveResult implements Serializable {
    public int scorePlayerX;
    public int scorePlayerO;
    public int draws;
    public int totalGames;
    public String winner;

    public SaveResult(int scorePlayerX, int scorePlayerO, int draws, int totalGames, String winner) {
        this.scorePlayerX = scorePlayerX;
        this.scorePlayerO = scorePlayerO;
        this.draws = draws;
        this.totalGames = totalGames;
        this.winner = winner;
    }
}
