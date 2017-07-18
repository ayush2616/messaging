package com.ayush.shivman.ourmessaging;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.*;

/**
 * Created by ayush on 18/7/17.
 */

public class FragementContact extends Fragment {
    ListView listView;
    FirebaseDatabase db=FirebaseDatabase.getInstance();
    DatabaseReference users=db.getReference().child("users");
    HashMap<String,Boolean> mapUser;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.contact_fragement, container, false);
        listView=(ListView)view.findViewById(R.id.listview);
        mapUser=new HashMap<>();

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    void updateUI()throws Exception
    {
        ArrayList<BriefMessageModel> arr=new ArrayList<>();
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
        while (phones.moveToNext()) {

            String name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(name.length()>17)
            {
                name=name.substring(0,17);
                name=name+"...";
            }

            String temp="";
            for(int i=0;i<number.length();i++)
            {
                if(number.charAt(i)>='0' && number.charAt(i)<='9')
                    temp+=number.charAt(i);
                if(temp.length()>=10)
                    temp=temp.substring(temp.length()-10);
            }
            if(temp.length()==10)
            {
                if(mapUser.get(temp)!=null)
                {
                    BriefMessageModel obj=new BriefMessageModel();
                    obj.setunread(0);
                    obj.setImage(R.drawable.send);
                    obj.setUserName(name);
                    obj.setBriefMessage(temp);
                    arr.add(obj);
                }
            }
        }
        phones.close();
        Collections.sort(arr,new BriefComparator());
        listView.setAdapter(new BriefMeassageAdapter(arr,getActivity()));
    }

    class BriefComparator implements Comparator<BriefMessageModel>
    {

        @Override
        public int compare(BriefMessageModel o1, BriefMessageModel o2) {
            return o1.getUserName().compareToIgnoreCase(o2.getUserName());
        }
    }
    public void askForContactPermission() throws Exception{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , 1212);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            1212);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                updateUI();
            }
        }
        else{
            updateUI();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapUser.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String number=ds.getKey();
                    String temp="";
                    for(int i=0;i<number.length();i++)
                    {
                        if(number.charAt(i)>='0' && number.charAt(i)<='9')
                            temp+=number.charAt(i);
                        if(temp.length()>=10)
                            temp=temp.substring(temp.length()-10);
                    }
                    mapUser.put(temp,true);
                }
                try {
                    askForContactPermission();
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults){
        switch (requestCode) {
            case 1212: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        updateUI();
                    }
                    catch (Exception e)
                    {
                        Log.d("error", "onRequestPermissionsResult: ");
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
