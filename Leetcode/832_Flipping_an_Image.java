class Solution {
    public int[][] flipAndInvertImage(int[][] A) {
        for (int i = 0; i < A.length; i ++) {
            for (int j = 0; j < A[0].length / 2; j ++) {
                int tmp = A[i][j];
                A[i][j] = A[i][A[0].length - j - 1];
                A[i][A[0].length - j - 1] = tmp;
            }
            for (int j = 0; j < A[0].length; j ++)
                A[i][j] = 1 - A[i][j];
        }
        return A;
    }
}