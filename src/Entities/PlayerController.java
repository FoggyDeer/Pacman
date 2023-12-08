package Entities;

import GamePanel.FieldCell;
import GamePanel.GameField;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerController extends EntityController implements KeyListener {
    private int lives = 3;

    public PlayerController(Entity entity, String name, GameField field, Integer... impassableBlocks) {
        super(entity, name, field, impassableBlocks);
        entity.addKeyListener(this);
        setSpeed(1f);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W)
            setDirection(UP);
        else if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S)
            setDirection(DOWN);
        else if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
            setDirection(LEFT);
        else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)
            setDirection(RIGHT);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void startMovement() {
        entity.addKeyListener(this);
        super.startMovement();
    }

    @Override
    public void stopMovement(boolean lock) {
        entity.removeKeyListener(this);
        super.stopMovement(lock);
    }

    @Override
    public boolean canMakeStep(FieldCell cell){
        return (getCurrentCell().getCellType() == FieldCell.BRIDGE_ROAD && cell == null) || (cell != null && !impassableBlocks.contains(cell.getCellType()));
    }

    @Override
    public void cellChanged(FieldCell cell) {
        if(cell.getCellType() == FieldCell.POINT_ROAD || cell.getCellType() == FieldCell.BUFF_ROAD) {
            if(cell.getCellType() == FieldCell.POINT_ROAD)
                entity.actionPerformed(new ActionEvent(this, 0, "Point"));
            if(cell.getCellType() == FieldCell.BUFF_ROAD)
                entity.actionPerformed(new ActionEvent(this, 0, "Buff"));

            int score = 0;
            if(cell.getCellType() == FieldCell.POINT_ROAD)
                score = 10;
            else if(cell.getCellType() == FieldCell.BUFF_ROAD){
                score = 100;
            }
            cell.setCellType(FieldCell.EMPTY_ROAD);
            field.getFieldModel().setValueAt(cell, cell.getLocation().x, cell.getLocation().y);
            field.updateScore(score);
        }
    }

    @Override
    public void reboot() {
        last_direction = 0;
        new_direction = 0;
        shift.setLocation(0, 0);
        next_cell = null;
        died = false;
    }

    @Override
    protected void setLastDirection(int direction){
        super.setLastDirection(direction);
        if(last_direction != 0) {
            switch (last_direction) {
                case RIGHT -> {
                    shift.setLocation(shift.getX(), 0);
                    entity.body.setAngle(0);
                }
                case UP -> {
                    shift.setLocation(0, shift.getY());
                    entity.body.setAngle(-90);
                }
                case DOWN -> {
                    shift.setLocation(0, shift.getY());
                    entity.body.setAngle(90);
                }
                case LEFT -> {
                    shift.setLocation(shift.getX(), 0);
                    entity.body.setAngle(180);
                }
            }
        }
    }

    @Override
    protected void move() {
        if (last_direction != new_direction && (((last_direction == UP || last_direction == DOWN) && (new_direction == UP | new_direction == DOWN)) ||
                ((last_direction == LEFT || last_direction == RIGHT) && (new_direction == LEFT | new_direction == RIGHT)))) {
            setNextByDirection();
        }
        super.move();
    }

    public int getLives() {
        return lives;
    }

    @Override
    public void fireCollision(CollisionEvent event) {
        EntityController controller = ((Entity)event.getSource()).getController();
        if(controller instanceof GhostController ctrl) {
            if(ctrl.getStatus() == GhostController.CALM && ctrl.isIgnoredCollision()) {
                died = true;
                lives--;
                entity.getController().setLock(false);
                entity.body.setAngle(0);
                entity.body.play_animation("Death", 1, true);
                field.death();
            } else if(ctrl.getStatus() == GhostController.SCARED){
                field.updateScore(400);
                entity.body.play_animation("Eating", 1);
            }
        }
    }
}
