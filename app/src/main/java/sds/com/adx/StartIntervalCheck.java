package sds.com.adx;

import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;


public class StartIntervalCheck  {

	final String TAG = "LoadADX";

	final int OFFSET = 150;

	static DatabaseHandler db;

	Context thisContext;

	private PendingIntent pendingIntent;
	private PendingIntent pendingIntentQuote;

	DownloadObj dob;

	public StartIntervalCheck(Context c) {

		Log.i(TAG,"Starting StartIntervalCheck");

		thisContext = c;
		dob = new DownloadObj(thisContext);
		db = new DatabaseHandler(thisContext);

        startPotCheck();

		Log.i(TAG,"Ending StartIntervalCheck");
	}

    public static DatabaseHandler getDB() {
		return db;
	}

	public void startPotCheck() {

			Log.d(TAG,"startPotCheck STARTED");
			Intent alarmIntent = new Intent(thisContext, CheckQuotes.class);
			pendingIntent = PendingIntent.getBroadcast(thisContext, 0, alarmIntent, 0);

			AlarmManager manager = (AlarmManager) thisContext.getSystemService(Context.ALARM_SERVICE);

//			int interval = 900000; // 15 mins
			int interval = 18000000; // 3 hours

			manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

	}

}

