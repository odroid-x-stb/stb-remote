package fr.enseirb.odroidx.remote_server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import fr.enseirb.odroidx.remote_server.bkg_work.ServerRunnable;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView listview_clients;
	private ListView listview_actions;
	private TextView textview_ip;
	
	private ArrayList<String> clients_list;
	private ArrayList<String> clients_actions_list; 
	
	private final static int COMMUNICATION_PORT = 2000;
	
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
		ServerRunnable server_runnable = new ServerRunnable(this, COMMUNICATION_PORT);
        Thread server_thread = new Thread(server_runnable);
        server_thread.start();
		textview_ip.setText(getLocalIpAddress()+" : "+COMMUNICATION_PORT);
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