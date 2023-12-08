package HighScorePanel;

import Root.Window;

import javax.swing.*;
import java.awt.*;

public class HighScoreWindow extends Window {
    public HighScoreWindow() {
        super(900, 480);
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JScrollPane pane = new JScrollPane();
        pane.getViewport().setOpaque(false);
        pane.setOpaque(false);

        pane.getViewport().add(new HighScorePanel());
        getContentPane().add(pane);
    }
}
