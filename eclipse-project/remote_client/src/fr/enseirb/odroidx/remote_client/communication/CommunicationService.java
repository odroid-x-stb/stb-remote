package fr.enseirb.odroidx.remote_client.communication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class CommunicationService extends Service {
	
	private final IBinder communicationBinder = new CommunicationBinder(); 
	private STBCommunication stbDriver = new STBCommunication();
	
	public class CommunicationBinder extends Binder {
		public STBCommunication getSTBDriver() {
			return stbDriver;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return communicationBinder;
	}
}
