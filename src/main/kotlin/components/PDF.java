package components;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDF {
    private final PDDocument document;
    private final PDFRenderer renderer;
    private final int pages;

    private final int start = 1;
    private int end = Integer.MAX_VALUE;
    private List<BufferedImage> map;

    public PDF (String path) throws IOException {
        this.map = new ArrayList<>();
        try {
            document = Loader.loadPDF(new RandomAccessReadBufferedFile(path));
            renderer = new PDFRenderer(document);
            this.pages = document.getNumberOfPages();
            this.end = pages;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int totalPages() {
        return pages;
    }

    public List<BufferedImage> loadPage() throws IOException {
        BufferedImage bf;
        try {
            for (int i = 0; i <= end; i++) {
                map.add(renderer.renderImageWithDPI(i, 90));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        document.close();
        return map;
    }
}
