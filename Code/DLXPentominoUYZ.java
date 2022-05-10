/**
 * DLXPentominoUYZ reads a non-negative number (n) and computes the number of possible tessellations
 * with U-, Y- and Z-Pentominos for a 5xn field
 *
 * @author Nikola Bauer, Melisa Katilmis, Berkan Nur, Maximilian Knobloch, Carolin Niederhofer
 *
 * All computed a(n): a(0) = 1, a(1) = 0, a(2) = 0, a(3) = 0, a(4) = 0, a(5) = 4, a(6) = 6,
 * a(7) = 8, a(8) = 6, a(9) = 8, a(10) = 54, a(11) = 112, a(12) = 182, a(13) = 232, a(14) = 404,
 * a(15) = 930, a(16) = 2054, a(17) = 3880, a(18) = 6304
 */

public class DLXPentominoUYZ {
    private static final DLXNode h = new DLXNode(); //the entry to the linked list matrix
    private static int cnt = 0; //count of solutions

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int[][] a = new int[5][n];
        DLXNode[] colHeader = new DLXNode[n * 5 + 1];

        if(n == 0) {
            System.out.printf("a(%d) = %d\nThat means for an 5x%d field there were %d possible tessellations found", n, cnt, n, cnt);
            return;
        }

        for (int row = 0, counter = 1; row < 5; row++) {
            for (int col = 0; col < n; col++) {
                a[row][col] = counter;
                counter++;
            }
        }

        linkColHeader(colHeader);

