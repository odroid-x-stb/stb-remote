package fr.enseirb.odroidx.remote_server.bkg_work;

import java.net.ServerSocket;
import java.net.Socket;

import fr.enseirb.odroidx.remote_server.MainActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class ServerRunnable implements Runnable {

	private MainActivity activity;
	private int port;
	
	public ServerRunnable(MainActivity act, int communicationPort) {
		port = communicationPort;
		activity = act;
	}

	@Override
	public void run() {
		try {
			ServerSocket s = new ServerSocket(port);
			s.setReuseAddress(true);
			
			while (s != null) {
		        // waiting for a client connection
		        final Socket sock_client = s.accept();
		        Log.e(getClass().getSimpleName(), "new client: "+sock_client.hashCode());
		        
		        // update UI
		        new Handler(Looper.getMainLooper()).post(new Runnable() {
		            @Override
		            public void run() {
		                activity.add_client(sock_client.hashCode()+"");
		            }
		        });
		        
		        // launch a new thread for client requests
		        ClientRunnable client_runnable = new ClientRunnable(activity, sock_client);
		        Thread client_thread = new Thread(client_runnable);
		        client_thread.start();
		 	}
			
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "ERROR", e);
		}
	}
}
