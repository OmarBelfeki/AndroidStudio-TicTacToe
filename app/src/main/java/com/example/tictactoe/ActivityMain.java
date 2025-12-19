package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle first){
        super.onCreate(first);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActivityMain.this, EcranDaccueil.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }



}
