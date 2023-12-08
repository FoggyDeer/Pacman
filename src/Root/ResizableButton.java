package Root;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ResizableButton extends JButton {
    private final ImageIcon default_icon;
    private final ImageIcon pressed_icon;
    private final ImageIcon hover_icon;
    int width;
    int height;
    float relation;

    public ResizableButton(String path){
        setContentAreaFilled(false);
        setMargin(new Insets(0,0,0,0));
        setBorder(new EmptyBorder(0,0,0,0));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFocusPainted(false);

        this.default_icon = new ImageIcon(path+"\\Regular.png");
        this.pressed_icon = new ImageIcon(path+"\\Pressed.png");
        this.hover_icon = new ImageIcon(path+"\\Hover.png");


        this.width = default_icon.getIconWidth();
        this.height = default_icon.getIconHeight();
        this.relation = (float) width / (float) height;
    }

    @Override
    public Dimension getPreferredSize() {
        int width, height;

        width = Math.min(Math.min(this.width, (int) (getParent().getHeight() * relation)), getParent().getWidth() + getInsets().left + getInsets().right);
        height = (int) ((float) width / relation);

        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setPreferredSize(getPreferredSize());
        setIcons();
    }

    public void setIcons(){
        if(getSize().width > 0 && getSize().height > 0) {
            setIcon(getScaledButtonIcon(getSize(), default_icon));
            setPressedIcon(getScaledButtonIcon(getSize(), pressed_icon));
            setRolloverIcon(getScaledButtonIcon(getSize(), hover_icon));
        }
    }

    public ImageIcon getScaledButtonIcon(Dimension dimension, Icon icon){
        return new ImageIcon(((ImageIcon)icon).getImage().getScaledInstance(dimension.width, dimension.height, Image.SCALE_DEFAULT));
    }
}
