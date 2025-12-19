package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class PrincipeJeu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle four){
        super.onCreate(four);
        setContentView(R.layout.principe_jeu);

        RadioButton part5 = findViewById(R.id.part5);
        RadioButton part10 = findViewById(R.id.part10);
        RadioButton part15 = findViewById(R.id.part15);

        Button dem = findViewById(R.id.btn_start_tournament);
        Button score = findViewById(R.id.btn_retrieve_scores);

        if (!part5.isChecked() && !part10.isChecked() && !part15.isChecked()) part5.setChecked(true);


        dem.setOnClickListener(v -> {
            Intent intent = new Intent(PrincipeJeu.this, EcranJeu.class);

            if(part5.isChecked()) intent.putExtra("nbr_parties", 5);
            else if (part10.isChecked()) intent.putExtra("nbr_parties", 10);
            else intent.putExtra("nbr_parties", 15);

            intent.putExtra("x", true);

            startActivity(intent);
        });

        score.setOnClickListener(v -> {
            SaveResult savedResult = loadTournamentResult();
            if (savedResult != null) {
                String message = "Score X: " + savedResult.scorePlayerX +
                        "\nScore O: " + savedResult.scorePlayerO +
                        "\nNuls: " + savedResult.draws +
                        "\nTotal parties: " + savedResult.totalGames +
                        "\nVainqueur: " + savedResult.winner;

                new AlertDialog.Builder(this)
                        .setTitle("Dernier tournoi sauvegardé")
                        .setMessage(message)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });



    }

    private SaveResult loadTournamentResult() {
        SaveResult result = null;
        try {
            FileInputStream fis = openFileInput("tournament_result.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            result = (SaveResult) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Aucun résultat sauvegardé trouvé", Toast.LENGTH_SHORT).show();
        }
        return result;
    }



}
