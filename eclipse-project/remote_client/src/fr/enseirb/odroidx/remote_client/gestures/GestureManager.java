package fr.enseirb.odroidx.remote_client.gestures;

import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class GestureManager implements OnGestureListener {

	private static final int SLIDE_MIN_DISTANCE = 120;
    private static final int SLIDE_VELOCITY_THRESHOLD = 200;
	
	private GestureHandler receiver;
	
	public GestureManager(GestureHandler receiver) {
		this.receiver = receiver;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (e1.getX() - e2.getX() > SLIDE_MIN_DISTANCE && Math.abs(velocityX) > SLIDE_VELOCITY_THRESHOLD) {
			receiver.slidingLeft();
		} else if (e2.getX() - e1.getX() > SLIDE_MIN_DISTANCE && Math.abs(velocityX) > SLIDE_VELOCITY_THRESHOLD) {
			receiver.slidingRight();
		} else if (e1.getY() - e2.getY() > SLIDE_MIN_DISTANCE && Math.abs(velocityY) > SLIDE_VELOCITY_THRESHOLD) {
			receiver.slidingTop();
		} else if (e2.getY() - e1.getY() > SLIDE_MIN_DISTANCE && Math.abs(velocityY) > SLIDE_VELOCITY_THRESHOLD) {
			receiver.slidingBottom();
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
