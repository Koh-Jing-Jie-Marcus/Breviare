import java.util.*;


// TODO: 
// Clean up stuff, dont put everything in Main(), split into different functions - RESOLVED(?)
// Remove the stupid scanner try/except, the error only exists in eclipse - RESOLVED
// Make another input function for number of iterations (current set at 5) - RESOLVED
// Clean up or amend maxMax and stuff. The max score sentence doesnt print currently - uhhhhh
// Rank the sentences, and return them chronologically. Not v impt, but makes the output nicer
// Test the code. I still dk if its working or not.
// Think of more possible exceptions to '.'? Rn (n.d.) will trigger it, maybe add a list of exclusive words?
// After that can unit test each stuff, i dont even know if its properly working lol

public class summarizer {
        public static void main (String args[])
    {
        // Gets input for the main chunk of text to be summarized, self explanatory
        System.out.println("Input the chunk of text you want summarised");
        String para = getInput(); 

        // Gets input for number of summarized sentences to output
        System.out.println("Input number of output sentences you want");
        int numOutput = Integer.parseInt(getInput());

        // TODO: Add a check for if numOutput is > the total sentences in the main text. Maybe try/except?

        // Splits raw paragraph into its constituent words (roughly, this will be cleaned up later)
        String[] wordList = para.split(" "); 

        // Generates a HashMap (dictionary) to store the indiviual scores of each word
        HashMap<String, Integer> wordScore = new HashMap<String, Integer>();

        // Additional HashMap to store where the sentences starts and end
        HashMap<Integer, Integer> startEnd = new HashMap<Integer, Integer>();

        // Additional HashMap to store scores for sentences
        HashMap<Integer, Integer> startScore = new HashMap<Integer, Integer>();

        // Gets all the sentence ends
        startEnd.putAll(getEnd(wordList)); 

        // Iterates through every word in the word list to assign score
        wordScore.putAll(getScore(wordList));

        // Assigns scores for each sentence
        startScore.putAll(setScore(wordScore, startEnd, wordList));

        int upperbound = 99999; // lmao
        for (int j = 0; j < numOutput; j++)
        {
           upperbound = maxScores(startScore, startEnd, wordList, upperbound);
        }
    }

    public static Map<Integer, Integer> getEnd (String[] wList)
    {
        HashMap<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
        int start = 0;
        int lasti = 0;
        int Length = wList.length;

        while (start < Length)
        {
            for(int i = start; i < Length + 1; i++)
            {
                if (isSentenceEnd(wList[i]))
                {
                    lasti = i;
                    tempMap.put(start, lasti);
                    break;
                }
            }
            start = lasti + 1;
        }
        return tempMap;
    }

    public static String getInput () // Gets input
    {
        Scanner scanner = new Scanner (System.in);
        String rawInput = scanner.nextLine();
        return rawInput;
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

    public static int maxScores (Map<Integer,Integer> sSmap, Map<Integer, Integer> sEMap, String[] wList, int upper)
    {
        int senScore;
        int maxScore = 0;
        int maxKey = 0;
        for (int key : sSmap.keySet())
        {
            senScore = sSmap.get(key);
            if (senScore > maxScore && senScore < upper)
            {
                maxScore = senScore;
                maxKey = key;
            }
        }
        for (int i = maxKey; i < sEMap.get(maxKey) + 1; i++)
        {
            System.out.print(wList[i] + " ");
        }
        return maxScore;
    }

    public static Map<String, Integer> getScore (String[] wList)
    {
        HashMap<String, Integer> tempMap = new HashMap<String, Integer>();
        int oldScore;
        for (String word : wList)
        {
            String cleaned = cleanup(word);
            if (tempMap.containsKey(cleaned))
            {
                //System.out.println(cleaned);
                oldScore = tempMap.get(cleaned);
            } else {
                oldScore = 0;
            }
            tempMap.put(cleaned, oldScore + 1);
        }
        return tempMap;
    }

    public static Map<Integer, Integer> setScore (Map<String, Integer> scoreMap, Map<Integer, Integer> startMap, String[] wList)
    {
        HashMap<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
        int senScore = 0;
        for (int startSen : startMap.keySet()){
            {
                for (int i = startSen; i < startMap.get(startSen) + 1; i ++)
                {
                    String cleaned = cleanup(wList[i]);
                    senScore += scoreMap.get(cleaned);
                }
                tempMap.put(startSen, senScore);
                senScore = 0;
            }
        }
        return tempMap;
    }

}

