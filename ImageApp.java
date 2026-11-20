/*
 * ImageApp creates three color effects, three rotations, and a composite.
 * Each effect traverses a two-dimensional array of Pixel objects. The eight
 * results are saved to output/ and displayed when a graphical desktop exists.
 */
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class ImageApp {
  /** Returns a copy with each pixel's RGB channels rearranged. */
  public static Picture recolor(Picture original) {
    Picture result = new Picture(original);
    Pixel[][] pixels = result.getPixels2D();
    for (int row = 0; row < pixels.length; row++) {
      for (int col = 0; col < pixels[row].length; col++) {
        Color color = pixels[row][col].getColor();
        pixels[row][col].setColor(new Color(color.getBlue(), color.getRed(), color.getGreen()));
      }
    }
    return result;
  }

  /** Returns a photographic negative of the supplied picture. */
  public static Picture negative(Picture original) {
    Picture result = new Picture(original);
    Pixel[][] pixels = result.getPixels2D();
    for (int row = 0; row < pixels.length; row++) {
      for (int col = 0; col < pixels[row].length; col++) {
        Color color = pixels[row][col].getColor();
        pixels[row][col].setColor(new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()));
      }
    }
    return result;
  }

  /** Returns an image whose RGB channels are each the average of the original. */
  public static Picture grayscale(Picture original) {
    Picture result = new Picture(original);
    Pixel[][] pixels = result.getPixels2D();
    for (int row = 0; row < pixels.length; row++) {
      for (int col = 0; col < pixels[row].length; col++) {
        Color color = pixels[row][col].getColor();
        int average = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
        pixels[row][col].setColor(new Color(average, average, average));
      }
    }
    return result;
  }

  /** Returns a clockwise quarter-turn rotation using row-vector matrix multiplication. */
  public static Picture rotatePicture(Picture original, int degrees) {
    degrees = ((degrees % 360) + 360) % 360;
    if (degrees != 90 && degrees != 180 && degrees != 270) {
      throw new IllegalArgumentException("Rotation must be 90, 180, or 270 degrees.");
    }
    Pixel[][] source = original.getPixels2D();
    int height = source.length;
    int width = source[0].length;
    Picture result = degrees == 180 ? new Picture(height, width) : new Picture(width, height);
    Pixel[][] target = result.getPixels2D();
    Matrix2by2 matrix = new Matrix2by2(1, 0, 0, 1);
    matrix.setRotationMatrix(degrees);
    int rowOffset;
    int colOffset;
    if (degrees == 90) {
      rowOffset = 0;
      colOffset = height - 1;
    } else if (degrees == 180) {
      rowOffset = height - 1;
      colOffset = width - 1;
    } else {
      rowOffset = width - 1;
      colOffset = 0;
    }
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Vector1by2 destination = Vector1by2.multiply(new Vector1by2(row, col), matrix);
        int newRow = (int) destination.getFirst() + rowOffset;
        int newCol = (int) destination.getSecond() + colOffset;
        target[newRow][newCol].setColor(source[row][col].getColor());
      }
    }
    return result;
  }

  /** Returns a copy with a nonwhite overlay inserted and optionally blended. */
  public static Picture combine(Picture background, Picture overlay, int top, int left, boolean blend) {
    Picture result = new Picture(background);
    Pixel[][] large = result.getPixels2D();
    Pixel[][] small = overlay.getPixels2D();
    for (int row = 0; row < small.length; row++) {
      for (int col = 0; col < small[row].length; col++) {
        int targetRow = top + row;
        int targetCol = left + col;
        if (targetRow < 0 || targetRow >= large.length || targetCol < 0 || targetCol >= large[0].length) continue;
        Pixel foreground = small[row][col];
        Color color = foreground.getColor();
        if (color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255) continue;
        int alpha = foreground.getAlpha();
        if (blend) alpha = alpha / 2;
        Color behind = large[targetRow][targetCol].getColor();
        int red = (color.getRed() * alpha + behind.getRed() * (255 - alpha)) / 255;
        int green = (color.getGreen() * alpha + behind.getGreen() * (255 - alpha)) / 255;
        int blue = (color.getBlue() * alpha + behind.getBlue() * (255 - alpha)) / 255;
        large[targetRow][targetCol].setColor(new Color(red, green, blue));
      }
    }
    return result;
  }

  /** Returns the rotated picture as a two-dimensional pixel array. */
  public static Pixel[][] rotate(Picture original, int degrees) {
    return rotatePicture(original, degrees).getPixels2D();
  }

  /** Returns a rectangular part of a picture, with coordinates measured from its top left. */
  public static Picture crop(Picture image, int top, int left, int height, int width) {
    Pixel[][] source = image.getPixels2D();
    if (top < 0 || left < 0 || height <= 0 || width <= 0
        || top + height > source.length || left + width > source[0].length)
      throw new IllegalArgumentException("The selected rectangle must fit inside the image.");
    Picture part = new Picture(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
    for (int r = 0; r < height; r++) for (int c = 0; c < width; c++)
      part.getBufferedImage().setRGB(c, r, image.getBufferedImage().getRGB(left + c, top + r));
    return part;
  }

  /** Runs the interactive editor, or saves the original demonstrations with --demo. */
  public static void main(String[] args) {
    if (args.length > 0 && args[0].equals("--demo")) {
      saveDemo();
      return;
    }
    if (!GraphicsEnvironment.isHeadless() && (args.length == 0 || !args[0].equals("--console"))) {
      SwingUtilities.invokeLater(() -> new ImageAppUI().setVisible(true));
      return;
    }
    Picture current = new Picture("lib/beach.jpg");
    Scanner input = new Scanner(System.in);
    System.out.println("Image App: start with lib/beach.jpg");
    while (true) {
      System.out.println("\n1 Recolor  2 Negative  3 Grayscale  4 Rotate");
      System.out.println("5 Insert image  6 Insert part of image  7 Preview  8 Save  9 Reset  0 Quit");
      System.out.print("Choice: ");
      if (!input.hasNextLine()) break;
      String choice = input.nextLine().trim();
      try {
        switch (choice) {
          case "1": current = recolor(current); break;
          case "2": current = negative(current); break;
          case "3": current = grayscale(current); break;
          case "4": current = rotatePicture(current, askInt(input, "Clockwise degrees (90, 180, 270, or -90): ")); break;
          case "5":
          case "6":
            System.out.print("Overlay file (for example lib2/balloon.png): ");
            String path = input.nextLine().trim();
            if (!new File(path).isFile()) throw new IllegalArgumentException("Image file not found.");
            Picture overlay = new Picture(path);
            if (choice.equals("6")) {
              int top = askInt(input, "Crop top row: ");
              int left = askInt(input, "Crop left column: ");
              int height = askInt(input, "Crop height: ");
              int width = askInt(input, "Crop width: ");
              overlay = crop(overlay, top, left, height, width);
            }
            int row = askInt(input, "Insert top row: ");
            int col = askInt(input, "Insert left column: ");
            System.out.print("Blend? (y/n): ");
            boolean blend = input.nextLine().trim().equalsIgnoreCase("y");
            current = combine(current, overlay, row, col, blend);
            break;
          case "7":
            if (GraphicsEnvironment.isHeadless()) System.out.println("Preview requires a graphical desktop.");
            else current.explore();
            break;
          case "8":
            System.out.print("Output filename (for example result.png): ");
            String output = input.nextLine().trim();
            if (output.isEmpty() || !current.write(output)) throw new IllegalArgumentException("Could not save that file.");
            System.out.println("Saved " + output);
            break;
          case "9": current = new Picture("lib/beach.jpg"); break;
          case "0": return;
          default: System.out.println("Choose a number from the menu.");
        }
      } catch (IllegalArgumentException ex) {
        System.out.println("Could not complete that action: " + ex.getMessage());
      }
    }
  }

  /** Reads a whole number from one menu prompt. */
  private static int askInt(Scanner input, String prompt) {
    System.out.print(prompt);
    return Integer.parseInt(input.nextLine().trim());
  }

  /** Saves the original eight sample outputs for comparison. */
  private static void saveDemo() {
    File output = new File("output");
    if (!output.isDirectory() && !output.mkdirs()) throw new IllegalStateException("Could not create output folder.");
    Picture original = new Picture("lib/beach.jpg");
    Picture overlay = new Picture("lib2/balloon.png");
    Picture[] results = {recolor(original), negative(original), grayscale(original),
        rotatePicture(original, 180), rotatePicture(original, 90), rotatePicture(original, 270),
        combine(original, overlay, 30, 400, false), combine(original, overlay, 30, 400, true)};
    String[] names = {"01-recolored", "02-negative", "03-grayscale", "04-rotate-180",
        "05-rotate-90", "06-rotate-270", "07-composite", "08-composite-blend"};
    for (int i = 0; i < results.length; i++) {
      if (!results[i].write("output/" + names[i] + ".png")) throw new IllegalStateException("Could not save " + names[i]);
    }
  }
}
