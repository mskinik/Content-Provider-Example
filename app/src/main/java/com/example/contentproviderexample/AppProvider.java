package com.example.contentproviderexample;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

public class AppProvider extends ContentProvider {
    static final String AUTHORITY_NAME ="com.example.contentproviderexample.AppProvider";
    static final String URL="content://"+ AUTHORITY_NAME +"/contacts";
    static final Uri CONTENT_URI=Uri.parse(URL);
    static final String ID="id";
    static final String TITLE="title";
    static final String NAME="name";
    static final String NUMBER="number";

    static final UriMatcher URI_MATCHER;
    static {
        URI_MATCHER =new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY_NAME,"contacts",1);
    }
    private static Map<String,String> P_MAP;

    private SQLiteDatabase sqLiteDatabase;
    static final String DATABASE_NAME="Contacts.db";
    static final String TABLE_NAME="contacts";
    static final int DATABASE_VERSION=1;
    static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" (id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT NOT NULL, "+
            " name TEXT NOT NULL,number INTEGER NOT NULL );";
    public class DBHelper extends SQLiteOpenHelper {



        public DBHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);


        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(sqLiteDatabase);

        }
    }


    @Override
    public boolean onCreate() {
        DBHelper dbHelper=new DBHelper(getContext());
        sqLiteDatabase=dbHelper.getWritableDatabase();
        return sqLiteDatabase!=null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {


        SQLiteQueryBuilder sqLiteQueryBuilder= new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(TABLE_NAME);
        Cursor cursor=sqLiteQueryBuilder.query(sqLiteDatabase,null,s,strings1,null,null,null);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (URI_MATCHER.match(uri))
        {
            case 1:
                long back=sqLiteDatabase.insert(TABLE_NAME,"",contentValues);
                if(back>0)
                {
                    Uri insertUri=ContentUris.withAppendedId(uri,back);
                    getContext().getContentResolver().notifyChange(insertUri,null);
                    return insertUri;
                }

        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int backDelete=sqLiteDatabase.delete(TABLE_NAME,s,strings);
        getContext().getContentResolver().notifyChange(uri,null);

        return backDelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        int backUpdate =sqLiteDatabase.update(TABLE_NAME,contentValues,s,strings);

        if(backUpdate>0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
            return backUpdate;

        }
        return 0;

    }
}
