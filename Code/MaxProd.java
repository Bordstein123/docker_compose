/** This algorithm is a conclusion for the "Maximum Subarray Problem".

 * @author Nikola Bauer, Carolin Niederhofer, Melisa Katilmis, Maximilian Knobloch, Berkan Nur
 * @version 03.04.2022
 * Estimated runtime: O(n)
 */

class MaxProd {
  static class NotNullException extends Exception{
    public NotNullException(String s) {
      super(s);
    }
  }

  public static void main(String[] args) throws NotNullException {
    // The Strings in 'args' will be changed into doubles in 'Array'
    double[] array = new double[args.length];
    double maxProd = 1;
    double minProd = 1;
    double result = 0;
    if (array.length == 0) {
      throw new NotNullException("No Number inserted, please insert at least one number");
    } else {
      for (int i = 0; i < args.length; i++) {
        array[i] = Double.parseDouble(args[i]);
      }
    }

    // The largest product of consecutive numbers is determined and also the EndIndex is saved.
    int endIndex = 0;
    int startIndex = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] < 0) {
        double a = minProd;
        minProd = maxProd;
        maxProd = a;
      }
      maxProd = Math.max(array[i], maxProd * array[i]);
      minProd = Math.min(array[i], minProd * array[i]);
      if (maxProd >= result) {
        result = maxProd;
        endIndex = i;
      }
    }

    // With the Result and EndIndex known, the Algorithm can multiplicate backwards until
    // the "multiBackResult" is equal the Result we know.
    // At the Index, where both Results are equal, the Index is the "startIndex".
    double multiBackResult = 1;
    for (int i = endIndex; i >= 0; i--) {
      multiBackResult *= array[i];
      if (multiBackResult == result) {
        startIndex = i;
        break;
      }
    }

    System.out.println("The maximum product is: " + result);
    System.out.println("The highest Result was found between Index "
                + startIndex + " and " + endIndex);
  }
}