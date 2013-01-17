package fr.enseirb.odroidx.remote_client.communication;

import android.os.AsyncTask;
import android.util.Log;

public class STBCommunicationTask extends AsyncTask<String, Void, Boolean> {
	
	public interface STBTaskListenner {
		public void requestSucceed(String request, String message, String command);
		public void requestFailed(String request, String message, String command);
	}
	
	private static final String TAG = STBCommunicationTask.class.getSimpleName();
	private static final int COMMUNICATION_PORT = 2000;
	
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
		if (STBCommunication.REQUEST_CONNECT.equals(type)) {
			request = STBCommunication.REQUEST_CONNECT;
			String ip = params[1];
			success = stbDriver.stb_connect(ip, COMMUNICATION_PORT);
			if (!success) {
				message = "Error: Cannot connect to the STB (check your WiFi, IP, network configuration...)";
				return false;
			} else {
				message = "Connected to the STB";
				return true;
			}
		} else if (STBCommunication.REQUEST_DISCONNECT.equals(type)) {
			request = STBCommunication.REQUEST_DISCONNECT;
			success = stbDriver.stb_disconnect();
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
			success =  stbDriver.stb_send(cmd);
			Log.v(TAG, "sending to the STB: " + cmd);
			// sending text enter by the user
			try {
				if (params.length > 2) {
					success = stbDriver.stb_send(params[2]);
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