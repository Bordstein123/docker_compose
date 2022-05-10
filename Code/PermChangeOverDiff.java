import java.util.Arrays;

/**
 * PermChangeOverDiff liest als Argument eine natürliche Zahl n ein und listet
 * dann alle Permutationen π der Zahlen {1, 2, . . . , n} in aufsteigender Reihenfolge
 * auf, bei denen die absoluten Diﬀerenzen zweier direkt aufeinanderfolgender Zahlen abwechselnd
 * kleiner und größer werden (zuerst kleiner)
 * @author Berkan Nur, Melisa Katilmis, Nikola Bauer, Maximilian Knobloch, Carolin Niederhofer
 */

public class PermChangeOverDiff {
    private final int[] workArray;
    private int permutationCounter = 0;
    public PermChangeOverDiff(int size) {
        workArray = new int[size];
        for (int i = 0; i <= workArray.length - 1; i++) {
            workArray[i] = i + 1;
        }
    }

    public void permute() {
        permute(0, workArray.length - 1);
    }

    private void permute(int startIndex, int limit) {
        if (startIndex < limit) {
            for (int i = startIndex; i <= limit; i++) {
                swap(startIndex, i);
                permute(startIndex + 1, limit);
            }

            shift(startIndex, limit);
            return;
        }

        boolean willPrint = true;
        int oldDiff = Integer.MIN_VALUE;
        boolean ascend = true;
        for (int i = 0; i < limit; ++i) {
            int lhs = workArray[i];
            int rhs = workArray[i + 1];
            int currDiff = Math.abs(lhs - rhs);
            boolean triggered = false;
            if (ascend) {
                if (currDiff > oldDiff) {
                    triggered = true;
                }
            } else {
                if (currDiff < oldDiff) {
                    triggered = true;
                }
            }
            if (triggered) {
                oldDiff = currDiff;
                ascend = !ascend;
                continue;
            }

            willPrint = false;
            break;
        }

        if (!willPrint) {
            return;
        }
        permutationCounter++;
        System.out.println(Arrays.toString(workArray));
    }

    public int getPermutationCounter() {
        return permutationCounter;
    }
    // XOR SWAP (Bitwise)
    private void swap(int i, int j) {
        if (i == j) {
            return;
        }
        workArray[i] ^= workArray[j];
        workArray[j] ^= workArray[i];
        workArray[i] ^= workArray[j];
    }

    // Shifting to left side
    private void shift(int startIndex, int limit) {
        int temp = workArray[startIndex];
        System.arraycopy(workArray, startIndex + 1, workArray, startIndex, limit - startIndex);
        workArray[limit] = temp;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        if (n < 1) {
            System.out.println("Input muss grösser als 0 sein.");
            return;
        }
        int[] startPerm = new int[n];
        for (int i = 0; i < n; i++) {
            startPerm[i] = i + 1;
        }
        PermChangeOverDiff p = new PermChangeOverDiff(n);
        p.permute();
        int totalPermutations = p.getPermutationCounter();
        String resultStr = "Es gab genau " + totalPermutations + " Permutationen in " + Arrays.toString(startPerm)
                + " die der Bedingung gehorchten.";
        resultStr = resultStr.replace("[", "{");
        resultStr = resultStr.replace("]", "}");
        System.out.println(resultStr);
    }
}
