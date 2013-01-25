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
package fr.enseirb.odroidx.remote_client.communication;

import android.os.AsyncTask;
import android.util.Log;

public class STBCommunicationTask extends AsyncTask<String, Void, Boolean> {
	
	public interface STBTaskListenner {
		public void requestSucceed(String request, String message, String command);
		public void requestFailed(String request, String message, String command);
	}
	
	private static final String TAG = STBCommunicationTask.class.getSimpleName();
	
	private STBTaskListenner listenner;
	private STBCommunication stbDriver;
	private String message;
	private String request;
	private String command;
	
    public STBCommunicationTask(STBTaskListenner listenner, STBCommunication stbDriver) {
    	this.listenner = listenner;
    	this.stbDriver = stbDriver;
    }
	
	@Override
	protected Boolean doInBackground(String... params) {
		boolean success = false;
		String type = params[0];
		if (STBCommunication.REQUEST_SCAN.equals(type)) {
			request = STBCommunication.REQUEST_SCAN;
			String localIP = params[1];
			String serverIP = stbDriver.scanSubnet(localIP, STBCommunication.COMMUNICATION_PORT);
			success = (serverIP != null);
			if (!success) {
				message = "Error: Server not found";
				return false;
			} else {
				message = serverIP;
				return true;
			}
		} else if (STBCommunication.REQUEST_CONNECT.equals(type)) {
			request = STBCommunication.REQUEST_CONNECT;
			String ip = params[1];
			success = stbDriver.stbConnect(ip, STBCommunication.COMMUNICATION_PORT);
			if (!success) {
				message = "Error: Cannot connect to the STB (check your WiFi, IP, network configuration...)";
				return false;
			} else {
				message = "Connected to the STB";
				return true;
			}
		} else if (STBCommunication.REQUEST_DISCONNECT.equals(type)) {
			request = STBCommunication.REQUEST_DISCONNECT;
			success = stbDriver.stbDisconnect();
			if (!success) {
				message = "Error: Cannot disconnect from the STB";
				return false;
			} else {
				message = "Disconnected from the STB";
				return true;
			}
		} else if (STBCommunication.REQUEST_COMMAND.equals(type)) {
			request = STBCommunication.REQUEST_COMMAND;
			String cmd = params[1];
			command = cmd;
			success =  stbDriver.stbSend(cmd);
			Log.v(TAG, "sending to the STB: " + cmd);
			// sending text enter by the user
			try {
				if (params.length > 2) {
					success = stbDriver.stbSend(params[2]);
					Log.v(TAG, "sending to the STB: " + params[2]);
				}
			} catch (Exception e) {
				Log.e(TAG, "ERROR:\n", e);
				message = "Error: sending command to the STB failed";
				return false;
			}
			if (!success) {
				message = "Error: sending command to the STB failed";
				return false;
			} else {
				message = "sent to the STB: " + cmd;
				return true;
			}
		} else {
			request = STBCommunication.REQUEST_UNKNOWN;
			message = "Error";
			return false;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			listenner.requestSucceed(request, message, command);
		} else {
			listenner.requestFailed(request, message, command);
		}
	}
}