package fr.enseirb.odroidx.remote_client;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import fr.enseirb.odroidx.remote_client.UI.IPAddressKeyListener;
import fr.enseirb.odroidx.remote_client.communication.Commands;
import fr.enseirb.odroidx.remote_client.communication.CommunicationService;
import fr.enseirb.odroidx.remote_client.communication.CommunicationServiceConnection;
import fr.enseirb.odroidx.remote_client.communication.CommunicationServiceConnection.ComServiceListenner;
import fr.enseirb.odroidx.remote_client.communication.STBCommunication;
import fr.enseirb.odroidx.remote_client.communication.STBCommunicationTask;
import fr.enseirb.odroidx.remote_client.communication.STBCommunicationTask.STBTaskListenner;

public class MainActivity extends Activity implements OnClickListener, STBTaskListenner, ComServiceListenner {

	private static final String PREFS_NAME = "IPSTORAGE";
	private static final String TAG = "MainActivity";
	
	private CommunicationServiceConnection serviceConnection;
    
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
	private ImageView button_sound_mute;
	private ImageView button_sound_plus;
	private ImageView button_sound_minus;
	private ImageView button_enter_text;
	
	private ArrayList<ImageView> buttons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		serviceConnection = new CommunicationServiceConnection(this);
		
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
	    button_sound_mute = (ImageView) findViewById(R.id.button_sound_mute);
	    button_sound_plus = (ImageView) findViewById(R.id.button_sound_plus);
	    button_sound_minus = (ImageView) findViewById(R.id.button_sound_minus);
	    button_enter_text = (ImageView) findViewById(R.id.button_enter_text);
	    
	    // setting listeners
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
	    buttons.add(button_sound_mute);
	    buttons.add(button_sound_plus);
	    buttons.add(button_sound_minus);
	    buttons.add(button_enter_text);
	    
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
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.main_activity, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.change_view:
	        	Intent i = new Intent(getApplicationContext(), GestureActivity.class);
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
    	// save IP field for future executions
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString("ip", edIP.getText().toString());
    	editor.commit();
    	// Unbind the service
    	if (serviceConnection.isBound()) {
    		unbindService(serviceConnection);
    		serviceConnection.setBound(false);
    	}
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
		else if(v==button_sound_mute) sendMessageToSTB(Commands.SOUND_MUTE);
		else if(v==button_sound_plus) sendMessageToSTB(Commands.SOUND_PLUS);
		else if(v==button_sound_minus) sendMessageToSTB(Commands.SOUND_MINUS);
		else if(v==button_connect) {
			if (!serviceConnection.getSTBDriver().isConnected()) connectToTheSTB();
			else disconnectFromTheSTB();
		}
		else if( v==button_enter_text) {

			AlertDialog.Builder editalert = new AlertDialog.Builder(this);
			editalert.setTitle("Message to send to the STB");
			final EditText input = new EditText(this);
			editalert.setView(input);

			editalert.setPositiveButton("Send it", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	
			    	sendMessageToSTB(Commands.USER_TEXT, input.getText().toString());
			    }
			});

			editalert.show();
		}
	}
	
	private void connectToTheSTB() {
		if (serviceConnection.isBound()) {
			new STBCommunicationTask(this, serviceConnection.getSTBDriver()).execute(STBCommunication.REQUEST_CONNECT, edIP.getText().toString());
		}
	}
	
	private void disconnectFromTheSTB() {
		if (serviceConnection.isBound()) {
			new STBCommunicationTask(this, serviceConnection.getSTBDriver()).execute(STBCommunication.REQUEST_DISCONNECT);
		}
	}
	
	private void sendMessageToSTB(String msg) {
		if (serviceConnection.isBound()) {
			new STBCommunicationTask(this, serviceConnection.getSTBDriver()).execute(STBCommunication.REQUEST_COMMAND, msg);
		}
	}
	
	private void sendMessageToSTB(String msg, String extra) {
		if (serviceConnection.isBound()) {
			new STBCommunicationTask(this, serviceConnection.getSTBDriver()).execute(STBCommunication.REQUEST_COMMAND, msg, extra);
		}
	}

	@Override
	public void requestSucceed(String request, String message, String command) {
		if (STBCommunication.REQUEST_SCAN.equals(request)) {
			new STBCommunicationTask(this, serviceConnection.getSTBDriver()).execute(STBCommunication.REQUEST_CONNECT, message);
		} else if (STBCommunication.REQUEST_CONNECT.equals(request)) {
			buttons_layout.setVisibility(View.VISIBLE);
		} else if (STBCommunication.REQUEST_DISCONNECT.equals(request)) {
			buttons_layout.setVisibility(View.GONE);
		}
		for (ImageView iv : buttons) {
	    	iv.setBackgroundResource(R.color.black);
	    }
	}

	@Override
	public void requestFailed(String request, String message, String command) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		for (ImageView iv : buttons) {
	    	iv.setBackgroundResource(R.color.black);
	    }
	}
	
	public String getLocalIpAddress() {
		WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		String ipBinary = null;
		try {
			ipBinary = Integer.toBinaryString(wm.getConnectionInfo().getIpAddress());
		} catch (Exception e) {}
		if (ipBinary != null) {
			while(ipBinary.length() < 32) {
				ipBinary = "0" + ipBinary;
			}
			String a = ipBinary.substring(0,8);
			String b = ipBinary.substring(8,16);
			String c = ipBinary.substring(16,24);
			String d = ipBinary.substring(24,32);
			String actualIpAddress = Integer.parseInt(d,2) + "." + Integer.parseInt(c,2) + "." + Integer.parseInt(b,2) + "." + Integer.parseInt(a,2);
			return actualIpAddress;
		} else {
			return null;
		}
	}

	@Override
	public void serviceBound() {
		new STBCommunicationTask(this, serviceConnection.getSTBDriver()).execute(STBCommunication.REQUEST_SCAN, getLocalIpAddress());
	}

	@Override
	public void serviceUnbind() {
		// TODO Auto-generated method stub
	}
}
