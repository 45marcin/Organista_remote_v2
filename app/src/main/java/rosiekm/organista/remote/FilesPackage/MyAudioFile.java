package rosiekm.organista.remote.FilesPackage;

import android.annotation.SuppressLint;
import android.os.Parcel;

import java.util.ArrayList;

@SuppressLint("ParcelCreator")
public class MyAudioFile extends File {
    public ArrayList<Double> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(ArrayList<Double> stopPoints) {
        this.stopPoints.clear();
        this.stopPoints = stopPoints;
    }

    public ArrayList<Words> getWords() {
        return words;
    }

    public void setWords(ArrayList<Words> words) {
        this.words.clear();
        this.words = words;
    }

    ArrayList<Double> stopPoints;
    ArrayList<Words> words;
    int length;


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }



    public MyAudioFile(String title, String path, String album, int length) {
        super(title, path, album);
        this.length = length;
        this.stopPoints = new ArrayList<>();
        this.words = new ArrayList<>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
