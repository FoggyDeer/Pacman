package HighScorePanel;

import java.io.*;
import java.util.Objects;

public class ScoreInfoObject implements Serializable, Comparable<ScoreInfoObject> {
    String nickname;
    int score;
    int hashCode;

    public ScoreInfoObject(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
        this.hashCode = Objects.hash(nickname, score, Math.random());
    }

    @Override
    public String toString() {
        return nickname + ": " + score;
    }

    @Override
    public int compareTo(ScoreInfoObject o) {
        return Integer.compare(score, o.score) * (-1);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ScoreInfoObject obj1))
            return false;

        return nickname.equals(obj1.nickname) && score == obj1.score && hashCode == obj1.hashCode;
    }
}
