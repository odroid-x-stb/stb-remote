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
package fr.enseirb.odroidx.remote_server.bkg_work;

import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import fr.enseirb.odroidx.remote_server.service.RemoteControlService;

public class ServerRunnable implements Runnable {

	private static final String TAG = "ServerRunnable";
	
	private int port;
	private RemoteControlService rcs;
	
	public ServerRunnable(RemoteControlService serv, int communicationPort) {
		port = communicationPort;
		rcs = serv;
		Log.v(TAG, "ServerRunnable constructor");
	}

	@Override
	public void run() {
		try {
			Log.i(TAG, "Starting server...");
			
			ServerSocket s = new ServerSocket(port);
			s.setReuseAddress(true);
			
			Log.i(TAG, "Server is listening");
			
			while (s != null) {
				
		        final Socket sock_client = s.accept();
		        Log.i(TAG, "new client: "+sock_client.hashCode());
		        
		        // update UI
		        new Handler(Looper.getMainLooper()).post(new Runnable() {
		            @Override
		            public void run() {
		            	rcs.sendMessageToUI(RemoteControlService.MSG__PRINT_NEW_CLIENT, "client: "+sock_client.hashCode());
		            }
		        });
		        
		        // launch a new thread for each new client - This thread will handle client commands/request from the remote control
		        ClientRunnable client_runnable = new ClientRunnable(sock_client, rcs);
		        Thread client_thread = new Thread(client_runnable);
		        client_thread.start();
		 	}
			
		} catch (Exception e) {
			Log.e(TAG, "ERROR:\n", e);
		}
	}
}
