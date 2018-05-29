package com.company.mick;

//1) Найти сколько раз встречается каждое слово в тексте.
//2) Определить стоимость печати текста, если известна
//стоимость печати одного слова.
//3) Из текста удалить все символы не являющиеся буквами
//(кроме пробелов).

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("1 - task1; 2 - task2; 3 - task3!!!!!!!!!!!!!!!!!!!!!!");

        while (true) {

            byte choice = scanner.nextByte();

            switch (choice) {


                case 1:

                    WordFinding.findWord("Hello, my name is Michael. I'm from Minsk. Minsk is beautiful city.");
                    break;

                case 2:

                    WordFinding.findPrice("Hello, nice to meet you, suck my kiss.", 20);
                    break;

                case 3:

                    WordFinding.deleteChar("4e kak d!la ueb@k ***** pid0r@S!!!!");
                    break;
            }
        }
    }
}