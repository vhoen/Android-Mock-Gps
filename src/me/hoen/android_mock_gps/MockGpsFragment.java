package me.hoen.android_mock_gps;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MockGpsFragment extends Fragment implements LocationListener {
	protected LocationManager locationManager;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mock_gps, container,
				false);

		Button startMockGpsBt = (Button) rootView
				.findViewById(R.id.start_mock_gps);
		startMockGpsBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().startService(
						new Intent(getActivity(), MockLocationProvider.class));
			}
		});

		return rootView;
	}

	@Override
	public void onDestroy() {
		getActivity().sendBroadcast(new Intent("GP_PROVIDER_STOP_SERVICE"));
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
		super.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(MainActivity.TAG, "location: " + location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

}
