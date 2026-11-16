/** Immutable two-component row vector for image coordinates and matrix arithmetic. */
public class Vector1by2 {
  private final double first;
  private final double second;

  public Vector1by2(double first, double second) {
    this.first = first;
    this.second = second;
  }

  public double getFirst() { return first; }
  public double getSecond() { return second; }

  public static double dot(Vector1by2 a, Vector1by2 b) {
    return a.first * b.first + a.second * b.second;
  }

  /** Multiplies a row vector by a matrix using the matrix's columns. */
  public static Vector1by2 multiply(Vector1by2 vector, Matrix2by2 matrix) {
    return new Vector1by2(dot(vector, matrix.column(0)), dot(vector, matrix.column(1)));
  }
}
