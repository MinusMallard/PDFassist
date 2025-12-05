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
    private final Path filePath = Path.of("C:\\Users\\Rishav\\PDFassist\\");
    private final Path pathsFileLocationPaths = Path.of("C:\\Users\\Rishav\\PDFassist\\paths.txt");

    private List<String> directories;
    private List<Path> files;

    public FileHandler() {
        try {
            System.out.println("created the path");
            Files.createDirectory(filePath);
        } catch (Exception e) {
            System.out.println("path already exist");
        }

        try {
            Path createdFile = Files.createFile(pathsFileLocationPaths);
        } catch (Exception e) {
            System.out.println("file already exists");
        }
    }

    public void addDirectory(String dir) throws Exception {
        StringBuilder sb = new StringBuilder(dir);
        try (BufferedWriter bw = Files.newBufferedWriter(pathsFileLocationPaths, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            bw.newLine();
            bw.write(dir, 0, dir.length());
            getAllDirectories();
            searchInPaths();
        } catch (Exception e) {
            throw e;
        }
    }

    public List<String> getAllDirectories() throws Exception {
        List<String> directories = new ArrayList<>();
        try (BufferedReader bf = Files.newBufferedReader(pathsFileLocationPaths, StandardCharsets.UTF_8)) {
            String line;
            while((line = bf.readLine()) != null) {
                if (!line.trim().equals("")) {
                    directories.add(line);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        this.directories = directories;
        return directories;
    }

    public List<Path> searchInPaths() throws Exception {
        files = new ArrayList<>();
        for (String path: directories) {
            Path curPath = Path.of(path);
            if (Files.exists(curPath) && Files.isDirectory(curPath)) {
                try(Stream<Path> walk = Files.walk(curPath, 1)) {
                    walk.filter(Files::isRegularFile)
                            .filter(p -> p.endsWith(".pdf"))
                            .forEach(p ->this.files.add(p));
                }
            }
        }
        return files;
    }
}
