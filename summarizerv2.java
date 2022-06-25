import java.util.*;
import java.lang.Math;

public class summarizerv2 {
        public static void main (String args[])
    {
        // Gets input for the main chunk of text to be summarized, self explanatory
        System.out.println("Input the chunk of text you want summarised");
        String para = getInput(); 

        // Splits raw paragraph into its constituent words (roughly, this will be cleaned up later)
        String[] wordList = para.split(" "); 

        // Additional HashMap to store where the sentences starts and end
        HashMap<Integer, Integer> startEnd = new HashMap<Integer, Integer>();

        // Additional HashMap to store scores for sentences
        HashMap<Integer, Double> startScore = new HashMap<Integer, Double>();

        // Gets all the sentence ends
        startEnd.putAll(getEnd(wordList)); 

        // Gets input for number of summarized sentences to output
        System.out.println("Input number of output sentences you want");
        int numOutput = Integer.parseInt(getInput());

        // Checks if specified number of output is more than possible
        while (numOutput > startEnd.size())
        {
            System.out.println("Error. The number of output sentences exceeds the total number of sentences in the source text.");
            System.out.println("Input number of output sentences you want");
            numOutput = Integer.parseInt(getInput());
        }

        // Iterates through every word in the word list to assign score
        Double[] allScore = getScore(wordList, startEnd);

        // Assigns scores for each sentence
        startScore.putAll(setScore(allScore, startEnd));

        // gets highest scoring sentences
        int[] bestSentences = rankSentences(startScore, startEnd, numOutput);

        Arrays.sort(bestSentences);
        
        for (int i = 0; i < bestSentences.length; i++)
        {
            int sent = bestSentences[i];
            for (int j = sent; j < startEnd.get(sent)+1; j++)
            {
                System.out.print(wordList[j] + " ");
            }
            System.out.println("");
            System.out.println("");
        }
    }

    public static int[] rankSentences (Map<Integer, Double> sSMap, Map<Integer, Integer> sEMap, int maxOut)
    {
        int[] tempList = new int[maxOut];
        Double upper = 99999.0;
        Double max;
        int maxKey = 0;
        for (int i = 0; i < maxOut; i++)
        {
            max = 0.0;
            for (int startSen: sSMap.keySet())
            {
                Double currentScore = sSMap.get(startSen);
                if (currentScore > max && currentScore < upper)
                {
                    max = currentScore;
                    maxKey = startSen;
                }
            }
            upper = max;
            tempList[i] = maxKey;
        }
        return tempList;
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
        String[] fakeStop = {"mr.", "mrs.", "al.", "i.e.", "n.d.", "feat."}; // Changed to hardcode
        for (int i = 0; i < fakeStop.length; i++){
            if (word.toLowerCase() == fakeStop[i]){
                return false;
            }
        }
        char[] wordChar = word.toCharArray();
        if (wordChar.length > 3 && wordChar[wordChar.length-3] == '.')
        {
            return false;
        }
        if (wordChar[wordChar.length-1] == '.')
        {
            return true;
        }
        return false;
    }

    public static Double[] getScore (String[] wList, Map<Integer, Integer> startE)
    {
        HashMap<String, Double> localScore = new HashMap<String, Double>(); //I dont actually remember whats this used for, so im just using it to track which words have been checked
        Double[] wholeScore = new Double[wList.length];
        int docLength = startE.size();
        int oldSentence = -999;
        Double rawWeight;
        for (int i = 0; i < wList.length; i ++)
        {
            String word = wList[i];
            String cleaned = cleanup(word);
            int currentSentence = getSentenceNum(i, startE);
            int nextSentence = startE.get(currentSentence) + 1;
            if (currentSentence != oldSentence)
            {
                localScore.clear();
            }
            if (localScore.containsKey(cleaned))
            {
                wholeScore[i] = localScore.get(cleaned);
                continue;
            } else {
                Double sentenceOccur = 0.0;
                for (int j = currentSentence; j < nextSentence; j++)
                {
                    if (cleanup(wList[j]).equals(cleaned))
                    {
                        sentenceOccur += 1;
                    }
                }

                Double documentOccur = 0.0;
                for (int startSen: startE.keySet())
                {
                    for (int k = startSen; k < startE.get(startSen) + 1; k++)
                    {
                        if (cleanup(wList[k]).equals(cleaned))
                        {
                            documentOccur += 1;
                            break;
                        }
                    }
                }
                rawWeight = sentenceOccur * Math.log(docLength / documentOccur);
                wholeScore[i] = rawWeight;
                localScore.put(cleaned, rawWeight);
            }
            oldSentence = currentSentence;
        }
        return wholeScore;
    }

    public static int getSentenceNum (int currentIter, Map<Integer, Integer> startG)
    {
        int placeholder = 0;
        for (int startSen: startG.keySet())
        {
            if (currentIter >= startSen)
            {
                placeholder = startSen;
                break;
            }
        }
        return placeholder;
    }

    public static Map<Integer, Double> setScore (Double[] scoreList, Map<Integer, Integer> startMap)
    {
        HashMap<Integer, Double> tempMap = new HashMap<Integer, Double>();
        for (int startSen: startMap.keySet())
        {
            Double score = 0.0;
            for (int i = startSen; i < startMap.get(startSen) + 1; i++)
            {
                score += scoreList[i];
            }
            tempMap.put(startSen, score);
        }
        return tempMap;
    }

}
