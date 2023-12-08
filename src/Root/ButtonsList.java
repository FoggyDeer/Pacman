package Root;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ButtonsList extends ResizablePanel {
    private final int vgap;

    private final ArrayList<JButton> buttons = new ArrayList<>();

    public ButtonsList(List<JButton> buttons, int width, int height, int buttons_height){
        super(width, height);
        setOpaque(false);

        this.vgap = (height-buttons_height*buttons.size()) / (buttons.size()-1);

        setLayout(new GridLayout(buttons.size(), 1, 0, vgap));

        addAll(buttons);
        setMaximumSize(new Dimension(width, height));
    }

    public void addAll(List<JButton> buttons){
        for(JButton button : buttons) {
            addButton(button);
        }
    }

    public void addButton(JButton button){
        buttons.add(button);
        add(button);
        ((GridLayout)getLayout()).setRows(buttons.size());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((GridLayout)getLayout()).setVgap((int)(getHeight()/getRelativeVgap()));
        setButtonsSize(new Dimension(getWidth(), (int) (getHeight()/buttons.size()-(getHeight()/getRelativeVgap()))));
    }

    public float getRelativeVgap(){
        return (float)height / (float)vgap;
    }

    public void setButtonsSize(Dimension dimension) {
        for(JButton button : buttons)
            button.setSize(dimension);
    }
}
