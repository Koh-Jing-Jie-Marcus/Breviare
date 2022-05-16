import java.util.*;

public class summarizer {
    public static void main (String args[])
    {
        // Gets input, self explanatory
        String para = getInput(); 

        // Splits raw paragraph into its constituent words (roughly, this will be cleaned up later)
        String[] wordList = para.split(" "); 

        // Generates a HashMap (dictionary) to store the indiviual scores of each word
        HashMap<String, Integer> wordScore = new HashMap<String, Integer>();

        // Additional HashMap to store where the sentences starts and end
        HashMap<Integer, Integer> startEnd = new HashMap<Integer, Integer>();

        // Additional HashMap to store scores for sentences
        HashMap<Integer, Integer> startScore = new HashMap<Integer, Integer>();

        int start = 0;
        int lasti;
        while (start < wordList.length)
        {
            for (int i = start; i < wordList.length + 1; i++)
            {
                if (isSentenceEnd(wordList[i]))
                {   
                    lasti = i;
                    startEnd.put(start, lasti);
                    break;
                }

            }
            start = lasti + 1;
        }

        // Iterates through every word in the word list to assign score
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
            wordScore.put(word, oldScore + 1);
        }

        int senScore = 0;
        for (int startSen : startEnd.keySet()){
            for (int i = startSen; i < startEnd.get(startSen) + 1; i ++)
            {
                String cleaned = cleanup(wordList[i]);
                senScore += wordScore.get(cleaned);
            }
            startScore.put(startSen, senScore);
            senScore = 0;
        }

        int nthMax = 5;

        int maxMax = 0;
        for (int i: startScore.keySet())
        {
            if (startScore.get(i) > maxMax)
            {
                maxMax = startScore.get(i);
            }
        }

        int otherMax = 0;
        int startKey;
        int endKey;

        for (int i = 0; i < nthMax - 1; i++){
            for (int senSc : startScore.values())
            {
                if(senSc > otherMax && senSc < maxMax)
                {
                    otherMax = senSC;
                }
                
                for (int h : startScore.keySet())
                {
                    if (startScore.get(h) == otherMax)
                    {
                        startKey = h;
                        endKey = startEnd.get(startKey);
                        break;
                    }
                }

                for (int j = startKey; j < endKey + 1; j++)
                {
                    System.out.println(wordList[j]);
                }
            }
            maxMax = otherMax;
            otherMax = 0;
        }

    }

    public static String getInput () // Gets input
    {
        Scanner scanner = null;
        try
        {
            scanner = new Scanner (System.in);
            String rawInput = scanner.nextLine();
            return rawInput;
        }
        finally
        {
            if (scanner!=null)
            {
                scanner.close();
            }
        }
    }

    public static String cleanup (String word)
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

    public static boolean isSentenceEnd (String word)
    {   
        // String[] fakeStop = {"Mr.", "Mrs", }; NOT IN USE, can hardcode if bottom doesnt work as well
        char[] wordChar = word.toCharArray();
        if (wordChar[wordChar.length-1] == '.'){
            if (Character.isLowerCase(wordChar[0])){
                return true;
            }
        }
        return false;
    }
}
