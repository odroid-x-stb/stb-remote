package fr.enseirb.odroidx.remote_server.bkg_work;

import java.io.IOException;
import java.net.Socket;

import android.app.Instrumentation;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import fr.enseirb.odroidx.remote_server.communication.Commands;
import fr.enseirb.odroidx.remote_server.communication.STBCommunication;
import fr.enseirb.odroidx.remote_server.service.RemoteControlService;

public class ClientRunnable implements Runnable {

	private static final String TAG = "ClientRunnable";
	
	private Socket sock;
	private RemoteControlService rcs;
	
	public ClientRunnable(Socket sock_cli, RemoteControlService s) {
		sock = sock_cli;
		rcs = s;
	}

	@Override
	public void run() {

		STBCommunication STBCom = new STBCommunication(sock);
    	
        while (sock.isConnected()) {
	        
        	// waiting for data
        	final String msg = STBCom.stb_receive();
        	Log.i(TAG, "receive from client "+sock.hashCode()+": "+msg);
        	
        	// Send the key command to activities that listen to the service
	        new Handler(Looper.getMainLooper()).post(new Runnable() {
	            @Override
	            public void run() {
	        		if (msg.equals(Commands.VIDEO_PLAY)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_PLAY, null);
	        		else if (msg.equals(Commands.VIDEO_PAUSE)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_PAUSE, null);
	        		else if (msg.equals(Commands.VIDEO_PREVIOUS)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_PREVIOUS, null);
	        		else if (msg.equals(Commands.VIDEO_REWIND)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_REWIND, null);
	        		else if (msg.equals(Commands.VIDEO_FORWARD)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_FORWARD, null);
	        		else if (msg.equals(Commands.VIDEO_NEXT)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_NEXT, null);
	        		else if (msg.equals(Commands.SELECT)) rcs.sendMessageToUI(RemoteControlService.CMD__SELECT, null);
	        		else if (msg.equals(Commands.MOVE_UP)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_UP, null);
	        		else if (msg.equals(Commands.MOVE_DOWN)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_DOWN, null);
	        		else if (msg.equals(Commands.MOVE_LEFT)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_LEFT, null);
	        		else if (msg.equals(Commands.MOVE_RIGHT)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_RIGHT, null);
	        		else if (msg.equals(Commands.BACK)) rcs.sendMessageToUI(RemoteControlService.CMD__BACK, null);
	        		else if (msg.equals(Commands.HOME)) rcs.sendMessageToUI(RemoteControlService.CMD__HOME, null);
	            }
	        });
            
        	// check if client want to leave the communication
        	if (msg.equals("CLIENT_DISCONNECT")) {
        		
        		Log.i(TAG, "action for client "+sock.hashCode()+": disconnected");
        		
        		try {
					sock.close();
				} catch (IOException e) {
					Log.e(TAG, "ERROR", e);
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
	                rcs.sendMessageToUI(RemoteControlService.MSG__PRINT_NEW_CLIENT_ACTION, msg);
	            }
	        });
        	
        	// build feedback
        	String feedback = msg + "__ok";
        	
        	// send feedback
        	STBCom.stb_send(feedback);
        	Log.i(TAG, "send to client "+sock.hashCode()+": "+feedback);
		}
	}
}
