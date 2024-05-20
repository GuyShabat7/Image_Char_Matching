package ascii_art;

import image.Image;
import image.ImageProcess;
import image_char_matching.SubImgCharMatcher;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class represents an algorithm for generating ASCII art from an image.
 */
public class AsciiArtAlgorithm {

    /**
     * The image to be converted to ASCII art.
     */
    private final ImageProcess imageProcess;
    /**
     * The resolution of the ASCII art (number of characters per pixel).
     */
    private final int resolution;
    /**
     * The matcher used to map image brightness to ASCII characters.
     */
    private final SubImgCharMatcher asciiChars;

    /**
     * Constructs an instance of AsciiArtAlgorithm.
     *
     * @param img The image to be converted to ASCII art.
     * @param resolution The resolution of the ASCII art (number of characters per pixel).
     * @param asciiChars The matcher used to map image brightness to ASCII characters.
     */
    public AsciiArtAlgorithm(ImageProcess img, int resolution,SubImgCharMatcher asciiChars) {

        this.imageProcess = img;
        this.resolution = resolution;
        this.asciiChars = asciiChars;
    }

    /**
     * Runs the ASCII art algorithm to convert the image to ASCII art.
     *
     * @return A 2D array of characters representing the ASCII art.
     */
    public char[][] run() {
        ArrayList<Color[][]> arraySubIm = imageProcess.makeSubImages(this.resolution);
        char[][] returnChars =
                new char[imageProcess.getNewWidth()/
                        (imageProcess.getNewHeight()/this.resolution)][this.resolution];
        int m = 0;
        for (int i = 0; i < returnChars.length; i++) {
            for (int j = 0; j < returnChars[0].length; j++) {
                double brightness = imageProcess.calcSubImgBrightness(arraySubIm.get(m));
                m++;
                char c = asciiChars.getCharByImageBrightness(brightness);
                returnChars[i][j] = c;
            }
        }
        return returnChars;
    }

}
