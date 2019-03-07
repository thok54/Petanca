package com.example.ja86.a8project;

public class PlayerBall extends Items{
    int ballState = 0;


    public PlayerBall(float x, float y){
        radius = 45;
        this.x = x;
        this.y = y;
    }

    public void setBallState(int ballState) {
        this.ballState = ballState;
    }

    public int getBallState(){return ballState;}

    public double distanceToJack(Jack jack){
        return Math.sqrt((Math.abs(this.x - jack.getX())*Math.abs(this.x - jack.getX())) + (Math.abs(this.y - jack.getY())*Math.abs(this.y - jack.getY())));
    }
}