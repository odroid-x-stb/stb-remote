package fr.enseirb.odroidx.remote_server.bkg_work;

import java.io.IOException;
import java.net.Socket;

import fr.enseirb.odroidx.remote_server.MainActivity;
import fr.enseirb.odroidx.remote_server.communication.STBCommunication;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class ClientRunnable implements Runnable {

	private Socket sock;
	private MainActivity activity;
	
	public ClientRunnable(MainActivity act, Socket sock_cli) {
		sock = sock_cli;
		activity = act;
	}

	@Override
	public void run() {

		STBCommunication STBCom = new STBCommunication(sock);
    	
        while (sock.isConnected()) {
	        
        	// waiting for data
        	final String msg = STBCom.stb_receive();
        	Log.e(getClass().getSimpleName(), "receive from client "+sock.hashCode()+": "+msg);
        	
        	// check if client want to leave the communication
        	if (msg.equals("CLIENT_DISCONNECT")) {
        		
        		Log.e(getClass().getSimpleName(), "action for client "+sock.hashCode()+": disconnected");
        		
        		try {
					sock.close();
				} catch (IOException e) {
					Log.e(getClass().getSimpleName(), "ERROR", e);
				}
        		
        		break;
        	}
        	
        	if (msg == null || !sock.isConnected()) {
        		break;
        	}
        	
	        // update UI
	        new Handler(Looper.getMainLooper()).post(new Runnable() {
	            @Override
	            public void run() {
	                activity.add_client_action(sock.hashCode()+": "+msg);
	            }
	        });
        	
        	// build feedback
        	String feedback = msg + "__ok";
        	
        	// send feedback
        	STBCom.stb_send(feedback);
        	Log.e(getClass().getSimpleName(), "send to client "+sock.hashCode()+": "+feedback);
		}
	}
}
