public class insertion_sort {
    public static int countA = 0;
    public static int countB = 0;

    public static void main(String[] args)
    {
        int array[] = {12, 11, 13, 5, 6};
        System.out.println(array.length);
        insertionSort(array);
        insertionSortRecursive(array, array.length);

        System.out.println("Count A is: " + countA);
        System.out.println("Count B is: " + countB);
    }

    static void insertionSort(int arr[]) {
        for( int i = 0 ;i < arr.length ; i++ ) {
            countA++;
            int temp = arr[ i ];
            int j = i;

            while(  j > 0  && temp < arr[ j -1]) {
                countA+=2;
                arr[ j ] = arr[ j-1];
                j= j - 1;

            }
            // moving current element to its  correct position.
            arr[ j ] = temp;
        }
    }

    static void insertionSortRecursive(int arr[], int n)
    {
        // Base case
        countB++;
        if (n <= 1) {
            return;
        }

        // Sort first n-1 elements
        insertionSortRecursive(arr, n-1);

        // Insert last element at its correct position in sorted array.
        int last = arr[n-1];
        int j = n-2;

        //Move elements of arr[0..i-1], that are greater than key, to one position ahead of their current position
        while (j >= 0 && arr[j] > last) {
            countB+=2;
            arr[j+1] = arr[j];
            j--;
        }
        arr[j+1] = last;
    }
}
