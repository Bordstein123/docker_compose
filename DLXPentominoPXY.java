public class DLXPentominoPXY {
    // the entry to the linked list matrix
    private static final DLXNode h = new DLXNode();
    // array for the colum headers for easy acces
    private static DLXNode[] colHeader;
    // solutions
    private static int cnt = 0;

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int[][] a = new int[5][n];
        colHeader = new DLXNode[n * 5 + 1];
        // fill the 2D-Array
        for (int row = 0, counter = 1; row < 5; row++) {
            for (int col = 0; col < n; col++) {
                a[row][col] = counter;
                counter++;
            }
        }
        // fill the colum header and connect the linked list
        linkColHeader(colHeader);
        // create the double linked list matrix with all possible solutions
        for (int pos = 1; pos <= 5 * n; pos++) {
            int row = calcRow(pos, n);
            int col = calcCol(pos, n);
            // P-blocks
            linkSolution(P1(a, row, col), colHeader);
            linkSolution(P2(a, row, col), colHeader);
            linkSolution(P3(a, row, col), colHeader);
            linkSolution(P4(a, row, col), colHeader);
            linkSolution(P5(a, row, col), colHeader);
            linkSolution(P6(a, row, col), colHeader);
            linkSolution(P7(a, row, col), colHeader);
            linkSolution(P8(a, row, col), colHeader);
            // X-block
            linkSolution(X(a, row, col), colHeader);
            // Y-block
            linkSolution(Y1(a, row, col), colHeader);
            linkSolution(Y2(a, row, col), colHeader);
            linkSolution(Y3(a, row, col), colHeader);
            linkSolution(Y4(a, row, col), colHeader);
            linkSolution(Y5(a, row, col), colHeader);
            linkSolution(Y6(a, row, col), colHeader);
            linkSolution(Y7(a, row, col), colHeader);
            linkSolution(Y8(a, row, col), colHeader);
        }
        search(0);
        System.out.println("Mit der Zahl n = " + n + " wurden "
                + cnt + " eindeutige Loesungen gefunden.");
    }

    private static void linkSolution(int[] solution, DLXNode[] colHeader) {
        if (solution == null) return;
        // link the matrix with one`s vertically
        for (int i = 1; i < solution.length; i++) {
            if (solution[i] == 1) {
                DLXNode newNode = new DLXNode();
                newNode.C = newNode.D = colHeader[i];
                newNode.U = colHeader[i].U;
                colHeader[i].U.D = newNode;
                colHeader[i].U = newNode;
            }
        }
        // link the matrix horizontal
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
        // double link the first and last node
        start.L = end;
        end.R = start;
    }

    private static void linkColHeader(DLXNode[] nodeArr) {
        nodeArr[0] = h;
        // fill the array with nodes
        for (int i = 1; i < nodeArr.length; i++) nodeArr[i] = new DLXNode();
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

    private static int calcRow(int pos, int n) {
        int row = 0;
        for (int i = 0; i <= 5; i++) {
            if (i * n < pos) row = i;
        }
        return row;
    }

    private static int calcCol(int pos, int n) {
        if ((pos % n) == 0) {
            return ((pos - 1) % n);
        } else {
            return (pos % n) - 1;
        }
    }

    /*
     * | |
     * |_|
     * |
     */
    private static int[] P1(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 2][col]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     * | |
     * |_|
     *   |
     */
    private static int[] P2(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 2][col + 1]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     * |
     * |_|
     * |_|
     */
    private static int[] P3(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 2][col]] = 1;
            solution[a[row + 2][col + 1]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *   |
     * |_|
     * |_|
     */
    private static int[] P4(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 2][col]] = 1;
            solution[a[row + 2][col + 1]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *  ____
     *   |__|
     */
    private static int[] P5(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 1]] = 1;
            solution[a[row][col + 2]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 1][col + 2]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *   ____
     *  |__|
     */
    private static int[] P6(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 1]] = 1;
            solution[a[row][col + 2]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *   |**|
     *  _|**|
     */
    private static int[] P7(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col + 1]] = 1;
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
     *  |**|
     *  |**|_
     */
    private static int[] P8(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col]] = 1;
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 1][col + 2]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     *   |
     * --+--
     *   |
     */
    private static int[] X(int[][] a, int row, int col) {
        int[] solution = new int[a[0].length * a.length + 1];
        try {
            solution[a[row][col + 1]] = 1;
            solution[a[row + 1][col]] = 1;
            solution[a[row + 1][col + 1]] = 1;
            solution[a[row + 1][col + 2]] = 1;
            solution[a[row + 2][col + 1]] = 1;
            return solution;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /*
     * |
     * |_|
     * |
     * |
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
     *   |
     * |_|
     *   |
     *   |
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
     * _ _ _ _
     *     |_
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
     *    |-
     * _ _ _ _
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
     * _|__
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
     * _____
     *  |
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
     *   |
     *   |
     * |_|
     *   |
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
     *   |
     *   |
     *   |_|
     *   |
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

    static class DLXNode {       // represents 1 element or header
        DLXNode C;           // reference to column-header
        DLXNode L, R, U, D;  // left, right, up, down references

        public DLXNode() {
            C = L = R = U = D = this;
        } // supports circular lists
    }
}