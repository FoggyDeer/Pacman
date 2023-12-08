package GamePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FieldCellRenderer extends DefaultTableCellRenderer {
    private final Map<String, ImageIcon> images = new HashMap<>();
    private final Map<String, ImageIcon> scaled_images;
    private final Dimension size = new Dimension(0, 0);

    FieldCellRenderer(){
        setOpaque(false);
        File path = new File("img\\Game\\Cells\\");
        File[] files = path.listFiles();

        if(files != null && files.length > 0) {
            for (File file : files) {
                images.put(file.getName(), new ImageIcon(file.getPath()));
            }
        }

        scaled_images = new HashMap<>(images);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        String cell_style = ((FieldCell) value).getCellStyle();
        if(cell_style.length() > 0) {
            cell_style +=  ".png";
            size.width = table.getCellRect(row, column, true).width;
            size.height = table.getCellRect(row, column, true).height;

            if ((scaled_images.get(cell_style).getIconHeight() != size.width && scaled_images.get(cell_style).getIconWidth()!= size.width - 1 && scaled_images.get(cell_style).getIconWidth() != size.width + 1)
                    || (scaled_images.get(cell_style).getIconHeight() != size.height && scaled_images.get(cell_style).getIconHeight() != size.height - 1 && scaled_images.get(cell_style).getIconHeight() != size.height + 1))
                scaleImage(cell_style);

            setIcon(scaled_images.get(cell_style));
        }
        else
            setIcon(null);

        return this;
    }

    private void scaleImage(String cell_style){
        scaled_images.replace(cell_style, new ImageIcon(images.get(cell_style).getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT)));
    }
}
