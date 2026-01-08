package components;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Tab{
    private Path path;
    private PDF pdf;
    public boolean isLoaded;
    private String tabName;

    private PDDocument document;
    private PDFRenderer renderer;
    private int pages;

    private final Object lock = new Object();

    private final int start = 0;
    private int end = Integer.MAX_VALUE;
    private List<BufferedImage> map;
    public Tab() throws IOException {
        tabName = "New Tab";
        this.map = new ArrayList<>();
    }

    // This Functions receives the path sets it for current tab and Make isLoaded True
    public void setPath(Path path) throws IOException {
        this.path = path;
        this.pdf = new PDF(path.toString());
        try {
            document = Loader.loadPDF(new RandomAccessReadBufferedFile(path.toString()));
            renderer = new PDFRenderer(document);
            this.pages = document.getNumberOfPages();
            System.out.println("Total number of pages in this pdf is "  + pages);
            this.end = pages;
            this.tabName = path.toString().split("///")[path.toString().split("///").length-1];
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        this.isLoaded = false;
        tabName = path.getFileName().toString();
        System.out.println("System logs ------------------" + tabName);
        setIsLoaded(true);
    }

    public String getTabName() {
        return tabName;
    }

    public boolean getIsLoaded() {
        return this.isLoaded;
    }

    private void setIsLoaded(Boolean b) {
        this.isLoaded = b;
    }

    public List<BufferedImage> loadImage() throws IOException {
        if (map.size() == pages) return map;
        synchronized (lock) {
            try {
                for (int i = 0; i < end; i++) {
                    map.add(renderer.renderImageWithDPI(i, 180));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return map;
    }

    public int getTotalPages() {
        return pages;
    }
}
