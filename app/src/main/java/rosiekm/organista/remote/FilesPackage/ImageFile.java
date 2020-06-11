package rosiekm.organista.remote.FilesPackage;

import android.os.Parcel;


public class ImageFile extends File {
    public ImageFile(String title, String path, String album) {
        super(title, path, album);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
