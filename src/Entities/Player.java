package Entities;

import GamePanel.FieldCell;
import GamePanel.GameField;

import java.awt.event.*;

public class Player extends Entity implements ActionListener{

    public Player(GameField field) {
        setController(new PlayerController(this, "Pacman", field, FieldCell.WALL, FieldCell.EXTRA_WALL, FieldCell.VOID));

        body.addAnimation("Death", "animations\\Pacman\\Death", 80);
        body.addAnimation("Eating", "animations\\Pacman\\Eating", 100);

        setFocusable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Point")){
            body.play_animation("Eating", 1);
        }else if(e.getActionCommand().equals("Buff")){
            body.play_animation("Eating", 1);
            controller.getField().makeScared();
        }
    }
}
