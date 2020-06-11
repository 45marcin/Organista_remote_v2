package rosiekm.organista.remote;

public class StringUtils {
    public static String getTimeInString(int time){
        String timeString = "";
        int minutes = time/60;
        int seconds = time%60;

        timeString = String.valueOf(minutes) + ":";
        if (seconds < 10){
            timeString = timeString + "0";
        }

        timeString = timeString + String.valueOf(seconds);
        return timeString;
    }
}
