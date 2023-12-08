package GamePanel;

import Entities.*;
import Root.AbsoluteLayout;
import Root.ResizablePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.*;

public class GameField extends ResizablePanel {
    private boolean isRunning = true;
    public static final Object loadingMonitor = new Object();
    private final JTable field_table = new JTable();
    private FieldModel fieldModel;
    private final ArrayList<Entity> creatures = new ArrayList<>();
    private Thread buffThread;
    private final GamePanel parent;

    public GameField(GamePanel panel, Dimension field_size){
        this(panel, field_size.width, field_size.height);
    }

    public GameField(GamePanel panel, int field_width, int field_height) {
        super((float) (field_width + 2) / (float) (field_height + 2));
        this.parent = panel;
        setLayout(new AbsoluteLayout());
        setOpaque(false);
        putClientProperty("proportion", getRelation());

        field_table.putClientProperty("proportion", (float) field_width / field_height);
        field_table.setOpaque(false);
        field_table.setFocusable(false);
        field_table.setDefaultRenderer(FieldCell.class, new FieldCellRenderer());
        field_table.setIntercellSpacing(new Dimension(0, 0));
        field_table.setRowMargin(0);
        field_table.setShowGrid(false);
        field_table.setBorder(new EmptyBorder(0,0,0,0));
        field_table.addMouseListener(null);

        field_table.setAlignmentY(Component.TOP_ALIGNMENT);
        field_table.setAlignmentX(Component.LEFT_ALIGNMENT);

        Thread thread = new Thread(() -> {
            synchronized (loadingMonitor) {
                fieldModel = new FieldModel(field_width, field_height);
                field_table.setModel(fieldModel);
                Iterator<TableColumn> it = field_table.getColumnModel().getColumns().asIterator();
                while (it.hasNext()) {
                    TableColumn column = it.next();
                    column.setMinWidth(0);
                }
                add(field_table);
                loadingMonitor.notify();
            }
        });
        thread.start();
    }

    public Point getCellLocation(FieldCell cell){
        Rectangle rect = field_table.getCellRect(cell.getLocation().x, cell.getLocation().y, true);

        return new Point(rect.x + Math.round(rect.width / 2f), rect.y + Math.round(rect.height / 2f));
    }

    public void addCharacter(Entity entity){
        synchronized (loadingMonitor) {
            try {
                while (fieldModel == null)
                    loadingMonitor.wait();
                creatures.add(entity);
                initEntity(entity);
            }catch (InterruptedException ignored){}
        }
    }

    public void initEntity(Entity entity){
        entity.getController().reboot();
        entity.getController().setDied(false);
        entity.getController().setIgnoreCollision(false);
        if(entity instanceof Player) {
            FieldCell cell = fieldModel.getRandomRoad();
            cell.setCellType(FieldCell.EMPTY_ROAD);
            fieldModel.setValueAt(cell, cell.getLocation().x, cell.getLocation().y);
            entity.getController().setCurrentCell(cell);
        }
        else if(entity instanceof Ghost){

            Rectangle spawner = fieldModel.getSpawnerRectangle();

            int x = (int)(Math.random() * (spawner.width - 4) + spawner.x + 2);
            int y = (int)(Math.random() * (spawner.height - 2) + spawner.y + 1);

            FieldCell cell = (FieldCell) fieldModel.getValueAt(y, x);
            entity.getController().setCurrentCell(cell);
        }
        entity.setLocation(getCellLocation(entity.getController().getCurrentCell()));
        entity.initSize((int) Math.round(1.0 / fieldModel.getColumnCount() * 80.0), (int) Math.round(1.0 / fieldModel.getRowCount() * 80.0));
        add(entity);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        float height = (float) getHeight() / (float) fieldModel.getRowCount();
        int height_remainder = (int)((height - (int) height) * fieldModel.getRowCount());

        for (int i = 0, j = fieldModel.getRowCount(), l = 1; i < j; i++, height_remainder--) {
            g.setColor(Color.red);
            if (height_remainder <= 0)
                l = 0;
            if(field_table.getRowHeight(i) != (int) height + l) {
                field_table.setRowHeight(i, (int) height + l);
            }
            field_table.setSize(getWidth(), getHeight());
        }

        for(Entity entity : creatures) {
            entity.revalidate();
            if(entity.getController().isIgnoredCollision())
                checkCollision(entity);
        }
    }

    public Player getPlayer(){
        for(JPanel entity : creatures){
            if(entity instanceof Player player)
                return player;
        }
        return null;
    }

    public FieldModel getFieldModel() {
        return fieldModel;
    }

    private int buff = 0;
    public void startGame(){
        for(Entity entity : creatures) {
            entity.getController().setLock(false);
            initEntity(entity);
            entity.getController().startMovement();
        }

        if(buffThread != null)
            buffThread.interrupt();

        buffThread = new Thread(() -> {
            try {
                while (true){
                    Thread.sleep(5000);
                    if(buff == 0 && Math.random() < 0.25) {
                        buff = (int) (Math.random() * 4) + 1;
                        setBuff(buff);
                    } else {
                        buff = 0;
                        setBuff(0);
                    }
                }
            }catch (InterruptedException ignored){}
        });
        buffThread.start();

        isRunning = true;
    }

