import java.io.*;
import java.util.Scanner;

public class FileBackup {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter source directory path: ");
        String sourceFolderPath = scanner.nextLine();

        File sourceFolder = new File(sourceFolderPath);
        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            System.out.println("Invalid source directory path.");
            return;
        }

        System.out.print("Enter backup directory path: ");
        String backupFolderPath = scanner.nextLine();

        if (!createBackup(sourceFolder, backupFolderPath)) {
            System.out.println("Backup process failed.");
        } else {
            System.out.println("Backup process completed successfully.");
        }
    }

    public static boolean createBackup(File source, String backupFolderPath) {
        try {
            File[] files = source.listFiles();

            for (File file : files) {
                if (file.isFile()) {
                    System.out.print("Do you want to backup file " + file.getName() + "? (yes/no): ");
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("yes")) {
                        backupFile(file, backupFolderPath);
                    }
                } else if (file.isDirectory()) {
                    System.out.print("Do you want to backup files in subfolder " + file.getName() + "? (yes/no): ");
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("yes")) {
                        createBackup(file, backupFolderPath + File.separator + file.getName());
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void backupFile(File sourceFile, String backupFolderPath) throws IOException {
        File backupFolder = new File(backupFolderPath);

        if (!backupFolder.exists()) {
            backupFolder.mkdirs();
        }

        String backupFilePath = backupFolderPath + File.separator + sourceFile.getName();

        FileInputStream fis = new FileInputStream(sourceFile);
        FileOutputStream fos = new FileOutputStream(backupFilePath);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }

        fis.close();
        fos.close();

        System.out.println("Backup created for " + sourceFile.getName());
    }
}
