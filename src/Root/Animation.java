package Root;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Animation {
    private final String name;
    private final List<Image> frames;
    private final int fps_delay;

    public Animation(String name, String animation_folder_path, int fps_delay) throws NoFramesFoundException {
        this.name = name;
        List<Image> frames = new ArrayList<>();

        File path = new File(animation_folder_path);
        File[] frames_files = path.listFiles();

        if(frames_files != null) {
            for (File file : frames_files)
                frames.add(new ImageIcon(file.getPath()).getImage());
        }
        else
            throw new NoFramesFoundException();

        this.frames = frames;
        this.fps_delay = fps_delay;
    }

    public String getName() {
        return name;
    }

    public List<Image> getFrames() {
        return frames;
    }

    public int getFramesCount(){
        return frames.size();
    }

    public int getAnimationDelay() {
        return fps_delay;
    }

    public static class NoFramesFoundException extends Exception{
        NoFramesFoundException(){
            super("No frames found in directory!");
        }
    }
}
