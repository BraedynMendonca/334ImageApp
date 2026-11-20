/** A mutable 2 by 2 matrix for row-vector transformations. */
public class Matrix2by2 {
  private final double[][] values;

  /** Creates a matrix from its entries in row order. */
  public Matrix2by2(double a, double b, double c, double d) {
    values = new double[][] {{a, b}, {c, d}};
  }
  /** Returns the value at a zero-based row and column. */
  public double get(int row, int col) { check(row, col); return values[row][col]; }
  /** Sets the value at a zero-based row and column. */
  public void set(int row, int col, double value) { check(row, col); values[row][col] = value; }
  /** Returns a column as a vector. */
  public Vector1by2 column(int col) { return new Vector1by2(get(0, col), get(1, col)); }
  /** Sets this matrix to a clockwise image-coordinate rotation in degrees. */
  public void setRotationMatrix(int degrees) {
    int angle = ((degrees % 360) + 360) % 360;
    if (angle != 0 && angle != 90 && angle != 180 && angle != 270)
      throw new IllegalArgumentException("Rotation must be a multiple of 90 degrees.");
    int[][] entries = angle == 0 ? new int[][] {{1, 0}, {0, 1}}
        : angle == 90 ? new int[][] {{0, -1}, {1, 0}}
        : angle == 180 ? new int[][] {{-1, 0}, {0, -1}}
        : new int[][] {{0, 1}, {-1, 0}};
    for (int r = 0; r < 2; r++) for (int c = 0; c < 2; c++) values[r][c] = entries[r][c];
  }
  /** Adds another matrix to this matrix. */
  public void add(Matrix2by2 other) {
    for (int r = 0; r < 2; r++) for (int c = 0; c < 2; c++) values[r][c] += other.get(r, c);
  }
  /** Multiplies a row vector by this matrix using two dot products. */
  public Vector1by2 multiply(Vector1by2 vector) {
    return new Vector1by2(vector.dot(column(0)), vector.dot(column(1)));
  }
  /** Rejects coordinates outside the two-by-two array. */
  private static void check(int row, int col) {
    if (row < 0 || row > 1 || col < 0 || col > 1) throw new IndexOutOfBoundsException("Matrix index outside 0..1");
  }
}
