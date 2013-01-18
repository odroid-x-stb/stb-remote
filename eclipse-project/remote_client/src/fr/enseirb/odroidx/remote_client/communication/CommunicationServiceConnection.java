package fr.enseirb.odroidx.remote_client.communication;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import fr.enseirb.odroidx.remote_client.communication.CommunicationService.CommunicationBinder;

public class CommunicationServiceConnection implements ServiceConnection {

	public interface ComServiceListenner {
		public void serviceBound();
		public void serviceUnbind();
	}
	
	private ComServiceListenner listenner;
	private STBCommunication stbDriver;
	private boolean bound = false;
	
	public CommunicationServiceConnection() {}
	
	public CommunicationServiceConnection(ComServiceListenner listenner) {
		this.listenner = listenner;
	}
	
	public STBCommunication getSTBDriver() {
		return stbDriver;
	}
	
	public boolean isBound() {
		return bound;
	}
	
	public void setBound(boolean bound) {
		this.bound = bound;
	}
	
	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		CommunicationBinder binder = (CommunicationBinder) service;
		stbDriver = binder.getSTBDriver();
		bound = true;
		if (listenner != null) {
			listenner.serviceBound();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		bound = false;
		if (listenner != null) {
			listenner.serviceUnbind();
		}
	}
}