        for (int pos = 1; pos <= 5 * n; pos++) {
            int row = calcRow(pos, n);
            int col = calcCol(pos, n);
            //all possible U-pentominos
            linkSolution(U1(a, row, col), colHeader);
            linkSolution(U2(a, row, col), colHeader);
            linkSolution(U3(a, row, col), colHeader);
            linkSolution(U4(a, row, col), colHeader);
            //all possible Y-pentominos
            linkSolution(Y1(a, row, col), colHeader);
            linkSolution(Y2(a, row, col), colHeader);
            linkSolution(Y3(a, row, col), colHeader);
            linkSolution(Y4(a, row, col), colHeader);
            linkSolution(Y5(a, row, col), colHeader);
            linkSolution(Y6(a, row, col), colHeader);
            linkSolution(Y7(a, row, col), colHeader);
            linkSolution(Y8(a, row, col), colHeader);
            //all possible X-pentominos
            linkSolution(Z1(a, row, col), colHeader);
            linkSolution(Z2(a, row, col), colHeader);
            linkSolution(Z3(a, row, col), colHeader);
            linkSolution(Z4(a, row, col), colHeader);
        }
        search(0);
        System.out.printf("a(%d) = %d\nThat means for an 5x%d field there were %d possible tessellations found", n, cnt, n, cnt);
    }

    private static void linkSolution(int[] solution, DLXNode[] colHeader) {
        if (solution == null) return;

        for (int i = 1; i < solution.length; i++) {
            if (solution[i] == 1) {
                DLXNode newNode = new DLXNode();
                newNode.C = newNode.D = colHeader[i];
                newNode.U = colHeader[i].U;
                colHeader[i].U.D = newNode;
                colHeader[i].U = newNode;
            }
        }

        DLXNode start = null;
        DLXNode end = null;
        DLXNode temp = null;
        for (int i = 1; i < solution.length; i++) {
            if (solution[i] == 1 && temp == null) {
                temp = colHeader[i].U;
                start = colHeader[i].U;
            } else if (solution[i] == 1 && temp != null) {
                DLXNode a = colHeader[i].U;
                temp.R = a;
                a.L = temp;
                temp = a;
                end = colHeader[i].U;
            }
        }

        assert start != null;
        start.L = end;
        assert end != null;
        end.R = start;
    }

    private static void linkColHeader(DLXPentominoUYZ.DLXNode[] nodeArr) {
        nodeArr[0] = h;

        for (int i = 1; i < nodeArr.length; i++) nodeArr[i] = new DLXPentominoUYZ.DLXNode();
        for (int i = 1; i < nodeArr.length; i++) {
            nodeArr[i].L = nodeArr[i - 1];
            if (nodeArr.length - 1 != i) {
                nodeArr[i].R = nodeArr[i + 1];
            } else {
                nodeArr[i].R = h;
            }
        }
        if (nodeArr.length > 1) {
            h.R = nodeArr[1];
            h.L = nodeArr[nodeArr.length - 1];
        }
    }

    private static int calcRow(int pos, int n) {
        int row = 0;
        for (int i = 0; i <= 5; i++) {
            if (i * n < pos) row = i;
        }
        return row;
    }

    private static int calcCol(int pos, int n) {
        if ((pos % n) == 0) return ((pos - 1) % n);
        else return (pos % n) - 1;
    }

    //all pentominos can be found here: https://en.wikipedia.org/wiki/Pentomino

    /*
     * . .
     * ...
     */
    private static int[] U1(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 2]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 1][col + 2]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *  ..
     *  .
     *  ..
     */
    private static int[] U2(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 2][col]] = 1;
            solution[a[row + 2][col + 1]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     * ...
     * . .
     */
    private static int[] U3(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 1]] = 1;
            solution[a[row][col + 2]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 2]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *  ..
     *   .
     *  ..
     */
    private static int[] U4(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 2][col]] = 1;
            solution[a[row + 2][col + 1]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     * .
     * ...
     * .
     * .
     */
    private static int[] Y1(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 2][col]] = 1;
            solution[a[row + 3][col]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *   .
     *  ..
     *   .
     *   .
     */
    private static int[] Y2(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 2][col + 1]] = 1;
            solution[a[row + 3][col + 1]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     * ....
     *   .
     */
    private static int[] Y3(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 1]] = 1;
            solution[a[row][col + 2]] = 1;
            solution[a[row + 1][col + 2]] = 1;
            solution[a[row][col + 3]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *   .
     * ....
     */
    private static int[] Y4(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row][col + 2]] = 1;
            solution[a[row + 1][col + 2]] = 1;
            solution[a[row + 1][col + 3]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *   .
     * ....
     */
    private static int[] Y5(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 1][col + 2]] = 1;
            solution[a[row + 1][col + 3]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     * ....
     *  .
     */
    private static int[] Y6(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row][col + 2]] = 1;
            solution[a[row][col + 3]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *   .
     *   .
     *  ..
     *   .
     */
    private static int[] Y7(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 2][col]] = 1;
            solution[a[row + 2][col + 1]] = 1;
            solution[a[row + 3][col + 1]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *   .
     *   .
     *   ..
     *   .
     */
    private static int[] Y8(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 2][col]] = 1;
            solution[a[row + 2][col + 1]] = 1;
            solution[a[row + 3][col]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     * ..
     *  .
     *  ..
     */
    private static int[] Z1(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 2][col + 1]] = 1;
            solution[a[row + 2][col + 2]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *    .
     *  ...
     *  .
     *
     */
    private static int[] Z2(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col + 2]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 1][col + 2]] = 1;
            solution[a[row + 2][col]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *   ..
     *   .
     *  ..
     */
    private static int[] Z3(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col + 1]] = 1;
            solution[a[row][col + 2]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 2][col]] = 1;
            solution[a[row + 2][col + 1]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *  .
     *  ...
     *    .
     *
     */
    private static int[] Z4(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 1][col + 2]] = 1;
            solution[a[row + 2][col + 2]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    //Code from dlx_codings.java
    /**
     * Class DLXNode
     *   represents a matrix element of the cover matrix with value 1
     *   links go to up down left right neigbors, and column header
     *   can also be used as colm header or root of column headers
     *   matrix is sparsely coded
     *   try to do all operations very efficiently
     *   see:
     *      http://en.wikipedia.org/wiki/Dancing_Links
     *      http://arxiv.org/abs/cs/0011047
     */
    static class DLXNode{       // represents 1 element or header
        DLXNode C;           // reference to column-header
        DLXNode L, R, U, D;  // left, right, up, down references
        DLXNode(){ C=L=R=U=D=this; } // supports circular lists
    }


    /**
     * search tries to find and count all complete coverings of the DLX matrix.
     * Is a recursive, depth-first, backtracking algorithm that finds
     * all solutions to the exact cover problem encoded in the DLX matrix.
     * each time all columns are covered, static long cnt is increased
     *
     * @param k: number of level
     */
    public static void search(int k) { // finds & counts solutions
        if (h.R == h) {
            cnt++;
            return;
        }     // if empty: count & done
        DLXNode c = h.R;                   // choose next column c
        cover(c);                          // remove c from columns
        for (DLXNode r = c.D; r != c; r = r.D) {  // forall rows with 1 in c
            for (DLXNode j = r.R; j != r; j = j.R) // forall 1-elements in row
                cover(j.C);                    // remove column
            search(k + 1);                    // recursion
            for (DLXNode j = r.L; j != r; j = j.L) // forall 1-elements in row
                uncover(j.C);                  // backtrack: un-remove
        }
        uncover(c);                        // un-remove c to columns
    }

    /**
     * cover "covers" a column c of the DLX matrix
     * column c will no longer be found in the column list
     * rows i with 1 element in column c will no longer be found
     * in other column lists than c
     * so column c and rows i are invisible after execution of cover
     *
     * @param c: header element of column that has to be covered
     */
    public static void cover(DLXNode c) { // remove column c
        c.R.L = c.L;                         // remove header
        c.L.R = c.R;                         // .. from row list
        for (DLXNode i = c.D; i != c; i = i.D)      // forall rows with 1
            for (DLXNode j = i.R; i != j; j = j.R) {   // forall elem in row
                j.D.U = j.U;                     // remove row element
                j.U.D = j.D;                     // .. from column list
            }
    }

    /**
     * uncover "uncovers" a column c of the DLX matrix
     * all operations of cover are undone
     * so column c and rows i are visible again after execution of uncover
     *
     * @param c: header element of column that has to be uncovered
     */
    public static void uncover(DLXNode c) {//undo remove col c
        for (DLXNode i = c.U; i != c; i = i.U)      // forall rows with 1
            for (DLXNode j = i.L; i != j; j = j.L) {   // forall elem in row
                j.D.U = j;                       // un-remove row elem
                j.U.D = j;                       // .. to column list
            }
        c.R.L = c;                           // un-remove header
        c.L.R = c;                           // .. to row list
    }
}