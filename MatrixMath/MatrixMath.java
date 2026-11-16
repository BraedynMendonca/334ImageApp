/** Multiplies rectangular integer matrices using row-by-column products. */
public class MatrixMath {
  public static int[][] multiply(int[][] a, int[][] b) {
    int aColumns = columns(a);
    int bColumns = columns(b);
    if (aColumns != b.length) {
      throw new IllegalArgumentException("The first matrix's columns must equal the second matrix's rows.");
    }
    int[][] result = new int[a.length][bColumns];
    for (int row = 0; row < a.length; row++) {
      for (int col = 0; col < bColumns; col++) {
        for (int k = 0; k < aColumns; k++) {
          result[row][col] += a[row][k] * b[k][col];
        }
      }
    }
    return result;
  }

  private static int columns(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
      throw new IllegalArgumentException("Matrices must be nonempty and rectangular.");
    }
    int width = matrix[0].length;
    for (int[] row : matrix) {
      if (row == null || row.length != width) {
        throw new IllegalArgumentException("Matrices must be nonempty and rectangular.");
      }
    }
    return width;
  }
}
