import java.io.File;
import java.util.Scanner;

public class DiskScanner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter path: ");
        String path = scanner.nextLine();

        File file = new File(path);

        if (file.exists()) {
            scanDirectory(file);
        } else {
            System.out.println("Invalid path.");
        }
    }

    private static void scanDirectory(File directory) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else {

                    System.out.println(file.getAbsolutePath());
                }
            }
        }
    }
}





