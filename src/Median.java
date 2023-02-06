import java.util.Arrays;

public class Median {
    public static double median(double[] arr) {
        Arrays.sort(arr);
        double median;
        if (arr.length % 2 == 0) median = ((double) arr[arr.length/2] + (double)arr[arr.length/2 - 1])/2;
        else median = (double) arr[arr.length/2];
        return median;
    }
}