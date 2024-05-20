package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;

import javax.sound.midi.Soundbank;
import java.io.IOException;

/**
 * Represents a shell for interacting with the ASCII art program.
 */
public class Shell {

    /**
     * The default resolution for ASCII art generation.
     */
    public static final int DEFAULT_RESOLUTION = 128;
    /**
     * The character set matcher for ASCII art conversion.
     */
    private final SubImgCharMatcher charsSet;
    /**
     * The name of the output file.
     */
    private final String outputFile;
    /**
     * The resolution for ASCII art generation.
     */
    private int resolution;
    /**
     * The image to be converted to ASCII art.
     */
    private Image image;
    /**
     * The output strategy for the ASCII art.
     */
    private AsciiOutput output;
    /**
     * The methods for shell operations.
     */
    private ShellMethods shellMethods;

    /**
     * Constructs a Shell object.
     *
     * @throws IOException If there is a problem with the image file.
     */
    public Shell() throws IOException {
        char[] charsArray = {'0','1','2','3','4','5','6','7','8','9'};
        this.charsSet = new SubImgCharMatcher(charsArray);
        this.resolution = DEFAULT_RESOLUTION;
        this.image = new Image("cat.jpeg");
        this.outputFile = "out";
        this.output = new ConsoleAsciiOutput();
        this.shellMethods = new ShellMethods(charsSet, resolution, image, outputFile, output);
    }

    /**
     * Runs the shell, allowing interaction with the ASCII art program.
     */
    public void run()  {
        boolean runFlag = true;
        while (runFlag) {
            System.out.print(">>> ");
            String input = KeyboardInput.readLine();
            if (input.equals("exit")) {
                runFlag = false;
            } else {
                try {
                    shellMethods.run(input);
                } catch (IllegalCommandException | IncorrectFormatException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println("Did not execute due to problem with image file.");
                }
            }
        }

    }

    /**
     * Main method to start the ASCII art shell.
     *
     * @param args Command-line arguments (not used).
     * @throws IOException If there is a problem with the image file.
     */
    public static void main(String[] args) throws IOException {
        Shell shell = new Shell();
        shell.run();
    }
}
