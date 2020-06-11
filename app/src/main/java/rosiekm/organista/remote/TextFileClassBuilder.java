package rosiekm.organista.remote;

import java.util.ArrayList;

public class TextFileClassBuilder {
    private ArrayList<String> texts;
    private String refren;
    private int[] kolejnosc;
    private String title;
    private String genre;

    public TextFileClassBuilder setTexts(ArrayList<String> texts) {
        this.texts = texts;
        return this;
    }

    public TextFileClassBuilder setRefren(String refren) {
        this.refren = refren;
        return this;
    }

    public TextFileClassBuilder setKolejnosc(int[] kolejnosc) {
        this.kolejnosc = kolejnosc;
        return this;
    }

    public TextFileClassBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TextFileClassBuilder setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public TextFileClass createTextFileClass() {
        return new TextFileClass(texts, refren, kolejnosc, title, genre);
    }
}