package com.example.stickynotes;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

import static androidx.core.content.ContextCompat.getSystemService;

public class AddTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskText;
    private Button newTaskSaveButton;
    private Button newTaskRemindButton;
    private TaskDatabase db;
    private String email, acctName;
    private static String taskText;
    private Calendar calendar;
    private long diff;
    public static AddTask newInstance() {
        return new AddTask();
    }

    public static String getTaskText()
    {
        return taskText;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager)getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    private void setNotification()
    {
        createNotificationChannel();
        /*NotificationManager manager = (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //Set task notification tone same as the current tone
        Uri tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Properties of notification
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            acctName = acct.getGivenName();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),"CHANNEL_ID");
        builder.setContentTitle("Task Reminder " + acctName);
        builder.setContentText(newTaskText.getText());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setSound(tone);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent notifyIntent = new Intent(getContext(),AddTask.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        try {
            notificationManager.wait(calendar.getTimeInMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        notificationManager.notify("A",0,builder.build());*/
        Intent notifyIntent = new Intent(getContext(),AlarmBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,notifyIntent,0);
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(getContext().ALARM_SERVICE);
        //Toast.makeText(getContext(),String.valueOf(diff),Toast.LENGTH_SHORT).show();
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + diff, pendingIntent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);
        newTaskRemindButton = getView().findViewById(R.id.newTaskButton1R);

        //Adding onClickListener to the reminder button
        newTaskRemindButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        taskText = String.valueOf(newTaskText.getText());
                        //Set time after day, month and year.
                        final int hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog =  new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int h, int m) {
                                //set the notification
                                //Toast.makeText(getContext(),String.valueOf(calendar.getTimeInMillis()),Toast.LENGTH_SHORT).show();
                                diff = Math.abs(h-hour) * 60 + Math.abs(m - minute);//hrs to mins
                                diff = diff * 60;//mins to sec
                                diff = diff * 1000;//sec to millsec
                                setNotification();
                                Toast.makeText(getContext(),"Reminder set",Toast.LENGTH_SHORT).show();
                            }
                        },hour,minute,false);
                        timePickerDialog.show();
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        db = new TaskDatabase(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String title = bundle.getString("title");
            newTaskText.setText(title);
            if (title.length() > 0) {
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                newTaskRemindButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
            }
        }

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                    newTaskRemindButton.setEnabled(false);
                    newTaskRemindButton.setTextColor(Color.GRAY);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    newTaskRemindButton.setEnabled(true);
                    newTaskRemindButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = newTaskText.getText().toString();
                if (finalIsUpdate) {
                    db.updateTask(bundle.getLong("id"), title);
                } else {
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                    if (acct != null) {
                        email = acct.getEmail();
                    }
                    Task task = new Task();
                    task.setTitle(title);
                    task.setStatus(0);
                    task.setEmail(email);
                    db.insertTask(task);
                    Toast.makeText(getContext(), "Task Saved", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }

}
