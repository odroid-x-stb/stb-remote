/**
 * Copyright (C) 2012 Enseirb, Sylvain Bilange, Fabien Fleurey <fabien@fleurey.com>
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
package fr.enseirb.odroidx.remote_server;

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
import android.widget.ImageView;
import android.widget.Toast;
import fr.enseirb.odroidx.remote_server.service.RemoteControlService;

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
	private boolean[] escape = {false, false};

	public STBRemoteControlCommunication (MainActivity a) {
		act = a;
	}

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "Message: " + msg.what);
			Log.d(TAG, "escape: " + escape[0] + ", " + escape[1]);
			if (msg.what == CMD__MOVE_DOWN) {
				escape[0] = true;
				Log.d(TAG, "escape1");
			} else if (msg.what == CMD__MOVE_UP && escape[0]) {
				escape[1] = true;
				Log.d(TAG, "escape2");
			} else if (msg.what == CMD__BACK && escape[1]) {
				Log.d(TAG, "escape!");
				act.finish();
			} else if (msg.what != RemoteControlService.MSG__PRINT_NEW_CLIENT_ACTION) {
				escape[0] = false;
				escape[1] = false;
			}
			switch (msg.what) {
				case RemoteControlService.MSG__PRINT_NEW_CLIENT:
					String client = msg.getData().getString("msg_value");
					act.add_client(client);
					break;
				case RemoteControlService.MSG__PRINT_NEW_CLIENT_ACTION:
					String client_action = msg.getData().getString("msg_value");
					act.add_client_action(client_action);
					break;
				case CMD__VIDEO_PLAY:
					clear_button_pressed();
					act.button_play.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__VIDEO_PAUSE:
					clear_button_pressed();
					act.button_pause.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__VIDEO_PREVIOUS:
					clear_button_pressed();
					act.button_previous.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__VIDEO_STOP:
					clear_button_pressed();
					act.button_stop.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__VIDEO_NEXT:
					clear_button_pressed();
					act.button_next.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__MOVE_UP:
					clear_button_pressed();
					act.button_up.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__MOVE_DOWN:
					clear_button_pressed();
					act.button_down.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__MOVE_LEFT:
					clear_button_pressed();
					act.button_left.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__MOVE_RIGHT:
					clear_button_pressed();
					act.button_right.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__SELECT:
					clear_button_pressed();
					act.button_select.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__BACK:
					clear_button_pressed();
					act.button_back.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__HOME:
					clear_button_pressed();
					act.button_home.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__SOUND_MUTE:
					clear_button_pressed();
					act.button_sound_mute.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__SOUND_PLUS:
					clear_button_pressed();
					act.button_sound_plus.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__SOUND_MINUS:
					clear_button_pressed();
					act.button_sound_minus.setBackgroundResource(R.color.orange_light);
					break;
				case CMD__USER_TEXT:
					clear_button_pressed();
					act.button_enter_text.setBackgroundResource(R.color.orange_light);
					String user_text = msg.getData().getString("msg_value");
					Toast.makeText(act, "USER_TEXT value: "+user_text, Toast.LENGTH_LONG).show();
					break;
				default:
					super.handleMessage(msg);		
			}
		}
	}

	private void clear_button_pressed() {
		for (ImageView iv : act.buttons) {
			iv.setBackgroundResource(R.color.blue_dark);
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