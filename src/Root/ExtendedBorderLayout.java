package Root;
import java.awt.*;

public class ExtendedBorderLayout extends BorderLayout {

    int min_width;

    int min_height;

    int max_header_height;

    int max_footer_height;

    int max_left_bar_width;

    int max_right_bar_width;

    int vgap;

    int hgap;

    public ExtendedBorderLayout(int min_width, int min_height, int max_header_height, int max_footer_height){
        this(min_width, min_height, max_header_height, max_footer_height, 0, 0, 0, 0);
    }

    public ExtendedBorderLayout(int min_width, int min_height, int max_header_height, int max_footer_height, int max_left_bar_width, int max_right_bar_width, int vgap, int hgap){
        this.min_width = min_width;
        this.min_height = min_height;
        this.max_header_height = max_header_height;
        this.max_footer_height = max_footer_height;
        this.max_left_bar_width = max_left_bar_width;
        this.max_right_bar_width = max_right_bar_width;
        this.vgap = vgap;
        this.hgap = hgap;
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int top = insets.top;
            int bottom = parent.getHeight() - insets.bottom;
            int left = insets.left;
            int right = parent.getWidth() - insets.right;

            int height;
            int width;

            Component center = super.getLayoutComponent(BorderLayout.CENTER);
            Component north = super.getLayoutComponent(BorderLayout.NORTH);
            Component south = super.getLayoutComponent(BorderLayout.SOUTH);
            Component east = super.getLayoutComponent(BorderLayout.EAST);
            Component west = super.getLayoutComponent(BorderLayout.WEST);

            if (north != null) {
                height = Math.min(bottom/3, max_header_height);

                if(north.getParent().getWidth() < min_width)
                    height = Math.min(height, (int)(north.getParent().getWidth() * ((float)max_header_height/min_width)));

                north.setBounds(left, top, right - left, height);
                top += height + vgap;
            }
            if (south != null) {
                height = Math.min(bottom/3, max_footer_height);

                if(south.getParent().getWidth() < min_width)
                    height = Math.min(height, (int)((float) south.getParent().getWidth() * ((float) max_footer_height/(float) min_width)));

                south.setBounds(left, bottom - height, right - left, height);
                bottom -= height + vgap;
            }
            if (west != null) {
                width = Math.min(right/3, max_left_bar_width);

                if(bottom - top < min_height)
                    width = Math.min(width, (int)((bottom - top) * ((float)max_left_bar_width/min_height)));

                west.setBounds(left, top, width, bottom - top);
                left += width + hgap;
            }
            if (east != null) {
                width = Math.min(right/3, max_right_bar_width);

                if(bottom - top < min_height)
                    width = Math.min(width, (int)((bottom - top) * ((float)max_right_bar_width/min_height)));

                east.setBounds(right - width, top, width, bottom - top);
                right -= width + hgap;
            }

            int x = left;
            int y = top;

            if (center != null) {
                center.setBounds(x, y, right - left, bottom - top);
            }
        }
    }
}
