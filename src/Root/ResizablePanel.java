package Root;

import javax.swing.*;
import java.awt.*;


public class ResizablePanel extends JPanel{
    protected float relation;
    protected int width;
    protected int height;
    protected boolean proportional = true;

    public ResizablePanel(float relation){
        initSize(relation);
    }

    public ResizablePanel(int width, int height){
        setMaximumSize(new Dimension(width, height));
        initSize(width, height);
    }

    public ResizablePanel(Dimension dimension){
        this(dimension.width, dimension.height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        setSize(getPreferredSize());
    }

    @Override
    public Dimension getPreferredSize() {
        int width = this.width, height = this.height;

        if(getParent() != null) {
            JComponent parent = (JComponent) getParent();

            if (proportional) {
                if(this.width == 1 && this.height == 1){
                    width = parent.getWidth();
                }

                width = Math.min(Math.min(width, (int) ((float) parent.getHeight() * relation)), parent.getWidth() - parent.getInsets().left - parent.getInsets().right);
                height = (int) ((float) width / relation);
            } else {
                width = parent.getWidth() - getParent().getInsets().left - getParent().getInsets().right;
                height = parent.getHeight();
            }
        }

        width -= getInsets().left + getInsets().right;
        height -= getInsets().top + getInsets().bottom;

        if(width < 0) width = 0;
        if(height < 0) height = 0;

        return new Dimension(width, height);
    }

    public void initSize(int width, int height){
        if(width <= 0) width = 1;
        if(height <= 0) height = 1;

        if(this.width != width || this.height != height) {
            super.setSize(width, height);
            setPreferredSize(new Dimension(width, height));
        }

        this.width = width;
        this.height = height;
        this.relation = (float) width / (float) height;
    }

    public void initSize(float relation){
        if(relation <= 0) relation = 1;
        this.width = 1;
        this.height = 1;

        setSize(1, 1);
        setPreferredSize(new Dimension(1,1));
        setMinimumSize(new Dimension(1,1));

        this.relation = relation;
    }

    public float getRelation() {
        return relation;
    }
}
