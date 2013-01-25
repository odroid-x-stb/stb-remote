/**
 * Copyright (C) 2012 Sylvain Bilange, Fabien Fleurey <fabien@fleurey.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.enseirb.odroidx.remote_listenner_example;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;

public class STBRemoteControlCommunication {
	
	private static final String TAG = "STBRemoteControlCommunication"; 
	
	Messenger mService = null;
    boolean mIsBound;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    public static final int CMD__VIDEO_PLAY = 10;
	public static final int CMD__VIDEO_PAUSE = 11;
	public static final int CMD__VIDEO_STOP = 23;
	public static final int CMD__VIDEO_PREVIOUS = 12;
	public static final int CMD__VIDEO_NEXT = 15;
	public static final int CMD__MOVE_UP = 16;
	public static final int CMD__MOVE_DOWN = 17;
	public static final int CMD__MOVE_LEFT = 18;
	public static final int CMD__MOVE_RIGHT = 19;
	public static final int CMD__SELECT = 20;
	public static final int CMD__HOME = 21;
	public static final int CMD__BACK = 22;
	public static final int CMD__SOUND_MUTE = 24;
	public static final int CMD__SOUND_PLUS = 25;
	public static final int CMD__SOUND_MINUS = 26;
	public static final int CMD__USER_TEXT = 27;
    
	private static final int MSG__REGISTER_CLIENT = 1;
	private static final int MSG__UNREGISTER_CLIENT = 2;
	
	private MainActivity act;
	
	public STBRemoteControlCommunication (MainActivity a) {
		act = a;
	}
	
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	
        	switch (msg.what) {
	            case CMD__VIDEO_PLAY:
	            	press_key(KeyEvent.KEYCODE_MEDIA_PLAY);
	            	break;
	            case CMD__VIDEO_PAUSE:
	            	press_key(KeyEvent.KEYCODE_MEDIA_PAUSE);
	            	break;
	            case CMD__VIDEO_PREVIOUS:
	            	press_key(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
	            	break;
	            case CMD__VIDEO_STOP:
	            	press_key(KeyEvent.KEYCODE_MEDIA_STOP);
	            	break;
	            case CMD__VIDEO_NEXT:
	            	press_key(KeyEvent.KEYCODE_MEDIA_NEXT);
	            	break;
	            case CMD__MOVE_UP:
	            	press_key(KeyEvent.KEYCODE_DPAD_UP);
	            	break;
	            case CMD__MOVE_DOWN:
	            	press_key(KeyEvent.KEYCODE_DPAD_DOWN);
	            	break;
	            case CMD__MOVE_LEFT:
	            	press_key(KeyEvent.KEYCODE_DPAD_LEFT);
	            	break;
	            case CMD__MOVE_RIGHT:
	            	press_key(KeyEvent.KEYCODE_DPAD_RIGHT);
	            	break;
	            case CMD__SELECT:
	            	press_key(KeyEvent.KEYCODE_DPAD_CENTER);
	            	break;
	            case CMD__BACK:
	            	press_key(KeyEvent.KEYCODE_BACK);
	            	break;
	            case CMD__HOME:
	            	//press_key(KeyEvent.KEYCODE_HOME);
	            	try {
		            	Intent launchAppIntent = act.getPackageManager().getLaunchIntentForPackage("fr.enseirb.odroidx.home");
		            	act.startActivity(launchAppIntent);
	            	} 
	            	catch(Exception e) {
	            		Log.e(TAG, "Cannot lanch Home activity");
	            	}
	            	break;
				case CMD__SOUND_MUTE:
					press_key(KeyEvent.KEYCODE_VOLUME_MUTE);
					break;
				case CMD__SOUND_PLUS:
					press_key(KeyEvent.KEYCODE_VOLUME_UP);
					break;
				case CMD__SOUND_MINUS:
					press_key(KeyEvent.KEYCODE_VOLUME_DOWN);
					break;
				case CMD__USER_TEXT:
					String user_text = msg.getData().getString("msg_value");
					Log.v(TAG, "receiving from the STB: "+user_text);
					act.mUT.setText(user_text);
					break;
	            default:
	                super.handleMessage(msg);
            }
        }
    }
    
    private void press_key (final int keyCmd) {
    	Thread myThread = new Thread() {
            public void run() {
    	    	try {
    	    		Instrumentation inst = new Instrumentation();
    		        inst.sendKeyDownUpSync(keyCmd);
    		        Log.v(TAG, "key "+keyCmd+" pressed");
    	    	} catch (SecurityException e) {
    	    		Log.e(TAG, "Injecting Keys to other application is impossible due to non-system-permission");
    	    	} catch (Exception e) {
    	    		Log.e(TAG, "error while simulating key event :\n", e);
    	    	}
    	    }
    	};
    	myThread.start();
    }

	private void press_key_corresponding_to_string (String msg) {
		for (char ch: msg.toCharArray()) {
			switch(ch) {
				case '.': press_key(KeyEvent.KEYCODE_NUMPAD_DOT); break;
				case '0': press_key(KeyEvent.KEYCODE_NUMPAD_0); break;
				case '1': press_key(KeyEvent.KEYCODE_NUMPAD_1); break;
				case '2': press_key(KeyEvent.KEYCODE_NUMPAD_2); break;
				case '3': press_key(KeyEvent.KEYCODE_NUMPAD_3); break;
				case '4': press_key(KeyEvent.KEYCODE_NUMPAD_4); break;
				case '5': press_key(KeyEvent.KEYCODE_NUMPAD_5); break;
				case '6': press_key(KeyEvent.KEYCODE_NUMPAD_6); break;
				case '7': press_key(KeyEvent.KEYCODE_NUMPAD_7); break;
				case '8': press_key(KeyEvent.KEYCODE_NUMPAD_8); break;
				case '9': press_key(KeyEvent.KEYCODE_NUMPAD_9); break;
				case 'A': press_key(KeyEvent.KEYCODE_A); break;
				case 'B': press_key(KeyEvent.KEYCODE_B); break;
				case 'C': press_key(KeyEvent.KEYCODE_C); break;
				case 'D': press_key(KeyEvent.KEYCODE_D); break;
				case 'E': press_key(KeyEvent.KEYCODE_E); break;
				case 'F': press_key(KeyEvent.KEYCODE_F); break;
				case 'G': press_key(KeyEvent.KEYCODE_G); break;
				case 'H': press_key(KeyEvent.KEYCODE_H); break;
				case 'I': press_key(KeyEvent.KEYCODE_I); break;
				case 'J': press_key(KeyEvent.KEYCODE_J); break;
				case 'K': press_key(KeyEvent.KEYCODE_K); break;
				case 'L': press_key(KeyEvent.KEYCODE_L); break;
				case 'M': press_key(KeyEvent.KEYCODE_M); break;
				case 'N': press_key(KeyEvent.KEYCODE_N); break;
				case 'O': press_key(KeyEvent.KEYCODE_O); break;
				case 'P': press_key(KeyEvent.KEYCODE_P); break;
				case 'Q': press_key(KeyEvent.KEYCODE_Q); break;
				case 'R': press_key(KeyEvent.KEYCODE_R); break;
				case 'S': press_key(KeyEvent.KEYCODE_S); break;
				case 'T': press_key(KeyEvent.KEYCODE_T); break;
				case 'U': press_key(KeyEvent.KEYCODE_U); break;
				case 'V': press_key(KeyEvent.KEYCODE_V); break;
				case 'W': press_key(KeyEvent.KEYCODE_W); break;
				case 'X': press_key(KeyEvent.KEYCODE_X); break;
				case 'Y': press_key(KeyEvent.KEYCODE_Y); break;
				case 'Z': press_key(KeyEvent.KEYCODE_Z); break;
			}
		}
	}
    
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            try {
                Message msg = Message.obtain(null, MSG__REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
            	Log.e(TAG, "error :\n", e);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    public void doBindService() {
        act.bindService(new Intent("RemoteControlService.intent.action.Launch"), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
    
    public void doUnbindService() {
        if (mIsBound) {
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, MSG__UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    Log.e(TAG, "error:\n", e);
                }
            }
            act.unbindService(mConnection);
            mIsBound = false;
        }
    }
}