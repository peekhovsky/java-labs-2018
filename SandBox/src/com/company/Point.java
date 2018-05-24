package com.company;

public class Point {
    private Double x;
    private Double y;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }
    public Double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

    public void show(){
        System.out.println("x: " + x + ", y: " + y + ".");
    }
}
