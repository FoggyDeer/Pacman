package MenuPanel;

import HighScorePanel.HighScoreWindow;
import Root.*;
import Root.Window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MenuPanel extends JPanel implements WindowPanel{
    public MenuPanel(Window window){
        initPanel(window);

        ResizableImage title_image = new ResizableImage("img\\Menu\\Title.png");

        JPanel title_panel = new JPanel();
        title_panel.setOpaque(false);
        title_panel.setLayout(new AbsoluteLayout());
        title_panel.add(title_image);

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BorderLayout());
        footer.setPreferredSize(new Dimension(100,100));

        JPanel main = new JPanel();
        main.setOpaque(false);
        main.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        ResizableButton newGame = new ResizableButton("img\\Menu\\Buttons\\New Game");
        ResizableButton highScore = new ResizableButton("img\\Menu\\Buttons\\High Score");
        ResizableButton exit = new ResizableButton("img\\Menu\\Buttons\\Exit");

        ButtonsList buttonsList = new ButtonsList(List.of(newGame, highScore, exit), 250, 300, 90);
        main.add(buttonsList, gbc);

        add(title_panel, ExtendedBorderLayout.NORTH);
        add(footer, ExtendedBorderLayout.SOUTH);
        add(main, ExtendedBorderLayout.CENTER);

        newGame.addActionListener(e -> {
            main.removeAll();

            main.add(new ChooseSizePanel(window, 500, 200), gbc);
            revalidate();
            repaint();
        });

        highScore.addActionListener(e -> {
            new HighScoreWindow();
            window.close(false);
        });

        exit.addActionListener(e -> System.exit(0));
    }

    public void initPanel(Window window){
        setLayout(new ExtendedBorderLayout(window.getWidth(), window.getHeight(), 100, 100));
        setOpaque(false);
        setBorder(new EmptyBorder(0, 50, 0, 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("img\\Menu\\Background.png").getImage(), 0, 0, getWidth(), getHeight(), null);
    }
}
