/** Immutable 2 by 2 matrix; row vectors multiply on the left. */
public class Matrix2by2 {
  private final double[][] values;

  public Matrix2by2(double a, double b, double c, double d) {
    values = new double[][] {{a, b}, {c, d}};
  }

  public double get(int row, int col) {
    if (row < 0 || row > 1 || col < 0 || col > 1) throw new IndexOutOfBoundsException();
    return values[row][col];
  }

  public Vector1by2 column(int col) {
    return new Vector1by2(get(0, col), get(1, col));
  }
}
