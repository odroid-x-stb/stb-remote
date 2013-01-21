/**
 * Copyright (C) 2012 Enseirb, Sylvain Bilange, Fabien Fleurey <fabien@fleurey.com>
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

import fr.enseirb.odroidx.remote_client.communication.STBCommunicationTask.STBTaskListenner;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class CommunicationService extends Service implements STBTaskListenner {
	
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		new STBCommunicationTask(this, stbDriver).execute(STBCommunication.REQUEST_DISCONNECT);
	}

	@Override
	public void requestSucceed(String request, String message, String command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestFailed(String request, String message, String command) {
		// TODO Auto-generated method stub
		
	}
}
