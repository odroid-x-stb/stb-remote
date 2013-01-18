package fr.enseirb.odroidx.remote_client.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.util.Log;

/*
 * IMPORTANT :
 * This class performs network operations (communication through sockets)
 * It means long time operations, causing trouble (java exception) if they are executed in the main thread (UI)
 * That's why it has to be called from a Thread, Runnable, AsyncTask (and not from UI, or service without thread)
 */

public class STBCommunication {
	
	public static final int COMMUNICATION_PORT = 2000;
public static final String REQUEST_SCAN = "scan";
	public static final String REQUEST_CONNECT = "connect";
	public static final String REQUEST_DISCONNECT = "disconnect";
	public static final String REQUEST_COMMAND = "command";
	public static final String REQUEST_UNKNOWN = "unknown";
	
	private static final String TAG = "STBCommunication";
	
	private Socket sock;
	
	public STBCommunication () {
		sock = null;
	}
	
	public STBCommunication (Socket s) {
		sock = s;
	}
	
	public String scan_subnet(String localAddr, int port) {
		String subnet = localAddr.substring(0, localAddr.lastIndexOf(".") + 1);
		Socket socket = new Socket();
		PrintWriter out = null;
	    BufferedReader in = null;
	    String response = null;
	    String ipServer = null;
		for (int d = 1 ; d < 255 ; d ++) {
			String ip = subnet + d;
			Log.d(TAG, "Scan ip: " + ip);
			SocketAddress address = new InetSocketAddress(ip, port);
			try {
				socket.connect(address, 20);
				out = new PrintWriter(socket.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out.println("Hello");
				while ((response = in.readLine()) != null) {
					if ("World".equals(response)) {
						ipServer = ip;
						break;
					}
				}
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ipServer;
	}
	
	public boolean stb_connect(final String ip, final int port) {
        try {
            sock = new Socket(ip, port);
            sock.setReuseAddress(true);
            Log.i(TAG, "Connected to the STB");
            return true;
		} catch (Exception e) {
			Log.e(TAG, "ERROR: Opening socket failed", e);
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
			Log.i(TAG, "ERROR: Trouble sending: no connection");
			return false;
		}
		
		byte msg_to_bytes[] = msg.getBytes();
		try {
			sock.getOutputStream().write(msg_to_bytes, 0, msg_to_bytes.length);
			return true;
		} catch (Exception e) {
			Log.e(TAG, "ERROR:", e);
			return false;
		}
	}
	
	public String stb_receive() {
		if (! is_connected ()) {
			Log.i(TAG, "ERROR: Trouble receiving: no connection");
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
