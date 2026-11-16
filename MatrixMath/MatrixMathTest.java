import java.util.Arrays;

/** Black-box checks: compare public method results with independently calculated answers. */
public class MatrixMathTest {
  private static int passed = 0;

  private static void expect(String name, int[][] a, int[][] b, int[][] expected) {
    int[][] actual = MatrixMath.multiply(a, b);
    if (!Arrays.deepEquals(expected, actual)) {
      throw new AssertionError(name + ": expected " + Arrays.deepToString(expected)
          + ", got " + Arrays.deepToString(actual));
    }
    passed++;
  }

  private static void expectError(String name, int[][] a, int[][] b) {
    try {
      MatrixMath.multiply(a, b);
      throw new AssertionError(name + ": expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
      passed++;
    }
  }

  public static void main(String[] args) {
    expect("square", new int[][] {{1,2},{3,4}}, new int[][] {{5,6},{7,8}}, new int[][] {{19,22},{43,50}});
    expect("rectangular", new int[][] {{1,2,3},{4,5,6}}, new int[][] {{7,8},{9,10},{11,12}}, new int[][] {{58,64},{139,154}});
    expect("negative and zero", new int[][] {{-1,0,2}}, new int[][] {{3},{4},{-5}}, new int[][] {{-13}});
    expect("identity", new int[][] {{2,-3},{0,4}}, new int[][] {{1,0},{0,1}}, new int[][] {{2,-3},{0,4}});
    expect("single element", new int[][] {{-3}}, new int[][] {{4}}, new int[][] {{-12}});
    expectError("incompatible dimensions", new int[][] {{1,2}}, new int[][] {{1,2}});
    expectError("ragged matrix", new int[][] {{1,2},{3}}, new int[][] {{1},{2}});
    expectError("empty matrix", new int[][] {}, new int[][] {{1}});
    System.out.println(passed + " matrix tests passed.");
  }
}
