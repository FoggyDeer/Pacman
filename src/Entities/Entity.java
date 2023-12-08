package Entities;

import Root.AbsoluteLayout;
import Root.AnimatedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Entity extends JPanel implements ActionListener {
    protected EntityController controller;

    protected AnimatedImage body = new AnimatedImage();

    Entity(){
        super();
        putClientProperty("proportion", 1.0f);
        setOpaque(false);
        setLayout(new AbsoluteLayout());
        add(body);

        setAlignmentX(0.3f);
        setAlignmentY(0.3f);
    }

    public void initSize(int width, int height) {
        putClientProperty("width", width);
        putClientProperty("height", height);
    }

    public void setLocation(int x, int y) {
        super.setLocation(x - getWidth() / 2, y - getHeight() / 2);
    }

    public void setLocation(Point point) {
        super.setLocation(point.x - Math.round(getWidth() / 2f), point.y - Math.round(getHeight() / 2f));
    }

    public EntityController getController() {
        return controller;
    }

    public void setController(EntityController controller){
        this.controller = controller;
    }

    @Override
    public void revalidate() {
        if(getParent() != null) {
            float height = (float) getParent().getHeight() / (float) controller.field.getFieldModel().getRowCount();
            Point cell_loc = controller.getField().getCellLocation(controller.getCurrentCell());
            Point new_loc = new Point((int) Math.round(cell_loc.x + height / 100.0 * controller.getShift().getX()), (int) Math.round(cell_loc.y + height / 100.0 * controller.getShift().getY()));
            this.setLocation(new_loc);
        }
    }

    public AnimatedImage getBody() {
        return body;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        revalidate();
    }
}
