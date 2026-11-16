/*
 * ImageApp creates three color effects, three rotations, and a composite.
 * Each effect traverses a two-dimensional array of Pixel objects. The eight
 * results are saved to output/ and displayed when a graphical desktop exists.
 */
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.File;

public class ImageApp {
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

  // Clockwise rotation
  public static Picture rotate(Picture original, int degrees) {
    if (degrees != 90 && degrees != 180 && degrees != 270) {
      throw new IllegalArgumentException("Rotation must be 90, 180, or 270 degrees.");
    }
    Pixel[][] source = original.getPixels2D();
    int height = source.length;
    int width = source[0].length;
    Picture result = degrees == 180 ? new Picture(height, width) : new Picture(width, height);
    Pixel[][] target = result.getPixels2D();
    Matrix2by2 matrix;
    int rowOffset;
    int colOffset;
    if (degrees == 90) {
      matrix = new Matrix2by2(0, -1, 1, 0);
      rowOffset = 0;
      colOffset = height - 1;
    } else if (degrees == 180) {
      matrix = new Matrix2by2(-1, 0, 0, -1);
      rowOffset = height - 1;
      colOffset = width - 1;
    } else {
      matrix = new Matrix2by2(0, 1, -1, 0);
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

  public static void main(String[] args) {
    File output = new File("output");
    if (!output.isDirectory() && !output.mkdirs()) throw new IllegalStateException("Could not create output folder.");
    Picture original = new Picture("lib/beach.jpg");
    Picture overlay = new Picture("lib2/balloon.png");
    Picture[] results = {recolor(original), negative(original), grayscale(original),
        rotate(original, 180), rotate(original, 90), rotate(original, 270),
        combine(original, overlay, 30, 400, false), combine(original, overlay, 30, 400, true)};
    String[] names = {"01-recolored", "02-negative", "03-grayscale", "04-rotate-180",
        "05-rotate-90", "06-rotate-270", "07-composite", "08-composite-blend"};
    for (int i = 0; i < results.length; i++) {
      if (!results[i].write("output/" + names[i] + ".png")) throw new IllegalStateException("Could not save " + names[i]);
      if (!GraphicsEnvironment.isHeadless()) results[i].explore();
    }
  }
}
