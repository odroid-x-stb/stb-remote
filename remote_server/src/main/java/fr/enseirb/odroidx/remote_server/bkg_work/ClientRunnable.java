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
package fr.enseirb.odroidx.remote_server.bkg_work;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import fr.enseirb.odroidx.remote_server.communication.Commands;
import fr.enseirb.odroidx.remote_server.service.RemoteControlService;

public class ClientRunnable implements Runnable {

	private static final String TAG = ClientRunnable.class.getSimpleName();
	
	private Socket socket;
	private PrintWriter out;
    private BufferedReader in;
	private RemoteControlService rcs;
	
	public ClientRunnable(Socket socket, RemoteControlService s) {
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		rcs = s;
	}

	@Override
	public void run() {
		String messageTmp = null;
		String userTextTmp = null;
		boolean userTextInput = false;
		try {
			while (socket != null && socket.isConnected() && (messageTmp = in.readLine()) != null) {
				Log.d(TAG, "receive from client " + socket.hashCode() + ": " + messageTmp);
				userTextTmp = null;
				if ("Hello".equals(messageTmp)) {
					sendMessage("World");
				} else if ("CLIENT_DISCONNECT".equals(messageTmp)) {
					Log.i(TAG, "action for client " + socket.hashCode() + ": disconnected");
					break;
				} else if (Commands.USER_TEXT.equals(messageTmp)) {
					userTextInput = true;
				} else if (userTextInput) {
					userTextTmp = messageTmp;
					userTextInput = false;
				} 
				final String userText = userTextTmp;
				final String message = messageTmp;
				// Send the key command to activities that listen to the service
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						if (message.equals(Commands.VIDEO_PLAY)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_PLAY, null);
						else if (message.equals(Commands.VIDEO_PAUSE)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_PAUSE, null);
						else if (message.equals(Commands.VIDEO_PREVIOUS)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_PREVIOUS, null);
						else if (message.equals(Commands.VIDEO_STOP)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_STOP, null);
						else if (message.equals(Commands.VIDEO_NEXT)) rcs.sendMessageToUI(RemoteControlService.CMD__VIDEO_NEXT, null);
						else if (message.equals(Commands.SELECT)) rcs.sendMessageToUI(RemoteControlService.CMD__SELECT, null);
						else if (message.equals(Commands.MOVE_UP)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_UP, null);
						else if (message.equals(Commands.MOVE_DOWN)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_DOWN, null);
						else if (message.equals(Commands.MOVE_LEFT)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_LEFT, null);
						else if (message.equals(Commands.MOVE_RIGHT)) rcs.sendMessageToUI(RemoteControlService.CMD__MOVE_RIGHT, null);
						else if (message.equals(Commands.BACK)) rcs.sendMessageToUI(RemoteControlService.CMD__BACK, null);
						else if (message.equals(Commands.HOME)) rcs.sendMessageToUI(RemoteControlService.CMD__HOME, null);
						else if (message.equals(Commands.SOUND_MUTE)) rcs.sendMessageToUI(RemoteControlService.CMD__SOUND_MUTE, null);
						else if (message.equals(Commands.SOUND_PLUS)) rcs.sendMessageToUI(RemoteControlService.CMD__SOUND_PLUS, null);
						else if (message.equals(Commands.SOUND_MINUS)) rcs.sendMessageToUI(RemoteControlService.CMD__SOUND_MINUS, null);
						else if (userText != null) rcs.sendMessageToUI(RemoteControlService.CMD__USER_TEXT, userText);
					}
				});
				// update UI
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						if (!"Test".equals(message)) {
							rcs.sendMessageToUI(RemoteControlService.MSG__PRINT_NEW_CLIENT_ACTION, message);
						}
					}
				});
				// send feedback
				sendMessage(message + "__ok");
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}
	
	public void sendMessage(final String msg) {
		try {
			out.println(msg);
			Log.d(TAG, "send to client " + socket.hashCode() + ": " + msg);
		} catch (Exception e) {
			Log.e(TAG, "ERROR:", e);
		}
	}
}
