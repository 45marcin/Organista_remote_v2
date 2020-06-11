package rosiekm.organista.remote;

import java.util.ArrayList;

public class TextFileClass {
    public ArrayList<String> getTexts() {
        return texts;
    }

    public void setTexts(ArrayList<String> texts) {
        this.texts = texts;
    }

    public String getRefren() {
        return refren;
    }



    public void setRefren(String refren) {
        this.refren = refren;
    }

    public int[] getKolejnosc() {
        return kolejnosc;
    }

    public void setKolejnosc(int[] kolejnosc) {
        this.kolejnosc = kolejnosc;
    }

    ArrayList<String> texts;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    String refren;
    int[] kolejnosc;
    String title;

    public TextFileClass(ArrayList<String> texts, String refren, int[] kolejnosc, String title, String genre) {
        this.texts = texts;
        this.refren = refren;
        this.kolejnosc = kolejnosc;
        this.title = title;
        this.genre = genre;
    }

    String genre;
}
