package components;

import java.nio.file.Path;

public class Tab {
    private Path path;
    private PDF pdf;
    public boolean isLoaded;
    private final String tabName;
    public Tab(){
        tabName = "New Tab";
    }
    public Tab(Path path, PDF pdf) {
        this.path = path;
        this.pdf = pdf;
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

}
