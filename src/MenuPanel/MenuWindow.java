package MenuPanel;

import Root.Window;

import java.awt.*;

public class MenuWindow extends Window {
    public MenuWindow(Dimension size){
        this(size.width, size.height);
    }
    public MenuWindow(int width, int height){
        super(width, height);
        add(new MenuPanel(this));
    }
}
