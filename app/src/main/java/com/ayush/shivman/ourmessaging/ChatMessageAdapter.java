package com.ayush.shivman.ourmessaging;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SHIVM@n on 7/11/2017.
 */

public class ChatMessageAdapter extends BaseAdapter {
   ArrayList<ChatMessageModel> al;
    Context ct;
    ChatMessageAdapter(ArrayList<ChatMessageModel> al,Context ct)
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
        ChatMessageModel cmm = (ChatMessageModel) getItem(position);
        Activity at = (Activity) ct;
        LayoutInflater lif = at.getLayoutInflater();
        View vi = lif.inflate(R.layout.singlechat,null);
        TextView msg = (TextView) vi.findViewById(R.id.message);
        msg.setText(cmm.getmsg());
        LinearLayout layout = (LinearLayout) vi
                .findViewById(R.id.children);
        LinearLayout parent_layout = (LinearLayout) vi
                .findViewById(R.id.parent);

        // if message is mine then align to right
        if (cmm.getIsMine()) {
            layout.setBackgroundResource(R.drawable.bubble2);
            parent_layout.setGravity(Gravity.RIGHT);
        }
        // If not mine then align to left
        else {
            layout.setBackgroundResource(R.drawable.bubble1);
            parent_layout.setGravity(Gravity.LEFT);
        }
    return vi;
    }
}
