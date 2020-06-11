package rosiekm.organista.remote.FilesPackage;


import android.os.Parcel;

public class VideoFile  extends File{
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    Integer length;
    public VideoFile(String title, String path, String album, Integer length) {
        super(title, path, album);
        this.length = length;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
