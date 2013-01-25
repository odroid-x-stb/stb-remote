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
package fr.enseirb.odroidx.remote_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import fr.enseirb.odroidx.remote_client.communication.Commands;
import fr.enseirb.odroidx.remote_client.communication.CommunicationService;
import fr.enseirb.odroidx.remote_client.communication.CommunicationServiceConnection;
import fr.enseirb.odroidx.remote_client.communication.STBCommunication;
import fr.enseirb.odroidx.remote_client.communication.STBCommunicationTask;
import fr.enseirb.odroidx.remote_client.communication.STBCommunicationTask.STBTaskListenner;
import fr.enseirb.odroidx.remote_client.gestures.GestureHandler;
import fr.enseirb.odroidx.remote_client.gestures.GestureManager;

public class GestureActivity extends Activity implements GestureHandler, STBTaskListenner {

	private static final String TAG = GestureActivity.class.getSimpleName();
	private static final int DISPLAY_TIME = 1000;
	
	private final CommunicationServiceConnection serviceConnection = new CommunicationServiceConnection();
	private final Handler handler = new Handler();
	
	private GestureManager gestureManager;
	private GestureDetector gestureDetector;
	private ImageView image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture);
		gestureManager = new GestureManager(this);
		gestureDetector = new GestureDetector(getApplicationContext(), gestureManager);
		image = (ImageView) findViewById(R.id.image);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.gesture_activity, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.change_view:
	        	Intent i = new Intent(getApplicationContext(), MainActivity.class);
	            startActivity(i);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(getApplicationContext(), CommunicationService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

	@Override
    protected void onStop(){
    	super.onStop();
    	if (serviceConnection.isBound()) {
    		unbindService(serviceConnection);
    		serviceConnection.setBound(false);
    	}
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean slidingLeft() {
		Log.d(TAG, "Gesture detected: Left");
		sendMessageToSTB(Commands.MOVE_LEFT);
		return true;
	}

	@Override
	public boolean slidingRight() {
		Log.d(TAG, "Gesture detected: Right");
		sendMessageToSTB(Commands.MOVE_RIGHT);
		return true;
	}

	@Override
	public boolean slidingUp() {
		Log.d(TAG, "Gesture detected: Up");
		sendMessageToSTB(Commands.MOVE_UP);
		return true;
	}

	@Override
	public boolean slidingDown() {
		Log.d(TAG, "Gesture detected: Down");
		sendMessageToSTB(Commands.MOVE_DOWN);
		return true;
	}

	@Override
	public boolean singleTap() {
		Log.d(TAG, "Gesture detected: SingleTap");
		sendMessageToSTB(Commands.SELECT);
		return true;
	}

	@Override
	public boolean doubleTap() {
		Log.d(TAG, "Gesture detected: DoubleTap");
		sendMessageToSTB(Commands.BACK);
		return true;
	}
	
	private void sendMessageToSTB(String msg) {
		if (serviceConnection.isBound()) {
			new STBCommunicationTask(this, serviceConnection.getSTBDriver()).execute(STBCommunication.REQUEST_COMMAND, msg);
		}
	}

	@Override
	public void requestSucceed(String request, String message, String command) {
		if (Commands.MOVE_LEFT.equals(command)) {
			displayCallback(R.drawable.img_move_left);
		} else if (Commands.MOVE_RIGHT.equals(command)) {
			displayCallback(R.drawable.img_move_right);
		} else if (Commands.MOVE_UP.equals(command)) {
			displayCallback(R.drawable.img_move_up);
		} else if (Commands.MOVE_DOWN.equals(command)) {
			displayCallback(R.drawable.img_move_down);
		} else if (Commands.SELECT.equals(command)) {
			displayCallback(R.drawable.img_select);
		} else if (Commands.BACK.equals(command)) {
			displayCallback(R.drawable.img_back);
		}
	}

	@Override
	public void requestFailed(String request, String message, String command) {
		// TODO Auto-generated method stub
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
