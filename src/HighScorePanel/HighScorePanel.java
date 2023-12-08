package HighScorePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HighScorePanel extends JPanel {
    public HighScorePanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 0, 0, 0));

        JList<ScoreInfoObject> list = new JList<>();
        list.setModel(new HighScoreListModel());
        list.setCellRenderer(new ScoreCellRenderer());
        list.addMouseListener(null);
        list.setFocusable(false);
        list.setOpaque(false);

        add(list);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("img\\Menu\\Background.png").getImage(), 0, 0, getWidth(), getHeight(), null);
    }
}
