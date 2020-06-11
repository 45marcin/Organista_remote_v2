package rosiekm.organista.remote.FilesPackage;

import android.os.Parcelable;

public abstract class File implements Parcelable {
    String title;
    String path;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    String album;



    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File(String title, String path, String album) {
        this.title = title;
        this.path = path;
        this.album = album;
    }

}
