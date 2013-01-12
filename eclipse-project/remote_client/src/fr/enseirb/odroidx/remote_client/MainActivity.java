package fr.enseirb.odroidx.remote_client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import fr.enseirb.odroidx.remote_client.UI.IPAddressKeyListener;
import fr.enseirb.odroidx.remote_client.communication.Commands;
import fr.enseirb.odroidx.remote_client.communication.STBCommunication;

public class MainActivity extends Activity implements OnClickListener {

	private static final String PREFS_NAME = "IPSTORAGE";
	private static final String TAG = "MainActivity";
	private static final int COMMUNICATION_PORT = 2000;
	
	private boolean isConnectedToSTB = false;
    private STBCommunication STBCom = null;
    
    private EditText edIP;
	private LinearLayout buttons_layout;
	private ImageView button_connect;
	private ImageView button_play;
	private ImageView button_pause;
	private ImageView button_stop;
	private ImageView button_previous;
	private ImageView button_next;
	private ImageView button_up;
	private ImageView button_down;
	private ImageView button_right;
	private ImageView button_left;
	private ImageView button_select;
	private ImageView button_back;
	private ImageView button_home;
	
	private ArrayList<ImageView> buttons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
	    // loading view components
	    buttons_layout = (LinearLayout) findViewById(R.id.buttons_layout);
		edIP = (EditText) findViewById(R.id.EditTextIp);
	    button_connect = (ImageView) findViewById(R.id.button_connect);
	    button_play = (ImageView) findViewById(R.id.button_play);
	    button_pause = (ImageView) findViewById(R.id.button_pause);
	    button_stop = (ImageView) findViewById(R.id.button_stop);
	    button_previous = (ImageView) findViewById(R.id.button_previous);
	    button_next = (ImageView) findViewById(R.id.button_next);
	    button_up = (ImageView) findViewById(R.id.button_up);
	    button_down = (ImageView) findViewById(R.id.button_down);
	    button_left = (ImageView) findViewById(R.id.button_left);
	    button_right = (ImageView) findViewById(R.id.button_right);
	    button_select = (ImageView) findViewById(R.id.button_select);
	    button_back = (ImageView) findViewById(R.id.button_back);
	    button_home = (ImageView) findViewById(R.id.button_home);
	    
	    // setting listenners
	    buttons = new ArrayList<ImageView>();
	    buttons.add(button_connect);
	    buttons.add(button_play);
	    buttons.add(button_pause);
	    buttons.add(button_stop);
	    buttons.add(button_previous);
	    buttons.add(button_next);
	    buttons.add(button_up);
	    buttons.add(button_down);
	    buttons.add(button_left);
	    buttons.add(button_right);	
	    buttons.add(button_select);
	    buttons.add(button_back);
	    buttons.add(button_home);
	    
	    for (ImageView iv : buttons) {
	    	iv.setOnClickListener(this);
	    }
		
		// load Previous IP used
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    String ip = settings.getString("ip", null);
	    edIP.setText(ip);
	    edIP.setKeyListener(IPAddressKeyListener.getInstance());
	    
	    // hide buttons while not connected
	    buttons_layout.setVisibility(View.GONE);
	    
	    // initialize STBCom
	    STBCom = new STBCommunication();
	}
	
    @Override
    protected void onStop(){
    	super.onStop();

    	// save IP field for future executions
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString("ip", edIP.getText().toString());
    	editor.commit();
    }

	@Override
	public void onClick(View v) {
		Log.v(TAG, "click event on a button");
		v.setBackgroundResource(R.color.blue_light);
		
		if(v==button_play) sendMessageToSTB(Commands.VIDEO_PLAY);
		else if(v==button_pause) sendMessageToSTB(Commands.VIDEO_PAUSE);
		else if(v==button_stop) sendMessageToSTB(Commands.VIDEO_STOP);
		else if(v==button_previous) sendMessageToSTB(Commands.VIDEO_PREVIOUS);
		else if(v==button_next) sendMessageToSTB(Commands.VIDEO_NEXT);
		else if(v==button_up) sendMessageToSTB(Commands.MOVE_UP);
		else if(v==button_down) sendMessageToSTB(Commands.MOVE_DOWN);
		else if(v==button_left) sendMessageToSTB(Commands.MOVE_LEFT);
		else if(v==button_right) sendMessageToSTB(Commands.MOVE_RIGHT);
		else if(v==button_select) sendMessageToSTB(Commands.SELECT);
		else if(v==button_back) sendMessageToSTB(Commands.BACK);
		else if(v==button_home) sendMessageToSTB(Commands.HOME);
		else if(v==button_connect) {
			if (! isConnectedToSTB) connectToTheSTB();
			else disconnectFromTheSTB();
		}
	}
	
	private void connectToTheSTB() {
		new sendMessageToSTBAsyncTask(this).execute("connect");
	}
	
	private void disconnectFromTheSTB() {
		new sendMessageToSTBAsyncTask(this).execute("disconnect");
	}
	
	private void sendMessageToSTB(String msg) {
		new sendMessageToSTBAsyncTask(this).execute("command", msg);
	}
	
	private class sendMessageToSTBAsyncTask extends AsyncTask<String, String, Integer> {

		private MainActivity mParentActivity = null;
		
	    public sendMessageToSTBAsyncTask(MainActivity parentActivity) {
	        mParentActivity = parentActivity;
	    }
		
		@Override
		protected Integer doInBackground(String... params) {
			
			boolean success;
			String type = params[0];
			
			if (type.equals("connect")) {
				String ip = mParentActivity.edIP.getText().toString();
				success = mParentActivity.STBCom.stb_connect(ip, COMMUNICATION_PORT);
				
				if (! success) publishProgress("Error: Cannot connect to the STB (check your WiFi, IP, network configuration...)");
				else publishProgress("Connected to the STB", "connected");
			}
			else if (type.equals("disconnect")) {
				success = mParentActivity.STBCom.stb_disconnect();
				
				if (! success) publishProgress("Error: Cannot disconnect from the STB");
				else publishProgress("Disconnected from the STB", "disconnected");
			}
			else if (type.equals("command")) {
				String cmd = params[1];
				success =  mParentActivity.STBCom.stb_send(cmd);
				
				if (! success) publishProgress("Error: sending command to the STB failed");
			}
			
			return null;
		}
		
		protected void onProgressUpdate(String... params) {
			
			Toast.makeText(mParentActivity.getApplicationContext(), params[0], Toast.LENGTH_LONG).show();
			
			if (params.length == 2) {
				if (params[1].equals("connected")) {
					mParentActivity.buttons_layout.setVisibility(View.VISIBLE);
					mParentActivity.isConnectedToSTB = true;
				} else {
					mParentActivity.buttons_layout.setVisibility(View.GONE);
					mParentActivity.isConnectedToSTB = false;
				}
			}
	    }
		
		protected void onPostExecute(Integer result) {
			
			for (ImageView iv : buttons) {
		    	iv.setBackgroundResource(R.color.black);
		    }
		}
	}
}
