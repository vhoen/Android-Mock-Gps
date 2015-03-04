package me.hoen.android_mock_gps;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {
	public static final String TAG = "me.example";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Fragment f = new MockGpsFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().add(android.R.id.content, f, "home")
				.commit();
		fragmentManager.executePendingTransactions();

		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey("performAction")
				&& extras.getString("performAction").equals("stopMockGps")) {
			Intent i = new Intent(MockLocationProvider.SERVICE_STOP);
			sendBroadcast(i);
			NotificationManager notificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(MockLocationProvider.NOTIFICATION_ID);
		}
	}
}
