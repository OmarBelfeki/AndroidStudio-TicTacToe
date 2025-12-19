package com.example.tictactoe;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class EcranJeu extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private int roundCount = 0;
    private boolean xTurn;
    private int xScore = 0;
    private int oScore = 0;
    private int nullScore = 0;
    private int gameNumber = 1;
    private int totalGames;

    private TextView textX, textO, textNulls, textPartie;

    private MediaPlayer eg, vx, vo;
    private MediaPlayer clickX, clickO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecran_jeu);

        eg = MediaPlayer.create(this, R.raw.eg);
        vx = MediaPlayer.create(this, R.raw.vx);
        vo = MediaPlayer.create(this, R.raw.vo);
        clickX = MediaPlayer.create(this, R.raw.click_x);
        clickO = MediaPlayer.create(this, R.raw.click_o);

        totalGames = getIntent().getIntExtra("nbr_parties", 5);
        xTurn = getIntent().getBooleanExtra("x", true);

        textPartie = findViewById(R.id.partie);
        textX = findViewById(R.id.x);
        textO = findViewById(R.id.o);
        textNulls = findViewById(R.id.nulls);

        buttons[0][0] = findViewById(R.id.row1_1);
        buttons[0][1] = findViewById(R.id.row1_2);
        buttons[0][2] = findViewById(R.id.row1_3);
        buttons[1][0] = findViewById(R.id.row2_1);
        buttons[1][1] = findViewById(R.id.row2_2);
        buttons[1][2] = findViewById(R.id.row2_3);
        buttons[2][0] = findViewById(R.id.row3_1);
        buttons[2][1] = findViewById(R.id.row3_2);
        buttons[2][2] = findViewById(R.id.row3_3);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j].setOnClickListener(v -> makeMove(row, col));
            }
        }

        updateUI();
    }

    private void makeMove(int row, int col) {
        if (!buttons[row][col].getText().toString().equals("")) return;
        buttons[row][col].setText(xTurn ? "X" : "O");
        roundCount++;
        if (xTurn) clickX.start(); else clickO.start();
        if (checkWin()) {
            if (xTurn) xWins(); else oWins();
        } else if (roundCount == 9) {
            draw();
        } else {
            xTurn = !xTurn;
        }
    }

    private boolean checkWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                field[i][j] = buttons[i][j].getText().toString();
        for (int i = 0; i < 3; i++) {
            if (!field[i][0].equals("") && field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2])) return true;
            if (!field[0][i].equals("") && field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i])) return true;
        }
        if (!field[0][0].equals("") && field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2])) return true;
        if (!field[0][2].equals("") && field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0])) return true;
        return false;
    }

    private void xWins() {
        xScore++;
        nextGame("X gagne !");
    }

    private void oWins() {
        oScore++;
        nextGame("O gagne !");
    }

    private void draw() {
        nullScore++;
        nextGame("Match nul !");
    }

    private void nextGame(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        updateUI();
        if (gameNumber < totalGames) {
            gameNumber++;
            resetBoard();
            updateUI();
        } else {
            updateUI();
            showFinalResult();
        }
    }

    private void updateUI() {
        textX.setText("X: " + xScore);
        textO.setText("O: " + oScore);
        textNulls.setText("Nuls: " + nullScore);
        textPartie.setText("Partie: " + gameNumber + " / " + totalGames);
    }

    private void resetBoard() {
        roundCount = 0;
        xTurn = true;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                buttons[i][j].setText("");
    }

    private void showFinalResult() {
        String winner;
        MediaPlayer resultSound;

        if (xScore > oScore) {
            winner = "Victoire du joueur X";
            resultSound = vx;
        } else if (oScore > xScore) {
            winner = "Victoire du joueur O";
            resultSound = vo;
        } else {
            winner = "Egalité";
            resultSound = eg;
        }

        if (resultSound.isPlaying()) {
            resultSound.pause();
        }
        resultSound.seekTo(0);
        resultSound.start();

        SaveResult result = new SaveResult(xScore, oScore, nullScore, totalGames, winner);

        new AlertDialog.Builder(this)
                .setTitle("Résultat final")
                .setMessage(winner)
                .setPositiveButton("Sauvegarder le tournoi", (dialog, which) -> {
                    saveTournamentResult(result);
                    finish();
                })
                .setNegativeButton("Revenir à l’accueil", (dialog, which) -> finish())
                .show();
    }

    private void saveTournamentResult(SaveResult result) {
        try {
            FileOutputStream fos = openFileOutput("tournament_result.ser", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(result);
            oos.close();
            fos.close();
            Toast.makeText(this, "Tournoi sauvegardé avec succès!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la sauvegarde", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer(eg);
        releaseMediaPlayer(vx);
        releaseMediaPlayer(vo);
        releaseMediaPlayer(clickX);
        releaseMediaPlayer(clickO);
    }

    private void releaseMediaPlayer(MediaPlayer mp) {
        if (mp != null) {
            if (mp.isPlaying()) mp.stop();
            mp.release();
        }
    }
}
