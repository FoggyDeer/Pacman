package GamePanel;

import Root.AbsoluteLayout;
import Root.AnimatedImage;
import Root.ResizablePanel;
import Root.Window;

import javax.swing.*;
import java.awt.*;

public class LoadingScreen extends ResizablePanel {
    private final AnimatedImage loading;
    private final JPanel glass;
    public LoadingScreen(Window window){
        super(window.getSize());
        setLocation(0, 0);
        setLayout(new AbsoluteLayout());
        setOpaque(false);

        this.glass = (JPanel) window.getGlassPane();
        glass.setOpaque(true);
        glass.setBackground(Color.BLACK);
        glass.setLayout(new AbsoluteLayout());

        loading = new AnimatedImage();
        loading.addAnimation("Pacman_Menu", "animations\\MenuBackground", 180);

        add(loading);
    }

    public void startLoading(){
        glass.add(this);
        glass.setVisible(true);
        loading.play_animation("Pacman_Menu");
    }

    public void stopLoading(){
        loading.stop_animation();
        glass.removeAll();
        glass.setVisible(false);
    }
}
