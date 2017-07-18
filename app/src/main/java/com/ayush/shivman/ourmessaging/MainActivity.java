package com.ayush.shivman.ourmessaging;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.nfc.Tag;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String UNAME="USER";
    private static final String SENDER="SENDER";
    private static final String MESSAGE="MESSAGE";
    private static final String TIME="TIME";
    String TAG="MAINACTIVITY--";
    ListView listViewBrief;
    ArrayList< BriefMessageModel> briefArr;
    BriefMeassageAdapter myAdapter;
    HashMap<String,String > map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewBrief=(ListView)findViewById(R.id.list_view_brief);
        briefArr=new ArrayList<>();
        map=new HashMap<>();
    }

    @Override
    protected void onResume() {
        int unread=0;
        super.onResume();
        if(!isMyServiceRunning(ChatService.class))
        {
            startService(new Intent(this,ChatService.class));
        }
        SqlDatabase db=new SqlDatabase(this);
        db.open();
        Cursor c=db.getCursor(null);
        c.moveToFirst();
        briefArr.clear();
        while(!c.isAfterLast())
        {
            String name=c.getString(c.getColumnIndex(UNAME));
            String sender=c.getString(c.getColumnIndex(SENDER));
            String message=c.getString(c.getColumnIndex(MESSAGE));
            String time=c.getString(c.getColumnIndex(TIME));
           // int unreadT=c.getInt(c.getColumnIndex(SqlDatabase.UNREAD));
            Log.d("Cursor", "--"+name+","+sender+","+message+","+time);
            Log.d(TAG, "onResume: "+"ssss");
            map.put(name,message);
            /*BriefMessageModel briefTemp=new BriefMessageModel();
            briefTemp.setBriefMessage(message);
            briefTemp.setUserName(name);
            briefTemp.setImage(R.drawable.send);
            briefTemp.setunread(0);
            briefArr.add(briefTemp);*/
            c.moveToNext();
        }
        Log.d(TAG, "onResume: "+map);
        db.closeTable();
        for(Map.Entry<String,String > values : map.entrySet())
        {
            Log.d(TAG, ""+values.getKey()+"  "+values.getValue());
        }
//        myAdapter=new BriefMeassageAdapter(briefArr,this);
//        listViewBrief.setAdapter(myAdapter);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
