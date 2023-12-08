package Entities;

import GamePanel.FieldCell;
import GamePanel.GameField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public abstract class EntityController implements CollisionListener {
    protected final Entity entity;
    private final String name;
    protected GameField field;
    protected final ArrayList<Integer> impassableBlocks;
    protected boolean died = false;
    private boolean ignoreCollision = false;

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;

    private float speed;
    protected int last_direction = 0;
    protected int new_direction = 0;
    protected final Point2D shift = new Point2D.Double(0, 0);

    protected FieldCell current_cell;
    protected FieldCell next_cell;
    private Thread moveThread = null;

    public EntityController(Entity entity, String name, GameField field, Integer... impassableBlocks) {
        this.entity = entity;
        this.name = name;
        this.field = field;
        this.impassableBlocks = new ArrayList<>(List.of(impassableBlocks));
    }

    public void startMovement(){
        Component parent = entity.getParent();
        if(parent instanceof GameField) {
            field = (GameField) parent;
            moveThread = MoveThread();
            moveThread.start();
            entity.body.play_animation("Default");
        }
    }

    public void kill(){
        entity.body.stop_animation();
        shift.setLocation(0, 0);
        stopMovement(true);
        reboot();
        if(entity.getParent() != null)
            entity.getParent().remove(entity);
    }

    public void stopMovement(boolean lock){
        if(moveThread != null) {
            moveThread.interrupt();
            moveThread = null;
        }
        last_direction = 0;
        new_direction = 0;
        entity.body.setLock(lock);
    }

    public Thread MoveThread(){
        return new Thread(() -> {
            try {
                setNextByDirection();
                while (true){
                    if(last_direction != 0) {
                        if (canMakeStep(next_cell)) {
                            if (shift.getX() >= 100 || shift.getX() <= -100 || shift.getY() <= -100 || shift.getY() >= 100) {
                                shift.setLocation(0, 0);
                                current_cell = next_cell;

                                setNextByDirection();
                                cellChanged(current_cell);
                            }
                            move();
                        } else {
                            shift.setLocation(0, 0);
                            setLastDirection(0);
                            new_direction = 0;
                            entity.actionPerformed(new ActionEvent(this, 0, "Arrived"));
                        }
                    } else {
                        setLastDirection(new_direction);
                        new_direction = 0;
                        next_cell = getNextCellByDirection(current_cell, last_direction);
                    }
                    entity.repaint();
                    Thread.sleep(2);
                }
            } catch (InterruptedException ignored) {}
        });
    }

    protected void move(){
        switch (last_direction) {
            case LEFT -> shift.setLocation(shift.getX() - speed, shift.getY());
            case UP -> shift.setLocation(shift.getX(), shift.getY() - speed);
            case RIGHT -> shift.setLocation(shift.getX() + speed, shift.getY());
            case DOWN -> shift.setLocation(shift.getX(), shift.getY() + speed);
        }
    }

    protected void setLastDirection(int direction){
        this.last_direction = direction;
    }

    public FieldCell getNextCellByDirection(FieldCell cell, int direction){
        if(direction == 0) direction = last_direction;
        FieldCell next_cell;
        if((shift.getX() != 0 && shift.getY() == 0) || (shift.getX() == 0 && shift.getY() != 0)){
            next_cell = current_cell;
            current_cell = this.next_cell;
            if(shift.getX() > 0)
                shift.setLocation(-100 + shift.getX(), shift.getY());
            else if(shift.getY() > 0)
                shift.setLocation(shift.getX(), -100 + shift.getY());
            else if(shift.getX() < 0)
                shift.setLocation(100 + shift.getX(), shift.getY());
            else if(shift.getY() < 0)
                shift.setLocation(shift.getX(), 100 + shift.getY());
        } else {
            next_cell = direction == UP ? cell.north :
                    direction == DOWN ? cell.south :
                            direction == LEFT ? cell.west : cell.east;
        }

        if(next_cell == null) {
            FieldCell[] teleports = field.getFieldModel().getTeleports();
            this.current_cell = teleports[0].equals(cell) ? teleports[1] : teleports[0];
            next_cell = getNextCellByDirection(this.current_cell, last_direction);
        }
        return next_cell;
    }

    public void setNextByDirection(){
        FieldCell buff_cell = getNextCellByDirection(current_cell, new_direction);
        if (canMakeStep(buff_cell)) {
            if (new_direction != 0)
                setLastDirection(new_direction);
            next_cell = buff_cell;
        } else {
            next_cell = getNextCellByDirection(current_cell, last_direction);
        }
    }

    public int getDirection() {
        return last_direction;
    }

    public void setDirection(int move_direction){
        this.new_direction = move_direction;
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public FieldCell getCurrentCell(){
        return current_cell;
    }

    public void setCurrentCell(FieldCell cell){
        this.current_cell = cell;
    }

    public GameField getField() {
        return field;
    }

    public Point2D getShift() {
        return shift;
    }

    public void setLock(boolean lock){
        entity.body.setLock(lock);
    }

    public boolean isDied() {
        return died;
    }

    public void setDied(boolean died){
        this.died = died;
    }

    public ArrayList<Integer> getAvailableDirections(){
        ArrayList<Integer> dir = new ArrayList<>();
        if(canMakeStep(current_cell.west)) dir.add(LEFT);
        if(canMakeStep(current_cell.north)) dir.add(UP);
        if(canMakeStep(current_cell.east)) dir.add(RIGHT);
        if(canMakeStep(current_cell.south)) dir.add(DOWN);
        return dir;
    }

    public abstract boolean canMakeStep(FieldCell cell);

    public abstract void cellChanged(FieldCell cell);

    public abstract void reboot();

    public boolean isIgnoredCollision(){
        return !ignoreCollision;
    }

    public void setIgnoreCollision(boolean ignoreCollision) {
        this.ignoreCollision = ignoreCollision;
    }
}
