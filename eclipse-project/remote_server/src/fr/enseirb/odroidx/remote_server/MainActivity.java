package fr.enseirb.odroidx.remote_server;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.enseirb.odroidx.remote_server.service.RemoteControlService;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	
	private ListView listview_clients;
	private ListView listview_actions;
	private TextView textview_ip;
	
	private ArrayList<String> clients_list;
	private ArrayList<String> clients_actions_list; 
	
	public final static int COMMUNICATION_PORT = 2000;

	private ImageView button_play;
	private ImageView button_pause;
	private ImageView button_rewind;
	private ImageView button_forward;
	private ImageView button_previous;
	private ImageView button_next;
	private ImageView button_up;
	private ImageView button_down;
	private ImageView button_right;
	private ImageView button_left;
	private ImageView button_select;    
	private ImageView button_back;
	private ImageView button_home;
	
	Messenger mService = null;
    boolean mIsBound;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	
        	switch (msg.what) {
	            case RemoteControlService.MSG__PRINT_NEW_CLIENT:
	            	String client = msg.getData().getString("msg");
	            	add_client(client);
	                break;
	            case RemoteControlService.MSG__PRINT_NEW_CLIENT_ACTION:
	            	String client_action = msg.getData().getString("msg");
	            	add_client_action(client_action);
	                break;
	            case RemoteControlService.CMD__VIDEO_PLAY:
	            	clear_button_pressed();
	            	button_play.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_MEDIA_PLAY);
	            	break;
	            case RemoteControlService.CMD__VIDEO_PAUSE:
	            	clear_button_pressed();
	            	button_pause.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_MEDIA_PAUSE);
	            	break;
	            case RemoteControlService.CMD__VIDEO_PREVIOUS:
	            	clear_button_pressed();
	            	button_previous.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
	            	break;
	            case RemoteControlService.CMD__VIDEO_REWIND:
	            	clear_button_pressed();
	            	button_rewind.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_MEDIA_REWIND);
	            	break;
	            case RemoteControlService.CMD__VIDEO_FORWARD:
	            	clear_button_pressed();
	            	button_forward.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_MEDIA_FAST_FORWARD);
	            	break;
	            case RemoteControlService.CMD__VIDEO_NEXT:
	            	clear_button_pressed();
	            	button_next.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_MEDIA_NEXT);
	            	break;
	            case RemoteControlService.CMD__MOVE_UP:
	            	clear_button_pressed();
	            	button_up.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_DPAD_UP);
	            	break;
	            case RemoteControlService.CMD__MOVE_DOWN:
	            	clear_button_pressed();
	            	button_down.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_DPAD_DOWN);
	            	break;
	            case RemoteControlService.CMD__MOVE_LEFT:
	            	clear_button_pressed();
	            	button_left.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_DPAD_LEFT);
	            	break;
	            case RemoteControlService.CMD__MOVE_RIGHT:
	            	clear_button_pressed();
	            	button_right.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_DPAD_RIGHT);
	            	break;
	            case RemoteControlService.CMD__SELECT:
	            	clear_button_pressed();
	            	button_select.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_DPAD_CENTER);
	            	break;
	            case RemoteControlService.CMD__BACK:
	            	clear_button_pressed();
	            	button_back.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_BACK);
	            	break;
	            case RemoteControlService.CMD__HOME:
	            	clear_button_pressed();
	            	button_home.setBackgroundResource(R.color.orange_light);
