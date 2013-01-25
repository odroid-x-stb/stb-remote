/**
 * Copyright (C) 2012 Sylvain Bilange, Fabien Fleurey <fabien@fleurey.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
