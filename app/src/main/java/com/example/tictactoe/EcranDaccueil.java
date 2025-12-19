package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class EcranDaccueil extends AppCompatActivity {

    public String selectedItem;

    @Override
    protected void onCreate(Bundle acc){

        super.onCreate(acc);
        setContentView(R.layout.ecran_daccueil);


        RadioButton x = findViewById(R.id.radioX);
        RadioButton o = findViewById(R.id.radioO);

        TextView win = findViewById(R.id.win);


        Button jouer = findViewById(R.id.playButton);
        Button principe = findViewById(R.id.howButton);
        Button scoreButton = findViewById(R.id.scoreButton);


        Spinner spinner = findViewById(R.id.mySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nomber_parties, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                //Toast.makeText(EcranDaccueil.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (!x.isChecked() && !o.isChecked()) x.setChecked(true);

        jouer.setOnClickListener(v -> {
            Intent intent = new Intent(EcranDaccueil.this, EcranJeu.class);
            if(selectedItem.equals("5 parties")) intent.putExtra("nbr_parties", 5);
            else if (selectedItem.equals("10 parties")) intent.putExtra("nbr_parties", 10);
            else intent.putExtra("nbr_parties", 15);

            if(x.isChecked()) intent.putExtra("x", true);
            else intent.putExtra("x", false);

            startActivity(intent);
        });

        principe.setOnClickListener(v -> {
            Intent intent = new Intent(EcranDaccueil.this, PrincipeJeu.class);
            startActivity(intent);
        });

        scoreButton.setOnClickListener(v -> {

            SaveResult savedResult = loadTournamentResult();
            if (savedResult != null) {
                String message = "Score X: " + savedResult.scorePlayerX +
                        "\nScore O: " + savedResult.scorePlayerO +
                        "\nNuls: " + savedResult.draws +
                        "\nTotal parties: " + savedResult.totalGames +
                        "\nVainqueur: " + savedResult.winner;

                win.setText(message);
            }else{
                win.setText("Aucun tournoi sauvegardé");
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
