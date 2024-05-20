package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageProcess;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

/**
 * Provides methods for executing commands in the ASCII art shell.
 */
public class ShellMethods {
    /**
     * The character set matcher for ASCII art conversion.
     */
    private final SubImgCharMatcher charsSet;
    /**
     * The resolution for ASCII art generation.
     */
    private int resolution;
    /**
     * The name of the output file.
     */
    private ImageProcess img;
    /**
     * The name of the output file.
     */
    private final String outputFile;
    /**
     * The output strategy for the ASCII art.
     */
    private AsciiOutput output;

    /**
     * The array of valid commands.
     */
    private final String[] trueCommands = {"chars", "add", "remove", "res", "image", "output", "asciiArt"};

    /**
     * Constructs a ShellMethods object.
     *
     * @param charsSet   The character set matcher.
     * @param resolution The resolution for ASCII art generation.
     * @param img        The image to be converted to ASCII art.
     * @param outputFile The name of the output file.
     * @param output     The output strategy for the ASCII art.
     */
    public ShellMethods(
                        SubImgCharMatcher charsSet,
                        int resolution,
                        Image img,
                        String outputFile,
                        AsciiOutput output) {
        this.charsSet = charsSet;
        this.resolution = resolution;
        this.img = new ImageProcess(img);
        this.outputFile = outputFile;
        this.output = output;
    }

    /**
     * Executes the given command.
     *
     * @param input The input command.
     * @throws IOException If there is a problem with the image file.
     */
    public void run(String input) throws IOException {
        if (!catchInput(input)) {
            throw new IllegalCommandException("Did not execute due to incorrect command.");
        }
        returnInput(input);
    }

