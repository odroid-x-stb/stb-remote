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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ListView listview_clients;
	private ListView listview_actions;
	private TextView textview_ip;
	
	private ArrayList<String> clients_list;
	private ArrayList<String> clients_actions_list; 
	
	public final static int COMMUNICATION_PORT = 2000;

	public ImageView button_play;
	public ImageView button_pause;
	public ImageView button_stop;
	public ImageView button_previous;
	public ImageView button_next;
	public ImageView button_up;
	public ImageView button_down;
	public ImageView button_right;
	public ImageView button_left;
	public ImageView button_select;    
	public ImageView button_back;
	public ImageView button_home;
	public ImageView button_sound_mute;
	public ImageView button_sound_plus;
	public ImageView button_sound_minus;
	public ImageView button_enter_text;
	
	public ArrayList<ImageView> buttons;
	
	STBRemoteControlCommunication stbrcc;
	
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
		
		// loading view components
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
		
	    buttons = new ArrayList<ImageView>();
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
	    
	    stbrcc = new STBRemoteControlCommunication(this);
	    stbrcc.doBindService();
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stbrcc.doUnbindService();
	}
}