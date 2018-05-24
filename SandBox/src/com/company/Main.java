package com.company;

public class Main {

    /* Создать абстрактный класс Фигура с абстрактным
       методом вычислеия периметра. Описать 3-4 класса наследника.
       Продемонстрировать в методе main работу классов. Для задания исп. классы-обертки, в качестве
       полей наследников класса Фигура использовать доп. класс.*/

    public static void main(String[] args) {

        Point pointTriangleA = new Point(10,20);
        Point pointTriangleB = new Point(13,25);
        Point pointTriangleC = new Point(12,23);
        Triangle triangle = new Triangle(pointTriangleA, pointTriangleB, pointTriangleC);

        Point pointQuadrangleA = new Point(10,20);
        Point pointQuadrangleB = new Point(15,22);
        Point pointQuadrangleC = new Point(16,25);
        Point pointQuadrangleD = new Point(1,24);
        Quadrangle quadrangle = new Quadrangle(pointQuadrangleA, pointQuadrangleB, pointQuadrangleC, pointQuadrangleD);

        Square square = new Square(new Point(1,1), 5);

        Figure[] figures = new Figure[3];
        figures[0] = triangle;
        figures[1] = quadrangle;
        figures[2] = square;

        System.out.println("Triangle: ");
        figures[0].show();

        System.out.println("\nQuadrangle: ");
        figures[1].show();

        System.out.println("\nSquare: ");
        figures[2].show();

        System.out.println("\nPerimeter of triangle: " + String.format("%.2f", figures[0].getPerimeter()));
        System.out.println("Perimeter of quadrangle: " + String.format("%.2f", figures[1].getPerimeter()));
        System.out.println("Perimeter of square: " + String.format("%.2f", figures[2].getPerimeter()));

    }
}
