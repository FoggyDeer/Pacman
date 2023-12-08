package GamePanel;

import Root.ResizableImage;

public class TimePanel extends StatPanel{

    TimePanel(ResizableImage title, int width, int height) {
        super(title, width, height);
    }

    public void changeStat(int val){
        this.stat += val;
        statLabel.setText(parse(this.stat));
    }

    private String parse(int val){
        int hours = val / 3600;
        int minutes = val / 60 - hours * 60;
        int seconds = val - minutes * 60 - hours * 60;

        String time = "";

        if(hours < 10)
            time += "0";
        time += hours + ":";

        if(minutes < 10)
            time += "0";
        time += minutes + ":";

        if(seconds < 10)
            time += "0";
        time += seconds;

        return time;
    }
}
