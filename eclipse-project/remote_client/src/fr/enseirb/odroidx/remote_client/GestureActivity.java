package fr.enseirb.odroidx.remote_client;

import fr.enseirb.odroidx.remote_client.R;
import fr.enseirb.odroidx.remote_client.gestures.GestureHandler;
import fr.enseirb.odroidx.remote_client.gestures.GestureManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureActivity extends Activity implements GestureHandler {

	private static final String TAG = GestureActivity.class.getSimpleName();
	
	private GestureManager gestureManager;
	private GestureDetector gestureDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture);
		gestureManager = new GestureManager(this);
		gestureDetector = new GestureDetector(getApplicationContext(), gestureManager);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean slidingLeft() {
		Log.d(TAG, "Gesture detected: Left");
		return false;
	}

	@Override
	public boolean slidingRight() {
		Log.d(TAG, "Gesture detected: Right");
		return false;
	}

	@Override
	public boolean slidingTop() {
		Log.d(TAG, "Gesture detected: Top");
		return false;
	}

	@Override
	public boolean slidingBottom() {
		Log.d(TAG, "Gesture detected: Bottom");
		return false;
	}

	@Override
	public boolean touchSingle() {
		Log.d(TAG, "Gesture detected: SinglePress");
		return false;
	}

	@Override
	public boolean touchDouble() {
		Log.d(TAG, "Gesture detected: DoublePress");
		return false;
	}
}
