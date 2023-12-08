package Entities;

import GamePanel.FieldCell;
import GamePanel.GameField;

import java.awt.event.ActionEvent;

public class Ghost extends Entity{
    public Ghost(GameField field, String name){
        setController(new GhostController(this, name, field, FieldCell.WALL));
        body.addAnimation("Default", "animations\\Ghosts\\" + name, 100);
        body.addAnimation("Scared", "animations\\Ghosts\\Scared", 100);
        body.addAnimation("Died", "animations\\Ghosts\\Died", 100);
        body.addAnimation("Transparent", "animations\\Ghosts\\Transparent", 100);
        body.addAnimation("Aggressive", "animations\\Ghosts\\Aggressive", 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Arrived")) {
            ((GhostController)controller).updateDirection();
        }
    }
}
