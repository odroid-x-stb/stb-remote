package fr.enseirb.odroidx.remote_server.communication;

import android.util.Log;
import android.view.KeyEvent;

public class Commands {
	
	public static final String VIDEO_PLAY = "VIDEO_PLAY";
	public static final String VIDEO_PAUSE = "VIDEO_PAUSE";
	public static final String VIDEO_PREVIOUS = "VIDEO_PREVIOUS";
	public static final String VIDEO_REWIND = "VIDEO_REWIND";
	public static final String VIDEO_FORWARD = "VIDEO_FORWARD";
	public static final String VIDEO_NEXT = "VIDEO_NEXT";
	public static final String MOVE_UP = "MOVE_UP";
	public static final String MOVE_DOWN = "MOVE_DOWN";
	public static final String MOVE_LEFT = "MOVE_LEFT";
	public static final String MOVE_RIGHT = "MOVE_RIGHT";
	public static final String SELECT = "SELECT";
	
//	public static int MsgToKey (String msg) {
//		Log.v("ee", msg);
//		if (msg.equals(VIDEO_PLAY)) return KeyEvent.KEYCODE_MEDIA_PLAY;
//		else if (msg.equals(VIDEO_PAUSE)) return KeyEvent.KEYCODE_MEDIA_PAUSE;
//		else if (msg.equals(VIDEO_PREVIOUS)) return KeyEvent.KEYCODE_MEDIA_PREVIOUS;
//		else if (msg.equals(VIDEO_REWIND)) return KeyEvent.KEYCODE_MEDIA_REWIND;
//		else if (msg.equals(VIDEO_FORWARD)) return KeyEvent.KEYCODE_MEDIA_FAST_FORWARD;
//		else if (msg.equals(VIDEO_NEXT)) return KeyEvent.KEYCODE_MEDIA_NEXT;
//		else if (msg.equals(SELECT)) return KeyEvent.KEYCODE_DPAD_CENTER;
//		else if (msg.equals(MOVE_UP)) return KeyEvent.KEYCODE_DPAD_UP;
//		else if (msg.equals(MOVE_DOWN)) return KeyEvent.KEYCODE_DPAD_DOWN;
//		else if (msg.equals(MOVE_LEFT)) return KeyEvent.KEYCODE_DPAD_LEFT;
//		else if (msg.equals(MOVE_RIGHT)) return KeyEvent.KEYCODE_DPAD_RIGHT;
//		else return -1;
//	}
}
