import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
public class DiskSpaceAnalyzer {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean running = true;

        while (running) {
            System.out.println("Select an option:");
            System.out.println("1. Visualize Files");
            System.out.println("2. Search Files");
            System.out.println("3. Show Duplicate Files");
            System.out.println("4. Backup Files");
            System.out.println("5. Quit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter path to visualize: ");
                    String visualizePath = scanner.nextLine();
                    visualizeDiskInfo(new File(visualizePath), 0);
                    break;
                case 2:
                    System.out.print("Enter path to search: ");
                    String searchPath = scanner.nextLine();
                    System.out.print("Enter search keyword: ");
                    String keyword = scanner.nextLine();
                    searchFiles(new File(searchPath), keyword, true);
                    break;
                case 3:
                    System.out.print("Enter path to detect duplicate files: ");
                    String duplicatePath = scanner.nextLine();
                    DuplicateFileDetector.detectDuplicates(new File(duplicatePath));
                    DuplicateFileDetector.printDuplicateFile();
                    break;
                case 4:
                    System.out.print("Enter source directory path: ");
                    String sourceFolderPath = scanner.nextLine();
                    System.out.print("Enter backup directory path: ");
                    String backupFolderPath = scanner.nextLine();
                    FileBackup.createBackup(new File(sourceFolderPath), backupFolderPath);{
                    if (!FileBackup.createBackup(new File(sourceFolderPath), backupFolderPath)) {
                        System.out.println("Backup process failed.");
                    } else {
                        System.out.println("Backup process completed successfully.");
                    }
                    }
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    public static void getFileInformation(File file) {
        if (file.exists()) {
            System.out.println("File Name: " + file.getName());
            System.out.println("Total File Size: " + formatFileSize(calculateFileSize(file)));
        } else {
            System.out.println("File does not exist.");
        }
    }

    public static long calculateFileSize(File file) {
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

    public static String formatFileSize(long size) {
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

    public static void visualizeDiskInfo(File directory, int level) {
        File[] files = directory.listFiles();

        if (files != null) {
            getFileInformation(directory);
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

    public static void printFileIndention(String fileName, String fileSize, int level) {
        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indentation.append("   ");
        }
        System.out.println(indentation + "└── " + fileName + " (" + fileSize + ")");
    }

    public static void sortFiles(File directory) {
        File[] files = directory.listFiles();

        if (files != null && files.length > 1) {
            mergeSort(files, 0, files.length - 1);

            for (File file : files) {
                System.out.println(file.getName() + " (" + formatFileSize(file.length()) + ")");
            }
        }
    }

    public static void mergeSort(File[] files, int start, int end) {
        if (start < end) {
            int mid = (start + end) / 2;
            mergeSort(files, start, mid);
            mergeSort(files, mid + 1, end);
            merge(files, start, mid, end);
        }
    }

    public static void merge(File[] files, int start, int mid, int end) {
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

    public static void searchFiles(File directory, String keyword, boolean performSearch) {
        if (!performSearch) {
            return;
        }

        File[] files = directory.listFiles();

        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));

            for (File file : files) {
                if (file.getName().toLowerCase().contains(keyword)) {
                    System.out.println("   " + file.getPath());
                }

                if (file.isDirectory()) {
                    searchFiles(file, keyword, true);
                }
            }
        }
    }
}
