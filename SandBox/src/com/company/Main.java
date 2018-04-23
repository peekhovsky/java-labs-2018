package com.company;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
	// write your code here

        Quadrilateral quadrilaterals[] = new Quadrilateral[4];

        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < quadrilaterals.length; i++) {

            Node nodes[] = new Node[4];

            System.out.println("Enter coordinates (figure " + i  + "): ");
            for (int j = 0; j < nodes.length; j++) {
                System.out.println("Enter node " + j + " (x and y): ");
                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                nodes[j] = new Node(x, y);
            }
            quadrilaterals[i] = new Quadrilateral(nodes[0], nodes[1], nodes[2], nodes[3]);
        }
        scanner.close();

        double maxPerimeter = Quadrilateral.getPerimetr(quadrilaterals);

        System.out.println("Max perimeter: " + maxPerimeter);

    }
}
