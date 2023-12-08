package Root;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnimatedImage extends ResizableImage {
    private final Map<String, Animation> animations;
    private int current_loop;
    private int frame_id;
    private boolean lock;

    private Thread animation;
    private Animation curr_animation;

    public AnimatedImage(){
        this.animations = new LinkedHashMap<>();
        reset();
    }

    public void addAnimation(String name, String animation_directory_path, int fps_delay){
        try {
            animations.put(name, new Animation(name, animation_directory_path, fps_delay));
            setImage(animations.get(((String)animations.keySet().toArray()[0])).getFrames().get(0));
        }catch (Animation.NoFramesFoundException ignored){}
    }

    public synchronized void play_animation(String anim_name){
        play_animation(anim_name, 0);
    }

    public synchronized void play_animation(String anim_name, int count){
        play_animation(anim_name, count, false);
    }

    public synchronized void play_animation(String anim_name, int count, boolean fill_last_frame){
        if(animations != null && animations.size() > 0) {
            stop_animation();
            animate(anim_name, count, fill_last_frame);
        }
    }

    public synchronized void pause_animation(){
        if(this.animation != null)
            this.animation.interrupt();
        this.animation = null;
    }

    public synchronized void stop_animation(){
        reset();
        pause_animation();
    }

    public void reset(){
        frame_id = 0;
        current_loop = 0;
        if(curr_animation != null) {
            super.image = curr_animation.getFrames().get(0);
        }
    }

    private void animate(String anim_name, int count, boolean fill_last_frame){
        curr_animation = animations.get(anim_name);

        this.animation = new Thread(()-> {
            try {
                while ((count == 0 || current_loop < count) && !(lock && frame_id == 0)) {
                    repaint();
                    if(++frame_id >= curr_animation.getFramesCount()){
                        if(!fill_last_frame)
                            frame_id = 0;
                        if (count != 0)
                            current_loop++;
                    }
                    if(frame_id < curr_animation.getFramesCount())
                        super.image = curr_animation.getFrames().get(frame_id);
                    Thread.sleep(curr_animation.getAnimationDelay());
                }
            } catch (InterruptedException ignored) {
            }
        });

        if(curr_animation != null)
            this.animation.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(super.image == null && animations != null && animations.size() > 0)
            setImage(animations.get((String) animations.keySet().toArray()[0]).getFrames().get(0));

        super.paintComponent(g);
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
