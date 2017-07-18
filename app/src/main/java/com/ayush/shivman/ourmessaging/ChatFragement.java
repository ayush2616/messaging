package com.ayush.shivman.ourmessaging;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ayush on 18/7/17.
 */

public class ChatFragement extends Fragment {

    String TAG="MAINACTIVITY--";
    ListView listViewBrief;
    ArrayList< BriefMessageModel> briefArr;
    BriefMeassageAdapter myAdapter;
    HashMap<String,String > map;
    HashMap<String,Integer > unreadMap;
    FirebaseAuth mAuth;
    View view;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.chat_fragement, container, false);
        listViewBrief=(ListView)view.findViewById(R.id.list_view_brief);
        briefArr=new ArrayList<>();
        map=new HashMap<>();
        unreadMap=new HashMap<>();
        mAuth= FirebaseAuth.getInstance();

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences pref = getActivity().getSharedPreferences("com.ayush.shivman.ourmessaging.SECRET", Context.MODE_PRIVATE);
        String number=pref.getString("number",null);
        SqlDatabase db=new SqlDatabase(getContext());
        db.open();
        Cursor c=db.getCursor(null);
        c.moveToFirst();
        briefArr.clear();
        map.clear();
        unreadMap.clear();
        while(!c.isAfterLast())
        {
            String name=c.getString(c.getColumnIndex(SqlDatabase.UNAME));
            String sender=c.getString(c.getColumnIndex(SqlDatabase.SENDER));
            String message=c.getString(c.getColumnIndex(SqlDatabase.MESSAGE));
            String time=c.getString(c.getColumnIndex(SqlDatabase.TIME));
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
        myAdapter=new BriefMeassageAdapter(briefArr,getActivity());
        listViewBrief.setAdapter(myAdapter);
    }

}
