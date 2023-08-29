import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class TRY1 {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.print("Enter path: ");
            String path = scanner.nextLine();
            System.out.println();

            File file = new File(path);

            if (file.exists()) {
                getFileInformation(file);
                System.out.println();
                visualizeDiskInfo(file, 0);

                boolean searching = true;
                while (searching) {
                    System.out.print("Do you want to search for files (yes/no)? ");
                    String searchChoice = scanner.nextLine();

                    if (searchChoice.equalsIgnoreCase("yes")) {
                        System.out.print("Enter search keyword: ");
                        String keyword = scanner.nextLine();
                        searchFiles(file, keyword);
                    } else if (searchChoice.equalsIgnoreCase("no")) {
                        searching = false;
                    }
                }
            } else {
                System.out.println("Invalid path.");
            }
            if (running) {
                System.out.print("Do you want to run the program again (yes/no)? ");
                String againChoice = scanner.nextLine();
                if (!againChoice.equalsIgnoreCase("yes")) {
                    running = false;
                }
            }


        }
    }
    private static void getFileInformation(File file) {
        if (file.exists()) {
            System.out.println("File Name: " + file.getName());
            System.out.println("Total File Size: " + formatFileSize(calculateFileSize(file)));
        } else {
            System.out.println("File does not exist.");
        }
    }

    private static long calculateFileSize(File file) {
        long totalSize = 0;

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    totalSize += calculateFileSize(subFile);
                }
            }
        } else {
            totalSize += file.length();
        }

        return totalSize;
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

    private static void visualizeDiskInfo(File directory, int level) {
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
                    }
            )
            ;

            for (File file : files) {
                if (file.isDirectory()) {
                    printFileIndention(file.getName(), formatFileSize(calculateFileSize(file)), level);
                    visualizeDiskInfo(file, level + 1);
                } else {
                    printFileIndention(file.getName(), formatFileSize(file.length()), level);
                }
            }
        }
    }

    private static void printFileIndention(String fileName, String fileSize, int level) {
        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indentation.append("   ");
        }
        System.out.println(indentation + "└── " + fileName + " (" + fileSize + ")");
    }

    private static void sortFiles(File directory) {
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
        int mergeIndex = start;

        while (leftIndex < leftSize && rightIndex < rightSize) {
            if (leftArray[leftIndex].length() <= rightArray[rightIndex].length()) {
                files[mergeIndex] = leftArray[leftIndex];
                leftIndex++;
            } else {
                files[mergeIndex] = rightArray[rightIndex];
                rightIndex++;
            }
            mergeIndex++;
        }

        while (leftIndex < leftSize) {
            files[mergeIndex] = leftArray[leftIndex];
            leftIndex++;
            mergeIndex++;
        }

        while (rightIndex < rightSize) {
            files[mergeIndex] = rightArray[rightIndex];
            rightIndex++;
            mergeIndex++;
        }
    }



    private static void searchFiles(File directory, String keyword) {
        searchFilesRecursive(directory, keyword, true);
    }

    private static boolean searchFilesRecursive(File directory, String keyword, boolean performSearch) {
        if (!performSearch) {
            return false; // Exit early if user chooses not to search
        }

        File[] files = directory.listFiles();

        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));

            boolean foundMatch = false;

            for (File file : files) {
                if (file.getName().toLowerCase().contains(keyword)) {
                    System.out.println("   " + file.getPath());
                    foundMatch = true;
                }

                if (file.isDirectory()) {
                    boolean directoryFoundMatch = searchFilesRecursive(file, keyword, true);
                    foundMatch = foundMatch || directoryFoundMatch;
                }
            }

            return foundMatch;
        }

        return false;
    }
}
