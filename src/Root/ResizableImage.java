package Root;

import javax.swing.*;
import java.awt.*;

public class ResizableImage extends ResizablePanel {
    protected Image image;
    private double angle = 0;

    public ResizableImage(String path){
        this(new ImageIcon(path).getImage());
    }

    ResizableImage(Image image){
        super( (float) image.getWidth(null) / (float) image.getHeight(null));
        this.image = image;
        super.relation = (float) this.image.getWidth(null) / (float) this.image.getHeight(null);
        setOpaque(false);
        setAlignmentX(0.5f);
        setAlignmentY(0.5f);
    }

    protected ResizableImage(){
        super(0,0);
        this.image = null;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension d = getPreferredSize();

        int x = getWidth() / 2 - d.width / 2 + getInsets().left;
        int y = getHeight() / 2 - d.height / 2 + getInsets().top;
        int width = d.width - getInsets().right;
        int height = d.height - getInsets().bottom;

        ((Graphics2D) g).rotate(angle, width / 2.0, height / 2.0);
        g.drawImage(image, x, y, width, height, null);

    }

    public void setImage(Image image){
        if(this.image == null)
            initSize(image.getWidth(null), image.getHeight(null));
        this.image = image;
    }

    public void setAngle(double angle) {
        this.angle = angle / 180.0 * Math.PI;
    }
}
