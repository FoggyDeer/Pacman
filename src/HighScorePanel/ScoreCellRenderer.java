package HighScorePanel;

import Root.ResizableImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ScoreCellRenderer extends JPanel implements ListCellRenderer<ScoreInfoObject> {
    private final ResizableImage cup = new ResizableImage("img\\Game\\Cup.png");
    private final JPanel panel = new JPanel();
    private final JPanel nickname = new JPanel();
    private final JPanel score = new JPanel();
    private final JLabel nickname_label = new JLabel();
    private final JLabel score_label = new JLabel();

    ScoreCellRenderer(){
        setOpaque(false);

        cup.setSize(100, 80);
        cup.setPreferredSize(new Dimension(100, 80));

        panel.setLayout(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(Color.ORANGE);

        nickname.setLayout(new BorderLayout());
        nickname.setBorder(new EmptyBorder(0, 20, 0, 0));
        score.setLayout(new BorderLayout());
        nickname.setOpaque(false);
        score.setOpaque(false);

        nickname_label.setForeground(Color.WHITE);
        score_label.setForeground(Color.WHITE);
        score_label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ScoreInfoObject> list, ScoreInfoObject value, int index, boolean isSelected, boolean cellHasFocus) {
        panel.removeAll();
        nickname.removeAll();
        score.removeAll();

        nickname_label.setText(value.nickname);
        score_label.setText(Integer.toString(value.score));


        if(list.getModel().getElementAt(0).score == value.score) {
            nickname_label.setFont(new Font("Arial", Font.BOLD, 48));
            score_label.setFont(new Font("Arial", Font.BOLD, 48));

            panel.add(cup, BorderLayout.LINE_END);
            panel.setSize(800, 80);
            panel.setPreferredSize(new Dimension(800, 80));
        } else{
            nickname_label.setFont(new Font("Arial", Font.BOLD, 32));
            score_label.setFont(new Font("Arial", Font.BOLD, 32));
            panel.setSize(600, 50);
            panel.setPreferredSize(new Dimension(600, 50));
        }

        nickname.add(nickname_label);
        score.add(score_label);
        panel.add(nickname, BorderLayout.LINE_START);
        panel.add(score, BorderLayout.CENTER);
        add(panel);

        return this;
    }
}