//	            	press_key(KeyEvent.KEYCODE_DPAD_CENTER);
	            	break;
	            default:
	                super.handleMessage(msg);
            }
        }
    }
    
    private void clear_button_pressed() {
    	button_play.setBackgroundResource(R.color.blue_dark);
		button_pause.setBackgroundResource(R.color.blue_dark);
		button_forward.setBackgroundResource(R.color.blue_dark);
		button_rewind.setBackgroundResource(R.color.blue_dark);
		button_previous.setBackgroundResource(R.color.blue_dark);
		button_next.setBackgroundResource(R.color.blue_dark);
		button_up.setBackgroundResource(R.color.blue_dark);
		button_down.setBackgroundResource(R.color.blue_dark);
		button_left.setBackgroundResource(R.color.blue_dark);
		button_right.setBackgroundResource(R.color.blue_dark);
		button_select.setBackgroundResource(R.color.blue_dark);
		button_home.setBackgroundResource(R.color.blue_dark);
		button_back.setBackgroundResource(R.color.blue_dark);
    }
    
    private void press_key (final int keyCmd) {
    	Thread myThread = new Thread() {
            public void run() {
    	    	try {
    	    		Instrumentation inst = new Instrumentation();
    		        inst.sendKeyDownUpSync(keyCmd);
    		        Log.v(TAG, "key "+keyCmd+"pressed");
    	    	} catch (Exception e) {
    	    		Log.e(TAG, "error while simulating key event :\n", e);
    	    	}
    	    }
    	};
    	myThread.start();
    }
    
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            try {
                Message msg = Message.obtain(null, RemoteControlService.MSG__REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even do anything with it
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            mService = null;
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		clients_list = new ArrayList<String>();
		clients_actions_list = new ArrayList<String>();
		
		// View components
		listview_clients = (ListView) findViewById(R.id.listview_clients);
		listview_actions = (ListView) findViewById(R.id.listview_actions);
		textview_ip = (TextView) findViewById(R.id.textview_ip);
	
		// start to run the server
		textview_ip.setText(getLocalIpAddress()+" : "+COMMUNICATION_PORT);
		
		// ....
	    // loading view components
	    button_play = (ImageView) findViewById(R.id.button_play);
	    button_pause = (ImageView) findViewById(R.id.button_pause);
	    button_rewind = (ImageView) findViewById(R.id.button_rewind);
	    button_forward = (ImageView) findViewById(R.id.button_forward);
	    button_previous = (ImageView) findViewById(R.id.button_previous);
	    button_next = (ImageView) findViewById(R.id.button_next);
	    button_up = (ImageView) findViewById(R.id.button_up);
	    button_down = (ImageView) findViewById(R.id.button_down);
	    button_left = (ImageView) findViewById(R.id.button_left);
	    button_right = (ImageView) findViewById(R.id.button_right);
	    button_select = (ImageView) findViewById(R.id.button_select);
	    button_back = (ImageView) findViewById(R.id.button_back);
	    button_home = (ImageView) findViewById(R.id.button_home);
		
		startService(new Intent(MainActivity.this, RemoteControlService.class));
		bindService(new Intent(this, RemoteControlService.class), mConnection, Context.BIND_AUTO_CREATE);
	}
		
    void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, RemoteControlService.MSG__UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }
	
	// --
	// The following functions provide UI component modifiers :
	// --
	
	// 1st column: IP informations
	
	public String getLocalIpAddress() {
		WifiManager wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        String ipBinary = null;
        try {
        	ipBinary = Integer.toBinaryString(wm.getConnectionInfo().getIpAddress());
        } catch (Exception e) {
        	Toast.makeText(getApplicationContext(), "Error while retrieving local IP address", Toast.LENGTH_SHORT).show();
        }
        if (ipBinary != null) {
        	while(ipBinary.length() < 32) {
	        	ipBinary = "0" + ipBinary;
	      	}
	      	String a=ipBinary.substring(0,8);
	      	String b=ipBinary.substring(8,16);
	      	String c=ipBinary.substring(16,24);
	      	String d=ipBinary.substring(24,32);
	      	String actualIpAddress =Integer.parseInt(d,2)+"."+Integer.parseInt(c,2)+"."+Integer.parseInt(b,2)+"."+Integer.parseInt(a,2);
	      	return actualIpAddress;
        } else {
        	return "Unknown IP";
        }
    }
	
	// 2nd column: client login and logout
	
	public void add_client (String id) {
		clients_list.add(id);
		String[] clients_array = clients_list.toArray(new String[0]);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, clients_array);
		listview_clients.setAdapter(adapter);
		listview_clients.setSelection(clients_array.length-1);
	}
	
	// 3rd column : client actions corresponding to pressed buttons

	public void add_client_action (String id) {
		clients_actions_list.add(id);
		String[] actions_array = clients_actions_list.toArray(new String[0]);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, actions_array);
		listview_actions.setAdapter(adapter);
		listview_actions.setSelection(actions_array.length-1);
	}
}