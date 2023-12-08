package Root;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.List;

public class Window extends JFrame {
    private final HashSet<Component> key_listeners = new HashSet<>();
    protected WindowController controller;

    public Window(int width, int height){
        setSize(width,height);
        setBackground(Color.BLACK);
        getContentPane().setBackground(Color.BLACK);
        setMinimumSize(new Dimension(200,200));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setFocusable(true);

        controller = new WindowController(this);
        addKeyListener(controller);

        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                requestFocus(false);
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
            }
        });
    }

    public void clear(){
        getContentPane().removeAll();
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void close(boolean exit){
        setVisible(false);
        for(WindowFocusListener listener :getWindowFocusListeners()){
            removeWindowFocusListener(listener);
        }
        if(exit)
            System.exit(0);
    }

    public void initKeyListeners(){
        initKeyListeners(this, new HashSet<>());
    }

    public void initKeyListeners(Container curr_container, HashSet<Container> containers){
        if(containers.add(curr_container)){
            for(Component comp : curr_container.getComponents()) {
                if(comp instanceof Container)
                    initKeyListeners(((Container) comp), containers);

                if(List.of(comp.getKeyListeners()).size() > 0)
                    key_listeners.add(comp);
            }
        }
    }

    public WindowController getController() {
        return controller;
    }

    public HashSet<Component> getAllKeyListeners() {
        return key_listeners;
    }
}
