package MenuPanel;

import GamePanel.GameWindow;
import Root.*;
import Root.Window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChooseSizePanel extends ResizablePanel {
    ChooseSizePanel(Window window, int width, int height){
        super(width, height);
        setLayout(new ExtendedBorderLayout(width, height, 60, 48));
        setOpaque(false);

        JPanel title_panel = new JPanel();
        title_panel.setLayout(new AbsoluteLayout());

        ResizableImage title_image = new ResizableImage("img\\Menu\\SizePanelTitle.png");
        title_panel.add(title_image);

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());

        JTextField textField = new JTextField();
        textField.setOpaque(false);

        textField.setBorder(new EmptyBorder(0,0,0,0));
        textField.setCaretColor(Color.WHITE);
        textField.requestFocus();
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("Arial", Font.BOLD, 24));
        textField.setForeground(Color.WHITE);

        main.add(textField, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setLayout(new AbsoluteLayout());

        ResizableButton back_button = new ResizableButton("img\\Menu\\Buttons\\Back");
        back_button.setAlignmentX(Component.LEFT_ALIGNMENT);
        back_button.putClientProperty("width", 31);
        back_button.putClientProperty("height", 100);
        footer.add(back_button);

        ResizableButton continue_button = new ResizableButton("img\\Menu\\Buttons\\Continue");
        continue_button.setAlignmentX(Component.RIGHT_ALIGNMENT);
        continue_button.putClientProperty("width", 39);
        continue_button.putClientProperty("height", 100);
        footer.add(continue_button);

        title_panel.setOpaque(false);
        main.setOpaque(false);
        footer.setOpaque(false);

        add(title_panel, ExtendedBorderLayout.NORTH);
        add(main, ExtendedBorderLayout.CENTER);
        add(footer, ExtendedBorderLayout.SOUTH);

        final Thread[] thread = {null};

        continue_button.addActionListener(e->{
            try {
                if(thread[0] != null){
                    thread[0].interrupt();
                    title_panel.removeAll();
                    title_panel.add(title_image);
                    title_panel.revalidate();
                    title_panel.repaint();
                }

                String[] numbers = textField.getText().split("x");
                int number_1 = Integer.parseInt(numbers[0]);
                int number_2 = Integer.parseInt(numbers[1]);

                if(number_1 < 10 || number_1 > 100 || number_2 < 10 || number_2 > 100)
                    throw new NumberFormatException();

                new GameWindow(window.getSize(), new Dimension(number_1, number_2));
                window.close(false);

            }catch (NumberFormatException exception){
                ResizableTextLabel notification = new ResizableTextLabel("You must enter number between 10-100", Color.RED, getFont(), 880, title_panel.getHeight());

                thread[0] = new Thread(()->{
                    title_panel.removeAll();
                    title_panel.add(notification, BorderLayout.CENTER);
                    title_panel.revalidate();
                    title_panel.repaint();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {}

                    title_panel.removeAll();
                    title_panel.add(title_image);
                    title_panel.revalidate();
                    title_panel.repaint();
                });

                thread[0].start();
            }
        });

        back_button.addActionListener(e->{
            window.clear();
            window.getContentPane().add(new MenuPanel(window));
        });
    }
}
