package com.company.old;

public class Square extends Figure {

    private Point a;
    private Point b;
    private Point c;
    private Point d;
    private Double size;

    Square(Point a, double size) {
        this.a = a;
        this.b = new Point(a.getX(), a.getY() + size);
        this.c = new Point(a.getX() + size, a.getY() + size);
        this.d = new Point(a.getX() + size, a.getY());
        this.size = size;
    }

    @Override
    public Double getPerimeter() {
        return size*4;
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
