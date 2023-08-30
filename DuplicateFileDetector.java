import java.io.*;
import java.security.*;
import java.util.*;

public class DuplicateFileDetector {

    public static Map<String, List<String>> hashToMap = new HashMap<>();
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter path: ");
        String directoryPath = scanner.nextLine();
        detectDuplicates(new File(directoryPath));
        printDuplicateFile();
    }

    public static void detectDuplicates(File item) {
        if (item.isDirectory()) {
            File[] files = item.listFiles();
            if (files != null) {
                for (File file : files) {
                    detectDuplicates(file);
                }
            }
        } else {
            String hash = calculateFileHash(item);
            if (hash != null) {
                hashToMap.computeIfAbsent(hash, k -> new ArrayList<>()).add(item.getAbsolutePath());
            }
        }
    }

    public static String calculateFileHash(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            byte[] hashBytes = digest.digest();
            return bytesToHex(hashBytes);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void printDuplicateFile() {
        for (List<String> paths : hashToMap.values()) {
            if (paths.size() > 1) {
                System.out.println("Duplicate files:");
                for (String path : paths) {
                    System.out.println(path);
                }
                System.out.println();
            }
        }
    }
}
