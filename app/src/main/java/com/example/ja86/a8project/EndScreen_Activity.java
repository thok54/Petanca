package com.example.ja86.a8project;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EndScreen_Activity extends AppCompatActivity {

    Context context;
    int winnerScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen_);

        context = this;
        Intent intent = getIntent();
        String winner = intent.getStringExtra(Game_Activity.EXTRA_MESSAGE);

        String[] results = winner.split(",");
        String winnerNumber;
        if(Integer.parseInt(results[0]) > Integer.parseInt(results[1])) {
            winnerNumber = "Player 2";
            winnerScore = Integer.parseInt(results[1]);
        }
        else{
            winnerNumber = "Player 1";
            winnerScore = Integer.parseInt(results[0]);
        }

        TextView textView = findViewById(R.id.tv_Winner);
        textView.setText(winnerNumber+" Wins\n"+"Player 1 Score: "+results[0] + "\nPlayer 2 Score: "+results[1]);
    }

    public void onClick(View view){
        EditText et = findViewById(R.id.editText);
        et.setText("");
    }

    public void onSave(View view){
        try {
            File testFile = new File(context.getExternalFilesDir(null), "highscores.csv");
            if(!testFile.exists()){
                testFile.createNewFile();
            }

            EditText text = findViewById(R.id.editText);
            String winnerName = text.getText().toString();
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true));
            writer.write(winnerName + "," + winnerScore + "\n");
            writer.close();
            MediaScannerConnection.scanFile(context,
                    new String[]{testFile.toString()},
                    null,
                    null);
        } catch (IOException io) {
            io.printStackTrace();
        }
        Intent main = new Intent(context, MainActivity.class);
        startActivity(main);
        finish();
    }
}
