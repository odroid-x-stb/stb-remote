package fr.enseirb.odroidx.remote_client.communication;

import java.net.Socket;

import android.util.Log;

/*
 * IMPORTANT :
 * This class performs network operations (communication through sockets)
 * It means long time operations, causing trouble (java exception) if they are executed in the main thread (UI)
 * That's why it has to be called from a Thread, Runnable, AsyncTask (and not from UI, or service without thread)
 */

public class STBCommunication {
	
	private Socket sock;
	private static final String TAG = "STBCommunication";
	
	public STBCommunication () {
		sock = null;
	}
	
	public boolean stb_connect(final String ip, final int port) {
        try {
            sock = new Socket(ip, port);
            sock.setReuseAddress(true);
            Log.i(TAG, "Connected to the STB");
            return true;
		} catch (Exception e) {
			Log.e(TAG, "ERROR: Opening socket failed");
			return false;
		}
	}
	
	public boolean stb_disconnect() {

		if (! is_connected ()) {
			Log.i(TAG, "ERROR: Trouble disconnecting: no connection");
			return false;
		}
		
		String msg = "CLIENT_DISCONNECT";
		byte msg_to_bytes[] = msg.getBytes();
		try {
			sock.getOutputStream().write(msg_to_bytes, 0, msg_to_bytes.length);
		} catch (Exception e) {
			Log.e(TAG, "ERROR: read the OutputStream failed");
			return false;
		}
		
		try {
			sock.close();
			sock = null;
			
			Log.i(TAG, "Disconnected from the STB");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "ERROR: close the socket failed");
			return false;
		}
	}
	
	public boolean stb_send(final String msg) {

		if (! is_connected ()) {
			Log.i(TAG, "ERROR: Trouble disconnecting: no connection");
			return false;
		}
		
		byte msg_to_bytes[] = msg.getBytes();
		try {
			sock.getOutputStream().write(msg_to_bytes, 0, msg_to_bytes.length);
			Log.i(TAG, "ERROR: Trouble disconnecting: no connection");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "ERROR:", e);
			return false;
		}
	}
	
	public String stb_receive() {
		if (! is_connected ()) {
			Log.i(TAG, "ERROR: Trouble disconnecting: no connection");
			return null;
		}
		
		try {
	        byte[] buffer = new byte[1024];
	        int readBytes = sock.getInputStream().read(buffer, 0, 1024);
	        return new String(buffer, 0, readBytes, "UTF-8");
		} catch (Exception e) {
			Log.e(TAG, "ERROR:", e);
		}
		return null;
	}

	public boolean is_connected () {
		if (sock == null) return false;
		if (sock.isConnected()) return true;
		else return false;
	}
}
