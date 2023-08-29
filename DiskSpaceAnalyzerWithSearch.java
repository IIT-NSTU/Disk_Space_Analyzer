import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class DiskSpaceAnalyzerWithSearch{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter path: ");
        String path = scanner.nextLine();
        System.out.println();

        File file = new File(path);

        if (file.exists()) {
            getFileInformation(file);
            System.out.println();
            visualizeDiskInfo(file, 0);
        } else {
            System.out.println("Invalid path.");
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
            });

            for (File file : files) {
                if (file.isDirectory()) {
                    printFileIndention(file.getName(), formatFileSize(calculateFileSize(file)), level);
                    visualizeDiskInfo(file, level + 1);
                } else {
                    printFileIndention(file.getName(), formatFileSize(file.length()), level);
                }
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Search for: ");
            String searchKeyword = scanner.nextLine().toLowerCase();
            searchFiles(directory, searchKeyword);
        }
    }

    private static void printFileIndention(String fileName, String fileSize, int level) {
        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indentation.append("   ");
        }
        System.out.println(indentation + "└── " + fileName + " (" + fileSize + ")");
    }

    private static void searchFiles(File directory, String keyword) {
        File[] files = directory.listFiles();

        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));

            System.out.println("Search results for keyword '" + keyword + "':");
            for (File file : files) {
                if (file.getName().toLowerCase().contains(keyword)) {
                    System.out.println("   " + file.getName());
                }
            }
        }
    }
}
