package Root;

import javax.swing.*;
import java.awt.*;

public class ResizableTextLabel extends ResizablePanel {
    private final JLabel textLabel = new JLabel();

    public ResizableTextLabel(String text, Color color, Font font, int width, int height){
        super(width, height);
        setOpaque(false);

        textLabel.setText(text);
        textLabel.setFont(font);
        textLabel.setForeground(color);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setLayout(new BorderLayout());
        add(textLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font font = textLabel.getFont();
        int size = (int) Math.round(getHeight() / (96.0 / 72.0)) - getInsets().top - getInsets().bottom;
        textLabel.setFont(new Font(font.getFontName(), font.getStyle(), size));
    }

    public void setText(String text){
        textLabel.setText(text);
    }

    public void setTextAlignment(int textAlignment){
        textLabel.setHorizontalAlignment(textAlignment);
    }
}
