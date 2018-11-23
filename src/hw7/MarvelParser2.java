package hw7;

import java.io.*;
import java.util.*;

import hw6.MarvelParser.MalformedDataException;

/**
 * Parser utility to load the Marvel Comics dataset.
 * Updated to add frequency values for 
 * the number of books a character shares with
 * another one. 
 */
public class MarvelParser2 {
    
  /**
   * Reads the Marvel Universe dataset.
   * Each line of the input file contains a character name and a comic
   * book the character appeared in, separated by a tab character
   * 
   * @requires filename is a valid file path
   * @param filename the file that will be read
   * @param charMap the map to be filled with character
   *        and frequency values. 
   * @effects fills charMap with character and frequency values. 
   * @throws MalformedDataException if the file is not well-formed:
   *          each line contains exactly two tokens separated by a tab,
   *          or else starting with a # symbol to indicate a comment line.
   */
  public static void parseData(String filename,
      Map<String, HashMap<String,Double>> charMap) throws MalformedDataException {
    // Why does this method accept the Collections to be filled as
    // parameters rather than making them a return value? To allows us to
    // "return" two different Collections. If only one or neither Collection
    // needs to be returned to the caller, feel free to rewrite this method
    // without the parameters. Generally this is better style.
    BufferedReader reader = null;
    try {
        reader = new BufferedReader(new FileReader(filename));
        
        HashMap<String,ArrayList<String>> books = new HashMap<>();

        // Construct the collections of characters and books, one
        // <character, book> pair at a time.
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {

            // Ignore comment lines.
            if (inputLine.startsWith("#")) {
                continue;
            }

            // Parse the data, stripping out quotation marks and throwing
            // an exception for malformed lines.
            inputLine = inputLine.replace("\"", "");
            String[] tokens = inputLine.split("\t");
            if (tokens.length != 2) {
                throw new MalformedDataException("Line should contain exactly one tab: "
                                                 + inputLine);
            }

            String character = tokens[0];
            String book = tokens[1];
            
            if(!charMap.containsKey(character)){
                charMap.put(character, new HashMap<String,Double>());
            }
            
            if(!books.containsKey(book)){
                ArrayList<String> chars = new ArrayList<>();
                chars.add(character);
                books.put(book, chars);
            }
            // otherwise, update edge label (if books does contain the book)
            else{
                ArrayList<String> chars = books.get(book);
                HashMap<String,Double> freq = charMap.get(character);
                for(String charac: chars){
                    if(!freq.containsKey(charac)){
                        freq.put(charac, 1.0);
                    }
                    else{
                        freq.put(charac, freq.get(charac) + 1.0);
                    }
                    
                    String characOther =  charac;
                    HashMap<String,Double> freqOther = charMap.get(characOther);
                    if(!freqOther.containsKey(character)){
                        freqOther.put(character, 1.0);
                    }
                    else{
                        freqOther.put(character, freqOther.get(character)+1.0);
                    }
                    
                }
                chars.add(character);
                books.put(book,chars);
            }
        }
    } catch (IOException e) {
        System.err.println(e.toString());
        e.printStackTrace(System.err);
    } finally {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println(e.toString());
                e.printStackTrace(System.err);
            }
        }
    }
  }

}

