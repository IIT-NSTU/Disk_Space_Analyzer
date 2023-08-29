import java.io.File;
import java.util.Scanner;

public class FileSorter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter path: ");
        String path = scanner.nextLine();
        sortFilesBySize(path);
    }

    public static void sortFilesBySize(String path) {
        File file = new File(path);

        if (!file.exists() || !file.isDirectory()) {
            System.out.println("Invalid directory path.");
            return;
        }

        File[] files = file.listFiles();
        if (files == null || files.length <= 1) {

            return;
        }

        mergeSort(files, 0, files.length - 1);


        for (File sortedFile : files) {
            System.out.println(sortedFile.getName() + " (" + sortedFile.length() + " bytes)");
        }
    }

    private static void mergeSort(File[] files, int start, int end) {
        if (start < end) {
            int mid = (start + end) / 2;
            mergeSort(files, start, mid);
            mergeSort(files, mid + 1, end);
            merge(files, start, mid, end);
        }
    }

    private static void merge(File[] files, int start, int mid, int end) {
        int leftSize = mid - start + 1;
        int rightSize = end - mid;

        // Create temporary arrays to hold the left and right subarrays
        File[] leftArray = new File[leftSize];
        File[] rightArray = new File[rightSize];

        // Copy data from the original array to the temporary arrays
        System.arraycopy(files, start, leftArray, 0, leftSize);
        System.arraycopy(files, mid + 1, rightArray, 0, rightSize);

        int leftIndex = 0;
        int rightIndex = 0;
        int mergedIndex = start;


        while (leftIndex < leftSize && rightIndex < rightSize) {

            if (leftArray[leftIndex].length() <= rightArray[rightIndex].length()) {
                files[mergedIndex] = leftArray[leftIndex];
                leftIndex++;
            } else {
                files[mergedIndex] = rightArray[rightIndex];
                rightIndex++;
            }
            mergedIndex++;
        }


        while (leftIndex < leftSize) {
            files[mergedIndex] = leftArray[leftIndex];
            leftIndex++;
            mergedIndex++;
        }


        while (rightIndex < rightSize) {
            files[mergedIndex] = rightArray[rightIndex];
            rightIndex++;
            mergedIndex++;
        }
    }
}
