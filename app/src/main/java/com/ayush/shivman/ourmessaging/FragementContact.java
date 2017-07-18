package com.ayush.shivman.ourmessaging;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by ayush on 18/7/17.
 */

public class FragementContact extends Fragment {
    ListView listView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v=inflater.inflate(R.layout.contact_fragement,container);
        listView=(ListView)v.findViewById(R.id.listview);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<BriefMessageModel> arr=new ArrayList<>();
        Cursor phones = getActivity().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        while (phones.moveToNext()) {

            String name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            BriefMessageModel obj=new BriefMessageModel();
            obj.setunread(0);
            obj.setImage(R.drawable.send);
            obj.setUserName(name);
            obj.setBriefMessage(phoneNumber);
            arr.add(obj);
        }
        phones.close();

        listView.setAdapter(new BriefMeassageAdapter(arr,getActivity()));


    }


    @Override
    public void onStart() {
        super.onStart();
    }
}
