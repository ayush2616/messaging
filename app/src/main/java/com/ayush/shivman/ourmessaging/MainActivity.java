package com.ayush.shivman.ourmessaging;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.nfc.Tag;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
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
    HashMap<String,Integer > unreadMap;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewBrief=(ListView)findViewById(R.id.list_view_brief);
        briefArr=new ArrayList<>();
        map=new HashMap<>();
        unreadMap=new HashMap<>();
        mAuth=FirebaseAuth.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("GoogleSecure", Context.MODE_PRIVATE);
        String number=pref.getString("number",null);
        if(mAuth.getCurrentUser()==null)
        {
            Intent i=new Intent(this,FirstStart.class);
            startActivity(i);
            finish();
            return;
        }
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
        map.clear();
        unreadMap.clear();
        while(!c.isAfterLast())
        {
            String name=c.getString(c.getColumnIndex(UNAME));
            String sender=c.getString(c.getColumnIndex(SENDER));
            String message=c.getString(c.getColumnIndex(MESSAGE));
            String time=c.getString(c.getColumnIndex(TIME));
            int unreadT=c.getInt(c.getColumnIndex(SqlDatabase.UNREAD));
            Log.d("Cursor--aa-", "--"+name+","+sender+","+message+","+time);
            map.put(name,message);
            int temp=0;
            if(unreadT==1)
            {
                if(unreadMap.containsKey(name))
                    temp=unreadMap.get(name);
                temp++;
            }
            unreadMap.put(name,temp);
            /*BriefMessageModel briefTemp=new BriefMessageModel();
            briefTemp.setBriefMessage(message);
            briefTemp.setUserName(name);
            briefTemp.setImage(R.drawable.send);
            briefTemp.setunread(0);
            briefArr.add(briefTemp);*/
            c.moveToNext();
        }
        db.closeTable();
        for(Map.Entry<String,String > values : map.entrySet())
        {
            String name=values.getKey();
            BriefMessageModel briefTemp=new BriefMessageModel();
            briefTemp.setBriefMessage(map.get(name));
            briefTemp.setUserName(name);
            briefTemp.setImage(R.drawable.send);
            briefTemp.setunread(unreadMap.get(name));
            briefArr.add(briefTemp);
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.SignOut) {
            mAuth.signOut();
            listViewBrief.setVisibility(View.GONE);
        }

        return super.onOptionsItemSelected(item);
    }
}
