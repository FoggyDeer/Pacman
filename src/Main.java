import MenuPanel.MenuWindow;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new MenuWindow(720, 480));
    }
}