package com.ayush.shivman.ourmessaging;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SHIVM@n on 7/11/2017.
 */

public class BriefMeassageAdapter extends BaseAdapter {
    ArrayList<BriefMessageModel> al;
    Context ct;
    BriefMeassageAdapter(ArrayList<BriefMessageModel> al,Context ct)
    {
        this.al=al;
        this.ct=ct;
    }
    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int position) {
        return al.get(position);
    }

    @Override
    public long getItemId(int position) {
    return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BriefMessageModel bmm = (BriefMessageModel) getItem(position);
        Activity at = (Activity) ct;
        LayoutInflater lif = at.getLayoutInflater();
        View v1 = lif.inflate(R.layout.briefmessagelayout,null);
        ImageView iv = (ImageView) v1.findViewById(R.id.iv);
        TextView tv1 = (TextView) v1.findViewById(R.id.tv1);
        TextView tv2 = (TextView) v1.findViewById(R.id.tv2);
        TextView tv3 = (TextView) v1.findViewById(R.id.tv3);
        iv.setImageResource(bmm.getImage());
        tv1.setText(bmm.getUserName());
        tv2.setText(bmm.getBriefMessage());
        tv3.setText(bmm.getunread()+"");
        return v1;
    }
}
