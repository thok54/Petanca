package com.example.ja86.a8project;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Highscore_Activity extends AppCompatActivity {

    ArrayList<Score> scores = new ArrayList<>();
    File testFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_);
        Context context = this;

        try {
                testFile = new File(context.getExternalFilesDir(null), "highscores.csv");
                CSVReader reader = new CSVReader(new FileReader(testFile));
                String[] nextLine;
                int x = 0;
                while ((nextLine = reader.readNext()) != null) { //"name,score"
                    String n = nextLine[0];
                    int i = Integer.parseInt(nextLine[1]);
                    Log.d("bowls", "onCreate: name " + nextLine[0] + " score " + nextLine[1]);
                    Score s = new Score(n, i);
                    scores.add(s);
                    x++;
                }
            } catch (IOException e) {
                Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
            }

        Score rank1 = new Score("r1", Integer.MAX_VALUE);
        Score rank2 = new Score("r2", Integer.MAX_VALUE);
        Score rank3 = new Score("r3", Integer.MAX_VALUE);
        Score rank4 = new Score("r4", Integer.MAX_VALUE);
        Score rank5 = new Score("r5", Integer.MAX_VALUE);
        for(int i = 0; i < scores.size(); i++){
            if(scores.get(i).getResult() <rank5.getResult()) {
                if (scores.get(i).getResult() < rank4.getResult()) {
                    if (scores.get(i).getResult() < rank3.getResult()) {
                        if (scores.get(i).getResult() < rank2.getResult()) {
                            if (scores.get(i).getResult() < rank1.getResult()) {
                                rank5 = rank4;
                                rank4 = rank3;
                                rank3 = rank2;
                                rank2 = rank1;
                                rank1 = scores.get(i);
                            } else {
                                rank5 = rank4;
                                rank4 = rank3;
                                rank3 = rank2;
                                rank2 = scores.get(i);
                            }
                        } else {
                            rank5 = rank4;
                            rank4 = rank3;
                            rank3 = scores.get(i);
                        }
                    } else {
                        rank5 = rank4;
                        rank4 = scores.get(i);
                    }
                }
                else{
                    rank5=scores.get(i);
                }
            }
        }
        testFile.delete();
        try {
            testFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true));
            if(rank1.getResult() != Integer.MAX_VALUE) {
                writer.write(rank1.getName() + "," + rank1.getResult() + "\n");
                TextView tv3 = findViewById(R.id.textView3);
                tv3.setText("1. " + rank1.getName() + ", " + rank1.getResult());
                if(rank2.getResult() != Integer.MAX_VALUE) {
                    writer.write(rank2.getName() + "," + rank2.getResult() + "\n");
                    TextView tv4 = findViewById(R.id.textView4);
                    tv4.setText("2. " + rank2.getName() + ", " + rank2.getResult());
                    if(rank3.getResult() != Integer.MAX_VALUE) {
                        writer.write(rank3.getName() + "," + rank3.getResult() + "\n");
                        TextView tv5 = findViewById(R.id.textView5);
                        tv5.setText("3. " + rank3.getName() + ", " + rank3.getResult());
                        if(rank4.getResult() != Integer.MAX_VALUE) {
                            writer.write(rank4.getName() + "," + rank4.getResult() + "\n");
                            TextView tv6 = findViewById(R.id.textView6);
                            tv6.setText("4. " + rank4.getName() + ", " + rank4.getResult());
                            if (rank5.getResult() != Integer.MAX_VALUE) {
                                writer.write(rank5.getName() + "," + rank5.getResult() + "\n");
                                TextView tv7 = findViewById(R.id.textView7);
                                tv7.setText("5. " + rank5.getName() + ", " + rank5.getResult());
                            }
                        }
                    }
                }
            }
            writer.close();
        }catch (IOException io){io.printStackTrace();}
        }

    public class Score{
        int result;
        String name;

        public Score(String n, int i){
            result = i;
            name = n;
        }

        public String getName() {
            return name;
        }

        public int getResult() { return result; }
    }
}
