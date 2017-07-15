package com.ayush.shivman.ourmessaging;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.BoolRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ayush on 12/7/17.
 */

public class ChatService extends Service {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference db_message=db.getReference().child("messages").child("ayush");
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("Asasas", "onCreate: ");
        super.onCreate();

        db_message.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SqlDatabase db=new SqlDatabase(getApplicationContext());
                db.open();
                Log.d("asasas", "onDataChange: "+dataSnapshot);
                for(DataSnapshot ds1:dataSnapshot.getChildren())
                {
                    for(DataSnapshot ds2:ds1.getChildren())
                    {
                        generateNotification(getApplicationContext(),"Message From "+ds1.getKey());
                        Log.d("message", "from "+ds1.getKey()+" msg"+ds2.child("message").getValue());
                        String message=ds2.child("message").getValue().toString();
                        String time=ds2.getKey();
                        String sender=ds2.child("sender").getValue().toString();
                        String name=ds1.getKey();
                        Map<String,String> mp=new HashMap<String, String>();
                        mp.put("message",message);
                        mp.put("time",time);
                        mp.put("sender",sender);
                        mp.put("name",name);
                        Log.d("MAP", "\n\n"+mp.toString()+"\n\n");
                        db_message.child(ds1.getKey()).child(ds2.getKey()).removeValue();
                        db.createEntry(mp);

                        /*new AsyncTask<Map<String,String>,Void,Boolean>()
                        {

                            @Override
                            protected Boolean doInBackground(Map<String, String>... params) {
                                SqlDatabase db=new SqlDatabase(getApplicationContext());
                                db.open();
                                db.createEntry(params[0]);
                                db.closeTable();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Boolean aBoolean) {
                                super.onPostExecute(aBoolean);
                            }
                        }.execute(mp);
                        db_message.child(ds1.getKey()).child(ds2.getKey()).removeValue();*/
                    }
                }
                db.closeTable();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void generateNotification(Context context, String Text) {
        String message =Text;
        //Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.ringing);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("New Message").setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(122, notification);
    }
}
