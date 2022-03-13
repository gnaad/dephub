package com.dephub.android.favorite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favouritetable";
    private static final String TABLE_NAME = "favoritetable";
    private static final String KEY_ID = "id";

    private static final String KEY_DEPID = "depid";
    private static final String KEY_DEPENDENCYNAME = "name";
    private static final String KEY_DEVELOPERNAME = "devname";
    private static final String KEY_GITHUBLINK = "url";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_LICENSE = "license";
    private static final String KEY_LICENSELINK = "lurl";
    private static final String KEY_CARDBACKGROUND = "color";
    private static final String KEY_YOUTUBELINK = "yurl";

    SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = " CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DEPID + " TEXT UNIQUE,"
                + KEY_DEPENDENCYNAME + " TEXT,"
                + KEY_DEVELOPERNAME + " TEXT,"
                + KEY_GITHUBLINK + " TEXT,"
                + KEY_FULLNAME + " TEXT,"
                + KEY_LICENSE + " TEXT,"
                + KEY_LICENSELINK + " TEXT,"
                + KEY_CARDBACKGROUND + " TEXT,"
                + KEY_YOUTUBELINK + " TEXT " + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        db.close( );
    }

    public boolean insertData(String depid,String dependencyname,String developername,String githublink,String cardbackground,String fullname,String license,String licenselink,String youtubelink) {
        sqLiteDatabase = this.getWritableDatabase( );
        ContentValues values = new ContentValues( );
        values.put(KEY_DEPID,depid);
        values.put(KEY_DEPENDENCYNAME,dependencyname);
        values.put(KEY_DEVELOPERNAME,developername);
        values.put(KEY_GITHUBLINK,githublink);
        values.put(KEY_FULLNAME,fullname);
        values.put(KEY_LICENSE,license);
        values.put(KEY_LICENSELINK,licenselink);
        values.put(KEY_CARDBACKGROUND,cardbackground);
        values.put(KEY_YOUTUBELINK,youtubelink);
        long result = sqLiteDatabase.insert(TABLE_NAME,null,values);
        sqLiteDatabase.close( );
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getFavorite() {
        sqLiteDatabase = this.getWritableDatabase( );
        return sqLiteDatabase.rawQuery(" SELECT * FROM " + TABLE_NAME,null);
    }

    public Integer deleteFavorite(String depid) {
        sqLiteDatabase = this.getWritableDatabase( );
        return sqLiteDatabase.delete(TABLE_NAME,"depid=?",new String[]{depid});
    }
}
