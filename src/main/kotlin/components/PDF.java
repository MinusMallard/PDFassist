package components;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PDF {
    private PDDocument document;
    private PDFRenderer renderer;
    private String path;
    private int pages;

    public PDF (String path) throws IOException {
        this.path = path;
        try {
            document = Loader.loadPDF(new RandomAccessReadBufferedFile(path));
            this.pages = document.getNumberOfPages();
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public BufferedImage loadPage(int pageNo) throws IOException {
        if (pageNo < 0 || pageNo >= pages) {return null;}
        try {
            document = Loader.loadPDF(new RandomAccessReadBufferedFile(path));
            renderer =  new PDFRenderer(document);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            document.close();
        }

        return renderer.renderImage(pageNo);
    }
}
