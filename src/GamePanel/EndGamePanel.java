package GamePanel;

import Root.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class EndGamePanel extends ResizablePanel {
    private final GamePanel parent;
    public EndGamePanel(GamePanel panel) {
        super(600,200);
        parent = panel;
        setAlignmentX(CENTER_ALIGNMENT);
        setAlignmentY(CENTER_ALIGNMENT);
        setLayout(new ExtendedBorderLayout(width, height, height / 3, height / 3));
        setBorder(new EmptyBorder(0,0,0,0));
        setOpaque(false);

        JPanel title_panel = new JPanel();
        title_panel.setOpaque(false);

        ResizableImage title_image = new ResizableImage("img\\Game\\EnterNicknameTitle.png");
        title_panel.add(title_image);

        JPanel main = new JPanel();
        main.setOpaque(false);
        main.setLayout(new BorderLayout());
        main.setBorder(new EmptyBorder(10,0,10,0));

        JTextField textField = new JTextField();
        textField.setOpaque(false);

        textField.setBorder(new LineBorder(Color.white,2));
        textField.setCaretColor(Color.WHITE);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("Arial", Font.BOLD, 24));
        textField.setForeground(Color.WHITE);

        main.add(textField, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new AbsoluteLayout());

        ResizableButton ok_button = new ResizableButton("img\\Menu\\Buttons\\Ok");
        ok_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        ok_button.putClientProperty("proportion", 3f);
        ok_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        footer.add(ok_button);


        add(title_panel, ExtendedBorderLayout.NORTH);
        add(main, ExtendedBorderLayout.CENTER);
        add(footer, ExtendedBorderLayout.SOUTH);

        ok_button.addActionListener(e -> {
            if(textField.getText().length() > 0){
                parent.saveScore(textField.getText());
                parent.getWindow().getController().returnToMenu();
            }
        });
    }
}
