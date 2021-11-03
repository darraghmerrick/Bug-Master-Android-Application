package com.google.developer.bugmaster.reminders;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.developer.bugmaster.MainActivity;
import com.google.developer.bugmaster.QuizActivity;
import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.data.DatabaseManager;
import com.google.developer.bugmaster.data.Insect;
import com.google.developer.bugmaster.utils.QuizUtils;

import java.util.ArrayList;
import java.util.Random;

import static com.google.developer.bugmaster.QuizActivity.EXTRA_ANSWER;

public class ReminderService extends IntentService {

    private static final String TAG = ReminderService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    public ReminderService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Quiz reminder event triggered");

        //Present a notification to the user
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Create action intent
        Intent action = new Intent(this, QuizActivity.class);

        //TODO: Add data elements to quiz launch

        Cursor cursor = DatabaseManager.getInstance(this).queryAllInsects(MainActivity.ORIGINAL_ORDER);
        ArrayList<Insect> insects = new ArrayList<>();
        cursor.moveToFirst();
        insects.add(new Insect(cursor));

        while (cursor.moveToNext()) {
            insects.add(new Insect(cursor));
        }

        insects = QuizUtils.shuffleArrayList(insects);
        insects = QuizUtils.shrinkTo(insects, QuizActivity.ANSWER_COUNT);

        Insect answer = insects.get(new Random(System.nanoTime()).nextInt(insects.size()));
        action.putExtra(QuizActivity.EXTRA_INSECTS, insects);
        action.putExtra(EXTRA_ANSWER, answer);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(QuizActivity.class);
        stackBuilder.addNextIntent(action);

        //End of my code

        PendingIntent operation =
                PendingIntent.getActivity(this, 0, action, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification note = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_bug_empty)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, note);
        AlarmReceiver.scheduleAlarm(this);

    }
}
