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
		
		final STBCommunication STBCom = new STBCommunication(sock);
		
        while (sock.isConnected()) {
	        
        	// waiting for data
        	final String msg = STBCom.stb_receive();
        	Log.i(TAG, "receive from client "+sock.hashCode()+": "+msg);
        	
        	if (msg == null) {
        		Log.e(TAG, "previous message received is null (loop:continue;)");
        		continue;
        	}
        	
        	// if its a user input text, we retrieve the text
        	String user_textTmp = null;
        	if (msg.equals(Commands.USER_TEXT)) {
        		user_textTmp = STBCom.stb_receive();
        	}
        	final String user_text = user_textTmp;
        	
        	// Send the key command to activities that listen to the service
	        new Handler(Looper.getMainLooper()).post(new Runnable() {

				@Override
	            public void run() {
	        		if (msg.equals(Commands.VIDEO_PLAY)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_PLAY, null);
	        		else if (msg.equals(Commands.VIDEO_PAUSE)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_PAUSE, null);
	        		else if (msg.equals(Commands.VIDEO_PREVIOUS)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_PREVIOUS, null);
	        		else if (msg.equals(Commands.VIDEO_STOP)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_STOP, null);
	        		else if (msg.equals(Commands.VIDEO_NEXT)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_NEXT, null);
	        		else if (msg.equals(Commands.SELECT)) rcs.sendMessageToUI(RemoteControlService.CMD__SELECT, null);
	        		else if (msg.equals(Commands.MOVE_UP)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_UP, null);
	        		else if (msg.equals(Commands.MOVE_DOWN)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_DOWN, null);
	        		else if (msg.equals(Commands.MOVE_LEFT)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_LEFT, null);
	        		else if (msg.equals(Commands.MOVE_RIGHT)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_RIGHT, null);
	        		else if (msg.equals(Commands.BACK)) rcs.sendMessageToUI(RemoteControlService.CMD__BACK, null);
	        		else if (msg.equals(Commands.HOME)) rcs.sendMessageToUI(RemoteControlService.CMD__HOME, null);
	        		else if (msg.equals(Commands.SOUND_MUTE)) rcs.sendMessageToUI(RemoteControlService.CMD__SOUND_MUTE, null);
	        		else if (msg.equals(Commands.SOUND_PLUS)) rcs.sendMessageToUI(RemoteControlService.CMD__SOUND_PLUS, null);
	        		else if (msg.equals(Commands.SOUND_MINUS)) rcs.sendMessageToUI(RemoteControlService.CMD__SOUND_MINUS, null);
	        		else if (msg.equals(Commands.USER_TEXT)) rcs.sendMessageToUI(RemoteControlService.CMD__USER_TEXT, user_text);
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
