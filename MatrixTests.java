/** Black-box checks of the public vector and matrix interfaces. */
public class MatrixTests {
  private static int tests;
  private static void equal(double expected, double actual) {
    if (Math.abs(expected - actual) > 1e-9) throw new AssertionError("Expected " + expected + " but got " + actual);
    tests++;
  }
  private static void vector(double first, double second, Vector1by2 actual) {
    equal(first, actual.getFirst());
    equal(second, actual.getSecond());
  }
  public static void main(String[] args) {
    Vector1by2 a = new Vector1by2(2, -3);
    vector(2, -3, a);
    equal(-13, Vector1by2.dot(a, new Vector1by2(1, 5)));
    equal(0, Vector1by2.dot(new Vector1by2(0, 0), a));
    Matrix2by2 m = new Matrix2by2(1, 2, 3, 4);
    equal(1, m.get(0, 0)); equal(2, m.get(0, 1));
    equal(3, m.get(1, 0)); equal(4, m.get(1, 1));
    vector(2, 4, m.column(1));
    vector(-7, -8, Vector1by2.multiply(a, m));
    vector(2, -3, Vector1by2.multiply(a, new Matrix2by2(1, 0, 0, 1)));
    vector(-3, -2, Vector1by2.multiply(a, new Matrix2by2(0, -1, 1, 0)));
    vector(-2, 3, Vector1by2.multiply(a, new Matrix2by2(-1, 0, 0, -1)));
    vector(3, 2, Vector1by2.multiply(a, new Matrix2by2(0, 1, -1, 0)));
    try { m.get(2, 0); throw new AssertionError("Expected index error"); }
    catch (IndexOutOfBoundsException expected) { tests++; }
    System.out.println(tests + " vector and matrix checks passed.");
  }
}
