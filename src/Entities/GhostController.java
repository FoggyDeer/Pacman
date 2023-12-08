package Entities;

import GamePanel.FieldCell;
import GamePanel.GameField;

import java.util.AbstractList;
import java.util.List;

public class GhostController extends EntityController{
    private boolean respawned = false;
    public static final int CALM = 0;
    public static final int SCARED = 1;
    public static final int MAGIC = 2;
    public static final int AGGRESSIVE = 3;

    private int status;

    public GhostController(Entity entity, String name, GameField field, Integer... impassableBlocks) {
        super(entity, name, field, impassableBlocks);
        status = CALM;
        setSpeed(0.6f);
    }

    @Override
    public void cellChanged(FieldCell cell) {
        setDirection(getDirection());
    }

    @Override
    public void startMovement() {
        setDirection(UP);
        super.startMovement();
    }

    private int i = 0;
    public int getDirection(){
        AbstractList<Integer> dir = getAvailableDirections();
        if(++i < 2 && dir.contains(super.getDirection())) return super.getDirection();
        i = 0;

        return dir.get((int) (Math.random() * dir.size()));
    }

    @Override
    public boolean canMakeStep(FieldCell cell){
        if(getCurrentCell() != null && getCurrentCell().getCellType() == FieldCell.EXTRA_WALL)
            impassableBlocks.addAll(List.of(FieldCell.EXTRA_WALL, FieldCell.VOID));
        return (getCurrentCell().getCellType() == FieldCell.BRIDGE_ROAD && cell == null) || !impassableBlocks.contains(cell.getCellType());
    }

    public void reboot(){
        impassableBlocks.removeAll(List.of(FieldCell.EXTRA_WALL, FieldCell.VOID));
    }

    @Override
    public void stopMovement(boolean lock) {
        super.stopMovement(lock);
    }

    public void updateDirection(){
        setDirection(getDirection());
    }

    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status) {
        if(status == CALM)
            entity.body.play_animation("Default");
        else if(this.status != SCARED && status == SCARED) {
            entity.body.play_animation("Scared");
        }
        this.status = status;
    }

    public void setRespawned(boolean respawned) {
        this.respawned = respawned;
    }

    @Override
    public void fireCollision(CollisionEvent event) {
        if (event.getSource() instanceof Player) {
            if (status == SCARED) {
                reboot();
                stopMovement(false);
                entity.body.play_animation("Died");
                field.initEntity(entity);
                died = true;
                setIgnoreCollision(true);
                field.updateGhosts();
            }
        }
    }

    public boolean isRespawned() {
        if(respawned) {
            respawned = false;
            return true;
        }
        return false;
    }
}
