package Root;

import MenuPanel.MenuWindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WindowController implements KeyListener {
    private final Window window;

    public WindowController(Window window){
        this.window = window;
        window.requestFocus();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedForAll(e);
        if (!(window instanceof MenuWindow) && e.getModifiersEx() == (KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK) && KeyEvent.getKeyText(e.getKeyCode()).equals("Q")) {
            returnToMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void pressedForAll(KeyEvent event){
        for(Component listener : window.getAllKeyListeners()){
            if(!listener.equals(window) && listener.isFocusable()) {
                event.setSource(listener);
                listener.dispatchEvent(event);
            }
        }
    }

    public void returnToMenu(){
        new MenuWindow(window.getSize());
        window.close(false);
    }
}
