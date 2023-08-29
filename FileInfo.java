import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class FileInfo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter path: ");
        String path = scanner.nextLine();

        File file = new File(path);

        if (file.exists()) {
            System.out.println("File Name: " + file.getName());
            long totalSize = calculateTotalFileSize(file);
            System.out.println("Total File Size: " + formatFileSize(totalSize));
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
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
}
