package Root;

import javax.swing.*;
import java.awt.*;


public class AbsoluteLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {}

    @Override
    public void removeLayoutComponent(Component comp) {}

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int width = 0;
        int height = 0;

        for(Component component : parent.getComponents()){
            width = Math.max(width, component.getPreferredSize().width);
            height = Math.max(height, component.getPreferredSize().height);
        }

        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        int width = 0;
        int height = 0;

        for(Component component : parent.getComponents()){
            width = Math.max(width, component.getMinimumSize().width);
            height = Math.max(height, component.getMinimumSize().height);
        }

        return new Dimension(width, height);
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int top = insets.top;
            int bottom = parent.getHeight() - insets.bottom;
            int left = insets.left;
            int right = parent.getWidth() - insets.right;

            int x;
            int y;

            for (Component component : parent.getComponents()) {
                JComponent comp = (JComponent) component;

                Integer width = (Integer) comp.getClientProperty("width");
                Integer height = (Integer) comp.getClientProperty("height");
                Float proportion = (Float) comp.getClientProperty("proportion");

                if (width != null && height != null) {
                    if (width > 100) width = 100;
                    else if (width < 0) width = 0;

                    if (height > 100) height = 100;
                    else if (height < 0) height = 0;
                    if (proportion != null) {
                        if (proportion < 0) proportion = 0f;

                        width = (int) Math.min(Math.min(proportion * parent.getHeight(), parent.getWidth()), (float) (right - left) / 100.0 * width + (right - left) % 2);
                        height = (int) (width / proportion);

                    } else {
                        width = (int) ((float) (right - left) / 100.0 * width + (right - left) % 2);
                        height = (int) ((float) (bottom - top) / 100.0 * height + (bottom - top) % 2);

                    }
                } else if (proportion != null) {
                    if (proportion < 0) proportion = 0f;
                    width = (int) Math.min(proportion * parent.getHeight(), parent.getWidth());
                    height = (int) (width / proportion);
                } else {
                    width = comp.getWidth();
                    height = comp.getHeight();
                }

                float anchorX = component.getAlignmentX();
                float anchorY = component.getAlignmentY();

                if (anchorX == 0.5f)
                    x = ((right - left) / 2 + (right - left) % 2) - (width / 2 + width % 2);
                else if (anchorX == 0.0f)
                    x = left;
                else if (anchorX == 1.0f)
                    x = right - component.getWidth();
                else
                    x = comp.getX()+left;


                if (anchorY == 0.5f)
                    y = ((bottom - top) / 2 + (bottom - top) % 2) - (height / 2 + height % 2);
                else if (anchorY == 0.0f)
                    y = top;
                else if (anchorY == 1.0f)
                    y = bottom - component.getHeight();
                else
                    y = comp.getY()+top;

                comp.setBounds(x, y, width, height);
            }
        }
    }
}
