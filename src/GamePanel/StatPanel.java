package GamePanel;

import Root.ExtendedBorderLayout;
import Root.ResizablePanel;
import Root.ResizableImage;
import Root.ResizableTextLabel;

import javax.swing.*;
import java.awt.*;

public class StatPanel extends ResizablePanel {
    protected int stat;
    protected final ResizableTextLabel statLabel;

    StatPanel(ResizableImage title, int width, int height){
        super(width, height);
        setOpaque(false);
        setLayout(new ExtendedBorderLayout(width, height, height/2, height/2));

        statLabel = new ResizableTextLabel("0", Color.WHITE, new Font("Arial", Font.BOLD, 24), width, height/3);
        statLabel.setTextAlignment(SwingConstants.CENTER);

        add(title, ExtendedBorderLayout.CENTER);
        add(statLabel, ExtendedBorderLayout.SOUTH);
    }

    public void changeStat(int val){
        this.stat += val;
        statLabel.setText(Integer.toString(this.stat));
    }

    public int getStat() {
        return stat;
    }
}
