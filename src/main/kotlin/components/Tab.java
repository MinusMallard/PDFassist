package components;

import java.nio.file.Path;

public class Tab {
    private Path path;
    private PDF pdf;

    private final String tabName;
    public Tab(){
        tabName = "New Tab";
    }
    public Tab(Path path, PDF pdf) {
        this.path = path;
        this.pdf = pdf;
        tabName = path.toString().split("//")[path.toString().split("//").length-1];
    }

    public String getTabName() {
        return tabName;
    }

}
