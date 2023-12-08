package HighScorePanel;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class HighScoreListModel extends AbstractListModel<ScoreInfoObject> {
    private ArrayList<ScoreInfoObject> list;

    HighScoreListModel(){
        try {
            FileInputStream fis = new FileInputStream("score.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = ((ScoreListObject) ois.readObject()).getObjects();
        }catch (IOException | ClassNotFoundException ignored){
            list = new ArrayList<>();
        }
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public ScoreInfoObject getElementAt(int index) {
        return list.get(index);
    }
}
