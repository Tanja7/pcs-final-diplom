import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class StopWords {
    Set<String> ignoredWords = new HashSet<>();

    public StopWords (File textFile) throws IOException{
        String wordsIgn;
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            while((wordsIgn = reader.readLine()) != null)
                ignoredWords.add(wordsIgn);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public boolean contains(String word) {
        return ignoredWords.contains(word);
    }
}

