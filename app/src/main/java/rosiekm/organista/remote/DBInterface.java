package rosiekm.organista.remote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;

public class DBInterface extends SQLiteOpenHelper {
    public DBInterface(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    private static final String DATABASE_NAME = "db.db";
    private static final String AUDIO_TABLE_NAME = "audioFiles";
    private static final String MY_LIST_TABLE_NAME = "myAudio";
    private static final String AUDIO_COLUMN_PATH = "path";
    private static final String AUDIO_COLUMN_TITLE= "title";
    private static final String AUDIO_COLUMN_ALBUM ="album";
    private static final String AUDIO_COLUMN_LENGTH ="length";
    private static final String AUDIO_COLUMN_NUMBER ="no";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table if not exists audioFiles " +
                        "(path text, title text, album text, length number, no number)"
        );
        db.execSQL(
                "create table if not exists myAudio " +
                        "(path text, title text, album text, length number, no number)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS audioFiles");
        db.execSQL("DROP TABLE IF EXISTS myAudio");
        onCreate(db);
    }


    public void dropMainDB() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS audioFiles");
        onCreate(db);
    }


    public void dropMyListDB() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS myAudio");
        onCreate(db);
    }

    public boolean insertDevice (AudioFileClass audioFileClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(AUDIO_COLUMN_ALBUM, audioFileClass.getAlbum());
            contentValues.put(AUDIO_COLUMN_LENGTH, audioFileClass.getLength() );
            contentValues.put(AUDIO_COLUMN_PATH, audioFileClass.getPath());
            contentValues.put(AUDIO_COLUMN_TITLE, audioFileClass.getTitle());
            contentValues.put(AUDIO_COLUMN_NUMBER, audioFileClass.getNumber());
            db.insert(AUDIO_TABLE_NAME, null, contentValues);
        }
        catch (Exception e){
            Log.d("DB", e.toString());
        }

        return true;
    }


    public boolean insertDeviceMyList (AudioFileClass audioFileClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(AUDIO_COLUMN_ALBUM, audioFileClass.getAlbum());
            contentValues.put(AUDIO_COLUMN_LENGTH, audioFileClass.getLength() );
            contentValues.put(AUDIO_COLUMN_PATH, audioFileClass.getPath());
            contentValues.put(AUDIO_COLUMN_TITLE, audioFileClass.getTitle());
            contentValues.put(AUDIO_COLUMN_NUMBER, audioFileClass.getNumber());
            db.insert(MY_LIST_TABLE_NAME, null, contentValues);
        }
        catch (Exception e){
            Log.d("DB", e.toString());
        }

        return true;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, AUDIO_TABLE_NAME);
        return numRows;
    }

    public int numberOfRowsMyLists(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MY_LIST_TABLE_NAME);
        return numRows;
    }

    public ArrayList<AudioFileClass> getAllAudioFiles(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AudioFileClass> files = new ArrayList<>();

        Cursor res =  db.rawQuery( "select * from audioFiles", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            AudioFileClass tmp =  new AudioFileClassBuilder()
                    .setTitle(res.getString(res.getColumnIndex(AUDIO_COLUMN_TITLE)))
                    .setAlbum(res.getString(res.getColumnIndex(AUDIO_COLUMN_ALBUM)))
                    .setPath(res.getString(res.getColumnIndex(AUDIO_COLUMN_PATH)))
                    .setLength(res.getInt(res.getColumnIndex(AUDIO_COLUMN_LENGTH)))
                    .setNumber(res.getInt(res.getColumnIndex(AUDIO_COLUMN_NUMBER)))
                    .createAudioFileClass();
            files.add(tmp);
            try {
                res.moveToNext();
            }
            catch (Exception e){

            }
        }

        return  files;
    }

    public ArrayList<AudioFileClass> getAllAudioFilesMyLists(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AudioFileClass> files = new ArrayList<>();

        Cursor res =  db.rawQuery( "select * from myAudio", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            AudioFileClass tmp =  new AudioFileClassBuilder()
                    .setTitle(res.getString(res.getColumnIndex(AUDIO_COLUMN_TITLE)))
                    .setAlbum(res.getString(res.getColumnIndex(AUDIO_COLUMN_ALBUM)))
                    .setPath(res.getString(res.getColumnIndex(AUDIO_COLUMN_PATH)))
                    .setLength(res.getInt(res.getColumnIndex(AUDIO_COLUMN_LENGTH)))
                    .setNumber(res.getInt(res.getColumnIndex(AUDIO_COLUMN_NUMBER)))
                    .createAudioFileClass();
            tmp.setType((byte)0x1);
            files.add(tmp);
            try {
                res.moveToNext();
            }
            catch (Exception e){

            }
        }

        return  files;
    }

    public boolean removeFromMyAudio (AudioFileClass audioFileClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MY_LIST_TABLE_NAME,"path=? and album=?",new String[]{audioFileClass.path,audioFileClass.album});

        return true;
    }



}