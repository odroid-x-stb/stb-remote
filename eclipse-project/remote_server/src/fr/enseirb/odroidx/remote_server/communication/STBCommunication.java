package fr.enseirb.odroidx.remote_server.communication;

import java.net.Socket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class STBCommunication {
	
	// Attributes
	
	private Socket sock;
	private Context act;
	
	
	// Constructors
	
	public STBCommunication () {
		sock = null;
		act = null;
	}
	
	public STBCommunication (Socket s) {
		sock = s;
		act = null;
	}
	
	public STBCommunication (Context ctx) {
		sock = null;
		act = ctx;
	}
	
	public STBCommunication (Socket s, Context ctx) {
		sock = s;
		act = ctx;
	}
	
	
	// Methods
	
	public void stb_connect(String ip, int port) {
        try {
            sock = new Socket(ip, port);
            sock.setReuseAddress(true);
            if (act != null)
            	Toast.makeText(act, "Connected to the STB", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "ERROR:", e);
			if (act != null)
            	Toast.makeText(act, "Opening connection failed", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void stb_disconnect() {
		if (! is_connected()) {
			if (act != null)
            	Toast.makeText(act, "Trouble disconnecting: no connection", Toast.LENGTH_SHORT).show();
			return;
		}
			
		try {
			sock.close();
			sock = null;
			if (act != null)
            	Toast.makeText(act, "Disconnected from the STB", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "ERROR:", e);
		}
	}
	
	public void stb_send(final String msg) {
		if (! is_connected()) {
			if (act != null)
            	Toast.makeText(act, "Trouble sending data: no connection", Toast.LENGTH_SHORT).show();
			return;
		}
		
		byte msg_to_bytes[] = msg.getBytes();
		try {
			sock.getOutputStream().write(msg_to_bytes, 0, msg_to_bytes.length);
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "ERROR:", e);
		}
	}
	
	public String stb_receive() {
		if (! is_connected()) {
			if (act != null)
            	Toast.makeText(act, "Trouble receiving data: no connection", Toast.LENGTH_SHORT).show();
			return "";
		}
		
		try {
	        byte[] buffer = new byte[4096];
	        int readBytes = sock.getInputStream().read(buffer, 0, 4096);
	        return new String(buffer, 0, readBytes, "UTF-8");
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "ERROR:", e);
			return null;
		}
	}

	public boolean is_connected () {
		if (sock == null) return false;
		if (sock.isConnected()) return true;
		else return false;
	}
}
