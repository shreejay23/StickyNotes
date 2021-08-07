package com.example.stickynotes;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.EditText;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class AlarmBroadcast extends BroadcastReceiver
{
    private String acctName;
    private String taskText;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        taskText = AddTask.getTaskText();

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Set task notification tone same as the current tone
        Uri tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Properties of notification
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
        if (acct != null) {
            acctName = acct.getGivenName();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"CHANNEL_ID");
        builder.setContentTitle("Task Reminder " + acctName);
        builder.setContentText(taskText);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setSound(tone);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent notifyIntent = new Intent(context,AddTask.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify("A",0,builder.build());
    }
}
