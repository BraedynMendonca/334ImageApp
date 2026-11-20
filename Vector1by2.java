/** A mutable two-value row vector used for image coordinates. */
public class Vector1by2 {
  private double first;
  private double second;

  /** Creates the vector (first, second). */
  public Vector1by2(double first, double second) { this.first = first; this.second = second; }
  /** Returns the first component. */
  public double getFirst() { return first; }
  /** Returns the second component. */
  public double getSecond() { return second; }
  /** Changes the first component. */
  public void setFirst(double value) { first = value; }
  /** Changes the second component. */
  public void setSecond(double value) { second = value; }
  /** Adds another vector to this vector and returns this vector. */
  /** Returns the sum of this vector and another vector. */
  public Vector1by2 add(Vector1by2 other) { return new Vector1by2(first + other.first, second + other.second); }
  /** Returns the dot product with another vector. */
  public double dot(Vector1by2 other) { return first * other.first + second * other.second; }
  /** Returns the dot product of two vectors. */
  public static double dot(Vector1by2 a, Vector1by2 b) { return a.dot(b); }
  /** Multiplies a row vector by a matrix. */
  public static Vector1by2 multiply(Vector1by2 vector, Matrix2by2 matrix) { return matrix.multiply(vector); }
}
