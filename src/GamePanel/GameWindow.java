package GamePanel;

import Root.Window;

import java.awt.*;


public class GameWindow extends Window {
    private GamePanel gamePanel;
    private final Thread loading;

    public GameWindow(Dimension size, Dimension field_size){
        this(size.width, size.height, field_size);
    }

    public GameWindow(int width, int height, Dimension field_size){
        super(width, height);

        LoadingScreen loadingScreen = new LoadingScreen(this);

        loadingScreen.startLoading();

        loading = new Thread(() -> {
            synchronized (GameField.loadingMonitor){
                try {
                    gamePanel = new GamePanel(this, field_size);
                    add(gamePanel);
                    Thread.sleep(1000);
                    loadingScreen.stopLoading();

                    gamePanel.game_field.startGame();
                    initKeyListeners();
                }catch (InterruptedException ignored){}
            }
        });
        loading.start();
        setAutoRequestFocus(false);
    }

    @Override
    public void close(boolean exit) {
        loading.interrupt();
        super.close(exit);
    }
}
