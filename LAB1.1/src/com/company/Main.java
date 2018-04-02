package com.company;

import java.lang.reflect.Array;
import java.util.*;

public class Main {


    private static void scanVector(Vector<Double> vector) {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {

                Double temp = scanner.nextDouble();
                if (temp.equals(0d)) {
                    break;
                }
                vector.add(temp);

            } catch (InputMismatchException ex) {
                System.out.println("You've entered not a number!");
                scanner.next();
            }
        }

    }

    private static void outputVector(Vector<Double> vector) {
        System.out.println();
        for (Double i : vector) {
            System.out.print(i + " ");
        }
    }

    private static void composeVectors(Vector<Double> vector1, Vector<Double> vector2) {

       // System.out.println("Size of vector1: " + vector1.size());
       // System.out.println("Size of vector2: " + vector2.size());

        Double[] array1 = vector1.toArray(new Double[vector1.size()]);
        Double[] array2 = vector2.toArray(new Double[vector2.size()]);

        // System.out.println("Size of array1: " + array1.length);
        // System.out.println("Size of array2: " + array2.length);


        int i = 0;
        int j = 0;

        while ((i < array1.length) && j < array2.length) {
            if (array1[i] > array2[j]) {
                System.out.println("You can add array2[" + j + "] = " + array2[j] + "  before array1[" + i + "] = " + array1[i]);
                j++;
            } else {
                i++;
            }
        }
        while (j < array2.length) {
            System.out.println("You can add array2[" + j + "] = " + array2[j] + "  after array1[" + (i - 1) + "] = " + array1[i-1]);
            j++;
        }

    }

    public static void main(String[] args) {

        Vector<Double> vector1 = new Vector<>();
        Vector<Double> vector2 = new Vector<>();

        System.out.println("\nEnter Vector 1, (0 to stop)");
        scanVector(vector1);
        System.out.println("\nEnter Vector 2, (0 to stop)");
        scanVector(vector2);

        Collections.sort(vector1);
        Collections.sort(vector2);

        outputVector(vector1);
        outputVector(vector2);
        System.out.println();

        composeVectors(vector1,vector2);
    }
}


