package GamePanel;

import Entities.Ghost;
import Entities.Player;
import HighScorePanel.ScoreInfoObject;
import HighScorePanel.ScoreListObject;
import Root.*;
import Root.Window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

public class GamePanel extends JPanel implements WindowPanel {
    private final StatPanel highScoreTitle = new StatPanel(new ResizableImage("img\\Game\\HighScoreTitle.png"), 210, 100);
    private final StatPanel scoreTitle = new StatPanel(new ResizableImage("img\\Game\\Score.png"), 115, 100);
    private final StatPanel timeTitle = new TimePanel(new ResizableImage("img\\Game\\Time.png"), 115, 100);
    private final HealthBar healthBar;
    public final GameField game_field;
    private final Window window;
    private ScoreListObject scoreListObject;
    private final Thread timeThread = new Thread(() -> {
       try {
           while (true){
               updateTime();
               Thread.sleep(1000);
           }
       }catch (InterruptedException ignored){}
    });

    GamePanel(Window window, Dimension field_size){
        initPanel(window);
        this.window = window;

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new AbsoluteLayout());

        scoreTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        timeTitle.setAlignmentX(RIGHT_ALIGNMENT);

        header.add(highScoreTitle);
        header.add(scoreTitle);
        header.add(timeTitle);

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new AbsoluteLayout());

        healthBar = new HealthBar(3, window.getWidth()/2, 50);
        try {
            FileInputStream fis = new FileInputStream("score.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            scoreListObject = (ScoreListObject) ois.readObject();
            highScoreTitle.changeStat(scoreListObject.getHighScore());
        }catch (IOException | ClassNotFoundException ignored){
            scoreListObject = new ScoreListObject();
        }
        footer.add(healthBar);

        JPanel field_panel = new JPanel();
        field_panel.setOpaque(false);
        field_panel.setLayout(new AbsoluteLayout());

        game_field = new GameField(this, field_size);

        Player player = new Player(game_field);

        Ghost blinky = new Ghost(game_field, "Blinky");
        Ghost inky = new Ghost(game_field , "Inky");
        Ghost pinky = new Ghost(game_field, "Pinky");
        Ghost clyde = new Ghost(game_field, "Clyde");

        game_field.addCharacter(blinky);
        game_field.addCharacter(inky);
        game_field.addCharacter(pinky);
        game_field.addCharacter(clyde);
        game_field.addCharacter(player);
        field_panel.add(game_field);

        add(header, ExtendedBorderLayout.NORTH);
        add(field_panel, ExtendedBorderLayout.CENTER);
        add(footer, ExtendedBorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    if(scoreTitle.getStat() > 0)
                        endGame();
                }
            }
        });

        timeThread.start();
    }

    public void updateScore(int score){
        scoreTitle.changeStat(score);
        if(scoreTitle.getStat() > highScoreTitle.getStat())
            highScoreTitle.changeStat(scoreTitle.getStat() - highScoreTitle.getStat());
    }

    public void updateTime(){
        timeTitle.changeStat(1);
    }
    
    public void updateLives(int lives){
        healthBar.changeHealth(lives);
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public void endGame(){
        timeThread.interrupt();
        ((Container)((ExtendedBorderLayout)getLayout()).getLayoutComponent(BorderLayout.NORTH)).remove(highScoreTitle);
        ((Container)((ExtendedBorderLayout)getLayout()).getLayoutComponent(BorderLayout.NORTH)).remove(timeTitle);
        scoreTitle.setAlignmentX(CENTER_ALIGNMENT);
        ((Container)((ExtendedBorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER)).removeAll();
        ((Container)((ExtendedBorderLayout)getLayout()).getLayoutComponent(BorderLayout.SOUTH)).removeAll();
        setBorder(new EmptyBorder(0,0,0,0));

        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new AbsoluteLayout());
        EndGamePanel panel = new EndGamePanel(this);

        p.add(panel);
        add(p);
    }

    public void saveScore(String nickname){
        try {
            FileOutputStream fos = new FileOutputStream("score.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            ScoreInfoObject sio = new ScoreInfoObject(nickname, scoreTitle.getStat());
            scoreListObject.append(sio);
            oos.writeObject(scoreListObject);
            oos.close();
            fos.close();
        } catch (IOException ignored) {}
    }

    @Override
    public void initPanel(Window window) {
        setLayout(new ExtendedBorderLayout(window.getWidth(), window.getHeight(), 100, 50));
        setBorder(new EmptyBorder(0, 25, 0, 25));
        setOpaque(false);
    }

    public Window getWindow() {
        return window;
    }
}
