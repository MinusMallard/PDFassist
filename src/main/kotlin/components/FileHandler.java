package components;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileHandler {
    //I know this is not clean approach, but this is the location of the file
    private final Path pathsFileLocationPaths = Path.of("C:\\Users\\Rishav\\PDFassist\\paths.txt");

    //List of all the directories that is stored by the user this is useful in case of searching pdfs
    private List<String> directories;

    //List of all the files that were found by the app
    private List<Path> files;

    public FileHandler() {
        try {
            //Path to the file which stores all the directories
            Path filePath = Path.of("C:\\Users\\Rishav\\PDFassist\\");
            Files.createDirectory(filePath);
        } catch (Exception e) {
            System.out.println("path already exist");
        }

        try {
            Files.createFile(pathsFileLocationPaths);
        } catch (Exception e) {
            System.out.println("file already exists");
        }
    }

    public void addDirectory(String dir) {
        try (BufferedWriter bw = Files.newBufferedWriter(pathsFileLocationPaths, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            bw.newLine();
            bw.write(dir, 0, dir.length());
            getAllDirectories();
            searchInPaths();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Reads the line by line
    public List<String> getAllDirectories() throws Exception {
        List<String> directories = new ArrayList<>();
        try (BufferedReader bf = Files.newBufferedReader(pathsFileLocationPaths, StandardCharsets.UTF_8)) {
            String line;
            while((line = bf.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    directories.add(line);
                    System.out.println("directory: " + line);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        this.directories = directories;
        return directories;
    }

    public List<Path> searchInPaths() throws Exception {
        this.files = new ArrayList<>();
        for (String path: directories) {
            Path curPath = Path.of(path);
            if (Files.exists(curPath) && Files.isDirectory(curPath)) {
                System.out.println("this is directory");
                try(Stream<Path> walk = Files.walk(curPath, 1)) {
                    System.out.println("got inside the stream");
                    walk.filter(Files::isRegularFile)
                            .filter(p -> p.toString().endsWith(".pdf"))
                            .forEach(p ->this.files.add(p));
                }
            }
        }

        System.out.println(files);
        return files;
    }
}
