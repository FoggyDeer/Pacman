package HighScorePanel;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoreListObject implements Serializable {
    private final ArrayList<ScoreInfoObject> objects = new ArrayList<>();

    public void append(ScoreInfoObject object){
        objects.add(object);
        objects.sort(null);
    }

    public int getHighScore(){
        int max = 0;
        for(ScoreInfoObject object : objects)
            if(max < object.score)
                max = object.score;
        return max;
    }

    public ArrayList<ScoreInfoObject> getObjects() {
        return objects;
    }
}
