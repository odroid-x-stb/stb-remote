package fr.enseirb.odroidx.remote_client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import fr.enseirb.odroidx.remote_client.gestures.GestureHandler;
import fr.enseirb.odroidx.remote_client.gestures.GestureManager;

public class GestureActivity extends Activity implements GestureHandler {

	private static final String TAG = GestureActivity.class.getSimpleName();
	private static final int DISPLAY_TIME = 1000;
	
	private GestureManager gestureManager;
	private GestureDetector gestureDetector;
	private ImageView image;
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture);
		gestureManager = new GestureManager(this);
		gestureDetector = new GestureDetector(getApplicationContext(), gestureManager);
		image = (ImageView) findViewById(R.id.image);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean slidingLeft() {
		Log.d(TAG, "Gesture detected: Left");
		displayCallback(R.drawable.img_move_left);
		return true;
	}

	@Override
	public boolean slidingRight() {
		Log.d(TAG, "Gesture detected: Right");
		displayCallback(R.drawable.img_move_right);
		return true;
	}

	@Override
	public boolean slidingUp() {
		Log.d(TAG, "Gesture detected: Up");
		displayCallback(R.drawable.img_move_up);
		return true;
	}

	@Override
	public boolean slidingDown() {
		Log.d(TAG, "Gesture detected: Down");
		displayCallback(R.drawable.img_move_down);
		return true;
	}

	@Override
	public boolean singleTap() {
		Log.d(TAG, "Gesture detected: SingleTap");
		displayCallback(R.drawable.img_select);
		return false;
	}

	@Override
	public boolean doubleTap() {
		Log.d(TAG, "Gesture detected: DoubleTap");
		displayCallback(R.drawable.img_back);
		return false;
	}
	
	private void displayCallback(int resourceId) {
		handler.removeCallbacks(cancelDisplay);
		image.setBackgroundResource(resourceId);
		handler.postDelayed(cancelDisplay, DISPLAY_TIME);
	}
	
	private Runnable cancelDisplay = new Runnable() {
		@Override
		public void run() {
			image.setBackgroundResource(0);
		}
	};
}
