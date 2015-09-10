import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
       
        //TODO
        FileReader fr = new FileReader(this.inputFileName);
        BufferedReader textReader = new BufferedReader(fr);
        String line = textReader.readLine();
        List<String> lines = new ArrayList();
        List<Integer> indexes = Arrays.asList(this.getIndexes());
        
        while(line != null) {  	
        	lines.add(line);
            line = textReader.readLine();
        }
        
        textReader.close();
        
        String[] linesArray = new String[lines.size()];
        Map<String, Integer> m = new LinkedHashMap<String, Integer>();
        lines.toArray(linesArray);
        
        for (int index : indexes) {
        	line =  linesArray[index]; 
    		StringTokenizer tokenizer = new StringTokenizer(line, this.delimiters);
    		List<String> lineList = new ArrayList<String>();
    		
    		while(tokenizer.hasMoreTokens()) {
    			lineList.add(tokenizer.nextToken().trim().toLowerCase());
    		}
    		
        	List<String> stopWordsList = Arrays.asList(this.stopWordsArray);
        	lineList.removeAll(stopWordsList);
        	
        	
        	for (String a : lineList) {
                Integer freq = m.get(a);
                m.put(a, (freq == null) ? 1 : freq + 1);
            }
        	
        }
        
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(m.entrySet());
    	Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
    		public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b){
    			int returnVal = b.getValue().compareTo(a.getValue()) == 0 ? a.getKey().compareTo(b.getKey()) : b.getValue().compareTo(a.getValue());
    			
    			return returnVal;
    		}
    	});
    	int arraySize = entries.size() > 20 ? 20 : entries.size(); 
    	for (int i = 0; i < arraySize; i++ ) {
    		ret[i] = entries.get(i).getKey();
    	}
    	
        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
