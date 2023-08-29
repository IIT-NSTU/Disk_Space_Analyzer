import java.util.*;

class FileNode {
    String name;
    long size;
    List<FileNode> children;

    public FileNode(String name, long size) {
        this.name = name;
        this.size = size;
        this.children = new ArrayList<>();
    }
}

public class Try {
    public static void main(String[] args) {
        // Create a sample hierarchy (you can replace this with actual disk data)
        FileNode root = new FileNode("Root", 0);
        FileNode documents = new FileNode("Documents", 0);
        documents.children.add(new FileNode("file1.txt", 1000));
        documents.children.add(new FileNode("file2.txt", 1500));
        root.children.add(documents);

        // Display the tree map
        displayTreeMap(root, "", 100);

    }

    public static void displayTreeMap(FileNode node, String indent, int maxWidth) {
        System.out.println(indent + node.name + " [" + node.size + " KB]");

        if (node.children.isEmpty()) {
            return;
        }

        long totalSize = node.size;
        for (FileNode child : node.children) {
            totalSize += child.size;
        }

        for (FileNode child : node.children) {
            int childWidth = (int) (maxWidth * child.size / totalSize);
            displayTreeMap(child, indent + "  ", childWidth);
        }
    }
}

