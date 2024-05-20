package image_char_matching;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class provides functionality to match ASCII characters to image brightness.
 */
public class SubImgCharMatcher {

    /**
     * Default resolution renderer
     */
    public static final int DEFAULT_RESOLUTION = 16 * 16;
    /**
     * The list of characters used for matching.
     */
    private ArrayList<Character> charArray;
    /**
     * A map to store the brightness values of ASCII characters.
     */
    private static final HashMap<Character, Double> asciiBrightMap = new HashMap<>();

    /**
     * Constructs a SubImgCharMatcher with the given character set.
     *
     * @param charset The character set used for matching.
     */
    public SubImgCharMatcher(char[] charset) {
        this.charArray = new ArrayList<>();
        for (char c : charset) {
            this.charArray.add(c);
        }
    }

    /**
     * Matches the given brightness to the closest ASCII character.
     *
     * @param brightness The brightness value to match.
     * @return The closest ASCII character.
     */
    public char getCharByImageBrightness(double brightness) {
        char closestChar = 0;
        double diff = 1;
        HashMap<Character, Double> newBrightnessMap = calcNormalizeBrightness(this.charArray);
        for (char c : this.charArray) {
            double charBrightness = newBrightnessMap.get(c);
            if (Math.abs(charBrightness - brightness) <= diff) {
                if (Math.abs(charBrightness - brightness) == diff) {
                    if (c < closestChar) {
                        closestChar = c;
                    }
                } else {
                    closestChar = c;
                    diff = Math.abs(charBrightness - brightness);
                }
            }
        }
        return closestChar;
    }

    /**
     * Calculates and returns a map of characters to normalized brightness values.
     *
     * @param charArray The list of characters to calculate brightness for.
     * @return A map of characters to normalized brightness values.
     */
    private HashMap<Character, Double> calcNormalizeBrightness(ArrayList<Character> charArray) {
        double maxBrightness = 0;
        double minBrightness = 255;
        for (char c : charArray) {
            if (!asciiBrightMap.containsKey(c)){
                calcNormalizeNumber(c);
            }
            double charBrightness =  asciiBrightMap.get(c);
            if (charBrightness > maxBrightness) {
                maxBrightness = charBrightness;
            }
            if (charBrightness < minBrightness) {
                minBrightness = charBrightness;
            }
        }
        return convertToNewBrightness(maxBrightness, minBrightness);
    }

    /**
     * Converts the brightness values to a new scale between 0 and 1.
     *
     * @param maxBrightness The maximum brightness value.
     * @param minBrightness The minimum brightness value.
     * @return A map of characters to normalized brightness values.
     */
    private HashMap<Character, Double> convertToNewBrightness(double maxBrightness,
                                                              double minBrightness) {
        HashMap<Character, Double> newBrightnessMap = new HashMap<>();
        for (char c : charArray) {
            double charBrightness =  asciiBrightMap.get(c);
            double newBrightness =
                    (charBrightness - minBrightness)/(maxBrightness - minBrightness);
            newBrightnessMap.put(c, newBrightness);
        }
        return newBrightnessMap;
    }

    /**
     * Calculates and stores the brightness value for a given character.
     *
     * @param c The character to calculate brightness for.
     */
    private void calcNormalizeNumber(char c) {
        double numOfTrue = 0;
        boolean[][] boolArray = CharConverter.convertToBoolArray(c);
        for (int i = 0; i < boolArray.length ; i++){
            for (int j = 0; j < boolArray[i].length ; j++) {
                if (boolArray[i][j]) {
                    numOfTrue+=1;
                }
            }
        }
        asciiBrightMap.put(c,numOfTrue / DEFAULT_RESOLUTION);
    }

    /**
     * Adds a character to the character set.
     *
     * @param c The character to add.
     */
    public void addChar(char c) {
        if (charArray.contains(c)) {
           return;
        }
        charArray.add(c);
    }

    /**
     * Removes a character from the character set.
     *
     * @param c The character to remove.
     */
    public void removeChar(char c) {
        if (!charArray.contains(c)) {
            return;
        }
        charArray.remove(Character.valueOf(c));
    }

    /**
     * Returns the character set.
     *
     * @return The character set.
     */
    public ArrayList<Character> getter() {
        return charArray;
    }

}
