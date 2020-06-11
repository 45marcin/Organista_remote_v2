package rosiekm.organista.remote;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class AudioFileClass implements Parcelable, Cloneable {
    String title;
    String album;
    Integer length;
    Integer number;
    String path;
    byte type = 0x0;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    protected AudioFileClass(Parcel in) {
        title = in.readString();
        album = in.readString();
        if (in.readByte() == 0) {
            length = null;
        } else {
            length = in.readInt();
        }
        path = in.readString();
    }

    public static final Creator<AudioFileClass> CREATOR = new Creator<AudioFileClass>() {
        @Override
        public AudioFileClass createFromParcel(Parcel in) {
            return new AudioFileClass(in);
        }

        @Override
        public AudioFileClass[] newArray(int size) {
            return new AudioFileClass[size];
        }
    };

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

    public Integer getLength() {
        return length;
    }

    public Integer getNumber() {
        return number;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AudioFileClass(String title, String album, Integer length, String path, Integer number) {
        this.title = title;
        this.album = album;
        this.length = length;
        this.path = path;
        this.number = number;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(album);
        if (length == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(length);
        }
        dest.writeString(path);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AudioFileClass cloned = (AudioFileClass)super.clone();
        // the above is applicable in case of primitive member types,
        // however, in case of non primitive types
        // cloned.setNonPrimitiveType(cloned.getNonPrimitiveType().clone());
        return cloned;
    }

}
