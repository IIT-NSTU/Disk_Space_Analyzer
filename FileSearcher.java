import java.io.File;

public class FileSearcher {
    public static void main(String[] args) {
        String searchPath = "D:\\";
        String searchName = "pattern.exe";

        File file = new File(searchPath);

        if (file.exists()) {
            String foundPath = searchFile(file, searchName);
            if (foundPath != null) {
                System.out.println("File/Folder Found: " + foundPath);
            } else {
                System.out.println("The file/folder is not found.");
            }
        } else {
            System.out.println("Invalid search path.");
        }
    }

    private static String searchFile(File directory, String searchName) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String foundPath = searchFile(file, searchName);
                    if (foundPath != null) {
                        return foundPath;
                    }
                } else if (file.getName().equalsIgnoreCase(searchName)) {
                    return file.getAbsolutePath();
                }
            }
        }

        return null;
    }
}
