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
package fr.enseirb.odroidx.remote_server.service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import fr.enseirb.odroidx.remote_server.MainActivity;
import fr.enseirb.odroidx.remote_server.bkg_work.ServerRunnable;

public class RemoteControlService extends Service {
	
	public static final String TAG = "RemoteControlService" ;
	
	ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
	
    public static final int MSG__REGISTER_CLIENT = 1;
    public static final int MSG__UNREGISTER_CLIENT = 2;
    public static final int MSG__PRINT_NEW_CLIENT = 3;
    public static final int MSG__PRINT_NEW_CLIENT_ACTION = 4;
    
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
	
	
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
    
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG__REGISTER_CLIENT:
                mClients.add(msg.replyTo);
                break;
            case MSG__UNREGISTER_CLIENT:
                mClients.remove(msg.replyTo);
                break;
            default:
                super.handleMessage(msg);
            }
        }
    }
    
    public void sendMessageToUI(int msg_type, String msg_value) {
    	for (int i=mClients.size()-1; i>=0; i--) {
            try {
                Bundle b = new Bundle();
                b.putString("msg_value", ""+msg_value);
                Message msg = Message.obtain(null, msg_type);
                msg.setData(b);
                mClients.get(i).send(msg);
            } catch (RemoteException e) {
                mClients.remove(i);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        ServerRunnable server_runnable = new ServerRunnable(this, MainActivity.COMMUNICATION_PORT);
        Thread server_thread = new Thread(server_runnable);
        server_thread.start();
        Log.i(TAG, "Service Started.");
    }
        
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service Stopped.");
    }
}