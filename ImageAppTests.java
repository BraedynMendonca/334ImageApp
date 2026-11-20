import java.awt.Color;
import java.awt.image.BufferedImage;

/** Black-box tests for image effects, rotation, crop, and insertion. */
public class ImageAppTests {
  private static int checks;
  private static void color(Pixel pixel, int red, int green, int blue) {
    Color actual = pixel.getColor();
    if (actual.getRed() != red || actual.getGreen() != green || actual.getBlue() != blue)
      throw new AssertionError("Expected " + new Color(red, green, blue) + " but got " + actual);
    checks++;
  }
  private static void check(boolean condition, String message) {
    if (!condition) throw new AssertionError(message);
    checks++;
  }
  private static void invalid(Runnable action) {
    try { action.run(); throw new AssertionError("Expected IllegalArgumentException"); }
    catch (IllegalArgumentException expected) { checks++; }
  }
  /** Runs the image tests with small in-memory pictures. */
  public static void main(String[] args) {
    Picture source = new Picture(2, 3);
    Pixel[][] p = source.getPixels2D();
    p[0][0].setColor(new Color(10, 20, 30)); p[0][1].setColor(Color.RED); p[0][2].setColor(Color.GREEN);
    p[1][0].setColor(Color.BLUE); p[1][1].setColor(Color.WHITE); p[1][2].setColor(Color.BLACK);
    color(ImageApp.recolor(source).getPixels2D()[0][0], 30, 10, 20);
    color(ImageApp.negative(source).getPixels2D()[0][0], 245, 235, 225);
    color(ImageApp.grayscale(source).getPixels2D()[0][0], 20, 20, 20);
    color(source.getPixels2D()[0][0], 10, 20, 30);
    Pixel[][] clockwise = ImageApp.rotate(source, 90);
    check(clockwise.length == 3 && clockwise[0].length == 2, "90-degree dimensions");
    color(clockwise[0][0], 0, 0, 255); color(clockwise[0][1], 10, 20, 30);
    Pixel[][] reverse = ImageApp.rotate(source, 180);
    color(reverse[0][0], 0, 0, 0); color(reverse[1][2], 10, 20, 30);
    Pixel[][] ccw = ImageApp.rotate(source, -90);
    color(ccw[0][0], 0, 255, 0); color(ccw[2][1], 0, 0, 255);
    invalid(() -> ImageApp.rotate(source, 45));
    Picture crop = ImageApp.crop(source, 0, 1, 2, 2);
    check(crop.getHeight() == 2 && crop.getWidth() == 2, "crop dimensions");
    color(crop.getPixels2D()[0][0], 255, 0, 0);
    invalid(() -> ImageApp.crop(source, 1, 2, 2, 1));
    invalid(() -> ImageApp.crop(source, 0, 0, 0, 1));
    Picture bg = new Picture(2, 2); for (Pixel[] row : bg.getPixels2D()) for (Pixel px : row) px.setColor(new Color(100, 100, 100));
    BufferedImage raw = new BufferedImage(2, 1, BufferedImage.TYPE_INT_ARGB);
    raw.setRGB(0, 0, 0xffffffff); raw.setRGB(1, 0, 0xffff0000);
    Picture overlay = new Picture(raw);
    Picture combined = ImageApp.combine(bg, overlay, 0, 0, false);
    color(combined.getPixels2D()[0][0], 100, 100, 100);
    color(combined.getPixels2D()[0][1], 255, 0, 0);
    color(bg.getPixels2D()[0][1], 100, 100, 100);
    color(ImageApp.combine(bg, overlay, 0, 0, true).getPixels2D()[0][1], 177, 50, 50);
    Picture clipped = ImageApp.combine(bg, overlay, 1, -1, false);
    color(clipped.getPixels2D()[1][0], 255, 0, 0);
    Picture twice = ImageApp.combine(combined, overlay, 1, 0, false);
    color(twice.getPixels2D()[0][1], 255, 0, 0);
    color(twice.getPixels2D()[1][1], 255, 0, 0);
    BufferedImage transparent = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    transparent.setRGB(0, 0, 0x000000ff);
    Picture croppedTransparent = ImageApp.crop(new Picture(transparent), 0, 0, 1, 1);
    color(ImageApp.combine(bg, croppedTransparent, 0, 0, false).getPixels2D()[0][0], 100, 100, 100);
    System.out.println(checks + " image checks passed.");
  }
}
