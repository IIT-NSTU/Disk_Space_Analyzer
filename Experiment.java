import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public class Experiment {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter path: ");
        String path = scanner.nextLine();

        File file = new File(path);

        if (file.exists()) {
            searchFile(file, "file.txt");
            System.out.println();
            getFileInformation(file);
            System.out.println();
            visualizeDiskUsage(file, 0);
            System.out.println();
            System.out.println("Sorted files by size:");
            sortFilesBySize(file);
        } else {
            System.out.println("Invalid path.");
        }
    }
    private static void searchFile(File directory, String fileName) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchFile(file, fileName);
                } else if (file.getName().equals(fileName)) {
                    System.out.println("File found: " + file.getAbsolutePath());
                    return;
                  }
            }
           }
         }
    private static void getFileInformation(File file) {
        if (file.exists()) {
            System.out.println("File Name: " + file.getName());
            System.out.println("Total File Size: " + formatFileSize(calculateTotalFileSize(file)));
            System.out.println("File Type: " + getFileExtension(file));
            System.out.println("Last Modified: " + getLastModifiedDateString(file));
        } else {
            System.out.println("File does not exist.");
        }
    }

    private static long calculateTotalFileSize(File file) {
        long totalSize = 0;

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    totalSize += calculateTotalFileSize(subFile);
                }
            }
        } else {
            totalSize += file.length();
        }

        return totalSize;
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "Unknown";
        }
        return fileName.substring(dotIndex + 1).toLowerCase();
    }
    private static String getLastModifiedDateString(File file) {
        long lastModifiedTimestamp = file.lastModified();
        Date lastModifiedDate = new Date(lastModifiedTimestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
        return dateFormat.format(lastModifiedDate);
    }

    private static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " bytes";
        } else if (size < 1024 * 1024) {
            double sizeInKB = (double) size / 1024;
            return new DecimalFormat("#0.00").format(sizeInKB) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            double sizeInMB = (double) size / (1024 * 1024);
            return new DecimalFormat("#0.00").format(sizeInMB) + " MB";
        } else {
            double sizeInGB = (double) size / (1024 * 1024 * 1024);
            return new DecimalFormat("#0.00").format(sizeInGB) + " GB";
        }
    }

    private static void visualizeDiskUsage(File directory, int level) {
        File[] files = directory.listFiles();

        if (files != null) {
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File file1, File file2) {
                    if (file1.isDirectory() && file2.isFile()) {
                        return -1;
                    } else if (file1.isFile() && file2.isDirectory()) {
                        return 1;
                    } else {
                        long size1 = file1.length();
                        long size2 = file2.length();
                        return Long.compare(size1, size2);
                    }
                }
            });

            for (File file : files) {
                if (file.isDirectory()) {
                    printFileWithIndentation(file.getName(), formatFileSize(calculateTotalFileSize(file)), level);
                    visualizeDiskUsage(file, level + 1);
                } else {
                    printFileWithIndentation(file.getName(), formatFileSize(file.length()), level);
                }
            }
        }
    }

    private static void printFileWithIndentation(String fileName, String fileSize, int level) {
        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indentation.append("   ");
        }
        System.out.println(indentation + "└── " + fileName + " (" + fileSize + ")");
    }

    private static void sortFilesBySize(File directory) {
        File[] files = directory.listFiles();

        if (files != null && files.length > 1) {
            mergeSort(files, 0, files.length - 1);

            for (File file : files) {
                System.out.println(file.getName() + " (" + formatFileSize(file.length()) + ")");
            }
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

        File[] leftArray = new File[leftSize];
        File[] rightArray = new File[rightSize];

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