    public void setBuff(int buff){
        for(Entity entity : creatures){
            if(entity instanceof Ghost && ((GhostController)entity.getController()).getStatus() != GhostController.SCARED) {
                GhostController controller = (GhostController) entity.getController();
                if (buff == 0) {
                    controller.setSpeed(0.6f);
                    controller.setIgnoreCollision(false);
                    controller.setStatus(GhostController.CALM);
                    entity.getBody().play_animation("Default");
                } else if (buff == 1) {
                    controller.setSpeed(1.2f);
                } else if (buff == 2) {
                    entity.getBody().play_animation("Transparent");
                    controller.setIgnoreCollision(true);
                } else {
                    if (buff == 3) {
                        controller.setStatus(GhostController.MAGIC);
                    } else if (buff == 4) {
                        entity.getBody().play_animation("Aggressive");
                        controller.setSpeed(1.2f);
                        controller.setStatus(GhostController.AGGRESSIVE);
                    }

                    new Thread(() -> {
                        try {
                            while (controller.getStatus() == GhostController.MAGIC || controller.getStatus() == GhostController.AGGRESSIVE) {
                                Thread.sleep(1000);
                                entity.setVisible(!entity.isVisible());
                            }
                            entity.setVisible(true);
                        } catch (InterruptedException ignored) {
                        }
                    }).start();
                }
            }
        }
    }

    public void restartGame() throws InterruptedException {
        for(Entity entity : creatures)
            entity.getController().kill();
        fieldModel.initRoads();
        fieldModel.initBuffPoints();
        Thread.sleep(1000);
        startGame();
    }

    public void pauseGame(){
        isRunning = false;
        if(buffThread != null)
            buffThread.interrupt();
        for(Entity entity : creatures) {
            entity.getController().stopMovement(true);
        }
    }

    public void endGame(){
        isRunning = false;
        if(buffThread != null)
            buffThread.interrupt();
        for(Entity entity : creatures) {
            entity.getController().kill();
        }

        parent.endGame();
    }

    public int getDiedGhosts(){
        int count = 0;
        for(Entity entity : creatures){
            if(entity instanceof Ghost && entity.getController().isDied())
                count++;
        }
        return count;
    }

    public void updateGhosts(){
        if(getDiedGhosts() >= 4){
            for(Entity entity : creatures){
                if(entity instanceof Ghost){
                    entity.getController().kill();
                    entity.getController().setLock(false);
                    initEntity(entity);
                    entity.getController().startMovement();
                    entity.getController().setIgnoreCollision(false);
                    ((GhostController)entity.getController()).setStatus(GhostController.CALM);
                    ((GhostController)entity.getController()).setRespawned(true);
                }
            }
        }
    }

    public void updateScore(int score) {
        Container parent = getParent();
        while (!(parent instanceof GamePanel) && parent != null)
            parent = parent.getParent();
        if(parent != null)
            ((GamePanel) parent).updateScore(score);

        if (getFieldModel().getPointRoads().size() == 0) {
            new Thread(() -> {
                try {
                    for (Entity entity : creatures) {
                        entity.getController().stopMovement(true);
                    }
                    Thread.sleep(1500);

                    for (Entity entity : creatures) {
                        entity.getController().kill();
                    }

                    Thread.sleep(2000);
                    setOpaque(true);
                    for (int i = 0; i < 3; i++) {
                        setBackground(Color.ORANGE);
                        Thread.sleep(750);
                        setBackground(Color.BLACK);
                        Thread.sleep(750);
                    }
                    setOpaque(false);
                    Thread.sleep(1000);
                    restartGame();
                } catch (InterruptedException ignored) {
                }
            }).start();
        }
    }

    public void updateLives(int lives){
        parent.updateLives(lives);

        if(parent.getHealthBar().getHealth() <= 0){
            endGame();
        }
    }

    public void death(){
        isRunning = false;
        new Thread(() -> {
            try {
                pauseGame();
                Thread.sleep(2000);
                restartGame();
                updateLives(((PlayerController)getPlayer().getController()).getLives());
            }catch (InterruptedException ignored){}
        }).start();
    }

    public void checkCollision(Entity entity){
        Rectangle e_bounds = entity.getBounds();
        for (Entity ent : creatures) {
            if (isRunning && !ent.equals(entity) && ent.getBounds().intersects(e_bounds)) {
                entity.getController().fireCollision(new CollisionEvent(ent));
                ent.getController().fireCollision(new CollisionEvent(entity));
            }
        }
    }

    public void makeScared(){
        setBuff(0);
        new Thread(() -> {
            try {
                for (Entity entity : creatures) {
                    if (entity instanceof Ghost && !entity.getController().isDied() && ((GhostController)entity.getController()).getStatus() != GhostController.SCARED)
                        ((GhostController) entity.getController()).setStatus(GhostController.SCARED);
                }
                Thread.sleep(7_000);
                for (Entity entity : creatures) {
                    if (entity instanceof Ghost && !entity.getController().isDied() && !((GhostController) entity.getController()).isRespawned())
                        ((GhostController) entity.getController()).setStatus(GhostController.CALM);
                }
            }catch (InterruptedException ignored){}
        }).start();
    }
}
