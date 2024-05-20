package image;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * This class provides functionality for processing images for ASCII art conversion.
 */
public class ImageProcess  {

    /**
     * The original image to be processed.
     */
    private Image image;
    /**
     * The processed image with padding.
     */
    private Color[][] imProcessed;
    /**
     * Constructs an ImageProcess object with the given image.
     *
     * @param im The image to be processed.
     */
    public ImageProcess(Image im) {
        this.image = im;
        imProcessed = paddingImage(this.image);

    }

    /**
     * Pads the image to ensure it has dimensions that are powers of 2.
     *
     * @param im The image to be padded.
     * @return The padded image.
     */
    public Color[][] paddingImage(Image im) {
        Vector<Integer> newVecDimensions = newDimensions();
        Color[][] newPixelsArray = new Color[newVecDimensions.get(0)][newVecDimensions.get(1)];
        int heightPad = (newVecDimensions.get(0) - im.getHeight())/2;
        int widthPad = (newVecDimensions.get(1) - im.getWidth())/2;
        for (int i = 0; i < im.getHeight(); i++) {
            for (int j = 0; j < im.getWidth(); j++) {
                newPixelsArray[i + heightPad][j + widthPad] = im.getPixel(i, j);
            }
        }
        for (int i = 0; i < newPixelsArray.length; i++) {
            for (int j = 0; j < newPixelsArray[0].length; j++) {
                if (newPixelsArray[i][j] == null) {
                    newPixelsArray[i][j] = Color.WHITE;
                }
            }
        }
        return newPixelsArray;
    }

    /**
     * Gets the height of the processed image.
     *
     * @return The height of the processed image.
     */
    public int getNewHeight() {
        return imProcessed.length;
    }

    /**
     * Gets the width of the processed image.
     *
     * @return The width of the processed image.
     */
    public int getNewWidth() {
        return imProcessed[0].length;
    }

    /**
     * Calculates the new dimensions for the padded image.
     *
     * @return A vector containing the new height and width of the image.
     */
    private Vector<Integer> newDimensions() {
        Vector<Integer> dimensionsVec = new Vector<>();
        int newWidth = calcNewDimension(this.image.getWidth());
        int newHeight = calcNewDimension(this.image.getHeight());
        dimensionsVec.add(0, newHeight);
        dimensionsVec.add(1, newWidth);
        return dimensionsVec;
    }

    /**
     * Calculates a new dimension that is a power of 2.
     *
     * @param dim The original dimension.
     * @return The new dimension that is a power of 2.
     */
    private int calcNewDimension(int dim) {
        int temp = dim;
        while (temp % 2 == 0) {
            temp = temp / 2;
        }
        if (temp == 1) {
            return dim;
        } else {
            int initSize = 2;
            while(initSize < dim) {
                initSize = initSize * 2;
            }
            return initSize;
        }
    }

    /**
     * Creates sub-images from the processed image.
     *
     * @param subImageResolution The resolution of the sub-images.
     * @return An ArrayList containing the sub-images.
     */
    public ArrayList<Color[][]> makeSubImages (int subImageResolution) {
        int cols = this.imProcessed.length/subImageResolution; // number of columns of subImage
        int rows = this.imProcessed[0].length/cols;
        // cols = subResolution
        ArrayList<Color[][]> subImageArray = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < subImageResolution; j++) {
                int startRow = i * cols;
                int startCol = j * cols;
                subImageArray.add(subImage(this.imProcessed, startRow, startCol, cols));
            }
        }
        return subImageArray;
    }

    /**
     * Extracts a sub-image from the full image.
     *
     * @param fullImage The full image.
     * @param row The starting row of the sub-image.
     * @param col The starting column of the sub-image.
     * @param size The size of the sub-image.
     * @return The extracted sub-image.
     */
    private Color[][] subImage (Color[][] fullImage, int row, int col, int size) {
        Color[][] subImage = new Color[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                subImage[i][j] = fullImage[row + i][col + j];
            }
        }
        return subImage;
    }

    /**
     * Calculates the brightness of a sub-image.
     *
     * @param subImg The sub-image for which to calculate brightness.
     * @return The brightness value of the sub-image.
     */
    public double calcSubImgBrightness(Color[][] subImg) {
        double sumGreyPixels = 0;
        for (Color[] colors : subImg) {
            for (int col = 0; col < subImg.length; col++) {
                Color pixel = colors[col];
                double greyPixel = pixel.getRed()*0.2126 + pixel.getGreen()*0.7152 + pixel.getBlue()*0.0722;
                sumGreyPixels += greyPixel;
            }
        }
        return (sumGreyPixels / (subImg.length * subImg.length)) / 255;
    }
}
