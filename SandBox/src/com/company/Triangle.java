package com.company;

public class Triangle extends Figure {

    private Point a;
    private Point b;
    private Point c;

    Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public Double getPerimeter() {
        double d1 = Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
        double d2 = Math.sqrt(Math.pow((b.getX() - c.getX()), 2) + Math.pow((b.getY() - c.getY()), 2));
        double d3 = Math.sqrt(Math.pow((c.getX() - a.getX()), 2) + Math.pow((c.getY() - a.getY()), 2));
        return (d1 + d2 + d3);
    }

    @Override
    public void show() {
        System.out.print("A: ");
        a.show();
        System.out.print("B: ");
        b.show();
        System.out.print("C: ");
        c.show();
    }
}
