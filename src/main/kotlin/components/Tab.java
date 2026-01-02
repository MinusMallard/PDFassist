package components;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class Tab {
    private Path path;
    private PDF pdf;
    public boolean isLoaded;
    private final String tabName;
    public Tab(){
        tabName = "New Tab";
    }
    public Tab(Path path, PDF pdf) throws IOException {
        this.path = path;
        this.pdf = new PDF(path.toString());
        this.isLoaded = false;
        tabName = path.toString().split("//")[path.toString().split("//").length-1];
    }

    public String getTabName() {
        return tabName;
    }

    public boolean getIsLoaded() {
        return this.isLoaded;
    }

    public void setIsLoaded() {
        this.isLoaded = !this.isLoaded;
    }

    public BufferedImage loadImage(int pageNo) throws IOException {
        return pdf.loadPage(pageNo);
    }

}
