package me.hoen.android_mock_gps;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MockLocationProvider extends Service {
	private static final int GPS_START_INTERVAL = 3000;
	private ArrayList<Geoloc> data;
	private LocationManager locationManager;
	private String mockLocationProvider = LocationManager.NETWORK_PROVIDER;

	public static final int NOTIFICATION_ID = 42;

	public static final String SERVICE_STOP = "me.hoen.android_mock_gps.STOP";

	protected BroadcastReceiver stopServiceReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SERVICE_STOP)) {

				data.clear();
				MockLocationProvider.this.stopSelf();

				Log.d(MainActivity.TAG, "Mock GPS stopped");
			}
		}
	};

	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		super.onCreate();

		Log.d(MainActivity.TAG, "Mock GPS started");

		registerReceiver(stopServiceReceiver, new IntentFilter(SERVICE_STOP));
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.addTestProvider(mockLocationProvider, false, false,
				false, false, true, true, true, 0, 5);
		locationManager.setTestProviderEnabled(mockLocationProvider, true);

		new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... params) {
				initGpsLatLng();
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				displayStartNotification();
				handler.sendEmptyMessageDelayed(0, GPS_START_INTERVAL);
				super.onPostExecute(result);
			}
		}.execute("");
	}

	private void initGpsLatLng() {
		data = GeolocStore.getInstance().getGeolocs();
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (data.size() > 0) {
				int currentIndex = msg.what;
				sendLocation(currentIndex);

				int nextIndex = currentIndex + 1;
				if (data.size() == nextIndex) {
					nextIndex = currentIndex;
				}

				sendEmptyMessageDelayed(nextIndex, data.get(currentIndex).getDuration() * 1000);
			}
			super.handleMessage(msg);
		}
	};

	@SuppressLint("NewApi")
	private void sendLocation(int i) {
		Geoloc g = data.get(i);

		Location location = new Location(mockLocationProvider);
		location.setLatitude(g.getLatitude());
		location.setLongitude(g.getLongitude());
		location.setAltitude(g.getAltitude());
		location.setAccuracy(g.getAccuracy());
		location.setBearing(g.getBearing());
		location.setSpeed(g.getSpeed());
		location.setTime(g.getTime());
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
		}

		Log.d(MainActivity.TAG, "Set Position (" + i + ") : " + g.getLatitude()
				+ "," + g.getLongitude());
		locationManager.setTestProviderLocation(mockLocationProvider, location);
	}

	protected void displayStartNotification() {
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(getString(R.string.app_name))
				.setContentText(
						getString(R.string.start_mock_gps_notification_message))
				.setAutoCancel(false);

		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.putExtra("performAction", "stopMockGps");
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(contentIntent);

		Notification notification = mBuilder.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notificationManager.notify(NOTIFICATION_ID, notification);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(stopServiceReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}