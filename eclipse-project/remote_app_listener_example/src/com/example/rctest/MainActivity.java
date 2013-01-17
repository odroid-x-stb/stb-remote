package com.example.rctest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class MainActivity extends Activity {

	public TextView mTV;
	public TextView mUT;

	/* 1 */
    STBRemoteControlCommunication stbrcc;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTV = (TextView) findViewById(R.id.tv);
		mUT = (TextView) findViewById(R.id.ut);
		
		/* 2 */
		stbrcc = new STBRemoteControlCommunication(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		/* 3 */
		stbrcc.doBindService();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		/* 4 */
		stbrcc.doUnbindService();
	}

	/* 5 */
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    switch (keyCode) {
	        case KeyEvent.KEYCODE_MEDIA_PLAY: mTV.setText("play"); break;
	        case KeyEvent.KEYCODE_MEDIA_PAUSE: mTV.setText("pause"); break;
	        case KeyEvent.KEYCODE_MEDIA_STOP: mTV.setText("stop"); break;
	        case KeyEvent.KEYCODE_MEDIA_PREVIOUS: mTV.setText("previous"); break;
	        case KeyEvent.KEYCODE_MEDIA_NEXT: mTV.setText("next"); break;
	        case KeyEvent.KEYCODE_DPAD_UP: mTV.setText("up"); break;
	        case KeyEvent.KEYCODE_DPAD_DOWN: mTV.setText("down"); break;
	        case KeyEvent.KEYCODE_DPAD_LEFT: mTV.setText("left"); break;
	        case KeyEvent.KEYCODE_DPAD_RIGHT: mTV.setText("right"); break;
	        case KeyEvent.KEYCODE_DPAD_CENTER: mTV.setText("select"); break;
	        case KeyEvent.KEYCODE_HOME: mTV.setText("home"); break;
	        case KeyEvent.KEYCODE_BACK: mTV.setText("back"); break;
	        case KeyEvent.KEYCODE_MUTE: mTV.setText("sound mute"); break;
	        case KeyEvent.KEYCODE_PLUS: mTV.setText("sound +"); break;
	        case KeyEvent.KEYCODE_MINUS: mTV.setText("sound -"); break;
	    }
		return true;
	}
}
