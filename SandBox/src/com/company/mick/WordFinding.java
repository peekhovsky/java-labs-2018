package com.company.mick;
import java.util.ArrayList;
import java.util.Arrays;


    public class WordFinding {

        public static void findWord(String text) {
            ArrayList<String> words = new ArrayList<>(Arrays.asList(text.split(" ")));

            for (int i = 0; i < words.size(); i++) {
                words.set(i, words.get(i).replace(".", ""));
                words.set(i, words.get(i).replace(",", ""));
            }


            for (String findingWord : words) {

                int quantity = 0;

                for (int i = 0; i < words.size(); i++) {
                    if (findingWord.equals(words.get(i))) {
                        quantity++;
                    }
                }

                System.out.println("Word: " + findingWord + ", quantity: " + quantity);
            }
        }

        public static void findPrice(String text, int priceForWord) {

            ArrayList<String> words = new ArrayList<>(Arrays.asList(text.split(" ")));
            System.out.println("Price for text: " + words.size()*priceForWord);
        }

        public static void deleteChar(String text) {
            System.out.println("Text: " + text);

            char[] characters = text.toCharArray();
            for (char character : characters) {

                if (!Character.isLetterOrDigit(character) && (character != ' ')) {

                    text = text.replace(Character.toString(character), "");
                }
            }
            System.out.println("Corrected text: " + text);
        }

    }
