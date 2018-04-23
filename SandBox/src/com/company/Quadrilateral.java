package com.company;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Quadrilateral {
    Node a;
    Node b;
    Node c;
    Node d;

    Quadrilateral(Node a, Node b, Node c, Node d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    static double getPerimetr(Quadrilateral[] quadrilaterals) {
        double perimeter = 0.0;
        for (int i = 0; i < quadrilaterals.length; i++) {

            double ab = sqrt(pow((quadrilaterals[i].b.x - quadrilaterals[i].a.x), 2) + pow((quadrilaterals[i].b.y - quadrilaterals[i].a.y), 2));
            double bc = sqrt(pow((quadrilaterals[i].c.x - quadrilaterals[i].b.x), 2) + pow((quadrilaterals[i].c.y - quadrilaterals[i].b.y), 2));
            double cd = sqrt(pow((quadrilaterals[i].d.x - quadrilaterals[i].c.x), 2) + pow((quadrilaterals[i].d.y - quadrilaterals[i].c.y), 2));
            double da = sqrt(pow((quadrilaterals[i].a.x - quadrilaterals[i].d.x), 2) + pow((quadrilaterals[i].a.y - quadrilaterals[i].d.y), 2));


            if (perimeter < (ab + bc + cd + da)) {
                perimeter = ab + bc + cd + da;
            }
            //perimeter = (perimeter < (ab + bc + cd + da))? perimeter = (ab + bc + cd + da) : perimeter;
            // System.out.println("Perimeter " + i + ": " + (ab + bc + cd + da));
        }
        return perimeter;
    }

}
