package com.ayush.shivman.ourmessaging;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ayush on 19/7/17.
 */

public class ChatLayout extends AppCompatActivity {

    private Toolbar toolbar;
    ArrayList<ChatMessageModel> arr;
    ListView messages;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arr=new ArrayList<>();
        setContentView(R.layout.chat_layout);
        messages=(ListView)findViewById(R.id.messageList);
        String number=getIntent().getExtras().get("number").toString();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(number);
        SqlDatabase db=new SqlDatabase(this);
        db.open();
        Cursor c=db.getCursor(number);
        while (c.moveToNext())
        {
            ChatMessageModel obj=new ChatMessageModel();
            String msg=c.getString(c.getColumnIndex(SqlDatabase.MESSAGE));
            String date=c.getString(c.getColumnIndex(SqlDatabase.TIME));
            //String isMine=c.getString(c.getColumnIndex(SqlDatabase.SENDER));
            obj.setDate(new Date(Long.parseLong(date)));
            obj.setIsMine(false);
            obj.setText(msg);
            arr.add(obj);
        }
        c.close();
        db.closeTable();
        messages.setAdapter(new ChatMessageAdapter(arr,this));
    }
}
