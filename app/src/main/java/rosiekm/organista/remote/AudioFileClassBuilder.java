package rosiekm.organista.remote;

public class AudioFileClassBuilder {
    private String title;
    private String album;
    private Integer length;
    private Integer number;
    private String path;

    public AudioFileClassBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public AudioFileClassBuilder setAlbum(String album) {
        this.album = album;
        return this;
    }

    public AudioFileClassBuilder setLength(Integer length) {
        this.length = length;
        return this;
    }

    public AudioFileClassBuilder setNumber(Integer number) {
        this.number = number;
        return this;
    }

    public AudioFileClassBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public AudioFileClass createAudioFileClass() {
        return new AudioFileClass(title, album, length, path, number);
    }
}