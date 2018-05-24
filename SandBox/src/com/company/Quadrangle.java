package com.company;

public class Quadrangle extends Figure {

    private Point a;
    private Point b;
    private Point c;
    private Point d;

    Quadrangle(Point a, Point b, Point c, Point d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public Double getPerimeter() {
        double d1 = Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
        double d2 = Math.sqrt(Math.pow((b.getX() - c.getX()), 2) + Math.pow((b.getY() - c.getY()), 2));
        double d3 = Math.sqrt(Math.pow((c.getX() - d.getX()), 2) + Math.pow((c.getY() - d.getY()), 2));
        double d4 = Math.sqrt(Math.pow((d.getX() - a.getX()), 2) + Math.pow((d.getY() - a.getY()), 2));
        return (d1 + d2 + d3 + d4);
    }

    @Override
    public void show() {
        System.out.print("A: ");
        a.show();
        System.out.print("B: ");
        b.show();
        System.out.print("C: ");
        c.show();
        System.out.print("D: ");
        d.show();
    }
}
