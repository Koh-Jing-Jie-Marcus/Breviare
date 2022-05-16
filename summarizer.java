import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.Scanner;
import java.util.HashMap;

public class summary {
    public static void main (String args[])
    {
        // Gets input, self explanatory
        String para = getInput(); 

        // Splits raw input into its constituent words (roughly, this will be cleaned up later)
        String[] wordList = para.split(" "); 

        // Generates a HashMap (dictionary) to store the indiviual scores of each word
        HashMap<String, Integer> wordScore = new HashMap<String, Integer>();

        // Additional HashMap to store where the sentences starts and end
        HashMap<Integer, Integer> startEnd = new HashMap<Integer, Integer>();



        // Iterates through every word in the word list
        int oldScore;
        for (String word : wordList){

            // Cleans up word
            String cleaned = cleanup(word);

            if (wordScore.containsKey(cleaned))
            {
                oldScore = wordScore.get(word);
                
            } else {
                oldScore = 0;
            }
            wordScore.put(word, old_score + 1);
        }

    }

    private static String getInput () // Gets input
    {
        Scanner scanner = new Scanner (System.in);
        String rawInput = scanner.nextLine();
        return rawInput;
    }

    private static String cleanup (String word)
    {
        String rawWord = word.toLowerCase();
        String cleanedWord = "";
        for (char ch: rawWord.toCharArray())
        {
            int asciiVal = (int) ch;
            if (asciiVal > 47 && asciiVal < 58 || 
                asciiVal > 65 && asciiVal < 91 || 
                asciiVal > 96 && asciiVal < 123)
            {
                cleanedWord += ch;
            }
        }
        return cleanedWord;
    }

    private static boolean isSentenceEnd (String word)
    {   
        // String[] fakeStop = {"Mr.", "Mrs", }; NOT IN USE, can hardcode if bottom doesnt work as well
        char[] wordChar = word.toCharArray();
        if (wordChar[wordChar.length-1] == '.'){
            if (Character.isLowerCase(wordChar[0])){
                return true
            }
        }
        return false
    }
}
