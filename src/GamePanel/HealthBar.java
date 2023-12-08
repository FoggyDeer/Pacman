package GamePanel;

import Root.ResizablePanel;
import Root.ResizableImage;

import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HealthBar extends ResizablePanel {
    private int health;
    HealthBar(int health, int width, int height){
        super(width, height);
        this.health = health;

        setOpaque(false);
        FlowLayout layout = new FlowLayout();
        layout.setVgap(0);
        setLayout(layout);

        for(int i = 0; i < health; i++) {
            ResizableImage image = new ResizableImage("img\\Game\\HealthPoint.png");
            image.setBorder(new EmptyBorder(5,5,5,5));
            add(image);
        }
    }

   public void changeHealth(int health){
        if(health < 0) health = 0;

        if(health != this.health) {
            if (health < this.health)
                for (int i = this.health - health; i > 0; i--)
                    remove(0);

            else
                for (int i = health - this.health; i > 0; i--)
                    add(new ResizableImage("img\\Game\\HealthPoint.png"));
            repaint();
        }

        this.health = health;
    }

    public int getHealth() {
        return health;
    }
}