    /**
     * Checks if the input command is valid.
     *
     * @param input The input command.
     * @return True if the input command is valid, otherwise false.
     */
    private boolean catchInput (String input)  {
        input = input.split(" ")[0];
        for (String command : trueCommands) {
            if (input.equals(command)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Executes the valid input command.
     *
     * @param input The input command.
     * @throws IOException If there is a problem with the image file.
     */
    private void returnInput(String input) throws IOException {

        if (input.equals("chars")) {
            int count = 0;
            for (char c : charsSet.getter()) {
                System.out.print(c);
                count++;
                if (count < charsSet.getter().size()) {
                    System.out.print(" ");
                } else {
                    System.out.println();
                }
            }
        }
        if (input.startsWith("add")) {
            if (checkInput(input, 4, "add")){
                String suffix = input.substring(4);
                checkAdd(suffix);
            }
        }

        if (input.startsWith("remove")) {
            if (checkInput(input,7,"remove")) {
                String suffix = input.substring(7);
                checkRemove(suffix);
            }
        }

        if (input.startsWith("res")) {
            if(checkInput(input,4,"res")) {
                String suffix = input.substring(4);
                checkRes(suffix);
            }
        }

        if (input.startsWith("image")) {
            String suffix;
            if (input.length() > 5) {
                suffix = input.substring(6);
            } else {
                suffix = input.substring(5);
            }
            checkImage(suffix);
        }

        if (input.startsWith("output")) {
            if (checkInput(input,7,"output")){
            String suffix = input.substring(7);
            checkOutput(suffix);
            }
        }

        if (input.startsWith("asciiArt")) {
            if (this.charsSet.getter().isEmpty()) {
                throw new IllegalCommandException("Did not execute. Charset is empty.");
            }
            AsciiArtAlgorithm asciiArtAlgorithm =
                    new AsciiArtAlgorithm(this.img, this.resolution,this.charsSet);
            char[][] ret = asciiArtAlgorithm.run();
            output.out(ret);
        }

    }

    /**
     * Checks if the input command has the correct length.
     *
     * @param input         The input command.
     * @param correctLength The correct length of the command.
     * @param command       The type of command.
     * @return True if the input command has the correct length, otherwise false.
     */
    private boolean checkInput(String input, int correctLength, String command) {
        if (input.length() < correctLength) {
            if (command.equals("res")) {
                throw new IncorrectFormatException("Did not change resolution due to incorrect format.");
            } else if(command.equals("output")) {
                throw new IncorrectFormatException("Did not change output method due to incorrect format.");
            } else {
                throw new IncorrectFormatException("Did not " + command + " due to incorrect format.");
            }
        }
        return true;
    }

    /**
     * Checks and sets the output method based on the suffix.
     *
     * @param suffix The suffix indicating the output method.
     */
    private void checkOutput(String suffix) {
        if (suffix.equals("html")) {
            output = new HtmlAsciiOutput(this.outputFile + ".html", "Courier New");
        } else if (suffix.equals("console")) {
            output = new ConsoleAsciiOutput();
        } else {
            throw new IncorrectFormatException("Did not change output method due to incorrect format.");
        }
    }

    /**
     * Checks and sets the image based on the suffix.
     *
     * @param suffix The suffix indicating the image file name.
     * @throws IOException If there is a problem with the image file.
     */
    private void checkImage(String suffix) throws IOException {
        Image newImage = new Image(suffix);
        newImage.saveImage(suffix);
        this.img = new ImageProcess(newImage);
    }

    /**
     * Checks and sets the resolution based on the suffix.
     *
     * @param suffix The suffix indicating the change in resolution.
     */
    private void checkRes(String suffix) {
        if (!(suffix.equals("up") || suffix.equals("down"))) {
            throw new IncorrectFormatException("Did not change resolution due to incorrect format.");
        }
        if (suffix.equals("up")) {
            if (this.resolution*2 <= this.img.getNewWidth()) {
                this.resolution*=2;
            } else {
                throw new IllegalCommandException("Did not change resolution due to exceeding boundaries.");
            }
        } else  {
            if (this.resolution > (this.img.getNewWidth()/this.img.getNewHeight()) && this.resolution > 1) {
                this.resolution /= 2;
            } else {
                throw new IllegalCommandException("Did not change resolution due to exceeding boundaries.");
            }
        }
        System.out.println("Resolution set to " + this.resolution + ".");
    }


    /**
     * Checks and adds characters to the character set based on the suffix.
     *
     * @param suffix The suffix indicating the characters to add.
     */
    private void checkAdd (String suffix)  {
        char[] suffixArray = suffix.toCharArray();
        if (suffix.equals("all")) {
            for (int i = 32; i < 127; i++) {
                charsSet.addChar((char) i);
            }
        }
        else if (suffix.equals("space")) {
            charsSet.addChar((char) 32);
        }
        else if (suffixArray.length == 1 && suffixArray[0] < 127 && suffixArray[0] > 31) {
            charsSet.addChar(suffixArray[0]);
        }
        else if (suffixArray.length == 3 && suffixArray[0] < 127 && suffixArray[0] > 31 &&
                suffixArray[1] == 45 &&
                suffixArray[2] < 127 && suffixArray[2] > 31) {
            if (suffixArray[0] > suffixArray[2]) {
                for (int i = suffixArray[2]; i <= suffixArray[0]; i++) {
                    charsSet.addChar((char) i);
                }
            }
            else {
                for (int i = suffixArray[0]; i <= suffixArray[2]; i++) {
                    charsSet.addChar((char) i);
                }
            }
        } else {
            throw new IncorrectFormatException("Did not add due to incorrect format.");
        }
    }

    /**
     * Checks and removes characters from the character set based on the suffix.
     *
     * @param suffix The suffix indicating the characters to remove.
     */
    private void checkRemove(String suffix) {
        char[] suffixArray = suffix.toCharArray();
        if (suffix.equals("all")) {
            for (int i = 32; i < 127; i++) {
                charsSet.removeChar((char) i);
            }
        }
        else if (suffix.equals("space")) {
            charsSet.removeChar((char) 32);
        }
        else if (suffixArray.length == 1 && suffixArray[0] < 127 && suffixArray[0] > 31) {
            charsSet.removeChar(suffixArray[0]);
        }
        else if (suffixArray.length == 3 && suffixArray[0] < 127 && suffixArray[0] > 31 &&
                suffixArray[1] == 45 &&
                suffixArray[2] < 127 && suffixArray[2] > 31) {
            if (suffixArray[0] > suffixArray[2]) {
                for (int i = suffixArray[2]; i <= suffixArray[0]; i++) {
                    charsSet.removeChar((char) i);
                }
            }
            else {
                for (int i = suffixArray[0]; i <= suffixArray[2]; i++) {
                    charsSet.removeChar((char) i);
                }
            }
        } else {
            throw new IncorrectFormatException("Did not remove due to incorrect format.");
        }
    }
}



