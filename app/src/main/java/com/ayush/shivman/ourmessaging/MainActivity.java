package com.ayush.shivman.ourmessaging;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private static final String UNAME="USER";
    private static final String SENDER="SENDER";
    private static final String MESSAGE="MESSAGE";
    private static final String TIME="TIME";
    ListView listViewBrief;
    ArrayList< BriefMessageModel> briefArr;
    BriefMeassageAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewBrief=(ListView)findViewById(R.id.list_view_brief);
        briefArr=new ArrayList<>();
    }

    @Override
    protected void onResume() {
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
            Log.d("Cursor", "--"+name+","+sender+","+message+","+time);
            BriefMessageModel briefTemp=new BriefMessageModel();
            briefTemp.setBriefMessage(message);
            briefTemp.setUserName(name);
            briefTemp.setImage(R.drawable.send);
            briefTemp.setunread(0);
            briefArr.add(briefTemp);
            c.moveToNext();
        }
        db.closeTable();
        myAdapter=new BriefMeassageAdapter(briefArr,this);
        listViewBrief.setAdapter(myAdapter);
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
