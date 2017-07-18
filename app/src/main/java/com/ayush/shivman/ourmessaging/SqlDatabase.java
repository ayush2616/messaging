package com.ayush.shivman.ourmessaging;

/**
 * Created by ayush on 12/7/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;

/**
 * Created by ayush on 11/7/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;

/**
 * Created by Ayush on 1/28/2017.
 */
/*
public class Database{
    SQLiteDatabase db;
    public Database(int t,int c1,int c2,int c3,int c4,int c5,int c6,int c7)
    {
        String s="insert into ";
    }

}*/
public class SqlDatabase {

     static final String DATABASE_NAME = "MESSAGE";
     static final String UNAME="USER";
     static final String SENDER="SENDER";
     static final String MESSAGE="MESSAGE";
     static final String TIME="TIME";
     static final String UNREAD="UNREAD";
     static final String UID="UID";
     static final String MESSAGEID="MESSAGEID";


    private static final String TABLE = "CHATDB";
    private static final String USERS_TABLE = "USERS";

    private static final int DATABASE_VERSION = 20141;

    private DbHelper mHelper;

    private final Context mContext;

    private SQLiteDatabase mDatabase;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        //Set up database here
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                    //Column name     Type of variable
                    UNAME + " VARCHAR(30), " +
                    SENDER + " VARCHAR(5), " +
                    MESSAGE + " VARCHAR(200), " +
                    UNREAD + " INTEGER DEFAULT 1, " +
                    MESSAGEID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TIME + " NUMBER);");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " (" +
                    //Column name     Type of variable
                    TIME + " NUMBER, " +
                    UID + " VARCHAR(40), " +
                    UNAME + " VARCHAR(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);

            onCreate(db);
        }

    }

    public SqlDatabase (Context c) {
        mContext = c;
    }

    public SqlDatabase open() throws SQLException {
        //Set up the helper with the context
        mHelper = new DbHelper (mContext);
        //Open the database with our helper
        mDatabase = mHelper.getWritableDatabase();
        return this;
    }
    public void trunc()
    {
        mDatabase.execSQL("delete from "+USERS_TABLE);
    }
    public void createEntry(Map<String,String> map) {
        ContentValues cv = new ContentValues();
        cv.put(UNAME,map.get("name"));
        cv.put(SENDER,map.get("sender"));
        cv.put(MESSAGE,map.get("message"));
        cv.put(TIME,map.get("time"));

        //do somthing
        mDatabase.insert(TABLE, null, cv);

    }
    public void closeTable()
    {
        if(mDatabase!=null && mHelper!=null) {
            mHelper.close();
            mDatabase.close();
        }
    }
    public Cursor getCursor(String user)
    {
        String sql;
        if(user==null)
            sql="select * from "+TABLE;
        else
            sql="select * from "+TABLE+" where "+UNAME+"='"+user+"' ORDER BY "+TIME;
        Cursor c=mDatabase.rawQuery(sql,null);
        return c;
    }
}


