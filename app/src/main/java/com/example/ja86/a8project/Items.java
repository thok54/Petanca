package com.example.ja86.a8project;

public class Items {
    float x;
    float y;
    float radius;
    float xAcceleration = 0;
    float yAcceleration = 0;

    public float getX(){return x;}
    public float getY(){return y;}
    public float getRadius(){return radius;}
    public void setXAcceleration(float xAcceleration){this.xAcceleration = xAcceleration;}
    public void setYAcceleration(float yAcceleration){this.yAcceleration = yAcceleration;}
    public void setX(float x){this.x = x;}
    public void setY(float y){this.y = y;}
    public float getyAcceleration(){return yAcceleration;}
    public float getxAcceleration(){return xAcceleration;}

    public void colission(Items items){
        float xa = this.xAcceleration;
        float ya = this.yAcceleration;
        this.xAcceleration = 0;
        this.yAcceleration = 0;
        items.setXAcceleration((float) 1.021 * xa);
        items.setYAcceleration((float) 1.021 * ya);
    }
}