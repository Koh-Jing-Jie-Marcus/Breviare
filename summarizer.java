import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.Scanner;
import java.util.HashMap;

public class summary {
    public static void main (String args[])
    {
        // Gets input, self explanatory
        String para = get_input(); 

        // Splits raw input into its constituent words (roughly, this will be cleaned up later)
        String[] wordList = para.split(" "); 

        // Generates a HashMap (dictionary) to store the indiviual scores of each word
        HashMap<String, Integer> wordScore = new HashMap<String, Integer>();

        // Iterates through every word in the word list
        int old_score;
        for (String word : wordList){

            // Cleans up word. 
            String cleaned = cleanup(word);

            if (wordScore.containsKey(cleaned))
            {
                old_score = wordScore.get(word);
                
            } else {
                old_score = 0;
            }
            wordScore.put(word, old_score + 1)
        }
    }

    private static String get_input () // Gets input
    {
        Scanner scanner = new Scanner (System.in);
        String raw_input = scanner.nextLine();
        return raw_input;
    }

    private static String cleanup (String word)
    {
        String raw_word = word.toLowerCase();
        String cleaned_word;
        for 
    }
}
