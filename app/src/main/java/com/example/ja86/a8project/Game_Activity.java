package com.example.ja86.a8project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Game_Activity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.corbynx010.myapplication.MESSAGE";

    SensorManager sensorMgr;
    Sensor accelerometer;
    float accelX;
    GestureDetectorCompat gestureDetector;
    onFlingListener gestureListener;

    PlayerBall[] balls = new PlayerBall[6];
    Jack jaky;
    int x = 0;

    float width;
    float height;

    float XSTARTPOSITION;
    float YSTARTPOSITIONBALL;
    float YSTARTPOSITIONJACK;

    ///Accelerometer
    SensorEventListener accelListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d("Game_Activity", "Accelerometer => " + event.values[0]);

            accelX = -(event.values[0]);

            if(balls[x-1].getBallState() == 1){
                balls[x-1].setXAcceleration(balls[x-1].getxAcceleration()+(accelX*7));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    class GraphicsView extends View {
        Context context;
        public GraphicsView(Context c) {
            super(c);
            context = c;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint p = new Paint();
            int i = 0;
            canvas.drawRGB(37, 163, 50);
            if(balls[0] != null) {
                boolean newBall = false;
                for (int z = 0; z < x; z++) {
                    if (i % 2 == 0) {
                        p.setColor(Color.BLUE);
                    } else {
                        p.setARGB(255, 255, 156, 28);
                    }
                    boolean collide = false;
                    if (Math.abs(balls[z].getyAcceleration()) > 50) {
                        float yACCEL = balls[z].getyAcceleration();
                        float xACCEL = balls[z].getxAcceleration();
                        balls[z].setYAcceleration((float)(yACCEL * 0.98));
                        balls[z].setXAcceleration((float)(xACCEL * 0.98));

                        balls[z].setX(balls[z].getX()+balls[z].xAcceleration/100);
                        balls[z].setY(balls[z].getY()+balls[z].yAcceleration/180);

                        if(Math.abs(balls[z].getyAcceleration()) <=50){
                            balls[z].setYAcceleration(0);
                        }

                        if((balls[z].getX()-balls[z].getRadius()) <= 0 || (balls[z].getX()+balls[z].getRadius()) >= width){
                            balls[z].setXAcceleration((float)(-1.021*balls[z].getxAcceleration()));
                        }

                        if((balls[z].getY()-balls[z].getRadius()) <= 0 || (balls[z].getY()+balls[z].getRadius()) >= height){
                            balls[z].setYAcceleration((float)(-1.021*balls[z].getyAcceleration()));
                        }

                            for (int m = 0; m <= x - 1; m++) {
                            if(m != z) {
                                if (Math.abs(balls[z].getX() - balls[m].getX()) < (balls[z].getRadius() + balls[m].getRadius())
                                        && Math.abs(balls[z].getY() - balls[m].getY()) < (balls[z].getRadius() + balls[m].getRadius())) {
                                    Log.d("CollisionTAG", "onDraw: called");
                                    balls[z].colission(balls[m]);
                                    collide = true;
                                }
                            }
                            }

                        if (Math.abs(balls[z].getX() - jaky.getX()) < (balls[z].getRadius() + jaky.getRadius())
                                && Math.abs(balls[z].getY() - jaky.getY()) < (balls[z].getRadius() + jaky.getRadius())) {
                            Log.d("CollisionTAG", "onDraw: called");
                            balls[z].colission(jaky);
                            collide = true;
                        }

                        if (balls[z].getyAcceleration() == 0 && jaky.getyAcceleration() == 0 && !collide) {
                            newBall = true;
                            balls[z].ballState = 2;
                        }
                        collide=false;
                    }



                    canvas.drawCircle(balls[z].getX(), balls[z].getY(), balls[z].getRadius(), p);
                    i++;
                }

                if(Math.abs(jaky.getyAcceleration()) > 50){
                    newBall= false;
                    float yACCEL = jaky.getyAcceleration();
                    float xACCEL = jaky.getxAcceleration();
                    jaky.setYAcceleration((float) (yACCEL * 0.98));
                    jaky.setXAcceleration((float) (xACCEL * 0.98));

                    jaky.setX(jaky.getX() + jaky.xAcceleration / 100);
                    jaky.setY(jaky.getY() + jaky.yAcceleration / 180);

                    if (Math.abs(jaky.getyAcceleration()) <= 50) {
                        jaky.setYAcceleration(0);
                        newBall = true;
                    }

                    if ((jaky.getX() - jaky.getRadius()) <= 0 || (jaky.getX() + jaky.getRadius()) >= width) {
                        jaky.setXAcceleration((float) (-1.021 * jaky.getxAcceleration()));
                    }

                    if ((jaky.getY() - jaky.getRadius()) <= 0 || (jaky.getY() + jaky.getRadius()) >= height) {
                        jaky.setYAcceleration((float) (-1.021 * jaky.getyAcceleration()));
                    }
                    for (int m = 0; m <= x - 1; m++) {
                        if (Math.abs(jaky.getX() - balls[m].getX()) < (jaky.getRadius() + balls[m].getRadius())
                                && Math.abs(jaky.getY() - balls[m].getY()) < (jaky.getRadius() + balls[m].getRadius())) {
                            Log.d("CollisionTAG", "onDraw: called");
                            jaky.colission(balls[m]);
                            newBall = false;
                        }
                    }
                }

                if(newBall){

                    if (x < 6) {
                        PlayerBall nBall = new PlayerBall(XSTARTPOSITION, YSTARTPOSITIONBALL);
                        addBall(nBall);
                    }
                    else{
                        //Finish
                        int p1Score = 0;
                        int p2Score = 0;
                        for(int n = 0; n < 6; n++){
                            if(n%2 == 0){
                                p1Score += (int)balls[n].distanceToJack(jaky);
                            }
                            else if(n%2 == 1){
                                p2Score += (int)balls[n].distanceToJack(jaky);
                            }
                        }

                        Intent intent = new Intent(context, EndScreen_Activity.class);
                        String winner = p1Score+","+p2Score;

                        intent.putExtra(EXTRA_MESSAGE,winner);
                        startActivity(intent);
                        finish();
                    }
                }

            }
            p.setColor(Color.WHITE);
            canvas.drawCircle(jaky.getX(), jaky.getY(), jaky.getRadius(),p);
            invalidate();
        }

        public void addBall(PlayerBall ball){
            balls[x] = ball;
            x++;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            Log.d("Game_Activity202", "OnTouchEvent");
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_);

        sensorMgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.d("Game_Activity", "Obtained accelerometer "+ accelerometer);

        RelativeLayout layout = new RelativeLayout(this);
        GraphicsView gv = new GraphicsView(this);
        Canvas canvas = new Canvas();
        layout.addView(gv);
        setContentView(layout);
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        XSTARTPOSITION = width/2;
        YSTARTPOSITIONBALL = (float)(height*0.9);
        YSTARTPOSITIONJACK = (float)(height*0.2);

        gestureListener = new onFlingListener();
        gestureDetector = new GestureDetectorCompat(this, gestureListener);
        jaky = new Jack(XSTARTPOSITION, YSTARTPOSITIONJACK);
        PlayerBall pb1 = new PlayerBall(XSTARTPOSITION, YSTARTPOSITIONBALL);
        gv.addBall(pb1);
    }


    public class onFlingListener implements GestureDetector.OnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("Game_Activity202", "OnDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d("Game_Activity202", "OnShowPress");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d("Game_Activity202", "OnSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("Game_Activity202", "OnScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("Game_Activity202", "OnLongPress");

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){

            if(Math.abs(velocityY) > 100) {
                if (balls[x - 1].getBallState() == 0) {
                    balls[x - 1].setBallState(1);
                    balls[x - 1].setYAcceleration(velocityY);
                    balls[x - 1].setXAcceleration(velocityX);
                    Log.d("Game_Activity202", "OnFling, balls yAccel = " + balls[x - 1].getyAcceleration() + " yAccel = " + velocityY);
                }
            }
            return true;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Game_Activity", "OnPause");
        sensorMgr.unregisterListener(accelListener, accelerometer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Game_Activity", "OnResume");
        sensorMgr.registerListener(accelListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
