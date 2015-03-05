package me.hoen.android_mock_gps;

public class Geoloc {
	protected double latitude;
	protected double longitude;
	protected double altitude;

	protected float accuracy = 5;
	protected float bearing = 193;
	protected float speed = 1;
	protected Long time = System.currentTimeMillis();

	protected int duration = 15;

	public Geoloc(double latitude, double longitude, double altitude, int duration) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.duration = duration;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public float getBearing() {
		return bearing;
	}

	public void setBearing(float bearing) {
		this.bearing = bearing;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
