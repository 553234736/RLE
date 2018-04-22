import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        File file = new File(args[1]);
        byte[] example;
        if (args[0].equals("-c")) {
            try {
                example = FilesManager.readFile(file);
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден");
                return;
            }
            byte[] compressed = rle.compressed(example);
            String fileName = file.getName().substring(0, file.getName().lastIndexOf('.')) + "-ZIP.txt";
            FilesManager.writeFile(fileName, compressed);
        }
        if (args[0].equals("-d")) {
            try {
                example = FilesManager.readFile(file);
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден");
                return;
            }
            byte[] decompressed = rle.deCompressed(example);
            String fileName = file.getName().substring(0, file.getName().lastIndexOf('-')) + "-unZIP.txt";
            FilesManager.writeFile(fileName, decompressed);
        }
    }
}